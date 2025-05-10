package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwSchRsisDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.StaffStfDAO;
import com.ssi.ms.resea.database.dao.StaffUnavaiabilityStunDAO;
import com.ssi.ms.resea.database.dao.StaffUnavailabilityReqSunrDAO;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseActivityRscaRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.StaffStfRepository;
import com.ssi.ms.resea.database.repository.StaffUnavailabilityReqSunrRepository;
import com.ssi.ms.resea.database.repository.StaffUnavailabilityStunRepository;
import com.ssi.ms.resea.dto.ReseaCaseReassignReqDTO;
import com.ssi.ms.resea.dto.ReseaReassignAllReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaReassignValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CaseReassign.APPLY_UNAVAILABLITY_FAILED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CaseReassign.APPLY_UNAVAILABLITY_FAILED_NHL;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.CREATE_ACTIVITY_EXCEPTION;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.util.ReseaUtilFunction.*;
import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Service
public class ReseaCaseReassignService {
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    private ReseaCaseRscsRepository rscsRepository;
    @Autowired
    private ReseaReassignValidator reassignValidator;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private StaffUnavailabilityStunRepository stunRepository;
    @Autowired
    private StaffStfRepository stfRepository;
    @Autowired
    private ReseaCaseActivityRscaRepository rscaRepo;
    @Autowired
    private ReseaAppointmentService apptService;
    @Autowired
    private ReseaRescheduleRequestService rescheduleRequestService;
    @Autowired
    private StaffUnavailabilityReqSunrRepository sunrRepository;
    @Autowired
    private LocalOfficeLofRepository lofRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    @Transactional
    public String submitCaseReassignment(ReseaCaseReassignReqDTO reassignDTO, String userId, String roleId) {
        boolean activity_failed_reassign, activity_failed_schedule = false, clear_wait_failed = false;
        final ReseaCaseRscsDAO rscsDAO = rscsRepository.findById(reassignDTO.getCaseId())
                .orElseThrow(() -> new NotFoundException("Invalid Case ID:" + reassignDTO.getCaseId(), CASE_ID_NOT_FOUND));
        final ReseaIntvwerCalRsicDAO newRsicDAO = rsicRepository.findById(reassignDTO.getEventId())
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + reassignDTO.getEventId(), EVENT_ID_NOT_FOUND));
        final HashMap<String, List<String>> errorMap = reassignValidator.validateCaseReassign(rscsDAO, newRsicDAO,roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Case Reassignment Validation Failed.", errorMap);
        }
        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final StaffStfDAO oldStfDAO = rscsDAO.getStfDAO();

        final ReseaIntvwerCalRsicDAO oldRsicDAO = rsicRepository.findByScheduledTimeByClaimId(
                rscsDAO.getClmDAO().getClmId(), RSCS_STAGE_TO_RSIC_USAGE_MAP.get(rscsDAO.getRscsStageCdALV().getAlvId()));
        boolean apply_stun_failed = false;
        if (oldRsicDAO != null) {
            String oldStaffNotes = oldRsicDAO.getRsicStaffNotes();
            AllowValAlvDAO reassignReasonAlv = alvRepository.findById(reassignDTO.getReassignReasonCd()).orElseThrow();
            if (!RSCS_REASSIGN_REASON_DO_NOT_BLOCK.equalsIgnoreCase(StringUtils.trimToEmpty(reassignReasonAlv.getAlvDecipherCode()))) {
                StaffUnavailabilityReqSunrDAO sunrDAO = insertSunr(reassignDTO, oldStfDAO, oldRsicDAO, userId, systemDate);
                clearCaseReassignmentRsic(oldRsicDAO, ReseaAlvEnumConstant.RsicCalEventTypeCd.DO_NOT_SCHEDULE.getCode(), userId, systemDate);
                StaffUnavaiabilityStunDAO stunDAO = addStaffUnavailability(reassignDTO, oldStfDAO, oldRsicDAO, sunrDAO, userId, systemDate);
                apply_stun_failed = applyUnavailablity(stunDAO, oldRsicDAO.getRsicId());
            } else {
                clearCaseReassignmentRsic(oldRsicDAO, ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode(), userId, systemDate);
            }
            updateCaseReassignmentRsic(reassignDTO, rscsDAO, newRsicDAO, oldStaffNotes, userId, systemDate);
            /*activity_failed_schedule = apptService.createActivityForStaffSch(newRsicDAO, rscsDAO.getRscsStageCdALV().getAlvId(), rscsDAO.getRscsStatusCdALV().getAlvId(),
                    reassignDTO.getStaffNotes(), systemDate);*/
        } else if (RSCS_STATUS_SCHEDULED.equals(rscsDAO.getRscsStatusCdALV().getAlvId())) {
            rscsDAO.setRscsStatusCdALV(alvRepository.findById(RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF).get());
        }
        final boolean clearWaitList = INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd());
        updateCaseReassignmentRscs(reassignDTO, rscsDAO, newRsicDAO, userId, oldRsicDAO != null, systemDate);
        if (clearWaitList) {
            clear_wait_failed = apptService.createActivityForWaitList(newRsicDAO, rscsDAO,
                    RSIC_USAGE_TO_RSCS_STAGE_MAP.get(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    null, false, systemDate, false, UPDATED_USING_CASE_REASSIGN, false);
        }
        activity_failed_reassign = createActivityForReassign(newRsicDAO, rscsDAO,
                reassignDTO.getStaffNotes(), systemDate, reassignDTO.isIncludeThisNoteInCNO());
        return activity_failed_reassign || apply_stun_failed || clear_wait_failed ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription()
                : "Case Reassignment Success";
    }

    @Transactional
    private StaffUnavailabilityReqSunrDAO insertSunr(ReseaCaseReassignReqDTO reassignDTO, StaffStfDAO stfDAO,
                                                     ReseaIntvwerCalRsicDAO oldRsicDAO, String userId, Date systemDate) {
        StaffUnavailabilityReqSunrDAO sunrDAO = new StaffUnavailabilityReqSunrDAO();
        sunrDAO.setSunrStartDt(oldRsicDAO.getRsicCalEventDt());
        sunrDAO.setSunrStartTime(oldRsicDAO.getRsicCalEventStTime());
        sunrDAO.setSunrEndDt(oldRsicDAO.getRsicCalEventDt());
        sunrDAO.setSunrEndTime(oldRsicDAO.getRsicCalEventEndTime());
        sunrDAO.setSunrTypeInd(ReseaConstants.SUNR_TYPE_IND.O.getCode());
        sunrDAO.setSunrStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode())
                .orElseThrow());
        sunrDAO.setStfDAO(stfDAO);
        sunrDAO.setApproverStfDAO(stfRepository.findByUserId(Long.parseLong(userId)));
        sunrDAO.setSunrReasonTypeCdAlv(alvRepository.findById(CASE_REASSIGN_STUN_MAP
                .get(reassignDTO.getReassignReasonCd())).orElseThrow());
        sunrDAO.setSunrMondayInd(INDICATOR_NO);
        sunrDAO.setSunrTuesdayInd(INDICATOR_NO);
        sunrDAO.setSunrWednesdayInd(INDICATOR_NO);
        sunrDAO.setSunrThursdayInd(INDICATOR_NO);
        sunrDAO.setSunrFridayInd(INDICATOR_NO);
        sunrDAO.setSunrNote("SYSTEM GENERATED");
        sunrDAO.setSunrCreatedBy(userId);
        sunrDAO.setSunrCreatedUsing(UPDATED_USING_CASE_REASSIGN);
        sunrDAO.setSunrCreatedTs(systemDate);
        sunrDAO.setSunrLastUpdBy(userId);
        sunrDAO.setSunrLastUpdUsing(UPDATED_USING_CASE_REASSIGN);
        sunrDAO.setSunrLastUpdTs(systemDate);
        return sunrRepository.save(sunrDAO);
    }

    @Transactional
    void updateCaseReassignmentRsic(ReseaCaseReassignReqDTO reassignDTO, ReseaCaseRscsDAO rscsDAO,
                                    ReseaIntvwerCalRsicDAO rsicDAO, String oldStaffNotes,
                                    String userId, Date systemDate) {
        rsicDAO.setClaimDAO(rscsDAO.getClmDAO());
        rsicDAO.setRscsDAO(rscsDAO);
        rsicDAO.setRsicCalEventTypeCdAlv(alvRepository.findById(RSIC_CAL_EVENT_TYPE_IN_USE_ALV).orElseThrow());
        rsicDAO.setRsicMtgStatusCdAlv(alvRepository.findById(RSIC_MTG_STATUS_SCHEDULED).orElseThrow());
        AllowValAlvDAO noticeStatusToBeSent = alvRepository.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.TO_BE_SENT.getCode()).orElseThrow();
        if (INDICATOR_YES.equals(reassignDTO.getCaseOffice())) {
            if (INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowRemoteInd()) &&
                    !INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowOnsiteInd())) {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
            } else {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
            }
        } else {
            rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
        }
        rsicDAO.setRsicMtgStatusUpdTs(systemDate);
        rsicDAO.setRsicMtgStatusUpdBy(Long.parseLong(userId));
        final Date lastApptDate = switch ((int) rscsDAO.getRscsStageCdALV().getAlvId().longValue()) {
            case (int) RSCS_STAGE_FIRST_SUBS_APPT ->  rscsDAO.getRscsInitApptDt();
            case (int) RSCS_STAGE_SECOND_SUBS_APPT ->  rscsDAO.getRscsFirstSubsApptDt();
            default -> rscsDAO.getRscsOrientationDt();
        };
        final long scheduleDays = DAYS.between(dateToLocalDate.apply(lastApptDate), dateToLocalDate.apply(rsicDAO.getRsicCalEventDt()));
        final ParameterParDao reseaDeadlinePar = commonParRepository.findByParShortName(RESEA_DEADLINE_DAYS);
        if (scheduleDays > reseaDeadlinePar.getParNumericValue()) {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_YES);
            rsicDAO.setRsicLateSchNotes("Appointment scheduled beyond 21 days due to case reassignment");
        } else {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_NO);
        }
        rsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(oldStaffNotes),
                userService.getUserName(Long.valueOf(userId)),
                dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(reassignDTO.getStaffNotes())}));
        rsicDAO.setRsicScheduledOnTs(systemDate);
        rsicDAO.setRsicScheduledByUsr(Long.parseLong(userId));
        rsicDAO.setRsicScheduledByCdAlv(alvRepository.findById(RSIC_SCHEDULED_BY_STAFF).orElseThrow());
        rsicDAO.setRsicNoticeStatusCdAlv(noticeStatusToBeSent);
        rsicDAO.setRsicProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());
        rsicDAO.setRsicLastUpdTs(systemDate);
        rsicDAO.setRsicLastUpdBy(userId);
        rsicDAO.setRsicLastUpdUsing(UPDATED_USING_CASE_REASSIGN);
        rsicRepository.save(rsicDAO);
    }
    @Transactional
    void clearCaseReassignmentRsic(ReseaIntvwerCalRsicDAO oldRsicDAO,
                                   Long eventTypeCd,
                                   String userId, Date systemDate) {
        AllowValAlvDAO rsicCalEventTypeCdForAvailable = alvRepository.findById(eventTypeCd).orElseThrow();
        AllowValAlvDAO noticeStatusNotApplicable = alvRepository.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.NA.getCode()).orElseThrow();
        oldRsicDAO.setClaimDAO(null);
        oldRsicDAO.setRscsDAO(null);
        oldRsicDAO.setRsicCalEventTypeCdAlv(rsicCalEventTypeCdForAvailable);
        oldRsicDAO.setRsicInSchWindowInd(ReseaConstants.INDICATOR.N.toString());
        oldRsicDAO.setRsicLateSchNotes(null);
        oldRsicDAO.setRsicScheduledOnTs(null);
        oldRsicDAO.setRsicScheduledByCdAlv(null);
        oldRsicDAO.setRsicScheduledByUsr(null);
        oldRsicDAO.setRsicMtgModeInd(null);
        oldRsicDAO.setRsicModeChgRsnCdAlv(null);
        oldRsicDAO.setRsicModeChgRsnTxt(null);
        oldRsicDAO.setRsicMtgmodeSwchCnt(0);
        oldRsicDAO.setRsicMtgReschCnt(0);
        oldRsicDAO.setRsicMtgStatusCdAlv(null);
        oldRsicDAO.setRsicMtgStatusUpdTs(null);
        oldRsicDAO.setRsicMtgStatusUpdBy(null);
        oldRsicDAO.setRsicStaffNotes(null);
        oldRsicDAO.setRsicTimeslotSysNotes(StringUtils.trimToEmpty(dateToString.apply(oldRsicDAO.getRsicCreatedTs()) +":" + " Create session"));
        oldRsicDAO.setRsicNoticeStatusCdAlv(noticeStatusNotApplicable);
        oldRsicDAO.setRsicProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());
        oldRsicDAO.setRsicLastUpdBy(userId);
        oldRsicDAO.setRsicLastUpdUsing(UPDATED_USING_CASE_REASSIGN);
        oldRsicDAO.setRsicLastUpdTs(systemDate);
        rsicRepository.save(oldRsicDAO);
    }
    @Transactional
    void updateCaseReassignmentRscs(ReseaCaseReassignReqDTO reassignDTO, ReseaCaseRscsDAO rscsDAO,
                                    ReseaIntvwerCalRsicDAO rsicDAO, String userId, boolean scheduled,
                                    Date systemDate) {
        rscsDAO.setStfDAO(rsicDAO.getRsisDAO().getStfDAO());
        Date apptDate = null;
        if (scheduled) {
            rscsDAO.setRscsStatusCdALV(alvRepository.findById(RSCS_STATUS_SCHEDULED).get());
            apptDate = rsicDAO.getRsicCalEventDt();
        }
        
        if (apptDate != null && rscsDAO.getRscsStageCdALV().getAlvId() == RSCS_STAGE_INITIAL_APPT) {
            rscsDAO.setRscsInitApptDt(apptDate);
        } else if (apptDate != null && rscsDAO.getRscsStageCdALV().getAlvId() == RSCS_STAGE_FIRST_SUBS_APPT) {
            rscsDAO.setRscsFirstSubsApptDt(apptDate);
        } else if (apptDate != null && rscsDAO.getRscsStageCdALV().getAlvId() == RSCS_STAGE_SECOND_SUBS_APPT) {
            rscsDAO.setRscsSecondSubsApptDt(apptDate);
        }
        rscsDAO.setRscsReassignReasonCd(alvRepository.findById(reassignDTO.getReassignReasonCd()).orElseThrow());
        rscsDAO.setRscsReassignedBy(Long.parseLong(userId));
        rscsDAO.setRscsReassignedDt(DateUtils.truncate(systemDate, Calendar.DATE));
        if (INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd())) {
            rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
            rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
            rscsDAO.setRscsOnWaitlistDt(null);
            rscsDAO.setRscsOnWaitlistClearDt(commonParRepository.getCurrentDate());
        }
        rscsDAO.setRscsLastUpdUsing(UPDATED_USING_CASE_REASSIGN);
        rscsDAO.setRscsLastUpdBy(userId);
        rscsDAO.setRscsLastUpdTs(systemDate);
        rscsRepository.save(rscsDAO);
    }
    @Transactional
    StaffUnavaiabilityStunDAO addStaffUnavailability(ReseaCaseReassignReqDTO reassignDTO, StaffStfDAO stfDAO,
                                ReseaIntvwerCalRsicDAO oldRsicDAO,
                                StaffUnavailabilityReqSunrDAO sunrDAO,
                                String userId, Date systemDate) {
        StaffUnavaiabilityStunDAO stunDAO = new StaffUnavaiabilityStunDAO();
        stunDAO.setStfDAO(stfDAO);
        stunDAO.setSunrDAO(sunrDAO);
        stunDAO.setStfApproverDAO(stfRepository.findByUserId(Long.parseLong(userId)));
        stunDAO.setStunReasonTypeAlv(alvRepository.findById(CASE_REASSIGN_STUN_MAP
                .get(reassignDTO.getReassignReasonCd())).orElseThrow());
        if (StringUtils.isBlank(reassignDTO.getStaffNotes())) {
            stunDAO.setStunNote("Case Reassigned due to Staff Unavailability. Reason: "
                    + stunDAO.getStunReasonTypeAlv().getAlvShortDecTxt());
        } else {
            stunDAO.setStunNote(reassignDTO.getStaffNotes()
                    .substring(0, Math.min(250, reassignDTO.getStaffNotes().length())));
        }

        stunDAO.setStunStartDt(oldRsicDAO.getRsicCalEventDt());
        stunDAO.setStunStartTime(oldRsicDAO.getRsicCalEventStTime());
        stunDAO.setStunEndDt(oldRsicDAO.getRsicCalEventDt());
        stunDAO.setStunEndTime(oldRsicDAO.getRsicCalEventEndTime());
        stunDAO.setStunStatusAlv(alvRepository.findById(STUN_STATUS_APPROVED).orElseThrow());
        stunDAO.setStunCreatedBy(userId);
        stunDAO.setStunCreatedUsing(UPDATED_USING_CASE_REASSIGN);
        stunDAO.setStunCreatedTs(systemDate);
        stunDAO.setStunLastUpdBy(userId);
        stunDAO.setStunLastUpdUsing(UPDATED_USING_CASE_REASSIGN);
        stunDAO.setStunLastUpdTs(systemDate);
        stunRepository.save(stunDAO);
        return stunDAO;
    }
    @Transactional
    private boolean createActivityForReassign(ReseaIntvwerCalRsicDAO newRsicDAO, ReseaCaseRscsDAO rscsDAO,
                                           String staffNotes, Date systemDate, boolean includeThisNoteInCNO) {
        boolean activity_failed = false;
        final ParameterParDao parameterParDao = commonParRepository.findByParShortName(ReseaConstants.RESEA_DEADLINE_DAYS);
        final long parNumericValue = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;
        String synopsisType, caseSynopsis, lastSchRsn = null, rscaDetails;
        if (StringUtils.isNotBlank(newRsicDAO.getRsicLateSchNotes())) {
            synopsisType = ReseaConstants.RSCA_SYNOPSIS_TYPE.A.toString();
            caseSynopsis = "Scheduled beyond " + parNumericValue + " days.";
            lastSchRsn = "The new appointment had to be scheduled beyond the " + parNumericValue + "-day timeframe because: " + newRsicDAO.getRsicLateSchNotes() + ".\n\n";
        } else {
            synopsisType = ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString();
            caseSynopsis = "--";
        }
        ReseaIntvwSchRsisDAO rsisDAO = newRsicDAO.getRsisDAO();
        rscaDetails = "Case has been reassigned to " + rsisDAO.getStfDAO().getStfFirstName() + space + rsisDAO.getStfDAO().getStfLastName()
                + " at the " + rsisDAO.getLofDAO().getLofName() + ".\n\n" +
                "Reason: "+ rscsDAO.getRscsReassignReasonCd().getAlvShortDecTxt() + ".\n\n";
        if (StringUtils.isNotBlank(lastSchRsn)) {
            rscaDetails = rscaDetails + lastSchRsn;
        }

        String rscnNoteCategory = null;
        String rscnNote = null;
        String showInNhuisInd = null;

        if(StringUtils.isNotBlank(staffNotes)) {
            rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), rscsDAO.getRscsStageCdALV().getAlvId()));
            rscnNote = staffNotes;
            showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
        }
        else {
            rscnNoteCategory = StringUtils.EMPTY;
            rscnNote = StringUtils.EMPTY;
            showInNhuisInd = StringUtils.EMPTY;
        }

        if(includeThisNoteInCNO && StringUtils.isNotBlank(staffNotes)) {
            showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
        }


        Map<String, Object> createActivity = null;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        //try {
            entityManager.flush();
            createActivity = rscaRepo.createCaseActivity(
                    rscsDAO.getRscsId(),
                    getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId()),
                    getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId()),
                    ReseaAlvEnumConstant.RscaTypeCd.CASE_REASSIGN.getCode(),
                    systemDate,
                    "Case has been reassigned.",
                    synopsisType,
                    caseSynopsis,
                    0L, 0L, stringToDate.apply(DATE_01_01_2000),
                    StringUtils.EMPTY, StringUtils.EMPTY,
                    rscaDetails,
                    ReseaAlvEnumConstant.RscaReferenceIfkCd.RSCS.getCode(),
                    rscsDAO.getRscsId(),
                    rscnNoteCategory,
                    rscnNote,
                    showInNhuisInd,
                    ReseaConstants.RSCA_CALLING_PROGRAM_REASSIGN
            );
            if(createActivity != null) {
                Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
                Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
                Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
                String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

                if(spPoutSuccess == 0L && null != spPoutRscaId) {
                    activity_failed = false;
                }
                else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                    errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                    errorParams.add(String.valueOf(spPoutNhlId));
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                    if (!errorMap.isEmpty()) {
                        throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                    }
                    activity_failed = true;
                }
                else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                    errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                    errorParams.add(String.valueOf(spPoutNhlId));
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                    if (!errorMap.isEmpty()) {
                        throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                    }
                    activity_failed = true;
                }
                else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                    errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                    errorParams.add(String.valueOf(spPoutNhlId));
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                    if (!errorMap.isEmpty()) {
                        throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                    }
                    activity_failed = true;
                }
            }
        /*} catch (Exception e) {
            activity_failed = true;
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Reassign Activity type (5594) associated with RSCS ID "
                    + rscsDAO.getRscsId() + "\n" +Arrays.toString(e.getStackTrace()));
        }*/
        return activity_failed;
    }

    @Autowired
    public ReseaCaseReassignService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    @Transactional
    private boolean applyUnavailablity(StaffUnavaiabilityStunDAO stunDAO, Long rsicId) {
        boolean failed = true;
        try {
            entityManager.flush();
            Map<String, Object> stunActivity = stunRepository.applyStaffUnavailibility(stunDAO.getStunEndDt(), stunDAO.getStunId(), "CS_REASGN");
            if(stunActivity != null) {
                Long spPoutSuccess = (Long) stunActivity.get("POUT_SUCCESS");
                Long spPoutNhlId = (Long) stunActivity.get("pout_nhl_id");
                String spPoutErrorMsg = (String) stunActivity.get("pout_error_msg");
                if(spPoutSuccess != 0L) {
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
                    errorEnums.add(APPLY_UNAVAILABLITY_FAILED_NHL);
                    errorParams.add(String.valueOf(spPoutNhlId));
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                    if (!errorMap.isEmpty()) {
                        throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                    }
                } else {
                    failed = false;
                }
            } else {
                final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                final HashMap<String, List<String>> errorMap = new HashMap<>();
                errorEnums.add(APPLY_UNAVAILABLITY_FAILED);
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
                throw new CustomValidationException(APPLY_UNAVAILABLITY_FAILED.getDescription(), errorMap);
            }
        }catch (Exception e) {
            log.error("Error in calling the stored procedure Apply_Unavailibility for Reassign Activity type (5594) associated with RSCS ID "
                    + rsicId + "\n" +Arrays.toString(e.getStackTrace()));
            throw e;
        }
        return failed;
    }

    public String submitAllReassignment(ReseaReassignAllReqDTO reassignDTO, String userId, String roleId) {
        Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = reassignValidator.validateReassignAll(reassignDTO, systemDate, roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Case Reassignment Validation Failed.", errorMap);
        }
        final ArrayList<String> executionMsg = new ArrayList<>();
        lofRepository.findLocalOfficesByStaffRole(ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(),
                        reassignDTO.getCaseManagerId(),
                        Long.valueOf(ROL_RESEA_CASE_MANAGER),
                        ReseaAlvEnumConstant.UsrStatusCd.ACTIVE.getCode()).
                forEach(lofDAO -> {
            try {
                Map<String, Object> reassignAllOutMap = null;
                reassignAllOutMap = rscsRepository.reassignAll(
                        stfRepository.findByUserId(reassignDTO.getCaseManagerId()).getStfId(), 
                        lofDAO.getLofId(),
                        INDICATOR_YES.equals(reassignDTO.getLimitOffice()) ? "L" : "A",
                        reassignDTO.getReassignDt(),
                        reassignDTO.getReassignEndDt() == null ? stringToDate.apply(DATE_01_01_2000) : reassignDTO.getReassignEndDt(),
                        reassignDTO.getReassignReasonCd(),
                        stfRepository.findByUserId(Long.parseLong(userId)).getStfId(),
                        reassignDTO.getStaffNotes()
                );
                if (reassignAllOutMap != null) {
                    String poutExecutionResult = (String) reassignAllOutMap.get("POUT_EXECUTION_RESULT");
                    String poutExecutionMsg = (String) reassignAllOutMap.get("POUT_EXECUTION_MSG");
                    Long spPoutNhlId = (Long) reassignAllOutMap.get("POUT_NHL_ID");
                    Long spPoutSuccess = (Long) reassignAllOutMap.get("POUT_SUCCESS");
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    final HashMap<String, List<DynamicErrorDTO>> dynamicErrorMap = new HashMap<>();
                    if(spPoutSuccess == 1L && null != spPoutNhlId) {
                        errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                        errorParams.add(String.valueOf(spPoutNhlId));
                        ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                        if (!dynamicErrorMap.isEmpty()) {
                            throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), dynamicErrorMap);
                        }
                    }
                    else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                        errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                        errorParams.add(String.valueOf(spPoutNhlId));
                        ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                        if (!dynamicErrorMap.isEmpty()) {
                            throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), dynamicErrorMap);
                        }
                    }
                    else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                        errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                        errorParams.add(String.valueOf(spPoutNhlId));
                        ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                        if (!dynamicErrorMap.isEmpty()) {
                            throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), dynamicErrorMap);
                        }
                    } else if (!"F".equals(poutExecutionResult) && !"P".equals(poutExecutionResult) || spPoutSuccess != 0L) {
                        errorEnums.add(ErrorMessageConstant.CaseReassign.REASSIGN_ALL_FAILED);
                        errorParams.add(String.valueOf(spPoutNhlId));
                        ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                        throw new DynamicValidationException(ErrorMessageConstant.CaseReassign.REASSIGN_ALL_FAILED.getDescription(), dynamicErrorMap);
                    }
                    if ("P".equals(poutExecutionResult)) {
                        executionMsg.add(poutExecutionMsg);
                    }
                } else {
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    final HashMap<String, List<DynamicErrorDTO>> dynamicErrorMap = new HashMap<>();
                    errorEnums.add(ErrorMessageConstant.CaseReassign.REASSIGN_ALL_FAILED);
                    ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                    throw new DynamicValidationException(ErrorMessageConstant.CaseReassign.REASSIGN_ALL_FAILED.getDescription(), dynamicErrorMap);
                }
            } catch (DynamicValidationException e) {
                throw e;
            } catch (Exception e) {
                log.error("Error in calling the stored procedure for Reassign All: "
                        + "\n" +Arrays.toString(e.getStackTrace()));
                throw new CustomValidationException(ErrorMessageConstant.CaseReassign.REASSIGN_ALL_FAILED.getDescription(), null);
            }
        });
        return CollectionUtils.isEmpty(executionMsg) ? "Reassign Successful"
                : executionMsg.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
}
