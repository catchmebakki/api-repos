package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.AppointmentReqDTO;
import com.ssi.ms.resea.service.ReseaInterviewService;
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
import javax.validation.constraints.NotNull;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;

@RestController
@RequestMapping("/interview")
@Validated
@Slf4j
@CrossOrigin
public class ReseaInterviewController {

    @Autowired
    private ReseaInterviewService intvwService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity submitInterviewDetails(
            @Valid @RequestBody final AppointmentReqDTO apptDTO, HttpServletRequest request) {
        final String response = intvwService.submitInterviewDetails(apptDTO, getLoggedInStaffId.apply(request),
                getLoggedInRoleId.apply(request));
        if (response != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @GetMapping(path = "/details/{eventId}", produces = "application/json")
    public ResponseEntity loadInterviewDetails(
            @Valid @NotNull(message = EVENT_ID_MANDATORY) @PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(intvwService.loadInterviewDetails(eventId));
    }
}

