package com.ssi.ms.logingov.apis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssi.ms.logingov.apis.constant.ApplicationConstants;
import com.ssi.ms.logingov.apis.constant.ErrorMessageConstant;
import com.ssi.ms.logingov.apis.dto.UspsIppResDTO;
import com.ssi.ms.logingov.apis.handler.JWTHandler;

import lombok.NoArgsConstructor;

/**
 * Service class for interacting with the United States Postal Service's
 * Intelligent Mail Package Protocol (USPS IPP).
 *
 * This class provides functionality related to USPS IPP, such as generating
 * shipping labels, tracking packages, and interacting with USPS services.
 *
 * Note: This class is designed to work with the USPS IPP protocol, which
 * enables businesses to integrate their shipping and tracking processes with
 * the USPS.
 * 
 * @Author: munirathnam.surepall
 */
@Service
@NoArgsConstructor
public class USPSIPPService {
	private static String jwtToken;
	private static RestTemplate restTemplate = new RestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private static String url;
	// The USPS web service path injected from application.yml
	@Value("${webservice-usps-path}")
	private String uspsIPPPrefix;

	@Autowired
	private JWTHandler jWTHandler;

	/**
	 * Retrieves USPS SWA records for the given SWA XID.
	 * 
	 * @param swaXid the SWA XID to retrieve records for
	 * @return a LoginGovResDTO object containing the USPS SWA records
	 * @throws Exception if an error occurs during the retrieval process
	 */
	public UspsIppResDTO getRecordsBySwaXid(String swaXid) throws Exception {
		UspsIppResDTO resDTO = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		url = uspsIPPPrefix + ApplicationConstants.USPSIPP_URL_PATH + swaXid + ApplicationConstants.FSLASH;
		jwtToken = "JWT " + jWTHandler.getJWT();
		final HttpEntity<String> jwtEntity = new HttpEntity<>(getHeaders(headers, jwtToken));
		try {
			resDTO = restTemplate.exchange(url, HttpMethod.GET, jwtEntity, UspsIppResDTO.class).getBody();
		} catch (HttpClientErrorException e) {
			resDTO = new UspsIppResDTO();
			resDTO.setErrorMessage(objectMapper.readTree(e.getResponseBodyAsString()).get("error_message").toString()
					.replaceAll("\"", ""));
		}
		return resDTO;
	}

	/**
	 * Retrieves the Login.gov resource DTO containing USPS data based on the
	 * provided SWA XID and enrollment code.
	 *
	 * @param swaXid         The SWA XID associated with the user.
	 * @param enrollmentCode The enrollment code associated with the user.
	 * @return A LoginGovResDTO object containing USPS data.
	 * @throws Exception If an error occurs during the retrieval process.
	 */
	public UspsIppResDTO getRecordBySwaXidAndEnrollmentCode(String swaXid, String enrollmentCode) throws Exception {
		UspsIppResDTO resEntity = null;
		url = uspsIPPPrefix + ApplicationConstants.USPSIPP_URL_PATH + swaXid + ApplicationConstants.ENROLLMENT_URL_PATH
				+ enrollmentCode;
		jwtToken = "JWT " + jWTHandler.getJWT();
		final HttpEntity<String> jwtEntity = new HttpEntity<>(getHeaders(headers, jwtToken));
		try {
			resEntity = restTemplate.exchange(url, HttpMethod.GET, jwtEntity, UspsIppResDTO.class).getBody();
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessageConstant.GET_RECORDS_BY_SWAXID_FAILED, e);
		}
		return resEntity;
	}

	public HttpHeaders getHeaders(HttpHeaders headers, String jwtToken) {
		headers.set(ApplicationConstants.AUTHORIZATION, jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}