package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.DashboardCalReqDTO;
import com.ssi.ms.resea.dto.ReseaCaseLoadViewReqDTO;
import com.ssi.ms.resea.service.ReseaDashboardService;
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
import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@RestController
@RequestMapping("/dashboard")
@Validated
@Slf4j
@CrossOrigin
public class ReseaDashboardController {

    @Autowired
    private ReseaDashboardService dashboardService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/staff-calendar", produces = "application/json")
    public ResponseEntity getStaffDashboardCalendar(@Valid @RequestBody final DashboardCalReqDTO calDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                dashboardService.getStaffDashboardCalendar(calDTO));
    }

    @GetMapping(path = "/caseload-metrics/{userId}", produces = "application/json")
    public ResponseEntity getCaseLoadMetrics(@Valid @NotNull(message = USR_ID_MANDATORY)
                                                 @PathVariable("userId") Long userId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                dashboardService.getCaseLoadMetrics(userId));
    }

    @PostMapping(path = "/caseload-summary", produces = "application/json")
    public ResponseEntity getCaseLoadSummary(@Valid @RequestBody final ReseaCaseLoadViewReqDTO caseLoadViewReqDTO,
                                             HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                dashboardService.getCaseLoadSummary(caseLoadViewReqDTO, getLoggedInRoleId.apply(request)));
    }
}

