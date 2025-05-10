package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.service.ReseaNoShowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;

/**
 * {@code ReseaNoShowController} is a controller that handles HTTP requests and responses for
 *  NO Show functionality.
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 *
 * <ul>
 *   <li>{@code @PostMapping /appointment/noshow/{eventId}}: Handles request to update the 'No show' functionality details </li>
 * </ul>
 *
 * <p>
 * This controller interacts with the {@code ReseaNoShowService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/appointment/noshow")
@Validated
@Slf4j
@CrossOrigin
public class ReseaNoShowController {

    @Autowired
    private ReseaNoShowService noShowService;

    @PostMapping(path = "/{eventId}", produces = "application/json")
    public ResponseEntity submitNoShowForAppt(@Valid @NotNull(message = EVENT_ID_MANDATORY)
                                        @PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return ResponseEntity.ok().body(noShowService.saveNoShowDetails(eventId, getLoggedInStaffId.apply(request)));
    }

}