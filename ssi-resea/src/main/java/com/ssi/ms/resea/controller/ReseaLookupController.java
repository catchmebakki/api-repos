package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.repository.ReseaPreStepsRspsRepository;
import com.ssi.ms.resea.dto.LookupCaseSummaryReqDTO;
import com.ssi.ms.resea.dto.LookupSummaryReqDTO;
import com.ssi.ms.resea.service.ReseaLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lookup")
@Validated
@Slf4j
@CrossOrigin
public class ReseaLookupController {

    @Autowired
    private ReseaLookupService lookupService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @Autowired
    private ReseaPreStepsRspsRepository rspsRepository;

    @PostMapping(path = "/appointments-summary", produces = "application/json")
    public ResponseEntity getApptLookupSummary(@Valid @RequestBody final LookupSummaryReqDTO lookupReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                lookupService.getApptLookupSummary(lookupReqDTO));
    }

    @PostMapping(path = "/case-summary", produces = "application/json")
    public ResponseEntity getCaseLookupSummary(@Valid @RequestBody final LookupCaseSummaryReqDTO lookupReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                lookupService.getCaseLookupSummary(lookupReqDTO));
    }


    @PostMapping(path = "/case-summary/pending-schedule", produces = "application/json")
    public ResponseEntity getPendingCaseLookupSummary(@Valid @RequestBody final LookupCaseSummaryReqDTO pendingLookupReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                lookupService.getInitApptPendingSummary(pendingLookupReqDTO));
    }
}

