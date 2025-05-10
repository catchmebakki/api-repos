package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.service.ReseaStaffService;
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
@RequestMapping("/staff")
@Validated
@Slf4j
@CrossOrigin
public class ReseaStaffController {

    @Autowired
    private ReseaStaffService stfService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/case-manager", produces = "application/json")
    public ResponseEntity getReseaCaseManagerList() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stfService.getReseaCaseManagerList());
    }

    @GetMapping(path = "/event-other-case-managers/{eventId}", produces = "application/json")
    public ResponseEntity getReseaCaseManagerList(@Valid @NotNull @PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stfService.getReseaCaseManagerList(eventId));
    }

    @GetMapping(path = "/reassign-list/{caseId}/{caseOffice}", produces = "application/json")
    public ResponseEntity getReassignCaseManagers(@Valid @NotNull @PathVariable("caseId") Long caseId,
                                                  @Valid @NotNull @PathVariable("caseOffice") String caseOffice) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stfService.getReassignCaseManagers(caseId, caseOffice));
    }

    @GetMapping(path = "/appt-case-manager", produces = "application/json")
    public ResponseEntity getAllApptReseaCaseManagerList() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stfService.getAllApptReseaCaseManagerList());
    }

}

