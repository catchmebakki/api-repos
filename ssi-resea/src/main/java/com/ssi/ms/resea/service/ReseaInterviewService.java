package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.*;
import com.ssi.ms.resea.database.mapper.ReseaIssueIdentifiedRsiiMapper;
import com.ssi.ms.resea.database.mapper.ReseaJobReferralRsjrMapper;
import com.ssi.ms.resea.database.repository.*;
import com.ssi.ms.resea.dto.AppointmentReqDTO;
import com.ssi.ms.resea.dto.IssuesDTO;
import com.ssi.ms.resea.dto.JobReferralDTO;
import com.ssi.ms.resea.dto.ReseaInterviewResDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaInterviewValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import java.util.Objects;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.getPrevSundayDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.CREATE_ACTIVITY_EXCEPTION;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMATTER;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRsicTimeslotUsage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRsicTimeslotUsage;

/**
 *
 * 10/28/2024		Anand			AnD249239 	UE-241007-RESEA Rewrite-3
 */
@Slf4j
@Service
public class ReseaInterviewService {
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ReseaInterviewValidator interviewValidator;
    @Autowired
    private ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    private ReseaCaseRscsRepository rscsRepository;
    @Autowired
    private ReseaIntvwDetRsidRepository rsidRepository;
    @Autowired
    private ReseaIssueIdentifiedRsiiRepository rsiiRepository;
    @Autowired
    private ReseaWrkSrchWksReviewRswrRepository rswrRepository;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private NonMonIssuesNmiRepository nmiRepository;
    @Autowired
    private CcApplnCcaRepository ccaRepository;
    @Autowired
    private ReseaJobReferralRsjrRepository rsjrRepository;
    @Autowired
    private ReseaCaseActivityRscaRepository rscaRepository;
    @Autowired
    private ReseaJobReferralRsjrMapper rsjrMapper;
    @Autowired
    private ReseaIssueIdentifiedRsiiMapper rsiiMapper;
    @Autowired
    private ReseaCaseActivityRscaRepository rscaRepo;
    @Autowired
    private ReseaAppointmentService apptService;
    @Autowired
    EntityManager entityManager;
    @Autowired
    CmtNotesCnoRepository cnoRepo;
    public ReseaInterviewService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    private boolean CREATE_ACTIVITY_FAILED = false;
    @Transactional
    public String submitInterviewDetails(AppointmentReqDTO apptReqDTO, String userId, String roleId) {
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(apptReqDTO.getEventId())
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + apptReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
        String response = null;
        CREATE_ACTIVITY_FAILED = false;
        boolean preserveStageAndStatus = rsicDAO.getRsidDAO() != null;
        if (!preserveStageAndStatus) {
            preserveStageAndStatus = rsicDAO.getRscsDAO() != null &&
                    (!RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())
                            .equals(rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId())
                    || RSCS_STAGE_TERMINATED_APPT == rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId());
        }
        final boolean reedit = rsicDAO.getRsidDAO() != null;
        if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV) {
            response = submitInitialAppointment(apptReqDTO, userId, roleId, rsicDAO, reedit, preserveStageAndStatus);
        } else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV) {
            response = submitFirstOneOnOne(apptReqDTO, userId, roleId, rsicDAO, reedit, preserveStageAndStatus);
        } else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV) {
            response = submitSecondOneOnOne(apptReqDTO, userId, roleId, rsicDAO, reedit, preserveStageAndStatus);
        }
        return CREATE_ACTIVITY_FAILED ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : response;
    }
    @Transactional
    private String submitInitialAppointment(AppointmentReqDTO apptReqDTO, String userId,  String roleId,
                                            ReseaIntvwerCalRsicDAO rsicDAO, final boolean reedit,
                                            final boolean preserveStageAndStatus) {
        ReseaIntvwDetRsidDAO rsidDAO = reedit ? rsicDAO.getRsidDAO() : new ReseaIntvwDetRsidDAO();
        setRsidIndicators(rsidDAO, apptReqDTO.getItemsCompletedInJMS());
        setRsidIndicators(rsidDAO, apptReqDTO.getActionTaken());
        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final HashMap<String, List<DynamicErrorDTO>> errorMap = interviewValidator
                .validateInitialAppointment(apptReqDTO, rsicDAO, rsidDAO, userId, roleId, systemDate);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException(INITIAL_REQ_FAILED, errorMap);
        }
        updateRsic(rsicDAO, apptReqDTO.getStaffNotes(), systemDate, userId, "RESEA Initial Appt");
        saveRsidData(apptReqDTO, rsidDAO, rsicDAO, userId, "RESEA Initial Appt", systemDate, reedit);
        saveRsiiData(apptReqDTO, rsicDAO, rsidDAO, userId, "RESEA Initial Appt", systemDate, preserveStageAndStatus);
        saveRsjrData(apptReqDTO, rsicDAO, rsidDAO, systemDate, userId, "RESEA Initial Appt", preserveStageAndStatus);
        clearWaitlist(rsicDAO, rsicDAO.getRscsDAO(), commonParRepository.getCurrentDate(), userId, "RESEA Initial Appt", preserveStageAndStatus);
        if (!preserveStageAndStatus) createCompleteActivity(apptReqDTO, rsicDAO, rsidDAO, systemDate, preserveStageAndStatus, userId);
        return INITIAL_REQ_SAVED;
    }

    @Transactional
    private void clearWaitlist(ReseaIntvwerCalRsicDAO rsicDAO, ReseaCaseRscsDAO rscsDAO,
                               Date systemDate, String staffUserId, String createdUsing,
                                  boolean preserveStageAndStatus) {
        if (rscsDAO != null && INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd())) {
            rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
            rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
            rscsDAO.setRscsOnWaitlistDt(null);
            rscsDAO.setRscsStatusCdALV(alvRepository.findById(RSCS_STATUS_SCHEDULED).get());
            rscsDAO.setRscsOnWaitlistClearDt(systemDate);
            rscsDAO.setRscsLastUpdBy(staffUserId);
            rscsDAO.setRscsLastUpdUsing(createdUsing);
            rscsRepository.save(rscsDAO);
            apptService.createActivityForWaitList(rsicDAO, rscsDAO,
                    RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    null, false, commonParRepository.getCurrentTimestamp(),
                    preserveStageAndStatus, createdUsing, false);
        }
    }

    @Transactional
    private void createCompleteActivity(AppointmentReqDTO apptReqDTO,
                                        ReseaIntvwerCalRsicDAO rsicDAO,
                                        ReseaIntvwDetRsidDAO rsidDAO,
                                        Date systemDate,
                                        final boolean preserveStageAndStatus,
                                        String userId) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity = createActivityForCompleteinInterviewSubmit(apptReqDTO,
                rsicDAO, rsidDAO, systemDate, preserveStageAndStatus, userId);
        if(createActivity != null) {
            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                rscaDAO.setRscaId(spPoutRscaId);
                rsidDAO.setRscaDAO(rscaDAO);
                rsidRepository.save(rsidDAO);
                CREATE_ACTIVITY_FAILED = false;
            }
            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
        } else {
            CREATE_ACTIVITY_FAILED = true;
            /*final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
            final HashMap<String, List<String>> errorMap = new HashMap<>();
            errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED);
            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
            throw new CustomValidationException(ReseaConstants.CREATE_ACTIVITY_FAILED_MSG, errorMap);*/
        }
    }
    @Transactional
    private String submitFirstOneOnOne(AppointmentReqDTO apptReqDTO, String userId, String roleId,
                                       ReseaIntvwerCalRsicDAO rsicDAO, final boolean reedit,
                                       final boolean preserveStageAndStatus) {
        ReseaIntvwDetRsidDAO rsidDAO = reedit ? rsicDAO.getRsidDAO() : new ReseaIntvwDetRsidDAO();
        setRsidIndicators(rsidDAO, apptReqDTO.getItemsCompletedInJMS());
        setRsidIndicators(rsidDAO, apptReqDTO.getActionTaken());

        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final HashMap<String, List<DynamicErrorDTO>> errorMap = interviewValidator
                .validateFirstSubsequentAppt(apptReqDTO, rsicDAO, rsidDAO, userId, roleId, systemDate);
		if (!errorMap.isEmpty()) {
			throw new DynamicValidationException(FIRST_ONE_ON_ONE_REQ_FAILED, errorMap);
		}
		
        updateRsic(rsicDAO, apptReqDTO.getStaffNotes(), systemDate, userId, "RESEA First 1-On-1 Appt");
        saveRsidData(apptReqDTO, rsidDAO, rsicDAO, userId, "RESEA First 1-On-1 Appt", systemDate, reedit);
        saveRsiiData(apptReqDTO, rsicDAO, rsidDAO, userId, "RESEA First 1-On-1 Appt", systemDate, preserveStageAndStatus);
        saveRsjrData(apptReqDTO, rsicDAO, rsidDAO, systemDate, userId, "RESEA First 1-On-1 Appt", preserveStageAndStatus);
        clearWaitlist(rsicDAO, rsicDAO.getRscsDAO(), commonParRepository.getCurrentDate(), userId, "RESEA First 1-On-1 Appt", preserveStageAndStatus);
        if (!preserveStageAndStatus) createCompleteActivity(apptReqDTO, rsicDAO, rsidDAO, systemDate, preserveStageAndStatus, userId);
        return FIRST_ONE_ON_ONE_REQ_SAVED;
    }
    @Transactional
    private String submitSecondOneOnOne(AppointmentReqDTO apptReqDTO, String userId, String roleId,
                                        ReseaIntvwerCalRsicDAO rsicDAO, final boolean reedit,
                                        final boolean preserveStageAndStatus) {
        ReseaIntvwDetRsidDAO rsidDAO = reedit ? rsicDAO.getRsidDAO() : new ReseaIntvwDetRsidDAO();
        setRsidIndicators(rsidDAO, apptReqDTO.getItemsCompletedInJMS());
        setRsidIndicators(rsidDAO, apptReqDTO.getActionTaken());
        final Date systemDate = commonParRepository.getCurrentTimestamp();
        final HashMap<String, List<DynamicErrorDTO>> errorMap = interviewValidator
                .validateSecondSubsequentAppt(apptReqDTO, rsicDAO, rsidDAO,
                        userId, roleId, systemDate);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException(SECOND_ONE_ON_ONE_REQ_FAILED, errorMap);
        }
        rsicDAO.setRsicProcStatusInd("U");
        updateRsic(rsicDAO, apptReqDTO.getStaffNotes(), systemDate, userId, "RESEA Second 1-On-1 Appt");
        saveRsidData(apptReqDTO, rsidDAO, rsicDAO, userId, "RESEA Second 1-On-1 Appt", systemDate, reedit);
        saveRsiiData(apptReqDTO, rsicDAO, rsidDAO, userId, "RESEA Second 1-On-1 Appt", systemDate, preserveStageAndStatus);
        saveRsjrData(apptReqDTO, rsicDAO, rsidDAO, systemDate, userId, "RESEA Second 1-On-1 Appt", preserveStageAndStatus);
        clearWaitlist(rsicDAO, rsicDAO.getRscsDAO(), commonParRepository.getCurrentDate(), userId, "RESEA Second 1-On-1 Appt", preserveStageAndStatus);
        if (!preserveStageAndStatus) createCompleteActivity(apptReqDTO, rsicDAO, rsidDAO, systemDate, preserveStageAndStatus, userId);
        return SECOND_ONE_ON_ONE_REQ_SAVED;
    }
    @Transactional
    private void updateRsic(ReseaIntvwerCalRsicDAO rsicDAO, String staffNotes,
                            Date systemDate, String userId, String updatedUsing) {
        rsicDAO.setRsicMtgStatusCdAlv(alvRepository.findById(RSIC_MTG_STATUS_COMPLETED).orElseThrow());
        rsicDAO.setRsicMtgStatusUpdTs(systemDate);
        rsicDAO.setRsicMtgStatusUpdBy(Long.valueOf(userId));
        rsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
                        StringUtils.trimToEmpty(rsicDAO.getRsicStaffNotes()),
                        userService.getUserName(Long.valueOf(userId)),
                        dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                        StringUtils.trimToEmpty(staffNotes)}));
        rsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
                dateToLocalDateTime.apply(systemDate).format(DATE_FORMATTER),
                StringUtils.trimToEmpty(rsicDAO.getRsicStaffNotes()),
                StringUtils.trimToEmpty(rsicDAO.getRsicTimeslotSysNotes())
        }));
        if(StringUtils.equalsIgnoreCase(INDICATOR.Y.toString(),rsicDAO.getRsicReopenAllowedInd())) {
            rsicDAO.setRsicReopenAllowedInd(INDICATOR.N.toString());
            //rsicDAO.setRsicReopenAllowedTs(null);
            createReopenTurnedOffActivity(rsicDAO, systemDate);
        }
        rsicDAO.setRsicLastUpdBy(userId);
        rsicDAO.setRsicLastUpdUsing(updatedUsing);
        rsicDAO.setRsicLastUpdTs(systemDate);
        rsicRepository.save(rsicDAO);
    }

    @Transactional
    private void createReopenTurnedOffActivity(ReseaIntvwerCalRsicDAO rsicDAO,
                                        Date systemDate) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity = createActivityForReopenTurnedOff(rsicDAO, systemDate);
        if(createActivity != null) {
            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                CREATE_ACTIVITY_FAILED = false;
            }
            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
        } else {
            CREATE_ACTIVITY_FAILED = true;
        }
    }

    @Transactional
    private void saveRsidData(AppointmentReqDTO apptReqDTO, ReseaIntvwDetRsidDAO rsidDAO,
                              ReseaIntvwerCalRsicDAO rsicDAO, String userId, String createdUsing, Date systemDate,
                              final boolean reedit){
        rsidDAO.setRsidEsConfirmInd(apptReqDTO.getEmpServicesConfirmInd());
        rsidDAO.setRsicDAO(rsicDAO);
        int jobReferralCnt = 0;
        if (!CollectionUtils.isEmpty(apptReqDTO.getJMSJobReferral())) {
            jobReferralCnt = jobReferralCnt + apptReqDTO.getJMSJobReferral().size();
        }
        if (!CollectionUtils.isEmpty(apptReqDTO.getOutsideWebReferral())) {
            jobReferralCnt = jobReferralCnt + apptReqDTO.getOutsideWebReferral().size();
        }
        rsidDAO.setRsidJobReferralCnt(jobReferralCnt);
        if (apptReqDTO.getWorkSearchIssues() != null) {
            rsidDAO.setRsidWsIssuesCnt(apptReqDTO.getWorkSearchIssues().size());
        }
        if (!CollectionUtils.isEmpty(apptReqDTO.getOtherIssues())) {
            rsidDAO.setRsidOthIssuesCnt(apptReqDTO.getOtherIssues().size());
        }
        if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV) {
            if (INDICATOR.Y.toString().equals(rsidDAO.getRsidMrpAssgndInd())) {
                rsidDAO.setRsidMrpAssgndCdALV(alvRepository.findById(RSID_MRP_CHAPTER_MAP.get(apptReqDTO.getAssignedMrpChap())).orElseThrow());
            }
            if (INDICATOR.Y.toString().equals(rsidDAO.getRsidMrpRvwdInd())) {
                rsidDAO.setRsidMrpRvwdCdALV(alvRepository.findById(RSID_MRP_CHAPTER_1_TO_4).orElseThrow());
            }
        } else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV) {
            if (INDICATOR.Y.toString().equals(rsidDAO.getRsidMrpAssgndInd())) {
                rsidDAO.setRsidMrpAssgndCdALV(alvRepository.findById(RSID_MRP_CHAPTER_5_TO_10).orElseThrow());
            }
            if (INDICATOR.Y.toString().equals(rsidDAO.getRsidMrpRvwdInd())) {
                rsidDAO.setRsidMrpRvwdCdALV(alvRepository.findById(RSID_MRP_CHAPTER_MAP.get(apptReqDTO.getReviewedMrpChap())).orElseThrow());
            }
        } else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV) {
            if (INDICATOR.Y.toString().equals(rsidDAO.getRsidMrpRvwdInd())) {
                rsidDAO.setRsidMrpRvwdCdALV(alvRepository.findById(RSID_MRP_CHAPTER_5_TO_10).orElseThrow());
            }
        }
        rsidDAO.setRsidJmsResumeExpDt(apptReqDTO.getJmsResumeExpDt());
        rsidDAO.setRsidJmsVRecrtExpDt(apptReqDTO.getJmsVRExpDt());
        rsidDAO.setRsidSlfSchByDt(localDateToDate.apply(dateToLocalDate.apply(rsicDAO.getRsicCalEventDt()).plusDays(10)));
        rsidDAO.setRsidStatfNotes(apptReqDTO.getStaffNotes());
        if (!reedit) {
            rsidDAO.setRsidCreatedBy(userId);
            rsidDAO.setRsidCreatedUsing(createdUsing);
            rsidDAO.setRsidCreatedTs(systemDate);
        }
        rsidDAO.setRsidLastUpdBy(userId);
        rsidDAO.setRsidLastUpdUsing(createdUsing);
        rsidDAO.setRsidLastUpdTs(systemDate);
        rsidRepository.save(rsidDAO);
    }
    @Transactional
    private void setRsidIndicators(ReseaIntvwDetRsidDAO rsidDAO, List<String> indicatorList) {
        if (indicatorList != null) {
            for (String indicator : indicatorList) {
                try {
                    Method setIndicator = rsidDAO.getClass().getMethod(RSID_INDICATOR_MAP.get(indicator), String.class);
                    setIndicator.setAccessible(true);
                    setIndicator.invoke(rsidDAO, "Y");
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                }
            }
        }
    }
    @Transactional
    private void saveRsiiData(AppointmentReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
                              ReseaIntvwDetRsidDAO rsidDAO, String userId, String createdUsing, Date systemDate,
                              final boolean preserveStageAndStatus) {
        AllowValAlvDAO issueDuringAlv = switch ((int) rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId().longValue()){
            case (int) RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV -> alvRepository.findById(RSII_ISSUE_ID_DURING_INITIAL_APPT).orElseThrow();
            case (int) RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV -> alvRepository.findById(RSII_ISSUE_ID_DURING_FIRST_SUB_APPT).orElseThrow();
            case (int) RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV -> alvRepository.findById(RSII_ISSUE_ID_DURING_SECOND_SUB_APPT).orElseThrow();
            default -> null;
        };
        if (apptReqDTO.getWorkSearchIssues() != null) {
            AllowValAlvDAO rsiiSourceAlv = alvRepository.findById(RSII_DEC_IFK_RSWR).orElseThrow();
            List<ReseaWrkSrchWksReviewRswrDAO> oldRswrList = rswrRepository.findInterviewWorkSearchIssuesDAO(rsidDAO.getRsidId());
            int count = 0;
            for (String ccaDt : apptReqDTO.getWorkSearchIssues().keySet()) {
                Date ccaDate = stringToDate.apply(ccaDt);
                ReseaWrkSrchWksReviewRswrDAO rswrDAO = CollectionUtils.isEmpty(oldRswrList) ? null :
                    oldRswrList.stream().filter(dao -> dao.getCcaDAO().getCcaWeekEndingDt().compareTo(ccaDate) == 0).findAny().orElse(null);
                if (rswrDAO == null) {
                    rswrDAO = new ReseaWrkSrchWksReviewRswrDAO();
                    rswrDAO.setCcaDAO(ccaRepository.findByClaimIdWeekEndingDt(rsicDAO.getClaimDAO().getClmId(), ccaDate));
                    rswrDAO.setRswrCreatedBy(userId);
                    rswrDAO.setRswrCreatedUsing(createdUsing);
                    rswrDAO.setRswrCreatedTs(systemDate);
                    rswrDAO.setRsidDAO(rsidDAO);
                    rswrDAO.setRswrJrReviewedInd("Y");
                    rswrDAO.setRswrLastUpdBy(userId);
                    rswrDAO.setRswrLastUpdUsing(createdUsing);
                    rswrDAO.setRswrLastUpdTs(systemDate);
                    rswrRepository.save(rswrDAO);

                    final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    Map<String, Object> createActivity = createActivityForWorkSrchinInterviewSubmit(
                            rsicDAO, rswrDAO, systemDate, preserveStageAndStatus);
                    if (createActivity != null) {
                        Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
                        Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
                        Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
                        String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

                        if(spPoutSuccess == 0L && null != spPoutRscaId) {
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                    } else {
                        CREATE_ACTIVITY_FAILED = true;
                        /*final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                        final HashMap<String, List<String>> errorMap = new HashMap<>();
                        errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED);
                        ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
                        throw new CustomValidationException(ReseaConstants.CREATE_ACTIVITY_FAILED_MSG, errorMap);*/
                    }
                }
                Long nmiId = apptReqDTO.getWorkSearchIssues().get(ccaDt);
                // NMI ID Value is 0 for No Issues
                if (nmiId != 0) {
                    if (rswrDAO.getRsiiDAO() == null) {
                        ReseaIssueIdentifiedRsiiDAO rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
                        rsiiDAO.setRsicDAO(rsicDAO);
                        rsiiDAO.setRswrDAO(rswrDAO);
                        rsiiDAO.setNmiDAO(nmiRepository.findById(nmiId).orElseThrow());
                        rsiiDAO.setRsiiIssueEffDt(getPrevSundayDate.apply(ccaDate));
                        rsiiDAO.setRsiiIssueEndDt(ccaDate);
                        rsiiDAO.setRsiiIssueIdDuringCdALV(issueDuringAlv);
                        rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(userId));
                        rsiiDAO.setRsiiSourceIfkCd(rsiiSourceAlv);
                        rsiiDAO.setRsiiSourceIfk(rswrDAO.getRswrId());
                        rsiiDAO.setRsiiDecDetectDtInd(RSII_DEC_DETECT_DT_IND.APPOINTMENT_DT.getCode());
                        rsiiDAO.setRsiiStaffNotes(apptReqDTO.getStaffNotes());
                        rsiiDAO.setRsiiCreatedBy(userId);
                        rsiiDAO.setRsiiCreatedUsing(createdUsing);
                        rsiiDAO.setRsiiCreatedTs(systemDate);
                        rsiiDAO.setRsiiLastUpdBy(userId);
                        rsiiDAO.setRsiiLastUpdUsing(createdUsing);
                        rsiiDAO.setRsiiLastUpdTs(systemDate);
                        rsiiRepository.save(rsiiDAO);
                        String staffNotes, showInNhuisInd;
                        if (count == 0) {
                            staffNotes = apptReqDTO.getStaffNotes();
                            showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
                            count++;
                        } else {
                            staffNotes = "Refer to the notes associated with the completed activity.";
                            showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
                        }
                        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
                        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                        final List<String> errorParams = new ArrayList<>();
                        Map<String, Object> createActivity = createActivityForIssuesAddedinInterviewSubmit(
                                apptReqDTO, rsicDAO, rsiiDAO, staffNotes, showInNhuisInd, systemDate, preserveStageAndStatus);
                        if (createActivity != null) {
                            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
                            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
                            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
                            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

                            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                                ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                                rscaDAO.setRscaId(spPoutRscaId);
                                rsiiDAO.setRscaDAO(rscaDAO);
                                rsiiRepository.save(rsiiDAO);
                                CREATE_ACTIVITY_FAILED = false;
                            }
                            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                                errorParams.add(String.valueOf(spPoutNhlId));
                                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                                if (!errorMap.isEmpty()) {
                                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                                }
                                CREATE_ACTIVITY_FAILED = true;
                            }
                            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                                errorParams.add(String.valueOf(spPoutNhlId));
                                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                                if (!errorMap.isEmpty()) {
                                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                                }
                                CREATE_ACTIVITY_FAILED = true;
                            }
                            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                                errorParams.add(String.valueOf(spPoutNhlId));
                                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                                if (!errorMap.isEmpty()) {
                                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                                }
                                CREATE_ACTIVITY_FAILED = true;
                            }
                        } else {
                            CREATE_ACTIVITY_FAILED = true;
                            /*final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                            final HashMap<String, List<String>> errorMap = new HashMap<>();
                            errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED);
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
                            throw new CustomValidationException(ReseaConstants.CREATE_ACTIVITY_FAILED_MSG, errorMap);*/
                        }
                    } else {
                        count++;
                        if (!Objects.equals(rswrDAO.getRsiiDAO().getNmiDAO().getNmiId(), nmiId)) {
                            final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                            final HashMap<String, List<String>> errorMap = new HashMap<>();
                            errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Rswr_WorkSearch_Issue_Edit);
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
                            throw new CustomValidationException("Invalid Work Search Review", errorMap);
                        }
                    }
                }
            }
        }

        if (apptReqDTO.getOtherIssues() != null) {
            AllowValAlvDAO rsiiSourceAlv = alvRepository.findById(RSII_DEC_IFK_RSID).orElseThrow();
            int count = 0;
            for (IssuesDTO issuesDTO : apptReqDTO.getOtherIssues()) {
                if (issuesDTO.getOtherIssueId() == null || issuesDTO.getOtherIssueId() == 0L) {
                    ReseaIssueIdentifiedRsiiDAO rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
                    rsiiDAO.setRsicDAO(rsicDAO);
                    rsiiDAO.setNmiDAO(nmiRepository.findById(issuesDTO.getIssueId()).orElseThrow());
                    rsiiDAO.setRsiiIssueEffDt(issuesDTO.getStartDt());
                    rsiiDAO.setRsiiIssueEndDt(issuesDTO.getEndDt());
                    rsiiDAO.setRsiiIssueIdDuringCdALV(issueDuringAlv);
                    rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(userId));
                    rsiiDAO.setRsiiSourceIfkCd(rsiiSourceAlv);
                    rsiiDAO.setRsiiSourceIfk(rsidDAO.getRsidId());
                    rsiiDAO.setRsiiDecDetectDtInd(RSII_DEC_DETECT_DT_IND.APPOINTMENT_DT.getCode());
                    rsiiDAO.setRsiiStaffNotes(apptReqDTO.getStaffNotes());
                    rsiiDAO.setRsiiCreatedBy(userId);
                    rsiiDAO.setRsiiCreatedUsing(createdUsing);
                    rsiiDAO.setRsiiCreatedTs(systemDate);
                    rsiiDAO.setRsiiLastUpdBy(userId);
                    rsiiDAO.setRsiiLastUpdUsing(createdUsing);
                    rsiiDAO.setRsiiLastUpdTs(systemDate);
                    rsiiRepository.save(rsiiDAO);

                    String staffNotes, showInNhuisInd;
                    if (count == 0) {
                        staffNotes = apptReqDTO.getStaffNotes();
                        showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
                        count++;
                    } else {
                        staffNotes = "Refer to the notes associated with the completed activity.";
                        showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
                    }
                    final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    Map<String, Object> createActivity = createActivityForIssuesAddedinInterviewSubmit(
                            apptReqDTO, rsicDAO, rsiiDAO, staffNotes, showInNhuisInd, systemDate, preserveStageAndStatus);
                    if (createActivity != null) {
                        Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
                        Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
                        Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
                        String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

                        if(spPoutSuccess == 0L && null != spPoutRscaId) {
                            ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                            rscaDAO.setRscaId(spPoutRscaId);
                            rsiiDAO.setRscaDAO(rscaDAO);
                            rsiiRepository.save(rsiiDAO);
                            CREATE_ACTIVITY_FAILED = false;
                        }
                        else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                    } else {
                        CREATE_ACTIVITY_FAILED = true;
                        /*final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                        final HashMap<String, List<String>> errorMap = new HashMap<>();
                        errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED);
                        ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
                        throw new CustomValidationException(ReseaConstants.CREATE_ACTIVITY_FAILED_MSG, errorMap);*/
                    }
                }
            }
        }
    }
    @Transactional
    private void saveRsjrData(AppointmentReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
                              ReseaIntvwDetRsidDAO rsidDAO, Date systemDate,
                              String userId, String createdUsing, final boolean preserveStageAndStatus) {
        saveJobReferral(apptReqDTO.getJMSJobReferral(), RSJR_SOURCE_JMS_REFERRAL, rsidDAO, rsicDAO, systemDate, userId, createdUsing, preserveStageAndStatus);
        saveJobReferral(apptReqDTO.getOutsideWebReferral(), RSJR_SOURCE_OUTSIDE_WEB_REFERRAL, rsidDAO, rsicDAO, systemDate, userId, createdUsing, preserveStageAndStatus);
    }
    @Transactional
    private void saveJobReferral(List<JobReferralDTO> jobReferralDTOList, Long source, ReseaIntvwDetRsidDAO rsidDAO, ReseaIntvwerCalRsicDAO rsicDAO,
                                 Date systemDate, String userId, String createdUsing, final boolean preserveStageAndStatus) {
        if (jobReferralDTOList != null) {
            AllowValAlvDAO jmsReferralALV = alvRepository.findById(source).orElseThrow();
            List<ReseaJobReferralRsjrDAO> rsjrDAOList = rsjrRepository.getInterviewJobReferralsList(rsidDAO.getRsidId(), source);
            for (JobReferralDTO jobReferralDTO : jobReferralDTOList) {
                ReseaJobReferralRsjrDAO rsjrDAO = !CollectionUtils.isEmpty(rsjrDAOList) && jobReferralDTO.getJobRefId() != null ?
                        rsjrDAOList.stream().filter(dao -> Objects.equals(dao.getRsjrId(), jobReferralDTO.getJobRefId())).findAny().orElse(null) : null;
                if (rsjrDAO == null) {
                    rsjrDAO = new ReseaJobReferralRsjrDAO();
                    rsjrDAO.setRsjrSourceCdALV(jmsReferralALV);
                    saveRsjrDAO(jobReferralDTO, rsicDAO, rsjrDAO, rsidDAO, systemDate, userId, createdUsing, preserveStageAndStatus);
                } else {
                    rsjrDAOList.remove(rsjrDAO);
                    if (!rsjrDAO.getRsjrEmpName().equals(jobReferralDTO.getEmpName()) || !rsjrDAO.getRsjrExactJobTitle().equals(jobReferralDTO.getJobTitle())) {
                        saveRsjrDAO(jobReferralDTO, rsicDAO, rsjrDAO, rsidDAO, systemDate, userId, createdUsing, preserveStageAndStatus);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(rsjrDAOList)) {
                for (ReseaJobReferralRsjrDAO deleteRjsr: rsjrDAOList) {
                    final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
                    final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                    final List<String> errorParams = new ArrayList<>();
                    Map<String, Object> createActivity = createActivityForJobReferalinInterviewSubmit(rsicDAO, deleteRjsr, systemDate,
                            ReseaAlvEnumConstant.RscaTypeCd.DELETED_JOB_REFERRAL.getCode(), preserveStageAndStatus);
                    if(createActivity != null) {
                        Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
                        Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
                        Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
                        String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

                        if(spPoutSuccess == 0L && null != spPoutRscaId) {
                            CREATE_ACTIVITY_FAILED = false;
                        }
                        else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                        else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                            errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                            errorParams.add(String.valueOf(spPoutNhlId));
                            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                            if (!errorMap.isEmpty()) {
                                throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                            }
                            CREATE_ACTIVITY_FAILED = true;
                        }
                    } else {
                        CREATE_ACTIVITY_FAILED = true;
                    }
                    rsjrRepository.delete(deleteRjsr);
                }
            }
        }
    }
    @Transactional
    private void saveRsjrDAO(JobReferralDTO jobReferralDTO,
                             ReseaIntvwerCalRsicDAO rsicDAO,
                             ReseaJobReferralRsjrDAO rsjrDAO,
                             ReseaIntvwDetRsidDAO rsidDAO, Date systemDate,
                             String userId, String createdUsing,
                             final boolean preserveStageAndStatus) {
        boolean create = rsjrDAO.getRsjrId() == null || rsjrDAO.getRsjrId() == 0L;
        Date sysDate = DateUtils.truncate(systemDate, Calendar.DATE);
        rsjrDAO.setRsidDAO(rsidDAO);
        rsjrDAO.setFkEmpId(jobReferralDTO.getEmpNum());
        rsjrDAO.setRsjrEffectiveFromDt(sysDate);
        rsjrDAO.setRsjrEffectiveUntilDt(localDateToDate
                .apply(dateToLocalDate.apply(sysDate).plusDays(7)));
        rsjrDAO.setRsjrEmpName(formatRsjrEmpName(jobReferralDTO.getEmpName()));
        rsjrDAO.setRsjrExactJobTitle(jobReferralDTO.getJobTitle());
        if (create) {
            rsjrDAO.setRsjrCreatedBy(userId);
            rsjrDAO.setRsjrCreatedUsing(createdUsing);
            rsjrDAO.setRsjrCreatedTs(systemDate);
        }
        rsjrDAO.setRsjrLastUpdBy(userId);
        rsjrDAO.setRsjrLastUpdUsing(createdUsing);
        rsjrDAO.setRsjrLastUpdTs(systemDate);
        rsjrRepository.save(rsjrDAO);

        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity = createActivityForJobReferalinInterviewSubmit(rsicDAO, rsjrDAO, systemDate,
                create ? ReseaAlvEnumConstant.RscaTypeCd.CREATED_JOB_REFERRAL.getCode() : ReseaAlvEnumConstant.RscaTypeCd.UPDATED_JOB_REFERRAL.getCode(),
                preserveStageAndStatus);
        if(createActivity != null) {
            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                rscaDAO.setRscaId(spPoutRscaId);
                rsjrDAO.setRscaDAO(rscaDAO);
                rsjrRepository.save(rsjrDAO);
                CREATE_ACTIVITY_FAILED = false;
            }
            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                }
                CREATE_ACTIVITY_FAILED = true;
            }
        } else {
            CREATE_ACTIVITY_FAILED = true;
            /*final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
            final HashMap<String, List<String>> errorMap = new HashMap<>();
            errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED);
            ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
            CREATE_ACTIVITY_FAILED = true;
            throw new CustomValidationException(ReseaConstants.CREATE_ACTIVITY_FAILED_MSG, errorMap);*/
        }
    }
    @Transactional
    private Map<String, Object> createActivityForIssuesAddedinInterviewSubmit(AppointmentReqDTO apptReqDTO,
                                                                              ReseaIntvwerCalRsicDAO rsicDAO, ReseaIssueIdentifiedRsiiDAO rsiiDAO,
                                                                              String staffNotes, String showInNhuisInd, Date systemDate,
                                                                              final boolean preserveStageAndStatus) {
        Map<String, Object> createActivity = null;
        try {
            ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
            if(null != rsiiDAO.getRsiiId()  &&  null != rscsDAO) {
                String rscaDetails;
                String rschCalEventStTime = null;
                String issueDatesDesc;
                NonMonIssuesNmiDAO nmiDAO = rsiiDAO.getNmiDAO();
                if(null != rsiiDAO.getRsiiIssueEndDt()) {
                    issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to " + dateToString.apply(rsiiDAO.getRsiiIssueEndDt());
                }
                else {
                    issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to -";
                }
                AllowValAlvDAO apptTypeAlvDao = rsicDAO.getRsicTimeslotUsageCdAlv();

                if(null != rsicDAO.getRsicCalEventStTime()) {
                    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
                    LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    rschCalEventStTime = time.format(formatter12Hour);
                }
                rscaDetails =  "Staff has created the issue " + nmiDAO.getParentNmiDAO().getNmiShortDescTxt() + " / " + nmiDAO.getNmiShortDescTxt() + " effective " + issueDatesDesc
                        + " while completing for the " + apptTypeAlvDao.getAlvShortDecTxt() + " that was scheduled for " + rschCalEventStTime + " on " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n";

                int count = 0;
                if (!CollectionUtils.isEmpty(apptReqDTO.getOtherIssues())) {
                    if(apptReqDTO.getOtherIssues().size() == 1) {
                        showInNhuisInd =  ReseaConstants.INDICATOR.Y.toString();
                    }
                    else {
                        showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                    }
                }
                else {
                    showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                }

                String rscnNoteCategory = null;
                String rscnNote = null;

                if(StringUtils.isNotBlank(staffNotes)) {
                    rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),
                            rscsDAO.getRscsStageCdALV().getAlvId()));
                    rscnNote = staffNotes;
                    showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                }
                else {
                    rscnNoteCategory = StringUtils.EMPTY;
                    rscnNote = StringUtils.EMPTY;
                    showInNhuisInd = StringUtils.EMPTY;
                }


                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        preserveStageAndStatus ? getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())
                                : getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        preserveStageAndStatus ? getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())
                                : ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode(),
                        ReseaAlvEnumConstant.RscaTypeCd.CREATED_ISSUE.getCode(),
                        systemDate,
                        "Staff has created an issue",
                        ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
                        "--",
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        rscaDetails,
                        ReseaAlvEnumConstant.RscaReferenceIfkCd.RSII.getCode(),
                        rsiiDAO.getRsiiId(),
                        rscnNoteCategory,
                        rscnNote,
                        showInNhuisInd,
                        ReseaConstants.RSCA_CALLING_PROGRAM_COMPLETE
                );
            }
        }
        catch (Exception e) {
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Issues added in Interview page associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    @Transactional
    private Map<String, Object> createActivityForWorkSrchinInterviewSubmit(ReseaIntvwerCalRsicDAO rsicDAO,
                                                                           ReseaWrkSrchWksReviewRswrDAO rswrDAO,
                                                                           Date systemDate,
                                                                           final boolean preserveStageAndStatus) {
        Map<String, Object> createActivity = null;
        try {
            ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
            if(null != rswrDAO.getRswrId()  &&  null != rscsDAO) {
                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        preserveStageAndStatus ? getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())
                                : getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        preserveStageAndStatus ? getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())
                                : ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode(),
                        ReseaAlvEnumConstant.RscaTypeCd.REVIEWED_WORK_SEARCH.getCode(),
                        systemDate,
                        "Staff has reviewed the weekly work search on a continued claim week.",
                        ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
                        "--",
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        "Staff has reviewed the claimant's weekly work search records associated with the continued claim for the week ending "
                                + dateToString.apply(rswrDAO.getCcaDAO().getCcaWeekEndingDt()) + ".",
                        ReseaAlvEnumConstant.RscaReferenceIfkCd.RSWR.getCode(),
                        rswrDAO.getRswrId(),
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        ReseaConstants.RSCA_CALLING_PROGRAM_COMPLETE
                );
            }
        }
        catch (Exception e) {
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Work Search in Interview page associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    @Transactional
    private Map<String, Object> createActivityForJobReferalinInterviewSubmit(ReseaIntvwerCalRsicDAO rsicDAO,
                                                                           ReseaJobReferralRsjrDAO rsjrDAO,
                                                                           Date systemDate, Long rscaTypeCd,
                                                                             final boolean preserveStageAndStatus) {
        Map<String, Object> createActivity = null;
        try {
            ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
            if(null != rsjrDAO.getRsjrId()  &&  null != rscsDAO) {
                String rscaDetails = null;
                String rscaDesc = null;
                String rscaDetailsEffUntil = null;
                boolean delete = Objects.equals(ReseaAlvEnumConstant.RscaTypeCd.DELETED_JOB_REFERRAL.getCode(), rscaTypeCd);
                boolean update = Objects.equals(ReseaAlvEnumConstant.RscaTypeCd.UPDATED_JOB_REFERRAL.getCode(), rscaTypeCd);
                entityManager.flush();

                if(delete) {
                    rscaDetails = rsjrDAO.getRsjrSourceCdALV().getAlvId().equals(ReseaAlvEnumConstant.RsjrSourceCd.JMS_REFERRAL.getCode()) ?
                            "Staff has deleted a Job Referral with a " : "Staff has deleted a Job Referral with an ";
                    rscaDesc = "Deleted a job referral.";
                    rscaDetailsEffUntil = "This job referral was effective until ";
                }
                else if(update) {
                    rscaDetails = rsjrDAO.getRsjrSourceCdALV().getAlvId().equals(ReseaAlvEnumConstant.RsjrSourceCd.JMS_REFERRAL.getCode()) ?
                            "Staff has updated a " : "Staff has updated an ";
                    rscaDesc = "Job Referral has been modified.";
                    rscaDetailsEffUntil = "This job referral was effective until ";
                }
                else {
                    rscaDetails = rsjrDAO.getRsjrSourceCdALV().getAlvId().equals(ReseaAlvEnumConstant.RsjrSourceCd.JMS_REFERRAL.getCode()) ?
                            "Staff has provided the claimant with a " : "Staff has provided the claimant with an ";
                    rscaDesc = "Claimant has been provided a job referral.";
                    rscaDetailsEffUntil = "This job referral is effective until ";
                }

                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        preserveStageAndStatus ? getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())
                                : getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        preserveStageAndStatus ? getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())
                                : ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode(),
                        rscaTypeCd,
                        systemDate,
                        rscaDesc,
                        ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
                        "--",
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        rscaDetails
                                + rsjrDAO.getRsjrSourceCdALV().getAlvShortDecTxt()
                                + " to " + rsjrDAO.getRsjrEmpName() + " for the position of " + rsjrDAO.getRsjrExactJobTitle() + ". "
                                + rscaDetailsEffUntil + dateToString.apply(rsjrDAO.getRsjrEffectiveUntilDt()) + ".\n\n",
                        delete ? ReseaAlvEnumConstant.RscaReferenceIfkCd.RSIC.getCode() : ReseaAlvEnumConstant.RscaReferenceIfkCd.RSJR.getCode(),
                        delete ? rsicDAO.getRsicId() : rsjrDAO.getRsjrId(),
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        ReseaConstants.RSCA_CALLING_PROGRAM_COMPLETE
                );
            }
        }
        catch (Exception e) {
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Job Referrals added in Interview page associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    @Transactional
    private Map<String, Object> createActivityForCompleteinInterviewSubmit(AppointmentReqDTO apptReqDTO,
                                                                           ReseaIntvwerCalRsicDAO rsicDAO,
                                                                           ReseaIntvwDetRsidDAO rsidDAO,
                                                                           Date systemDate,
                                                                           final boolean preserveStageAndStatus,
                                                                           String userId) {
        Map<String, Object> createActivity = null;
        String showInNhuisInd = null;
        String meetingModeDesc = null;
        try {
            ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
            if(null != rscsDAO && null != rsidDAO && rsidDAO.getRsidId() != null) {
                Long totalIssues = 0L;
                if (apptReqDTO.getWorkSearchIssues() != null) {
                    for (String ccaDt : apptReqDTO.getWorkSearchIssues().keySet())
                        totalIssues += apptReqDTO.getWorkSearchIssues().get(ccaDt) != 0L ? 1L : 0L;
                }
                if (apptReqDTO.getOtherIssues() != null) {
                    totalIssues += apptReqDTO.getOtherIssues().size();
                }
                String rschCalEventStTime = null;
                if(null != rsicDAO.getRsicCalEventStTime()) {
                    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
                    LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    rschCalEventStTime = time.format(formatter12Hour);
                }

                if(StringUtils.isNotBlank(rsicDAO.getRsicMtgModeInd())) {
                    meetingModeDesc = rsicDAO.getRsicMtgModeInd().equalsIgnoreCase(ReseaConstants.MEETING_MODE.IN_PERSON.getCode()) ?
                            ReseaConstants.MEETING_MODE.IN_PERSON.getDescription() :ReseaConstants.MEETING_MODE.VIRTUAL.getDescription();
                }
                else {
                    meetingModeDesc = ReseaConstants.MEETING_MODE.IN_PERSON.getDescription();
                }

                String rscaDetails = "Claimant attended the "+ rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt()
                        +" at " + rschCalEventStTime + " on " + dateToString.apply(rsicDAO.getRsicCalEventDt())
                        + ", and the requisite services have been provided.\n\n"
                        + (apptReqDTO.getJMSJobReferral() ==null ? "" :
                        apptReqDTO.getJMSJobReferral().size() + " job referrals were made.\n\n")
                        + (apptReqDTO.getWorkSearchIssues() ==null ? "" :
                        apptReqDTO.getWorkSearchIssues().size() + " Continued claims weeks were reviewed for work search.\n\n")
                        + (totalIssues == 0L ? "" : totalIssues + " issue(s) have been created.");

                String rscnNoteCategory = null;
                String rscnNote = null;

                if(StringUtils.isNotBlank(apptReqDTO.getStaffNotes())) {
                    rscnNoteCategory =  String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),
                            rscsDAO.getRscsStageCdALV().getAlvId()));
                    rscnNote = apptReqDTO.getStaffNotes();
                    showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                }
                else {
                    rscnNoteCategory = StringUtils.EMPTY;
                    rscnNote = StringUtils.EMPTY;
                    showInNhuisInd = StringUtils.EMPTY;
                }

                saveNotes(apptReqDTO, rsicDAO, systemDate, userId, meetingModeDesc, rschCalEventStTime);

                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        preserveStageAndStatus ? getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())
                                : getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        preserveStageAndStatus ? getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())
                                : ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode(),
                        ReseaAlvEnumConstant.RscaTypeCd.COMPLETED.getCode(),
                        systemDate,
                        "Completed " + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt(),
                        ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
                        "--",
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        rscaDetails,
                        ReseaAlvEnumConstant.RscaReferenceIfkCd.RSID.getCode(),
                        rsidDAO.getRsidId(),
                        rscnNoteCategory,
                        rscnNote,
                        showInNhuisInd,
                        ReseaConstants.RSCA_CALLING_PROGRAM_COMPLETE
                );
            }
        }
        catch (Exception e) {
            log.error("Error in calling the stored procedure CREATE_ACTIVITY for Complete in Interview page associated with RSIC ID "
                    + rsicDAO.getRsicId() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    @Transactional
    private void saveNotes(AppointmentReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO, Date systemDate, String userId, String meetingModeDesc, String rschCalEventStTime) {
        CmtNotesCnoDao cnoDao = new CmtNotesCnoDao();
        cnoDao.setCnoEnteredBy(userId);
        cnoDao.setCnoEnteredTs(systemDate);
        cnoDao.setFkCmtId(rsicDAO.getClaimDAO().getClaimantDAO().getCmtId());
        cnoDao.setCnoSubjectTxt("RESEA: "+ rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt() + " Completed");
        String notes = StringUtils.join("The " + meetingModeDesc + " " + rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt() +
                " scheduled for " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + " at " + rschCalEventStTime + " has been marked as completed.\n\n");
        if(apptReqDTO.isIncludeThisNoteInCNO() && StringUtils.isNotBlank(apptReqDTO.getStaffNotes())) {
            notes = notes + apptReqDTO.getStaffNotes();
        }
        cnoDao.setCnoNotesTxt(notes);
        cnoDao.setCnoLastUpdBy(userId);
        cnoDao.setCnoLastUpdTs(systemDate);
        cnoRepo.save(cnoDao);
    }

    @Transactional
    private Map<String, Object>  createActivityForReopenTurnedOff(ReseaIntvwerCalRsicDAO rsicDAO,
                                                                  Date systemDate) {
        Map<String, Object> createActivity = null;
        try {
            ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
            if(null != rscsDAO) {
                String rscaDetails;
                String rschCalEventStTime = null;

                if(null != rsicDAO.getRsicCalEventStTime()) {
                    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
                    LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    rschCalEventStTime = time.format(formatter12Hour);
                }
                rscaDetails = StringUtils.join("The special reopen window to record details of appointment attendance is now closed at "+
                        rschCalEventStTime+ " on " +  dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n");
                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        //getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId()),
                        //ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode(),
                        getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId()),
                        ReseaAlvEnumConstant.RscaTypeCd.REOPEN_TURNED_OFF.getCode(),
                        systemDate,
                        "The special reopen window to record details of appointment attendance is now closed.",
                        ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
                        "--",
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        rscaDetails,
                        ReseaAlvEnumConstant.RscaReferenceIfkCd.RSIC.getCode(),
                        rsicDAO.getRsicId(),
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        StringUtils.EMPTY,
                        ReseaConstants.RSCA_CALLING_PROGRAM_COMPLETE
                );
            }
        }
        catch (Exception e) {
            System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Reopen Turned Off Activity type (5760) associated with " +
                    "RSIC ID "+ rsicDAO.getRsicId());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    public ReseaInterviewResDTO loadInterviewDetails(Long eventId) {
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + eventId, EVENT_ID_NOT_FOUND));
        final ReseaIntvwDetRsidDAO rsidDAO = rsicDAO.getRsidDAO();
        ReseaInterviewResDTO resDTO = null;
        if (rsidDAO != null) {
            final Map<String, Boolean> itemsCompletedInJMS = new HashMap<>() {{
                put("InitialAssessment", INDICATOR_YES.equals(rsidDAO.getRsidJms102Ind()));
                put("Eri1On1", INDICATOR_YES.equals(rsidDAO.getRsidJms106Ind()));
                put("ELMIServices", INDICATOR_YES.equals(rsidDAO.getRsidJms107Ind()));
                put("JobDevelopment", INDICATOR_YES.equals(rsidDAO.getRsidJms123Ind()));
                put("CaseManagement", INDICATOR_YES.equals(rsidDAO.getRsidJms153Ind()));
                put("AttendedRESEA", INDICATOR_YES.equals(rsidDAO.getRsidJms160Ind()));
                put("OutsideWebReferral", INDICATOR_YES.equals(rsidDAO.getRsidJms179Ind()));
                put("DevelopIEP", INDICATOR_YES.equals(rsidDAO.getRsidJms205Ind()));
                put("ReferWIOATraining", INDICATOR_YES.equals(rsidDAO.getRsidJms209Ind()));
                put("JMSJobReferral", INDICATOR_YES.equals(rsidDAO.getRsidJms500Ind()));
                put("AddSelfCaseManager", INDICATOR_YES.equals(rsidDAO.getRsidJmsSelfCmInd()));
                put("JMSCaseNotes", INDICATOR_YES.equals(rsidDAO.getRsidJmsCaseNotesInd()));
                put("JMSRegComplete", INDICATOR_YES.equals(rsidDAO.getRsidJmsRegCompltInd()));
                put("JMSRegIncomplete", INDICATOR_YES.equals(rsidDAO.getRsidJmsRegIncompInd()));
                put("ActiveResume", INDICATOR_YES.equals(rsidDAO.getRsidJmsResumeInd()));
                put("ActiveVirtualRecuiter", INDICATOR_YES.equals(rsidDAO.getRsidJmsVRecruiterInd()));
                put("WagnerPeyserApplComplete", INDICATOR_YES.equals(rsidDAO.getRsidJmsWpApplInd()));
                put("WagnerPeyserApplSignature", INDICATOR_YES.equals(rsidDAO.getRsidJmsWpApplSigInd()));
                put("IEPSignatureCopy", INDICATOR_YES.equals(rsidDAO.getRsidJmsIepSigInd()));
                put("ReferToVRorDHHS", INDICATOR_YES.equals(rsidDAO.getRsidJmsVrDhhsInd()));
                put("CloseGoals", INDICATOR_YES.equals(rsidDAO.getRsidJmsClseGoalsInd()));
                put("JmsCloseIEP", INDICATOR_YES.equals(rsidDAO.getRsidJmsClseIepInd()));
            }};
            final Map<String, Boolean> actionTaken = new HashMap<>() {{
                put("ReviewedReEmpPlan", INDICATOR_YES.equals(rsidDAO.getRsidMrpRvwdInd()));
                put("AssignedReEmpPlan", INDICATOR_YES.equals(rsidDAO.getRsidMrpAssgndInd()));
                put("PhysicallyVerifiedID", INDICATOR_YES.equals(rsidDAO.getRsidIdVerifiedInd()));
                put("RemindedSelfSchedule", INDICATOR_YES.equals(rsidDAO.getRsidSlfSchRmdrInd()));
                put("CheckedPriorJobReferrals", INDICATOR_YES.equals(rsidDAO.getRsidPrevRefrlCcfInd()));
                put("EPandCheckListUpld", INDICATOR_YES.equals(rsidDAO.getRsidJmsEpcklstUplInd()));
            }};

            resDTO = new ReseaInterviewResDTO()
                    .withEventId(rsicDAO.getRsicId())
                    .withJmsItems(itemsCompletedInJMS)
                    .withActionTaken(actionTaken)
                    .withOutsideWebReferral(INDICATOR_YES.equals(rsidDAO.getRsidJms179Ind()) ? rsjrRepository
                            .getInterviewJobReferralsList(rsidDAO.getRsidId(), RSJR_SOURCE_OUTSIDE_WEB_REFERRAL)
                            .stream().map(dao -> rsjrMapper.interviewDaoToDto(dao)).toList() : null)
                    .withJMSJobReferral(INDICATOR_YES.equals(rsidDAO.getRsidJms500Ind()) ? rsjrRepository
                            .getInterviewJobReferralsList(rsidDAO.getRsidId(), RSJR_SOURCE_JMS_REFERRAL)
                            .stream().map(dao -> rsjrMapper.interviewDaoToDto(dao)).toList() : null)
                    .withJmsResumeExpDt(rsidDAO.getRsidJmsResumeExpDt())
                    .withJmsVRExpDt(rsidDAO.getRsidJmsVRecrtExpDt())
                    .withWorkSearchIssues(rswrRepository.findInterviewWorkSearchIssues(rsidDAO.getRsidId()))
                    .withOtherIssues(rsiiRepository.getInterviewIssues(rsicDAO.getRsicId(), RSII_DEC_IFK_RSID)
                            .stream().map(dao -> rsiiMapper.daoToDto(dao)).map(dto -> dto.withSelected(true))
                            .map(dto -> dto.withEditable(false)).toList())
                    .withReviewedMrpChap(rsidDAO.getRsidMrpRvwdCdALV() != null ? RSID_MRP_CHAPTER_MAP_TXT
                            .get(rsidDAO.getRsidMrpRvwdCdALV().getAlvId()) : null)
                    .withAssignedMrpChap(rsidDAO.getRsidMrpAssgndCdALV() != null ? RSID_MRP_CHAPTER_MAP_TXT
                            .get(rsidDAO.getRsidMrpAssgndCdALV().getAlvId()) : null)
                    .withSelfSchByDt(rsidDAO.getRsidSlfSchByDt())
                    .withStaffNotes(rsidDAO.getRsidStatfNotes())
                    .withEmpServicesConfirmInd(INDICATOR_YES);
        }
        return resDTO;
    }

    public String formatRsjrEmpName(String rsjrEmpName) {
        if(StringUtils.isNotBlank(rsjrEmpName)) {
            if (rsjrEmpName.contains("DBA:")) {
                rsjrEmpName = rsjrEmpName.replaceAll("\\s+DBA:", "  DBA:"); // Remove excessive spaces before DBA and Ensure exactly 2 spaces before DBA
            }
            if (rsjrEmpName.contains(" - #")) {
                rsjrEmpName = rsjrEmpName.replaceAll("\\s+- #", " - #");// Remove excessive spaces before - # and enssure exactly 1 space before - #
            }
        }
        return rsjrEmpName;
    }


}
