package com.ssi.ms.resea.controller;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.resea.service.ReseaParameterService;
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
@RequestMapping("/parameter")
@Validated
@Slf4j
@CrossOrigin
public class ReseaParameterController {

    @Autowired
    private ReseaParameterService parService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/resea-parameters", produces = "application/json")
    public ResponseEntity getAppointmentBufferTimes() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                parService.getAppointmentBufferTimes());
    }
}

