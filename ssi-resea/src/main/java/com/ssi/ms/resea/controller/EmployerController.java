package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.dto.EmployerReqDTO;
import com.ssi.ms.resea.service.AlvService;
import com.ssi.ms.resea.service.EmployerService;
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

/**
 * {@code AlvController} is a controller that handles HTTP requests and responses for
 *  fetching the Allowable Values  associated for RESEA
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 * <p>
 * This controller interacts with the {@code AlvService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/employer")
@Validated
@Slf4j
@CrossOrigin
public class EmployerController {
    @Autowired
    private EmployerService alvService;

    @PostMapping(path = "/search", produces = "application/json")
    public ResponseEntity getEmployerSearchByName(@Valid @RequestBody final EmployerReqDTO employerReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                alvService.getEmployerSearchByName(employerReqDTO));
    }
}
