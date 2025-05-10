package com.ssi.ms.logingov.apis.constant;

/**
 * Interface containing ErrorMessageConstant for better code organization and
 * reuse.
 * @Author: munirathnam.surepall
 */
public interface ErrorMessageConstant {
	String SWA_XID_MANDATORY = "swa_xid.mandatory";
	String FETCHED_NOT_NULL = "fetched.notnull";
	String FETCHED_MAX_CHARACTERS = "fetched.noOfDigits";
	String DECRYPT_EXCEPTION_MSG="Exception occured IOException OR JOSEException OR NoSuchAlgorithmException OR InvalidKeySpecException.";
	String IOEXCEPTION_MSG  ="IOException occured while reading the file.";
	String NOSUCHALGORITHMEXCEPTION_MSG  ="NoSuchAlgorithm Exception occured while sending algorithm to key factory.";
	String INVALIDKEYSPECEXCEPTION_MSG = "InvalidKeySpec Exception occured while generating private key.";
	String JOSEEXCEPTION_MSG = "Jose Exception occured while calling the jwtBuilder.decrypt().";
	String FILE_READ_FAILED = "fileRead.failed";
	String NO_SUCH_ALGORITHM_FOUND = "noSuchAlgorithm.found";
	String KEYSPEC_INVALID = "keySpec.invalid";
	String CLAIM_READ_FAILED = "claimRead.failed";
	String CLAIMS_READ_FAILED = "Exception occured while fetching claims.";
	String GET_RECORDS_BY_SWAXID_FAILED = "Exception occured while fetching records by using swa_xid.";
}
