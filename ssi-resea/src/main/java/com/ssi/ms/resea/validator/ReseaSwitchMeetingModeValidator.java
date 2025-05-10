package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.ReseaSwitchMeetingModeReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.validator.ReseaCommonValidator.validateCreateIssue;

/**
 * {@code ReseaSwitchMeetingModeValidator} is a validation component in the application responsible for
 * validating the Switch Meeting Mode functionality.
 *
 * @author Anand
 */

@Component
@AllArgsConstructor
@Slf4j
public class ReseaSwitchMeetingModeValidator {

	public HashMap<String, List<String>> validate(ReseaSwitchMeetingModeReqDTO switchMtgModeReqDTO, Date clmByeDt, Date systemDate, String roleId, boolean isInitialAppt) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (Long.valueOf(ReseaConstants.REASONS_SWITCH_MEETING_MODE_OTHERS).equals(switchMtgModeReqDTO.getReasonForSwitchMeetingMode())) {
			if(StringUtils.isBlank(switchMtgModeReqDTO.getMeetingModeChgReasonTxt())) {
				errorEnums.add(ErrorMessageConstant.SwitchMeetingModeErrorDetail.OTHERS_TEXT_FOR_SWITCH_MEETING_MODE_MANDATORY);
			}
		}
		int rolId = Integer.parseInt(roleId);

		if (isInitialAppt && rolId == ROL_LOCAL_OFFICE_MANAGER &&
				!ReseaConstants.MEETING_MODE.IN_PERSON.getCode().equalsIgnoreCase(switchMtgModeReqDTO.getCurrentMeetingMode())) {
			errorEnums.add(ErrorMessageConstant.SwitchMeetingModeErrorDetail.SWITCH_MEETING_MODE_ROLE_INVALID);
		}
		validateCreateIssue(switchMtgModeReqDTO.getIssuesDTOList(), clmByeDt, errorEnums);
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

}
