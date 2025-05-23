package com.ssi.ms.collecticase.constant;

import java.text.SimpleDateFormat;

public interface CollecticaseConstants {

    enum INDICATOR {
        Y,
        N,
        C
    }

    String DATE_FORMAT = "MM/dd/yyyy";

    String TIME_FORMAT_12_HR_AM = "hh:mm a";

    String SEPARATOR = "\\^[$]+";

    String NON_REGEX_SEPARATOR = "^$";

    //Activity Entity Type

    String EMP_STRING = "EMP";

    String REPRESENTATIVE_IND_STRING = "REP(I)";

    String NEW_REPRESENTATIVE_IND_STRING = "New REP(I)";

    //Country
    Long UNITED_STATES = 47L;
    Long CANANDA = 48L;
    String COUNTRY_ID = "countryId";
    String COUNTRY_NAME = "countryName";
    String UNITED_STATES_NAME = "United States";
    String CANADA_NAME = "Canada";

    Long ENTITY_PREFERENCE_FAX = 4079L;
    Long ENTITY_CONTACT_PREFERENCE_WORK_PHONE = 4083L;
    Long ENTITY_CONTACT_PREFERENCE_CELL_PHONE = 4084L;
    Long ENTITY_CONTACT_PREFERENCE_HOME_PHONE = 4085L;
    Long ENTITY_CONTACT_PREFERENCE_FAX = 4086L;
    Long ENTITY_CONTACT_PREFERENCE_EMAIL = 4087L;

    Long ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT = 3861L;
    Long ACTIVITY_TYPE_CMT_NO_LONGER_EMPLOYED = 3863L;
    Long ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE = 3865L;

    Long ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE = 3867L;
    Long ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT = 3868L;
    Long ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE = 3869L;
    Long ACTIVITY_TYPE_SENT_NOTICE_OF_WG = 3870L;

    Long ACTIVITY_TYPE_FIN_AFF_EMPLOYED_CMT = 3871L;

    Long ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT = 3874L;
    Long ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP = 3875L;

    Long ACTIVITY_TYPE_IRORA_SUBMISSION = 3878L;
    Long ACTIVITY_TYPE_OTHER_ENTITY_CONTACT = 3880L;
    Long ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT = 3881L;
    Long ACTIVITY_TYPE_RECIEVED_PAYMENT_NO_SIGNED_PP = 3882L;
    Long ACTIVITY_TYPE_RECIEVED_PP_OFFSET = 3883L;
    Long ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT = 3884L;
    Long ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY = 3885L;
    Long ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT = 3887L;
    Long ACTIVITY_TYPE_RECORD_NEW_FOLLOW_UP = 3889L;
    Long ACTIVITY_TYPE_REOPEN_CASE = 3891L;
    Long ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT = 3892L;

    Long ACTIVITY_TYPE_RESEARCH_IB8606 = 3893L;
    Long ACTIVITY_TYPE_RESEARCH_NH_PROPERTY = 3894L;

    Long ACTIVITY_TYPE_PP_FIXED = 3911L;

    Long ACTIVITY_TYPE_SENT_TEMP_PP_REDUCTION_LTR = 3913L;
    Long ACTIVITY_TYPE_SENT_TEMP_PP_SUSPENSION_LTR = 3914L;
    Long ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT = 3915L;

    Long ACTIVITY_TYPE_ADD_UPD_ATTY_CONTACT = 3916L;
    Long ACTIVITY_TYPE_ADD_UPD_EMP_CONTACT = 3918L;
    Long ACTIVITY_TYPE_ADD_UPD_OTHER_REP_CONTACT = 3919L;

    Long ACTIVITY_TYPE_USER_ALERT_INITIATE_EMP_NC = 3922L;

    Long ACTIVITY_TYPE_USER_ALERTED_RESEARCH_POT_LIEN = 3924L;
    Long ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS = 4104L;

    Long ACTIVITY_TYPE_ASSIGN_TO_SELF = 4324L;

    Long ACTIVITY_TYPE_PP_VARIABLE = 4071L;

    Long ACTIVITY_TYPE_PP_OFFSET = 4072L;

    Long PAYMENT_RESPONSE_REASON_OTHER = 4075L;

    Long CASE_STATUS_OPEN = 3930L;
    Long CASE_STATUS_CLOSED = 3931L;
    Long CASE_STATUS_REOPEN = 3932L;

    Long COMM_METHOD_NOT_APPLICABLE = 3594L;

    //Wage Garnishment Source
    Long WAGE_GARNISH_SOURCE = 4100L;

    //Stored Procedure Return Value
    Long RETURN_SUCCESS = 1L;
    Long RETURN_FAILURE = 0L;
    Integer NHUIS_RETURN_SUCCESS = 0;
    Integer NHUIS_RETURN_FAILURE = 1;

    SimpleDateFormat TIME_FORMAT_INPUT = new SimpleDateFormat(
            "hh:mma");

    SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "hh:mm a");

    String GENERAL_ACTIVITY_SAVED = "General Activity Info Saved Successfully";

    String GENERAL_ACTIVITY_FAILED = "General Activity Request Failed";

    String CREATE_ACTIVITY_SUCCESSFUL = "Activity Created Successfully";

    //Activity Specifics For Temp Suspension

    String ACTIVITY_SPECIFICS_TEMP_REDUCTION = "Manual Temp PP Reduction With Lien Sent";

    String ACTIVITY_SPECIFICS_TEMP_SUSPENSION = "Manual Temp PP Suspension With Lien Sent";

    String ACTIVITY_DETAILS_GENERAL = "ACTIVITY_DETAILS_GENERAL";

    String GENERAL_ACTIVITY_TEMPLATE = "GEN";

    String PP_ACTIVITY_TEMPLATE = "PP";

    String WG_ACTIVITY_TEMPLATE = "WG";

    String UC_ACTIVITY_TEMPLATE = "UC";

    String ONLINE_COE_TXT = "O";

    String TILE_SYMBOL = "~";

    Long COR_STATUS_CD_NOT_PROCESSED = 1424L;

    Long COR_STATUS_CD_PROCESSED = 1425L;

    // Remedy Name
    Long REMEDY_SECOND_DEMAND_LETTER = 3970L;
    Long REMEDY_PAYMENT_PLAN = 3971L;
    Long REMEDY_IRORA = 3972L;
    Long REMEDY_WAGE_GARNISHMENT = 3973L;
    Long REMEDY_LIEN = 3974L;
    Long REMEDY_WRIT = 3975L;
    Long REMEDY_GENERAL = 3977L;
    Long REMEDY_BANKRUPTCY = 3978L;

    // Payment Category
    Long PAYMENT_CATEGORY_GUIDELINE = 3958L;
    Long PAYMENT_CATEGORY_REDUCED = 3959L;
    Long PAYMENT_CATEGORY_SUSPENDED = 3960L;
    Long PAYMENT_CATEGORY_VARIABLE = 3961L;

    //Case Remedy Stage
    Long CMR_STAGE_INELIGIBLE = 3950L;
    Long CMR_STAGE_NOT_INITIATED = 3951L;
    Long CMR_STAGE_INPROCESS = 3952L;
    Long CMR_STAGE_INEFFECT = 3953L;
    Long CMR_STAGE_SUSPENDED = 3954L;

    //Case Remedy Status
    Long CMR_STATUS_COUNTY_SELECTED = 3939L;
    Long CMR_STATUS_LIEN_FILED = 3981L;
    Long CMR_STATUS_ADMIN_REINSTATE = 3996L;
    Long CMR_STATUS_ADMIN_SUSPENDED = 3997L;
    Long CMR_STATUS_COURT_REINSTATE = 3999L;
    Long CMR_STATUS_COURT_SUSPENDED = 4001L;
    Long CMR_STATUS_COURT_ORDERED_WG = 4002L;
    Long CMR_STATUS_DO_NOT_GARNISH = 4004L;
    Long CMR_STATUS_EMP_FOUND = 4008L;
    Long CMR_STATUS_FA_DECISION = 4009L;
    Long CMR_STATUS_FA_RECIEVED = 4011L;
    Long CMR_STATUS_IN_EFFECT = 4014L;
    Long CMR_STATUS_NO_COUNTY = 4019L;
    Long CMR_STATUS_NO_EMP = 4020L;
    Long CMR_STATUS_NO_PMT = 4022L;
    Long CMR_STATUS_USER_ALERT_LIEN = 4033L;
    Long CMR_STATUS_WG_CHANGED = 4037L;
    Long CMR_STATUS_UNKNOWN = 4068L;

    //Case Remedy Next Step
    Long CMR_NEXT_STEP_SECOND_PP_LTR = 4042L;
    Long CMR_NEXT_STEP_MANUAL_LETTER = 4054L;
    Long CMR_NEXT_STEP_NONE = 4056L;
    //Long CMR_NEXT_STEP_PP_LTR = 4057L;
    Long CMR_NEXT_STEP_REINSTATE_LTR = 4058L;
    Long CMR_NEXT_STEP_RESEARCH_EMP = 4059L;
    Long CMR_NEXT_STEP_REV_WG_NOTICE = 4061L;
    Long CMR_NEXT_STEP_REVIEW_FA = 4062L;
    Long CMR_NEXT_STEP_SECOND_DEMAND_LETTER = 4064L;
    Long CMR_NEXT_STEP_SUSPENSION_LTR = 4066L;
    Long CMR_NEXT_STEP_WG_NOTICE = 4067L;

    //Employer Non Compliance
    Long EMP_NON_COMP_FAIL_IMP_GARNISH = 3955L;
    Long EMP_NON_COMP_FAIL_NOTIFY_TERMINATION = 3957L;
    Long EMP_NON_COMP_DISP_ACTION_ON_CMT = 3957L;

    //Activity Update Contact Constants

    String ENTITY_EMP_SHORT_FORM = "EMP";

    String ENTITY_COURT_SHORT_FORM = "COURT";

    String ENTITY_ATTY_SHORT_FORM = "ATTY";

    String ENTITY_REP_IND_SHORT_FORM = "REP(I)";

    String ENTITY_REP_ORG_SHORT_FORM = "REP(O)";

    String ENTITY_NEW_ATTY_SHORT_FORM = "New ATTY";

    String ENTITY_NEW_REP_IND_SHORT_FORM = "New REP(I)";

    String ENTITY_NEW_REP_ORG_SHORT_FORM = "New REP(O)";

    String CME_TYPE_ORG = "O";

    String CME_TYPE_IND = "I";

    String CLAIMANT_REPRESENTS_FOR = "C";

    String EMPLOYER_REPRESENTS_FOR = "E";


    String NO_KNOWN_NH_EMPLOYER = "No Known NH Employer";

    String OUT_OF_STATE_EMPLOYER = "Out of state employer";

    //Entity Type
    Long CASE_ENTITY_CONTACT_TYPE_EMP = 3941L;
    Long CASE_ENTITY_CONTACT_TYPE_COURT = 3942L;
    Long CASE_ENTITY_CONTACT_TYPE_ATTY = 3943L;
    Long CASE_ENTITY_CONTACT_TYPE_REP_CMI = 3944L;
    Long CASE_ENTITY_CONTACT_TYPE_REP_CMO = 3945L;

    //Case Priority Status
    Long CASE_PRIORITY_IMMDT = 3929L;
    Long CASE_PRIORITY_LO = 3928L;
    Long CASE_PRIORITY_HI = 3926L;
    Long CASE_PRIORITY_MD = 3927L;

    //CRC

    Long TEMP_REDUCTION_LIEN = 20L;

    Long TEMP_SUSPENSION_LIEN = 21L;

    Long NOTICE_OF_CHANGED_WG = 3L;

    Long NOTICE_OF_GARNISHMENT = 40L;

    Long NOTICE_OF_SUSPENDED_WG = 5L;

    Long NOTICE_OF_COURT_ORDERED_WG = 6L;

    //Correspondence
    Integer COR_STATUS_NOT_PROCESSED = 1424;
    Long COR_STATUS_PROCESSED_CD = 1425L;
    Integer COR_RECEIPT_CLAIMANT = 1773;
    Integer COR_RECEIPT_EMPLOYER = 1774;
    Integer COR_RECEIPT_INTERESTED_PARTY = 1723;
    Integer COR_RECEIPT_STAFF_CD = 3552;

    // Entity Contact Type
    Long ENTITY_CONTACT_TYPE_CMT = 3940L;
    Long ENTITY_CONTACT_TYPE_EMP = 3941L;
    Long ENTITY_CONTACT_TYPE_COURT = 3942L;
    Long ENTITY_CONTACT_TYPE_ATTY = 3943L;
    Long ENTITY_CONTACT_TYPE_REP_I = 3944L;
    Long ENTITY_CONTACT_TYPE_REP_O = 3945L;

    Long COR_SOURCE_IFK_CD_FOR_CMC = 4093L;

    //County
    Long COUNTY_NONE = 65L;

    // Remedy Long Description

    String NOT_REMEDY = "Not a Remedy";

    String USER_INTERFACE = "U";

    //Nhuis Notes Constants

    String NHUIS_NOTES_SUBJECT = "Collecticase Notes";

    String SYSTEM_USER_ID = "6190381";

    String EMP_DIS_ASSOCIATE_SHORT_NOTE = "Employer disassociated";

    // Create Case Stored Procedure Constants
    String PIN_CMT = "pin_cmt";

    String PIN_STAFF_ID = "pin_staff_id";

    String PIN_PRIORITY = "pin_priority";

    String PIN_REMEDY_CD = "pin_remedy_cd";

    String PIN_ACTIVITY_CD = "pin_activity_cd";

    // Create Base Activity

    String PIN_CMC_ID = "pin_cmc_id";
    String PIN_ACTIVITY_TYPE_CD = "pin_activity_type_cd";
    String PIN_REMEDY_TYPE_CD = "pin_remedy_type";
    String PIN_ACTIVITY_DT = "pin_activity_dt";
    String PIN_ACTIVITY_TIME = "pin_activity_time";
    String PIN_ACTIVITY_SPECIFICS = "pin_activity_specifics";
    String PIN_ACTIVITY_NOTES = "pin_activity_notes";
    String PIN_ACTIVITY_NOTES_ADDL = "pin_activity_notes_addl";
    String PIN_ACTIVITY_NOTES_NHUIS = "pin_activity_notes_nhuis";
    String PIN_ACTIVITY_COMM_METHOD = "pin_comm_method";
    String PIN_ACTIVITY_CASE_CHARACTERISTICS = "pin_case_characteristics";
    String PIN_ACTIVITY_CMT_REP_CD = "pin_cmt_rep_type_cd";
    String PIN_ACTIVITY_CASE_PRIORITY = "pin_case_priority";
    String PIN_FOLLOWUP_DT = "pin_follow_up_dt";
    String PIN_FOLLOWUP_SH_NOTE = "pin_follow_sh_note";
    String PIN_FOLLOWUP_COMP_SH_NOTE = "pin_fup_comp_sh_note";
    String PIN_CMA_ID = "pin_cma_id";

    // Case Lookup Stored Proc Constants

    String PIN_CASE_NUM = "pin_case_num";

    String PIN_SSN = "pin_ssn";

    String PIN_LAST_NAME = "pin_last_name";

    String PIN_FIRST_NAME = "pin_first_name";

    String PIN_OP_TYPE = "pin_op_type";

    String PIN_OP_BAL_RANGE_FROM = "pin_op_bal_range_from";

    String PIN_OP_BAL_RANGE_TO = "pin_op_bal_range_to";

    String PIN_CASE_PRIORITY = "pin_case_priority";

    String PIN_NEXT_FOLLOW_UP = "pin_next_follow_up";

    String PIN_BKT_STATUS = "pin_bkt_status";

    String PIN_ASSIGNED_TO = "pin_assigned_to";

    String PIN_TELE_NUM = "pin_tele_num";

    String PIN_CASE_OPEN = "pin_case_open";

    String PIN_REMEDY = "pin_remedy";

    String PIN_REMEDY_ST_FROM_DT = "pin_remedy_st_from_dt";

    String PIN_REMEDY_ST_TO_DT = "pin_remedy_st_to_dt";

    String PIN_CASE_OPEN_FROM_DT = "pin_case_open_from_dt";

    String PIN_CASE_OPEN_TO_DT = "pin_case_open_to_dt";

    String PIN_RPM_FROM_DT = "pin_rpm_from_dt";

    String PIN_RPM_TO_DT = "pin_rpm_to_dt";

    // Correspondence Constants for Stored Procedure

    String PIN_CRC_ID = "pin_crc_id";

    String PIN_WLP_I720_RPT_ID = "wlp_i720_rpt_id";

    String PIN_WLP_I720_CMT_ID = "wlp_i720_cmt_id";

    String PIN_WLP_I720_CLM_ID = "wlp_i720_clm_id";

    String PIN_WLP_I720_EMP_ID = "wlp_i720_emp_id";

    String PIN_WLP_I720_COR_COE_IND = "wlp_i720_cor_coe_ind";

    String PIN_WLP_I720_FORCED_IND = "wlp_i720_forced_ind";

    String PIN_WLP_I720_COR_STATUS_CD = "wlp_i720_cor_status_cd";

    String PIN_WLP_I720_COR_DEC_ID_IFK = "wlp_i720_cor_dec_id_ifk";

    String PIN_WLP_I720_COR_RECEIP_IFK = "wlp_i720_cor_receip_ifk";

    String PIN_WLP_I720_COR_RECEIP_CD = "wlp_i720_cor_receip_cd";

    String PIN_WLP_I720_COR_TS = "wlp_i720_cor_ts";

    String PIN_WLP_I720_COE_STRING = "wlp_i720_coe_string";

    // Common Constants for Stored Procedure
    String PIN_USER = "pin_user";

    String PIN_USING = "pin_using";

    String PIN_STF_ID = "pin_stf_id";

    String PIN_EMP_ID = "pin_emp_id";

    // SQL Output Parameters
    String POUT_SUCCESS = "pout_success";

    String POUT_SQL_STRING = "pout_sql_string";

    String POUT_CMC_ID = "pout_cmc_id";

    String POUT_CMA_ID = "pout_cma_id";

    String POUT_NOT_STARTED = "pout_not_started";
    String POUT_OVERDUE = "pout_overdue";
    String POUT_HIGH_PRIORITY = "pout_high_priority";
    String POUT_BANKRUPTCY_CASES = "pout_bankruptcy_cases";

    String POUT_WLP_O720_COR_ID = "wlp_o720_cor_id";
    String POUT_WLP_O720_RETURN_CD = "wlp_o720_return_cd";
    String POUT_WLP_O720_RETURN_MSG = "wlp_o720_return_msg";

    String SP_AJI720 = "AJI720";

    String PAR_COLC_NO_OF_LKUP_REC = "COLC_NO_OF_LKUP_REC";

    String COLC_NO_OF_LKUP_REC = "COLC_NO_OF_LKUP_REC";

    String STATE_OF_NEW_HAMPSHIRE = "NH";
    String YES = "Y";
    String NO = "N";

    Long MERRIMACK_COUNTY_DISTRICT_COURT = 23L;
    Long CATEGORY_CCASE_EMPLOYER_REP = 635L;
    Long COLLECTION_LOF_ID = 32L;

    Long COLLECTION_SUPERVISOR = 87L;
    Long COLLECTION_SPECIALIST = 47L;
    Long BANKRUPTCY_SPECIALIST = 88L;

    Long USR_STATUS_CD = 1724L;
    String COLON = ":";

    String FRAUD_STATUS_FRAUD = "Fraud Only";
    String FRAUD_STATUS_NF_EARNING = "Non-Fraud Earnings Only";
    String FRAUD_STATUS_FRAUD_NF_EARNING = "Fraud and Non-Fraud Earnings Only";
    String FRAUD_STATUS_NF = "Non-Fraud Only";
    String BANKRUPTCY = "Bankruptcy";

    // Case Load Fraud Status Value
    String FRAUD_STATUS_FRAUD_VALUE = "FRD";
    String FRAUD_STATUS_NF_EARNING_VALUE = "NFE";
    String FRAUD_STATUS_FRAUD_NF_EARNING_VALUE = "F-NFE";
    String FRAUD_STATUS_NF_VALUE = "NF";
    String BANKRUPTCY_VALUE = "B";

    String NEXT_FOLLOWUP_OVERDUE = "Overdue";
    String NEXT_FOLLOWUP_DUE_WITHIN_WEEK = "Due within a week";
    String NEXT_FOLLOWUP_DUE_WITHIN_MONTH = "Due within a month";
    String NEXT_FOLLOWUP_DUE_TODAY = "Due Today";
    String NEXT_FOLLOWUP_OVERDUE_ONE_WEEK = "1 week Overdue";
    String NEXT_FOLLOWUP_OVERDUE_ONE_MONTH = "1 month Overdue";

    String NEXT_FOLLOWUP_OVERDUE_VALUE = "OD";
    String NEXT_FOLLOWUP_DUE_WITHIN_WEEK_VALUE = "DW";
    String NEXT_FOLLOWUP_DUE_WITHIN_MONTH_VALUE = "DM";
    String NEXT_FOLLOWUP_DUE_TODAY_VALUE = "DT";
    String NEXT_FOLLOWUP_OVERDUE_ONE_WEEK_VALUE = "ODW";
    String NEXT_FOLLOWUP_OVERDUE_ONE_MONTH_VALUE = "ODM";

    String ZERO_TO_HUNDERED_DOLLAR = "$0 - $100";

    String HUNDERED_TO_TWOHUNDERED_DOLLAR = "$101 - $200";

    String TWOHUNDERED_TO_THREEHUNDERED_DOLLAR = "$201 - $300";

    String THREEHUNDERED_TO_FOURHUNDERED_DOLLAR = "$301 - $400";

    String FOURHUNDERED_TO_FIVEHUNDERED_DOLLAR = "$401 - $500";

    String FIVEHUNDERED_TO_SIXHUNDERED_DOLLAR = "$501 - $600";

    String SIXHUNDERED_TO_SEVENHUNDERED_DOLLAR = "$601 - $700";

    String SEVENHUNDERED_TO_EIGHTHUNDERED_DOLLAR = "$701 - $800";

    String EIGHTHUNDERED_TO_NINEHUNDERED_DOLLAR = "$801 - $900";

    String NINEHUNDERED_TO_ONETHOUSAND_DOLLAR = "$901 - $1000";

    String THOUSAND_TO_TWOTHOUSAND_DOLLAR = "$1,001 - $2,000";

    String TWOTHOUSAND_TO_THREETHOUSAND_DOLLAR = "$2,001 - $3,000";

    String THIRDTHOUSAND_TO_FOURTHOUSAND_DOLLAR = "$3,001 - $4,000";

    String FOURTHOUSAND_TO_FIVETHOUSAND_DOLLAR = "$4,001 - $5,000";

    String FIVETHOUSAND_TO_SIXTHOUSAND_DOLLAR = "$5,001 - $6,000";

    String SIXTHOUSAND_TO_SEVENTHOUSAND_DOLLAR = "$6,001 - $7,000";

    String SEVENTHOUSAND_TO_EIGHTTHOUSAND_DOLLAR = "$7,001 - $8,000";

    String EIGHTTHOUSAND_TO_NINETHOUSAND_DOLLAR = "$8,001 - $9,000";

    String NINETHOUSAND_TO_TENTHOUSAND_DOLLAR = "$9,001 - $10,000";

    String ABOVE_TEN_THOUSAND_DOLLAR = "> $10,000";

    String ZERO_TO_HUNDERED = "0-100";

    String HUNDERED_TO_TWOHUNDERED = "101-200";

    String TWOHUNDERED_TO_THREEHUNDERED = "201-300";

    String THREEHUNDERED_TO_FOURHUNDERED = "301-400";

    String FOURHUNDERED_TO_FIVEHUNDERED = "401-500";

    String FIVEHUNDERED_TO_SIXHUNDERED = "501-600";

    String SIXHUNDERED_TO_SEVENHUNDERED = "601-700";

    String SEVENHUNDERED_TO_EIGHTHUNDERED = "701-800";

    String EIGHTHUNDERED_TO_NINEHUNDERED = "801-900";

    String NINEHUNDERED_TO_ONETHOUSAND = "901-1000";

    String THOUSAND_TO_TWOTHOUSAND = "1001-2000";

    String TWOTHOUSAND_TO_THREETHOUSAND = "2001-3000";

    String THIRDTHOUSAND_TO_FOURTHOUSAND = "3001-4000";

    String FOURTHOUSAND_TO_FIVETHOUSAND = "4001-5000";

    String FIVETHOUSAND_TO_SIXTHOUSAND = "5001-6000";

    String SIXTHOUSAND_TO_SEVENTHOUSAND = "6001-7000";

    String SEVENTHOUSAND_TO_EIGHTTHOUSAND = "7001-8000";

    String EIGHTTHOUSAND_TO_NINETHOUSAND = "8001-9000";

    String NINETHOUSAND_TO_TENTHOUSAND = "9001-10000";

    String ABOVE_TEN_THOUSAND = "10001-99999";

    String DISASSOCIATE_ORGANIZATIONAL_CONTACT_ACTIVITY = "Disassociate Organizational Contact from Case";

    String ACTIVITY_TEMPLATE_NAME = "templateName";

    String CASE_PRIORITY_IMMEDIATE = "IM";

    String SPACE = " ";

    String BLANK_SPACE = "";

    SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm a");

    String SQUARE_BRACKET_START = "[";

    String SQUARE_BRACKET_END = "]";

    String BREAK = "<br />";

    String RETURN_NEW_LINE = "\r\n";

    String ACTIVITY_FOLLOWUP_NOTES1 = "Follow-up has been Completed for the activity ";

    String ACTIVITY_FOLLOWUP_NOTES2 = " that was logged on ";

    String ACTIVITY_FOLLOWUP_NOTES3 = " by ";

    String ACTIVITY_FOLLOWUP_NOTES4 = " on ";

    String ACTIVITY_SHORT_NOTE = "Short Note: ";

    String INSERT_GTT_NHUIS_EMP_DATA = "insert into GTT_FOR_ORGLOOKUP select distinct empl.emp_Id, UPPER(empl.emp_Name), UPPER(empl.EMP_DBA_NAME), empl.EMP_UI_ACCT_NBR ,'EMP' AS ORIGIN,'NHUIS' AS SOURCE,TO_CHAR(empl.EMP_FEIN_NBR ),'EMP' as ENTITY_TYPE, CASE WHEN empl.EMP_KILLED_DT is null THEN 'A' ELSE 'T' END AS STATUS  from EMPLOYER_EMP empl,EMP_ADDRESS_EAD ead left outer join CCASE_ORGANIZATION_CMO cmo on ead.FK_EMP_ID != cmo.FK_EMP_ID where empl.emp_id = ead.fk_emp_id and (empl.EMP_DELETE_IND  is null or empl.EMP_DELETE_IND  <> 'Y') and empl.EMP_UI_ACCT_NBR is not null and empl.EMP_ID not in (SELECT cme.fk_emp_id FROM CCASE_ENTITY_CME  cme WHERE cme.fk_emp_id IS NOT NULL AND cme.fk_cmc_id = :cmcId and cme.cme_Active_Ind = :activeInd) and ead.EAD_TYPE_CD  in (:eadTypeList)";
    String INSERT_GTT_CMO_DATA = "insert into GTT_FOR_ORGLOOKUP select distinct empl.emp_Id, UPPER(empl.emp_Name), UPPER(empl.EMP_DBA_NAME),empl.EMP_UI_ACCT_NBR ,'CMO' AS ORIGIN,'NHUIS' AS SOURCE,TO_CHAR(empl.EMP_FEIN_NBR ),'EMP' as ENTITY_TYPE, CASE WHEN empl.EMP_KILLED_DT is null THEN 'A' ELSE 'T' END AS STATUS from EMPLOYER_EMP empl,EMP_ADDRESS_EAD ead left outer join CCASE_ORGANIZATION_CMO cmo on ead.FK_EMP_ID != cmo.FK_EMP_ID where empl.emp_id = ead.fk_emp_id and (empl.EMP_DELETE_IND  is null or empl.EMP_DELETE_IND  <> 'Y') and empl.EMP_UI_ACCT_NBR  is not null and empl.EMP_ID not in (SELECT cme.fk_emp_id FROM CCASE_ENTITY_CME  cme WHERE cme.fk_emp_id IS NOT NULL AND cme.fk_cmc_id = :cmcId) and ead.EAD_TYPE_CD in (:eadTypeList) UNION SELECT DISTINCT CMO_ID,CMO_NAME,'' AS DBA_NAME,CMO_UI_ACCT_NBR,'CMO' AS ORIGIN, 'COLLECTICASE:'|| CASE WHEN CMO.FK_EMP_ID is not null THEN 'ATTY/REP, EMP' ELSE 'ATTY/REP' END AS SOURCE,CMO_FEIN_NBR,FN_GET_SEC_ALV_DESC(CME_ROLE) AS ENTITY_TYPE,'' AS STATUS FROM CCASE_ENTITY_CME CME JOIN CCASE_ORGANIZATION_CMO CMO ON CMO.CMO_ID = CME.FK_CMO_ID and CME.CME_ROLE in(:cmeRoleList) and CMO.CMO_ID not in (select DISTINCT FK_CMO_ID from CCASE_ENTITY_CME where FK_CMC_ID = :cmcId and FK_CMO_ID is not null)";


    //Employer Corporate Address
    Long EMP_ADDRESS_CORPORATE = 492L;

    //Employer Mailing Address
    Long EMP_ADDRESS_MAILING = 493L;

    //Organization Lookup Parameters
    String ENTITY_NAME = "entityName";

    String ENTITY_UI_ACC_NBR = "entityUIAccNbr";

    String ENTITY_FEIN_NBR = "entityFEINNbr";

    String ENTITY_TYPE = "entityType";

    String ENTITY_SOURCE = "entitySource";

    String ENTITY_DBA_NAME = "entityDBAName";

    String LIKE_OPERATOR = "%";
    String EMPLOYER_ENTITY_TYPE = "Employer";
    String ATTORNEY_ENTITY_TYPE = "Attorney";

    String REPRESENTATIVE_ENTITY_TYPE = "Representative";

    Long ACTIVITY_TYPE_USER_ALERT_DISCHARGE_LIEN = 3921L;
    Long ACTIVITY_TYPE_COMPLETE_DOCKET_MARKING = 3890L;
    String PAGE_ASC_ENABLE_NAME = "pageAscendingEnable";
    String PAGE_SIZE_NAME = "pageSize";
    String PAGE_NO_NAME = "pageNo";
}
