package com.ssi.ms.controller;

import com.ssi.ms.dto.Profile;
import com.ssi.ms.handler.ProfileHandler;
import com.ssi.service.core.platform.exception.SSIExceptionManager;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("profile")
@Validated
@Slf4j
public class DomainController {

private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);
    @Autowired
    private ProfileHandler profileHandler;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @CircuitBreaker(name = "profileService", fallbackMethod = "fallbackFunction")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Profile> getProfiles(
            @Min(value = 0, message = "Id cannot be in Negative") @NotNull @PathVariable final long id) {
        // final int random = 5;
        Profile profileToReturn = null; // profileHandler.getProfileForId(id);
       /* if (id > random) {
            throw new NullPointerException();
        }*/
        return ResponseEntity.ok(profileToReturn);
    }

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity addProfiles(@Valid @RequestBody final Profile profile) {
        return Optional.of(profile)
                .map(profileDto -> profileHandler.addProfile(profile))
                .map(profileId -> ResponseEntity
                        .ok()
                        .body(Map.of("id", profileId)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping(path = "/", produces = "application/json")
    public ResponseEntity updateProfiles(@RequestBody final Profile profile) {
        return (profileHandler.saveProfile(profile) ? ResponseEntity.status(HttpStatus.NO_CONTENT)
                : ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity deleteProfiles(
            @Min(value = 0, message = "Id cannot be in Negative") @NotNull @PathVariable final long id) {
        return (profileHandler.deleteProfile(id) ? ResponseEntity.status(HttpStatus.NO_CONTENT)
                : ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
    }

    public ResponseEntity<String> fallbackFunction(final Exception ex) throws Exception {
        ResponseEntity<String> toReturn = null;
        if (ex instanceof CallNotPermittedException) {
            toReturn = ssiExceptionManager
                    .onError(LOGGER, HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), "Circuit Opened", ex)
                    .toJsonResponseEntity();
        } else {
            throw ex;
        }
        return toReturn;
    }
}
