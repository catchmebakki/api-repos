package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.dto.wswc.ConfigWswcChildListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcSaveReqDTO;
import com.ssi.ms.configuration.service.ConfigWswcService;
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
@RequestMapping("/wswc")
@Validated
@Slf4j
@CrossOrigin
public class ConfigWswcController {
    @Autowired
    private ConfigWswcService wswcService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/search", produces = "application/json")
    public ResponseEntity getWeeklyWorkSearchReqSummary(
            @Valid
            @RequestBody final ConfigWswcListReqDTO wswcReqDTO) {
        return ResponseEntity.ok().body(wswcService.getWorkSearchWaiverSummary(wswcReqDTO));
    }

    @GetMapping(path = "/wswcId/{wswcId}", produces = "application/json")
    public ResponseEntity getWeeklyWorkSearchWaiverDetails(
            @Valid @NotNull(message = "wswcId.mandatory")
            @PathVariable("wswcId") Long wswcId) {
        return ResponseEntity.ok().body(wswcService.getWorkSearchWaiverDetails(wswcId));
    }

    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity saveWeeklyWorkSearchWaiverDetails(
            @Valid @RequestBody final ConfigWswcSaveReqDTO wswcDTO,
            HttpServletRequest request) {
        wswcService.saveWorkSearchWaiverDetails(wswcDTO, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/childlist", produces = "application/json")
    public ResponseEntity getWswcChildList(
            @Valid
            @RequestBody final ConfigWswcChildListReqDTO wswcReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                wswcService.getChildWswcById(wswcReqDTO));
    }

    @DeleteMapping(path = "/delete/{wswcId}", produces = "application/json")
    public ResponseEntity deleteWscc(@Valid
                                     @NotNull(message = "wswcId.mandatory")
                                     @PathVariable("wswcId") Long wswcId,
                                     HttpServletRequest request) {
        wswcService.deleteWswc(wswcId, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }
}

