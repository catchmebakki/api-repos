package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.UserDAO;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.database.repository.UserRepository;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.ClaimClmDAO;
import com.ssi.ms.resea.database.dao.ClaimantCmtDAO;
import com.ssi.ms.resea.database.dao.CmtPhoneNbrCpoDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.ReseaRmtMtgInfoRsrmDAO;
import com.ssi.ms.resea.database.mapper.IssueDecisionDecMapper;
import com.ssi.ms.resea.database.mapper.ReseaJobReferralRsjrMapper;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.CmtPhoneNbrCpoRepository;
import com.ssi.ms.resea.database.repository.IssueDecisionDecRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseActivityRscaRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.ReseaJobReferralRsjrRepository;
import com.ssi.ms.resea.database.repository.ReseaRmtMtgInfoRsrmRepository;
import com.ssi.ms.resea.database.repository.WeeklyWorkSearchWwsRepository;
import com.ssi.ms.resea.dto.HeaderDetailsResDTO;
import com.ssi.ms.resea.dto.HeaderIssueDetailsDTO;
import com.ssi.ms.resea.dto.HeaderJobRefDetailsDTO;
import com.ssi.ms.resea.dto.HeaderWorkSrchDetailsDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaCalendarEventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateTimeToDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.CREATE_ACTIVITY_EXCEPTION;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_01_01_2000;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_CURR_APP_CLSOUT_TIME;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_REOPN_FUT_DAY;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class ReseaCalendarEventService {

    @Autowired
    private ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    private ReseaRmtMtgInfoRsrmRepository rsrmRepository;
    @Autowired
    private IssueDecisionDecRepository decRepository;
    @Autowired
    private WeeklyWorkSearchWwsRepository wwsRepository;
    @Autowired
    private ReseaJobReferralRsjrRepository rsjrRepository;
    @Autowired
    private IssueDecisionDecMapper decMapper;
    @Autowired
    private ReseaJobReferralRsjrMapper rsjrMapper;
    @Autowired
    private ParameterParRepository parRepository;
    @Autowired
    private ReseaCaseActivityRscaRepository rscaRepo;
    @Autowired
    private ReseaCalendarEventValidator calendarEventValidator;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private CmtPhoneNbrCpoRepository cpoRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${ccf-view-certified-clm.url}")
    private String viewCertifiedCCFUrl;
    @Autowired
    EntityManager entityManager;
    public ReseaCalendarEventService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }


    public HeaderDetailsResDTO getCaseHeaderDetails(Long rsicId, String userId, String roleId) {
        HeaderDetailsResDTO headerDetailsResDTO = null;
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(rsicId)
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + rsicId, EVENT_ID_NOT_FOUND));
        if (rsicDAO.getClaimDAO() != null) {
            final ClaimClmDAO clmDAO = rsicDAO.getClaimDAO();
            final ClaimantCmtDAO cmtDAO = clmDAO.getClaimantDAO();
            final ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();

            List<HeaderWorkSrchDetailsDTO> workSrchDetailsDTOList = getCaseWorkSearchReq(clmDAO, rsicDAO.getRsicCalEventDt());
            //workSrchDetailsDTOList = includeViewCertifiedUrl(workSrchDetailsDTOList);
            headerDetailsResDTO = new HeaderDetailsResDTO()
                    .withAppointmentStatus((rsicDAO.getRsidDAO() != null ||
                            dateToLocalDateTime.apply(parRepository.getCurrentTimestamp()).isAfter(
                                    dateToLocalDateTime.apply(string24HToDate().apply(dateToString
                                            .apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventEndTime()))
                            )) &&
                            rsicDAO.getRsicMtgStatusCdAlv() != null ? rsicDAO.getRsicMtgStatusCdAlv().getAlvShortDecTxt() : null)
                    .withClmBeginDt(clmDAO.getClmEffectiveDt())
                    .withClmByDt(clmDAO.getClmBenYrEndDt())
                    .withIssues(getCaseIssueDetails(clmDAO, rsicDAO.getRsicCalEventDt()))
                    .withWorkSearch(workSrchDetailsDTOList)
                    .withJobReferrals(getCaseJobReferrals(rscsDAO, rsicDAO.getRsicCalEventDt()))
                    .withWeeksFiled(workSrchDetailsDTOList.size())
                    .withReopenInd(rsicDAO.getRsicReopenAllowedTs() != null && rsicDAO.getRsicReopenAllowedTs().after(parRepository.getCurrentTimestamp())
                            && ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())?
                            ReseaConstants.INDICATOR.Y.toString() : ReseaConstants.INDICATOR.N.toString())
                    .withApplyWaitlistInd(rscsDAO != null && !ReseaConstants.INDICATOR.Y.name().equals(rscsDAO.getRscsOnWaitlistInd())
                            && checkFutureDate.test(rsicDAO.getRsicCalEventDt(), parRepository.getCurrentDate())
                            ? ReseaConstants.INDICATOR.Y.name() : ReseaConstants.INDICATOR.N.name())
                    .withClearWaitlistInd(rscsDAO != null && ReseaConstants.INDICATOR.Y.name().equals(rscsDAO.getRscsOnWaitlistInd())
                            ? ReseaConstants.INDICATOR.Y.name() : ReseaConstants.INDICATOR.N.name());
            if (cmtDAO != null) {
                headerDetailsResDTO = headerDetailsResDTO
                        .withClaimant(cmtDAO.getClaimantName())
                        .withEmail(cmtDAO.getCmtEmailAddress())
                        .withPhone(Optional.ofNullable(cpoRepository.findByFkCmtIdAndCpoPhoneNbrPref(cmtDAO.getCmtId(), (short) 1))
                                .map(CmtPhoneNbrCpoDAO::getPhoneNbr)
                                .orElse(null));
            }
            if (rscsDAO != null) {
                headerDetailsResDTO = populateCaseDetails(headerDetailsResDTO, rscsDAO, rsicDAO.getRsicCalEventDt());
            }

            if (ReseaConstants.MEETING_MODE.VIRTUAL.getCode().equals(rsicDAO.getRsicMtgModeInd())) {
                headerDetailsResDTO = populateVirtualAppointmentDetails(headerDetailsResDTO,
                        rsrmRepository.getMeetingInfo(rsicDAO.getRsisDAO().getRsisId(), rsicDAO.getRsicCalEventDt()));
            }
            headerDetailsResDTO = populateAccessDetails(headerDetailsResDTO, rsicDAO, userId, roleId);
        }
        return headerDetailsResDTO;
    }

    /*private List<HeaderWorkSrchDetailsDTO> includeViewCertifiedUrl(List<HeaderWorkSrchDetailsDTO> workSrchDetailsDTOList) {
        List<HeaderWorkSrchDetailsDTO> updWorkSrchDetailsDTOList = new ArrayList<HeaderWorkSrchDetailsDTO>();
        if(!isEmpty(workSrchDetailsDTOList)) {
            workSrchDetailsDTOList.forEach(headerWorkSrchDetailsDTO -> {
                headerWorkSrchDetailsDTO.setViewCertifiedCCFUrl(viewCertifiedCCFUrl + headerWorkSrchDetailsDTO.getCcaId());
                updWorkSrchDetailsDTOList.add(headerWorkSrchDetailsDTO);
            });
        }
        return updWorkSrchDetailsDTOList;
    }*/

    private HeaderDetailsResDTO populateAccessDetails(HeaderDetailsResDTO detailsResDTO, ReseaIntvwerCalRsicDAO rsicDAO,
                                                      String userId, String roleId) {
        String submitAccess = ReseaConstants.ACCESS_TYPE.N.getCode();
        final int rolId = Integer.parseInt(roleId);
        final long usrId = Long.parseLong(userId);
        if ((usrId == rsicDAO.getRsisDAO().getStfDAO().getUserDAO().getUserId()
                && rolId == ROL_RESEA_CASE_MANAGER) ||
                (rolId == ROL_RESEA_PROG_STAFF || rolId == ROL_LOCAL_OFFICE_MANAGER)
        ) {
            submitAccess = ReseaConstants.ACCESS_TYPE.Y.getCode();
        }
        String reopenAccess = ReseaConstants.ACCESS_TYPE.N.getCode();
        boolean reopened = false;
        LocalDateTime systemDate = dateToLocalDateTime.apply(parRepository.getCurrentTimestamp());
        if (ReseaConstants.ACCESS_TYPE.Y.getCode().equals(rsicDAO.getRsicReopenAllowedInd()) &&
           systemDate.isBefore(dateToLocalDateTime.apply(rsicDAO.getRsicReopenAllowedTs()))) {
            reopened = true;
        } else if(rolId == ROL_RESEA_PROG_STAFF
              // && ReseaConstants.ACCESS_TYPE.N.getCode().equals(rsicDAO.getRsicReopenAllowedInd())
                && null != rsicDAO.getRsicCalEventEndTime()
                && dateToLocalDateTime.apply(rsicDAO.getRsicCalEventDt()).isBefore(systemDate)) {
            LocalDateTime reopenExpiry = dateToLocalDateTime.apply(rsicDAO.getRsicCalEventDt())
                    .plusDays(parRepository.findByParShortName(PAR_RESEA_REOPN_FUT_DAY).getParNumericValue());
            if (reopenExpiry.isAfter(systemDate)) {
                LocalDateTime reopenStart = dateToLocalDateTime.apply(string24HToDate().apply(dateToString
                                .apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventEndTime()))
                        .plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue());
                if (systemDate.isAfter(reopenStart)) {
                    reopenAccess = ReseaConstants.ACCESS_TYPE.Y.getCode();
                }
            }
        }
        if (reopened) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.Y.getCode());
        } else if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() == ReseaAlvEnumConstant.RsicMeetingStatusCd.SCHEDULED.getCode().longValue()) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.Y.getCode());
            if (rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId() == ReseaAlvEnumConstant.RscsStageCd.TERMINATED.getCode().longValue()) {
                detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                                             .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode());
            }
        } else if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() == ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED.getCode().longValue()) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.V.getCode());
            if (rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId() == ReseaAlvEnumConstant.RscsStageCd.TERMINATED.getCode().longValue()) {
                detailsResDTO = detailsResDTO.withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.N.getCode());
            }
        } else if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() == ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED.getCode().longValue()) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.Y.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.V.getCode());
        } else if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() == ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED_RTW.getCode().longValue()) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.V.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.N.getCode());
        } else if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() == ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED_RTW.getCode().longValue()) {
            detailsResDTO = detailsResDTO.withRescheduleAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withSwitchModeAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withReturnToWorkAccess(ReseaConstants.ACCESS_TYPE.V.getCode())
                    .withAppointmentAccess(ReseaConstants.ACCESS_TYPE.N.getCode())
                    .withNoShowAccess(ReseaConstants.ACCESS_TYPE.N.getCode());
        }
        return detailsResDTO.withSubmitAccess(submitAccess).withReopenAccess(reopenAccess);
    }

    private HeaderDetailsResDTO populateCaseDetails(HeaderDetailsResDTO detailsResDTO,
                                                    ReseaCaseRscsDAO rscsDAO,
                                                    Date apptDt) {
        return detailsResDTO
                .withCaseStatus(rscsDAO.getRscsStatusCdALV().getAlvShortDecTxt())
                .withCaseStage(rscsDAO.getRscsStageCdALV().getAlvShortDecTxt())
                .withSsnLast4Digits(rscsDAO.getClmDAO().getClaimantDAO().getSsn().substring(5))
                .withOrientationDt(rscsDAO.getRscsOrientationDt().after(stringToDate.apply(DATE_01_01_2000))
                        ? dateToString.apply(rscsDAO.getRscsOrientationDt()) : "Conversion")
                .withOrientatonRschCnt(rscsDAO.getRscsNumOrntnReschs())
                .withInitialAppttDt(rscsDAO.getRscsInitApptDt() != null && apptDt.compareTo(rscsDAO.getRscsInitApptDt()) >= 0
                        ? rscsDAO.getRscsInitApptDt() : null)
                .withInitialAppttRschCnt(rscsDAO.getRscsInitApptDt() != null && apptDt.compareTo(rscsDAO.getRscsInitApptDt()) >= 0
                        ? rscsDAO.getRscsNumInitReschs() : null)
                .withFirstSubsequentApptDt(rscsDAO.getRscsFirstSubsApptDt() != null && apptDt.compareTo(rscsDAO.getRscsFirstSubsApptDt()) >= 0
                        ? rscsDAO.getRscsFirstSubsApptDt() : null)
                .withFirstSubsequentApptRschCnt(rscsDAO.getRscsFirstSubsApptDt() != null && apptDt.compareTo(rscsDAO.getRscsFirstSubsApptDt()) >= 0
                        ? rscsDAO.getRscsNumFstSubReschs() : null)
                .withSecondSubsequentApptDt(rscsDAO.getRscsSecondSubsApptDt() != null && apptDt.compareTo(rscsDAO.getRscsSecondSubsApptDt()) >= 0
                        ? rscsDAO.getRscsSecondSubsApptDt() : null)
                .withSecondSubsequentApptRschCnt(rscsDAO.getRscsSecondSubsApptDt() != null && apptDt.compareTo(rscsDAO.getRscsSecondSubsApptDt()) >= 0
                        ? rscsDAO.getRscsNumSecSubReschs() : null)
                .withSelfScheduleDt(localDateToDate.apply(dateToLocalDate.apply(apptDt).plusDays(10)));
    }

    private HeaderDetailsResDTO populateVirtualAppointmentDetails(HeaderDetailsResDTO detailsResDTO,
                                                                  ReseaRmtMtgInfoRsrmDAO rsrmDAO) {
        return rsrmDAO == null ? detailsResDTO :
                detailsResDTO.withMeetingUrl(rsrmDAO.getRsrmMeetingUrl())
                        .withMeetingId(rsrmDAO.getRsrmMeetingId())
                        .withPasscode(rsrmDAO.getRsrmMeetingPwd())
                        .withTelephoneNum1(rsrmDAO.getRsrmMeetingTele1())
                        .withTelephoneNum2(rsrmDAO.getRsrmMeetingTele2());
    }

    public List<HeaderIssueDetailsDTO> getCaseIssueDetails(ClaimClmDAO clmDAO, Date apptDt) {
        return clmDAO == null ? null :
                decRepository.getOpenDenyNonMonIssue(clmDAO.getClmId(), apptDt).stream()
                        .map(dao -> decMapper.daoToDto(dao))
                        .map(dto -> dto.withCreatedBy(dto.getRsiiId() != null ? "RESEA" : "Other"))
                        .map(dto -> dto.withDecStatus(dto.getDecStatusCd() == 281 ? dto.getDecStatus() : dto.getDecDecision()))
                        .toList();
    }

    public List<HeaderWorkSrchDetailsDTO> getCaseWorkSearchReq(ClaimClmDAO clmDAO, Date apptDt) {
        return clmDAO == null ? null : wwsRepository.getWorkSearchDetails(clmDAO.getClmId(), apptDt)
                .stream().map(headerWorkSrchDetailsDTO ->
                        headerWorkSrchDetailsDTO.withViewCertifiedCCFUrl(viewCertifiedCCFUrl + headerWorkSrchDetailsDTO.getCcaId()))
                .toList();
    }

    public List<HeaderJobRefDetailsDTO> getCaseJobReferrals(ReseaCaseRscsDAO rscsDAO, Date apptDt) {
        return rscsDAO == null ? null : rsjrRepository.getJobReferralsList(rscsDAO.getRscsId(), apptDt)
                .stream().map(dao -> rsjrMapper.daoToDto(dao))
                .map(dto -> dto.withCreatedBy(StringUtils.isNumeric(dto.getCreatedBy()) 
                        ? Optional.ofNullable(userRepository.findByUserId(Long.parseLong(dto.getCreatedBy())))
                            .filter(dao -> !dao.getUserFirstName().equalsIgnoreCase(CommonConstant.SYSTEM))
                            .map(UserDAO::getFullName).orElse(CommonConstant.SYSTEM)
                        : CommonConstant.SYSTEM))
                .toList();
    }
    @Transactional
    public String reopenInterviewDetails(Long rsicId, String userId, String roleId) {
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(rsicId)
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + rsicId, EVENT_ID_NOT_FOUND));
        Date systemDate = parRepository.getCurrentTimestamp();
        Long reopenExpiryInDays = parRepository.findByParShortName(PAR_RESEA_REOPN_FUT_DAY).getParNumericValue();
        final HashMap<String, List<String>> errorMap = calendarEventValidator.validateReopen(rsicDAO, roleId, systemDate, reopenExpiryInDays);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Event Reopen Validation Failed.", errorMap);
        }
        boolean CREATE_ACTIVITY_FAILED = false;
        if (rsicDAO != null) {
            rsicDAO.setRsicReopenAllowedInd(ReseaConstants.INDICATOR.Y.toString());
            rsicDAO.setRsicReopenAllowedTs(localDateTimeToDate.apply(dateToLocalDateTime.apply(systemDate)
                    .plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue())));
            //rsicDAO.setRsicMtgStatusCdAlv(alvRepository.findById(RSIC_MTG_STATUS_SCHEDULED).get());
            //rsicDAO.setRsicMtgStatusUpdBy(Long.parseLong(userId));
            //rsicDAO.setRsicMtgStatusUpdTs(systemDate);
            //rsicDAO.setRsicCalEventTypeCdAlv(alvRepository.findById(RSIC_CAL_EVENT_TYPE_IN_USE_ALV).get());
            rsicDAO.setRsicLastUpdTs(systemDate);
            rsicDAO.setRsicLastUpdBy(userId);
            rsicDAO.setRsicLastUpdUsing(ReseaConstants.RSCA_CALLING_PROGRAM_REOPEN);
            CREATE_ACTIVITY_FAILED = createReopenTurnedOnActivity(rsicDAO, systemDate);
        }
        return CREATE_ACTIVITY_FAILED ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : "Success";
    }
    @Transactional
    private boolean createReopenTurnedOnActivity(ReseaIntvwerCalRsicDAO rsicDAO,
                                               Date systemDate) {
        boolean CREATE_ACTIVITY_FAILED = true;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> createActivity = createActivityForReopenTurnedOn(rsicDAO, systemDate);
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
        }
        return CREATE_ACTIVITY_FAILED;
    }

    @Transactional
    private Map<String, Object>  createActivityForReopenTurnedOn(ReseaIntvwerCalRsicDAO rsicDAO,
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
                rscaDetails = StringUtils.join("The special reopen window to record details of appointment attendance is now opened at "+
                        rschCalEventStTime+ " on " +  dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n");
                entityManager.flush();
                createActivity = rscaRepo.createCaseActivity(
                        rscsDAO.getRscsId(),
                        getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId()),
                        getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId()),
                        ReseaAlvEnumConstant.RscaTypeCd.REOPEN_TURNED_ON.getCode(),
                        systemDate,
                        "The special reopen window to record details of appointment attendance is now opened.",
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
                        ReseaConstants.RSCA_CALLING_PROGRAM_REOPEN
                );
            }
        }
        catch (Exception e) {
            System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Reopen Turned On Activity type (5759) associated with " +
                    "RSIC ID "+ rsicDAO.getRsicId());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return createActivity;
    }
}
