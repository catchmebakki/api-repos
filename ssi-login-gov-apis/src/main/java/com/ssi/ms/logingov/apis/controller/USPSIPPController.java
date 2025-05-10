package com.ssi.ms.logingov.apis.controller;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.logingov.apis.dto.UspsIppResDTO;
import com.ssi.ms.logingov.apis.service.USPSIPPService;

import lombok.extern.slf4j.Slf4j;

/**
 * The USPSIPPController class handles requests related to USPS IPP (United
 * States Postal Service Intelligent Package Platform). This controller is
 * responsible for handling various endpoints related to USPS IPP
 * functionalities.
 * 
 * @Author: munirathnam.surepall
 */
@RestController
@RequestMapping("/")
@Validated
@Slf4j
@CrossOrigin
public class USPSIPPController {
	@Autowired
	private USPSIPPService uspsIPPService;

	/**
	 * Retrieves USPS SWA records for a given SWA XID.
	 * 
	 * @param swaXid the SWA XID to retrieve records for
	 * @return ResponseEntity containing LoginGovResDTO
	 * @throws Exception if an error occurs during the retrieval process
	 */
	@GetMapping(path = "/results/{swaXid}", produces = "application/json")
	public ResponseEntity<UspsIppResDTO> getRecordsBySwaXid(
			@Size(min = 33, max = 36) @PathVariable("swaXid") String swaXid) throws Exception {
		ResponseEntity<UspsIppResDTO> response = null;
		final UspsIppResDTO uspsIppResDTO = uspsIPPService.getRecordsBySwaXid(swaXid);
		final String errorMessage = uspsIppResDTO.getErrorMessage();
		if (errorMessage != null) {
			if (errorMessage.contains("No records found")) {
				response = new ResponseEntity<>(uspsIppResDTO, HttpStatus.NOT_FOUND);
			} else {
				response = new ResponseEntity<>(uspsIppResDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = new ResponseEntity<>(uspsIppResDTO, HttpStatus.OK);
		}
		return response;

	}

	/**
	 * Retrieves USPS of SWA and Enrollment Code.
	 *
	 * @param swaXid         The SWA XID to retrieve information for.
	 * @param enrollmentCode The enrollment code associated with the SWA XID.
	 * @return ResponseEntity containing the LoginGovResDTO.
	 * @throws Exception If there's an error processing the request.
	 */
	@GetMapping(path = "/swa/{swaXid}", produces = "application/json")
	public ResponseEntity<UspsIppResDTO> getRecordBySwaXidAndEnrollmentCode(
			@Size(min = 33, max = 36) @PathVariable("swaXid") String swaXid,
			@RequestParam("enrollment_code") String enrollmentCode) throws Exception {
		return new ResponseEntity<>(uspsIPPService.getRecordBySwaXidAndEnrollmentCode(swaXid, enrollmentCode),
				HttpStatus.OK);
	}
}
