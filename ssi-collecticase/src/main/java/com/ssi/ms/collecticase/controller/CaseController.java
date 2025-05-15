package com.ssi.ms.collecticase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.dto.*;
import com.ssi.ms.collecticase.service.AlvService;
import com.ssi.ms.collecticase.service.CaseService;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

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
    public ResponseEntity getCaseHeaderInfoByCaseId(@Valid @PathVariable("caseId") Long caseId)
            throws JsonProcessingException {

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        CreateActivityDTO createActivityDTO = new CreateActivityDTO();

        createActivityDTO.setCaseId(71046L);
        createActivityDTO.setEmployerId(null);
        createActivityDTO.setActivityTypeCd(3866L);
        createActivityDTO.setRemedyTypeCd(3977L);
        createActivityDTO.setActivityDt(new Date());
        createActivityDTO.setActivityTime("12:00 PM");
        createActivityDTO.setActivitySpecifics("Case Created");
        createActivityDTO.setActivityNotes("Case Created");
        createActivityDTO.setActivityNotesAdditional(null);
        createActivityDTO.setActivityNotesNHUIS(null);
        createActivityDTO.setCommunicationMethod(3601L);
        createActivityDTO.setCaseCharacteristics("Case created based on user request");
        createActivityDTO.setActivityCmtRepCd(3933L);
        createActivityDTO.setActivityCasePriority(3926L);
        createActivityDTO.setFollowupDt(new Date());
        createActivityDTO.setFollowupShortNote("Short Note");
        createActivityDTO.setFollowupCompleteShortNote("Short Note Complete");
        createActivityDTO.setCallingUser("Post Man");
        createActivityDTO.setUsingProgramName("Test");

        // Convert the DTO to JSON string
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createActivityDTO);

        System.out.println(jsonString);

        CreateCorrespondenceDTO createCorrespondenceDTO = new CreateCorrespondenceDTO();
        createCorrespondenceDTO.setReportId(660);
        createCorrespondenceDTO.setClaimId(null);
        createCorrespondenceDTO.setEmployerId(null);
        createCorrespondenceDTO.setClaimantId(2001);
        createCorrespondenceDTO.setCorCoeInd(CollecticaseConstants.INDICATOR.Y.name());
        createCorrespondenceDTO.setCorForcedInd(CollecticaseConstants.INDICATOR.N.name());
        createCorrespondenceDTO.setCorStatusCd(1424);
        createCorrespondenceDTO.setCorDecId(null);
        createCorrespondenceDTO.setCorReceipientIfk(2001);
        createCorrespondenceDTO.setCorReceipientCd(1773);
        //createCorrespondenceDTO.setCorTimeStamp(LocalDateTime.now());
        createCorrespondenceDTO.setCorCoeString("OP Balance");

        // Convert the DTO to JSON string
        String jsonString1 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createCorrespondenceDTO);

        System.out.println(jsonString1);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseHeaderInfoByCaseId(caseId));
    }

    @GetMapping(path = "/claimantoverpayment/{caseId}", produces = "application/json")
    public ResponseEntity getClaimantOpmInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getClaimantOpmInfoByCaseId(caseId));
    }

    @GetMapping(path = "/caseremedy/{caseId}", produces = "application/json")
    public ResponseEntity getCaseRemedyInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseRemedyInfoByCaseId(caseId));
    }

    @GetMapping(path = "/caseentity/{caseId}", produces = "application/json")
    public ResponseEntity getCaseEntityInfoByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseEntityInfoByCaseId(caseId));
    }

    @GetMapping(path = "/casenotes/{caseId}", produces = "application/json")
    public ResponseEntity getCaseNotesByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseNotesByCaseId(caseId));
    }

    @GetMapping(path = "/caseload/{staffId}", produces = "application/json")
    public ResponseEntity getCaseLoadByStaffId(@Valid @PathVariable("staffId") Long staffId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getCaseLoadByStaffId(staffId, 0, 9, "caseNo", true));
    }

    @GetMapping(path = "/getActivitiesDataByCaseId/{caseId}", produces = "application/json")
    public ResponseEntity getActivitiesDataByCaseId(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseService.getActivitiesDataByCaseId(caseId, 1, 2, "cmaId", true));
    }

    @PostMapping(path = "/createCollecticaseCase", produces = "application/json")
    public ResponseEntity createCollecticaseCase(
            @Valid @RequestBody final CreateCaseDTO createCaseDTO, HttpServletRequest request) {
        final Map<String, Object> createCollecticaseActivity = caseService.createCollecticaseCase(createCaseDTO.getClaimantId(),
                createCaseDTO.getStaffId(), createCaseDTO.getCasePriority(),
                createCaseDTO.getCaseRemedyCd(), createCaseDTO.getCaseActivityCd(),
                createCaseDTO.getCallingUser(), createCaseDTO.getUsingProgramName());
        if (createCollecticaseActivity != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_CMC_ID) != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createCollecticaseActivity);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/createCollecticaseActivity", produces = "application/json")
    public ResponseEntity createCollecticaseCase(
            @Valid @RequestBody final CreateActivityDTO createActivityDTO, HttpServletRequest request) {

        final Map<String, Object> createCollecticaseActivity = caseService.createCollecticaseActivity(
                createActivityDTO.getCaseId(), createActivityDTO.getEmployerId(), createActivityDTO.getActivityTypeCd(),
                createActivityDTO.getRemedyTypeCd(), createActivityDTO.getActivityDt(), createActivityDTO.getActivityTime(),
                createActivityDTO.getActivitySpecifics(), createActivityDTO.getActivityNotes(),
                createActivityDTO.getActivityNotesAdditional(), createActivityDTO.getActivityNotesNHUIS(),
                createActivityDTO.getCommunicationMethod(), createActivityDTO.getCaseCharacteristics(),
                createActivityDTO.getActivityCmtRepCd(), createActivityDTO.getActivityCasePriority(),
                createActivityDTO.getFollowupDt(), createActivityDTO.getFollowupShortNote(),
                createActivityDTO.getFollowupCompleteShortNote(), createActivityDTO.getCallingUser(),
                createActivityDTO.getUsingProgramName());

        if (createCollecticaseActivity != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null
                && createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID) != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createCollecticaseActivity);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/addGeneralActivity", produces = "application/json")
    public ResponseEntity addGeneralActivity(
            @Valid @RequestBody final GeneralActivityDTO generalActivityDTO, HttpServletRequest request,
            BindingResult result) {
        if(!result.hasErrors())
        {
            caseService.createGeneralActivity(generalActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(generalActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/addUpdateContactActivity", produces = "application/json")
    public ResponseEntity addUpdateContactActivity(
            @Valid @RequestBody final UpdateContactActivityDTO updateContactActivityDTO, HttpServletRequest request,
            BindingResult result) {
        if(!result.hasErrors())
        {
            caseService.createGeneralActivity(updateContactActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updateContactActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @GetMapping(path = "/getCaseRemedyActivity/{caseId}", produces = "application/json")
    public ResponseEntity getCaseRemedyActivity(@Valid @PathVariable("caseId") Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                alvService.getAlvsByAlvIds(caseService.getRemedyActivityByCaseRemedyId
                        (caseService.getCaseRemedyActivityByCaseId(caseId, CollecticaseConstants.INDICATOR.Y.name()),
                                CollecticaseConstants.INDICATOR.Y.name())));
    }

    @GetMapping(path = "/getCaseActivityByRemedyType/{caseId}/{remedyTypeCd}", produces = "application/json")
    public ResponseEntity getCaseActivityByRemedyType(@Valid @PathVariable("caseId") Long caseId, @Valid @PathVariable("remedyTypeCd") Long remedyTypeCd) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                alvService.getAlvsByAlvIds(caseService.getCaseActivityByRemedyType
                        (caseService.getCaseRemedyActivityByCaseId(caseId, CollecticaseConstants.INDICATOR.Y.name()),
                                CollecticaseConstants.INDICATOR.Y.name(), remedyTypeCd)));
    }

}