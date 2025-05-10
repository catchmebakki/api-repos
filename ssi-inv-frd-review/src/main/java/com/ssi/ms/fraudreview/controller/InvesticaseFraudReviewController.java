package com.ssi.ms.fraudreview.controller;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.fraudreview.dto.IdTheftHijackReqDTO;
import com.ssi.ms.fraudreview.dto.caseassign.CaseAssignReqDTO;
import com.ssi.ms.fraudreview.dto.lookup.LookupReqDTO;
import com.ssi.ms.fraudreview.service.FraudReviewService;
import com.ssi.ms.fraudreview.service.LookupService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Validated
@Slf4j
@CrossOrigin
public class InvesticaseFraudReviewController {
	@Autowired
	private LookupService lookupService;

	@Autowired
	private FraudReviewService fraudReviewService;

	@PostMapping(path = "/lookup", produces = "application/json")
	public ResponseEntity lookupSourceClaims(@Valid @RequestBody final LookupReqDTO lookupReqDTO, HttpServletRequest request) throws Exception {
		final String userId = getLoggedInStaffId.apply(request);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
				lookupService.getSearchResult(lookupReqDTO, userId));
	}

	@GetMapping(path = "/getBPCStaffList", produces = "application/json")
	public ResponseEntity getBPCStaffList(HttpServletRequest request) throws Exception {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
				fraudReviewService.getBPCStaffList(getLoggedInStaffId.apply(request)));
	}

	@PostMapping(path = "/caseassign", produces = "application/json")
	public ResponseEntity caseAssign(@Valid @RequestBody final CaseAssignReqDTO caseAssignReqDTO,
			HttpServletRequest request) throws Exception {
		fraudReviewService.assignCases(caseAssignReqDTO, getLoggedInStaffId.apply(request));
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/markAsIdTheft", produces = "application/json")
	public ResponseEntity markAsIdTheft(@Valid @RequestBody final IdTheftHijackReqDTO idTheftHijackReqDTO,
			HttpServletRequest request) {
		fraudReviewService.markAsIdTheft(idTheftHijackReqDTO,  getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
	}
}
