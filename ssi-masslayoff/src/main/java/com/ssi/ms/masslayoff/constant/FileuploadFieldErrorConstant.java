package com.ssi.ms.masslayoff.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author Praveenraja Paramsivam
 * Interface containing FileuploadFieldErrorConstant for better code organization and reuse.
 */
public interface FileuploadFieldErrorConstant {
	String DISCARD_HEADER_MSG = "Missing Header Value. Invalid column no:";
	String FILE_PARSING_FAILED = "File parsing failed.";
	String FILE_UPLOAD_FAILED = "File upload failed.";
	String DISCARD_HEADER_COMLUMN = "Missing header column in the uploaded excel. It has {0} columns instead of 10.";
	String HEADER_WORKBOOK_MISSING = "Missing Workbook in the uploaded excel.";
	String HEADER_SHEET_MISSING = "Missing Sheet in the uploaded excel.";
	String HEADER_ROW_MISSING = "Missing Row in the uploaded excel.";
	String ADDMSL_VALIDATION_FAILED = "Add Mass-layoff validation failed.";
	String EDITMSL_VALIDATION_FAILED = "Edit Mass-layoff validation failed.";
	String VALIDATING_STAGING_TO_SUMMARY_DATA = "Staging record parsing failed, must id: {0} , "
			+ "Check error desc from error table for more details.";
	String CLONEMSL_VALIDATION_FAILED = "Clone Mass Layoff validation failed.";
	String MSL_NUM = "MSL_NUM";
	String EMP_AC_NUM  = "EMP_AC_NUM";
 	String EMP_AC_LOC  = "EMP_AC_LOC";
	String MSL_DATE  = "MSL_DATE";
	String MSL_EFF_DATE = "MSL_EFF_DATE";
	String RECALL_DATE  = "RECALL_DATE ";
	String DI_IND = "DI_IND";
	String SSN = "SSN";
	String FIRST_NAME = "FIRST_NAME";
	String LAST_NAME = "LAST_NAME";
	String RECORDS_FAILED = "Some records are failed, for more details check error table.";
	String EXCEPTION_PROCESSING_RECORDS = "Exception while processing this record";
	int SSN_LENGTH = 9;
	/**
	 * Enumeration representing upload field error details.
	 */
    @Getter
	@AllArgsConstructor
	enum UploadFieldErrorDetail {
		// Summary validation and AddMassLayOff fields validation here
		MSL_MSLNUMBER_IS_REQUIRED(1, "Mass Layoff Number should be mandatory.", "massLayOffNo", "massLayOffNo.notnull"),
		MSL_MSLNUMBER_IS_NUMERIC_REQUIRED(2, "Mass Layoff Number should be Numeric.", "massLayOffNo", "massLayOffNo.numeric"),
		MSL_EMPACCNUMBER_NOT_IN_NHUIS(3, "Employer Account number should be existing in NHUIS.", "uiAccountNo",
				"uiAccountNo.notinnhuis"),
		MSL_EMPACCLOC_NOT_IN_NHUIS(4, "Employer Account Location should be existing in NHUIS.", "unit", "unit.notinnhuis"),
		MSL_MSLDATE_IS_REQUIRED(5, "The uploaded file is missing the Mass Layoff date.", "massLayOffDate", "massLayOffDate.notnull"),
		MSL_MSLDATE_INVALID_PAST_DATE(6, "Mass Layoff date is earlier than the previous Sunday.", "massLayOffDate",
				"massLayOffDate.invalidpastdate"),
		MSL_MSLDATE_INVALID_FUTURE_DATE(7, "Mass Layoff date is later than 2 months in the future.", "massLayOffDate",
				"massLayOffDate.invalidfuturedate"),
		MSL_MSLEFFDATE_IS_REQUIRED(8, "The uploaded file is missing the Mass Layoff Effective date.", "mslEffectiveDate.",
				"mslEffectiveDate.notnull"),
		MSL_MSLEFFDATE_SUNDAY(9, "Mass Layoff Effective Date should be same as Mass Layoff date since Mass Layoff date is on Sunday.",
				"mslEffectiveDate", "mslEffectiveDate.sunday"),
		MSL_MSLEFFDATE_WEEKDAY(10, "Mass Layoff Effective Date should be preceding or following Sunday.", "mslEffectiveDate",
				"mslEffectiveDate.weekday"),
		MSL_RECALLDATE_IS_REQUIRED(11, "The uploaded file is missing the Recall date.", "recallDate", "recallDate.notnull"),
		MSL_RECALLDATE_IS_INVALID(12, "Recall date should not be earlier than the Mass Layoff Date.", "recallDate",
				"recallDate.invalid"),
		// ssn and cliamant data validation
		SSN_NUMBER_IS_INVALID(13, "Please re-enter the SSN # as this must be 9 digits.", "ssn", "ssn.invalid"),
		FIRST_NAME_IS_REQUIRED(14, "The uploaded file is missing the claimant's First name.", "firstName.", "firstName.notnull"),
		FIRSTNAME_NOT_IN_NHUIS(15, "First name does not match the NHUIS. Correct and reupload the file or enter this claimant manually.",
				"firstName", "firstName.notNHUIS"),
		LAST_NAME_IS_REQUIRED(16, "The uploaded file is missing the claimant's Last name.", "lastName", "lastName.notnull"),
		LASTNAME_NOT_IN_NHUIS(17, "Last name does not match the NHUIS. Correct and reupload the file or enter this claimant manually.",
				"lastName",	"lastName.notNHUIS"),
		// After Stored Staging table, Staging to summary validation
		MSLNUMBER_IS_NOT_EQUALS(18, "The uploaded file MSL# does not match NHUIS.Add claimant manually or update excel and upload again.",
				"massLayOffNo",
				"massLayOffNo.notequal"),
		EMPACCNUMBER_IS_NOT_EQUALS(19, "The file Emp Acct # does not match NHUIS.Add claimant manually or update excel and upload again.",
				"uiAccountNo",
				"uiAccountNo.notequal"),
		EMPACCLOC_IS_NOT_EQUALS(20, "The file Emp Acct Unit does not match NHUIS.Add claimant manually or update excel and upload again.",
				"unit",	"unit.notequal"),
		MSLDATE_IS_NOT_EQUALS(21, "The uploaded file Mass Layoff date does not match NHUIS.", "massLayOffDate",
				"massLayOffDate.notequal"),
		MSLEFFDATE_IS_NOT_EQUALS(22, "The uploaded file Mass Layoff Effectived date does not match NHUIS.",
				"mslEffectiveDate", "mslEffectiveDate.notequal"),
		RECALLDATE_IS_NOT_EQUALS(23, "The uploaded file Recall date does not match NHUIS.", "recallDate",
				"recallDate.notequal"),
		ADDMSL_MSLNO_ALREADY_IN_MASSLAYOFF(24, "Mass Layoff number already there in NHUIS.", "massLayOffNo",
				"massLayOffNo.alreadythereinnhuis"),
		//Edit Mass Layoff validation
		MSL_PENDING_CLIAMANTS_EXISTS(25, "Pending claimants exists in DB.", "statusCdValue", "statusCdValue.Pendingcliamantsexists"),
		PARSING_ERROR(26, "Parsing failed.", "", ""),
		MSL_FINAL_UPLOAD_NOT_ALLOWED(27, "Upload not allowed when MSL is in Final status.",
				"statusCdValue", "statusCdValue.finalUploadNotAllowed"),
		CLAIMANT_MRLR_ALREADY_ASSOCIATED(28, "The claimant you are trying to add to this Mass Layoff is already associated with "
				+ "the MSL Ref List.", "ssn", "claimant.mrlr.already.associated"),
		DI_IND_IS_NOT_EQUALS(29, "The uploaded file DI indicator does not match NHUIS.", "deductibleIncome", "deductibleIncome.notequal");
		private int code;
		private String description;
		private String frontendField;
		private String frontendErrorCode;
	}
}