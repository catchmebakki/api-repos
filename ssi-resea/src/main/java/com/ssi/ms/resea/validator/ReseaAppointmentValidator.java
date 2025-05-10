package com.ssi.ms.resea.validator;


import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.dto.ReseaSchApptCaseMgrAvailListReqDTO;
import com.ssi.ms.resea.dto.ScheduleInitialApptSaveReqDTO;
import com.ssi.ms.resea.dto.WaitlistClearReqDTO;
import com.ssi.ms.resea.dto.WaitlistReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateTimeToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_CAL_LAPSE_TIME;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaAppointmentValidator {
	@Autowired
	ParameterParRepository parRepository;

	public HashMap<String, List<String>> validateSaveAvailableAppointment(AvaliableApptSaveReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
																		  boolean lateSchedule, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();

		if (rsicDAO.getRsicCalEventTypeCdAlv().getAlvId() != RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		}
		final long grace_minute = 1L, grace_seconds = 55L;
		final Date eventDate = localDateTimeToDate.apply(
				dateToLocalDateTime.apply(string24HToDate().apply(dateToString
								.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventStTime()))
						.plusMinutes(parRepository.findByParShortName(PAR_RESEA_CAL_LAPSE_TIME).getParNumericValue()+grace_minute).plusSeconds(grace_seconds));
		if (eventDate != null && eventDate.before(systemDate)) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		}

		if(apptReqDTO.getClaimId() == null) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.CLAIMANT_MANDATORY);
		}
		if (!INDICATOR_YES.equals(apptReqDTO.getInformedCmtInd())) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.INFORM_CLAIMANT_MANDATORY);
		}
		if (CollectionUtils.isEmpty(apptReqDTO.getInformedConveyedBy())) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.INFORMATION_CONVEYED_MANDATORY);
		}
		if (lateSchedule && StringUtils.isBlank(apptReqDTO.getLateStaffNote())) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.LATE_NOTES_MANDATORY);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateGetCaseMgrsAvailListForSchAppt(ReseaSchApptCaseMgrAvailListReqDTO reseaSchApptCaseMgrAvailListReqDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();

		if(StringUtils.isEmpty(reseaSchApptCaseMgrAvailListReqDTO.getClmLofInd())) {
			errorEnums.add(ErrorMessageConstant.ScheduleApptErrorDetail.LOCAL_OFFICE_OPTION_NOT_SELECTED);
		}
		if (INDICATOR_YES.equals(reseaSchApptCaseMgrAvailListReqDTO.getClmLofInd())) {
			if(Stream.of(reseaSchApptCaseMgrAvailListReqDTO.getMeetingModeInperson(), reseaSchApptCaseMgrAvailListReqDTO.getMeetingModeVirtual()).allMatch(StringUtils::isBlank)) {
				errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.MEETING_MODE_NOT_CHECKED);
			}
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}


	public HashMap<String, List<String>> validateSaveSchInitialAppointment(ScheduleInitialApptSaveReqDTO schInitialApptSaveReqDTO,
																		   ReseaIntvwerCalRsicDAO rsicDAO,
																		   boolean lateSchedule, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();

		if (rsicDAO.getRsicCalEventTypeCdAlv().getAlvId() != RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		}
		final long grace_minute = 1L, grace_seconds = 55L;
		final Date eventDate = localDateTimeToDate.apply(
				dateToLocalDateTime.apply(string24HToDate().apply(dateToString
								.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventStTime()))
						.plusMinutes(parRepository.findByParShortName(PAR_RESEA_CAL_LAPSE_TIME).getParNumericValue()+grace_minute).plusSeconds(grace_seconds));
		if (eventDate != null && eventDate.before(systemDate)) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		}

		if(schInitialApptSaveReqDTO.getClaimId() == null) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.CLAIMANT_MANDATORY);
		}
		/*if (lateSchedule && StringUtils.isBlank(schInitialApptSaveReqDTO.getLateStaffNote())) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.LATE_NOTES_MANDATORY);
		}*/
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateApplyWaitlist(WaitlistReqDTO waitlistReqDTO,
															   ReseaIntvwerCalRsicDAO rsicDAO,
															   ReseaCaseRscsDAO rscsDAO,
															   String staffUserId,
															   String roleId,
															   Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (waitlistReqDTO.getCaseId() == null && waitlistReqDTO.getEventId() == null) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.CASE_EVENT_MANDATORY);
		} else {
			if (rsicDAO != null && !checkFutureDate.test(rsicDAO.getRsicCalEventDt(), systemDate)) {
				errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.CASE_EVENT_MANDATORY);
			}
		}
		if (INDICATOR_YES.equalsIgnoreCase(rsicDAO.getRscsDAO().getRscsOnWaitlistInd())) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.APPOINTMENT_DT_IN_WAITLIST);
		}
		if (ROL_RESEA_PROG_STAFF != rolId && ROL_LOCAL_OFFICE_MANAGER != rolId
				&& Long.parseLong(staffUserId) != rscsDAO.getStfDAO().getUserDAO().getUserId()) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.USR_ACCESS_APPLY_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateClearWaitlist(WaitlistClearReqDTO waitlistReqDTO,
															   ReseaIntvwerCalRsicDAO rsicDAO,
															   ReseaCaseRscsDAO rscsDAO,
															   String staffUserId,
															   String roleId,
															   Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (waitlistReqDTO.getCaseId() == null && waitlistReqDTO.getEventId() == null) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.CASE_EVENT_MANDATORY);
		} else {
			if (rsicDAO != null && !checkFutureDate.test(rsicDAO.getRsicCalEventDt(), systemDate)) {
				errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.APPOINTMENT_DT_CLEAR_FUTURE);
			}
		}
		if (!INDICATOR_YES.equalsIgnoreCase(rscsDAO.getRscsOnWaitlistInd())) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.APPOINTMENT_DT_NO_WAITLIST);
		}
		if (ROL_RESEA_PROG_STAFF != rolId && ROL_LOCAL_OFFICE_MANAGER != rolId
				&& Long.parseLong(staffUserId) != rscsDAO.getStfDAO().getUserDAO().getUserId()) {
			errorEnums.add(ErrorMessageConstant.WaitlistErrorDetail.USR_ACCESS_CLEAR_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
}
