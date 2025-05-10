package com.ssi.ms.resea.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public class ReseaAlvEnumConstant {

    @Getter
    @AllArgsConstructor
    public enum RsrwWorkModeCd  {
        HYBRID(5651L, "Hybrid"),
        ONSITE_ONLY(5650L, "Onsite"),
        REMOTE_ONLY(5649L, "Remote");
        private Long code;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public enum RsrsReschReasonCd {
        DOCTORS_APPOINTMENT_DEPENDENT(3159L, "Doctor's Appointment - Dependent"),
        DOCTORS_APPOINTMENT_SELF(3160L, "Doctor's Appointment - Self"),
        JOB_INTERVIEW(3163L, "Job Interview"),
        LOCAL_OFFICE_CONVENIENCE(3165L, "Local Office Convenience");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsrsReschEntityTypeCd {
        DOCTOR(5647L, "Doctor"),
        EMPLOYER(5648L, "Employer");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsicTimeslotUsageCd  {
        INITIAL_APPOINTMENT(5632L, "Initial Appointment"),
        FIRST_SUBS_APPOINTMENT(5633L, "1st Subsequent Appointment"),
        SECOND_SUBS_APPOINTMENT(5634L, "2nd Subsequent Appointment"),
        TIME_OFF(5635L, "Time-Off"),
        STATE_HOLIDAY(5636L, "State Holiday");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsisIntvwTypeCd  {
        INITIAL_APPOINTMENT(5620L, "Initial Appointment"),
        FIRST_SUBS_APPOINTMENT(5621L, "1st Subsequent Appointment"),
        SECOND_SUBS_APPOINTMENT(5622L, "2nd Subsequent Appointment");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsicCalEventTypeCd  {
        DO_NOT_SCHEDULE(5628L, "Do not schedule"),
        AVAILABLE(5629L, "Available"),
        IN_USE(5630L, "In Use"),
        UNUSED(5631L, "Unused");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsicMeetingStatusCd {
        SCHEDULED(5640L, "Scheduled"),
        COMPLETED(5641L, "Completed"),
        FAILED(5642L, "Failed"),
        FAILED_RTW(5697L, "Failed - Return to work"),
        COMPLETED_RTW(5757L, "Completed - Return to work");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RspsItemTypeCd {
        SCHEDULED(5553L, "Scheduled"),
        SCORED(5548L, "Scored");
        private Long code;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public enum RsiiIssueIdDuringCd {
        WS_REVIEW(5652L, "WS Review"),
        RESCHEDULE(5673L, "Reschedule"),
        INITIAL_APPT(5674L, "Initial Appt"),
        FIRST_SUBSEQUENT_APPT(5675L, "First Subsequent Appt"),
        SECOND_SUBSEQUENT_APPT(5676L, "Second Subsequent Appt"),
        SWITCH_MEETING_MODE(5677L, "Switch Meeting Mode"),
        NO_SHOW(5707L, "No Show");
        private Long code;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public enum RsiiDecIfkCd {
        RSRS(5678L, "RSRS"),
        RSID(5679L, "RSID"),
        RSIC(5680L, "RSIC"),
        RSWR(5681L, "RSWR");
        private Long code;
        private String description;
    }



    @Getter
    @AllArgsConstructor
    public enum RscaReferenceIfkCd {
        RSPS(5659L, "RSPS"),
        RSIC(5664L, "RSIC"),
        RSRS(5665L, "RSRS"),
        RSRW(5666L, "RSRW"),
        RSID(5667L, "RSID"),
        RSWR(5668L, "RSWR"),
        RSII(5669L, "RSII"),
        RSJR(5670L, "RSJR"),
        RSRM(5671L, "RSRM"),
        RSIS(5672L, "RSIS"),
        RSCS(5748L, "RSCS");
        private Long code;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public enum RscaTypeCd {
        APPROVED_TRAINING(5563L, "Approved training"),
        REFER_TO_TRAINING(5569L, "Refer to training"),
        REPORTED_TO_TRAINING_RESEA_TERMINATED(5733L, "Reported to training as referred: RESEA Terminated"),
        RETURNED_TO_WORK(5584L, "Returned to Work"),
        FAILED(5583L, "Failed"),
        CREATED_ISSUE(5586L, "Created Issue"),
        REVIEWED_WORK_SEARCH(5588L, "Reviewed Work Search"),
        CREATED_JOB_REFERRAL(5587L, "Created Job Referral"),
        UPDATED_JOB_REFERRAL(5761L, "Updated Job Referral"),
        DELETED_JOB_REFERRAL(5762L, "Deleted Job Referral"),
        COMPLETED(5582L, "Completed"),
        SCHEDULED(5570L, "Scheduled"),
        STAFF_SCHEDULED(5574L, "Staff-scheduled"),
        STAFF_RESCHEDULED(5577L, "Staff rescheduled"),
        STAFF_MODE_CHANGE(5581L, "Staff mode change"),
        CASE_REASSIGN(5594L, "Reassigned Case"),
        REOPEN_TURNED_ON(5759L, "Reopen turned on"),
        REOPEN_TURNED_OFF(5760L, "Reopen turned off"),
        PLACED_ON_WAITLIST(5590L, "Placed on Waitlist"),
        WAITLIST_CLEARED(5591L, "Waitlist Cleared");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsicScheduledByCd {
        CLAIMANT(5637L, "Claimant"),
        STAFF(5638L, "Staff"),
        SYSTEM(5639L, "System");
        private Long code;
        private String description;
    }



    @Getter
    @AllArgsConstructor
    public enum RsicNoticeStatusCd {
        TO_BE_SENT(5643L, "To be sent"),
        SENT(5644L, "Sent"),
        NA(5645L, "NA"),
        NO(5646L, "No");
        private Long code;
        private String description;
    }



    @Getter
    @AllArgsConstructor
    public enum RscaStageCd {
        PRE_RESEA(5710L, "PRE RESEA"),
        INITIAL_APT(5711L, "Initial Appointment"),
        FIRST_SUBS_APT(5712L, "1st Subsequent Appointment"),
        SECOND_SUBS_APT(5713L, "2nd Subsequent Appointment"),
        TERMINATED(5714L, "Terminated");
        private Long code;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public enum RscaStatusCd {

        SCHEDULED(5715L, "Scheduled"),
        WAITLIST_REQ(5716L, "Waitlist Requested"),
        PENDING_FURTHER_SCH_BY_STAFF(5717L, "Pending further scheduling by staff"),
        COMPLETED(5718L, "Completed"),
        FAILED(5719L, "Failed"),
        FAILED_RTW(5720L, "Failed- Returned to Work"),
        ON_PLACEHOLDER_SCH_WS_WAIVER(5721L, "On Placeholder schedule due to work search waiver"),
        REPORTED_TO_TRAINING(5722L, "Reported to training as referred"),
        COMPLETED_RTW(5723L, "Completed - Returned to Work"),
        NO_CC_FILED_RECENTLY(5724L, "No Continued Claims filed recently"),
        SECOND_SUB_APPT_COMPLETED(5725L, "2nd Subsequent appointment complete"),
        BYE(5726L, "Benefit year has ended"),
        RETURNED_TO_WORK(5780L, "Returned to Work"),
        INCORRECTLY_SELECTED_FOR_RESEA_PARTICIPATION(5766L, "Incorrectly selected for RESEA Participation"),
        OUT_OF_STATE_CLAIM(5785L, "Out of state claim"),
        BENEFIT_EXHAUSTED(5786L, "Benefit exhausted"),
        WITHDRAWN_CLM_BY_CMT(5787L, "Withdrawn claim by claimant (not a backdate)");

        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RscsStageCd {
        PRE_RESEA(5596L, "PRE RESEA"),
        INITIAL_APT(5597L, "Initial Appointment"),
        FIRST_SUBS_APT(5598L, "1st Subsequent Appointment"),
        SECOND_SUBS_APT(5599L, "2nd Subsequent Appointment"),
        TERMINATED(5600L, "Terminated");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RscsStatusCd {

        SCHEDULED(5601L, "Scheduled"),
        WAITLIST_REQ(5602L, "Waitlist Requested"),
        PENDING_FURTHER_SCH_BY_STAFF(5603L, "Pending further scheduling by staff"),
        COMPLETED(5604L, "Completed"),
        FAILED(5605L, "Failed"),
        FAILED_RTW(5606L, "Failed- Returned to Work"),
        ON_PLACEHOLDER_SCH_WS_WAIVER(5607L, "On Placeholder schedule due to work search waiver"),
        REPORTED_TO_TRAINING(5608L, "Reported to training as referred"),
        COMPLETED_RTW(5609L, "Completed - Returned to Work"),
        NO_CC_FILED_RECENTLY(5610L, "No Continued Claims filed recently"),
        SECOND_SUB_APPT_COMPLETED(5611L, "2nd Subsequent appointment complete"),
        BYE(5612L, "Benefit year has ended"),
        RETURNED_TO_WORK(5781L, "Returned to Work"),
        INCORRECTLY_SELECTED_FOR_RESEA_PARTICIPATION(5770L, "Incorrectly selected for RESEA Participation"),
        OUT_OF_STATE_CLAIM(5782L, "Out of state claim"),
        BENEFIT_EXHAUSTED(5783L, "Benefit exhausted"),
        WITHDRAWN_CLM_BY_CMT(5784L, "Withdrawn claim by claimant (not a backdate)");

        private Long code;
        private String description;
    }




    @Getter
    @AllArgsConstructor
    public enum RscnNoteCategoryCd {
        PRE_RESEA(5734L, "PRE RESEA"),
        INITIAL_APT(5735L, "Initial Appointment"),
        FIRST_SUBS_APT(5736L, "1st Subsequent Appointment"),
        SECOND_SUBS_APT(5737L, "2nd Subsequent Appointment"),
        TERMINATED(5738L, "Terminated");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum LofBuTypeCd {
        LOCAL_OFFICE(2500L, "Local Office");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public
    enum ALC_TYPE {
        RSCA_TYPE_CD(883L,"activity-type"),
        RSCS_STAGE_CD(884L, "case-stage"),
        RSCS_STATUS_CD(885L, "case-status"),
        RSCS_REASSIGN_REASON_CD(886L, "reassign-reasons"),
        RSCS_REASSIGN_REASON_CD_REASSIGN_ALL(886L, "reassign-all-reasons"),
        RSCS_CLOSED_REASON_CD(887L, "case-close-reason"),
        RSIS_INTVW_TYPE_CD(888L, "interview-types"),
        RSIC_CAL_EVENT_TYPE_CD(891L, "event-type"),
        RSIC_TIMESLOT_USAGE_CD(892L, "event-usage"),
        RSIC_MTG_STATUS_CD(894L, "meeting-status"),
        RSIC_SCHEDULED_BY_CD(893L, "scheduled-by"),
        SUNR_STATUS_CD(911L, "unavailablity-status"),
        STUN_REASON_TYPE_CD(905L, "unavailablity-reason"),
        RSCS_NXT_FOL_UP_TYPE_CD(913L, "followup-type"),
        CORR_STANDARD_TXT(914L, "corr-standard-text");
        private final Long code;
        @Getter
        private final String description;

        public static ALC_TYPE getByDescription(String description) {
            return Arrays.stream(ALC_TYPE.values())
                    .filter(alcType -> alcType.getDescription().equalsIgnoreCase(description))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No ALC exists with description " + description));
        }
    }



    @Getter
    @AllArgsConstructor
    public enum SUNR_STATUS_CD {
        REQUESTED(5772L, "Requested"),
        APPROVED(5773L, "Approved"),
        REJECTED(5774L, "Rejected"),
        WITHDRAWN(5771L, "Withdrawn");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum SUNR_REASON_CD {
        REQUESTED(5772L, "Requested"),
        APPROVED(5773L, "Approved"),
        REJECTED(5774L, "Rejected"),
        WITHDRAWN(5771L, "Withdrawn");
        private final Long code;
        private final String description;
    }
    @Getter
    @AllArgsConstructor
    public enum EAD_TYPE_CD {
        CORPORATE(492L, "Corporate");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum EMP_SOURCE_CD {
        UC_TAX(397L, "UC Tax"),
        UIM_UNIT(417L, "UIM - Unit"),
        UIN_CHARGABLE_ACC(1049L, "UIM - Chargeable Account");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RusmStatusCd {
        INITIATED(4880L, "To be initiated"),
        UPLOADED(4881L, "File uploaded"),
        UPLOAD_FAILED(4882L, "File upload failed"),
        PARSING_STARTED(4883L, "File parsing started"),
        PARSING_FAILED(4884L, "File parsing failed"),
        PARSING_COMPLETE(4885L, "File parsing completed"),
        STAGING_INITIATED(4886L, "Transfer from Staging Initiated"),
        COMPLETE(4887L, "Upload process completed");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RucsStatusCd {
        TO_BE_INITIATED(5795L),
        UPLOAD_IN_PROCESS(5796L),
        INITIATED(5797L),
        UPLOADED_PENDING_FINAL(5798L),
        FINAL(5799L),
        DISCARDED(5800L);
        private final Long code;
    }
    @Getter
    @AllArgsConstructor
    public enum UsrStatusCd {
        ACTIVE(1724L, "Active");
        private final Long code;
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsrmMeetingTypeCd  {
        INITIAL_APPOINTMENT(5625L, "Initial Appointment"),
        FIRST_SUBS_APPOINTMENT(5626L, "1st Subsequent Appointment"),
        SECOND_SUBS_APPOINTMENT(5627L, "2nd Subsequent Appointment");
        private Long code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    public enum RsjrSourceCd  {
        JMS_REFERRAL(5653L, "JMS Referral"),
        OUTSIDE_WEB_REFERRAL(5654L, "Outside Web Referral");
        private Long code;
        private String description;
    }
}