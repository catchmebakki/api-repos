package com.ssi.ms.masslayoff.controller;

import com.ssi.ms.masslayoff.dto.claimant.ClaimantListReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.MrecClaimantListReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.MrecClaimantReqDTO;
import com.ssi.ms.masslayoff.service.ClaimantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

/**
 * @author Praveenraja Paramsivam
 * ClaimantListController provides services to ConfirmCmtList, addClaimantToList, deleteClaimantFromList, and
 *         getClaimant details.
 */
@RestController
@RequestMapping("/claimant")
@Validated
@Slf4j
@CrossOrigin
public class ClaimantListController {

	@Autowired
	private ClaimantService claimantService;

	/**
	 * searchLook method will provide lookups based search on the provided ClaimantListReqDTO.
	 * @param claimantListReqDTO {@link ClaimantListReqDTO} The request body containing search criteria.
	 * @return {@link ResponseEntity} ResponseEntity containing the response data.
	 */
	@PostMapping(path = "/lookup", produces = "application/json")
	public ResponseEntity searchLook(@Valid @RequestBody final ClaimantListReqDTO claimantListReqDTO) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(claimantService.getClaimantByIdMrlrId(claimantListReqDTO));
	}

	/**
	 * confirmClaimantList method is to confirm a list of claimants based on the
	 * provided MrecClaimantListReqDTO.
	 * @param mrecClaimantListReqDTO {@link MrecClaimantListReqDTO} The request body containing claimant
	 *                               confirmation data.
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseEntity} ResponseEntity containing the response data.
	 */
	@PatchMapping(path = "/", produces = "application/json")
	public ResponseEntity confirmClaimantList(@Valid @RequestBody final MrecClaimantListReqDTO mrecClaimantListReqDTO,
			HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(claimantService.confirmClaimantList(mrecClaimantListReqDTO, getLoggedInStaffId.apply(request)));
	}

	/**
	 * addClaimantToList method is to add claimants based on the provided MrecClaimantReqDTO.
	 * @param mrecClaimantReqDTO {@link MrecClaimantReqDTO} The request body containing claimant confirmation
	 *                           data.
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseEntity} ResponseEntity containing the response data.
	 */
	@PostMapping(path = "/", produces = "application/json")
	public ResponseEntity addClaimantToList(@Valid @RequestBody final MrecClaimantReqDTO mrecClaimantReqDTO,
			HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(claimantService.addClaimantToList(mrecClaimantReqDTO, getLoggedInStaffId.apply(request)));
	}

	/**
	 * deleteClaimantFromList method is to delete a claimant from a list based on
	 * the provided MrecClaimantListReqDTO.
	 * @param mrecClaimantListReqDTO {@link MrecClaimantListReqDTO} The request body containing claimant deletion data.
	 * @return {@link ResponseEntity} ResponseEntity containing the response data.
	 */
	@DeleteMapping(path = "/", produces = "application/json")
	public ResponseEntity deleteClaimantFromList(
			@Valid @RequestBody final MrecClaimantListReqDTO mrecClaimantListReqDTO) {
		claimantService.deleteClaimantFromList(mrecClaimantListReqDTO);
		return ResponseEntity.ok().build();
	}

	/**
	 * getClaimantDetails method is to retrieve ClaimantDetails based on the
	 * provided ClaimantReqDTO.
	 * @param claimantDto {@link ClaimantReqDTO} The request body containing claimant search criteria.
	 * @return {@link ResponseEntity} ResponseEntity containing the response data.
	 */
	@PostMapping(path = "/search", produces = "application/json")
	public ResponseEntity getClaimantDetails(@Valid @RequestBody final ClaimantReqDTO claimantDto) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(claimantService.getClaimantDetail(claimantDto));
	}

}
