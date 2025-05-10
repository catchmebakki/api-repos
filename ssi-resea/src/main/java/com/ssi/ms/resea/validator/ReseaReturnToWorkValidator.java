package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.ReturnToWorkReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.NotNull;


import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

/**
 * {@code ReseaReturnToWorkValidator} is a validation component in the application responsible for
 * validating the RTW functionality.
 *
 * @author Anand
 */

@Component
@AllArgsConstructor
@Slf4j
public class ReseaReturnToWorkValidator {

	public HashMap<String, List<String>> validateReturnToWorkDetails(@NotNull ReturnToWorkReqDTO returnToWorkReqDTO, Date systemDate, Long parNumericValue) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		LocalDate sysDatePlusRtwFutDays = null;

		if(null != returnToWorkReqDTO.getEmploymentStartDt()) {
			sysDatePlusRtwFutDays =  dateToLocalDate.apply(systemDate).plusDays(parNumericValue);
			if(returnToWorkReqDTO.getEmploymentStartDt().isAfter(sysDatePlusRtwFutDays)) {
				errorEnums.add(ErrorMessageConstant.ReturnToWorkErrorDetail.EMP_ST_DATE_AFTER_RTW_FUT_DAYS);
			}
		}
		/*if(localDateToDate.apply(returnToWorkReqDTO.getEmploymentStartDt()).after(systemDate)) {
			validateJMSItemsForEmpFutureDt(returnToWorkReqDTO, errorEnums);
		}
		else {
			validateJMSItemsForEmpPastDt(returnToWorkReqDTO, errorEnums);
		}*/
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public List<ReseaErrorEnum>  validateJMSItemsForEmpFutureDt(@NotNull ReturnToWorkReqDTO rtwReqDTO, List<ReseaErrorEnum> errorEnums) {
		List<String> checkedJmsCompletedCheckListItems = Arrays.asList(rtwReqDTO.getJms890Ind(), rtwReqDTO.getJmsReferralInd(),
				rtwReqDTO.getJmsCloseGoalsInd(), rtwReqDTO.getJmsCloseIEPInd(),
				rtwReqDTO.getJmsCaseNotesInd(), rtwReqDTO.getJmsResumeOffInd(), rtwReqDTO.getEpChecklistUploadInd());
		boolean containsY = checkedJmsCompletedCheckListItems.stream().anyMatch(item -> item.contains("Y"));
		if(containsY) {
			errorEnums.add(ErrorMessageConstant.ReturnToWorkErrorDetail.FURUTE_DATE_JMS_CHECKLIST_COMPLETED_CHECKED_OFF);
		}
		return errorEnums;
	}

	public List<ReseaErrorEnum> validateJMSItemsForEmpPastDt(@NotNull ReturnToWorkReqDTO rtwReqDTO, List<ReseaErrorEnum> errorEnums ) {
		if(StringUtils.equals(ReseaConstants.INDICATOR.N.toString(), rtwReqDTO.getJms890Ind())
				&& StringUtils.equals(ReseaConstants.INDICATOR.N.toString(), rtwReqDTO.getJmsReferralInd())) {
			errorEnums.add(ErrorMessageConstant.ReturnToWorkErrorDetail.NON_DIRECT_PLACEMENT_AND_JMS_REFERRAL_RECORDED_EMPTY);
		}
		List<String> checkedJmsCompletedCheckListItems = Arrays.asList(rtwReqDTO.getJmsCloseGoalsInd(), rtwReqDTO.getJmsCloseIEPInd(),
				rtwReqDTO.getJmsCaseNotesInd(), rtwReqDTO.getJmsResumeOffInd(), rtwReqDTO.getEpChecklistUploadInd());
		boolean containsN = checkedJmsCompletedCheckListItems.stream().anyMatch(item -> item.contains("N"));
		if(containsN) {
			errorEnums.add(ErrorMessageConstant.ReturnToWorkErrorDetail.PAST_DATE_JMS_CHECKLIST_COMPLETED_NOT_CHECKED_OFF);
		}
		return errorEnums;
	}

}
