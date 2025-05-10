package com.ssi.ms.collecticase.controller;

import com.ssi.ms.collecticase.service.CollecticaseParameterService;
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
@RequestMapping("/parameter")
@Validated
@Slf4j
@CrossOrigin
public class CollecticaseParameterController {

    @Autowired
    private CollecticaseParameterService parService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @GetMapping(path = "/collecticase-parameters", produces = "application/json")
    public ResponseEntity getParametersValues() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                parService.getParametersValues());
    }
}


