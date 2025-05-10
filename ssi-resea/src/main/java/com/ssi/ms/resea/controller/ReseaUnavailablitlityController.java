package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.dto.UnavailablityApproveReqDTO;
import com.ssi.ms.resea.dto.UnavailablityRejectReqDTO;
import com.ssi.ms.resea.dto.UnavailablityRequestReqDTO;
import com.ssi.ms.resea.dto.UnavailablitySummaryReqDTO;
import com.ssi.ms.resea.dto.UnavailablityWithdrawReqDTO;
import com.ssi.ms.resea.service.ReseaUnavailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/unavailablity")
@Validated
@Slf4j
@CrossOrigin
public class ReseaUnavailablitlityController {

    @Autowired
    private ReseaUnavailabilityService stunService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity getUnavailabilitySummary(@Valid @RequestBody final UnavailablitySummaryReqDTO stunReqDTO,
                                                HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stunService.getUnavailabilitySummary(stunReqDTO, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @GetMapping(path = "/detail/{unavailabilityId}", produces = "application/json")
    public ResponseEntity getUnavailablityDetail(@Valid @NotNull @PathVariable("unavailabilityId") Long unavailabilityId,
                                            HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(stunService.getUnavailablityDetail(unavailabilityId, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @PostMapping(path = "/request", produces = "application/json")
    public ResponseEntity requestUnavailability(@Valid @RequestBody final UnavailablityRequestReqDTO stunReqDTO,
                                       HttpServletRequest request) {
        final String response = stunService.requestUnavailability(stunReqDTO, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        return StringUtils.isNotEmpty(response) ? ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
                : ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
    }

    @PostMapping(path = "/approve/{unavailabilityId}", produces = "application/json")
    public ResponseEntity approveUnavailability(@Valid @NotNull @PathVariable("unavailabilityId") Long unavailabilityId,
                                                HttpServletRequest request) {
        final String response = stunService.approveUnavailability(unavailabilityId, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        return StringUtils.isNotEmpty(response) ? ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
                : ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
    }

    @PostMapping(path = "/reject", produces = "application/json")
    public ResponseEntity rejectUnavailability(@Valid @RequestBody final UnavailablityRejectReqDTO stunReqDTO,
                                                HttpServletRequest request) {
        final String response = stunService.rejectUnavailability(stunReqDTO, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        return StringUtils.isNotEmpty(response) ? ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
                : ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
    }

    @PostMapping(path = "/withdraw", produces = "application/json")
    public ResponseEntity withdrawUnavailability(@Valid @RequestBody final UnavailablityWithdrawReqDTO stunReqDTO,
                                               HttpServletRequest request) {
        final String response = stunService.withdrawUnavailability(stunReqDTO, getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request));
        return StringUtils.isNotEmpty(response) ? ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
                : ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
    }

    @GetMapping(path = "/check/{eventId}", produces = "application/json")
    public ResponseEntity checkStaffUnavailabilityForAvailableslot(@Valid @NotNull(message = EVENT_ID_MANDATORY)
                                  @PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(stunService.checkStaffUnavailabilityForAvailableslot(eventId));

    }
}

