package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.NhuisLogNhlDao;
import com.ssi.ms.common.database.repository.NhuisLogNhlRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.StaffUnavailabilityReqSunrDAO;
import com.ssi.ms.resea.database.mapper.StaffUnavailabilityReqSunrMapper;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.CommonRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.StaffStfRepository;
import com.ssi.ms.resea.database.repository.StaffUnavailabilityReqSunrRepository;
import com.ssi.ms.resea.dto.IdNameResDTO;
import com.ssi.ms.resea.dto.UnavailablityDetailResDTO;
import com.ssi.ms.resea.dto.UnavailablityRejectReqDTO;
import com.ssi.ms.resea.dto.UnavailablityRequestReqDTO;
import com.ssi.ms.resea.dto.UnavailablitySummaryReqDTO;
import com.ssi.ms.resea.dto.UnavailablitySummaryResDTO;
import com.ssi.ms.resea.dto.UnavailablityWithdrawReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaUnavailabilityValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_APPROVE_FAILED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_FAILED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UNAVAILABILITY_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_01_01_2000;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.resea.constant.ReseaConstants.GENERATECOMMENTS;
import static com.ssi.ms.resea.constant.ReseaConstants.INDICATOR.N;
import static com.ssi.ms.resea.constant.ReseaConstants.INDICATOR.Y;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.SUNR_TYPE_IND;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert12hTo24h;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert24hTo12h;

@Slf4j
@Service
public class ReseaUnavailabilityService {
    @Autowired
    EntityManager entityManager;
    public ReseaUnavailabilityService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    @Autowired
    CommonRepository commonRepo;
    @Autowired
    StaffUnavailabilityReqSunrRepository sunrRepository;
    @Autowired
    ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    StaffUnavailabilityReqSunrMapper sunrMapper;
    @Autowired
    StaffStfRepository stfRepository;
    @Autowired
    ReseaUnavailabilityValidator sunrValidator;
    @Autowired
    AllowValAlvRepository alvRepository;
    @Autowired
    NhuisLogNhlRepository nhlRepository;
    @Autowired
    UserService userService;
    @Autowired
    ReseaStaffService staffService;
    public List<UnavailablitySummaryResDTO> getUnavailabilitySummary(UnavailablitySummaryReqDTO stunReqDTO,
                                                                     String userId, String roleId) {
        final int rolId = Integer.parseInt(roleId);
        List<UnavailablitySummaryResDTO> summaryResDTOList = null;
        if ((stunReqDTO == null || stunReqDTO.getUserId() == null) &&
                ROL_LOCAL_OFFICE_MANAGER.equals(rolId)) {
            summaryResDTOList = getPendingUnavailabilitySummary(stunReqDTO, userId);
        } else if (stunReqDTO != null) {
            final Date systemDate = commonRepo.getCurrentDate();
            final HashMap<String, List<String>> errorMap = sunrValidator.validateSummaryRequest(stunReqDTO);
            if (!errorMap.isEmpty()) {
                throw new CustomValidationException("Summary Request Failed", errorMap);
            }
            final long usrId = Long.parseLong(userId);
            List<Long> caseManager = List.of(stunReqDTO.getUserId());
            final Sort.Direction sortDir = (null != stunReqDTO.getSortBy() && "DESC".equalsIgnoreCase(stunReqDTO.getSortBy().getDirection()))
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            final String sortField = null != stunReqDTO.getSortBy() && UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING
                    .containsKey(StringUtils.trimToEmpty(stunReqDTO.getSortBy().getField()))
                    ? stunReqDTO.getSortBy().getField() : "default";
            final Long caseManagerId = stunReqDTO.getUserId();
            summaryResDTOList = sunrRepository.getStaffSunrSummary(caseManager, stunReqDTO.getStartDt(), stunReqDTO.getEndDt(), stunReqDTO.getStatus(),
                            Sort.by(UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING.get(sortField).stream()
                                    .map(column -> new Sort.Order(sortDir, column)).toList()))
                    .stream()
                    .map(dto -> dto.withRecurring(ReseaConstants.SUNR_TYPE_IND.R.getCode().equalsIgnoreCase(dto.getTypeInd()))
                            .withApproveInd((ROL_LOCAL_OFFICE_MANAGER.equals(rolId) || ROL_RESEA_PROG_STAFF.equals(rolId))
                                    && ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode().equals(dto.getStatusId())
                                    ? Y.name() : N.name())
                            .withRejectInd((ROL_LOCAL_OFFICE_MANAGER.equals(rolId) || ROL_RESEA_PROG_STAFF.equals(rolId))
                                    && ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode().equals(dto.getStatusId())
                                    ? Y.name() : N.name())
                            .withWithdrawInd((ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode().equals(dto.getStatusId())
                                        || ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode().equals(dto.getStatusId()))
                                    && ((usrId == caseManagerId && ROL_RESEA_CASE_MANAGER.equals(rolId))
                                        || ROL_RESEA_PROG_STAFF.equals(rolId) || ROL_LOCAL_OFFICE_MANAGER.equals(rolId))
                                    && dateToLocalDateTime.apply(dto.getEndDateTime()).isAfter(dateToLocalDateTime.apply(systemDate))
                                    ? Y.name() : N.name()))
                    .toList();
        }
        return summaryResDTOList;
    }

    public List<UnavailablitySummaryResDTO> getPendingUnavailabilitySummary(UnavailablitySummaryReqDTO stunReqDTO, String userId) {
        final Sort.Direction sortDir = (stunReqDTO != null && null != stunReqDTO.getSortBy()
                && "DESC".equalsIgnoreCase(stunReqDTO.getSortBy().getDirection()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        final String sortField = (stunReqDTO != null && null != stunReqDTO.getSortBy()
                && UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING.containsKey(StringUtils.trimToEmpty(stunReqDTO.getSortBy().getField())))
                ? stunReqDTO.getSortBy().getField() : "default";
        List<Long> caseManagers = staffService.getLocalOfficeManagerReseaCaseManagerList(Long.parseLong(userId));
        return sunrRepository.getStaffSunrSummary(caseManagers, stringToDate.apply(DATE_01_01_2000), null,
                        ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode(),
                        Sort.by(UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING.get(sortField).stream()
                                .map(column -> new Sort.Order(sortDir, column)).toList()))
                .stream()
                .map(dto -> dto.withRecurring(ReseaConstants.SUNR_TYPE_IND.R.getCode().equalsIgnoreCase(dto.getTypeInd()))
                        .withApproveInd(Y.name())
                        .withRejectInd((Y.name()))
                        .withWithdrawInd(N.name()))
                .toList();
    }

    public UnavailablityDetailResDTO getUnavailablityDetail(Long unavailabilityId,
                                                            String userId, String roleId) {
        final StaffUnavailabilityReqSunrDAO sunrDAO = sunrRepository.findById(unavailabilityId)
                .orElseThrow(() -> new NotFoundException("Invalid Unavailablity ID:" + unavailabilityId, UNAVAILABILITY_ID_NOT_FOUND));
        List<Integer> days = getDaysSelected(sunrDAO);
        return sunrMapper.detailDaoToDto(sunrDAO)
                .withRecurring(ReseaConstants.SUNR_TYPE_IND.R.getCode().equals(sunrDAO.getSunrTypeInd()))
                .withStartTime(convert24hTo12h().apply(sunrDAO.getSunrStartTime()))
                .withEndTime(convert24hTo12h().apply(sunrDAO.getSunrEndTime()))
                .withDays(days);
    }

    private static List<Integer> getDaysSelected(StaffUnavailabilityReqSunrDAO sunrDAO) {
        List<Integer> days = null;
        if (ReseaConstants.SUNR_TYPE_IND.R.getCode().equals(sunrDAO.getSunrTypeInd())) {
            days = new ArrayList<>();
            if (Y.name().equals(sunrDAO.getSunrMondayInd())) days.add(2);
            if (Y.name().equals(sunrDAO.getSunrTuesdayInd())) days.add(3);
            if (Y.name().equals(sunrDAO.getSunrWednesdayInd())) days.add(4);
            if (Y.name().equals(sunrDAO.getSunrThursdayInd())) days.add(5);
            if (Y.name().equals(sunrDAO.getSunrFridayInd())) days.add(6);
        }
        return days;
    }

    @Transactional
    public String requestUnavailability(UnavailablityRequestReqDTO stunReqDTO, String userId, String roleId) {
        StaffUnavailabilityReqSunrDAO sunrDAO = stunReqDTO.getUnavailabilityId() == null ? new StaffUnavailabilityReqSunrDAO() :
                sunrRepository.findById(stunReqDTO.getUnavailabilityId()).orElseThrow(() ->
                        new NotFoundException("Invalid Unavailablity ID:" + stunReqDTO.getUnavailabilityId(), UNAVAILABILITY_ID_NOT_FOUND));
        final List<ReseaIntvwerCalRsicDAO> rsicList = stunReqDTO.isRecurring()
                ? rsicRepository.getRecurringInUseInterviewerCal(stunReqDTO.getUserId(),
                        stunReqDTO.getStartDt(), convert12hTo24h().apply(stunReqDTO.getStartTime()),
                        stunReqDTO.getEndDt(), convert12hTo24h().apply(stunReqDTO.getEndTime()),
                        stunReqDTO.getDays())
                : rsicRepository.getOneTimeInUseInterviewerCal(stunReqDTO.getUserId(),
                        stunReqDTO.getStartDt(), convert12hTo24h().apply(stunReqDTO.getStartTime()),
                        stunReqDTO.getEndDt(), convert12hTo24h().apply(stunReqDTO.getEndTime()));
        final boolean eventInUseOrStun = !CollectionUtils.isEmpty(rsicList);
        final HashMap<String, List<String>> errorMap = sunrValidator.validateUnavailabilityRequest(stunReqDTO.getUnavailabilityId(),
                stunReqDTO, eventInUseOrStun, userId, roleId);
        final Date systemDate = commonRepo.getCurrentTimestamp();
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Summary Request Failed", errorMap);
        }
        sunrDAO.setStfDAO(stfRepository.findByUserId(stunReqDTO.getUserId()));
        sunrDAO.setSunrTypeInd(stunReqDTO.isRecurring() ? ReseaConstants.SUNR_TYPE_IND.R.getCode()
                : ReseaConstants.SUNR_TYPE_IND.O.getCode());
        sunrDAO.setSunrReasonTypeCdAlv(alvRepository.findById(stunReqDTO.getReason()).get());
        sunrDAO.setSunrStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode()).get());
        sunrDAO.setSunrStartDt(stunReqDTO.getStartDt());
        sunrDAO.setSunrEndDt(stunReqDTO.getEndDt());
        sunrDAO.setSunrStartTime(convert12hTo24h().apply(stunReqDTO.getStartTime()));
        sunrDAO.setSunrEndTime(convert12hTo24h().apply(stunReqDTO.getEndTime()));
        sunrDAO.setSunrMondayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(2) ? Y.name() : N.name());
        sunrDAO.setSunrTuesdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(3) ? Y.name() : N.name());
        sunrDAO.setSunrWednesdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(4) ? Y.name() : N.name());
        sunrDAO.setSunrThursdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(5) ? Y.name() : N.name());
        sunrDAO.setSunrFridayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(6) ? Y.name() : N.name());
        if (StringUtils.isNotBlank(stunReqDTO.getNotes())) {
            sunrDAO.setSunrNote(GENERATECOMMENTS.apply(new String[]{
                    StringUtils.trimToEmpty(sunrDAO.getSunrNote()),
                    userService.getUserName(Long.valueOf(userId)),
                    dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                    StringUtils.trimToEmpty(stunReqDTO.getNotes())}));
        }
        if (stunReqDTO.getUnavailabilityId() == null) {
            sunrDAO.setSunrCreatedBy(userId);
            sunrDAO.setSunrCreatedUsing("UNAVAILABILITY_API_REQ");
        }
        sunrDAO.setSunrLastUpdBy(userId);
        sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_REQ");
        sunrRepository.save(sunrDAO);
        return "Request submitted successfully";
    }

    @Transactional
    public String approveUnavailability(Long sunrId, String userId, String roleId) {
        StaffUnavailabilityReqSunrDAO sunrDAO = sunrRepository.findById(sunrId).orElseThrow(() ->
                        new NotFoundException("Invalid Unavailablity ID:" + sunrId, UNAVAILABILITY_ID_NOT_FOUND));
        final List<ReseaIntvwerCalRsicDAO> rsicList = SUNR_TYPE_IND.R.getCode().equalsIgnoreCase(sunrDAO.getSunrTypeInd())
                ? rsicRepository.getRecurringInUseInterviewerCal(sunrDAO.getStfDAO().getUserDAO().getUserId(),
                        sunrDAO.getSunrStartDt(), sunrDAO.getSunrStartTime(),
                        sunrDAO.getSunrEndDt(), sunrDAO.getSunrEndTime(),
                        getDaysSelected(sunrDAO))
                : rsicRepository.getOneTimeInUseInterviewerCal(sunrDAO.getStfDAO().getUserDAO().getUserId(),
                        sunrDAO.getSunrStartDt(), sunrDAO.getSunrStartTime(),
                        sunrDAO.getSunrEndDt(), sunrDAO.getSunrEndTime());
        final boolean eventInUseOrStun = !CollectionUtils.isEmpty(rsicList);
        HashMap<String, List<String>> errorMap = sunrValidator.validateUnavailabilityApproval(eventInUseOrStun, sunrDAO, userId, roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Summary Request Failed", errorMap);
        }
        HashMap<String, List<DynamicErrorDTO>> dynmicErrorMap = processUnavailReq(sunrDAO.getSunrId(), userId, userId);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException(UNAVAILABLITY_APPROVE_FAILED.getDescription(), dynmicErrorMap);
        }
        sunrDAO.setSunrStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode()).get());
        sunrDAO.setApproverStfDAO(stfRepository.findByUserId(Long.valueOf(userId)));
        sunrDAO.setSunrLastUpdBy(userId);
        sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_APR");
        sunrRepository.save(sunrDAO);
        return "Request approoved successfully";
    }

    @Transactional
    public String rejectUnavailability(UnavailablityRejectReqDTO stunReqDTO, String userId, String roleId) {
        StaffUnavailabilityReqSunrDAO sunrDAO = sunrRepository.findById(stunReqDTO.getUnavailabilityId()).orElseThrow(() ->
                        new NotFoundException("Invalid Unavailablity ID:" + stunReqDTO.getUnavailabilityId(), UNAVAILABILITY_ID_NOT_FOUND));
        final HashMap<String, List<String>> errorMap = sunrValidator.validateUnavailabilityReject(stunReqDTO, sunrDAO, userId, roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Summary Request Failed", errorMap);
        }
        final Date systemDate = commonRepo.getCurrentTimestamp();

        sunrDAO.setSunrReasonTypeCdAlv(alvRepository.findById(stunReqDTO.getReason()).get());
        sunrDAO.setSunrStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.SUNR_STATUS_CD.REJECTED.getCode()).get());
        sunrDAO.setSunrStartDt(stunReqDTO.getStartDt());
        sunrDAO.setSunrEndDt(stunReqDTO.getEndDt());
        sunrDAO.setSunrStartTime(convert12hTo24h().apply(stunReqDTO.getStartTime()));
        sunrDAO.setSunrEndTime(convert12hTo24h().apply(stunReqDTO.getEndTime()));
        sunrDAO.setSunrMondayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(2) ? Y.name() : N.name());
        sunrDAO.setSunrTuesdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(3) ? Y.name() : N.name());
        sunrDAO.setSunrWednesdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(4) ? Y.name() : N.name());
        sunrDAO.setSunrThursdayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(5) ? Y.name() : N.name());
        sunrDAO.setSunrFridayInd(stunReqDTO.getDays() != null && stunReqDTO.getDays().contains(6) ? Y.name() : N.name());
        if (StringUtils.isNotBlank(stunReqDTO.getNotes())) {
            sunrDAO.setSunrNote(GENERATECOMMENTS.apply(new String[]{
                    StringUtils.trimToEmpty(sunrDAO.getSunrNote()),
                    userService.getUserName(Long.valueOf(userId)),
                    dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                    StringUtils.trimToEmpty(stunReqDTO.getNotes())}));
        }
        sunrDAO.setApproverStfDAO(stfRepository.findByUserId(Long.valueOf(userId)));
        sunrDAO.setSunrLastUpdBy(userId);
        sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_RJT");
        sunrRepository.save(sunrDAO);
        return "Request rejected successfully";
    }

    @Transactional
    public String withdrawUnavailability(UnavailablityWithdrawReqDTO stunReqDTO, String userId, String roleId) {
        StaffUnavailabilityReqSunrDAO sunrDAO = sunrRepository.findById(stunReqDTO.getUnavailabilityId()).orElseThrow(() ->
                        new NotFoundException("Invalid Unavailability ID:" + stunReqDTO.getUnavailabilityId(), UNAVAILABILITY_ID_NOT_FOUND));
        HashMap<String, List<String>> errorMap = sunrValidator.validateUnavailabilityWithdraw(stunReqDTO, sunrDAO, userId, roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Summary Request Failed", errorMap);
        }
        final Date systemDate = commonRepo.getCurrentTimestamp();
        String dateChangeNote = StringUtils.isNotBlank(stunReqDTO.getNotes())
            ? GENERATECOMMENTS.apply(new String[]{
                    StringUtils.trimToEmpty(sunrDAO.getSunrNote()),
                    userService.getUserName(Long.valueOf(userId)),
                    dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                    stunReqDTO.getNotes()})
            : GENERATECOMMENTS.apply(new String[]{
                    StringUtils.trimToEmpty(sunrDAO.getSunrNote()),
                    "SYSTEM",
                    dateToLocalDateTime.apply(systemDate).format(DATE_TIME_FORMATTER),
                    userService.getUserName(Long.valueOf(userId))
                            + " withdrew Unavailability Request"});

        if (ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode().equals(sunrDAO.getSunrStatusCdAlv().getAlvId())) {
            sunrDAO.setSunrNote(StringUtils.trimToEmpty(dateChangeNote));
            sunrDAO.setSunrLastUpdBy(userId);
            sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_WTH");
            sunrRepository.save(sunrDAO);
            entityManager.flush();
            HashMap<String, List<DynamicErrorDTO>> dynamicErrorMap = processWithdrawSunr(sunrDAO.getSunrId(),
                    stunReqDTO.getWithdrawDt() != null ? stunReqDTO.getWithdrawDt() : stringToDate.apply(DATE_01_01_2000),
                    StringUtils.isNotBlank(stunReqDTO.getWithdrawTime()) ? convert12hTo24h().apply(stunReqDTO.getWithdrawTime()) : "",
                    userId);
            if (!dynamicErrorMap.isEmpty()) {
                final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
                final List<String> errorParams = new ArrayList<>();
                errorEnums.add(UNAVAILABLITY_WITHDRAW_FAILED);
                ReseaUtilFunction.updateErrorMap(dynamicErrorMap, errorEnums, errorParams);
                throw new DynamicValidationException(UNAVAILABLITY_WITHDRAW_FAILED.getDescription(), dynamicErrorMap);
            }
        } else {
            if (stunReqDTO.getWithdrawDt() == null) {
                sunrDAO.setSunrStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.SUNR_STATUS_CD.WITHDRAWN.getCode()).get());
                sunrDAO.setSunrNote(StringUtils.trimToEmpty(dateChangeNote));
                sunrDAO.setSunrLastUpdBy(userId);
                sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_WTH");
                sunrRepository.save(sunrDAO);
            } else {
                if (ReseaConstants.SUNR_TYPE_IND.R.getCode().equals(sunrDAO.getSunrTypeInd())) {
                    sunrDAO.setSunrEndDt(localDateToDate.apply(dateToLocalDate.apply(stunReqDTO.getWithdrawDt()).minusDays(1)));
                } else {
                    sunrDAO.setSunrEndDt(stunReqDTO.getWithdrawDt());
                    if (StringUtils.isNotEmpty(stunReqDTO.getWithdrawTime())) {
                        sunrDAO.setSunrEndTime(convert12hTo24h().apply(stunReqDTO.getWithdrawTime()));
                    }
                }
                sunrDAO.setSunrNote(StringUtils.trimToEmpty(dateChangeNote));
                sunrDAO.setSunrLastUpdBy(userId);
                sunrDAO.setSunrLastUpdUsing("UNAVAILABILITY_API_WTH");
                sunrRepository.save(sunrDAO);
            }
        }
        return "Request withdrew successfully";
    }

    @Transactional
    private HashMap<String, List<DynamicErrorDTO>> processUnavailReq(Long sunrId, String userId, String pgmName) {
        boolean success = false;
        Map<String, Object> outParam = null;
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        try {
            //entityManager.flush();
            outParam = sunrRepository.processUnavailReq(sunrId,
                    stfRepository.findByUserId(Long.parseLong(userId)).getStfId(), pgmName);
            if(outParam != null) {
                Long spPoutNhlId = (Long) outParam.get("pout_nhl_id");
                Long spPoutSuccess = (Long) outParam.get("pout_success");
                if(null != spPoutNhlId && null != spPoutSuccess && spPoutSuccess != 0L) {
                    NhuisLogNhlDao nhlDao = nhlRepository.findById(spPoutNhlId).orElse(null);
                    if (nhlDao != null) {
                        if (nhlDao.getNhlErrTxt().contains("SUNR ID IS NULL or zero") ||
                                nhlDao.getNhlErrTxt().contains("SUNR ID does not exists") ||
                                nhlDao.getNhlErrTxt().contains("Get SUNR detail")) {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ID_INVALID);
                        } else if (nhlDao.getNhlErrTxt().contains("STUN already exists for the input SUNR")) {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ALREADY_EXISTS);
                        } else if (nhlDao.getNhlErrTxt().contains("Recurring SUNR start time > End time")) {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_STARTDT_ENDDT_INVALID);
                        } else if (nhlDao.getNhlErrTxt().contains("Insert one time STUN") ||
                                nhlDao.getNhlErrTxt().contains("Insert recurring STUN")) {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_STUN_ADMIN);
                            errorParams.add(String.valueOf(spPoutNhlId));
                        } else if (nhlDao.getNhlErrTxt().contains("Populate RSIC record") ||
                                nhlDao.getNhlErrTxt().contains("Create RSIC")) {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_RSIC_ADMIN);
                        } else {
                            errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ADMIN_NHL);
                            errorParams.add(String.valueOf(spPoutNhlId));
                        }
                    } else {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ADMIN);
                    }
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                }
            }
        }catch (Exception e) {
            log.error("Error in calling the stored procedure PROCESS_UNAVAIL_REQ associated with SUNR ID: "
                    + sunrId + " with error code: "+ outParam +"\n" + Arrays.toString(e.getStackTrace()));
            throw e;
        }
        return errorMap;
    }

    @Transactional
    private HashMap<String, List<DynamicErrorDTO>> processWithdrawSunr(Long sunrId, Date withdrawDt, String withdrawTime, String pgmName) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Map<String, Object> outParam = null;
        try {
            //entityManager.flush();
            outParam = sunrRepository.processWithdrawSunr(sunrId,  withdrawDt, withdrawTime, pgmName);
            if(outParam != null) {
                Long spPoutNhlId = (Long) outParam.get("pout_nhl_id");
                Long spPoutSuccess = (Long) outParam.get("pout_success");
                String spPoutErrMsg = StringUtils.trimToEmpty((String) outParam.get("pout_error_msg"));
                if (null != spPoutNhlId && null != spPoutSuccess && spPoutSuccess != 0L) {
                    if (spPoutErrMsg.contains("unavailability record not in approved status")) {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_STATUS_INVALID);
                    } else if (spPoutErrMsg.contains("Only future withdrwal allowed")) {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_FUTURE_DT);
                    } else if (spPoutErrMsg.contains("Withdrawal start date beyond end of unavailability period")) {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_DATE_INVALID);
                    } else if (spPoutErrMsg.contains("Can not change start/end time for withdrawal of a recurring unavailability")) {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_TIME_INVALID);
                    } else if (spPoutErrMsg.contains("Check overlapping first layer RSIC")) {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_OVERLAP);
                    } else {
                        errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ADMIN_NHL);
                        errorParams.add(String.valueOf(spPoutNhlId));
                    }
                    ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
                }
            } else {
                errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_ADMIN);
                ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
            }
        }catch (Exception e) {
            log.error("Error in calling the stored procedure WITHDRAW_UNAVAILABILITY associated with SUNR ID: "
                    + sunrId + " with error code: "+ outParam +"\n" + Arrays.toString(e.getStackTrace()));
            throw e;
        }
        return errorMap;
    }

    @Transactional
    public String checkStaffUnavailabilityForAvailableslot(Long eventId) {
        final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Invalid Event ID:" + eventId, EVENT_ID_NOT_FOUND));
        String staffUnavailability = ReseaConstants.INDICATOR.N.toString();
        if (INDICATOR_YES.equals(sunrRepository.checkStaffUnavailabilityForAvailableslot(eventId))) {
            staffUnavailability = ReseaConstants.INDICATOR.Y.toString();
        }
        return staffUnavailability;
    }
    
}
