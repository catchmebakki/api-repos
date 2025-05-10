package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsReqDTO;
import com.ssi.ms.resea.dto.ReseaRescheduleSaveReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.ssi.ms.resea.validator.ReseaCommonValidator.validateCreateIssue;

/**
 * {@code ReseaRescheduleRequestValidator} is a validation component in the application responsible for
 * validating the Rescheduling functionality.
 *
 * @author Anand
 */

@Component
@AllArgsConstructor
@Slf4j
public class ReseaRescheduleRequestValidator {

	/**
	 * Validation to fetch the list of available slots
	 *
	 * @param reseaRescheduleGetAvailableSlotsReqDTO
	 * @return
	 */
	public HashMap<String, List<String>> validateGetAvailableSlotsForReschedule(ReseaRescheduleGetAvailableSlotsReqDTO reseaRescheduleGetAvailableSlotsReqDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if(Stream.of(reseaRescheduleGetAvailableSlotsReqDTO.getMeetingModeInperson(), reseaRescheduleGetAvailableSlotsReqDTO.getMeetingModeVirtual()).allMatch(StringUtils::isBlank)) {
			errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.MEETING_MODE_NOT_CHECKED);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	/**
	 * Validation for SAVE Rescheduling details
	 *
	 * @param rschSaveReqDTO
	 * @param systemDate
	 * @return
	 */
	public HashMap<String, List<String>> validateSaveRescheduleRequest(ReseaRescheduleSaveReqDTO rschSaveReqDTO, Date clmByeDt, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (Stream.of(rschSaveReqDTO.getSelectedPrefMtgModeInPerson(), rschSaveReqDTO.getSelectedPrefMtgModeVirtual()).allMatch(StringUtils::isBlank)) {
			errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.MEETING_MODE_NOT_CHECKED);
		}
		if (ReseaConstants.INDICATOR.Y.toString().equalsIgnoreCase(rschSaveReqDTO.getNonComplianceInd())) {
			if(StringUtils.isBlank(rschSaveReqDTO.getLateSchedulingReason())) {
				errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.LATE_SCHEDULING_REASON_EMPTY);
			}
		}
		validateReasonForReschedulingFields(rschSaveReqDTO, errorEnums);
		validateCreateIssue(rschSaveReqDTO.getIssuesDTOList(), clmByeDt, errorEnums);
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}



	private static void validateReasonForReschedulingFields(ReseaRescheduleSaveReqDTO reseaRescheduleSaveReqDTO, List<ReseaErrorEnum> errorEnums) {
		List<Long> doctorAppointmentCodes = Arrays.asList(
				ReseaAlvEnumConstant.RsrsReschReasonCd.DOCTORS_APPOINTMENT_DEPENDENT.getCode().longValue(),
				ReseaAlvEnumConstant.RsrsReschReasonCd.DOCTORS_APPOINTMENT_SELF.getCode().longValue() );
		List<Long> jobInterviewCodes = List.of(ReseaAlvEnumConstant.RsrsReschReasonCd.JOB_INTERVIEW.getCode().longValue());
		if (doctorAppointmentCodes.contains(reseaRescheduleSaveReqDTO.getReasonForRescheduling().longValue())) {
			if(null == reseaRescheduleSaveReqDTO.getAppointmentDate()) {
				errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.APPOINTMENT_DATE_MANDATORY);
			}
			validateField(reseaRescheduleSaveReqDTO.getAppointmentTime(), ErrorMessageConstant.RescheduleRequestErrorDetail.APPOINTMENT_TIME_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getEntityCity(), ErrorMessageConstant.RescheduleRequestErrorDetail.CITY_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getEntityState(), ErrorMessageConstant.RescheduleRequestErrorDetail.STATE_MANDATORY, errorEnums);
		}
		else if (jobInterviewCodes.contains(reseaRescheduleSaveReqDTO.getReasonForRescheduling().longValue())) {
			if(null == reseaRescheduleSaveReqDTO.getAppointmentDate()) {
				errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.APPOINTMENT_DATE_MANDATORY);
			}
			validateField(reseaRescheduleSaveReqDTO.getAppointmentTime(), ErrorMessageConstant.RescheduleRequestErrorDetail.APPOINTMENT_TIME_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getEntityName(), ErrorMessageConstant.RescheduleRequestErrorDetail.EMPLOYER_NAME_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getJobTitle(), ErrorMessageConstant.RescheduleRequestErrorDetail.JOB_TITLE_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getPartFullTimeInd(), ErrorMessageConstant.RescheduleRequestErrorDetail.PART_FULL_TIME_MANDATORY, errorEnums);
			validateField(reseaRescheduleSaveReqDTO.getEntityTeleNumber(), ErrorMessageConstant.RescheduleRequestErrorDetail.EMP_TELEPHONE_MANDATORY, errorEnums);
			if(null != reseaRescheduleSaveReqDTO.getEntityTeleNumber() && reseaRescheduleSaveReqDTO.getEntityTeleNumber().length() > 10) {
				errorEnums.add(ErrorMessageConstant.RescheduleRequestErrorDetail.LATE_SCHEDULING_REASON_EMPTY);
			}
			//validateField(reseaRescheduleSaveReqDTO.getEntityCity(), ErrorMessageConstant.RescheduleRequestErrorDetail.CITY_MANDATORY, errorEnums);
			//validateField(reseaRescheduleSaveReqDTO.getEntityState(), ErrorMessageConstant.RescheduleRequestErrorDetail.STATE_MANDATORY, errorEnums);
		}
	}

	static void validateField(String field, ErrorMessageConstant.RescheduleRequestErrorDetail errorDetail,  List<ReseaErrorEnum> errorEnums) {
		if (StringUtils.isBlank(field)) {
			errorEnums.add(errorDetail);
		}
	}

}
