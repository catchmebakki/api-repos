package com.ssi.ms.configuration.constant;

import com.ssi.ms.configuration.util.ConfigErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ErrorMessageConstant {
    String PAR_ID_MANDATORY = "parId.mandatory";

    String ALC_ID_MANDATORY = "alcId.mandatory";

    String ALC_CATEGORY_MANDATORY = "alcCategoryCd.mandatory";

    String ALV_ID_MANDATORY = "alvId.mandatory";

    String ALV_SORT_ORDER_MANDATORY = "alvSortOrderNbr.mandatory";

    String ALV_ID_NOT_FOUND = "alvId.notFound";

    String PAR_CATEGORY_CD_MANDATORY = "parCategoryCd.mandatory";

    String PAR_ID_NOT_FOUND = "parId.notFound";

    String PAR_VALUE_MANDATORY = "parValue.mandatory";

    String PAR_REMARKS_MANDATORY = "parRemarks.mandatory";

    String PAR_MAME_MANDATORY = "parName.mandatory";

    String ALV_TYPE_MANDATORY = "alvType.mandatory";

    String NAME_MANDATORY = "name.mandatory";
    String DISPLAY_ON_MANDATORY = "displayOn.mandatory";
    String COMMENTS_MANDATORY = "comments.mandatory";

    String WSCC_ID_MANDATORY = "wsccId.mandatory";

    String MODIFICATION_TYPE_MANDATORY = "modificationType.mandatory";

    String MODIFICATION_DATE_MANDATORY = "modificationDate.mandatory";

    String MODIFICATION_DT_MANDATORY = "modificationDt.mandatory";

    String INITIAL_CLAIM_MANDATORY = "initialClaim.mandatory";

    String ADDITIONAL_CLAIM_MANDATORY = "additionalClaim.mandatory";

    String INCREMENT_FREQ_MANDATORY = "incrementFrequency.mandatory";

    String INCREMENT_VALUE_MANDATORY = "incrementVal.mandatory";

    String WSWC_ID_MANDATORY = "wswcId.mandatory";

    String REASON_CD_MANDATORY = "reasonCd.mandatory";

    String BUSINESS_UNIT_MANDATORY = "businessUnit.mandatory";

    String REASON_CD_INVALID = "reasonCd.inValid";

    String WSCC_ID_NOT_FOUND = "wsccId.notFound";

    String WSWC_ID_NOT_FOUND = "wswcId.notFound";

    String BUSINESS_UNIT_INVALID = "businessUnit.inValid";

    String DISPLAY_ON_CD_INVALID = "displayOnCd.inValid";
    String ACTIVE_NOT_VALID_VAL = "active.not-valid-value";
    String MODIFICATION_TYPE_NOT_VALID_VAL = "modificationType.not-valid-value";

    @Getter
    @AllArgsConstructor
    enum ParameterErrorDetail implements ConfigErrorEnum {
        CONFIG_PAR_NUMERICVAL_IS_REQUIRED(1, "Parameter numeric value is required.", "numericValue", "numericValue.notnull"),
        CONFIG_PAR_NUMERICVAL_IS_NOT_ALLOWED(2, "Parameter cannot have numeric value.", "numericValue", "numericValue.notAllowed"),
        CONFIG_PAR_TEXTVAL_IS_REQUIRED(3, "Parameter text value is mandatory.", "textValue", "textValue.notnull"),
        CONFIG_PAR_TEXTVAL_IS_NOT_ALLOWED(4, "Parameter cannot have text value.", "textValue", "textValue.notAllowed"),
        CONFIG_PAR_TEXTVAL_DATE_INVALID(5, "Parameter text value cannot be date.", "textValue", "textValue.dateNotAllowed"),
        CONFIG_PAR_DATEVAL_IS_REQUIRED(6, "Parameter date value is mandatory.", "dateValue", "dateValue.notnull"),
        CONFIG_PAR_DATEVAL_IS_NOT_ALLOWED(7, "Parameter cannot have date value.", "dateValue", "dateValue.notAllowed"),
        CONFIG_PAR_INACTIVE(8, "Inactive Parameter cannot be edited", "editFlag", "parameterEdit.inActive"),
        CONFIG_PAR_REINSTATE_INACTIVE(9, "Inactive Parameter with past End Date cannot be re-instated", "editFlag",
                "parameterReactivate.inActive"),
        CONFIG_PAR_REINSTATE_INVALID(10, "Parameter can only be re-instated with valid future date", "editFlag",
                "parReinstate.inValidDate"),
        CONFIG_PAR_REINSTATE_ACTIVE_EXISTS(11, "Parameter cannot be Reinstated, parameter that this entry pertains to is currently active",
                "editFlag", "parReinstate.activeExists"),
        CONFIG_PAR_CHANGEDT_INVALID(12, "Change As of Date is Invalid. Change As of date has to be a future date.",
                "modificationDt", "changeAsOfDt.inValid"),
        CONFIG_PAR_ENDDT_INVALID(12, "Parameter End Date is Invalid. Parameter End Date has to be a future date.",
                "modificationDt", "parEndDt.inValid"),
        CONFIG_PAR_REINSTATEDT_INVALID(12, "Reinstate Date is Invalid. Reinstate date has to be a future date.",
                "modificationDt", "reinstateDt.inValid"),
        CONFIG_PAR_CHANGEDT_INVALID_EFFDT(13,
                "Change As of Date is Invalid. Change As of Date cannot be on or prior to Start/Effective Date of Parameter.",
                "modificationDt", "changeAsOfDt.inValidEffDt"),
        CONFIG_PAR_ENDDT_INVALID_EFFDT(13,
                "Parameter End Date is Invalid. Parameter End Date cannot be on or prior to Start/Effective Date of Parameter.",
                "modificationDt", "parEndDt.inValidEffDt"),
        CONFIG_PAR_REINSTATEDT_INVALID_EFFDT(13,
                "Reinstate Date is Invalid. Reinstate Date cannot be on or prior to Start/Effective Date of Parameter.",
                "modificationDt", "reinstateDt.inValidEffDt"),
        CONFIG_PAR_DELETE_NOT_ALLOWED(14, "Parameter is not a future entry. Parameter cannot be deleted.", "parDelete", "parDelete.notAllowed"),
        CONFIG_PAR_ENDDATE_NOT_ALLOWED(15, "Inactive Parameter cannot be end-dated.", "parEndDate", "parEndDate.notAllowed"),
        CONFIG_PAR_ENDDATE_NOT_VALID(16,
            "Parameter cannot be end-dated. End date has to be a valid future date that is after the current Effective Date of the parameter entry",
            "parEndDate", "parEndDate.inValid"),
        CONFIG_PAR_EFF_UNTIL_NOT_VALID(17, "Effective until cannot be prior to Effective As Of Date", "effectiveUntilDt",
                "effectiveUntilDt.inValid"),
        CONFIG_PAR_EFF_UNTIL_MANDATORY(18, "Effective until Date is Mandatory", "effectiveUntilDt",
                "effectiveUntilDt.mandatory"),
        CONFIG_PAR_OPEN_EFF_UNTIL_NOT_ALLOWED(19, "Effective until is not allowed on an Open-Ended parameter", "effectiveUntilDt",
                "effectiveUntilDt.notAllowed"),
        CONFIG_PAR_CHANGE_NOT_ALLOWED(20, "Parameter change is not allowed. Please modify at-least one attribute of the parameter.",
                "parChange", "parChange.notAllowed"),
        CONFIG_PAR_CHANGEDT_PAST_EXPIRED(21,
                "Change As of Date is Invalid. Change As of Date has to be prior to Expiration Date of the parameter being modified.",
                "modificationDt", "changeAsOfDt.inValidExpired"),
        CONFIG_PAR_EFFECTIVE_UNTIL_PRIOR_EXPIRED(22,
                "Effective Until Date is Invalid. Effective Until date cannot be prior to Expiration Date of the parameter being modified.",
                "effectiveUntilDt", "effectiveUntilDt.inValidExpired"),
        CONFIG_PAR_PARNAME_INVALID(23,
                "Please enter a valid parameter name. Parameter name should not exceed 60 characters.",
                "name", "name.inValid"),
        CONFIG_PAR_NUMERICVAL_INVALID(24,
                "Please enter a valid parameter numeric value. Parameter Numeric Value should be a 15 digits with 4 decimal points.",
                "numericValue", "numericValue.inValid"),
        CONFIG_PAR_TEXTVAL_INVALID(25,
                "Please enter a valid parameter alpha value. Parameter alpha value should not exceed 100 characters.",
                "textValue", "textValue.inValid");
        private final int code;
        private final String description;
        private final String frontendField;
        private final String frontendErrorCode;
    }

    @Getter
    @AllArgsConstructor
    enum WorkSearchAlvErrorDetail implements ConfigErrorEnum {
        CONFIG_ALV_INACTIVE(1, "Inactive ALV cannot be edited", "editFlag", "alvEdit.inActive"),
        CONFIG_ALV_REINSTATE_ACTIVE(1, "Active ALV cannot be Reactivated", "editFlag", "alvEdit.reactivateInvalid"),
        CONFIG_ALV_TYPE_MANDATORY(2, "ALV Decipher is Mandatory", "alvDecipherCd", "alvDecipher.mandatory"),
        CONFIG_ALV_DECIPHER_INVALID(3, "ALV does not have Decipher", "alvDecipherCd", "alvDecipher.inValid"),
        CONFIG_ALV_NAME_INVALID(3, "Please enter a valid Name. Name should not exceed 50 characters.",
                "name", "name.inValid"),
        CONFIG_ALV_SPANISH_NAME_INVALID(3, "Please enter a valid Spanish Name. Spanish Name should not exceed 200 characters.",
                "spanishName", "spanishName.inValid"),
        CONFIG_ALV_DESCRIPTION_INVALID(3, "Please enter a valid Description. Description should not exceed 1000 characters.",
                "description", "description.inValid");
        private int code;
        private String description;
        private String frontendField;
        private String frontendErrorCode;
    }

    @Getter
    @AllArgsConstructor
    enum WorkSearchConfigErrorDetail implements ConfigErrorEnum {
        CONFIG_COMMENT_INVALID(1, "Generated User Comments total length is exceeding 4000 characters", "comments", "comments.inValid");
        private int code;
        private String description;
        private String frontendField;
        private String frontendErrorCode;
    }
    @Getter
    @AllArgsConstructor
    enum WorkSearchReqErrorDetail implements ConfigErrorEnum {
        CONFIG_WSCC_INACTIVE(1, "Inactive Work Search Requirement cannot be edited", "editFlag", "requirementEdit.inActive"),
        CONFIG_WSCC_CONFIGURATIONDATE_INVALID_EFFDT(3,
                "Configuration Date is Invalid. Configuration date cannot be on or prior to Start/Effective Date of Parameter.",
                "modificationDate", "configurationDate.inValidEffDt"),
        CONFIG_WSCC_STARTDATE_INVALID_EFFDT(3,
                "Start Date is Invalid. Start date cannot be on or prior to current Start/Effective Date of Parameter.",
                "modificationDate", "startDate.inValidEffDt"),
        CONFIG_WSCC_CONFIGURATIONDATE_INVALID(3, "Configuration Date is Invalid. Configuration date has to be a future date.", "modificationDate",
                "configurationDate.inValid"),
        CONFIG_WSCC_STARTDATE_INVALID(3, "Start Date is Invalid. Start date has to be a future date.", "modificationDate",
                "startDate.inValid"),
        CONFIG_WSCC_CONFIGURATIONDATE_EXPDT_INVALID(3,
                "Configuration Date is Invalid. Configuration date cannot be greater than Expiration Date of Work Search Requirement.",
                "modificationDate", "configurationDate.inValidExp"),
        CONFIG_WSCC_STARTDATE_EXPDT_INVALID(3, "Start Date is Invalid. Start date cannot be greater than Expiration Date of Work Search Requirement.",
                "modificationDate", "startDate.inValidExp"),
        CONFIG_WSCC_INIT_CLAIM_INVALID(4, "Initial Claim Value has to be two digit integer",
                "initialClaim", "initialClaim.inValid"),
        CONFIG_WSCC_ADDL_CLAIM_INVALID(5, "Additional Claim Value has to be two digit integer",
                "additionalClaim", "additionalClaim.inValid"),
        CONFIG_WSCC_INCREMENT_FREQUENCY_INVALID(6, "Increment Frequency Value has to be two digit integer",
                "incrementFrequency", "incrementFrequency.inValid"),
        CONFIG_WSCC_DELETE_NOT_ALLOWED(14, "Work Search Requirement is not a future entry. Work Search Requirement cannot be deleted.",
                "wsccDelete", "wsccDelete.notAllowed"),
        CONFIG_WSCC_INCREMENT_VALUE_INVALID(7, "Increment Value has to be two digit integer",
                "incrementVal", "incrementVal.inValid");
        private int code;
        private String description;
        private String frontendField;
        private String frontendErrorCode;
    }

    @Getter
    @AllArgsConstructor
    enum WorkSearchWaiverErrorDetail implements ConfigErrorEnum {
        CONFIG_WSWC_INACTIVE(1, "Inactive Work Search Waiver cannot be edited", "editFlag", "waiverEdit.inActive"),
        CONFIG_WSWC_REACTIVATE_INACTIVE(2, "Invalid Reactivate Date. Reactivate Date should be greater than end date of waiver.", "reactivateOn",
                "waiverReactivate.inActive"),
        CONFIG_WSWC_REACTIVATE_ACTIVE(2, "Active Work Search Waiver cannot be reactivated", "reactivateOn",
                "waiverReactivate.active"),
        CONFIG_WSWC_CONFIGURATIONDATE_INVALID(3, "Configuration Date is Invalid. Configuration date has to be a future date.",
                "modificationDate", "configurationDate.inValid"),
        CONFIG_WSWC_ENDDATE_INVALID(3, "End Date is Invalid. End date has to be a future date.",
                "modificationDate", "endDate.inValid"),
        CONFIG_WSWC_STARTDATE_INVALID(3, "Start Date is Invalid. Start date has to be a future date.",
                "modificationDate", "startDate.inValid"),
        CONFIG_WSWC_DEACTIVATEDATE_INVALID(3, "Deactivate Date is Invalid. Deactivate date has to be a future date.",
                "modificationDate", "deactivateDate.inValid"),
        CONFIG_WSWC_REACTIVATEDATE_INVALID(3, "Reactivate Date is Invalid. Reactivate date has to be a future date.",
                "modificationDate", "reactivateDate.inValid"),
        CONFIG_WSWC_CONFIGURATIONDATE_INVALID_EFFDT(3,
                "Configuration Date is Invalid. Configuration date cannot be on or prior to Start/Effective Date of Waiver.",
                "modificationDate", "configurationDate.inValidEffDt"),
        CONFIG_WSWC_ENDDATE_INVALID_EFFDT(3,
                "End Date is Invalid. End date cannot be on or prior to Start/Effective Date of Waiver.",
                "modificationDate", "endDate.inValidEffDt"),
        CONFIG_WSWC_STARTDATE_INVALID_EFFDT(3,
                "Start Date is Invalid. Start date cannot be on or prior to Start/Effective Date of Waiver.",
                "modificationDate", "startDate.inValidEffDt"),
        CONFIG_WSWC_DEACTIVATE_INVALID_EFFDT(3,
                "Deactivate Date is Invalid. Deactivate date cannot be on or prior to Start/Effective Date of Waiver.",
                "modificationDate", "deactivateDate.inValidEffDt"),
        CONFIG_WSWC_REACTIVATE_INVALID_EFFDT(3,
                "Reactivate Date is Invalid. Reactivate date cannot be on or prior to Start/Effective Date of Waiver.",
                "modificationDate", "reactivateDate.inValidEffDt"),
        CONFIG_WSWC_CONFIGURATIONDATE_EXPDT_INVALID(3,
                "Configuration Date is Invalid. Configuration date cannot be greater than Expiration Date of Waiver.",
                "modificationDate", "configurationDate.inValidExp"),
        CONFIG_WSWC_DELETE_NOT_ALLOWED(14,
                "Work Search Waiver is not a future entry. Work Search Waiver cannot be deleted.",
                "wswcDelete", "wswcDelete.notAllowed"),
        CONFIG_WSWC_STARTDATE_EXPDT_INVALID(3,
                "Start Date is Invalid. Start date cannot be greater than Expiration Date of Waiver.",
                "modificationDate", "startDate.inValidExp");

        private int code;
        private String description;
        private String frontendField;
        private String frontendErrorCode;
    }
}
