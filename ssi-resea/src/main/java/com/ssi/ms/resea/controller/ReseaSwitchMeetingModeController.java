package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.dto.ReseaSwitchMeetingModeReqDTO;
import com.ssi.ms.resea.service.ReseaSwitchMeetingModeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

/**
 * {@code ReseaSwitchMeetingModeController} is a controller that handles HTTP requests and responses for
 *  Switch Meeting Mode functionality.
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 *
 * <ul>
 *    <li>{@code @GetMapping /appointment/switchmeetingmode/reasons/{currentMeetingMode}: Handles loading of the page
 *                                                      with reasons for switching meeting modes</li>
 *   <li>{@code @PostMapping /appointment/switchmeetingmode/save}: Handles form submissions for saving the Switch Meeting Modes </li>
 * </ul>
 *
 * <p>
 * This controller interacts with the {@code ReseaSwitchMeetingModeService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */
@RestController
@RequestMapping("/appointment/switchmeetingmode")
@Validated
@Slf4j
@CrossOrigin
public class ReseaSwitchMeetingModeController {

    @Autowired
    private ReseaSwitchMeetingModeService reseaSwitchMeetingModeService;


    /**
     * Handles loading of the page with reasons for switching meeting modes
     * @param curMtgMode
     *
     * @return
     */
    @GetMapping(path = "/reasons", produces = "application/json")
    public ResponseEntity getReasonsForSwitchMeetingMode(@Valid @RequestParam("currentmeetingmode") String curMtgMode) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                reseaSwitchMeetingModeService.getReasonsForSwitchMeetingMode(curMtgMode));
    }

    /**
     * Handles form submissions for saving the Switch Meeting Modes
     *
     * @param switchMtgModeReqDTO
     * @param request
     * @return
     */
    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity saveSwitchMeetingMode(
            @Valid @RequestBody final ReseaSwitchMeetingModeReqDTO switchMtgModeReqDTO, HttpServletRequest request) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(reseaSwitchMeetingModeService.saveSwitchMeetingMode(switchMtgModeReqDTO, getLoggedInStaffId.apply(request),
                            getLoggedInRoleId.apply(request)));
    }

}