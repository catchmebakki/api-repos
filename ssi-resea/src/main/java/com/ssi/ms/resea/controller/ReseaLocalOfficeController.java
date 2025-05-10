package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.service.ReseaLocalOfficeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/office")
@Validated
@Slf4j
@CrossOrigin
public class ReseaLocalOfficeController {

    @Autowired
    private ReseaLocalOfficeService lofService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/case/{caseId}", produces = "application/json")
    public ResponseEntity getCaseLocalOfficeName(
            @Valid @NotNull @PathVariable("caseId") Long caseId) {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(lofService.getCaseLocalOfficeName(caseId));
    }

    @GetMapping(path = "/lookup/offices", produces = "application/json")
    public ResponseEntity getLookupLocalOffices() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(lofService.getLookupLocalOffices());
    }

    @GetMapping(path = "/claim/{clmId}", produces = "application/json")
    public ResponseEntity getLofNameByClmId(
            @Valid @NotNull @PathVariable("clmId") Long clmId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(lofService.getLofNameByClmId(clmId));
    }

    @GetMapping(path = "/case-manager/{userId}", produces = "application/json")
    public ResponseEntity getStaffLocalOffice(
            @Valid @NotNull @PathVariable("userId") Long userId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(lofService.getStaffLocalOffice(userId));
    }
}

