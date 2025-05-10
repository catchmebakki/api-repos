package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.dto.ReseaSchApptCaseMgrAvailListReqDTO;
import com.ssi.ms.resea.dto.ScheduleInitialApptSaveReqDTO;
import com.ssi.ms.resea.dto.WaitlistClearReqDTO;
import com.ssi.ms.resea.dto.WaitlistReqDTO;
import com.ssi.ms.resea.service.ReseaAppointmentService;
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
@RequestMapping("/appointment")
@Validated
@Slf4j
@CrossOrigin
public class ReseaAppointmentController {

    @Autowired
    private ReseaAppointmentService apptService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/available/save", produces = "application/json")
    public ResponseEntity saveAvailableAppt(@Valid @RequestBody final AvaliableApptSaveReqDTO apptReqDTO,
                                       HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                apptService.saveAvailableAppt(apptReqDTO, getLoggedInStaffId.apply(request)));
    }

    @PostMapping(path = "/scheduleAppt/getCaseMgrAvailList", produces = "application/json")
    public ResponseEntity getCaseMgrsAvailListForSchAppt(@Valid @RequestBody
                                                         final ReseaSchApptCaseMgrAvailListReqDTO reseaSchApptCaseMgrAvailListReqDTO,
                                                         HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                apptService.getCaseMgrsAvailListForSchAppt(reseaSchApptCaseMgrAvailListReqDTO));
    }

    @PostMapping(path = "/scheduleAppt/save", produces = "application/json")
    public ResponseEntity saveScheduleInitialAppt(@Valid @RequestBody final ScheduleInitialApptSaveReqDTO schInitialApptSaveReqDTO,
                                            HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                apptService.saveScheduleInitialAppt(schInitialApptSaveReqDTO, getLoggedInStaffId.apply(request)));
    }

    @PostMapping(path = "/waitlist", produces = "application/json")
    public ResponseEntity applyWaitlist(@Valid @RequestBody final WaitlistReqDTO waitlistReqDTO,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                apptService.applyWaitlist(waitlistReqDTO,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }

    @PostMapping(path = "/waitlist-clear", produces = "application/json")
    public ResponseEntity clearWaitlist(@Valid @RequestBody final WaitlistClearReqDTO waitlistReqDTO,
                                        HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                apptService.clearWaitlist(waitlistReqDTO,
                        getLoggedInStaffId.apply(request), getLoggedInRoleId.apply(request)));
    }
}

