package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.dto.alc.ConfigAlcListReqDTO;
import com.ssi.ms.configuration.service.ConfigAlcService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/alc")
@Validated
@Slf4j
@CrossOrigin
public class ConfigAlcController {
    @Autowired
    private ConfigAlcService configAlcService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/details", produces = "application/json")
    public ResponseEntity getAlcs(@Valid @RequestBody final ConfigAlcListReqDTO alcReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configAlcService.getAlcsByCategory(alcReqDTO));
    }

    @GetMapping(path = "/details/{alcCategoryCd}", produces = "application/json")
    public ResponseEntity getAlcs(
            @Valid
            @NotNull(message = "alcCategoryCd.mandatory")
            @PathVariable("alcCategoryCd") Long alcCategoryCd
    ) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configAlcService.getAlcsByCategory(alcCategoryCd));
    }
}

