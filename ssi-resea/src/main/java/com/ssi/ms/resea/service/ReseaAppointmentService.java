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
import com.ssi.ms.resea.database.dao.ClaimClmDAO;
import com.ssi.ms.resea.database.dao.ClmLofClfDao;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.repository.*;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO;
import com.ssi.ms.resea.dto.ReseaSchApptCaseMgrAvailListReqDTO;
import com.ssi.ms.resea.dto.ScheduleInitialApptSaveReqDTO;
import com.ssi.ms.resea.dto.WaitlistClearReqDTO;
import com.ssi.ms.resea.dto.WaitlistReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaAppointmentValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.CREATE_ACTIVITY_EXCEPTION;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CLAIM_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMATTER;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRsicTimeslotUsage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRsicMtgStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRsicTimeslotUsage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.rscsStageToRsicUsage;

@Slf4j
@Service
public class ReseaAppointmentService {
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ReseaAppointmentValidator apptValidator;
    @Autowired
    private ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private ReseaCaseRscsRepository rscsRepository;
    @Autowired
    private ReseaCaseActivityRscaRepository rscaRepo;
    @Autowired
    CommonRepository commonRepository;
    @Autowired
    ClaimClmRepository clmRepository;
    @Autowired
    ReseaRatRepository ratRepository;
    @Autowired
    ClmLofClfRepository clfRepo;

    @Autowired
    EntityManager entityManager;
    public ReseaAppointmentService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }

    @Transactional
    public String saveAvailableAppt(AvaliableApptSaveReqDTO apptReqDTO, String userId) {
        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(apptReqDTO.getEventId())
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + apptReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
        final ReseaCaseRscsDAO rscsDAO = rscsRepository.findByClaimId(apptReqDTO.getClaimId(),
                List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED));
        final ReseaIntvwerCalRsicDAO scheduledRsicDAO = rsicRepository.findByScheduledByClaimId(apptReqDTO.getClaimId(),
                rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId());
        final Long deadlinePar = commonParRepository.findByParShortName(RESEA_DEADLINE_DAYS).getParNumericValue();
        boolean lateSchedule = false;
        boolean clearWaitList = false;
        if (rscsDAO != null) {
            rsicDAO.setRscsDAO(rscsDAO);
            final Date prevStageApptDt = switch ((int) rscsDAO.getRscsStageCdALV().getAlvId().longValue()) {
                case (int) RSCS_STAGE_FIRST_SUBS_APPT -> rscsDAO.getRscsInitApptDt();
                case (int) RSCS_STAGE_SECOND_SUBS_APPT -> rscsDAO.getRscsFirstSubsApptDt();
                default -> rscsDAO.getRscsOrientationDt();
            };

            switch ((int) rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId().longValue()) {
                case (int) RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV -> rscsDAO.setRscsFirstSubsApptDt(rsicDAO.getRsicCalEventDt());
                case (int) RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV -> rscsDAO.setRscsSecondSubsApptDt(rsicDAO.getRsicCalEventDt());
                default -> {
                    rscsDAO.setRscsInitApptDt(rsicDAO.getRsicCalEventDt());
                    rscsDAO.setRscsAssignedTs(DateUtils.truncate(systemDate, Calendar.DATE));
                }
            }
            if (INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd())) {
                rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
                rscsDAO.setRscsOnWaitlistDt(null);
                rscsDAO.setRscsOnWaitlistClearDt(commonParRepository.getCurrentDate());
                rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
                clearWaitList = true;
            }
            rscsDAO.setRscsLastUpdBy(userId);
            rscsDAO.setRscsLastUpdUsing(LAST_UPDT_USING_AVAILABLE);
            rscsDAO.setRscsLastUpdTs(systemDate);
            rscsRepository.save(rscsDAO);

            if(prevStageApptDt.after(localDateToDate.apply(dateToLocalDate.apply(
                    rsicDAO.getRsicCalEventDt()).plusDays(deadlinePar)))) {
                lateSchedule = true;
            }
        } else if (INDICATOR_YES.equals(ratRepository.checkInitialApptBeyond21(apptReqDTO.getClaimId(), rsicDAO.getRsicCalEventDt()))) {
            lateSchedule = true;
        }
        final HashMap<String, List<String>> errorMap = apptValidator.validateSaveAvailableAppointment(apptReqDTO, rsicDAO, lateSchedule, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException(INITIAL_REQ_FAILED, errorMap);
        }
        if (scheduledRsicDAO != null) {
            // Releasing Old Scheduled Appointment Slot
            updateOldRsicDetails(scheduledRsicDAO, commonRepository.getCurrentTimestamp(), userId, "RESEA_AVAILABLE");
        }
        if (rsicDAO.getRsisDAO() != null) {
            if (INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowRemoteInd()) &&
                    !INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowOnsiteInd())) {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
            } else if (INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowOnsiteInd())) {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
            }
        }
        rsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
                dateToLocalDateTime.apply(systemDate).format(DATE_FORMATTER),
                StringUtils.trimToEmpty("Information Conveyed to Claimant by: "+String.join(",", apptReqDTO.getInformedConveyedBy())),
                StringUtils.trimToEmpty(rsicDAO.getRsicTimeslotSysNotes())
        }));
        rsicDAO.setRsicCalEventTypeCdAlv(alvRepository.findById(RSIC_CAL_EVENT_TYPE_IN_USE_ALV).get());
        rsicDAO.setRsicMtgStatusCdAlv(alvRepository.findById(RSIC_MTG_STATUS_SCHEDULED).get());
        rsicDAO.setRsicMtgStatusUpdTs(systemDate);
        rsicDAO.setRsicMtgStatusUpdBy(Long.parseLong(userId));
        rsicDAO.setClaimDAO(clmRepository.findById(apptReqDTO.getClaimId()).get());
        if (lateSchedule) {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_NO);
            rsicDAO.setRsicLateSchNotes(apptReqDTO.getLateStaffNote());
        } else {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_YES);
        }
        if (rscsDAO == null && rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV) {
            rsicDAO.setRsicProcStatusInd("U");
        }
        rsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(rsicDAO.getRsicStaffNotes()),
                userService.getUserName(Long.valueOf(userId)),
                dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(apptReqDTO.getStaffNotes())}));
        rsicDAO.setRsicNoticeStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.TO_BE_SENT.getCode()).get());
        rsicDAO.setRsicScheduledByCdAlv(alvRepository.findById(RSIC_SCHEDULED_BY_STAFF).get());
        rsicDAO.setRsicScheduledByUsr(Long.parseLong(userId));
        rsicDAO.setRsicScheduledOnTs(systemDate);
        rsicDAO.setRsicLastUpdBy(userId);
        rsicDAO.setRsicLastUpdUsing(LAST_UPDT_USING_AVAILABLE);
        rsicDAO.setRsicLastUpdTs(systemDate);
        rsicRepository.save(rsicDAO);

        /*if (rscsDAO != null) {
            switch ((int) rscsDAO.getRscsStageCdALV().getAlvId().longValue()) {
                case (int) RSCS_STAGE_INITIAL_APPT -> {
                    rscsDAO.setRscsInitApptDt(rsicDAO.getRsicCalEventDt());
                    rscsDAO.setRscsAssignedTs(DateUtils.truncate(systemDate, Calendar.DATE));
                }
                case (int) RSCS_STAGE_FIRST_SUBS_APPT -> rscsDAO.setRscsFirstSubsApptDt(rsicDAO.getRsicCalEventDt());
                case (int) RSCS_STAGE_SECOND_SUBS_APPT -> rscsDAO.setRscsSecondSubsApptDt(rsicDAO.getRsicCalEventDt());
            }
            rscsDAO.setRscsLastUpdBy(userId);
            rscsDAO.setRscsLastUpdUsing(LAST_UPDT_USING_AVAILABLE);
            rscsDAO.setRscsLastUpdTs(systemDate);
            rscsRepository.save(rscsDAO);
        }*/

        boolean activity_failed = false, clear_wait_failed = false;
        if (rscsDAO != null) {
            if (clearWaitList) {
                clear_wait_failed = createActivityForWaitList(rsicDAO, rscsDAO,
                        RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        null, false, systemDate, false, LAST_UPDT_USING_AVAILABLE, false);
            }
            activity_failed = createActivityForStaffSch(rsicDAO, RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    apptReqDTO.getStaffNotes(), systemDate, apptReqDTO.isIncludeThisNoteInCNO());
        }
        //return activity_failed || clear_wait_failed ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : APPOINTMENT_SCHEDULE_SUCCESS;
        return activity_failed || clear_wait_failed ? REQUEST_FAILED_MSG : APPOINTMENT_SCHEDULE_SUCCESS;
    }

    @Transactional
    public boolean createActivityForStaffSch(ReseaIntvwerCalRsicDAO rsicDAO, Long rscsStageCdAlv, String staffNotes, Date systemDate, boolean includeThisNoteInCNO) {
        boolean activity_failed = false;
        String rschCalEventStTime = null;
        final String meetingModeDesc = StringUtils.isNotBlank(rsicDAO.getRsicMtgModeInd())
                ? rsicDAO.getRsicMtgModeInd().equalsIgnoreCase(ReseaConstants.MEETING_MODE.IN_PERSON.getCode())
                ? ReseaConstants.MEETING_MODE.IN_PERSON.getDescription()
                : ReseaConstants.MEETING_MODE.VIRTUAL.getDescription()
                : null;

        final ParameterParDao parameterParDao = commonParRepository.findByParShortName(ReseaConstants.RESEA_DEADLINE_DAYS);
        final long parNumericValue = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;
        String synopsisType, caseSynopsis, lastSchRsn = null, rscaDetails;
        if (StringUtils.isNotBlank(rsicDAO.getRsicLateSchNotes())) {
            synopsisType = ReseaConstants.RSCA_SYNOPSIS_TYPE.A.toString();
            caseSynopsis = "Scheduled beyond " + parNumericValue + " days.";
            lastSchRsn = "The new appointment had to be scheduled beyond the " + parNumericValue + "-day timeframe because: " + rsicDAO.getRsicLateSchNotes() + ".\n\n";
        } else {
            synopsisType = ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString();
            caseSynopsis = "--";
        }

        if(null != rsicDAO.getRsicCalEventStTime()) {
            DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
            rschCalEventStTime = time.format(formatter12Hour);
        }

        rscaDetails = StringUtils.join("Staff has scheduled an " + meetingModeDesc + space + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt() +
                " to " + rschCalEventStTime
                + " on " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n");
        if (StringUtils.isNotBlank(lastSchRsn)) {
            rscaDetails = StringUtils.join(rscaDetails + lastSchRsn);
        }

        String rscnNoteCategory = null;
        String rscnNote = null;
        String showInNhuisInd = null;

        if(StringUtils.isNotBlank(staffNotes)) {
            rscnNoteCategory =  String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), rscsStageCdAlv));
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

        Map<String, Object> createActivity;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        //try {
            entityManager.flush();
            createActivity = rscaRepo.createCaseActivity(
                    rsicDAO.getRscsDAO().getRscsId(),
                    getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
                    //getRscaStageFromRscsStage(rscsStageCdAlv),
                    //getRscaStatusFromRscsStatus(rscsStatusCdAlv),
                    ReseaAlvEnumConstant.RscaTypeCd.STAFF_SCHEDULED.getCode(),
                    systemDate,
                    "Staff has scheduled " + meetingModeDesc + " " + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt(),
                    synopsisType,
                    caseSynopsis,
                    0L, 0L, stringToDate.apply(DATE_01_01_2000),
                    StringUtils.EMPTY, StringUtils.EMPTY,
                    rscaDetails,
                    ReseaAlvEnumConstant.RscaReferenceIfkCd.RSIC.getCode(),
                    rsicDAO.getRsicId(),
                    rscnNoteCategory,
                    rscnNote,
                    showInNhuisInd,
                    ReseaConstants.LAST_UPDT_USING_AVAILABLE
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

            } else {
                activity_failed = true;
            }
        /*} catch (Exception e) {
            activity_failed = true;
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Scheduling Activity type (5577) associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" +Arrays.toString(e.getStackTrace()));
        }*/
        return activity_failed;
    }



    public List<CaseManagerAvailabilityResDTO> getCaseMgrsAvailListForSchAppt(ReseaSchApptCaseMgrAvailListReqDTO reseaSchApptCaseMgrAvailListReqDTO) {
        Long clmLofId = null;
        List<CaseManagerAvailabilityResDTO> caseManagerList = null;
        final HashMap<String, List<String>> errorMap = apptValidator.validateGetCaseMgrsAvailListForSchAppt(reseaSchApptCaseMgrAvailListReqDTO);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException(ErrorMessageConstant.GET_CASE_MGR_AVAILABILITY_FOR_SCH_INITIAL_APPT_FAILED, errorMap);
        } else {
            final ClaimClmDAO claimClmDAO = clmRepository.findById(reseaSchApptCaseMgrAvailListReqDTO.getClmId())
                    .orElseThrow(() -> new NotFoundException("Invalid Clm ID:" + reseaSchApptCaseMgrAvailListReqDTO.getClmId(), CLAIM_ID_NOT_FOUND));

            Date bssStartDate = rscsRepository.getBssStartDtForInitialAppt(reseaSchApptCaseMgrAvailListReqDTO.getClmId());

            List<ClmLofClfDao> clmLofClfDaoList = null;
            clmLofClfDaoList = clfRepo.getLofByClmId(reseaSchApptCaseMgrAvailListReqDTO.getClmId(),
                    ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            if (clmLofClfDaoList != null && !CollectionUtils.isEmpty(clmLofClfDaoList)) {
                clmLofId = clmLofClfDaoList.get(0).getLocalOfficeLofDAO().getLofId();
            }
            String allowOnsiteIndVal = ReseaConstants.INDICATOR.N.toString();
            String allowRemoteIndVal = ReseaConstants.INDICATOR.N.toString();
            if (reseaSchApptCaseMgrAvailListReqDTO.getMeetingModeInperson().equals(ReseaConstants.MEETING_MODE.IN_PERSON.getCode())) {
                allowOnsiteIndVal = ReseaConstants.INDICATOR.Y.toString();
            }
            if (reseaSchApptCaseMgrAvailListReqDTO.getMeetingModeVirtual().equals(ReseaConstants.MEETING_MODE.VIRTUAL.getCode())) {
                allowRemoteIndVal = ReseaConstants.INDICATOR.Y.toString();
            }
            if (null != bssStartDate) {
                // reseaSchApptCaseMgrAvailListReqDTO.getClmLofInd() = 'Y' -> For Claimant Local office
                // reseaSchApptCaseMgrAvailListReqDTO.getClmLofInd() = 'N' -> All other local office
                if (INDICATOR_YES.equals(reseaSchApptCaseMgrAvailListReqDTO.getClmLofInd())) {
                    // Fetch Case Mgr availability for claimant lof and beyond Resea Deadline date
                    if (INDICATOR_YES.equals(reseaSchApptCaseMgrAvailListReqDTO.getShowBeyondReseaDeadlineInd())) {
                        caseManagerList = rscsRepository.getCaseMgrsAvailListForClmLofBeyondReseaDeadLine(RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV, clmLofId,
                                RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
                                List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                                List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                                claimClmDAO.getClmBenYrEndDt(),
                                allowOnsiteIndVal,
                                allowRemoteIndVal,
                                bssStartDate);
                    } else {
                        caseManagerList = rscsRepository.getCaseMgrsAvailListForClmLof(RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV, clmLofId,
                                RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
                                List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                                List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                                claimClmDAO.getClmBenYrEndDt(),
                                allowOnsiteIndVal,
                                allowRemoteIndVal,
                                bssStartDate);
                    }
                } else {
                    // Fetch Case Mgr availability for all other lof and beyond Resea Deadline date
                    if (INDICATOR_YES.equals(reseaSchApptCaseMgrAvailListReqDTO.getShowBeyondReseaDeadlineInd())) {
                        caseManagerList = rscsRepository.getCaseMgrsAvailListForAllOtherLofBeyondReseaDeadLine(RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV,
                                RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
                                List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                                List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                                claimClmDAO.getClmBenYrEndDt(),
                                bssStartDate);
                    } else {
                        caseManagerList = rscsRepository.getCaseMgrsAvailListForAllOtherLof(RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV,
                                RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
                                List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                                List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                                claimClmDAO.getClmBenYrEndDt(),
                                bssStartDate);
                    }
                }
            }
        }
        return caseManagerList;
    }

    @Transactional
    public String saveScheduleInitialAppt(ScheduleInitialApptSaveReqDTO schInitialApptSaveReqDTO, String userId) {
        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(schInitialApptSaveReqDTO.getEventId())
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + schInitialApptSaveReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
        final Long deadlinePar = commonParRepository.findByParShortName(RESEA_DEADLINE_DAYS).getParNumericValue();

        boolean lateSchedule = false;
        if (INDICATOR_YES.equals(ratRepository.checkInitialApptBeyond21(schInitialApptSaveReqDTO.getClaimId(), rsicDAO.getRsicCalEventDt()))) {
            lateSchedule = true;
        }

        final HashMap<String, List<String>> errorMap = apptValidator.validateSaveSchInitialAppointment(schInitialApptSaveReqDTO, rsicDAO, lateSchedule, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException(ErrorMessageConstant.SAVE_SCH_INITIAL_APPT_REQ_FAILED, errorMap);
        }

        if (rsicDAO.getRsisDAO() != null) {
            if (INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowRemoteInd()) &&
                    !INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowOnsiteInd())) {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
            } else if (INDICATOR_YES.equals(rsicDAO.getRsisDAO().getRsisAllowOnsiteInd())) {
                rsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
            }
        }

        rsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
                dateToLocalDateTime.apply(systemDate).format(DATE_FORMATTER),
                StringUtils.trimToEmpty("Staff scheduled the initial appointment."),
                StringUtils.trimToEmpty(rsicDAO.getRsicTimeslotSysNotes())
        }));

        rsicDAO.setRsicCalEventTypeCdAlv(alvRepository.findById(RSIC_CAL_EVENT_TYPE_IN_USE_ALV).get());
        rsicDAO.setRsicMtgStatusCdAlv(alvRepository.findById(RSIC_MTG_STATUS_SCHEDULED).get());
        rsicDAO.setRsicMtgStatusUpdTs(systemDate);
        rsicDAO.setRsicMtgStatusUpdBy(Long.parseLong(userId));
        rsicDAO.setClaimDAO(clmRepository.findById(schInitialApptSaveReqDTO.getClaimId()).get());

        if (lateSchedule) {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_NO);
            //rsicDAO.setRsicLateSchNotes(apptReqDTO.getLateStaffNote());
        } else {
            rsicDAO.setRsicInSchWindowInd(INDICATOR_YES);
        }

        if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV) {
            rsicDAO.setRsicProcStatusInd("U");
        }

        rsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(rsicDAO.getRsicStaffNotes()),
                userService.getUserName(Long.valueOf(userId)),
                dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(schInitialApptSaveReqDTO.getStaffNotes())}));

        rsicDAO.setRsicNoticeStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.TO_BE_SENT.getCode()).get());
        rsicDAO.setRsicScheduledByCdAlv(alvRepository.findById(RSIC_SCHEDULED_BY_STAFF).get());
        rsicDAO.setRsicScheduledByUsr(Long.parseLong(userId));
        rsicDAO.setRsicScheduledOnTs(systemDate);
        rsicDAO.setRsicLastUpdBy(userId);
        rsicDAO.setRsicLastUpdUsing(LAST_UPDT_USING_SCH_INITIAL_APPT);
        rsicDAO.setRsicLastUpdTs(systemDate);
        rsicRepository.save(rsicDAO);

        return SCH_INITIAL_APPT_SUCCESS;
    }

    @Transactional
    public String applyWaitlist(WaitlistReqDTO waitlistReqDTO, String staffUserId, String roleId) {
        ReseaIntvwerCalRsicDAO rsicDAO = null;
        ReseaCaseRscsDAO rscsDAO = null;
        if (waitlistReqDTO.getEventId() != null) {
            rsicDAO = rsicRepository.findById(waitlistReqDTO.getEventId())
                    .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + waitlistReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
            rscsDAO = rsicDAO.getRscsDAO();
        } else if (waitlistReqDTO.getCaseId() != null){
            rscsDAO = rscsRepository.findById(waitlistReqDTO.getCaseId())
                    .orElseThrow(() -> new NotFoundException("Invalid Case ID:" + waitlistReqDTO.getCaseId(), CASE_ID_NOT_FOUND));
            final Long rsicUsageCd = rscsStageToRsicUsage(rscsDAO.getRscsStageCdALV().getAlvId());
            if (rsicUsageCd != null && rsicUsageCd > 0L) {
                rsicDAO = rsicRepository.findByScheduledByClaimId(rscsDAO.getClmDAO().getClmId(), rsicUsageCd);
            }
        }
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = apptValidator.validateApplyWaitlist(
                waitlistReqDTO, rsicDAO, rscsDAO, staffUserId, roleId, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Apply Wait-list Request Failed", errorMap);
        }
        if (rscsDAO != null) {
            rscsDAO.setRscsStatusCdALV(alvRepository.findById(RSCS_STATUS_WAITLIST_REQ).get());
            rscsDAO.setRscsOnWaitlistInd(INDICATOR_YES);
            rscsDAO.setRscsOnWaitlistAutoSchInd(waitlistReqDTO.getWaitlist());
            rscsDAO.setRscsOnWaitlistDt(systemDate);
            rscsDAO.setRscsOnWaitlistClearDt(null);
            rscsDAO.setRscsLastUpdBy(staffUserId);
            rscsDAO.setRscsLastUpdUsing("WAIT_API");
            rscsRepository.save(rscsDAO);
        }
        boolean activity_failed = false;
        if (rscsDAO != null && rsicDAO != null) {
            activity_failed = createActivityForWaitList(rsicDAO, rscsDAO,
                    RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    waitlistReqDTO.getNotes(), true, commonParRepository.getCurrentTimestamp(), false, "WAIT_API",
                    waitlistReqDTO.isIncludeThisNoteInCNO());
        }
        return activity_failed
                ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription()
                : "Case Appointment wait-listed successfully";
    }

    @Transactional
    public String clearWaitlist(WaitlistClearReqDTO waitlistReqDTO, String staffUserId, String roleId) {
        ReseaIntvwerCalRsicDAO rsicDAO = null;
        ReseaCaseRscsDAO rscsDAO = null;
        if (waitlistReqDTO.getEventId() != null) {
            rsicDAO = rsicRepository.findById(waitlistReqDTO.getEventId())
                    .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + waitlistReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
            rscsDAO = rsicDAO.getRscsDAO();
        } else if (waitlistReqDTO.getCaseId() != null){
            rscsDAO = rscsRepository.findById(waitlistReqDTO.getCaseId())
                    .orElseThrow(() -> new NotFoundException("Invalid Case ID:" + waitlistReqDTO.getCaseId(), CASE_ID_NOT_FOUND));
            final Long rsicUsageCd = rscsStageToRsicUsage(rscsDAO.getRscsStageCdALV().getAlvId());
            if (rsicUsageCd != null && rsicUsageCd > 0L) {
                rsicDAO = rsicRepository.findByScheduledByClaimId(rscsDAO.getClmDAO().getClmId(), rsicUsageCd);
            }
        }
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = apptValidator.validateClearWaitlist(
                waitlistReqDTO, rsicDAO, rscsDAO, staffUserId, roleId, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Apply Wait-list Request Failed", errorMap);
        }
        if (rscsDAO != null) {
            if (RSCS_STATUS_WAITLIST_REQ.equals(rscsDAO.getRscsStatusCdALV().getAlvId())) {
                rscsDAO.setRscsStatusCdALV(alvRepository.findById(RSCS_STATUS_SCHEDULED).get());
            }
            rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
            rscsDAO.setRscsOnWaitlistDt(null);
            rscsDAO.setRscsOnWaitlistClearDt(systemDate);
            rscsDAO.setRscsLastUpdBy(staffUserId);
            rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
            rscsDAO.setRscsLastUpdUsing("WAIT_CLR_API");
            rscsRepository.save(rscsDAO);
        }
        boolean activity_failed = false;
        if (rscsDAO != null && rsicDAO != null) {
            activity_failed = createActivityForWaitList(rsicDAO, rscsDAO,
                    RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    waitlistReqDTO.getNotes(), false, commonParRepository.getCurrentTimestamp(),
                    false, "WAIT_CLR_API", waitlistReqDTO.isIncludeThisNoteInCNO());
        }
        return activity_failed
                ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription()
                : "Case Appointment wait-listed cleared successfully";
    }

    @Transactional
    public boolean createActivityForWaitList(ReseaIntvwerCalRsicDAO rsicDAO, ReseaCaseRscsDAO rscsDAO,
                                             Long rscsStageCdAlv, String staffNotes,
                                             boolean applyWaitlist, Date systemTimestamp,
                                             boolean preserveStageAndStatus,
                                             String prog,
                                             boolean includeThisNoteInCNO) {
        //applyWaitlist is set to false when clearing claimant from waitlist
        boolean activity_failed = false;
        String synopsisType, caseSynopsis, rscaDetails;
        synopsisType = ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString();
        caseSynopsis = "--";
        rscaDetails = applyWaitlist
                ? StringUtils.join("The claimant has been placed on the waitlist for the next available "
                    + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt() + " at "
                    + rsicDAO.getRsisDAO().getLofDAO().getLofName() + " with " + rsicDAO.getRsisDAO().getStfDAO().getStaffName() + ".\n\n")
                : StringUtils.join("The claimant has been cleared from the waitlist for the next available "
                    + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt() + " at "
                    + rsicDAO.getRsisDAO().getLofDAO().getLofName() + " with " + rsicDAO.getRsisDAO().getStfDAO().getStaffName() + ".\n\n");

        String rscnNoteCategory;
        String rscnNote;
        String showInNhuisInd;

        if(StringUtils.isNotBlank(staffNotes)) {
            rscnNoteCategory =  String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), rscsStageCdAlv));
            rscnNote = staffNotes;
            showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
        } else {
            rscnNoteCategory = StringUtils.EMPTY;
            rscnNote = StringUtils.EMPTY;
            showInNhuisInd = StringUtils.EMPTY;
        }

        if(includeThisNoteInCNO && StringUtils.isNotBlank(staffNotes)) {
            showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
        }

        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity;
        //try {
            entityManager.flush();
            createActivity = rscaRepo.createCaseActivity(
                    rscsDAO.getRscsId(),
                    preserveStageAndStatus ? getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())
                            : getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    applyWaitlist ? ReseaAlvEnumConstant.RscaStatusCd.WAITLIST_REQ.getCode()
                            : preserveStageAndStatus
                                ? getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())
                                : getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
                    applyWaitlist
                            ? ReseaAlvEnumConstant.RscaTypeCd.PLACED_ON_WAITLIST.getCode()
                            : ReseaAlvEnumConstant.RscaTypeCd.WAITLIST_CLEARED.getCode(),
                    systemTimestamp,
                    applyWaitlist
                            ? "Claimant has been placed on the waitlist"
                            : "Claimant has been cleared from the waitlist",
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
                    prog
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
            } else {
                activity_failed = true;
            }
        /*} catch (Exception e) {
            activity_failed = true;
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Scheduling Activity type ("
                    + (applyWaitlist ? ReseaAlvEnumConstant.RscaTypeCd.PLACED_ON_WAITLIST.getCode()
                                    : ReseaAlvEnumConstant.RscaTypeCd.WAITLIST_CLEARED.getCode())
                    + ") associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" +Arrays.toString(e.getStackTrace()));
        }*/
        return activity_failed;
    }

    public void updateOldRsicDetails(ReseaIntvwerCalRsicDAO oldRsicDAO, Timestamp timestamp, String userId, String updatedUsing) {
        AllowValAlvDAO rsicCalEventTypeCdForAvailable = alvRepository.findById(ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode()).orElseThrow();
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
        oldRsicDAO.setRsicLastUpdUsing(updatedUsing);
        rsicRepository.save(oldRsicDAO);
    }
}
