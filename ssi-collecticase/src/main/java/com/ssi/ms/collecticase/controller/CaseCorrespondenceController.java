package com.ssi.ms.collecticase.controller;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.dto.CorrespondenceDTO;
import com.ssi.ms.collecticase.dto.CreateCorrespondenceDTO;
import com.ssi.ms.collecticase.service.CaseCorrespondenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/casecorrespondence")
@Validated
@Slf4j
@CrossOrigin
public class CaseCorrespondenceController {

    @Autowired
    private CaseCorrespondenceService caseCorrespondenceService;

    @PostMapping(path = "/createCorrespondence", produces = "application/json")
    public ResponseEntity createCorrespondence(
            @Valid @RequestBody final CreateCorrespondenceDTO createCorrespondenceDTO, HttpServletRequest request) {

        final Map<String, Object> createCorrespondence = caseCorrespondenceService
                .createCorrespondence(createCorrespondenceDTO);

        if (createCorrespondence != null
                && createCorrespondence.get(CollecticaseConstants.POUT_WLP_O720_COR_ID) != null
                && createCorrespondence.get(CollecticaseConstants.POUT_WLP_O720_RETURN_CD) != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createCorrespondence);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @GetMapping(path = "/case-correspondence/{caseId}", produces = "application/json")
    public ResponseEntity<List<CorrespondenceDTO>> getCaseCorrespondenceByCaseId(@Valid @PathVariable("caseId")
                                                                                 Long caseId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseCorrespondenceService.getCaseCorrespondenceByCaseId(caseId,
                        List.of(CollecticaseConstants.COR_STATUS_CD_PROCESSED)));
    }
}