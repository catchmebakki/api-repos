package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.service.ConfigNmiService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/nmi")
@Validated
@Slf4j
@CrossOrigin
public class ConfigNmiController {
    @Autowired
    private ConfigNmiService configNmiService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/list/{nmiId}", produces = "application/json")
    public ResponseEntity getChildNmiList(@Valid
                           @PathVariable("nmiId") Long nmiId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configNmiService.getChildNmiList(nmiId));
    }
}
