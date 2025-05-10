package com.ssi.ms.resea.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface ReseaConstants {

    enum ITEMS_COMPLETED_IN_JMS {
        initialAssesment, eriOneOnOne, elmiServices,
        jobDevelopment, caseManagement, attendedResea,
        outsideWebReferral, developIEP, wioaStateLocalTraining,
        jmsJobReferral, addSelfCaseManager, jmsCaseNotes,
        jmsRegistrationComplete, jmsRegIncompleteWarning,
        activeResume, activeVirtualRecruiter,
        wagnerPeyserApplComplete, sentWagnerPeyserSigneture,
        sendIEPSignature
    }

    enum APPOINTMENT_TYPE {
        InitialAppointment,
        FirstSubsequentAppointment,
        SecondSubsequentAppointment
    }
    enum ACTIONS_TAKEN {
        reviewdChapters1To4, assignedReemploymentPlan,
        physicalIDVerification, remindCmtSelfSchedule
    }
    enum PROCESS_STATUS {
        U,
        P,
        F,
        S
    }

    enum INDICATOR {
        Y,
        N
    }

    enum RSCA_SYNOPSIS_TYPE {
        A,
        R,
        N
    }

    enum WORK_SCHEDULE_TYPE {
        F,
        P
    }

    enum KPI_PERIOD_RANGE{
    	THREE_MONTHS,SIX_MONTHS,ONE_YEAR
    }

    @Getter
    @AllArgsConstructor
    enum WORK_SCHEDULE {
        FULL_TIME("F", "Full Time"),
        PART_TIME("P", "Part Time");
        private String code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    enum MEETING_MODE {
        IN_PERSON("I", "In-person"),
        VIRTUAL("V", "Virtual");
        private String code;
        private String description;
    }

    @Getter
    @AllArgsConstructor
    enum RSII_DEC_DETECT_DT_IND {
        SYSTEM_DT("S", "System Date"),
        APPOINTMENT_DT("A", "Appointment Date");
        private String code;
        private String description;
    }


    String DATE_FORMAT_VALIDATOR = "^\\d{1,2}/\\d{1,2}/\\d{2,4}$";

    MessageFormat APPENDUSERCOMMENTFORMAT = new MessageFormat("{0}<br/>[{1} - {2}]: {3}");
    //{Old Comment}<br/>[{User Name} - {Date Time}]: {User Comment}
    MessageFormat APPENDUSERCOMMENTFORMAT_REVERSE = new MessageFormat("[{1} - {2}]: {3}<br/>{0}");
    //[{User Name} - {Date Time}]: {User Comment}<br/>{Old Comment}
    MessageFormat ADDUSERCOMMENTFORMAT = new MessageFormat("[{1} - {2}]: {3}");
    //[{User Name} - {Date Time}]: {User Comment}

    MessageFormat APPEND_USERCOMMENT_REVERSE_CHRONOLOGICAL_FORMAT = new MessageFormat("{0}: {1}<br/>{2}");
    //{Date}: {User Comment}<br/>{Old Comment}

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    String DATE_TIME_FORMAT_24H = "MM/dd/yyyy HH:mm";

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    int GENERATED_COMMENT_LENGTH = 4000;

    Function<String[], String> GENERATECOMMENTS = (comments) -> {
        if (comments == null || comments.length == 0) {
            // throw new InvalidFormatException("Invalid Comment Arguments");
            System.out.println("Invalid Comment Arguments");
        }
        String formattedComment;
        if (StringUtils.isNotBlank(comments[0])) {
            formattedComment = APPENDUSERCOMMENTFORMAT_REVERSE.format(comments).trim();
        } else {
            formattedComment = ADDUSERCOMMENTFORMAT.format(comments).trim();
        }
        return formattedComment.length() > GENERATED_COMMENT_LENGTH
                ? formattedComment.substring(0, GENERATED_COMMENT_LENGTH)
                : formattedComment;
    };

    //Reverse chronological order - In order from newest to oldest.
    Function<String[], String> GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER = (comments) -> {
        if (comments == null || comments.length == 0) {
            System.out.println("Invalid Comment Arguments");
        }
        String formattedComment;
        formattedComment = APPEND_USERCOMMENT_REVERSE_CHRONOLOGICAL_FORMAT.format(comments).trim();
        return formattedComment.length() > GENERATED_COMMENT_LENGTH
                ? formattedComment.substring(0, GENERATED_COMMENT_LENGTH)
                : formattedComment;
    };

    long RSIC_CAL_EVENT_TYPE_DO_NOT_SCHEDULE_ALV = 5628L; //9100L;
    long RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV = 5629L; //9101L;
    long RSIC_CAL_EVENT_TYPE_IN_USE_ALV = 5630L; //9102L;
    long RSIC_CAL_EVENT_TYPE_UNUSED_ALV = 5631L; //9103L;
    long RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV = 5632L; //9113L;
    long RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV = 5633L; //9114L;
    long RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV = 5634L; //9115L;
    long RSIC_TIMESLOT_USAGE_TIMEOFF_ALV = 5635L; //9111L;
    long RSIC_TIMESLOT_USAGE_HOLIDAY_ALV = 5636L; //9112L;
    long RSRM_MEETING_CATG_CD_RESEA_ONR_ON_ONE = 5623L;

    String RTW_BY_USING = "RESEA_RTW";
    String RTW_SAVED = "RTW Saved Successfully";


    long RSCS_STAGE_PRE_RESEA = 5596L;
    long RSCS_STAGE_INITIAL_APPT = 5597L;
    long RSCS_STAGE_FIRST_SUBS_APPT = 5598L;
    long RSCS_STAGE_SECOND_SUBS_APPT = 5599L;
    long RSCS_STAGE_TERMINATED_APPT = 5600L;
    Long RSID_MRP_CHAPTER_1_TO_4 = 5705L;
    Long RSID_MRP_CHAPTER_5_TO_10 = 5706L;

    Long RSIC_SCHEDULED_BY_STAFF = 5638L;
    Long RSII_ISSUE_ID_DURING_WS_REVIEW = 5652L;
    Long RSII_ISSUE_ID_DURING_INITIAL_APPT = 5674L;
    Long RSII_ISSUE_ID_DURING_FIRST_SUB_APPT = 5675L;
    Long RSII_ISSUE_ID_DURING_SECOND_SUB_APPT = 5676L;
    Long RSII_DEC_IFK_RSRS = 5678L;
    Long RSII_DEC_IFK_RSID = 5679L;
    Long RSII_DEC_IFK_RSIC = 5680L;
    Long RSII_DEC_IFK_RSWR = 5681L;
    Long REA_ATTENDANCE_RAT = 4375L;
    Long RSJR_SOURCE_JMS_REFERRAL = 5653L;
    Long RSJR_SOURCE_OUTSIDE_WEB_REFERRAL = 5654L;

    Long RSIC_MTG_STATUS_SCHEDULED = 5640L;
    Long RSIC_MTG_STATUS_COMPLETED = 5641L;
    Long RSIC_MTG_STATUS_COMPLETED_RTW = 5757L;
    Long RSIC_MTG_STATUS_FAILED = 5642L;
    Long RSIC_MTG_STATUS_FAILED_RTW = 5697L;

    Long RSIC_NOTICE_STATUS_NA = 5645L;

    String INITIAL_REQ_SAVED = "Initial Appointment Request Saved Successfully";
    String INITIAL_REQ_FAILED = "Initial Appointment Request Failed";
    String FIRST_ONE_ON_ONE_REQ_SAVED = "First Subsequent One-On-One Appointment Request Saved Successfully";
    String FIRST_ONE_ON_ONE_REQ_FAILED = "First Subsequent One-On-One Appointment Request Failed";
    String SECOND_ONE_ON_ONE_REQ_SAVED = "Second Subsequent One-On-One Appointment Request Saved Successfully";
    String SECOND_ONE_ON_ONE_REQ_FAILED = "Second Subsequent One-On-One Appointment Request Failed";
    String APPOINTMENT_SCHEDULE_SUCCESS = "Appointment Scheduled Successfully";

    String INITIAL_APPOINTMENT_MINUTES = "RESEA_FIRST_1_ON_1";

    String FIRST_SUBS_APPOINTMENT_MINUTES = "RESEA_SECOND_1_ON_1";

    String SECOND_SUBS_APPOINTMENT_MINUTES = "RESEA_THIRD_1_ON_1";

    String RESEA_DEADLINE_DAYS = "RESEA_DEADLINE_DAYS";
    Integer ROL_LOCAL_OFFICE_MANAGER = 54;
    Integer ROL_RESEA_CASE_MANAGER = 94;
    Integer ROL_RESEA_PROG_STAFF = 95;
    Integer ROL_SUPERUSER = 29;
    //Reschedule page starts
    String RESCHEUDLE_REQ_SAVED = "Rescheduled Request Saved Successfully";

    String RESCHEDULE_BY_USING = "RESEA_Reschedule";
    //Reschedule page ends

    //Switch meeting mode starts
    Long RAT_MEETING_MODE_CHG_RSN_CD = 904L;

    String SWITCH_MEETING_MODE_BY_USING = "RESEA_SWITCH_MEETING_MODE";

    String SWITCH_MEETING_MODE_SAVED = "Switch Meeting Mode Saved Successfully";

    long REASONS_SWITCH_MEETING_MODE_OTHERS = 5696L;

    //Switch meeting mode ends
    //NO SHOW STARTS
    String NO_SHOW_BY_USING = "RESEA_NO_SHOW";

    String NO_SHOW_SAVED = "No Show Saved Successfully";

    String ACTIVITY_REOPEN_TURNED_OFF_SUCCESS = "Create Activity for Reopen Turned Off type successfully";

    String NO_SHOW_FAILED = "No Show failed";
    //NO SHOW ENDS

    Map<Long, Long> RSIC_USAGE_TO_RSCS_STAGE_MAP = Map.of(
            RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV, RSCS_STAGE_INITIAL_APPT,
            RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV, RSCS_STAGE_FIRST_SUBS_APPT,
            RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV, RSCS_STAGE_SECOND_SUBS_APPT
    );

    Map<Long, Long> RSCS_STAGE_TO_RSIC_USAGE_MAP = Map.of(
            RSCS_STAGE_INITIAL_APPT, RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV,
            RSCS_STAGE_FIRST_SUBS_APPT, RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV,
            RSCS_STAGE_SECOND_SUBS_APPT, RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV
    );

    Long RSCS_STATUS_SCHEDULED = 5601L;
    Long RSCS_STATUS_WAITLIST_REQ = 5602L;
    Long RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF = 5603L;
    Long RSCS_STATUS_FAILED = 5605L;
    Long RSCS_STATUS_COMPLETED = 5604L;
    Long RSCS_STATUS_FAILED_RTW = 5606L;
    Long RSCS_STATUS_RTW = 5609L;

    Long RSCS_STATUS_SECOND_SUB_COMPLETED = 5611L;
    Long RSCS_STATUS_BENEFIT_YEAR_ENDED = 5612L;

    enum METRIC_TYPE {
        initialAppointment("Initial Appointment"),
        firstSubsequentAppointment("1st Subsequent Appointment"),
        secondSubsequentAppointment("2nd Subsequent Appointment"),
        followUp("followUp"),
        hiPriority("hiPriority"),
        failed("failed"),
        delayed("delayed"),
        All("All"),
        pendingSchedule("pendingSchedule"),
        waitlisted("waitlisted");
        public final String label;

        METRIC_TYPE(String label) {
            this.label = label;
        }
    }

    Map<String, String> RSID_INDICATOR_MAP = new HashMap<String, String>() {{
        put("InitialAssessment", "setRsidJms102Ind");
        put("Eri1On1", "setRsidJms106Ind");
        put("ELMIServices", "setRsidJms107Ind");
        put("JobDevelopment", "setRsidJms123Ind");
        put("CaseManagement", "setRsidJms153Ind");
        put("AttendedRESEA", "setRsidJms160Ind");
        put("OutsideWebReferral", "setRsidJms179Ind");
        put("DevelopIEP", "setRsidJms205Ind");
        put("ReferWIOATraining", "setRsidJms209Ind");
        put("JMSJobReferral", "setRsidJms500Ind");
        put("AddSelfCaseManager", "setRsidJmsSelfCmInd");
        put("JMSCaseNotes", "setRsidJmsCaseNotesInd");
        put("JMSRegComplete", "setRsidJmsRegCompltInd");
        put("JMSRegIncomplete", "setRsidJmsRegIncompInd");
        put("ActiveResume", "setRsidJmsResumeInd");
        put("ActiveVirtualRecuiter", "setRsidJmsVRecruiterInd");
        put("WagnerPeyserApplComplete", "setRsidJmsWpApplInd");
        put("WagnerPeyserApplSignature", "setRsidJmsWpApplSigInd");
        put("IEPSignatureCopy", "setRsidJmsIepSigInd");
        put("ReferToVRorDHHS", "setRsidJmsVrDhhsInd");
        put("CloseGoals", "setRsidJmsClseGoalsInd");
        put("JmsCloseIEP", "setRsidJmsClseIepInd");
        put("EPandCheckListUpld", "setRsidJmsEpcklstUplInd");
        put("ReviewedReEmpPlan", "setRsidMrpRvwdInd");
        put("AssignedReEmpPlan", "setRsidMrpAssgndInd");
        put("PhysicallyVerifiedID", "setRsidIdVerifiedInd");
        put("RemindedSelfSchedule", "setRsidSlfSchRmdrInd");
        put("CheckedPriorJobReferrals", "setRsidPrevRefrlCcfInd");
        put("EsConfirm", "setRsidEsConfirmInd");
    }};

    String MODULE_NAME_RESEA = "resea";

    String PAGE_NAME_RESEA_RESCHEDULE = "reschedule";

    String PAGE_NAME_RESEA_APPOINTMENT_DET = "appointmentDetails";
    String PAGE_NAME_RESEA_NEW_ACTIVITY = "newActivity";
    Long COUNTRY_CD_USA = 47L;

    Long COUNTRY_CD_CANADA = 48L;

    String SUCCESS = "Success";

    String UPDATED_USING_CASE_REASSIGN = "Case Reassign";

    long STUN_STATUS_REQUESTED = 5660L;
    long STUN_STATUS_APPROVED = 5661L;
    long RSCS_REASSIGN_REASON_OTHER_STAFF_UNAVAILABLITY = 5613L;
    long RSCS_REASSIGN_REASON_WORKLOAD = 5614L;
    long RSCS_REASSIGN_REASON_SICK = 5708L;
    long RSCS_REASSIGN_REASON_PREMISES_CLOSED = 5709L;
    long RSCS_REASSIGN_REASON_CONFERENCE = 5752L;
    long RSCS_REASSIGN_REASON_MEETING = 5753L;
    long RSCS_REASSIGN_REASON_TRAINING = 5754L;
    long RSCS_REASSIGN_REASON_UNION_TIME = 5755L;
    long RSCS_REASSIGN_REASON_VACATION = 5756L;
    long STUN_REASON_SICK = 5698L;
    long STUN_REASON_WORKLOAD = 5699L;
    long STUN_REASON_UNION_TIME = 5700L;
    long STUN_REASON_TRAINING = 5701L;
    long STUN_REASON_VACATION = 5702L;
    long STUN_REASON_CONFERENCE = 5703L;
    long STUN_REASON_MEETING = 5704L;
    long STUN_REASON_OTHER = 5747L;

    Map<Long, Long> CASE_REASSIGN_STUN_MAP = new HashMap<>(){{
        put(RSCS_REASSIGN_REASON_WORKLOAD, STUN_REASON_WORKLOAD);
        put(RSCS_REASSIGN_REASON_OTHER_STAFF_UNAVAILABLITY, STUN_REASON_OTHER);
        put(RSCS_REASSIGN_REASON_SICK, STUN_REASON_SICK);
        put(RSCS_REASSIGN_REASON_PREMISES_CLOSED, STUN_REASON_OTHER);
        put(RSCS_REASSIGN_REASON_CONFERENCE, STUN_REASON_CONFERENCE);
        put(RSCS_REASSIGN_REASON_MEETING, STUN_REASON_MEETING);
        put(RSCS_REASSIGN_REASON_TRAINING, STUN_REASON_TRAINING);
        put(RSCS_REASSIGN_REASON_UNION_TIME, STUN_REASON_UNION_TIME);
        put(RSCS_REASSIGN_REASON_VACATION, STUN_REASON_VACATION);
    }};

    Map<String, Long> RSID_MRP_CHAPTER_MAP = new HashMap<>(){{
        put("Chapter1To4", RSID_MRP_CHAPTER_1_TO_4);
        put("Chapter5To10", RSID_MRP_CHAPTER_5_TO_10);
    }};

    Map<Long, String> RSID_MRP_CHAPTER_MAP_TXT = new HashMap<>(){{
        put(RSID_MRP_CHAPTER_1_TO_4, "Chapter1To4");
        put(RSID_MRP_CHAPTER_5_TO_10, "Chapter5To10");
    }};

    String CREATE_ACTIVITY_FAILED_MSG = "Request Processed. However, case activity could not created.  Please Contact WeCare.";

    String RSCA_DESC_RTW = "Claimant has returned to work.";

    String RSCA_CALLING_PROGRAM_RTW = "API_RTW";


    String RSCA_CALLING_PROGRAM_RESCHEDULE = "API_RSCH";

    String RSCA_CALLING_PROGRAM_REASSIGN = "API_RASG";

    String RSCA_CALLING_PROGRAM_COMPLETE = "API_COMP";

    String PAR_CURR_APP_CLSOUT_TIME = "CURR_APP_CLSOUT_TIME";

    String PAR_RESEA_REOPN_FUT_DAY = "RESEA_REOPN_FUT_DAY";
    String PAR_CURR_APP_PREP_TIME = "CURR_APP_PREP_TIME";

    String LAST_UPDT_USING_AVAILABLE = "API_AVAL";

    String PAR_RESEA_NO_CCF_WEEKS = "RESEA_NO_CCF_WEEKS";

    String PAR_NEW_RESEA_CUTOFF_DT = "NEW_RESEA_CUTOFF_DT";

    String RSCA_CALLING_PROGRAM_SWITCH_MTG_MODE = "API_MTGMD";

    String RSCA_CALLING_PROGRAM_NO_SHOW = "API_NOSHOW";

    String RSCA_CALLING_PROGRAM_REOPEN = "API_REOPEN";

    String PAR_RESEA_RTW_PAST_DAYS = "RESEA_RTW_PAST_DAYS";

    String PAR_RESEA_RTW_FUT_DAYS = "RESEA_RTW_FUT_DAYS";

    String PAR_RESEA_CAL_LAPSE_TIME = "RESEA_CAL_LAPSE_TIME";

    @Getter
    @AllArgsConstructor
    enum ACCESS_TYPE {
        Y ("Y", "Yes - Edit/View"),
        N ("N", "No Access"),
        V ("V", "View Only");
        final String code;
        final String description;
    }

    String RTW_VIEW_MODE = "View";

    String RTW_INSERT_MODE = "Insert";

    String RSCS_PRIORITY_HI = "HI";

    String DATE_01_01_2000 = "01/01/2000";

    @Getter
    @AllArgsConstructor
    enum SUNR_TYPE_IND {
        O ("O", "One-Time"),
        R ("R", "Reccuring");
        final String code;
        final String description;
    }
    String YES_OR_NO_IND_Y = "Y";
    String YES_OR_NO_IND_N= "N";
    
    String INDICATOR_WAITLISTED = "WL";
    String INDICATOR_HIGH_PRIORITY = "HI";
    String INDICATOR_LATE = "LATE";
    Long LOF_INTERSTATE = 14L;
    String LAST_UPDT_USING_SCH_INITIAL_APPT = "API_SCH_INITIAL_APPT";
    String SCH_INITIAL_APPT_SUCCESS = "Schedule Initial Appt Successfully";

    String DATE_FORMAT = "MM/dd/yyyy";

    String DATE_TIME_FORMAT_24H_SEC = "MM/dd/yyyy HH:mm:ss";

    int ERROR_MSG_LENGTH_250 = 250;
    int ERROR_MSG_INDEX_249 = 249;

    int RESEA_UPLOAD_HEADER_COLUMN_COUNT = 6;
    String DISCARD_HEADER_COMLUMN = "Missing header column in the uploaded excel. It has {0} columns instead of {1}.";
    String DISCARD_HEADER_MSG = "Missing Header Value. Invalid column no:";
    String DISCARD_HEADER_MISSING_MSG = "Upload Document is missing header.";
    String HEADER_ROW_MISSING = "Missing Row in the uploaded excel.";
    Pattern TIME_PATTERN_24H = Pattern.compile("\\d{2}:\\d{2}");
    Pattern TIME_PATTERN_12H = Pattern.compile("\\d{2}:\\d{2} (AM|PM)");
    Pattern ZOOM_LINK_URL = Pattern.compile("^https?://(www\\\\.)?zoom.\\\\.us/.*");
    String ZOOM_LINK_TOPIC = "Topic:";
    String ZOOM_LINK_MEETING_LINK = "Join Zoom Meeting:";
    String ZOOM_LINK_MEETING_ID = "Meeting ID:";
    String ZOOM_LINK_PASSCODE = "PASSCODE:";
    String ZOOM_LINK_DIAL_BY_LOC = "Dial by your location:";
    String ZOOM_LINK_FIND_LOC_NUM = "Find your local number:";

    String PAR_RESEA_INITIAL_APPT_DURATION = "RESEA_FIRST_1_ON_1";
    String PAR_RESEA_FIRST_SUB_APPT_DURATION = "RESEA_SECOND_1_ON_1";
    String PAR_RESEA_SEC_SUB_APPT_DURATION = "RESEA_THIRD_1_ON_1";

    Pattern PATTERN_PHONE_NBR = Pattern.compile("\\+1\\s\\d{3}\\s\\d{3}\\s\\d{4}");
    Pattern PATTERN_MEETING_ID = Pattern.compile("MEETING ID:\\s\\d{3}\\s\\d{4}\\s\\d{4}");
    Pattern PATTERN_PASSCODE = Pattern.compile("PASSCODE:\\s\\w+");
    Pattern PATTERN_OTHER_REF = Pattern.compile("FIND YOUR LOCAL NUMBER: HTTPS://[\\w\\./]+");
    String DAIL_BY_LOCATION = "DIAL BY YOUR LOCATION";

    String RSCS_REASSIGN_REASON_DO_NOT_BLOCK = "DO NOT BLOCK";

    Long RSIS_INTVW_TYPE_CD_INIT_APPT = 5620L;

    Long TRX_01_IFK_CD_STF = 5794L;
    Long TRX_TRAN_CD_CREATE_CAL = 5793L;
    Long TRX_STATUS_CD_NOT_PROCESSED = 926L;

    String TOPIC_INITIAL = "INITIAL MEETING";
    String TOPIC_1ST_SUB = "1ST SUBSEQUENT";
    String TOPIC_2ND_SUB = "2ND SUBSEQUENT";

    String space = " ";

    String REQUEST_FAILED_MSG = "Request not processed. Please Contact WeCare.";
    
    String REASSIGN_ALL_DECIPHER_CD = "ALLOW REASSIGN ALL";
    String API_NEW_ACTIVITY = "NEW_ACTIVITY";
    String ACTIVITY_TEMPLATE_TRN = "TRN";
    String ACTIVITY_TEMPLATE_TER = "TER";
    String ACTIVITY_TEMPLATE_ISS = "ISS";

    Map<String, String> ADD_ACTIVITY_STANDARD_TXT = new LinkedHashMap<>() {{
        put("Standard Text Placeholder 1", "Description for Standard Text Placeholder 1");
        put("Standard Text Placeholder 2", "Description for Standard Text Placeholder 2");
        put("Standard Text Placeholder 3", "Description for Standard Text Placeholder 3");
        put("Standard Text Placeholder 4", "Description for Standard Text Placeholder 4");
        }};
}
