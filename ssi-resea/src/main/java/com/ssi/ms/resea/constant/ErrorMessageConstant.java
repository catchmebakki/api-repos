package com.ssi.ms.resea.constant;

import com.ssi.ms.resea.util.ReseaErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ErrorMessageConstant {
    String USR_ID_MANDATORY = "userId.mandatory";
    String START_DT_MANDATORY = "startDt.mandatory";
    String END_DT_MANDATORY = "endDt.mandatory";
    String APPOINTMENT_TYPE_INVALID = "type.invalid";

    //Return to Work Error Messages starts
    String EVENT_ID_MANDATORY = "eventId.mandatory"; //RSIC_ID
    String CASE_ID_MANDATORY = "caseId.mandatory";
    String CASE_ID_NOT_FOUND = "caseId.notFound";
    String CLAIM_ID_NOT_FOUND = "clmId.notFound";

    String EVENT_ID_NOT_FOUND = "eventId.notFound"; //RSIC_ID
    String EMP_START_DT_MANDATORY = "empStartDt.mandatory";
    String COMPANY_NAME_MANDATORY = "companyName.mandatory";
    String JOB_TITLE_MANDATORY = "jobTitle.mandatory";
    String WORK_SCHEDULE_MANDATORY = "workSchedule.mandatory";
    String HOURLY_PAY_RATE = "hourlyPayRate.mandatory";
    String STATE_MANDATORY = "state.mandatory";
    String CITY_MANDATORY = "city.mandatory";
    String WORK_MODE_MANDATORY = "workMode.mandatory";
    String WORK_MODE_VALUE_NOT_VALID = "workMode.notValidValue";
    String STAFF_NOTES_MANDATORY = "staffNotes.mandatory";
    String STAFF_NOTES_MAX_CHARACTERS = "staffNotes.exceeds_limit";
    String SAVE_RTW_VALIDATION_FAILED = "Save RTW validation failed.";
    String STATUS_MANDATORY = "status.mandatory";
    String KPI_PERIOD_RANGE_INVALID = "kpi.periodRange.invalid";
    String INDICATOR_YES = "Y";
    String INDICATOR_NO = "N";
    @Getter
    @AllArgsConstructor
    enum InterviewErrorDTODetail implements ReseaErrorEnum {
        WORK_SREACH_ISSUE_DUPLICATE("workSearchIssues", "workSearchIssues.duplicate",
                "Work Search issue '{0}' already exists for week ending '{1}'. Please remove or change the Issue.", 2),
        OTHER_ISSUE_DUPLICATE("otherIssues", "otherIssues.duplicate",
                "Other Issue '{0}' already exists for Start Date '{1}' and End Date '{2}'. Please remove or change the Issue.", 3);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }
    @Getter
    @AllArgsConstructor
    enum InitialApptErrorDetail implements ReseaErrorEnum {
        EVENT_CASE_INVALID("appointment", "appointment.case.invalid",
                "Appointment is not associated with RESEA case, cannot submit Interview Details.", 0),
        USR_ACCESS_INVALID("appointment", "appointment.access.invalid",
                "User doesnot have access to submit interview details for another case manager.", 0),
        CASE_STAGE_INVALID("appointment", "appointment.stage.invalid",
                "Case stage has progressed. This appointment can be no longer edited.", 0),
        Appointment_Status_Invalid("appointment", "appointment.status.invalid",
                "Interview Appointment Status is invalid. Cannot submit Interview Details", 0),
        RsidJms102Ind_MANDATORY("InitialAssessment", "InitialAssessment.mandatory",
                "102 Initial Assessment is mandatory", 0),
        RsidJms106Ind_MANDATORY("Eri1On1", "Eri1On1.mandatory",
                "106 ERI 1-on-1 is mandatory", 0),
        RsidJms107Ind_MANDATORY("ELMIServices", "ELMIServices.mandatory",
                "107 ELMI Services is mandatory", 0),
        RsidJms123Ind_MANDATORY("JobDevelopment", "JobDevelopment.mandatory",
                "123 Job Development is mandatory", 0),
        RsidJms153Ind_MANDATORY("CaseManagement", "CaseManagement.mandatory",
                "153 Case Management is mandatory", 0),
        RsidJms160Ind_MANDATORY("AttendedRESEA", "AttendedRESEA.mandatory",
                "160 Attended RESEA is mandatory", 0),
        RsidJms179Ind_MANDATORY("OutsideWebReferral", "OutsideWebReferral.mandatory",
                "179 Outside Web Referral is mandatory", 0),
        RsidJms205Ind_MANDATORY("DevelopIEP", "DevelopIEP.mandatory",
                "205 Develop IEP is mandatory", 0),
        RsidJms209Ind_MANDATORY("ReferWIOATraining", "ReferWIOATraining.mandatory",
                "209 Refer to WIOA state & lical training is mandatory", 0),
        RsidJmsVrDhhsInd_MANDATORY("ReferToVRorDHHS", "ReferToVRorDHHS.mandatory",
                "Refer to VR or DHHS is mandatory", 0),
        RsidJms500Ind_MANDATORY("JMSJobReferral", "JMSJobReferral.mandatory",
                "500 JMS Job referral is mandatory", 0),
        RsidJmsSelfCmInd_MANDATORY("AddSelfCaseManager", "AddSelfCaseManager.mandatory",
                "Add Self as Case Manager is mandatory", 0),
        RsidJmsCaseNotesInd_MANDATORY("JMSCaseNotes", "JMSCaseNotes.mandatory",
                "JMS Case Notes is mandatory", 0),
        RsidJmsClseGoalsInd_MANDATORY("CloseGoals", "CloseGoals.mandatory",
                "Close Goals is mandatory", 0),
        RsidJmsClseIepInd_MANDATORY("JmsCloseIEP", "JmsCloseIEP.mandatory",
                "Close IEP if not being case managed by a Partner is mandatory", 0),
        RsidJmsRegCompltInd_MANDATORY("JMSRegComplete", "JMSRegComplete.mandatory",
                "JMS Registration complete is mandatory", 0),
        RsidJmsRegIncompInd_MANDATORY("JMSRegIncomplete", "JMSRegIncomplete.mandatory",
                "JMS Registration Incomplete & Warning Issued is mandatory", 0),
        RsidJmsResumeInd_MANDATORY("ActiveResume", "ActiveResume.mandatory",
                "Active Resume is mandatory", 0),
        RsidJmsVRecruiterInd_MANDATORY("ActiveVirtualRecuiter", "ActiveVirtualRecuiter.mandatory",
                "Active Virtual Recruiter is mandatory", 0),
        RsidJmsWpApplInd_MANDATORY("WagnerPeyserApplComplete", "WagnerPeyserApplComplete.mandatory",
                "Wagner-Peyser Application Completed with Individual is mandatory", 0),
        RsidJmsWpApplSigInd_MANDATORY("WagnerPeyserApplSignature", "WagnerPeyserApplSignature.mandatory",
                "Wagner-Peyser Application Signature sent is mandatory", 0),
        RsidJmsIepSigInd_MANDATORY("IEPSignatureCopy", "IEPSignatureCopy.mandatory",
                "Send IEP for Signature and give copy is mandatory", 0),
        RsidJms179Ind_JobReferral("OutsideWebReferral", "OutsideWebReferral.jobReferral",
                "179 Outside Web Referral is selected, please enter the related Job Referral entries", 0),
        RsidJms179Ind_JobReferral_INVALID("OutsideWebReferral", "OutsideWebReferral.jobReferral.invalid",
                "179 Outside Web Referral Job Referral entries are invalid, employee name and job title are mandatory", 0),
        RsidJms500Ind_JobReferral("JMSJobReferral", "JMSJobReferral.jobReferral",
                "500+: JMS Job Referral is selected, please enter the related Job Referral entries", 0),
        RsidJms500Ind_JobReferral_INVALID("JMSJobReferral", "JMSJobReferral.jobReferral.invalid",
                "500+: JMS Job Referral Job Referral entries are invalid, employee name and job title are mandatory", 0),
        JMS_REGISTRATION_MUTUAL_EXCLUSIVE("JMSRegComplete", "JMSRegComplete.exclusive",
                "One and only one of the following two checkboxes must be checked-of : JMS Registration Complete OR JMS Registration Incomplete & Warning Issued", 0),
        SELF_SCHEDULE_BY_DT_MANDATORY("selfSchByDt", "selfSchByDt.mandatory",
                "Self-schedule by date is mandatory when option is selected. Please enter valid date", 0),
        SELF_SCHEDULE_BY_DT_INVALID("selfSchByDt", "selfSchByDt.invalid",
                "Self-schedule by date should be future date with in 10 days of the appointment. Please enter valid date", 0),
        VIRTUAL_RECRUITER_MANDATORY("ActiveVirtualRecuiter", "ActiveVirtualRecuiter.mandatoryDt",
                "Please select Active Virtual Recruiter option to enter Expiration date", 0),
        VIRTUAL_RECRUITER_EXP_DT_MANDATORY("jmsVRExpDt", "jmsVRExpDt.mandatory",
                "Virtual Recruiter Expiration date is mandatory when option is selected. Please enter valid date", 0),
        VIRTUAL_RECRUITER_EXP_DT_INVALID("jmsVRExpDt", "jmsVRExpDt.invalid",
                "Virtual Recruiter Expiration date should be a valid future date", 0),
        ACTIVE_RESUME_MANDATORY("ActiveResume", "ActiveResume.mandatoryDt",
                "Active Resume Expiration date is mandatory when option is selected. Please enter valid date", 0),
        ACTIVE_RESUME_EXP_DT_MANDATORY("jmsResumeExpDt", "jmsResumeExpDt.mandatory",
                "Active Resume Expiration date is mandatory when option is selected. Please enter valid date", 0),
        ACTIVE_RESUME_EXP_DT_INVALID("jmsResumeExpDt", "jmsResumeExpDt.invalid",
                "Active Resume Expiration date should be a valid future date", 0),
        WORK_SEARCH_ISSUE_INVALID("workSearchIssues", "workSearchIssues.invalid",
                "Please check-off at least each of the most recent three continued claim weeks as having been reviewed", 0),
        RsidMrpRvwdInd_MANDATORY("ReviewedReEmpPlan", "ReviewedReEmpPlan.mandatory",
                "Reviewed Chapters 1-4 of My Reemployment Plan is mandatory.", 0),
        RsidMrpRvwd_MANDATORY("reviewedMrpChap", "reviewedMrpChap.mandatory",
                "Please select chapters for Reviewed My Reemployment Plan", 0),
        RsidMrpAssgndInd_MANDATORY("AssignedReEmpPlan", "AssignedReEmpPlan.mandatory",
                "Assigned My Reemployment Plan is mandatory.", 0),
        RsidIdVerifiedInd_MANDATORY("PhysicallyVerifiedID", "PhysicallyVerifiedID.mandatory",
                "Physically Verified Claimant's ID is mandatory.", 0),
        RsidSlfSchRmdrInd_MANDATORY("RemindedSelfSchedule", "RemindedSelfSchedule.mandatory",
                "Reminded Claimant to Self-schedule is mandatory.", 0),
        RsidMrpAssgnd_MANDATORY("assignedMrpChap", "assignedMrpChap.mandatory",
                "Please select chapters for Assigned My Reemployment Plan", 0),
        RsidEsConfirmInd_MANDATORY("empServicesConfirmInd", "empServicesConfirmInd.mandatory",
                "Please select checkbox confirming that Employment Services have been provided", 0),
        RsidPrevRefrlCcfInd_MANDATORY("CheckedPriorJobReferrals", "CheckedPriorJobReferrals.mandatory",
                "Checked Prior Job Referrals is mandatory", 0),
        RsidJmsEpcklstUplInd_MANDATORY("EPandCheckListUpld", "EPandCheckListUpld.mandatory",
                "Copy of EP and Checklist uploaded into JMS is mandatory", 0),
        Rswr_WorkSearch_Issue_Edit("workSearchIssues", "workSearchIssues.edit.invalid",
                "Work Search Review cannot be edited for existing entries where issues have been created.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum ReturnToWorkErrorDetail implements ReseaErrorEnum {

        NON_DIRECT_PLACEMENT_AND_JMS_REFERRAL_RECORDED_EMPTY("Either Non-Direct placement Recorded or JMS Referral Recorded should be checked",
                "jms890IndAndjmsReferralInd", "jms890IndAndjmsReferralInd.bothEmpty", 0),

        FURUTE_DATE_JMS_CHECKLIST_COMPLETED_CHECKED_OFF("For future start date, None of the items listed for JMS completed should not be checked.",
                "jmsCompletedItemsCheckOff", "jmsCompletedItems.checkedOff", 0),

        PAST_DATE_JMS_CHECKLIST_COMPLETED_NOT_CHECKED_OFF("For past start date,JMS completed checklist should be checked.",
                "jmsCompletedItemsNotCheckOff", "jmsCompletedItems.checkedNotOff", 0),

        EMP_ST_DATE_AFTER_RTW_FUT_DAYS("Employment start date should be on or before 30 days from the current date.",
                "employmentStartDt", "employmentStartDt.afterRtwFutureDays", 0),

        CREATE_ACTIVITY_FAILED("Request Processed. However, case activity could not created.  Please Contact WeCare.",
                "createActivity", "createActivity.failed", 0);

        private String description;
        private String frontendField;
        private String frontendErrorCode;
        private Integer params;
    }
    //Return to Work Error Messages ends


    //Reschedule Request Error Message starts

    String OLD_EVENT_ID_MANDATORY = "oldEventId.mandatory";

    String NEW_EVENT_ID_MANDATORY = "newEventId.mandatory";
    String NON_COMPLAIANCE_MANDATORY = "nonComplaince.indicator.mandatory";

    String REASON_FOR_RESCHEDULING_MANDATORY = "reschedule.reason.mandatory";

    String LATE_SCHEDULING_REASON_MAX_CHARACTERS = "lateSchedulingNotes.exceeds_limit";

    String SAVE_RESCHEDULE_VALIDATION_FAILED = "Save Reschedule validation failed.";

    String GET_AVAILABLE_SLOTS_FOR_RESCHEDULE_FAILED = "Get Available slots for reschedule failed.";

    String WORK_SCHEDULE_INVALID = "workSchedule.invalid";

    @Getter
    @AllArgsConstructor
    enum RescheduleRequestErrorDetail implements ReseaErrorEnum {

        MEETING_MODE_NOT_CHECKED("Either In-Person or Virtual should be checked", "preferredMeetingMode", "preferredMeetingMode.bothNotChecked", 0),

        APPOINTMENT_DATE_MANDATORY("Appointment Date mandatory", "appointmentDate", "appointmentDate.mandatory", 0),

        APPOINTMENT_TIME_MANDATORY("Appointment Time mandatory", "appointmentTime", "appointmentTime.mandatory", 0),

        CITY_MANDATORY("City mandatory", "entityCity", "entityCity.mandatory", 0),

        STATE_MANDATORY("State mandatory", "entityState", "entityState.mandatory", 0),

        EMPLOYER_NAME_MANDATORY("employer name mandatory", "entityName", "entityName.mandatory", 0),

        EMP_TELEPHONE_MANDATORY( "employer telephone number mandatory", "entityTeleNumber", "entityTeleNumber.mandatory", 0),

        EMP_TELEPHONE_INVALID( "employer telephone number invalid", "entityTeleNumber", "entityTeleNumber.invalid", 0),

        JOB_TITLE_MANDATORY("job title mandatory", "jobTitle", "jobTitle.mandatory", 0),

        PART_FULL_TIME_MANDATORY("part time full time indicator mandatory", "partFullTimeInd", "partFullTimeInd.mandatory", 0),

        LATE_SCHEDULING_REASON_EMPTY ("Please enter the reason for late scheduling.",
                "lateSchedulingReason.empty", "lateSchedulingReason", 0);
        private String description;
        private String frontendField;
        private String frontendErrorCode;
        private Integer params;
    }
    //Reschedule Request Error Message ends


    @Getter
    @AllArgsConstructor
    enum AvailableApptErrorDetail implements ReseaErrorEnum {
        APPOINTMENT_NOT_AVAILABLE("eventId", "eventId.notAvailable",
                "Appointment Slot is not Available", 0),
        APPOINTMENT_FOR_MANDATORY("for", "for.mandatory",
                "Appointment Slot is not Available", 0),
        APPOINTMENT_FOR_LOF_INVALID("for", "for.lof.invalid",
                "User does not have access to For Local Office option", 0),
        APPOINTMENT_FOR_MGR_INVALID("for", "for.mgr.invalid",
                "User does not have access to For Case Manager option", 0),
        CLAIMANT_MANDATORY("claimId", "claimId.mandatory",
                "Please select a claimant for scheduling the appointment", 0),
        INFORM_CLAIMANT_MANDATORY("informedCmtInd", "informedCmtInd.mandatory",
                "Please inform claimant and select Informed Claimant to check claimant portal", 0),
        INFORMATION_CONVEYED_MANDATORY("informedConveyedBy", "informedConveyedBy.mandatory",
                "Please check option by which information was convyed to claimant.", 0),
        LATE_NOTES_MANDATORY("lateStaffNote", "lateStaffNote.mandatory",
                "Appointment is scheduled beyond the RESEA Deadline, please submit Late Notes.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    String CLAIM_ID_MANDATORY = "claimId.mandatory";
    String INFORM_CLAIMANT_MANDATORY = "informedCmtInd.mandatory";
    //Switch Meeting Mode starts
    String REASON_FOR_SWITCH_MEETING_MODE_MANDATORY = "switchmeetingmode.reason.mandatory";
    String CHANGE_REASON_TEXT_FOR_SWITCH_MEETING_MODE_MAX_CHARACTERS = "change.reason.text.exceeds_limit";
    String CURRENT_MEETING_MODE_MANDATORY = "currentmeetingmode.mandatory";
    String SAVE_SWITCH_MEETING_MODE_VALIDATION_FAILED = "Save Switch Meeting Mode validation failed.";

    @Getter
    @AllArgsConstructor
    enum SwitchMeetingModeErrorDetail implements ReseaErrorEnum {
        OTHERS_TEXT_FOR_SWITCH_MEETING_MODE_MANDATORY("meetingModeChgReasonTxt", "meetingModeChgReasonTxt.mandatory",
                "Please enter the description for switching meeting mode", 0),
        SWITCH_MEETING_MODE_ROLE_INVALID("currentMeetingMode", "currentMeetingMode.role.invalid",
                "User does not have access to switch Initial Appointment to 'Virtual' mode.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }
    //Switch Meeting Mode ends

    @Getter
    @AllArgsConstructor
    enum CommonErrorDetail implements ReseaErrorEnum {
        CREATE_ISSUE_INVALID("createIssue", "createIssue.invalid",
                "Please enter all fields for selected Create Issue", 0),
        /*CREATE_ISSUE_START_DT_INVALID("createIssue.issueStartDt", "createIssue.issueStartDt.invalid",
                "Start Date for a Create Issue should be a valid future date", 0),*/
        CREATE_ISSUE_END_DT_INVALID("createIssue.issueEndDt", "createIssue.issueEndDt.invalid",
                "End Date for a Create Issue should be after Start Date", 0),
        CREATE_ISSUE_START_DT_INVALID("createIssue.issueStartDt", "createIssue.issueStartDt.invalid",
                "Start Date for a Create Issue should be prior to Current Benefit Year End Date.", 0),
        CREATE_ISSUE_END_DT_PRIOR_TO_BYE("createIssue.issueEndDt", "createIssue.issueEndDt.priorToBye",
                "End Date for a Create Issue should be prior to Current Benefit Year End Date.", 0),
        CREATE_ACTIVITY_FAILED("createActivity", "createActivity.failed",
                    "Request Processed. However, case activity could not created. Please Contact WeCare.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum CaseReassign implements ReseaErrorEnum {
        APPOINTMENT_NOT_AVAILABLE("eventId", "eventId.notAvailable",
                "Appointment is no longer available for scheduling. Please select another appointment.", 0),
        /*CREATE_ISSUE_START_DT_INVALID("createIssue.issueStartDt", "createIssue.issueStartDt.invalid",
                "Start Date for a Create Issue should be a valid future date", 0),*/
        APPOINTMENT_USAGE_CASE_STAGE_MISMATCH("eventId", "eventId.mismatch",
                "Case Stage and Apponinment Selected do not match. Please select appropriate appointment.", 0),
        REASSIGN_ACCESS_INVALID("reassign", "reassign.access.invalid",
                "User does not have access to Reassign cases.", 0),
        APPLY_UNAVAILABLITY_FAILED_NHL("applyUnavailibility", "applyUnavailibility.failed.nhl",
                "Unavailablity could not be applied. Please contact Wecare with reference #: {0}", 1),
        APPLY_UNAVAILABLITY_FAILED("applyUnavailibility", "applyUnavailibility.failed",
                "Unavailablity could not be applied. Please contact Wecare.", 0),
        REASSIGN_ALL_FAILED("reassignAll", "reassignAll.failed",
                "Request Failed. Could not reassign cases. Please Contact WeCare.", 0),
        REASSIGN_DATE_INVALID("reassignDt", "reassignDt.invalid",
                "Reassign Start Date has to be a future date.", 0),
        REASSIGN_END_DT_INVALID("reassignEndDt", "reassignEndDt.invalid",
                "Reassign End Date cannot be prior to Reassign Start Date.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    String CASE_OFFICE_IND_MANDATORY = "caseOffice.mandatory";
    String REASSIGN_REASON_MANDATORY = "reassignReasonCd.mandatory";

    @Getter
    @AllArgsConstructor
    enum CalendarEventErrorDetail implements ReseaErrorEnum {
        APPOINTMENT_REOPEN_INVALID("reopen", "reopen.invalid",
                "User does not have access to Reopen Calendar Appointment.", 0),
        APPOINTMENT_REOPEN_EXPIRED("reopen", "reopen.expired",
                "Appointment cannot be reopened. Appointments can be reopened with in 30 days only.", 0),
        APPOINTMENT_REOPEN_STAGE_EXPIRED("reopen", "reopen.stage.expired",
                "Appointment cannot be reopened. Case has progressed to next stage.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum LookupErrorDetail implements ReseaErrorEnum {
        LOOKUP_EXCEED_LIMIT("lookup", "lookup.limit.exceed",
                "Search give more than 2000 results. Please refine search criteria.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    String UNAVAILABILITY_ID_MANDATORY = "unavailabilityId.mandatory";
    String UNAVAILABILITY_ID_NOT_FOUND = "unavailabilityId.notFound";
    String REASON_MANDATORY = "reason.mandatory";
    String START_TIME_MANDATORY = "startTime.mandatory";
    String END_TIME_MANDATORY = "endTime.mandatory";
    @Getter
    @AllArgsConstructor
    enum StaffUnavailabilityErrorDetail implements ReseaErrorEnum {
        SUMMARY_CASE_MANAGER_ID_MANDATORY("userId", "userId.mandatory",
                "User Id is mandatory", 0),
        SUMMARY_START_DT_MANDATORY("startDt", "startDt.mandatory",
                "Start Date is mandatory.", 0),
        UNAVAILABILITY_NOTE_MANDATORY("notes", "notes.mandatory",
                "Unavailability Notes is mandatory.", 0),
        UNAVAILABLITY_STARTDT_ENDDT_INVALID("endDt", "endDt.invalid",
                "Unvailablitiy End Date cannot be prior to Start Date", 0),
        UNAVAILABLITY_REQUEST_EVENT_INVALID("request", "request.events.invalid",
                "Scheduled appointments present during the unavailability period being requested. Please modify unavailability request.", 0),
        UNAVAILABLITY_REQUEST_DATE_INVALID("request", "request.startDt.invalid",
                "Unavailability Start date cannot be a past date. Please modify unavailability request.", 0),
        UNAVAILABLITY_REQUEST_ACCESS_INVALID("request", "request.access.invalid",
                "User does not have access to request Unavailability for another case manager", 0),
        UNAVAILABLITY_APPROVE_ACCESS_INVALID("approve", "approve.access.invalid",
                "User does not have access to approve Unavailability request", 0),
        UNAVAILABLITY_APPROVE_STATUS_INVALID("approve", "approve.status.invalid",
                "Cannot Approve Unavailability. Unavailability Record is not in pending status.", 0),
        UNAVAILABLITY_REJECT_ACCESS_INVALID("reject", "reject.access.invalid",
                "User does not have access to reject Unavailability request", 0),
        UNAVAILABLITY_REJECT_STATUS_INVALID("reject", "reject.status.invalid",
                "Cannot Reject Unavailability. Unavailability Record is not in pending status.", 0),
        UNAVAILABLITY_WITHDRAW_ACCESS_INVALID("withdraw", "withdraw.access.invalid",
                "User does not have access to withdraw this Unavailability request.", 0),
        UNAVAILABLITY_WITHDRAW_DATE_INVALID("withdraw", "withdraw.date.invalid",
                "Withdraw Date cannot be prior to Unavailability start date or after Unavailability end date.", 0),
        UNAVAILABLITY_WITHDRAW_NOTE_MANDATORY("withdraw", "withdraw.note.mandatory",
                "Withdraw Notes is mandatory.", 0),
        UNAVAILABLITY_APPROVE_FAILED("approve", "approve.failed",
                "Unavailablity could not be applied. Please Contact WeCare.", 0),
        UNAVAILABLITY_WITHDRAW_FAILED("withdraw", "withdraw.failed",
                "Unavailablity could not be removed. Please Contact WeCare.", 0),
        UNAVAILABILITY_START_TIME_MANDATORY("withdrawTime", "withdrawTime.mandatory",
                "Unavailability Withdraw Time is mandatory.", 0),
        UNAVAILABLITY_WITHDRAW_INVALID("withdraw", "withdraw.invalid",
                "Unavailability duration has started, please enter valid withdraw date.", 0),
        UNAVAILABILITY_ID_INVALID("unavailabilityId", "unavailabilityId.invalid",
                "Unavailability ID is Invalid. No Record exists.", 0),
        UNAVAILABILITY_ALREADY_EXISTS("unavailabilityId", "unavailability.exists.invalid",
                "Staff Unavailability already exists for provided Unavailability ID.", 0),
        UNAVAILABILITY_STUN_ADMIN("unavailabilityId", "unavailability.stun.admin",
                "Unable to generate Staff Unavailablility records for the duration of Unavailablility. Please conctact Wecare with reference #: {0}", 1),
        UNAVAILABILITY_RSIC_ADMIN("unavailabilityId", "unavailability.schedule.admin",
                "Unable to generate Block Staff Calendar records for the duration of Unavailablility. Please conctact Wecare with reference #: {0}", 1),
        UNAVAILABILITY_ADMIN_NHL("unavailabilityId", "unavailability.admin.nhl",
                "Unable to process Staff Unavailablility. Please conctact Wecare with reference #: {0}", 1),
        UNAVAILABILITY_ADMIN("unavailabilityId", "unavailability.admin",
                "Unable to process Staff Unavailablility. Please conctact Wecare.", 0),
        UNAVAILABLITY_REQUEST_ALREADY_EXISTS("request", "request.unavailability.exists",
                "Overlapping Staff Unavailability exists for request period. Please modify unavailability request.", 0),
        UNAVAILABLITY_WITHDRAW_STATUS_INVALID("withdraw", "withdraw.status.invalid",
                "Unavailability is not in approved status, cannot withdraw.", 0),
        UNAVAILABLITY_WITHDRAW_FUTURE_DT("withdraw", "withdraw.date.future",
                "Withdraw Start Date has to be a future date.", 0),
        UNAVAILABLITY_WITHDRAW_TIME_INVALID("withdraw", "withdraw.time.invalid",
                "Can not change start/end time for withdrawal of a recurring unavailability.", 0),
        UNAVAILABLITY_WITHDRAW_OVERLAP("withdraw", "withdraw.overlap",
                "Unable to withdraw due to a conflicting Unavailability Overlap.", 0),
        UNAVAILABLITY_WITHDRAW_DATE_FUTURE("withdraw", "withdraw.pastdate.invalid",
                "Withdraw Date cannot be past date, please enter valid withdraw date.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }
	
	@Getter
    @AllArgsConstructor
    enum KPIMetricsRequestErrorDetail implements ReseaErrorEnum {
        KPI_INPUT_INVALID("kpi", "kpi.input.invalid",
                "Atleast one the following criteria should be selected. Case Mgr/Lof/Agency.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum ScheduleApptErrorDetail implements ReseaErrorEnum {
        LOCAL_OFFICE_OPTION_NOT_SELECTED("clmLofInd", "clmLofInd.input.invalid",
                "Please select the local office option for case manager availability.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    String GET_CASE_MGR_AVAILABILITY_FOR_SCH_INITIAL_APPT_FAILED = "Get Case Manager Availability list for Schedule Initial Appointment failed.";
    String SAVE_SCH_INITIAL_APPT_REQ_FAILED = "Save Schedule Initial Appointment Request Failed";

    String UNAVAILABLILITY_DT_MANDATORY = "unavailabilityId.mandatory";

    @Getter
    @AllArgsConstructor
    enum UploadFieldErrorDetail implements ReseaErrorEnum {
        RESEA_DAYOFWEEK_IS_REQUIRED("dayOfWeek", 1, "dayOfWeek.required",
                "The uploaded file is missing the Day of Week.", 0),
        RESEA_APPT_TIMEFRAME_IS_REQUIRED("time", 2, "time.required",
                "The uploaded file is missing the Appointment Time Frame.", 0),
        RESEA_APPT_TIMEFRAME_IS_INVALID("time", 3, "time.invalid",
                "The uploaded file had invalid Appointment Time Frame.", 0),
        RESEA_STARTTIME_IS_REQUIRED("time", 4, "time.startTime.required",
                "The uploaded file is missing the Start Time.", 0),
        RESEA_ENDTIME_IS_REQUIRED("time", 5, "time.endTime.required",
                "The uploaded file is missing the End Time.", 0),
        RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED("zoomLink", 6, "zoomLink.required",
                "The uploaded file is missing the Zoom Link Details.", 0),
        RESEA_ALLOW_ONSITE_IS_REQUIRED("allowOnsite", 7, "allowOnsite.required",
                "The uploaded file is missing the Allow Onsite Indicator.", 0),
        RESEA_ALLOW_REMOTE_IS_REQUIRED("allowRemote", 8, "allowRemote.required",
                "The uploaded file is missing the Allow Remote Indicator.", 0),
        RESEA_UPLOAD_ACCESS("uploadForm", 9, "uploadForm.accessInvalid",
                "User does not have access to upload Staff Schedule.", 0),
        RESEA_UPLOAD_EFFECTIVE_DT_INVALID("effectiveDt", 10, "effectiveDt.invalid",
                "Upload Effective date should be a valid future date.", 0),
        PARSING_ERROR("",9, "Parsing failed.", "", 0),
        DAY_OF_WEEK_IS_INVALID("dayOfWeek", 11, "dayOfWeek.invalid",
                "Please re-enter the day of week. It has to be a weekday.", 0),
        START_TIME_IS_INVALID("time", 12, "time.startTime.invalid",
                "Please re-enter the start time. It has to be in 24 Hour format.", 0),
        END_TIME_IS_INVALID("dayOfWeek", 13, "time.endTime.invalid",
                "Please re-enter the end time. It has to be in 24 Hour format.", 0),
        ALLOW_ONSITE_IS_INVALID("allowOnsite", 14, "allowOnsite.invalid",
                "Please re-enter Allow Onsite indicator.", 0),
        ALLOW_REMOTE_IS_INVALID("allowRemote", 15, "allowRemote.invalid",
                "Please re-enter Allow Remote indicator.", 0),
        ZOOM_LINK_IS_REQUIRED("zoomLink", 16, "zoomLink.mandatory",
                "Please re-enter Zoom Link details.", 0),
        ZOOM_LINK_TOPIC_IS_REQUIRED("zoomLink", 17, "zoomLink.topic.mandatory",
                "Please re-enter Zoom Link details with Topic.", 0),
        ZOOM_LINK_URL_REQUIRED("zoomLink", 18, "zoomLink.link.mandatory",
                "Please re-enter Zoom Link details with Zoom meeting URL link.", 0),
        ZOOM_LINK_TOPIC_IS_INVALID("zoomLink", 19, "zoomLink.topic.invalid",
                "Please re-enter Zoom Link Topic details.", 0),
        ZOOM_MEETING_DURATION_MISMATCH("zoomLink", 20, "time.duration.invalid",
                "TIme Duration provided and Zoom Link details Topic are not matching.", 0),
        ZOOM_LINK_MEETING_ID_IS_REQUIRED("zoomLink", 21, "zoomLink.joinurl.mandatory",
                "Please re-enter Zoom Link details with valid Zoom meeting Link.", 0),
        ZOOM_LINK_PASSCODE_IS_REQUIRED("zoomLink", 22, "zoomLink.passcode.mandatory",
                "Please re-enter Zoom Link details with Passcode.", 0),
        ZOOM_LINK_PHONE_IS_REQUIRED("zoomLink", 23, "zoomLink.phone.mandatory",
                "Please re-enter Zoom Link details with Phone Number.", 0),
        SCHEDULE_EVENT_EXISTS("finalize", 24, "finalize.reassignScheduled",
                "Staff has scheduled appointments conflicting with the schedule change. Please reschedule/reassign all such appointments.", 0),
        FINALIZE_STATUS_INVALID("finalize", 25, "finalize.status.invalid",
                "Staff Schedule Upload status is invalid.", 0),
        FINALIZE_STATUS_INCOMPLETE("finalize", 26, "finalize.status.incomplete",
                "Staff Schedule Upload status is not complete, please upload a schedule with no errors.", 0),
        INITIALIZE_DUPLICATE("uploadForm", 27, "uploadForm.duplicate",
                "Open Upload Form already exists for this Staff. Please finalize or discard previous Upload Form.", 0),
        DISCARD_FINALIZED("discard", 28, "discard.finalized",
                "Upload Form is finalized and cannot be discarded.", 0),
        RESEA_DISCARD_ACCESS("discard", 29, "discard.accessInvalid",
                "User does not have access to discard Staff Schedule.", 0),
        RESEA_DISCARD_INVALID("discard", 30, "discard.invalid",
                "Upload Form is already in discarded status.", 0),
        ZOOM_LINK_CASE_MGR_MISMATCH("zoomLink", 31, "zoomLink.mgr.mismatch",
                "Case Manager Name provided in Zoom Link Details does not match the Upload Case Manager.", 0),
        START_TIME_IS_OUT_OF_CAL_RANGE("time", 32, "time.startTime.outrange",
                "Please re-enter the start time. It has to be in calendar range 08:00 to 17:00.", 0),
        END_TIME_IS_OUT_OF_CAL_RANGE("time", 33, "time.endTime.outrange",
                "Please re-enter the end time. It has to be in calendar range 08:00 to 18:00.", 0),
        APPT_TIME_OVERLAP("time", 34, "time.overlap",
                "Appointment times are overlapping. Please correct all overlapping records.", 0);
        private String frontendField;
        private Integer code;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }
    String EXCEPTION_PROCESSING_RECORDS = "Exception while processing this record";
    String CASE_MANAGER_ID_MANDATORY = "caseManagerId.mandatory";
    String OFFICE_ID_MANDATORY = "officeId.mandatory";
    String EFFECTIVE_DT_MANDATORY = "effectiveDt.mandatory";
    String INTERNAL_ERROR = "internal.error";
    String FILE_UPLOAD_FAILED = "File upload failed.";
    String HEADER_SHEET_MISSING = "Missing Sheet in the uploaded excel.";
    String HEADER_ROW_MISSING = "Missing Row in the uploaded excel.";
    String HEADER_WORKBOOK_MISSING = "Missing Workbook in the uploaded excel.";

    @Getter
    @AllArgsConstructor
    enum WaitlistErrorDetail implements ReseaErrorEnum {
        USR_ACCESS_APPLY_INVALID("waitlist", "waitlist.access.invalid",
                "User doesnot have access to apply waitlist.", 0),
        USR_ACCESS_CLEAR_INVALID("waitlist", "clear.access.invalid",
                "User doesnot have access to clear waitlist.", 0),
        CASE_EVENT_MANDATORY("caseEventId", "caseEventId.mandatory",
                "Case ID or Event ID is required.", 0),
        APPOINTMENT_DT_APPLY_FUTURE("eventDt", "eventDt.apply.future",
                "Appointment is must be a future event to apply waitlist.", 0),
        APPOINTMENT_DT_CLEAR_FUTURE("eventDt", "eventDt.clear.future",
                "Appointment is must be a future event to clear waitlist.", 0),
        APPOINTMENT_DT_IN_WAITLIST("eventDt", "eventDt.waitlist.exist",
                "Appointment Case is already on Waitlist.", 0),
        APPOINTMENT_DT_NO_WAITLIST("eventDt", "eventDt.waitlist.notexist",
                "Cannot Clear Waitlist. Appointment Case is not on Waitlist.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    String ACTIVITY_ID_MANDATORY = "activityId.mandatory";
    String FOLLOW_UP_TYPE_MANDATORY = "followUpType.mandatory";
    String FOLLOW_UP_COMP_BY_DT_MANDATORY = "followUpCompByDt.mandatory";
    String FOLLOW_UP_COMP_DT_MANDATORY = "completionDt.mandatory";
    String FOLLOW_UP_COMPLETE_MANDATORY = "complete.mandatory";
    @Getter
    @AllArgsConstructor
    enum FollowUpErrorDetail implements ReseaErrorEnum {
        USR_ACCESS_ADD_INVALID("followup", "add.access.invalid",
                "User doesnot have access to add follow-up.", 0),
        USR_ACCESS_EDIT_INVALID("followup", "edit.access.invalid",
                "User doesnot have access to edit follow-up.", 0),
        FOLLOW_UP_COMP_DT_MANDATORY("completionDt", "completionDt.mandatory",
                "Completion Date is mandatory.", 0),
        FOLLOW_UP_COMP_BY_DT_FUTURE("followUpCompByDt", "followUpCompByDt.future",
                "Follow-up Complete By Date has to be a future date.", 0),
        FOLLOW_UP_COMP_DT_FUTURE("completionDt", "completionDt.future",
                "Completion Date cannot be a future date.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum CreateActivityErrorDTODetail implements ReseaErrorEnum {
        CREATE_ACTIVITY_TECH_ERROR("createActivity", "createActivity.failed.techError",
                "Technical error encountered. Please contact Wecare with reference #: {0}", 1),
        CREATE_ACTIVITY_DATA_INCONSIST("createActivity", "createActivity.failed.dataInconsistencies",
                "Unavailable to process this request due to data inconsistencies. Please contact Wecare with reference #: {0}", 1),
        CREATE_ACTIVITY_EXCEPTION("createActivity", "createActivity.failed.exception",
                "An exception has occurred. Please contact Wecare with reference #: {0}", 1);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum AddActivityErrorDTODetail implements ReseaErrorEnum {
        USER_ACCESS_INVALID("addActivity", "user.access.invalid",
                "User does not have access to Add New Activity for Case.", 0),
        CASE_MANAGER_ACCESS_INVALID("addActivity", "caseManager.access.invalid",
                "User does not have access to Add New Activity for another case manager.", 0),
        ADD_TIME_INVALID("addTime", "addTime.invalid",
                "Activity Time is invalid.", 0),
        ADD_DATE_TIME_FUTURE("addDate", "addDateTime.future.invalid",
                "Activity Date & Time cannot be in the future.", 0),
        ADD_DATE_TIME_PRIOR_CASE("addDate", "addDateTime.priorCase",
                "Activity Date & Time cannot be prior to RESEA Case Creation.", 0),
        TRAINING_DETAIL_MANDATORY("trainingDetails", "trainingDetails.mandatory",
                "Training Details are mandatory.", 0),
        TRAINING_PROG_NAME_MANDATORY("progName", "trainingDetails.progName.mandatory",
                "Training Program Name is mandatory.", 0),
        TRAINING_PROVIDER_NAME_MANDATORY("providerName", "trainingDetails.providerName.mandatory",
                "Training Provider Name are mandatory.", 0),
        TRAINING_START_DT_MANDATORY("startDate", "trainingDetails.startDate.mandatory",
                "Training Start Date is mandatory.", 0),
        TRAINING_END_DT_MANDATORY("endDate", "trainingDetails.endDate.mandatory",
                "Training End Date is mandatory.", 0),
        TRAINING_START_DT_PRIOR_CASE("startDate", "trainingDetails.startDate.priorCase",
                "Training Start Date cannot be prior to RESEA Case Creation.", 0),
        TRAINING_END_DT_PRIOR_START_DT("endDate", "trainingDetails.endDate.priorStartDt",
                "Training End Date cannot be prior to Start Date.", 0),
        TERMINATION_DETAIL_MANDATORY("terminationDetail", "terminationDetail.mandatory",
                "Termination Detail is mandatory.", 0),
        ISSUE_DETAIL_MANDATORY("issueDetails", "issueDetails.mandatory",
                "Issue Details are mandatory.", 0),
        ISSUE_SUB_TYPE_MANDATORY("issueSubType", "issueDetails.issueSubType.mandatory",
                "Issue Sub Type is mandatory.", 0),
        ISSUE_START_DT_MANDATORY("issueStartDt", "issueDetails.startDt.mandatory",
                "Issue Start Date is mandatory.", 0),
        ISSUE_START_DT_PRIOR_CASE("issueStartDt", "issueDetails.startDt.priorCase",
                "Issue Start Date cannot be prior to RESEA Case Creation.", 0),
        ISSUE_START_DT_BYE_DT("issueStartDt", "issueDetails.startDt.pastClmByeDt",
                "Issue Start Date cannot be past RESEA Case Claim Bye Date.", 0),
        ISSUE_END_DT_MANDATORY("issueEndDt", "issueDetails.endDt.mandatory",
                "Issue End Date is mandatory.", 0),
        ISSUE_END_DT_PRIOR_START_DT("issueEndDt", "issueDetails.endDt.priorStartDt",
                "Issue End Date cannot be prior to Start Date.", 0),
        CORR_NOT_SELECTED("correspondence", "correspondence.notSelected",
                "Generate Correspondence Check box is not selected.", 0),
        CORR_STANDARD_TXT_MANDATORY("standardText", "standardText.mandatory",
                "Correspondence Standard Text is mandatory.", 0),
        STAFF_NOTES_NOT_PRESENT("staffNotes", "staffNotes.notPresent",
                "Staff Notes is mandatory when 'Include in Claimant Notes for non-RESEA Users' checkbox is selected.", 0);
        private String frontendField;
        private String frontendErrorCode;
        private String description;
        private Integer params;
    }
}
