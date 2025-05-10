package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.database.dao.ReseaCaseActivityRscaDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.dto.CaseActivityAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupEditReqDTO;
import com.ssi.ms.resea.dto.GenerateCorrespondanceDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.checkPastDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.ACTIVITY_TEMPLATE_ISS;
import static com.ssi.ms.resea.constant.ReseaConstants.ACTIVITY_TEMPLATE_TER;
import static com.ssi.ms.resea.constant.ReseaConstants.ACTIVITY_TEMPLATE_TRN;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.TIME_PATTERN_12H;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert12hTo24h;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaCaseSummaryValidator {

	public HashMap<String, List<String>> validateAddFollowup(CaseActivityFollowupAddReqDTO followupReqDTO,
															 ReseaCaseActivityRscaDAO rscaDAO,
															 String userId, String roleId, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		Long caseUserId = rscaDAO.getRscsDAO().getStfDAO().getUserDAO().getUserId();
		Long loginUserId = Long.parseLong(userId);
		Integer rolId = Integer.parseInt(roleId);
		if (caseUserId == null || (!caseUserId.equals(loginUserId)
				&& !ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId))) {
			errorEnums.add(ErrorMessageConstant.FollowUpErrorDetail.USR_ACCESS_ADD_INVALID);
		}
		if (!checkFutureDate.test(followupReqDTO.getFollowUpCompByDt(), systemDate)) {
			errorEnums.add(ErrorMessageConstant.FollowUpErrorDetail.FOLLOW_UP_COMP_BY_DT_FUTURE);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateEditFollowup(CaseActivityFollowupEditReqDTO followupReqDTO,
															  ReseaCaseActivityRscaDAO rscaDAO,
															  String userId, String roleId, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		Long caseUserId = rscaDAO.getRscsDAO().getStfDAO().getUserDAO().getUserId();
		Long loginUserId = Long.parseLong(userId);
		Integer rolId = Integer.parseInt(roleId);
		if (caseUserId == null || (!caseUserId.equals(loginUserId)
				&& !ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId))) {
			errorEnums.add(ErrorMessageConstant.FollowUpErrorDetail.USR_ACCESS_EDIT_INVALID);
		}

		if (INDICATOR_YES.equals(followupReqDTO.getComplete())) {
			if (followupReqDTO.getCompletionDt() == null) {
				errorEnums.add(ErrorMessageConstant.FollowUpErrorDetail.FOLLOW_UP_COMP_DT_MANDATORY);
			} else if (checkFutureDate.test(followupReqDTO.getCompletionDt(), systemDate)) {
				errorEnums.add(ErrorMessageConstant.FollowUpErrorDetail.FOLLOW_UP_COMP_DT_FUTURE);
			}
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

    public HashMap<String, List<String>> validateAddCaseActivity(CaseActivityAddReqDTO addActivityReqDTO,
																 ReseaCaseRscsDAO rscsDAO, String template,
																 String loginStfUsrId, String roleId,
																 Date systemDate, Date systemTimestamp) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final long loginUserId = Long.parseLong(loginStfUsrId);
		final int rolId = Integer.parseInt(roleId);
		if (rolId != ROL_RESEA_PROG_STAFF && rolId != ROL_LOCAL_OFFICE_MANAGER &&
				!(rolId == ROL_RESEA_CASE_MANAGER && loginUserId == rscsDAO.getStfDAO().getUserDAO().getUserId())) {
			if (rolId == ROL_RESEA_CASE_MANAGER) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.CASE_MANAGER_ACCESS_INVALID);
			} else {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.USER_ACCESS_INVALID);
			}
		}
		if (!TIME_PATTERN_12H.matcher(addActivityReqDTO.getAddTime()).matches()) {
			errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ADD_TIME_INVALID);
		} else {
			final Date activityAddDateTs = string24HToDate().apply(dateToString.apply(addActivityReqDTO.getAddDate()),
					convert12hTo24h().apply(addActivityReqDTO.getAddTime()));
			if (checkFutureDate.test(activityAddDateTs, systemTimestamp)) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ADD_DATE_TIME_FUTURE);
			} else if (checkPastDate.test(activityAddDateTs, rscsDAO.getRscsCreatedTs())) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ADD_DATE_TIME_PRIOR_CASE);
			}
		}
		if (ACTIVITY_TEMPLATE_TRN.equals(template)) {
			if (addActivityReqDTO.getTrainingDetails() == null) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_DETAIL_MANDATORY);
			} else {
				if (!StringUtils.hasLength(addActivityReqDTO.getTrainingDetails().getProgName())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_PROG_NAME_MANDATORY);
				}
				if (!StringUtils.hasLength(addActivityReqDTO.getTrainingDetails().getProviderName())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_PROVIDER_NAME_MANDATORY);
				}
				if (addActivityReqDTO.getTrainingDetails().getStartDate() == null) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_START_DT_MANDATORY);
				} else if (checkPastDate.test(addActivityReqDTO.getTrainingDetails().getStartDate(),
						DateUtils.truncate(rscsDAO.getRscsCreatedTs(), Calendar.DAY_OF_MONTH))) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_START_DT_PRIOR_CASE);
				}
				if (addActivityReqDTO.getTrainingDetails().getEndDate() == null) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_END_DT_MANDATORY);
				} else if (checkPastDate.test(addActivityReqDTO.getTrainingDetails().getEndDate(),
						addActivityReqDTO.getTrainingDetails().getStartDate())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TRAINING_END_DT_PRIOR_START_DT);
				}
			}
		} else if (ACTIVITY_TEMPLATE_TER.equals(template)) {
			if (addActivityReqDTO.getTerminationDetail() == null || addActivityReqDTO.getTerminationDetail() <= 0L) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.TERMINATION_DETAIL_MANDATORY);
			}
		} else if (ACTIVITY_TEMPLATE_ISS.equals(template)) {
			if (addActivityReqDTO.getIssueDetail() == null) {
				errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_DETAIL_MANDATORY);
			} else {
				if (addActivityReqDTO.getIssueDetail().getIssueSubType() == null || addActivityReqDTO.getTerminationDetail() <= 0L) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_SUB_TYPE_MANDATORY);
				}
				if (addActivityReqDTO.getIssueDetail().getStartDt() == null) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_START_DT_MANDATORY);
				} else if (checkPastDate.test(addActivityReqDTO.getIssueDetail().getStartDt(),
							DateUtils.truncate(rscsDAO.getRscsCreatedTs(), Calendar.DAY_OF_MONTH))) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_START_DT_PRIOR_CASE);
				} else if (checkFutureDate.test(addActivityReqDTO.getIssueDetail().getStartDt(),
								rscsDAO.getClmDAO().getClmBenYrEndDt())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_START_DT_BYE_DT);
				}
				if (addActivityReqDTO.getIssueDetail().getEndDt() == null) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_END_DT_MANDATORY);
				} else if (checkPastDate.test(addActivityReqDTO.getIssueDetail().getEndDt(),
						addActivityReqDTO.getIssueDetail().getStartDt())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.ISSUE_END_DT_PRIOR_START_DT);
				}
			}
		}
		if (addActivityReqDTO.getCorrespondence() != null) {
			GenerateCorrespondanceDTO corrDTO = addActivityReqDTO.getCorrespondence();
			if (!StringUtils.hasLength(corrDTO.getGenerateNotice()) || INDICATOR_NO.equals(corrDTO.getGenerateNotice())) {
				if (StringUtils.hasLength(corrDTO.getCustomText()) || StringUtils.hasLength(corrDTO.getStandardText())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.CORR_NOT_SELECTED);
				}
			} else if (INDICATOR_YES.equals(corrDTO.getGenerateNotice())) {
				if (!StringUtils.hasLength(corrDTO.getStandardText())) {
					errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.CORR_STANDARD_TXT_MANDATORY);
				}
			}
		}
		if (!StringUtils.hasLength(addActivityReqDTO.getStaffNotes()) && INDICATOR_YES.equals(addActivityReqDTO.getViewNoteNonReseaInd())) {
			errorEnums.add(ErrorMessageConstant.AddActivityErrorDTODetail.STAFF_NOTES_NOT_PRESENT);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
    }
}
