package com.ssi.ms.resea.controller;


import com.ssi.ms.resea.service.StateService;
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

/**
 * {@code StateController} is a controller that handles HTTP requests and responses for
 *  fetching the States.
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 *
 * <ul>
 *    <li>{@code @GetMapping /state/list/{country} - Fetches the list of states based on the country code </li>
 *    <li> Country code : 47 (USA)</li>
 *    <li> Country code : 48 (Canada)</li>
 * </ul>
 *
 * <p>
 * This controller interacts with the {@code StateService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/state")
@Validated
@Slf4j
@CrossOrigin
public class StateController {
    @Autowired
    private StateService stateService;

    @GetMapping(path = "/list/{country}", produces = "application/json")
    public ResponseEntity getStates(@Valid @PathVariable("country") Long countryCode) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                stateService.getStates(countryCode));
    }
}
