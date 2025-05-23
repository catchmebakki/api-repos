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

    String COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY = "complete.followup.completed.on.mandatory";

    String COMPELTE_FOLLOWUP_COMPLETED_BY_MANDATORY = "complete.followup.completed.by.mandatory";

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
                "Activity Followup Date is required.", 0),

        FOLLOWUP_SHORT_NOTE_NA("activityFollowupShortNote",
                "activityFollowupShortNote.notApplicable",
                "Activity Followup Short Note not applicable.", 0),

        FOLLOWUP_SHORT_NOTE_REQUIRED("activityFollowupShortNote",
                "activityFollowupShortNote.required",
                "Activity Followup Short Note is required", 0),

        PROPERTY_LIEN_REQUIRED("propertyLien", "propertyLien.required",
                "Property Lien is required.", 0),

        ENTITY_CONTACT_REQUIRED("activityEntityContact", "activityEntityContact.required",
                "Activity Entity Contact is required.", 0),

        CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN("activityCorrespondence",
                "activityCorrespondence.notApplicable",
                "'{0}' Notices can be sent when the Property Lien is None.", 1),

        REOPEN_CASE_CLOSED("activityTypeCd", "activityTypeCd.reopen.case.closed",
                "Reopen activity cannot be added when case is not closed", 0),

        REOPEN_OPM_BAL_ZERO("activityTypeCd", "activityTypeCd.reopen.opm.bal.zero",
                "Reopen activity cannot be added when opm balance is zero", 0);

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
                "Payment Plan Guide Line Amount is required.", 0),

        GUIDE_LINE_AMOUNT_ZERO("paymentPlanGuideLineAmount",
                "paymentPlanGuideLineAmount.zero",
                "Payment Plan Guide Line Amount cannot be zero.", 0),

        GUIDE_LINE_AMOUNT_INVALID("paymentPlanGuideLineAmount",
                "paymentPlanGuideLineAmount.invalid",
                "Payment Plan Guide Line Amount should be 4 digit with 2 decimal.", 0),

        RESPONSE_TO_REQUIRED("paymentPlanReponseToCd",
                "paymentPlanResponseToCd.required",
                "Payment Plan Response To is mandatory.", 0),

        RESPONSE_TO_OTHER_REQUIRED("paymentPlanReponseToOther",
                "paymentPlanReponseToOther.required",
                "Payment Plan Response To Other is mandatory.", 0),

        SIGNED_DATE_REQUIRED("paymentPlanSignedDate", "paymentPlanSignedDate.required",
                "Payment Plan Signed Date is required.", 0),

        SIGNED_DATE_FUTURE("paymentPlanSignedDate", "paymentPlanSignedDate.future",
                "Payment Plan Signed Date cannot be in future.", 0),

        FA_SIGNED_DATE_REQUIRED("paymentPlanFinAffidavitSignedDate",
                "paymentPlanFinAffidavitSignedDate.required",
                "Payment Plan FA Signed Date is required.", 0),

        FA_SIGNED_DATE_FUTURE("paymentPlanFinAffidavitSignedDate",
                "paymentPlanFinAffidavitSignedDate.future",
                "Payment Plan FA Signed Date cannot be in future.", 0),

        PAYMENT_AMOUNT_REQUIRED("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.required",
                "Payment Plan Payment Amount is required.", 0),

        PAYMENT_AMOUNT_ZERO("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.zero",
                "Payment Plan Payment Amount cannot be zero.", 0),

        PAYMENT_AMOUNT_INVALID("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.invalid",
                "Payment Plan Payment Amount should be 4 digit with 2 decimal.", 0),

        GUIDE_LINE_PAYMENT_AMOUNT_NOT_SAME("paymentPlanPaymentAmount",
                "guideLineAmount.paymentAmount.not.same",
                "Payment Plan Payment and Guideline Amount are not same.", 0),

        PAYMENT_AMOUNT_SHOULD_BE_ZERO("paymentPlanPaymentAmount",
                "PaymentAmount.should.be.zero",
                "Payment Plan Payment Amount should be zero.", 0),

        PAYMENT_AMOUNT_LESS_THAN_GUIDELINE_AMOUNT("paymentPlanPaymentAmount",
                "paymentAmount.less.than.guideline.amount",
                "Payment Plan Payment Amount should be less than guideline amount.", 0),

        PAYMENT_PLAN_PAYMENT_CATEGORY_REQUIRED("paymentPlanPaymentCategory",
                "paymentPlanPaymentCategory.required",
                "Payment Plan Payment category when payment plan amount is entered.", 0),

        PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_APPLICABLE("paymentPlanPaymentAmount",
                "paymentPlanPaymentCategory.invalid",
                "Payment Plan Payment category is not applicable when payment plan amount is not entered.",
                0),

        PAYMENT_PLAN_PAYMENT_CATEGORY_NOT_APPLICABLE("paymentPlanPaymentCategory",
                "paymentPlanPaymentCategory.notapplicable",
                "Payment Plan Payment category is not applicable when activity type is Guideline" +
                        " based Payment Plan.", 0),

        PAYMENT_PLAN_EFF_UNITL_REQUIRED("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.required",
                "Payment Plan Effective Until is required when payment amount is entered.", 0),

        PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.notapplicable",
                "Payment Plan Effective Until is not applicable for payment category guideline.", 0),

        PAYMENT_PLAN_EFF_UNITL_MORE_THAN_6_MONTHS("paymentPlanEffectiveUntilDate",
                "paymentPlanEffectiveUntilDate.morethan.6months",
                "Payment Plan Effective Until is more than 6 months.", 0),

        PAYMENT_PLAN_NO_ACTIVE_PAYMENT_PLAN("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.noactiveplan",
                "Payment Plan with no active payment plan.", 0),

        PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_SAME("paymentPlanPaymentAmount",
                "paymentPlanPaymentAmount.paymentplan.amount",
                "Payment Plan payment amount not same as nhuis payment plan amount .", 0),

        PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NA("activityCorrespondence",
                "activityCorrespondence.pmtcategory.notapplicable",
                "'{0}' Notices cannot be sent when the Payment Category is '{1}'", 2),

        PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NONE("activityCorrespondence",
                "activityCorrespondence.pmtcategory.none",
                "'{0}' Notices cannot be sent when the Payment Category is none", 1),

        PAYMENT_PLAN_COR_CURRENT_FILING_NONE("activityCorrespondence",
                "activityCorrespondence.current.filing.none",
                "'{0}' Notices cannot be sent when the Claimant is not currently Filing", 1),

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
                "Employer is required.", 0),

        WG_EMPLOYER_NA_FOR_SUSPEND("employerId", "employerId.suspend.notapplicable",
                "Employer is not eligible for Suspend Wage Garnishment.", 0),

        WG_EMPLOYER_NA_FOR_UNKNOWN("employerId", "employerId.unknown.notapplicable",
                "Unknown Employer is not eligible for this activity.", 0),

        WG_EMPLOYER_UNKNOWN_NA_FOR_WAGE_EMP("employerId", "employerId.unknown.wage.emp",
                "Unknown Employer is not eligible for this case.", 0),

        WG_EMPLOYER_CONTACT_REQUIRED("employerContactId", "employerContactId.required",
                "Employer contact is required.", 0),

        WG_EMPLOYER_REPRESENTATIVE_REQUIRED("employerRepresentativeCd",
                "employerRepresentativeCd.required",
                "Employer Representative is required.", 0),

        WG_AMOUNT_REQUIRED("wageAmount", "wageAmount.required",
                "Wage Amount is required.", 0),

        WG_FREQUENCY_REQUIRED("wageFrequency", "wageFrequency.required",
                "Wage Frequency is required.", 0),

        WG_AMOUNT_VALID_EMPLOYER_INVALID("wageAmount", "wageAmount.valid.employer.invalid",
                "Wage amount is valid but employer not selected.", 0),

        WG_AMOUNT_POSITIVE_NUMBER("wageAmount", "wageAmount.should.be.positive.number",
                "Wage amount should be positive number.", 0),

        WG_AMOUNT_INVALID("wageAmount", "wageAmount.should.be.four.digit.two.decimal",
                "Wage amount should be Four digit with two decimal.", 0),

        WG_EMPLOYER_MANDATORY_DO_NOT_GARNISH("doNotGarnishInd",
                "doNotGarnishInd.employer.mandatory",
                "Employer mandatory when Do Not garnish is checked.", 0),

        WG_FREQUENCY_DO_NOT_GRANISH("doNotGarnishInd",
                "doNotGarnishInd.wage.frequency.exists",
                "Wage Frequency is not applicable when Do n0t garnish exists", 0),

        WG_FREQUENCY_NO_EMPLOYER("wageFrequency", "employer.notexists.frequency.exists",
                "Wage Frequency is not applicable when employer doesnot exists", 0),

        WG_FREQUENCY_WG_AMOUNT_POSITIVE_NUMBER("wageFrequency",
                "wage.frequency.wage.amount.non.positive.number",
                "Wage Frequency is not applicable when wage amount is not positive number", 0),

        WG_NON_COMPLIANCE_REQUIRED("wageNonCompliance", "wageNonCompliance.required",
                "Wage non compliance required", 0),

        WG_NON_COMPLIANCE_REPAYMENT_EXISTS("wageNonCompliance",
                "wageNonCompliance.repayment.exists",
                "Wage non compliance Failure to implement but repayment exists", 0),

        WG_NON_COMPLIANCE_CMT_NO_LONGER("wageNonCompliance",
                "wageNonCompliance.cmt.no.longer",
                "Wage non compliance Failure to implement but claimant no longer activity exists", 0),

        WG_MOTION_FILED_ON_REQUIRED("wageMotionFiledOn", "wageMotionFiledOn.required",
                "Wage Motion filed on is required", 0),

        WG_MOTION_FILED_ON_DATE_FUTURE("wageMotionFiledOn", "wageMotionFiledOn.future",
                "Wage Motion filed on cannot be in future", 0),

        WG_MOTION_FILED_ON_EMPLOYER_NONE("wageMotionFiledOn",
                "wageMotionFiledOn.employer.none",
                "Wage Motion filed is not applicable when no employer selected", 0),

        WG_EFFECTIVE_DT_REQUIRED("wageEffectiveFrom", "wageEffectiveFrom.required",
                "Wage Effective Date is required", 0),

        WG_EFFECTIVE_DT_EMPLOYER_NONE("wageEffectiveFrom",
                "wageEffectiveFrom.employer.none",
                "Wage Effective Date exist when no employer selected", 0),

        WG_EFFECTIVE_UNILT_DT_EFF_DT_NONE("wageEffectiveUntil",
                "wageEffectiveUntil.wageEffectiveFrom.none",
                "Wage Effective Until Date exist when no Effective Date", 0),

        WG_EFFECTIVE_DT_GREATER_THAN_UNTIL_DT("wageEffectiveUntil",
                "wageEffectiveFrom.greaterthan.wageEffectiveUntil",
                "Wage Effective Date greater than Until Date", 0),

        WG_COURT_ORDERED_NO_WAGE_SUSPENDED("courtOrderedInd",
                "courtOrderedInd.no.wagesuspended",
                "Court Ordered No when Wage Suspended", 0),

        WG_COURT_ORDERED_REQUIRED("courtOrderedInd", "courtOrderedInd.required",
                "Court Ordered mandatory", 0),

        WG_COURT_ORDERED_DATE_REQUIRED("courtOrderedDate", "courtOrderedDate.required",
                "Court Ordered date mandatory", 0),

        WG_COURT_ORDERED_DATE_FUTURE("courtOrderedDate", "courtOrderedDate.future",
                "Court Ordered date canot be in future", 0),

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

}