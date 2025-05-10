package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.dto.alv.ConfigAlvListReqDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvReorderReqDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvSaveReqDTO;
import com.ssi.ms.configuration.service.ConfigAlvService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
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

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

@RestController
@RequestMapping("/alv")
@Validated
@Slf4j
@CrossOrigin
public class ConfigAlvController {
    @Autowired
    private ConfigAlvService configAlvService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/search", produces = "application/json")
    public ResponseEntity getAlvsByAlc(@Valid @RequestBody final ConfigAlvListReqDTO alvReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configAlvService.getAlvsByAlc(alvReqDTO));
    }

    @GetMapping(path = "/alvId/{alvId}", produces = "application/json")
    public ResponseEntity getAlvDetails(
            @Valid
            @NotNull(message = "alvId.mandatory")
            @PathVariable("alvId") Long alvId) {
        return ResponseEntity.ok().body(configAlvService.getAlvDetails(alvId));
    }

    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity saveAlvDetails(@Valid @RequestBody final ConfigAlvSaveReqDTO alvSaveDTO,
                                         HttpServletRequest request) throws Exception {
        configAlvService.saveAlvDetails(alvSaveDTO, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/reorder", produces = "application/json")
    public ResponseEntity reorderAlvs(@Valid @RequestBody final ConfigAlvReorderReqDTO reorderAlvDTO,
                                         HttpServletRequest request) throws Exception {
        configAlvService.reorderAlvs(reorderAlvDTO, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/decipher/{alcId}", produces = "application/json")
    public ResponseEntity getAlcAlvDecipherCdList(
            @Valid
            @NotNull(message = "alcId.mandatory")
            @PathVariable("alcId") Long alcId) {
        return ResponseEntity.ok().body(configAlvService.getAlcAlvDecipherCdList(alcId));
    }
    @GetMapping(path = "/displayon", produces = "application/json")
    public ResponseEntity getDisplayOnAlvs() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configAlvService.getDisplayOnAlvs());
    }
}
