package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.dto.wscc.ConfigWsccChildListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccSaveReqDTO;
import com.ssi.ms.configuration.service.ConfigWsccService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

@RestController
@RequestMapping("/wscc")
@Validated
@Slf4j
@CrossOrigin
public class ConfigWsccController {
    @Autowired
    private ConfigWsccService configWsccService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/search", produces = "application/json")
    public ResponseEntity getWeeklyWorkSearchReqSummary(
            @Valid
            @RequestBody final ConfigWsccListReqDTO wsccReqDTO) {
        return ResponseEntity.ok().body(configWsccService.getWorkSearchReqSummary(wsccReqDTO));
    }

    @GetMapping(path = "/wsccId/{wsccId}", produces = "application/json")
    public ResponseEntity getWeeklyWorkSearchReqDetails(
            @Valid @NotNull(message = "wsccId.mandatory")
            @PathVariable("wsccId") Long wsccId) {
        return ResponseEntity.ok().body(configWsccService.getWorkSearchReqDetails(wsccId));
    }

    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity saveWeeklyWorkSearchReqDetails(
            @Valid @RequestBody final ConfigWsccSaveReqDTO wsccDTO,
            HttpServletRequest request) {
        configWsccService.saveWorkSearchReqDetails(wsccDTO, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/childlist", produces = "application/json")
    public ResponseEntity getWsccChildList(
            @Valid
            @RequestBody final ConfigWsccChildListReqDTO wsccReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configWsccService.getChildWsccById(wsccReqDTO));
    }

    @DeleteMapping(path = "/delete/{wsccId}", produces = "application/json")
    public ResponseEntity deleteWscc(@Valid
                                          @NotNull(message = "wsccId.mandatory")
                                          @PathVariable("wsccId") Long wsccId,
                                          HttpServletRequest request) {
        configWsccService.deleteWscc(wsccId, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }
}

