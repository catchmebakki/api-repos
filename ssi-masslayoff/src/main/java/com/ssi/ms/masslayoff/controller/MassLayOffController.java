package com.ssi.ms.masslayoff.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.ssi.ms.masslayoff.dto.lookup.LookupReqDTO;
import com.ssi.ms.masslayoff.service.LookUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssi.ms.masslayoff.constant.ApplicationConstant;
import com.ssi.ms.masslayoff.constant.ErrorMessageConstant;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.dto.CloneMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.EditMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MslRefListMlrlResDTO;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsReqDTO;
import com.ssi.ms.masslayoff.service.MassLayOffService;
import com.ssi.ms.masslayoff.service.MassLayOffUploadService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author munirathnam.surepalli
 * MassLayOffController provides services to add, edit, upload form, upload file,
 * re-upload file, clone and statistics for MassLayOff details.
 */
@RestController
@RequestMapping("/")
@Validated
@Slf4j
@CrossOrigin
public class MassLayOffController {

	@Autowired
	private MassLayOffUploadService massLayOffUploadService;
	@Autowired
	private MassLayOffService massLayOffService;

	@Autowired
	private LookUpService lookUpService;

	/**
	 * Handle the upload form request for mass layoff data.
	 *
	 * @param uploadMassLayOffReqDTO {@link MassLayOffReqDTO} The MassLayOffReqDTO containing the uploaded data.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the upload request.
	 * @return {@link ResponseEntity<Object>} A ResponseEntity containing the response for the upload request.
	 * @throws ValidationException If validation of the MassLayOffReqDTO fails.
	 */
	@PostMapping(path = "/uploadForm", produces = "application/json")
	public ResponseEntity<Object> uploadForm(
			@Valid @RequestBody @NotNull(message = "uploadMassLayOffReqDTO.notnull") final MassLayOffReqDTO uploadMassLayOffReqDTO,
			HttpServletRequest request) {
		final MslUploadSummaryMusmDAO mslUpload = massLayOffService.uploadForm(uploadMassLayOffReqDTO, request);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(Map.of(ApplicationConstant.MUSMID, mslUpload.getMusmId()));
	}

	/**
	 * Handle the upload request for a specific musmId and file.
	 *
	 * @param musmId {@link Long} The musmId associated with the upload.
	 * @param file {@link MultipartFile} The MultipartFile containing the uploaded file data.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the upload request.
	 * @return {@link ResponseEntity<Map<String, String>>} A ResponseEntity containing a map of status information for the upload.
	 */
	@PostMapping(path = "/upload/musmid/{musmId}", produces = "application/json")
	public ResponseEntity<Map<String, String>> upload(@PathVariable Long musmId,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		massLayOffUploadService.uploadForMusmId(file, musmId, request);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(Map.of(ApplicationConstant.UPLOADED_FILENAME, file.getOriginalFilename()));
	}

	/**
	 * Handle the append claimants request for a specific mlrlId and file.
	 *
	 * @param mlrlId {@link long} The mlrlId associated with the append claimants request.
	 * @param file {@link MultipartFile} The MultipartFile containing the data to be appended.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the append claimants request.
	 * @return {@link ResponseEntity<Map<String, String>>} A ResponseEntity containing a map of status information for the
	 * append claimants operation.
	 */
	@PostMapping(path = "/upload/mlrlid/{mlrlId}", produces = "application/json")
	public ResponseEntity<Map<String, String>> appendClaimantsForMlrlId(
			@PathVariable long mlrlId, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
		massLayOffService.uploadforMlrl(file, mlrlId, request);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(Map.of(ApplicationConstant.UPLOADED_FILENAME, file.getOriginalFilename()));
	}

	/**
	 * Retrieve upload statistics based on the provided UploadStatisticsReqDTO.
	 *
	 * @param uploadStatisticsReqDTO {@link UploadStatisticsReqDTO} The UploadStatisticsReqDTO containing criteria for
	 * retrieving upload statistics.
	 * @return {@link ResponseEntity} A ResponseEntity containing the upload statistics response.
	 */
	@PostMapping(path = "/uploadStat", produces = "application/json")
	public ResponseEntity getUploadStatistics(@Valid @RequestBody UploadStatisticsReqDTO uploadStatisticsReqDTO) {
		return ResponseEntity.ok().body(massLayOffUploadService.getUploadStatistics(uploadStatisticsReqDTO));
	}

	/**
	 * Handle the request to add a mass layoff record.
	 *
	 * @param massLayOffReqDTO {@link MassLayOffReqDTO} The MassLayOffReqDTO containing the information to be added.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link ResponseEntity<Object>} A ResponseEntity containing the response for the add mass layoff request.
	 * @throws ValidationException If validation of the MassLayOffReqDTO fails.
	 */
	@PostMapping(path = "/save", produces = "application/json")
	public ResponseEntity<Object> addMassLayOff(
			@Valid @RequestBody @NotNull(message = "massLayOffReqDTO.notnull") final MassLayOffReqDTO massLayOffReqDTO,
			HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(massLayOffService.addMassLayOff(massLayOffReqDTO, request));
	}

	/**
	 * Retrieve a mass layoff record based on the provided mlrlId.
	 *
	 * @param mlrlId {@link Long} The mlrlId associated with the mass layoff record to retrieve.
	 * @return {@link ResponseEntity<MslRefListMlrlResDTO>} A ResponseEntity containing the mass layoff record response.
	 * @throws ValidationException If validation of the mlrlId fails.
	 */
	@GetMapping(path = "/mlrlid/{mlrlId}", produces = "application/json")
	public ResponseEntity<MslRefListMlrlResDTO> getMassLayOff(
			@Valid @PathVariable @NotNull(message = ErrorMessageConstant.MRLR_ID_MANDATORY) Long mlrlId) {
		final MslRefListMlrlResDTO massLayOffResDTO = massLayOffService.getMassLayOff(mlrlId);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(massLayOffResDTO);
	}

	/**
	 * Handle the request to update a mass layoff record.
	 *
	 * @param editMassLayOffReqDTO {@link EditMassLayOffReqDTO} The EditMassLayOffReqDTO containing the information to be updated.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return ResponseEntity<Long> A ResponseEntity containing the response for the update mass layoff request.
	 * @throws ValidationException If validation of the EditMassLayOffReqDTO fails.
	 */
	@PutMapping(path = "/update", produces = "application/json")
	public ResponseEntity<Long> updateMassLayOff(
			@Valid @RequestBody @NotNull(message = "massLayOffResDTO.notnull") final EditMassLayOffReqDTO editMassLayOffReqDTO,
			HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(massLayOffService.updateMassLayOff(editMassLayOffReqDTO, request));
	}

	/**
	 * Handle the request to clone a mass layoff record.
	 *
	 * @param cloneMassLayOffReqDTO {@link CloneMassLayOffReqDTO} The CloneMassLayOffReqDTO containing the information to be cloned.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link ResponseEntity<Object>} A ResponseEntity containing the response for the clone mass layoff request.
	 * @throws ValidationException If validation of the CloneMassLayOffReqDTO fails.
	 */
	@PostMapping(path = "/clone", produces = "application/json")
	public ResponseEntity<Object> cloneMassLayOff(
			@Valid @RequestBody @NotNull(message = "massLayOffReqDTO.notnull") final CloneMassLayOffReqDTO cloneMassLayOffReqDTO,
			HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(massLayOffService.cloneMassLayOff(cloneMassLayOffReqDTO, request));
	}

	/**
	 * Handle the request to search for mass layoff reference list records based on lookup criteria.
	 *
	 * @param lookupReqDTO {@link LookupReqDTO} The LookupReqDTO containing the criteria for the lookup.
	 * @return {@link ResponseEntity} A ResponseEntity containing the response for the mass layoff reference list lookup request.
	 * @throws ValidationException If validation of the LookupReqDTO fails.
	 */
	@PostMapping(path = "/lookup", produces = "application/json")
	public ResponseEntity searchMlrl(@Valid @RequestBody final LookupReqDTO lookupReqDTO) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
				lookUpService.getSearchResult(lookupReqDTO));
	}

}