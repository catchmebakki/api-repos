package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.dto.AvaliableApptReqDTO;
import com.ssi.ms.resea.service.ReseaClaimantService;
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
@RequestMapping("/claimant")
@Validated
@Slf4j
@CrossOrigin
public class ReseaClaimantController {

    @Autowired
    private ReseaClaimantService cmtService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/available-list", produces = "application/json")
    public ResponseEntity getAvailableClaimants(@Valid @RequestBody final AvaliableApptReqDTO apptReqDTO,
                                                HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                cmtService.getAvailableClaimants(apptReqDTO, getLoggedInStaffId.apply(request),
                        getLoggedInRoleId.apply(request)));
    }
}

