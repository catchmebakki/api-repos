package com.ssi.ms.collecticase.constant;

import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ErrorMessageConstant {

    String CASE_ID_NOT_FOUND = "caseId.notFound";

    String ACTIVITY_ID_NOT_FOUND = "activityId.notFound";

    String ALV_ID_NOT_FOUND = "activityId.notFound";

    String COR_ID_NOT_FOUND = "correspondenceId.notFound";

    String CMO_ID_NOT_FOUND = "cmoId.notFound";

    String CRA_ID_NOT_FOUND = "craId.notFound";

    String CRC_ID_NOT_FOUND = "crcId.notFound";

    String CME_ID_NOT_FOUND = "entityId.notFound";

    String CMI_ID_NOT_FOUND = "individualId.notFound";

    String CMN_ID_NOT_FOUND = "activity.notices.notFound";

    String EMP_ID_NOT_FOUND = "employerdid.notices.notFound";

    String STAFF_ID_MANDATORY = "staffId.mandatory";

    String CASE_LOOKUP_CLAIMANT_SSN_INVALID = "caselookup.claimantSSN.invalid";

    String CASE_LOOKUP_CASE_OPEN_FROM_DATE_FUTURE = "caselookup.caseOpenFromDate.future";

    String CASE_LOOKUP_CASE_OPEN_TO_DATE_FUTURE = "caselookup.caseOpenToDate.future";

    String CASE_LOOKUP_REMEDY_FROM_DATE_FUTURE = "caselookup.caseRemedyFromDate.future";

    String CASE_LOOKUP_REMEDY_TO_DATE_FUTURE = "caselookup.caseRemedyToDate.future";

    String CASE_LOOKUP_REPAYMENT_FROM_DATE_FUTURE = "caselookup.repaymentFromDate.future";

    String CASE_LOOKUP_REPAYMENT_TO_DATE_FUTURE = "caselookup.repaymentToDate`.future";

    String CASE_REMEDY_ACTIVITY_ID_NOT_FOUND = "remedy.activityId.notFound";

    String ACTIVITY_DATE_MANDATORY = "activity.date.required";

    String ACTIVITY_DATE_NOT_FUTURE = "activity.date.notFuture";

    String CASE_CHARACTERISTIC_MANDATORY = "case.characteristic.required";

    String CASE_CHARACTERISTIC_INVALID = "case.characteristic.invalid";

    String ACTIVITY_TIME_MANDATORY = "activity.time.required";

    String ACTIVITY_METHOD_MANDATORY = "activity.method.required";

    String ACTIVITY_SPECIFICS_MANDATORY = "activity.specifics.required";

    String ACTIVITY_SPECIFICS_INVALID = "activity.specifics.invalid";

    String ACTIVITY_FOLLOW_UP_DATE_FUTURE = "activity.followup.date.future";

    String CLAIMANT_REP_MANDATORY = "claimant.rep.required";

    String NOTES_LENGTH_EXCEED = "notes.length.exceed";

    String NHUIS_NOTES_LENGTH_EXCEED = "nhuis.notes.length.exceed";

    String ACTIVITY_NOTES_MANDATORY = "activity.notes.required";

    String COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY = "complete.followup.completed.on.mandatory";

    String COMPELTE_FOLLOWUP_COMPLETED_BY_MANDATORY = "complete.followup.completed.by.mandatory";

    String ORG_LOOKUP_ORG_NAME_INVALID = "orgLookup.orgName.invalid";

    String ORG_LOOKUP_UI_ACCT_NBR_INVALID = "orgLookup.uiAcctNbr.invalid";

    String ORG_LOOKUP_FEIN_INVALID = "orgLookup.fein.invalid";

    @Getter
    @AllArgsConstructor
    enum CommonErrorDetail implements CollecticaseErrorEnum {
        CREATE_ACTIVITY_FAILED("createActivity", "createActivity.failed",
                "Request Processed. However, case activity could not created. Please Contact WeCare.", 0);
        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum GeneralActivityDTODetail implements CollecticaseErrorEnum {
        FOLLOWUP_DT_REQUIRED("activityFollowupDt", "followupDt.required",
                "Activity Follow-up Date is required when the activity Type is Record new Follow-up.", 0),

        FOLLOWUP_SHORT_NOTE_NA("activityFollowupShortNote",
                "activityFollowupShortNote.notApplicable",
                "Activity Follow-up Short Note is not applicable when" +
                        " activity Follow-up date is not provided.", 0),

        FOLLOWUP_SHORT_NOTE_REQUIRED("activityFollowupShortNote",
                "activityFollowupShortNote.required",
                "Activity Follow-up Short Note is required when " +
                        "activity Follow-up date is provided.", 0),

        PROPERTY_LIEN_REQUIRED("propertyLien", "propertyLien.required",
                "Property Lien is mandatory when the activity is Research for NH Property.", 0),

        ENTITY_CONTACT_REQUIRED("activityEntityContact", "activityEntityContact.required",
                "Please select the entity contact.", 0),

        CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN("activityCorrespondence",
                "activityCorrespondence.notApplicable",
                "'{0}' Notices can be sent when the Property Lien is None.", 1),

        REOPEN_CASE_CLOSED("activityTypeCd", "activityTypeCd.reopen.case.closed",
                "Case cannot be reopened when the case status is open.", 0),

        REOPEN_OPM_BAL_ZERO("activityTypeCd", "activityTypeCd.reopen.opm.bal.zero",
                "Case cannot be reopened when the overpayment is zero.", 0);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum PaymentPlanActivityDTODetail implements CollecticaseErrorEnum {
        GUIDE_LINE_AMOUNT_REQUIRED("paymentPlanGuideLineAmount",
                "paymentPlanGuideLineAmount.required",
                "Please provide the Guideline Amount.", 0),

        GUIDE_LINE_AMOUNT_ZERO("paymentPlanGuideLineAmount",
                "paymentPlanGuideLineAmount.zero",
                "lease provide a valid positive number for Guideline Amount.", 0),

        GUIDE_LINE_AMOUNT_INVALID("paymentPlanGuideLineAmount",
                "paymentPlanGuideLineAmount.invalid",
                "Please provide a valid Guideline Amount.", 0),

        RESPONSE_TO_REQUIRED("paymentPlanReponseToCd",
                "paymentPlanResponseToCd.required",
                "Please provide Response To.", 0),

        RESPONSE_TO_OTHER_REQUIRED("paymentPlanReponseToOther",
                "paymentPlanReponseToOther.required",
                "Please provide Other.", 0),

        SIGNED_DATE_REQUIRED("paymentPlanSignedDate", "paymentPlanSignedDate.required",
                "Please provide the Payment Plan Signed On Date.", 0),

        SIGNED_DATE_FUTURE("paymentPlanSignedDate", "paymentPlanSignedDate.future",
                "Payment Plan Signed On Date cannot be in future.", 0),

        FA_SIGNED_DATE_REQUIRED("paymentPlanFinAffidavitSignedDate",
                "paymentPlanFinAffidavitSignedDate.required",
                "Please provide the Fin Affidavit Signed On Date.", 0),

        FA_SIGNED_DATE_FUTURE("paymentPlanFinAffidavitSignedDate",
                "paymentPlanFinAffidavitSignedDate.future",
                "Fin Affidavit Signed On Date cannot be in future.", 0),

        PAYMENT_AMOUNT_REQUIRED("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.required",
                "Please provide the Payment Amount.", 0),

        PAYMENT_AMOUNT_ZERO("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.zero",
                "Payment Plan Payment Amount cannot be zero.", 0),

        PAYMENT_AMOUNT_INVALID("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.invalid",
                "Please provide a valid Payment Amount.", 0),

        GUIDE_LINE_PAYMENT_AMOUNT_NOT_SAME("paymentPlanPaymentAmount",
                "guideLineAmount.paymentAmount.not.same",
                "Payment Amount and Guideline Amount should be the same" +
                        " when Payment Category is Guideline Payment.", 0),

        PAYMENT_AMOUNT_SHOULD_BE_ZERO("paymentPlanPaymentAmount",
                "PaymentAmount.should.be.zero",
                "Payment Plan Payment Amount should be zero.", 0),

        PAYMENT_AMOUNT_LESS_THAN_GUIDELINE_AMOUNT("paymentPlanPaymentAmount",
                "paymentAmount.less.than.guideline.amount",
                "Payment amount cannot be greater than or equal to Guideline" +
                        " Amount when the Payment category is Reduced or Variable.", 0),

        PAYMENT_PLAN_PAYMENT_CATEGORY_REQUIRED("paymentPlanPaymentCategory",
                "paymentPlanPaymentCategory.required",
                "Please provide the Payment Category when Payment Amount is entered.", 0),

        PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_APPLICABLE("paymentPlanPaymentAmount",
                "paymentPlanPaymentCategory.invalid",
                "Payment Category is not applicable when Payment Amount is not entered.",
                0),

        PAYMENT_PLAN_PAYMENT_CATEGORY_NOT_APPLICABLE("paymentPlanPaymentCategory",
                "paymentPlanPaymentCategory.notapplicable",
                "Payment Plan Payment category is not applicable when activity type is Guideline" +
                        " based Payment Plan.", 0),

        PAYMENT_PLAN_EFF_UNITL_REQUIRED("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.required",
                "Please provide the Effective Until when the Payment Category is not Guideline Payment.", 0),

        PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.notapplicable",
                "Effective Until cannot be entered when the Payment Category is Guideline Payment.", 0),

        PAYMENT_PLAN_EFF_UNITL_MORE_THAN_6_MONTHS("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.morethan.6months",
                "Effective Until cannot be more than 6 Months.", 0),

        PAYMENT_PLAN_NO_ACTIVE_PAYMENT_PLAN("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.noactiveplan",
                "Activity cannot be added when the Claimant does not have an Active Payment Plan in NHUIS.", 0),

        PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_SAME("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.paymentplan.amount",
                "Activity cannot be added when the NHUIS Payment Plan Monthly amount " +
                        "and Activity Payment amount is not same and NHUIS Payment Plan monthly amount is '{0}'", 1),

        PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NA("activityCorrespondence",
                "activityCorrespondence.pmtcategory.notapplicable",
                "'{0}' Notices cannot be sent when the Payment Category is '{1}'", 2),

        PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NONE("activityCorrespondence",
                "activityCorrespondence.pmtcategory.none",
                "'{0}' Notices cannot be sent when the Payment Category is none", 1),

        PAYMENT_PLAN_COR_CURRENT_FILING_NONE("activityCorrespondence",
                "activityCorrespondence.current.filing.none",
                "'{0}' Notices cannot be sent when the Claimant is not currently Filing", 1),

        PAYMENT_AMOUNT_ZERO_WHEN_PAYMENT_CATEGORY_SUSPENDED("paymentPlanPaymentAmount",
                "paymentAmount.should.be.zero.when.payment.category.suspended",
                "Payment amount should be $0 when the Payment Category is Suspended.", 0),

        PAYMENT_AMOUNT_NON_ZERO_PAYMENT_CATEGORY_REDUCED("paymentPlanPaymentAmount",
                "paymentAmount.zero.when.payment.category.reduced",
                "Please provide a valid positive number for Payment Amount.", 0),

        PAYMENT_PLAN_EFF_UNITL_SHOULD_BE_IN_FUTURE("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.should.be.in.future",
                "Payment Plan Effective Until must be in the future.", 0),

        PAYMENT_PLAN_COR_CURRENT_FILING_NA("activityCorrespondence",
                "activityCorrespondence.current.filing.notapplicable",
                "'{0}' Notices cannot be sent when the Claimant is currently Filing", 1);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum WageGarnishmentActivityDTODetail implements CollecticaseErrorEnum {
        WG_EMPLOYER_ID_REQUIRED("employerId", "employerId.required",
                "Please select the Employer.", 0),

        WG_EMPLOYER_NA_FOR_SUSPEND("employerId", "employerId.suspend.notapplicable",
                "No employer must be selected when the request pertains to Suspension of " +
                        "Wage Garnishment prior to the Notice of Wage Garnishment being issued.", 0),

        WG_EMPLOYER_NA_FOR_UNKNOWN("employerId", "employerId.unknown.notapplicable",
                "No Known NH employer is not applicable when claimant has a current Wage Garnishment.", 0),

        WG_EMPLOYER_UNKNOWN_NA_FOR_WAGE_EMP("employerId", "employerId.unknown.wage.emp",
                "No Known NH employer is only applicable for Research for Employment Activity.", 0),

        WG_EMPLOYER_CONTACT_REQUIRED("employerContactId", "employerContactId.required",
                "Employer Contact is mandatory when the employer is selected.", 0),

        WG_EMPLOYER_REPRESENTATIVE_REQUIRED("employerRepresentativeCd",
                "employerRepresentativeCd.required",
                "Employer Rep is mandatory when the employer is selected.", 0),

        WG_AMOUNT_REQUIRED("wageAmount", "wageAmount.required",
                "Please provide the Wage Amount.", 0),

        WG_FREQUENCY_REQUIRED("wageFrequency", "wageFrequency.required",
                "Please select the Wage Frequency.", 0),

        WG_AMOUNT_VALID_EMPLOYER_INVALID("wageAmount", "wageAmount.valid.employer.invalid",
                "Please select the employer when the wage amount is provided.", 0),

        WG_AMOUNT_POSITIVE_NUMBER("wageAmount", "wageAmount.should.be.positive.number",
                "Please provide a valid positive number for Wage Amount.", 0),

        WG_AMOUNT_INVALID("wageAmount", "wageAmount.should.be.four.digit.two.decimal",
                "Wage Amount is invalid.", 0),

        WG_EMPLOYER_MANDATORY_DO_NOT_GARNISH("doNotGarnishInd",
                "doNotGarnishInd.employer.mandatory",
                "Please select the employer when Do Not Garnish is checked off.", 0),

        WG_FREQUENCY_DO_NOT_GRANISH("doNotGarnishInd",
                "doNotGarnishInd.wage.frequency.exists",
                "Do Not Garnish cannot be checked off when Wage Frequency is selected.", 0),

        WG_FREQUENCY_NO_EMPLOYER("wageFrequency", "employer.notexists.frequency.exists",
                "Employer needs to be selected when Wage Frequency is selected.", 0),

        WG_FREQUENCY_WG_AMOUNT_POSITIVE_NUMBER("wageFrequency",
                "wage.frequency.wage.amount.non.positive.number",
                "Wage Amount should be greater than zero when Wage Frequency is selected.", 0),

        WG_NON_COMPLIANCE_REQUIRED("wageNonCompliance", "wageNonCompliance.required",
                "Please select the Non-compliance.", 0),

        WG_NON_COMPLIANCE_REPAYMENT_EXISTS("wageNonCompliance",
                "wageNonCompliance.repayment.exists",
                "Employer Non-Compliance cannot be Failure to implement garnishment when re-payment" +
                        " happened in the past 40 days via Wage Garnishment.", 0),

        WG_NON_COMPLIANCE_CMT_NO_LONGER("wageNonCompliance",
                "wageNonCompliance.cmt.no.longer",
                "Employer Non-Compliance cannot be Failure to implement garnishment when termination" +
                        " has been recorded via Claimant No longer employed activity.", 0),

        WG_MOTION_FILED_ON_REQUIRED("wageMotionFiledOn", "wageMotionFiledOn.required",
                "Please provide the Motion Filed on.", 0),

        WG_MOTION_FILED_ON_DATE_FUTURE("wageMotionFiledOn", "wageMotionFiledOn.future",
                "Motion Filed on cannot be in the future.", 0),

        WG_MOTION_FILED_ON_EMPLOYER_NONE("wageMotionFiledOn",
                "wageMotionFiledOn.employer.none",
                "Please select the employer when Motion Filed on is provided.", 0),

        WG_CHANGE_REQ_DATE_REQUIRED("wageMotionFiledOn", "wageChangeReqDateOn.required",
                "Please provide the Change Request Date.", 0),

        WG_CHANGE_REQ_DATE_FUTURE("wageMotionFiledOn", "wageChangeReqDate.future",
                "hange Request Date cannot be in the future.", 0),

        WG_CHANGE_REQ_DATE_EMPLOYER_NONE("wageMotionFiledOn",
                "wageChangeReqDate.employer.none",
                "Please select the employer when Change Request Date is provided.", 0),

        WG_EFFECTIVE_DT_REQUIRED("wageEffectiveFrom", "wageEffectiveFrom.required",
                "Please provide the Effective From date.", 0),

        WG_EFFECTIVE_DT_EMPLOYER_NONE("wageEffectiveFrom",
                "wageEffectiveFrom.employer.none",
                "Please select the employer when Effective From is provided.", 0),

        WG_EFFECTIVE_UNILT_DT_EFF_DT_NONE("wageEffectiveUntil",
                "wageEffectiveUntil.wageEffectiveFrom.none",
                "Effective From is mandatory when Effective Until is provided.", 0),

        WG_EFFECTIVE_DT_GREATER_THAN_UNTIL_DT("wageEffectiveUntil",
                "wageEffectiveFrom.greaterthan.wageEffectiveUntil",
                "Effective From cannot be greater than Effective Until date.", 0),

        WG_COURT_ORDERED_NO_WAGE_SUSPENDED("courtOrderedInd",
                "courtOrderedInd.no.wagesuspended",
                "Wage Garnishment has been suspended. You cannot send the Notice of Wage garnishment " +
                        "unless it is court ordered.", 0),

        WG_COURT_ORDERED_REQUIRED("courtOrderedInd", "courtOrderedInd.required",
                "Please indicate whether Court Ordered or not.", 0),

        WG_COURT_ORDERED_DATE_REQUIRED("courtOrderedDate", "courtOrderedDate.required",
                "Please provide the Court Ordered Date.", 0),

        WG_COURT_ORDERED_DATE_FUTURE("courtOrderedDate", "courtOrderedDate.future",
                "Court Ordered date cannot be in future.", 0),

        WG_CORR_NA_DO_NOT_GARNISH("activityCorrespondence",
                "activityCorrespondence.na.dont.garnish",
                "'{0}' Notices cannot be sent when do not garnish is checked", 1),

        WG_CORR_NA_COURT_ORDERED_CHECKED("activityCorrespondence",
                "activityCorrespondence.na.court.ordered.checked",
                "'{0}' Notices can be sent when the Court Ordered is Checked", 1),

        WG_CORR_NA_COURT_ORDERED_NOT_CHECKED("activityCorrespondence",
                "activityCorrespondence.na.court.ordered.notchecked",
                "'{0}' Notices can be sent when the Court Ordered is not Checked", 1);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum UpdateContactActivityDTODetail implements CollecticaseErrorEnum {

        ENTITY_NAME_REQUIRED("entityName", "entityName.required",
                "Entity Name is required.", 0),

        ENTITY_ADDRESS_LINE1_REQUIRED("entityAddressLine1", "entityAddressLine1.required",
                "Entity Address Line is required.", 0),

        ENTITY_CITY_REQUIRED("entityCity", "entityCity.required",
                "Entity City is required.", 0),

        ENTITY_COUNTRY_REQUIRED("entityCountry", "entityCountry.required",
                "Entity Country is required.", 0),

        ENTITY_STATE_REQUIRED("entityState", "entityState.required",
                "Entity State is required.", 0),

        ENTITY_ZIP_REQUIRED("entityZip", "entityZip.required",
                "Entity ZIP is required.", 0),

        ENTITY_TELEPHONE_REQUIRED("entityTelephone", "entityTelephone.required",
                "Entity Telephone is required.", 0),

        ENTITY_PREFERENCE_REQUIRED("entityPreference", "entityPreference.required",
                "Entity Preference is required.", 0),

        ENTITY_FAX_REQUIRED("entityFax", "entityFax.required",
                "Entity Fax is required.", 0),

        ENTITY_UI_ACCT_NBR_INVALID("entityUIAcctNBR", "entityUIAcctNBR.invalid",
                "Entity UI Account NBR is invalid.", 0),

        ENTITY_FEIN_INVALID("entityFEIN", "entityFEIN.invalid",
                "Entity FEIN is invalid.", 0),
        ENTITY_ADDRESS_INVALID("entityAddressLine1", "entityAddressLine1.invalid",
                "Entity Address Line is invalid.", 0),
        ENTITY_CITY_INVALID("entityCity", "entityCity.invalid",
                "Entity City is invalid.", 0),

        ENTITY_ZIP_INVALID("entityZip", "entityZip.invalid",
                "Entity Zip is invalid.", 0),

        ENTITY_PHONE_INVALID("entityTelephone", "entityTelephone.invalid",
                "Entity Phone is invalid.", 0),

        ENTITY_FAX_INVALID("entityFax", "entityFax.invalid",
                "Entity Fax is invalid.", 0),

        ENTITY_REPRESENTED_FOR_REQUIRED("entityRepresentedFor",
                "entityRepresentedFor.required",
                "Entity Represented is required.", 0),

        ENTITY_CONTACT_REQUIRED("entityContactId", "entityContactId.required",
                "Entity Contact Id is required.", 0),

        ENTITY_CONTACT_FIRST_NAME_REQUIRED("entityContactFirstName",
                "entityContactFirstName.required",
                "Entity Contact First Name is required.", 0),

        ENTITY_CONTACT_FIRST_NAME_INVALID("entityContactFirstName",
                "entityContactFirstName.invalid",
                "Entity Contact First Name is invalid.", 0),

        ENTITY_CONTACT_LAST_NAME_REQUIRED("entityContactLastName",
                "entityContactLastName.required",
                "Entity Contact Last Name is required.", 0),

        ENTITY_CONTACT_LAST_NAME_INVALID("entityContactLastName",
                "entityContactLastName.invalid",
                "Entity Contact Last Name is invalid.", 0),

        ENTITY_CONTACT_ADDRESS_REQUIRED("entityAddressLine1",
                "entityAddressLine1.required",
                "Entity Contact Address is required.", 0),

        ENTITY_CONTACT_ADDRESS_INVALID("entityAddressLine1",
                "entityAddressLine1.invalid",
                "Entity Contact Address is invalid.", 0),
        ENTITY_CONTACT_ADDRESS_LINE_2_INVALID("entityAddressLine2",
                "entityAddressLine2.invalid",
                "Entity Contact Address Line 2 is invalid.", 0),

        ENTITY_CONTACT_CITY_REQUIRED("entityContactCity", "entityContactCity.required",
                "Entity Contact City is required.", 0),

        ENTITY_CONTACT_CITY_INVALID("entityContactCity", "entityContactCity.invalid",
                "Entity Contact City is invalid.", 0),

        ENTITY_CONTACT_STATE_REQUIRED("entityContactState", "entityContactState.required",
                "Entity Contact State is required.", 0),

        ENTITY_CONTACT_ZIP_REQUIRED("entityContactZip", "entityContactZip.required",
                "Entity Contact Zip is required.", 0),

        ENTITY_CONTACT_ZIP_INVALID("entityContactZip", "entityContactZip.invalid",
                "Entity Contact Zip is invalid.", 0),

        ENTITY_CONTACT_PREFERENCE_REQUIRED("entityContactPreference",
                "entityContactPreference.required",
                "Entity Contact Preference is required.", 0),

        ENTITY_CONTACT_WORK_PHONE_REQUIRED("entityContactPhoneWork",
                "entityContactPhoneWork.required",
                "Entity Contact Work Phone is required.", 0),

        ENTITY_CONTACT_WORK_PHONE_INVALID("entityContactPhoneWork",
                "entityContactPhoneWork.invalid",
                "Entity Contact Work Phone is invalid.", 0),

        ENTITY_CONTACT_EXT_PROVIDED_WORK_PHONE_REQUIRED("entityContactPhoneWork",
                "workExt.provided.phoneWork.required",
                "Entity Contact Work Phone is required When Work Phone Extension is provided.", 0),

        ENTITY_CONTACT_WORK_PHONE_EXT_INVALID("entityContactPhoneWorkExt",
                "entityContactPhoneWorkExt.invalid",
                "Entity Contact Work Phone Extension is invalid.", 0),

        ENTITY_CONTACT_HOME_PHONE_REQUIRED("entityContactPhoneHome",
                "entityContactPhoneHome.required",
                "Entity Contact Home Phone is required.", 0),

        ENTITY_CONTACT_HOME_PHONE_INVALID("entityContactPhoneHome",
                "entityContactPhoneHome.invalid",
                "Entity Contact Home Phone is invalid.", 0),

        ENTITY_CONTACT_CELL_PHONE_REQUIRED("entityContactPhoneCell",
                "entityContactPhoneCell.required",
                "Entity Contact Cell Phone is required.", 0),

        ENTITY_CONTACT_CELL_PHONE_INVALID("entityContactPhoneCell",
                "entityContactPhoneCell.invalid",
                "Entity Contact Cell Phone is invalid.", 0),

        ENTITY_CONTACT_FAX_REQUIRED("entityContactFax", "entityContactFax.required",
                "Entity Contact Fax is required.", 0),

        ENTITY_CONTACT_FAX_INVALID("entityContactFax", "entityContactFax.invalid",
                "Entity Contact Fax is invalid.", 0),
        ENTITY_CONTACT_EMAILS_INVALID("entityContactEmails", "entityContactEmails.invalid",
                "Entity Contact Emails is invalid.", 0);
        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum CaseLookupErrorDetail implements CollecticaseErrorEnum {
        CASE_LOOKUP_INPUT_INVALID("caseNumber",
                "caselookup.caseNumber.atleastoneshouldbeselected",
                "At least one search option should be selected.", 0),

        CASE_LOOKUP_REMEDY_FROM_TO_DATE("caseRemedyFromDate",
                "caselookup.remedy.from.date.to.date.both.mandatory",
                "Remedy From Date and Remedy To date is Mandatory ", 0),

        CASE_LOOKUP_CASE_OPEN_FROM_TO_DATE("caseOpenFromDate",
                "caselookup.case.from.date.to.date.both.mandatory",
                "Case Open From Date and Case Open To date is Mandatory", 0),

        CASE_LOOKUP_RPM_FROM_TO_DATE("repaymentFromDate",
                "caselookup.rpm.from.date.to.date.both.mandatory",
                "Case Repayment From Date and To date is Mandatory", 0),

        REMEDY_FROM_GREATER_THAN_TO_DATE("caseRemedyFromDate",
                "caselookup.remedy.from.date.greater.than.to.date",
                "Remedy From Date should less than Remedy To date", 0),

        CASE_OPEN_FROM_GREATER_THAN_TO_DATE("caseOpenFromDate",
                "caselookup.caseopen.from.date.greater.than.to.date",
                "Case Opened From Date should less than Case Opened To date", 0),

        RPM_FROM_GREATER_THAN_TO_DATE("repaymentFromDate",
                "caselookup.rpm.from.date.greater.than.to.date",
                "Repayment From Date should less than Repayment To date", 0);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum CompleteFollowupDetail implements CollecticaseErrorEnum {
        COMPLETE_FOLLOWUP_COMPLETED_ON_LESS_THAN_ACTIVITY_DATE("activityCompletedOn",
                "completefollowup.completed.on.greater.than.activity.date",
                "Completed on date should be greater than activity creation date.", 0),

        COMPELTE_FOLLOWUP_COMPLETED_ON_FUTURE("activityCompletedOn",
                "complete.followup.completed.on.future",
                "Follow-up completion date cannot be in the future.",
                0);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

    @Getter
    @AllArgsConstructor
    enum OrgLookupDetail implements CollecticaseErrorEnum {
        ORG_LOOKUP_ATLEAST_ONE_SHOULD_BE_SELECTED("orgName",
                "orgName.atleastoneshouldbeselected",
                "At least one filter condition should be provided.", 0);

        private final String frontendField;
        private final String frontendErrorCode;
        private final String description;
        private final Integer params;
    }

}