package com.ssi.ms.resea.validator;


import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateTimeToDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_CURR_APP_CLSOUT_TIME;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_USAGE_TO_RSCS_STAGE_MAP;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaCalendarEventValidator {
	@Autowired
	ParameterParRepository parRepository;

	public HashMap<String, List<String>> validateSaveAvailableAppointment(AvaliableApptSaveReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
																		  boolean lateSchedule, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();

		if (rsicDAO.getRsicCalEventTypeCdAlv().getAlvId() != RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		} else {
			final Date eventDate = localDateTimeToDate.apply(
					dateToLocalDateTime.apply(string24HToDate().apply(dateToString
									.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventStTime()))
							.plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue()));
			if (eventDate != null && eventDate.before(Calendar.getInstance().getTime())) {
				errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
			}
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

	public HashMap<String, List<String>> validateReopen(ReseaIntvwerCalRsicDAO rsicDAO, String roleId, Date systemDate, Long reopenExpiryInDays) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (roleId != null) {
			int rolId = Integer.parseInt(roleId);
			if (rolId != ROL_RESEA_PROG_STAFF) {
				errorEnums.add(ErrorMessageConstant.CalendarEventErrorDetail.APPOINTMENT_REOPEN_INVALID);
			}
		} else {
			errorEnums.add(ErrorMessageConstant.CalendarEventErrorDetail.APPOINTMENT_REOPEN_INVALID);
		}
		if (dateToLocalDate.apply(rsicDAO.getRsicCalEventDt()).plusDays(reopenExpiryInDays)
				.isBefore(dateToLocalDate.apply(systemDate))) {
			errorEnums.add(ErrorMessageConstant.CalendarEventErrorDetail.APPOINTMENT_REOPEN_EXPIRED);
		} else if (!RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())
				.equals(rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId())) {
			errorEnums.add(ErrorMessageConstant.CalendarEventErrorDetail.APPOINTMENT_REOPEN_STAGE_EXPIRED);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
}
