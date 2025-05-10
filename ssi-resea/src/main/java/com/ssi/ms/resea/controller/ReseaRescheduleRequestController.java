package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsReqDTO;
import com.ssi.ms.resea.dto.ReseaRescheduleSaveReqDTO;
import com.ssi.ms.resea.service.ReseaRescheduleRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

/**
 * {@code ReseaRescheduleRequestController} is a controller that handles HTTP requests and responses for
 *  Rescheduling functionality.
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 *
 * <ul>
 *    <li>{@code @PostMapping /appointment/reschedule/load}: Handles loading of reschedule page with list of available slots</li>
 *   <li>{@code @PostMapping /appointment/reschedule/save}: Handles form submissions for saving the Reschedule details </li>
 * </ul>
 *
 * <p>
 * This controller interacts with the {@code ReseaRescheduleRequestService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/appointment/reschedule")
@Validated
@Slf4j
@CrossOrigin
public class ReseaRescheduleRequestController {

    @Autowired
    private ReseaRescheduleRequestService reseaRescheduleRequestService;

    /**
     * Handles loading of reschedule page with list of available slots
     *
     * @param rschGetAvailableSlotsReqDto
     * @param request
     * @return
     */
    @PostMapping(path = "/load", produces = "application/json")
    public ResponseEntity<Object> getAvailableSlots(
            @Valid @RequestBody final ReseaRescheduleGetAvailableSlotsReqDTO rschGetAvailableSlotsReqDto,
            HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(reseaRescheduleRequestService.getAvailableSlotsForReschedule(rschGetAvailableSlotsReqDto));
    }


    /**
     * Handles form submissions for saving the Rescheduling details
     *
     * @param rschSaveReqDTO
     * @param request
     * @return
     */
    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity<Object> saveRescheduleRequest(
            @Valid @RequestBody final ReseaRescheduleSaveReqDTO rschSaveReqDTO, HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(reseaRescheduleRequestService.saveRescheduleRequest(rschSaveReqDTO, getLoggedInStaffId.apply(request)));
    }

}