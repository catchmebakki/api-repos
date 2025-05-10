package com.ssi.ms.controller;

import com.ssi.ms.database.MasterDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("mock")
@Validated
@Slf4j
public class WireMockController {

    @Autowired
    private MasterDataManager masterDataManager;

    // @CircuitBreaker(name="profileService", fallbackMethod = "fallbackFunction")
    @GetMapping(path = "/contact", produces = "application/json")
    public ResponseEntity<String> getAllVlue() {
        masterDataManager.getAllAlvValue(1L);
        return ResponseEntity.ok("Success");
    }


}
