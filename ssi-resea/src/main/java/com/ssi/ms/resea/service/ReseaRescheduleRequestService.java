package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.*;
import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsReqDTO;
import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsResDTO;
import com.ssi.ms.resea.dto.ReseaRescheduleSaveReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ssi.ms.platform.util.DateUtil.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.util.ReseaUtilFunction.*;


/**
 * {@code ReseaRescheduleRequestService} is a service component in the application responsible for
 * handling business logic related to Reschedule functionality.
 * This service performs operations such as to get list of AVAILABLE slots for reschedule and saving the rescheduling details.
 *
 * @author Anand
 *
 * 10/28/2024		Anand			AnD249239 	UE-241007-RESEA Rewrite-3
 * 11/6/2024		Anand			AnD250842   UE-241025-RESEA Rewrite-4
 * 11/8/2024		Seetha			SS250842    UE-241025-RESEA Rewrite-4
 */
@Service
@Slf4j
public class ReseaRescheduleRequestService extends ReseaBaseService {

    @Autowired
    EntityManager entityManager;
    @Autowired
    ReseaAppointmentService apptService;
    public ReseaRescheduleRequestService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    /**
     * Get the list of available slots for reschedule based on the preferred meeting mode selected and
     * previous timeslot usage.
     *
     * @return List<ReseaRescheduleGetAvailableSlotsResDTO> reseaRescheduleGetAvailableSlotsResDTO
     */
    @Transactional
    public List<ReseaRescheduleGetAvailableSlotsResDTO> getAvailableSlotsForReschedule(ReseaRescheduleGetAvailableSlotsReqDTO
                                                                                               rschGetAvailableSlotsReqDto) {
        List<ReseaRescheduleGetAvailableSlotsResDTO> rschGetAvailableSlotsResDTO = null;
        final HashMap<String, List<String>> errorMap = rschValidator.validateGetAvailableSlotsForReschedule(rschGetAvailableSlotsReqDto);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException(ErrorMessageConstant.GET_AVAILABLE_SLOTS_FOR_RESCHEDULE_FAILED, errorMap);
        } else {
            ParameterParDao parameterParDao;
            long parNumericValue;
            parameterParDao = parRepo.findByParShortName(ReseaConstants.RESEA_DEADLINE_DAYS);
            parNumericValue = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;

            String rsicAllowOnsiteIndVal = ReseaConstants.INDICATOR.N.toString();
            String rsicAllowRemoteIndVal = ReseaConstants.INDICATOR.N.toString();
            if (rschGetAvailableSlotsReqDto.getMeetingModeInperson().equals(ReseaConstants.MEETING_MODE.IN_PERSON.getCode())) {
                rsicAllowOnsiteIndVal = ReseaConstants.INDICATOR.Y.toString();
            }
            if (rschGetAvailableSlotsReqDto.getMeetingModeVirtual().equals(ReseaConstants.MEETING_MODE.VIRTUAL.getCode())) {
                rsicAllowRemoteIndVal = ReseaConstants.INDICATOR.Y.toString();
            }

            final ReseaIntvwerCalRsicDAO oldRsicDAO = rsicRepo.findById(rschGetAvailableSlotsReqDto.getEventId())
                    .orElseThrow(() -> new NotFoundException("Invalid Old RSIC ID:" + rschGetAvailableSlotsReqDto.getEventId(), "rsicOldRsicId.notFound"));
            ReseaCaseRscsDAO rscsDAO = oldRsicDAO.getRscsDAO();
            ReseaIntvwSchRsisDAO rsisDAO = oldRsicDAO.getRsisDAO(); //AnD249239
            LocalDate priorApptCompletedDate;
            if (Objects.equals(oldRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode())) {
                //AnD249239 starts
                /*
                Long claimantLofId = 0L;
                List<ClmLofClfDao> clmLofClfDaoList;
                    clmLofClfDaoList = clfRepo.getLofByClmId(oldRsicDAO.getClaimDAO().getClmId());
                if (clmLofClfDaoList != null && !CollectionUtils.isEmpty(clmLofClfDaoList)) {
                    claimantLofId = clmLofClfDaoList.get(0).getLocalOfficeLofDAO().getLofId();
                }*/
                //AnD249239 ends
                if (null != rscsDAO && rscsDAO.getRscsOrientationDt() != null) {
                    priorApptCompletedDate = dateToLocalDate.apply(rscsDAO.getRscsOrientationDt()).plusDays(parNumericValue);
                    rschGetAvailableSlotsResDTO = rsicRepo.getAvailableSlotsForReschInitialAppt(localDateToDate.apply(priorApptCompletedDate),
                            ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode(),
                            ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode(),
                            rsicAllowOnsiteIndVal, rsicAllowRemoteIndVal, rscsDAO.getStfDAO().getStfId()); //AnD249239 //AnD250842
                }
            } else if (Objects.equals(oldRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode())) {
                priorApptCompletedDate = dateToLocalDate.apply(rscsDAO.getRscsInitApptDt()).plusDays(parNumericValue);
                rschGetAvailableSlotsResDTO = rsicRepo.getAvailableSlotsForReschSubsequentAppt(localDateToDate.apply(priorApptCompletedDate),
                        ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode(),
                        ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode(),
                        rsicAllowOnsiteIndVal, rsicAllowRemoteIndVal, rscsDAO.getStfDAO().getStfId());
            } else {
                priorApptCompletedDate = dateToLocalDate.apply(rscsDAO.getRscsFirstSubsApptDt()).plusDays(parNumericValue);
                rschGetAvailableSlotsResDTO = rsicRepo.getAvailableSlotsForReschSubsequentAppt(localDateToDate.apply(priorApptCompletedDate),
                        ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode(),
                        ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode(),
                        rsicAllowOnsiteIndVal, rsicAllowRemoteIndVal, rscsDAO.getStfDAO().getStfId());
            }
        }
        return rschGetAvailableSlotsResDTO;
    }

    /**
     * Save the rescheduling request to DB.
     * -	Save rescheduling details to RSRS table.
     * -   Save the issue entered in reschedule page to RSII table
     * -   Update the RSIC table for old and new RSIC record ,it based on RSIC meeting status
     * -   Update the RSCS table
     *
     * @param rschSaveReqDTO ReseaRescheduleSaveReqDTO
     * @return String
     */
    @Transactional
    public String saveRescheduleRequest(ReseaRescheduleSaveReqDTO rschSaveReqDTO, String userId) {
        Date systemDate = commonRepo.getCurrentDate();
        Timestamp timestamp = commonRepo.getCurrentTimestamp();

        final ReseaIntvwerCalRsicDAO oldRsicDAO = rsicRepo.findById(rschSaveReqDTO.getOldEventId())
                .orElseThrow(() -> new NotFoundException("Invalid Old RSIC ID:" + rschSaveReqDTO.getOldEventId(), "rsicOldRsicId.notFound"));

        final ReseaIntvwerCalRsicDAO newRsicDAO = rsicRepo.findById(rschSaveReqDTO.getNewEventId())
                .orElseThrow(() -> new NotFoundException("Invalid New RSIC ID:" + rschSaveReqDTO.getNewEventId(), "rsicNewRsicId.notFound"));

        final HashMap<String, List<String>> errorMap = rschValidator.validateSaveRescheduleRequest(rschSaveReqDTO,
                oldRsicDAO.getClaimDAO().getClmBenYrEndDt(), systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException(ErrorMessageConstant.SAVE_RESCHEDULE_VALIDATION_FAILED, errorMap);
        }
        ReseaReschDetRsrsDAO rsrsDao = setReseaReschDetRsrsDAO(rschSaveReqDTO, userId, timestamp);
        Long rsrsId = rsrsRepo.save(rsrsDao).getRsrsId();
        ReseaCaseRscsDAO rscsDAO = oldRsicDAO.getRscsDAO();
        /*if(null !=  oldRsicDAO.getRsicId()) {
            rscsDAO = rsicRepo.getCaseDetailsByRsicId(oldRsicDAO.getRsicId());
        }*/
        Long rscsId = rscsDAO.getRscsId();
        Long rscsStageCdAlv = rscsDAO.getRscsStageCdALV().getAlvId();
        Long rscsStatusCdAlv = rscsDAO.getRscsStatusCdALV().getAlvId();

        List<ReseaIssueIdentifiedRsiiDAO> rsiiDaoList = saveRsiiData(rschSaveReqDTO, newRsicDAO, rsrsId, userId, systemDate, timestamp, rscsId, rscsStageCdAlv, rscsStatusCdAlv);
        updateRsic(oldRsicDAO, newRsicDAO, rschSaveReqDTO, timestamp, userId);
        final boolean clearWaitList = INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd());
        updateRscs(newRsicDAO, userId, rschSaveReqDTO);
        if (clearWaitList) {
            apptService.createActivityForWaitList(newRsicDAO, rscsDAO,
                    RSIC_USAGE_TO_RSCS_STAGE_MAP.get(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                    null, false, timestamp, false, ReseaConstants.RESCHEDULE_BY_USING, false);
        }
        boolean createStfRschActivityFlag  = createActivityForRsch(rschSaveReqDTO, userId, rscsId, rscsStageCdAlv, rscsStatusCdAlv, newRsicDAO, rsrsId, timestamp, systemDate, rsrsDao, oldRsicDAO);
        boolean createIssueActivityFlag = false;
        if(createStfRschActivityFlag) {
            for(ReseaIssueIdentifiedRsiiDAO rsiiDAO : rsiiDaoList) {
                createIssueActivityFlag = createActivityForIssuesAdded(rschSaveReqDTO, newRsicDAO, userId, systemDate, timestamp, rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsiiDAO);
            }
        }
        return createStfRschActivityFlag == true ? ReseaConstants.RESCHEUDLE_REQ_SAVED : ReseaConstants.REQUEST_FAILED_MSG;
    }

    @Transactional
    private boolean createActivityForRsch(ReseaRescheduleSaveReqDTO rschSaveReqDTO, String userId, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv,
                                          ReseaIntvwerCalRsicDAO newRsicDAO, Long rsrsId, Timestamp timestamp, Date systemDate, ReseaReschDetRsrsDAO rsrsDao,
                                          ReseaIntvwerCalRsicDAO oldRsicDAO) {
        boolean createStfRschActivityFlag = false;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();

        Map<String, Object> createActivity = createActivityForStaffRschType(rscsId, rscsStageCdAlv, rscsStatusCdAlv, newRsicDAO, rschSaveReqDTO, rsrsId, timestamp, systemDate, userId, oldRsicDAO);
        if(createActivity != null) {
            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                rscaDAO.setRscaId(spPoutRscaId);
                rsrsDao.setRscaDao(rscaDAO);
                setLogFieldsForCreatedFields(rsrsDao, userId, timestamp);
                setLogFieldsForLastUpdByFields(rsrsDao, userId, timestamp);
                rsrsId = rsrsRepo.save(rsrsDao).getRsrsId();
                createStfRschActivityFlag = true;
            }
            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                }
                createStfRschActivityFlag = false;
            }
            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                }
                createStfRschActivityFlag = false;
            }
            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                }
                createStfRschActivityFlag = false;
            }
        }
        else {
            createStfRschActivityFlag = false;
        }
        return createStfRschActivityFlag;
    }

    @Transactional
    private boolean createActivityForIssuesAdded(ReseaRescheduleSaveReqDTO rschSaveReqDTO, ReseaIntvwerCalRsicDAO newRsicDAO, String userId, Date systemDate,
                                                 Timestamp timestamp, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
        boolean createIssueActivityFlag = false;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity = createActivityForIssuesAddedinRschPage(rscsId, rscsStageCdAlv, rscsStatusCdAlv, newRsicDAO, rschSaveReqDTO, timestamp, systemDate, rsiiDAO);
        if(createActivity != null) {
            Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
            Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
            Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
            String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

            if(spPoutSuccess == 0L && null != spPoutRscaId) {
                ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
                rscaDAO.setRscaId(spPoutRscaId);
                rsiiDAO.setRscaDAO(rscaDAO);
                rsiiDAO.setRsiiLastUpdBy(userId);
                rsiiDAO.setRsiiLastUpdUsing(ReseaConstants.RESCHEDULE_BY_USING);
                rsiiDAO.setRsiiLastUpdTs(timestamp);
                rsiiRepo.save(rsiiDAO);
                createIssueActivityFlag = true;
            }
            else if(spPoutSuccess == 1L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
                }
                createIssueActivityFlag = false;
            }
            else if(spPoutSuccess == 2L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
                }
                createIssueActivityFlag = false;
            }
            else if(spPoutSuccess < 0L && null != spPoutNhlId) {
                errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
                errorParams.add(String.valueOf(spPoutNhlId));
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                if (!errorMap.isEmpty()) {
                    throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
                }
                createIssueActivityFlag = false;
            }
        }
        else {
            createIssueActivityFlag = false;
        }
        return createIssueActivityFlag;
    }

    /**
     * Set the rescheduling details to DAO
     *
     * @return ReseaReschDetRsrsDAO
     */
    private ReseaReschDetRsrsDAO setReseaReschDetRsrsDAO(ReseaRescheduleSaveReqDTO rschSaveReqDTO, String userId, Timestamp timestamp) {
        ReseaReschDetRsrsDAO rsrsDao = rschRequestMapper.dtoToDao(rschSaveReqDTO);
        if (ReseaConstants.INDICATOR.Y.toString().equalsIgnoreCase(rschSaveReqDTO.getNonComplianceInd()) &&
                StringUtils.isNotBlank(rschSaveReqDTO.getLateSchedulingReason())) {
            rsrsDao.setRsrsInSchWindowInd(ReseaConstants.INDICATOR.N.toString());
            rsrsDao.setRsrsLateSchNotes(StringUtils.trimToEmpty(rschSaveReqDTO.getLateSchedulingReason()));
        } else {
            rsrsDao.setRsrsInSchWindowInd(ReseaConstants.INDICATOR.Y.toString());
        }
        rsrsDao.setRsrsIssuesCnt(!CollectionUtils.isEmpty(rschSaveReqDTO.getIssuesDTOList()) ? rschSaveReqDTO.getIssuesDTOList().size() : 0);
        rsrsDao.setRsrsProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());

        List<Long> doctorAppointmentCodes = Arrays.asList(
                ReseaAlvEnumConstant.RsrsReschReasonCd.DOCTORS_APPOINTMENT_DEPENDENT.getCode(),
                ReseaAlvEnumConstant.RsrsReschReasonCd.DOCTORS_APPOINTMENT_SELF.getCode());
        List<Long> jobInterviewCodes = List.of(ReseaAlvEnumConstant.RsrsReschReasonCd.JOB_INTERVIEW.getCode());

        if(null != rschSaveReqDTO.getAppointmentTime() && null != rschSaveReqDTO.getAppointmentDate()) {
            DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
            DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(rschSaveReqDTO.getAppointmentTime(), formatter12Hour);
            String apptTime = time.format(formatter24Hour);
            rsrsDao.setRsrsApptDt(localDateToDate.apply(rschSaveReqDTO.getAppointmentDate()));
            rsrsDao.setRsrsApptTime(apptTime);
        }

        if (doctorAppointmentCodes.contains(rschSaveReqDTO.getReasonForRescheduling())) {
            final AllowValAlvDAO alvDao = alvRepo.findById(ReseaAlvEnumConstant.RsrsReschEntityTypeCd.DOCTOR.getCode())
                    .orElseThrow(() -> new NotFoundException("Invalid ALV ID for RSRS Resch Entity Type:" + ReseaAlvEnumConstant.RsrsReschEntityTypeCd.DOCTOR.getCode(), "alvId.notFound"));
            rsrsDao.setRsrsReasonEntityTypeCdALV(alvDao);
        } else if (jobInterviewCodes.contains(rschSaveReqDTO.getReasonForRescheduling())) {
            final AllowValAlvDAO alvDao = alvRepo.findById(ReseaAlvEnumConstant.RsrsReschEntityTypeCd.EMPLOYER.getCode())
                    .orElseThrow(() -> new NotFoundException("Invalid ALV ID for RSRS Resch Entity Type:" + ReseaAlvEnumConstant.RsrsReschEntityTypeCd.EMPLOYER.getCode(), "alvId.notFound"));
            rsrsDao.setRsrsReasonEntityTypeCdALV(alvDao);
        }
        setLogFieldsForCreatedFields(rsrsDao, userId, timestamp);
        setLogFieldsForLastUpdByFields(rsrsDao, userId, timestamp);
        return rsrsDao;
    }


    /*private ReseaCaseRscsDAO getReseaCaseByRsicId(Long rsicId) {
        ReseaCaseRscsDAO rscsDAO = null;
        if (rsicId != null) {
            rscsDAO = rsicRepo.getCaseDetailsByRsicId(rsicId);
        }
        return rscsDAO;
    }*/

    private ReseaReschDetRsrsDAO setLogFieldsForCreatedFields(ReseaReschDetRsrsDAO rsrsDao, String userId, Timestamp timestamp) {
        rsrsDao.setRsrsCreatedBy(userId);
        rsrsDao.setRsrsCreatedUsing(ReseaConstants.RESCHEDULE_BY_USING);
        rsrsDao.setRsrsCreatedTs(timestamp);
        return rsrsDao;
    }

    private ReseaReschDetRsrsDAO setLogFieldsForLastUpdByFields(ReseaReschDetRsrsDAO rsrsDao, String userId, Timestamp timestamp) {
        rsrsDao.setRsrsLastUpdBy(userId);
        rsrsDao.setRsrsLastUpdUsing(ReseaConstants.RESCHEDULE_BY_USING);
        rsrsDao.setRsrsLastUpdTs(timestamp);
        return rsrsDao;
    }

    /**
     * Save the Issue details (RSII) created from Reschedule page
     *
     */
    @Transactional
    private List<ReseaIssueIdentifiedRsiiDAO> saveRsiiData(ReseaRescheduleSaveReqDTO rschSaveReqDTO, ReseaIntvwerCalRsicDAO newRsicDAO, final Long rsrsId, String userId,
                                                           Date systemDate, Timestamp timestamp, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv) {
        List<ReseaIssueIdentifiedRsiiDAO> rsiiDaoList = new ArrayList<ReseaIssueIdentifiedRsiiDAO>();
        AllowValAlvDAO issueIdDuringCdForReschedule = alvRepo.findById(ReseaAlvEnumConstant.RsiiIssueIdDuringCd.RESCHEDULE.getCode()).orElseThrow();
        AllowValAlvDAO decIfkCdForRSRS = alvRepo.findById(ReseaAlvEnumConstant.RsiiDecIfkCd.RSRS.getCode()).orElseThrow();
        if (!CollectionUtils.isEmpty(rschSaveReqDTO.getIssuesDTOList())) {
            rschSaveReqDTO.getIssuesDTOList().forEach(getIssuesDTOList -> {
                try {
                    ReseaIssueIdentifiedRsiiDAO rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
                    rsiiDAO.setRsicDAO(newRsicDAO);
                    rsiiDAO.setNmiDAO(nmiRepo.findById(getIssuesDTOList.getIssueId()).orElseThrow());
                    rsiiDAO.setRsiiIssueEffDt(getIssuesDTOList.getStartDt());
                    rsiiDAO.setRsiiIssueEndDt(getIssuesDTOList.getEndDt());
                    rsiiDAO.setRsiiIssueIdDuringCdALV(issueIdDuringCdForReschedule);
                    rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(userId));
                    rsiiDAO.setRsiiDecDetectDtInd(RSII_DEC_DETECT_DT_IND.SYSTEM_DT.getCode());
                    rsiiDAO.setRsiiSourceIfkCd(decIfkCdForRSRS);
                    rsiiDAO.setRsiiSourceIfk(rsrsId);
                    rsiiDAO.setRsiiStaffNotes(rschSaveReqDTO.getStaffNotes());
                    rsiiDAO.setRsiiCreatedBy(userId);
                    rsiiDAO.setRsiiCreatedUsing(ReseaConstants.RESCHEDULE_BY_USING);
                    rsiiDAO.setRsiiCreatedTs(timestamp);
                    rsiiDAO.setRsiiLastUpdBy(userId);
                    rsiiDAO.setRsiiLastUpdUsing(ReseaConstants.RESCHEDULE_BY_USING);
                    rsiiDAO.setRsiiLastUpdTs(timestamp);
                    rsiiRepo.save(rsiiDAO);
                    rsiiDaoList.add(rsiiDAO);
                } catch (Exception e) {
                    System.out.println("Error saving Issue in Reschedule page: " + getIssuesDTOList.getIssueId()
                            + " associated with RSIC Id" + newRsicDAO.getRsicId());
                }
            });
        }
        return rsiiDaoList;
    }

    /**
     * Update the RSIC table for old and new RSIC record, it based on RSIC meeting status
     *
     */
    @Transactional
    private void updateRsic(ReseaIntvwerCalRsicDAO oldRsicDAO, ReseaIntvwerCalRsicDAO newRsicDAO, ReseaRescheduleSaveReqDTO rschSaveReqDTO,
                            Timestamp timestamp, String userId) {
        if (null != oldRsicDAO.getRsicMtgStatusCdAlv()) {
            if (ReseaAlvEnumConstant.RsicMeetingStatusCd.SCHEDULED.getCode().longValue() == oldRsicDAO.getRsicMtgStatusCdAlv().getAlvId()) {
                updateNewRsicDetails(oldRsicDAO, newRsicDAO, rschSaveReqDTO, timestamp, userId); //timeslot to which the claimant is being rescheduled
                updateOldRsicDetails(oldRsicDAO, timestamp, userId, ReseaConstants.RESCHEDULE_BY_USING); //timeslot that is being rescheduled
            } else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED.getCode().longValue() == oldRsicDAO.getRsicMtgStatusCdAlv().getAlvId()) {
                updateNewRsicDetails(oldRsicDAO, newRsicDAO, rschSaveReqDTO, timestamp, userId); //timeslot to which the claimant is being rescheduled
            }
        }
    }

    /**
     * Update for NEW RSIC Record
     *
     */
    private void updateNewRsicDetails(ReseaIntvwerCalRsicDAO oldRsicDAO, ReseaIntvwerCalRsicDAO newRsicDAO, ReseaRescheduleSaveReqDTO rschSaveReqDTO,
                                      Timestamp timestamp, String userId) {
        AllowValAlvDAO rsicCalEventTypeCdForInUse = alvRepo.findById(ReseaAlvEnumConstant.RsicCalEventTypeCd.IN_USE.getCode()).orElseThrow();
        AllowValAlvDAO scheduledMeetingStatus = alvRepo.findById(ReseaAlvEnumConstant.RsicMeetingStatusCd.SCHEDULED.getCode()).orElseThrow();
        AllowValAlvDAO scheduledBy = alvRepo.findById(ReseaAlvEnumConstant.RsicScheduledByCd.STAFF.getCode()).orElseThrow();
        AllowValAlvDAO noticeStatusToBeSent = alvRepo.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.TO_BE_SENT.getCode()).orElseThrow();
        Optional.ofNullable(oldRsicDAO.getClaimDAO()).ifPresentOrElse(getClaimDAO -> newRsicDAO.setClaimDAO(oldRsicDAO.getClaimDAO()), () -> newRsicDAO.setClaimDAO(null));
        Optional.ofNullable(oldRsicDAO.getRscsDAO()).ifPresentOrElse(getRscsDAO -> newRsicDAO.setRscsDAO(oldRsicDAO.getRscsDAO()), () -> newRsicDAO.setRscsDAO(null));
        newRsicDAO.setRsicCalEventTypeCdAlv(rsicCalEventTypeCdForInUse);
        if (ReseaConstants.INDICATOR.Y.toString().equalsIgnoreCase(rschSaveReqDTO.getNonComplianceInd()) &&
                StringUtils.isNotBlank(rschSaveReqDTO.getLateSchedulingReason())) {
            newRsicDAO.setRsicInSchWindowInd(ReseaConstants.INDICATOR.N.toString());
            newRsicDAO.setRsicLateSchNotes(rschSaveReqDTO.getLateSchedulingReason());
        } else {
            newRsicDAO.setRsicInSchWindowInd(ReseaConstants.INDICATOR.Y.toString());
            newRsicDAO.setRsicLateSchNotes(StringUtils.EMPTY);
        }
        newRsicDAO.setRsicScheduledOnTs(timestamp);
        newRsicDAO.setRsicScheduledByCdAlv(scheduledBy);
        newRsicDAO.setRsicScheduledByUsr(Long.valueOf(userId));
        //      newRsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
        //SS250842 Starts
        if( ReseaConstants.MEETING_MODE.IN_PERSON.getCode().equals(rschSaveReqDTO.getSelectedPrefMtgModeInPerson())){
            newRsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
        }else if ( ReseaConstants.MEETING_MODE.VIRTUAL.getCode().equals(rschSaveReqDTO.getSelectedPrefMtgModeVirtual())){
            newRsicDAO.setRsicMtgModeInd(ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
        }//SS250842 Ends
        if (ReseaAlvEnumConstant.RsrsReschReasonCd.LOCAL_OFFICE_CONVENIENCE.getCode().longValue()
                != rschSaveReqDTO.getReasonForRescheduling().longValue()) {
            Optional.ofNullable(oldRsicDAO.getRsicMtgReschCnt()).ifPresentOrElse(getRsicMtgReschCnt -> newRsicDAO.setRsicMtgReschCnt(oldRsicDAO.getRsicMtgReschCnt() + 1),
                    () -> newRsicDAO.setRsicMtgReschCnt(1));
        } else {
            newRsicDAO.setRsicMtgReschCnt(oldRsicDAO.getRsicMtgReschCnt());
        }
        newRsicDAO.setRsicMtgStatusCdAlv(scheduledMeetingStatus);
        newRsicDAO.setRsicMtgStatusUpdTs(timestamp);
        newRsicDAO.setRsicMtgStatusUpdBy(Long.valueOf(userId));
        newRsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(oldRsicDAO.getRsicStaffNotes()),
                userService.getUserName(Long.valueOf(userId)),
                dateToLocalDateTime.apply(timestamp).format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(rschSaveReqDTO.getStaffNotes())}));
        String timeSlotSystemMotesForRescheduleAppt = "Rescheduled appointment scheduled on " + oldRsicDAO.getRsicCalEventDt() + " at " + oldRsicDAO.getRsicCalEventStTime()
                + " to " + newRsicDAO.getRsicCalEventDt() + " at " + newRsicDAO.getRsicCalEventStTime() +".";
        newRsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
                dateToLocalDateTime.apply(timestamp).format(DATE_FORMATTER),
                StringUtils.trimToEmpty(timeSlotSystemMotesForRescheduleAppt),
                StringUtils.trimToEmpty(newRsicDAO.getRsicTimeslotSysNotes())
        }));
        newRsicDAO.setRsicNoticeStatusCdAlv(noticeStatusToBeSent);
        newRsicDAO.setRsicProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());
        newRsicDAO.setRsicLastUpdBy(userId);
        newRsicDAO.setRsicLastUpdUsing(ReseaConstants.RESCHEDULE_BY_USING);
        rsicRepo.save(newRsicDAO);
    }

    /**
     * Update for OLD RSIC record
     *
     */
    public void updateOldRsicDetails(ReseaIntvwerCalRsicDAO oldRsicDAO, Timestamp timestamp, String userId, String updatedUsing) {
        AllowValAlvDAO rsicCalEventTypeCdForAvailable = alvRepo.findById(ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode()).orElseThrow();
        AllowValAlvDAO noticeStatusNotApplicable = alvRepo.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.NA.getCode()).orElseThrow();
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
        rsicRepo.save(oldRsicDAO);
    }


    /**
     *  Update for RSCS Record to keep the case up-todate
     */
    @Transactional
    private void updateRscs(ReseaIntvwerCalRsicDAO newRsicDAO, String userId, ReseaRescheduleSaveReqDTO rschSaveReqDTO) {
        if(null != newRsicDAO.getRscsDAO()) {
            ReseaCaseRscsDAO rscsDAO = newRsicDAO.getRscsDAO();
            if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode().equals(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())) {
                rscsDAO.setRscsInitApptDt(newRsicDAO.getRsicCalEventDt());
                Optional.ofNullable(rscsDAO.getRscsNumInitReschs()).ifPresentOrElse(getRscsNumInitReschs
                        -> rscsDAO.setRscsNumInitReschs((short) (rscsDAO.getRscsNumInitReschs() + 1)), () -> rscsDAO.setRscsNumInitReschs((short) 1));
            }
            else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode().equals(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())) {
                rscsDAO.setRscsFirstSubsApptDt(newRsicDAO.getRsicCalEventDt());
                Optional.ofNullable(rscsDAO.getRscsNumFstSubReschs()).ifPresentOrElse(getRscsNumFstSubReschs
                        -> rscsDAO.setRscsNumFstSubReschs((short) (rscsDAO.getRscsNumFstSubReschs() + 1)), () -> rscsDAO.setRscsNumFstSubReschs((short) 1));
            }
            else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode().equals(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())) {
                rscsDAO.setRscsSecondSubsApptDt(newRsicDAO.getRsicCalEventDt());
                Optional.ofNullable(rscsDAO.getRscsNumSecSubReschs()).ifPresentOrElse(getRscsNumSecSubReschs
                        -> rscsDAO.setRscsNumSecSubReschs((short) (rscsDAO.getRscsNumSecSubReschs() + 1)), () -> rscsDAO.setRscsNumSecSubReschs((short) 1));
            }
            if(StringUtils.isNotBlank(rscsDAO.getRscsPriority()) &&
                    StringUtils.equalsIgnoreCase(rscsDAO.getRscsPriority(), RSCS_PRIORITY_HI)) {
                // When the selected rescheduled date is before 21 days and out of compliance
                if (ReseaConstants.INDICATOR.N.toString().equalsIgnoreCase(rschSaveReqDTO.getNonComplianceInd()) &&
                        StringUtils.isBlank(rschSaveReqDTO.getLateSchedulingReason())) {
                    //remove the HI priority from the case (RSCS)
                    rscsDAO.setRscsPriority(null);
                }
            }
            if (INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd())) {
                rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
                rscsDAO.setRscsOnWaitlistDt(null);
                rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
                rscsDAO.setRscsOnWaitlistClearDt(parRepo.getCurrentDate());
            }
            rscsDAO.setRscsLastUpdBy(userId);
            rscsDAO.setRscsLastUpdUsing(ReseaConstants.RESCHEDULE_BY_USING);
            rscsRepo.save(rscsDAO);
        }
    }

    /**
     * Create Activity associated with Reschedule page
     *  Activities types
     *      Staff Rescheduled (5577)
     *
     * @return  Map<String, Object>
     */
    @Transactional
    private Map<String, Object> createActivityForStaffRschType(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO newRsicDAO,
                                                               ReseaRescheduleSaveReqDTO rschSaveReqDTO, Long rsrsId,
                                                               Timestamp timestamp, Date systemDate, String userId, ReseaIntvwerCalRsicDAO oldRsicDAO) {
        Map<String, Object> createActivity = null;
        try {
            if(null != rsrsId  &&  null != rscsId && null != newRsicDAO) {
                String synopsisType;
                String caseSynopsis;
                String rscaDetails;
                String lastSchRsn = null;
                ParameterParDao parameterParDao;
                long parNumericValue;
                String issueCreatedDesc = null;
                String showInNhuisInd = null;
                String newRschCalEventStTime = null;
                String oldRschCalEventStTime = null;
                String newMeetingModeDesc = null;
                String oldMeetingModeDesc = null;
                DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
                AllowValAlvDAO reasonForRschAlvDao = alvRepo.findById(rschSaveReqDTO.getReasonForRescheduling()).orElseThrow();
                AllowValAlvDAO newApptTypeAlvDao = alvRepo.findById(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();
                AllowValAlvDAO oldApptTypeAlvDao = alvRepo.findById(oldRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();
                parameterParDao = parRepo.findByParShortName(ReseaConstants.RESEA_DEADLINE_DAYS);
                parNumericValue = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;

                if(StringUtils.isNotBlank(oldRsicDAO.getRsicMtgModeInd())) {
                    oldMeetingModeDesc = oldRsicDAO.getRsicMtgModeInd().equalsIgnoreCase(ReseaConstants.MEETING_MODE.IN_PERSON.getCode()) ?
                            ReseaConstants.MEETING_MODE.IN_PERSON.getDescription() :ReseaConstants.MEETING_MODE.VIRTUAL.getDescription();
                }
                else {
                    oldMeetingModeDesc = ReseaConstants.MEETING_MODE.IN_PERSON.getDescription();
                }

                if(StringUtils.isNotBlank(newRsicDAO.getRsicMtgModeInd())) {
                    newMeetingModeDesc = newRsicDAO.getRsicMtgModeInd().equalsIgnoreCase(ReseaConstants.MEETING_MODE.IN_PERSON.getCode()) ?
                            ReseaConstants.MEETING_MODE.IN_PERSON.getDescription() :ReseaConstants.MEETING_MODE.VIRTUAL.getDescription();
                }
                else {
                    newMeetingModeDesc = ReseaConstants.MEETING_MODE.IN_PERSON.getDescription();
                }

                if(null != oldRsicDAO.getRsicCalEventStTime()) {
                    LocalTime time = LocalTime.parse(oldRsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    oldRschCalEventStTime = time.format(formatter12Hour);
                }
                if(null != newRsicDAO.getRsicCalEventStTime()) {
                    LocalTime time = LocalTime.parse(newRsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    newRschCalEventStTime = time.format(formatter12Hour);
                }

                if (ReseaConstants.INDICATOR.Y.toString().equalsIgnoreCase(rschSaveReqDTO.getNonComplianceInd()) &&
                        StringUtils.isNotBlank(rschSaveReqDTO.getLateSchedulingReason())) {
                    synopsisType =  ReseaConstants.RSCA_SYNOPSIS_TYPE.R.toString();
                    caseSynopsis = "Scheduled beyond "+parNumericValue+ " days.";
                    lastSchRsn = "The new appointment had to be scheduled beyond the " +parNumericValue+ "-day timeframe because: " + rschSaveReqDTO.getLateSchedulingReason() + ".\n\n";
                } else {
                    synopsisType =  ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString();
                    caseSynopsis = "--";
                }

                if(!CollectionUtils.isEmpty(rschSaveReqDTO.getIssuesDTOList())) {
                    issueCreatedDesc = rschSaveReqDTO.getIssuesDTOList().size() + " issue(s) have been created.";
                    showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                }
                else {
                    showInNhuisInd =  ReseaConstants.INDICATOR.Y.toString();
                }


                rscaDetails = StringUtils.join("Staff has rescheduled " +newMeetingModeDesc+ " " + newApptTypeAlvDao.getAlvShortDecTxt() +
                        " to " + newRschCalEventStTime+ " on " +  dateToString.apply(newRsicDAO.getRsicCalEventDt()) + ".\n\n" +
                        "The reason for the reschedule is " + reasonForRschAlvDao.getAlvShortDecTxt() + ".\n\n");
                if(StringUtils.isNotBlank(lastSchRsn)) {
                    rscaDetails = StringUtils.join(rscaDetails + lastSchRsn);
                }
                if(StringUtils.isNotBlank(issueCreatedDesc)) {
                    rscaDetails = StringUtils.join(rscaDetails + issueCreatedDesc);
                }

                String rscnNoteCategory = null;
                String rscnNote = null;

                if(StringUtils.isNotBlank(rschSaveReqDTO.getStaffNotes())) {
                    rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),
                            rscsStageCdAlv));
                    rscnNote = rschSaveReqDTO.getStaffNotes();
                    showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
                }
                else {
                    rscnNoteCategory = StringUtils.EMPTY;
                    rscnNote = StringUtils.EMPTY;
                    showInNhuisInd =  StringUtils.EMPTY;
                }

                saveCno(newRsicDAO, rschSaveReqDTO, timestamp, userId, oldRsicDAO, newApptTypeAlvDao,
                        oldMeetingModeDesc, oldApptTypeAlvDao, oldRschCalEventStTime, newMeetingModeDesc, newRschCalEventStTime);

                entityManager.flush();

                createActivity = rscaRepo.createCaseActivity(
                        rscsId,
                        getRscaStageFromRsicTimeslotUsage(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        getRscaStatusFromRsicMtgStatus(newRsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
                        //getRscaStageFromRscsStage(rscsStageCdAlv),
                        //getRscaStatusFromRscsStatus(rscsStatusCdAlv),
                        ReseaAlvEnumConstant.RscaTypeCd.STAFF_RESCHEDULED.getCode(),
                        timestamp,
                        "Staff has rescheduled " + newMeetingModeDesc + " " +newApptTypeAlvDao.getAlvShortDecTxt(),
                        synopsisType,
                        caseSynopsis,
                        0L, 0L, stringToDate.apply(DATE_01_01_2000),
                        StringUtils.EMPTY, StringUtils.EMPTY,
                        rscaDetails,
                        ReseaAlvEnumConstant.RscaReferenceIfkCd.RSRS.getCode(),
                        rsrsId,
                        rscnNoteCategory,
                        rscnNote,
                        showInNhuisInd,
                        ReseaConstants.RSCA_CALLING_PROGRAM_RESCHEDULE
                );
            }
        }
        catch (Exception e) {
            System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Reschedule Activity type (5577) associated with " +
                    "RSIC ID "+ newRsicDAO.getRsicId());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }

    @Transactional
    private void saveCno(ReseaIntvwerCalRsicDAO newRsicDAO, ReseaRescheduleSaveReqDTO rschSaveReqDTO, Timestamp timestamp, String userId,
                         ReseaIntvwerCalRsicDAO oldRsicDAO, AllowValAlvDAO newApptTypeAlvDao, String oldMeetingModeDesc, AllowValAlvDAO oldApptTypeAlvDao,
                         String oldRschCalEventStTime, String newMeetingModeDesc, String newRschCalEventStTime) {
        CmtNotesCnoDao cnoDao = new CmtNotesCnoDao();
        cnoDao.setCnoEnteredBy(userId);
        cnoDao.setCnoEnteredTs(timestamp);
        cnoDao.setFkCmtId(newRsicDAO.getClaimDAO().getClaimantDAO().getCmtId());
        cnoDao.setCnoSubjectTxt("RESEA: "+ newApptTypeAlvDao.getAlvShortDecTxt() + " Rescheduled");
        String notes = StringUtils.join("The " + oldMeetingModeDesc + " " + oldApptTypeAlvDao.getAlvShortDecTxt() +
                " scheduled for " + dateToString.apply(oldRsicDAO.getRsicCalEventDt()) + " at " + oldRschCalEventStTime + " has been marked as rescheduled to an "+
                newMeetingModeDesc + " " + newApptTypeAlvDao.getAlvShortDecTxt() + " on " + dateToString.apply(newRsicDAO.getRsicCalEventDt()) + " at " + newRschCalEventStTime + ".\n\n");
        if(rschSaveReqDTO.isIncludeThisNoteInCNO() && StringUtils.isNotBlank(rschSaveReqDTO.getStaffNotes())) {
            notes = notes + rschSaveReqDTO.getStaffNotes();
        }
        cnoDao.setCnoNotesTxt(notes);
        cnoDao.setCnoLastUpdBy(userId);
        cnoDao.setCnoLastUpdTs(timestamp);
        cnoRepo.save(cnoDao);
    }

    /**
     * Create Activity for Issues added up in Reschedule page
     *  RSCA Activities type
     * 	 * 		- CREATED ISSUE 5586
     *
     * @return  Map<String, Object>
     */
    @Transactional
    private Map<String, Object> createActivityForIssuesAddedinRschPage(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO newRsicDAO,
                                                                       ReseaRescheduleSaveReqDTO rschSaveReqDTO, Timestamp timestamp, Date systemDate, ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
        Map<String, Object> createActivity = null;
        try {
            if(null != rsiiDAO.getRsiiId()  &&  null != rscsId &&  null != newRsicDAO) {
                String rscaDetails = null;
                String showInNhuisInd = null;
                String rschCalEventStTime = null;
                String issueDatesDesc = null;
                String staffNotes = null;
                NonMonIssuesNmiDAO nmiDAO = rsiiDAO.getNmiDAO();
                if(null != rsiiDAO.getRsiiIssueEndDt()) {
                    issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to " + dateToString.apply(rsiiDAO.getRsiiIssueEndDt());
                }
                else {
                    issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to -";
                }
                AllowValAlvDAO apptTypeAlvDao = alvRepo.findById(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();

                if(null != newRsicDAO.getRsicCalEventStTime()) {
                    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
                    LocalTime time = LocalTime.parse(newRsicDAO.getRsicCalEventStTime(), formatter24Hour);
                    rschCalEventStTime = time.format(formatter12Hour);
                }
                rscaDetails =  "Staff has created the issue " + nmiDAO.getParentNmiDAO().getNmiShortDescTxt() + " / " + nmiDAO.getNmiShortDescTxt() + " effective " + issueDatesDesc
                        + " while rescheduling for the " + apptTypeAlvDao.getAlvShortDecTxt() + " that was scheduled for " + rschCalEventStTime + " on " + dateToString.apply(newRsicDAO.getRsicCalEventDt()) + ".\n\n";

                int count = 0;
                if (!CollectionUtils.isEmpty(rschSaveReqDTO.getIssuesDTOList())) {
                    if(rschSaveReqDTO.getIssuesDTOList().size() == 1) {
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

                if(StringUtils.isNotBlank(rschSaveReqDTO.getStaffNotes())) {
                    rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),
                            rscsStageCdAlv));
                    rscnNote = rschSaveReqDTO.getStaffNotes();
                    showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
                }
                else {
                    rscnNoteCategory = StringUtils.EMPTY;
                    rscnNote = StringUtils.EMPTY;
                    showInNhuisInd = StringUtils.EMPTY;
                }
                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsId,
                        getRscaStageFromRsicTimeslotUsage(newRsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
                        getRscaStatusFromRsicMtgStatus(newRsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
                        //getRscaStageFromRscsStage(rscsStageCdAlv),
                        //getRscaStatusFromRscsStatus(rscsStatusCdAlv),
                        ReseaAlvEnumConstant.RscaTypeCd.CREATED_ISSUE.getCode(),
                        timestamp,
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
                        ReseaConstants.RSCA_CALLING_PROGRAM_RESCHEDULE
                );
            }
        }
        catch (Exception e) {
            System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Issues added in Reschedule page associated with RSIC ID "+ newRsicDAO.getRsicId());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }
}