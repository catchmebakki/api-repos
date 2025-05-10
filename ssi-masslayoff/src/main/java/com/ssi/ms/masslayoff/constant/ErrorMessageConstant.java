package com.ssi.ms.masslayoff.constant;
/**
 * @author Praveenraja Paramsivam
 * Interface containing ErrorMessageConstant for better code organization and reuse.
 */
public interface ErrorMessageConstant {
    String MRLR_ID_MANDATORY = "mlrlId.mandatory";
    String CLAIMANT_SOURCE_MANDATORY = "claimant.source.mandatory";
    String SSN_PATTEN_NOT_MATCHING = "ssn.pattern.not.matching";
    String SSN_MANDATORY = "ssn.mandatory";
    String CLAIMANT_LIST_MANDATORY = "claimant.list.mandatory";
    String FIRST_NAME_MANDATORY = "firstName.mandatory";
    String LAST_NAME_MANDATORY = "lastName.mandatory";
    String CLAIMANT_SOURCE_NOT_VALID = "claimant.source.notValid";
    String EMPLOYER_ACC_N0_MANDATORY = "uiAccountNo.notnull";
    String EMPLOYER_ACC_NO_NOT_10_DIGIT = "empAccNo.not.10.digit";
    String EMPLOYER_LOC_NOT_3_DIGIT = "empLoc.not.3.digit";
    String MUSM_ID_MANDATORY = "musmId.mandatory";
    String DELETE_CLAIMANT_MLRL_FINAL = "delete.claimant.mlrlId.final";
    String CLAIMANT_NOT_FOUND = "claimant.notFound";
    String EMPLOYER_NOT_FOUND = "Employer.notFound";
    String MUSM_ID_NOT_FOUND = "musmId.notFound";
    String MLRL_ID_NOT_FOUND = "mlrlId.notFound";
    String INTERNAL_ERROR = "internal.error";
    String CLAIMANT_MRLR_ALREADY_ASSOCIATED = "claimant.mrlr.already.associated";
    String MSLNO_NOT_NULL = "massLayOffNo.list.notnull";
    String STATUSCDVALUE_NOT_NULL = "statusCdValue.notnull";
    String STATUSCDVALUE_NOT_VALID = "statusCd.not-valid-value";
    String REMARKS_NOT_NULL = "remarks.notnull";
    String REMARKS_MAX_CHARACTERS = "remarks.noOfDigits";
    String MSLNO_MIN_DIGITS = "mslNo.min.1.digit";
    String MSLNO_MAX_DIGITS = "mslNo.max.10.digit";
    String EMPACCNUM_NOT_NULL = "uiAccountNo.notnull";
    String EMPACCLOC_NOT_NULL = "unit.notnull";
    String FILE_UPLOAD_FAILED = "fileUpload.failed";

    String SSN_INVALID = "ssn.inValid";
}
