package com.ssi.ms.logingov.apis.constant;

/**
 * Interface defining application-wide constants. These constants can be used
 * throughout the application for configuration, defining default values, or
 * specifying constant values. 
 * @Author: munirathnam.surepall
 */
public interface ApplicationConstants {
	String SWACLIENT = "-swaclient/";
	String LOGIN_GOV_URL_PATH = "/swa/v1/claims/";
	String USPSIPP_URL_PATH = "/swa/results/";
	String USER_AGENT = "User-Agent";
	String AUTHORIZATION = "Authorization";
	String SWA_OR_ISSUER = "NH";
	String LANG_CD = "en";
	String VERSION = "0.1.0";
	String IAT = "iat";
	String ISS = "iss";
	String NONCE = "nonce";
	String ALG_EC = "EC";
	String KID = "kid";
	String ALGORITHM_ES256 = "ES256";
	String FSLASH = "/";
	String ENROLLMENT_URL_PATH = "/?enrollment_code=";
	String JWE = "JWE";
}
