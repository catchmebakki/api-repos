package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.AvaliableApptReqDTO;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.dto.CaseActivityAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupEditReqDTO;
import com.ssi.ms.resea.service.ReseaCaseSummaryService;
import com.ssi.ms.resea.service.ReseaClaimantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/case-summary")
@Validated
@Slf4j
@CrossOrigin
public class ReseaCaseSummaryController {

    @Autowired
    private ReseaCaseSummaryService caseSummaryService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/detail/{caseId}", produces = "application/json")
    public ResponseEntity getCaseSummaryDetails(@Valid @NotNull @PathVariable("caseId") final Long caseId, HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.getCaseSummaryDetails(caseId, Sort.Direction.DESC, getLoggedInRoleId.apply(request)));
    }

    @GetMapping(path = "/activity/{caseId}/{sort}", produces = "application/json")
    public ResponseEntity getActivitySummaryDetails(@Valid @NotNull @PathVariable("caseId") final Long caseId,
                                                    @Valid @NotNull @PathVariable("sort") final String sort,
                                                    HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.getCaseActivitySummary(caseId,
                        "ASC".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @PostMapping(path = "/followup/add", produces = "application/json")
    public ResponseEntity addFollowup(@Valid @RequestBody final CaseActivityFollowupAddReqDTO followupReqDTO,
                                      HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.addFollowup(followupReqDTO,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @PostMapping(path = "/followup/edit", produces = "application/json")
    public ResponseEntity editFollowup(@Valid @RequestBody final CaseActivityFollowupEditReqDTO followupReqDTO,
                                      HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.editFollowup(followupReqDTO,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @GetMapping(path = "/activity/view/{activityId}", produces = "application/json")
    public ResponseEntity viewCaseActivity(@Valid @NotNull @PathVariable("activityId") final Long activityId,
                                           HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.viewCaseActivity(activityId,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @PostMapping(path = "/activity/create", produces = "application/json")
    public ResponseEntity addCaseActivity(@Valid @RequestBody final CaseActivityAddReqDTO addActivityReqDTO,
                                       HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.addCaseActivity(addActivityReqDTO,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @GetMapping(path = "/activity/standard-text", produces = "application/json")
    public ResponseEntity activityStandardText() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                caseSummaryService.activityStandardText());
    }
}

