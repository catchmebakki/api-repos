package com.ssi.ms.collecticase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.dto.*;
import com.ssi.ms.collecticase.inputpayload.CcaseInputPayload;
import com.ssi.ms.collecticase.outputpayload.PaginationResponse;
import com.ssi.ms.collecticase.service.AlvService;
import com.ssi.ms.collecticase.service.CaseService;
import com.ssi.ms.collecticase.util.CollecticaseHelper;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/case")
@Validated
@Slf4j
@CrossOrigin
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private AlvService alvService;

    @Autowired
    private GeneralActivityValidator generalActivityValidator;

    @GetMapping(path = "/caseheader/{caseId}", produces = "application/json")
    public ResponseEntity<List<VwCcaseHeaderDTO>> getCaseHeaderInfoByCaseId(@Valid @PathVariable("caseId") Long caseId)
            throws JsonProcessingException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseHeaderInfoByCaseId(caseId));
    }

    @GetMapping(path = "/claimantoverpayment/{caseId}", produces = "application/json")
    public ResponseEntity<List<VwCcaseOpmDTO>> getClaimantOpmInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getClaimantOpmInfoByCaseId(caseId));
    }

    @GetMapping(path = "/caseremedy/{caseId}", produces = "application/json")
    public ResponseEntity<List<VwCcaseRemedyDTO>> getCaseRemedyInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseRemedyInfoByCaseId(caseId));
    }

    @GetMapping(path = "/caseentity/{caseId}", produces = "application/json")
    public ResponseEntity<List<VwCcaseHeaderEntityDTO>> getCaseEntityInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseEntityInfoByCaseId(caseId));
    }

    @GetMapping(path = "/casenotes/{caseId}", produces = "application/json")
    public ResponseEntity<List<CcaseActivitiesCmaDTO>> getCaseNotesByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseNotesByCaseId(caseId));
    }

    @GetMapping(path = "/caseload", produces = "application/json")
    public ResponseEntity<PaginationResponse<VwCcaseCaseloadDTO>> getCaseLoadByStaffId(@ModelAttribute CcaseInputPayload
                                                                                     ccaseInputPayload) {
        Long staffId = ccaseInputPayload.getStaffId();
        Map<String, Object> pageInputs = CollecticaseHelper.getPageInputs(ccaseInputPayload);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseLoadByStaffId(staffId, (Integer) pageInputs.get("pageNo"),
                        (Integer) pageInputs.get("pageSize"), "caseNo",
                        (Boolean) pageInputs.get("pageAscendingEnable")));
    }

    @GetMapping(path = "/getActivitiesDataByCaseId", produces = "application/json")
    public ResponseEntity<PaginationResponse<ActivitiesSummaryDTO>>
                                        getActivitiesDataByCaseId(@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long caseId = ccaseInputPayload.getCaseId();
        Map<String, Object> pageInputs = CollecticaseHelper.getPageInputs(ccaseInputPayload);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getActivitiesDataByCaseId(caseId, (Integer) pageInputs.get("pageNo"),
                        (Integer) pageInputs.get("pageSize"), "cmaId",
                        (Boolean) pageInputs.get("pageAscendingEnable")));
    }

    @PostMapping(path = "/create-case", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createCollecticaseCase(
            @Valid @RequestBody final CreateCaseDTO createCaseDTO, HttpServletRequest request) {
        final Map<String, Object> createCollecticaseActivity = caseService
                                                                .createCollecticaseCase(createCaseDTO);
        if (createCollecticaseActivity != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_CMC_ID) != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createCollecticaseActivity);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/createCollecticaseActivity", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createCollecticaseCase(
            @Valid @RequestBody final CreateActivityDTO createActivityDTO, HttpServletRequest request) {

        final Map<String, Object> createCollecticaseActivity = caseService.createCollecticaseActivity(createActivityDTO);

        if (createCollecticaseActivity != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID) != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createCollecticaseActivity);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @GetMapping(path = "/getCaseRemedyActivity/{caseId}", produces = "application/json")
    public ResponseEntity<List<AllowValAlvResDTO>> getCaseRemedyActivity(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                alvService.getAlvsByAlvIds(caseService.getRemedyActivityByCaseRemedyId
                        (caseService.getCaseRemedyActivityByCaseId(caseId, CollecticaseConstants.INDICATOR.Y.name()),
                                CollecticaseConstants.INDICATOR.Y.name())));
    }

    @GetMapping(path = "/getCaseActivityByRemedyType/{caseId}/{remedyTypeCd}", produces = "application/json")
    public ResponseEntity<List<AllowValAlvResDTO>> getCaseActivityByRemedyType(@Valid @PathVariable("caseId") Long caseId,
                                                      @Valid @PathVariable("remedyTypeCd") Long remedyTypeCd) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                alvService.getAlvsByAlvIds(caseService.getCaseActivityByRemedyType
                        (caseService.getCaseRemedyActivityByCaseId(caseId, CollecticaseConstants.INDICATOR.Y.name()),
                                CollecticaseConstants.INDICATOR.Y.name(), remedyTypeCd)));
    }

    @GetMapping(path = "/reassign-case/{caseId}", produces = "application/json")
    public ResponseEntity<List<CaseReassignDTO>> getCaseReassignInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseReassignInfoByCaseId(caseId));
    }

    @GetMapping(path = "/collection-staffs", produces = "application/json")
    public ResponseEntity<List<StaffDTO>> getStaffListByLofAndRole() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getStaffListByLofAndRole());
    }

    @GetMapping(path = "/reassign-caseto/{staffId}", produces = "application/json")
    public ResponseEntity<List<StaffDTO>> getReassignStaffListByLofAndRole(@Valid @PathVariable("staffId") Long staffId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getReassignStaffListByLofAndRole(staffId));
    }

    @GetMapping(path = "/caseload-summary/{staffId}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getCaseLoadSummary(@Valid @PathVariable("staffId") Long staffId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseLoadSummary(staffId));
    }

    @GetMapping(path = "/cl-optype", produces = "application/json")
    public ResponseEntity<Map<String, String>> getCLOverPaymentType() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCLOverPaymentType());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/cl-opvalue", produces = "application/json")
    public ResponseEntity<Map<String, String>> getCLOverPaymentValue() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCLOverPaymentValue());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/cl-followupvalue", produces = "application/json")
    public ResponseEntity<Map<String, String>> getCLFollowUpValue() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCLFollowUpValue());
    }

    @PostMapping(path = "/caselookup", produces = "application/json")
    public ResponseEntity caseLookup(
            @Valid @RequestBody final CaseLookupDTO caseLookupDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.searchCaseLookup(caseLookupDTO));
    }

    @PostMapping(path = "/reassign-case", produces = "application/json")
    public ResponseEntity reassignCase(@Valid @RequestBody final ReassignDTO reassignDTO, BindingResult result) {
        if (!result.hasErrors()) {
            caseService.reassignCase(reassignDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(reassignDTO);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/validate-ssn/{encryptSSN}", produces = "application/json")
    public ResponseEntity validateSSN(@Valid @PathVariable("ssn") String encryptSSN) {
        // Bak TODO Need to think and write Encryption for ssn, which get from UI - refer CollecticaseHelper
        // TODO encrypt and decrypt method
        String ssn = encryptSSN;
        Long claimantId = caseService.validateSSN(ssn);
        if (claimantId != null)
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(claimantId);
        else
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
    }

}