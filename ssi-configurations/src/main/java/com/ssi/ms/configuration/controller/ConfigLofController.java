package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.service.ConfigLofService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/businessunit")
@Validated
@Slf4j
@CrossOrigin
public class ConfigLofController {
    @Autowired
    private ConfigLofService configlofService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/list", produces = "application/json")
    public ResponseEntity getAllBusinessUnits() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configlofService.getAllBusinessUnits());
    }
}

