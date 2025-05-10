package com.ssi.ms.logingov.apis.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.logingov.apis.dto.ClaimsResDTO;
import com.ssi.ms.logingov.apis.dto.SwaRecordFetchedDTO;
import com.ssi.ms.logingov.apis.service.LoginGovService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller class for handling Login.gov authentication requests. Author:
 * @Author: munirathnam.surepall
 */
@RestController
@RequestMapping("/")
@Validated
@Slf4j
@CrossOrigin
public class LoginGovController {
	@Autowired
	private LoginGovService loginGovService;

	/**
	 * Retrieves SWA records based on the provided SWA XID or UUID.
	 *
	 * @param swaXidOrUuid the SWA XID or UUID to retrieve records for
	 * @return ResponseEntity containing JSON representation of the SWA records
	 * @throws Exception if there is an error processing the request
	 */
	@GetMapping(path = "/claims/{swaXidOrUuid}", produces = "application/json")
	public ResponseEntity<String> getRecordsBySwaXidOrUuid(
			@Size(min = 33, max = 36) @PathVariable("swaXidOrUuid") String swaXidOrUuid) throws Exception {
		return loginGovService.getRecordsBySwaXidOrUuid(swaXidOrUuid);
	}

	/**
	 * Retrieves all claims in decrypted format and returns them as a list of
	 * GetClaimDecryptedResDTO objects.
	 *
	 * @return List of GetClaimDecryptedResDTO objects representing all claims.
	 * @throws Exception if an error occurs during the retrieval process.
	 */
	@GetMapping(path = "/allClaims", produces = "application/json")
	public @ResponseBody ResponseEntity<List<ClaimsResDTO>> getAllClaims() throws Exception {
		return loginGovService.getAllClaims();
	}

	/**
	 * PATCH endpoint to mark a claim as fetched.
	 * 
	 * @param swaXid              The unique identifier for the claim.
	 * @param swaRecordFetchedDTO The DTO containing the fetched claim data.
	 * @return A ResponseEntity containing a JSON response indicating the status of
	 *         the operation.
	 * @throws Exception If an error occurs during the operation.
	 */
	@PatchMapping(path = "/claims/{swaXid}", produces = "application/json")
	public ResponseEntity<String> markClaimAsFetched(@Size(min = 33, max = 36) @PathVariable("swaXid") String swaXid,
			@Valid @RequestBody SwaRecordFetchedDTO swaRecordFetchedDTO) throws Exception {
		final String jsonResponse;
		final ResponseEntity<String> status = loginGovService.markClaimAsFetched(swaRecordFetchedDTO, swaXid);
		if (status.getStatusCode() == HttpStatus.OK) {
			jsonResponse = "{\"status\": \"ok\"}";
		} else {
			jsonResponse = "{\"status\"" + ":\"" + status.getStatusCode() + "\"}";
		}
		return new ResponseEntity<>(jsonResponse, status.getStatusCode());
	}
}