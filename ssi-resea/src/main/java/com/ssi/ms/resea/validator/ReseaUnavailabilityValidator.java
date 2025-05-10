package com.ssi.ms.resea.validator;


import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.StaffUnavailabilityReqSunrDAO;
import com.ssi.ms.resea.database.repository.StaffUnavailabilityReqSunrRepository;
import com.ssi.ms.resea.dto.UnavailablityApproveReqDTO;
import com.ssi.ms.resea.dto.UnavailablityRejectReqDTO;
import com.ssi.ms.resea.dto.UnavailablityRequestReqDTO;
import com.ssi.ms.resea.dto.UnavailablitySummaryReqDTO;
import com.ssi.ms.resea.dto.UnavailablityWithdrawReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_NOTE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABILITY_START_TIME_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REQUEST_ACCESS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REQUEST_ALREADY_EXISTS;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REQUEST_DATE_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REQUEST_EVENT_INVALID;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert12hTo24h;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaUnavailabilityValidator {

	@Autowired
	ParameterParRepository parRepository;
	@Autowired
	StaffUnavailabilityReqSunrRepository sunrRepository;

	private void commonDateValidations(List<ReseaErrorEnum> errorEnums, Date startDt, Date endDt) {
		if (startDt.compareTo(endDt) > 0) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_STARTDT_ENDDT_INVALID);
		}
	}
	public HashMap<String, List<String>> validateSummaryRequest(UnavailablitySummaryReqDTO stunReqDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (stunReqDTO.getUserId() == null) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.SUMMARY_CASE_MANAGER_ID_MANDATORY);
		}
		if (stunReqDTO.getStartDt() == null) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.SUMMARY_START_DT_MANDATORY);
		} else if (stunReqDTO.getEndDt() != null && stunReqDTO.getStartDt().compareTo(stunReqDTO.getEndDt()) > 0) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_STARTDT_ENDDT_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateUnavailabilityRequest(Long sunrId,
																	   UnavailablityRequestReqDTO stunReqDTO,
																	   boolean eventInUseOrStun,
																	   String userId, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		final long usrId = Long.parseLong(userId);
		commonDateValidations(errorEnums, stunReqDTO.getStartDt(), stunReqDTO.getEndDt());
		if (string24HToDate().apply(dateToString.apply(stunReqDTO.getStartDt()), convert12hTo24h().apply(stunReqDTO.getStartTime()))
				.compareTo(parRepository.getCurrentTimestamp()) < 0) {
			errorEnums.add(UNAVAILABLITY_REQUEST_DATE_INVALID);
		}
		if (usrId != stunReqDTO.getUserId() && !ROL_RESEA_PROG_STAFF.equals(rolId) && !ROL_LOCAL_OFFICE_MANAGER.equals(rolId)) {
			errorEnums.add(UNAVAILABLITY_REQUEST_ACCESS_INVALID);
		}
		if (eventInUseOrStun) {
			errorEnums.add(UNAVAILABLITY_REQUEST_EVENT_INVALID);
		}
		if (stunReqDTO.getUnavailabilityId() == null && StringUtils.isEmpty(stunReqDTO.getNotes())) {
			errorEnums.add(UNAVAILABILITY_NOTE_MANDATORY);
		} else if (usrId != stunReqDTO.getUserId() && StringUtils.isEmpty(stunReqDTO.getNotes())) {
			errorEnums.add(UNAVAILABILITY_NOTE_MANDATORY);
		}
		/*if(stunReqDTO.isRecurring()) {
            final List<Long> existingSunrList = sunrRepository.checkOverlappingRecurringSunrExists(
					sunrId != null ? sunrId: 0L, stunReqDTO.getUserId(),
                    stunReqDTO.getStartDt(), stunReqDTO.getStartTime(),
                    stunReqDTO.getEndDt(), stunReqDTO.getEndTime(),
                    ReseaConstants.SUNR_TYPE_IND.O.getCode(), ReseaConstants.SUNR_TYPE_IND.R.getCode(),
                    stunReqDTO.getDays(),
					List.of(ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode(),
							ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode()));
			if (!existingSunrList.isEmpty()) {
				errorEnums.add(UNAVAILABLITY_REQUEST_ALREADY_EXISTS);
			}
        } else if (!sunrRepository.checkOverlappingOneTimeSunrExists(
				sunrId != null ? sunrId: 0L, stunReqDTO.getUserId(),
				stunReqDTO.getStartDt(), stunReqDTO.getStartTime(),
				stunReqDTO.getEndDt(), stunReqDTO.getEndTime(),
				List.of(ReseaConstants.SUNR_TYPE_IND.R.getCode(), ReseaConstants.SUNR_TYPE_IND.O.getCode())).isEmpty()) {
			errorEnums.add(UNAVAILABLITY_REQUEST_ALREADY_EXISTS);
		}*/
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateUnavailabilityApproval(boolean eventInUseOrStun,
																		StaffUnavailabilityReqSunrDAO sunrDAO,
																		String userId, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		final long usrId = Long.parseLong(userId);
		commonDateValidations(errorEnums, sunrDAO.getSunrStartDt(), sunrDAO.getSunrEndDt());
		if (!ROL_RESEA_PROG_STAFF.equals(rolId) && !ROL_LOCAL_OFFICE_MANAGER.equals(rolId)) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_APPROVE_ACCESS_INVALID);
		}
		if (!ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode().equals(sunrDAO.getSunrStatusCdAlv().getAlvId())) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_APPROVE_STATUS_INVALID);
		}
		if (eventInUseOrStun) {
			errorEnums.add(UNAVAILABLITY_REQUEST_EVENT_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	private boolean recurringDayChanges(UnavailablityApproveReqDTO stunReqDTO, StaffUnavailabilityReqSunrDAO sunrDAO){
		if (ReseaConstants.SUNR_TYPE_IND.R.getCode().equals(sunrDAO.getSunrTypeInd())) {
			for (Integer day: stunReqDTO.getDays()) {
				if(!switch ((int) day.longValue()) {
					case 2 -> INDICATOR_NO.equals(sunrDAO.getSunrMondayInd());
					case 3 -> INDICATOR_NO.equals(sunrDAO.getSunrTuesdayInd());
					case 4 -> INDICATOR_NO.equals(sunrDAO.getSunrWednesdayInd());
					case 5 -> INDICATOR_NO.equals(sunrDAO.getSunrThursdayInd());
					case 6 -> INDICATOR_NO.equals(sunrDAO.getSunrFridayInd());
					default -> true;
				}) {
					return true;
				}
			}
		}
		return false;
	}
	public HashMap<String, List<String>> validateUnavailabilityReject(UnavailablityRejectReqDTO stunReqDTO,
																	  StaffUnavailabilityReqSunrDAO sunrDAO,
																	  String userId, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		final long usrId = Long.parseLong(userId);
		commonDateValidations(errorEnums, stunReqDTO.getStartDt(), stunReqDTO.getEndDt());
		if (!ROL_RESEA_PROG_STAFF.equals(rolId) && !ROL_LOCAL_OFFICE_MANAGER.equals(rolId)) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REJECT_ACCESS_INVALID);
		}
		if (!ReseaAlvEnumConstant.SUNR_STATUS_CD.REQUESTED.getCode().equals(sunrDAO.getSunrStatusCdAlv().getAlvId())) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_REJECT_STATUS_INVALID);
		}
		if(StringUtils.isEmpty(stunReqDTO.getNotes())) {
			errorEnums.add(UNAVAILABILITY_NOTE_MANDATORY);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateUnavailabilityWithdraw(UnavailablityWithdrawReqDTO stunReqDTO,
																		StaffUnavailabilityReqSunrDAO sunrDAO,
																		String userId, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		final long usrId = Long.parseLong(userId);
		final Date sysTimestamp = parRepository.getCurrentTimestamp();
		if (!ROL_RESEA_PROG_STAFF.equals(rolId) && !ROL_LOCAL_OFFICE_MANAGER.equals(rolId) &&
				!(ROL_RESEA_CASE_MANAGER.equals(rolId) && sunrDAO.getStfDAO().getUserDAO().getUserId() == usrId)) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_ACCESS_INVALID);
		}
		if (stunReqDTO.getWithdrawDt() != null) {
			final LocalDate withdrawDt = dateToLocalDate.apply(stunReqDTO.getWithdrawDt());
			if (withdrawDt.isAfter(dateToLocalDate.apply(sunrDAO.getSunrEndDt())) ||
					withdrawDt.isBefore(dateToLocalDate.apply(sunrDAO.getSunrStartDt()))) {
				errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_DATE_INVALID);
			}
			if (StringUtils.isEmpty(stunReqDTO.getNotes())) {
				errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_NOTE_MANDATORY);
			}
			if (stunReqDTO.getWithdrawDt().compareTo(sunrDAO.getSunrStartDt()) >= 0) {
				final String withdrawTime = StringUtils.isNotBlank(stunReqDTO.getWithdrawTime()) ? stunReqDTO.getWithdrawTime() : "00:00";
				final Date withdrawDateTime = string24HToDate().apply(dateToString.apply(stunReqDTO.getWithdrawDt()), withdrawTime);
				if (ReseaConstants.SUNR_TYPE_IND.O.getCode().equalsIgnoreCase(sunrDAO.getSunrTypeInd())) {
					if (!checkFutureDate.test(withdrawDateTime, sysTimestamp)) {
						errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_DATE_FUTURE);
					}
				} else {
					if (StringUtils.isNotBlank(stunReqDTO.getWithdrawTime()) && !checkFutureDate.test(withdrawDateTime, sysTimestamp)) {
						errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_DATE_FUTURE);
					} else if (StringUtils.isBlank(stunReqDTO.getWithdrawTime()) && !checkFutureDate.test(string24HToDate()
							.apply(dateToString.apply(stunReqDTO.getWithdrawDt()), sunrDAO.getSunrStartTime()), sysTimestamp)) {
						errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_DATE_FUTURE);
					}
				}
			}
		} else if (string24HToDate().apply(dateToString.apply(sunrDAO.getSunrStartDt()), sunrDAO.getSunrStartTime())
				.compareTo(sysTimestamp) < 0) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_INVALID);
		}
		/*if (!ReseaAlvEnumConstant.SUNR_STATUS_CD.APPROVED.getCode().equals(sunrDAO.getSunrStatusCdAlv().getAlvId())) {
			errorEnums.add(ErrorMessageConstant.StaffUnavailabilityErrorDetail.UNAVAILABLITY_WITHDRAW_STATUS_INVALID);
		}*/
		if(StringUtils.isEmpty(stunReqDTO.getNotes())) {
			errorEnums.add(UNAVAILABILITY_NOTE_MANDATORY);
		}
		if (ReseaConstants.SUNR_TYPE_IND.O.getCode().equals(sunrDAO.getSunrTypeInd())
				&& stunReqDTO.getWithdrawDt() != null
				&& StringUtils.isEmpty(stunReqDTO.getWithdrawTime())) {
			errorEnums.add(UNAVAILABILITY_START_TIME_MANDATORY);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
}
