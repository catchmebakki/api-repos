package com.ssi.ms.resea.validator;


import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.IssueDecisionDecDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwDetRsidDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.repository.IssueDecisionDecRepository;
import com.ssi.ms.resea.database.repository.WeeklyWorkSearchWwsRepository;
import com.ssi.ms.resea.dto.AppointmentReqDTO;
import com.ssi.ms.resea.dto.IssuesDTO;
import com.ssi.ms.resea.dto.JobReferralDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.InterviewErrorDTODetail.OTHER_ISSUE_DUPLICATE;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.InterviewErrorDTODetail.WORK_SREACH_ISSUE_DUPLICATE;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_CURR_APP_CLSOUT_TIME;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_TERMINATED_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_USAGE_TO_RSCS_STAGE_MAP;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;
import static com.ssi.ms.resea.validator.ReseaCommonValidator.validateCreateIssue;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaInterviewValidator {

	@Autowired
	WeeklyWorkSearchWwsRepository wwsRepository;
	@Autowired
	ParameterParRepository parRepository;
	@Autowired
	IssueDecisionDecRepository decRepository;

	private void commonValidation(ReseaIntvwerCalRsicDAO rsicDAO,
								  String userId, String roleId,
								  List<ReseaErrorEnum> errorEnums) {
		int rolId = Integer.parseInt(roleId);
		long usrId = Long.parseLong(userId);
		if (rsicDAO.getRsisDAO() != null
				&& rsicDAO.getRsisDAO().getStfDAO().getUserDAO().getUserId() != usrId
				&& !ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.USR_ACCESS_INVALID);
		}
		if (rsicDAO.getRscsDAO() == null) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.EVENT_CASE_INVALID);
		} else if (RSCS_STAGE_TERMINATED_APPT != rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId()
				&& (!RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId())
					.equals(rsicDAO.getRscsDAO().getRscsStageCdALV().getAlvId()))) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.CASE_STAGE_INVALID);
		}
	}

	private void checkIssueDuplicates(AppointmentReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
									  List<ReseaErrorEnum> errorEnums, List<String> errorParams) {
		if (!CollectionUtils.isEmpty(apptReqDTO.getWorkSearchIssues())) {
			for (String ccaDt : apptReqDTO.getWorkSearchIssues().keySet()) {
				final Date ccaDate = stringToDate.apply(ccaDt);
				List<IssueDecisionDecDAO> decDAO = decRepository.checkIssueExists(rsicDAO.getClaimDAO().getClmId(),
						apptReqDTO.getWorkSearchIssues().get(ccaDt),
						getPrevSundayDate.apply(ccaDate),
						ccaDate);
				if(!decDAO.isEmpty()) {
					errorEnums.add(WORK_SREACH_ISSUE_DUPLICATE);
					errorParams.add(decDAO.get(0).getNmiDAO().getNmiShortDescTxt());
					errorParams.add(ccaDt);
				}
			}
		}

		if (!CollectionUtils.isEmpty(apptReqDTO.getOtherIssues())) {
			for (IssuesDTO issuesDTO : apptReqDTO.getOtherIssues()) {
				if (issuesDTO.getOtherIssueId() == null || issuesDTO.getOtherIssueId() == 0L) {
					List<IssueDecisionDecDAO> decDAO = decRepository.checkIssueExists(rsicDAO.getClaimDAO().getClmId(),
							issuesDTO.getIssueId(),
							issuesDTO.getStartDt(),
							issuesDTO.getEndDt());
					if (!decDAO.isEmpty()) {
						errorEnums.add(OTHER_ISSUE_DUPLICATE);
						errorParams.add(decDAO.get(0).getNmiDAO().getNmiShortDescTxt());
						errorParams.add(dateToString.apply(issuesDTO.getStartDt()));
						errorParams.add(dateToString.apply(issuesDTO.getEndDt()));
					}
				}
			}
		}

	}
	public HashMap<String, List<DynamicErrorDTO>> validateInitialAppointment(AppointmentReqDTO apptReqDTO,
                                                                             ReseaIntvwerCalRsicDAO rsicDAO,
                                                                             ReseaIntvwDetRsidDAO rsidDAO,
                                                                             String userId, String roleId,
                                                                             Date systemDate) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		commonValidation(rsicDAO, userId, roleId, errorEnums);
		/*if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() != RSIC_MTG_STATUS_SCHEDULED.longValue()) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else*/ if (ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
				&& rsicDAO.getRsicReopenAllowedTs() != null && systemDate.after(rsicDAO.getRsicReopenAllowedTs())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else {
			LocalDateTime editExpiry = dateToLocalDateTime.apply(string24HToDate().apply(dateToString
							.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventEndTime()))
					.plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue());
			if (!ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
					&& dateToLocalDateTime.apply(systemDate).isAfter(editExpiry)) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
			}
		}
		if (!"Y".equals(rsidDAO.getRsidJms102Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms102Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms106Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms106Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms107Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms107Ind_MANDATORY);
		}
		/*if (!"Y".equals(rsidDAO.getRsidJms123Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms123Ind_MANDATORY);
		}*/
		if (!"Y".equals(rsidDAO.getRsidJms153Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms153Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms160Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms160Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms205Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms205Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsSelfCmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsSelfCmInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsCaseNotesInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsCaseNotesInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsResumeInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsResumeInd_MANDATORY);
		} else if (apptReqDTO.getJmsResumeExpDt() == null) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.ACTIVE_RESUME_EXP_DT_MANDATORY);
		} else if (checkPastDate.test(apptReqDTO.getJmsResumeExpDt(), systemDate)) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.ACTIVE_RESUME_EXP_DT_INVALID);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsVRecruiterInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsVRecruiterInd_MANDATORY);
		} else if (apptReqDTO.getJmsVRExpDt() == null) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.VIRTUAL_RECRUITER_EXP_DT_MANDATORY);
		} else if (checkPastDate.test(apptReqDTO.getJmsVRExpDt(), systemDate)) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.VIRTUAL_RECRUITER_EXP_DT_INVALID);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsWpApplInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsWpApplInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsWpApplSigInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsWpApplSigInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsIepSigInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsIepSigInd_MANDATORY);
		}
		if ("Y".equals(rsidDAO.getRsidJms179Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getOutsideWebReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getOutsideWebReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		if ("Y".equals(rsidDAO.getRsidJms500Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getJMSJobReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getJMSJobReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		if ("Y".equals(rsidDAO.getRsidJmsRegCompltInd()) && "Y".equals(rsidDAO.getRsidJmsRegIncompInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.JMS_REGISTRATION_MUTUAL_EXCLUSIVE);
		}
		validateCreateIssue(apptReqDTO.getOtherIssues(), rsicDAO.getClaimDAO().getClmBenYrEndDt(), errorEnums);
		if (apptReqDTO.getWorkSearchIssues() != null) {
			List<Date> ccaDateList = wwsRepository.getLatestCCA(rsicDAO.getClaimDAO().getClmId(), rsicDAO.getRsicCalEventDt());
			int index = 1;
			for (Date ccaDate: ccaDateList) {
				if (!apptReqDTO.getWorkSearchIssues().containsKey(dateToString.apply(ccaDate))) {
					errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.WORK_SEARCH_ISSUE_INVALID);
				}
				if (index++ == 3) {
					break;
				}
			}
		}
		/*if (!"Y".equals(rsidDAO.getRsidMrpRvwdInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpRvwdInd_MANDATORY);
		}*/
		/*if (!"Y".equals(rsidDAO.getRsidMrpAssgndInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpAssgndInd_MANDATORY);
		} else*/
		if ("Y".equals(rsidDAO.getRsidMrpAssgndInd()) && !StringUtils.hasLength(apptReqDTO.getAssignedMrpChap())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpAssgnd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidIdVerifiedInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidIdVerifiedInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidSlfSchRmdrInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidSlfSchRmdrInd_MANDATORY);
		} /*else if (apptReqDTO.getSelfSchByDt() == null) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.SELF_SCHEDULE_BY_DT_MANDATORY);
		} else if (!checkFutureDate.test(apptReqDTO.getSelfSchByDt(), systemDate)
				|| apptReqDTO.getSelfSchByDt().after(localDateToDate.apply(dateToLocalDate
				.apply(rsicDAO.getRsicCalEventDt()).plusDays(10)))) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.SELF_SCHEDULE_BY_DT_INVALID);
		}*/
		if (!"Y".equals(apptReqDTO.getEmpServicesConfirmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidEsConfirmInd_MANDATORY);
		}
		checkIssueDuplicates(apptReqDTO, rsicDAO, errorEnums, errorParams);
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
		return errorMap;
	}

	public HashMap<String, List<DynamicErrorDTO>> validateFirstSubsequentAppt(AppointmentReqDTO apptReqDTO,
                                                                              ReseaIntvwerCalRsicDAO rsicDAO,
                                                                              ReseaIntvwDetRsidDAO rsidDAO,
                                                                              String userId, String roleId,
                                                                              Date systemDate) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		commonValidation(rsicDAO, userId, roleId, errorEnums);
		/*if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() != RSIC_MTG_STATUS_SCHEDULED.longValue()) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else*/ if (ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
				&& rsicDAO.getRsicReopenAllowedTs() != null && systemDate.after(rsicDAO.getRsicReopenAllowedTs())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else {
			LocalDateTime editExpiry = dateToLocalDateTime.apply(string24HToDate().apply(dateToString
							.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventEndTime()))
					.plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue());
			if (!ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
					&& dateToLocalDateTime.apply(systemDate).isAfter(editExpiry)) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
			}
		}
		if (!"Y".equals(rsidDAO.getRsidJms106Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms106Ind_MANDATORY);
		}
		/*if (!"Y".equals(rsidDAO.getRsidJms123Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms123Ind_MANDATORY);
		}*/
		if (!"Y".equals(rsidDAO.getRsidJms153Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms153Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms160Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms160Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms205Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms205Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms209Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms209Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsVrDhhsInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsVrDhhsInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsIepSigInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsIepSigInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsSelfCmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsSelfCmInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsCaseNotesInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsCaseNotesInd_MANDATORY);
		}
		if ("Y".equals(rsidDAO.getRsidJms179Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getOutsideWebReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getOutsideWebReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		if ("Y".equals(rsidDAO.getRsidJms500Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getJMSJobReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getJMSJobReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		validateCreateIssue(apptReqDTO.getOtherIssues(), rsicDAO.getClaimDAO().getClmBenYrEndDt(), errorEnums);
		if (apptReqDTO.getWorkSearchIssues() != null) {
			List<Date> ccaDateList = wwsRepository.getLatestCCA(rsicDAO.getClaimDAO().getClmId(), rsicDAO.getRsicCalEventDt());
			int index = 1;
			for (Date ccaDate: ccaDateList) {
				if (!apptReqDTO.getWorkSearchIssues().containsKey(dateToString.apply(ccaDate))) {
					errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.WORK_SEARCH_ISSUE_INVALID);
				}
				if (index++ == 3) {
					break;
				}
			}
		}
		/*if (!"Y".equals(rsidDAO.getRsidMrpRvwdInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpRvwdInd_MANDATORY);
		} else*/
		if ("Y".equals(rsidDAO.getRsidMrpRvwdInd()) && !StringUtils.hasLength(apptReqDTO.getReviewedMrpChap())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpRvwd_MANDATORY);
		}
		/*if (!"Y".equals(rsidDAO.getRsidMrpAssgndInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpAssgndInd_MANDATORY);
		}*/
		if (!"Y".equals(rsidDAO.getRsidIdVerifiedInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidIdVerifiedInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidPrevRefrlCcfInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidPrevRefrlCcfInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidSlfSchRmdrInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidSlfSchRmdrInd_MANDATORY);
		} /*else if (apptReqDTO.getSelfSchByDt() == null) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.SELF_SCHEDULE_BY_DT_MANDATORY);
		} else if (!checkFutureDate.test(apptReqDTO.getSelfSchByDt(), systemDate)
				|| apptReqDTO.getSelfSchByDt().after(localDateToDate.apply(dateToLocalDate
				.apply(rsicDAO.getRsicCalEventDt()).plusDays(10)))) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.SELF_SCHEDULE_BY_DT_INVALID);
		}*/
		if (!"Y".equals(apptReqDTO.getEmpServicesConfirmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidEsConfirmInd_MANDATORY);
		}
		checkIssueDuplicates(apptReqDTO, rsicDAO, errorEnums, errorParams);
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
		return errorMap;
	}

	public HashMap<String, List<DynamicErrorDTO>> validateSecondSubsequentAppt(AppointmentReqDTO apptReqDTO,
                                                                               ReseaIntvwerCalRsicDAO rsicDAO,
                                                                               ReseaIntvwDetRsidDAO rsidDAO,
                                                                               String userId, String roleId,
                                                                               Date systemDate) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		commonValidation(rsicDAO, userId, roleId, errorEnums);
		/*if (rsicDAO.getRsicMtgStatusCdAlv().getAlvId() != RSIC_MTG_STATUS_SCHEDULED.longValue()) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else*/ if (ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
				&& rsicDAO.getRsicReopenAllowedTs() != null && systemDate.after(rsicDAO.getRsicReopenAllowedTs())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
		} else {
			LocalDateTime editExpiry = dateToLocalDateTime.apply(string24HToDate().apply(dateToString
							.apply(rsicDAO.getRsicCalEventDt()), rsicDAO.getRsicCalEventEndTime()))
					.plusMinutes(parRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue());
			if (!ReseaConstants.INDICATOR.Y.toString().equals(rsicDAO.getRsicReopenAllowedInd())
					&& dateToLocalDateTime.apply(systemDate).isAfter(editExpiry)) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.Appointment_Status_Invalid);
			}
		}
		if (!"Y".equals(rsidDAO.getRsidJms106Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms106Ind_MANDATORY);
		}
		/*if (!"Y".equals(rsidDAO.getRsidJms123Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms123Ind_MANDATORY);
		}*/
		if (!"Y".equals(rsidDAO.getRsidJms153Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms153Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms160Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms160Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms205Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms205Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJms209Ind())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms209Ind_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsIepSigInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsIepSigInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsSelfCmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsSelfCmInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsCaseNotesInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsCaseNotesInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsClseGoalsInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsClseGoalsInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidJmsClseIepInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsClseIepInd_MANDATORY);
		}

		if ("Y".equals(rsidDAO.getRsidJms179Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getOutsideWebReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getOutsideWebReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms179Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		if ("Y".equals(rsidDAO.getRsidJms500Ind())) {
			if (CollectionUtils.isEmpty(apptReqDTO.getJMSJobReferral())) {
				errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral);
			} else {
				for (JobReferralDTO referralDTO: apptReqDTO.getJMSJobReferral()) {
					if (!StringUtils.hasLength(referralDTO.getEmpName()) || !StringUtils.hasLength(referralDTO.getJobTitle())){
						errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJms500Ind_JobReferral_INVALID);
						break;
					}
				}
			}
		}
		validateCreateIssue(apptReqDTO.getOtherIssues(), rsicDAO.getClaimDAO().getClmBenYrEndDt(), errorEnums);
		if (apptReqDTO.getWorkSearchIssues() != null) {
			List<Date> ccaDateList = wwsRepository.getLatestCCA(rsicDAO.getClaimDAO().getClmId(), rsicDAO.getRsicCalEventDt());
			int index = 1;
			for (Date ccaDate: ccaDateList) {
				if (!apptReqDTO.getWorkSearchIssues().containsKey(dateToString.apply(ccaDate))) {
					errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.WORK_SEARCH_ISSUE_INVALID);
				}
				if (index++ == 3) {
					break;
				}
			}
		}
		/*if (!"Y".equals(rsidDAO.getRsidMrpRvwdInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidMrpRvwdInd_MANDATORY);
		}*/
		if (!"Y".equals(rsidDAO.getRsidJmsEpcklstUplInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidJmsEpcklstUplInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidPrevRefrlCcfInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidPrevRefrlCcfInd_MANDATORY);
		}
		if (!"Y".equals(rsidDAO.getRsidIdVerifiedInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidIdVerifiedInd_MANDATORY);
		}
		if (!"Y".equals(apptReqDTO.getEmpServicesConfirmInd())) {
			errorEnums.add(ErrorMessageConstant.InitialApptErrorDetail.RsidEsConfirmInd_MANDATORY);
		}
		checkIssueDuplicates(apptReqDTO, rsicDAO, errorEnums, errorParams);
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
		return errorMap;
	}
}
