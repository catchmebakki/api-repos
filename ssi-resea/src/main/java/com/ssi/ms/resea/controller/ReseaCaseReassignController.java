package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.ReseaCaseReassignReqDTO;
import com.ssi.ms.resea.dto.ReseaReassignAllReqDTO;
import com.ssi.ms.resea.service.ReseaCaseReassignService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

@RestController
@RequestMapping("/reassign")
@Validated
@Slf4j
@CrossOrigin
public class ReseaCaseReassignController {

    @Autowired
    private ReseaCaseReassignService intvwService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/case", produces = "application/json")
    public ResponseEntity submitCaseReassign(
            @Valid @RequestBody final ReseaCaseReassignReqDTO reassignDTO, HttpServletRequest request) {
        final String response = intvwService.submitCaseReassignment(reassignDTO,
                getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        if (response != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/all", produces = "application/json")
    public ResponseEntity submitReassignAll(
            @Valid @RequestBody final ReseaReassignAllReqDTO reassignDTO, HttpServletRequest request) {
        final String response = intvwService.submitAllReassignment(reassignDTO,
                getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        if (response != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }
}

