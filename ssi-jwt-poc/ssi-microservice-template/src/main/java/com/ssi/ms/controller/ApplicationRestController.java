package com.ssi.ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.database.service.MasterDataManagerService;

import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("master-data")
@Validated
@Slf4j
public class ApplicationRestController {
    @Autowired
    private MasterDataManagerService masterDataManagerService;

    // @CircuitBreaker(name="profileService", fallbackMethod = "fallbackFunction")
    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<String> getAllValue() {
     masterDataManagerService.getAllAlvValue(1L);
        return ResponseEntity.ok("Success");
    }


}
