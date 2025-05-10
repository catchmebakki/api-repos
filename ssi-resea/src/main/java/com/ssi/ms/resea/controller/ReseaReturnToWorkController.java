package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.dto.ReturnToWorkReqDTO;
import com.ssi.ms.resea.service.ReseaReturnToWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;

/**
 * {@code ReseaReturnToWorkController} is a controller that handles HTTP requests and responses for
 *  RTW functionality.
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 *
 * <ul>
 *   <li>{@code @PostMapping /appointment/rtw/save}: Handles form submissions for saving the RTW details </li>
 * </ul>
 *
 * <p>
 * This controller interacts with the {@code ReseaReturnToWorkService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/appointment/rtw")
@Validated
@Slf4j
@CrossOrigin
public class ReseaReturnToWorkController {

    @Autowired
    private ReseaReturnToWorkService rtwService;

    /**
     * Handle the request to display the view-only version of return to work details
     */
    @GetMapping(path = "/view/{eventId}", produces = "application/json")
    public ResponseEntity viewRTW(@Valid @NotNull(message = EVENT_ID_MANDATORY)
            @PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(rtwService.viewReturnToWork(eventId));

    }

    /**
     * Handle the request to save return to work details
     */
    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity<Object> saveRTW(
            @Valid @RequestBody @NotNull(message = "rtwReqDTO.notnull") final ReturnToWorkReqDTO rtwReqDTO,
            HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(rtwService.saveReturnToWork(rtwReqDTO, getLoggedInStaffId.apply(request)));
    }




}