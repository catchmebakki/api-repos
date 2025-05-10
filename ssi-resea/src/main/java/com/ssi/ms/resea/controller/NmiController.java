package com.ssi.ms.resea.controller;

import com.ssi.ms.resea.dto.NonMonIssuesNmiListReqDTO;
import com.ssi.ms.resea.dto.NonMonIssuesNmiListResDTO;
import com.ssi.ms.resea.service.NmiService;
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
import java.util.List;

/**
 * {@code NmiController} is a controller that handles HTTP requests and responses for
 *  fetching the Non Monetary Issue (NMI)  associated for RESEA
 *
 * <p>
 * This controller is responsible for mapping incoming requests to appropriate service methods, processing
 * the data, and returning the appropriate view or response. It typically handles the following types of requests:
 * </p>
 * <p>
 * This controller interacts with the {@code NmiService} service to execute business logic and may also interact
 * with other services or repositories as needed. It returns JSON responses to the request.
 * </p>
 * <p>
 *
 * @author Anand
 */

@RestController
@RequestMapping("/nmi")
@Validated
@Slf4j
@CrossOrigin
public class NmiController {
    @Autowired
    private NmiService nmiService;


    @GetMapping(path = "/list/{nmiId}", produces = "application/json")
    public ResponseEntity getChildNmiList(@Valid
                                          @PathVariable("nmiId") Long nmiId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                nmiService.getChildNmiList(nmiId));
    }

    @PostMapping(path = "/list/", produces = "application/json")
    public ResponseEntity getNmiListBasedOnModuleAndPage(@Valid
            @RequestBody final NonMonIssuesNmiListReqDTO nmiReqDTO) {
        List<NonMonIssuesNmiListResDTO> nmiListResDTOS;
        if (nmiReqDTO.getIssueId() == null || nmiReqDTO.getIssueId() == 0L) {
            nmiListResDTOS = nmiService.getParentNmiListBasedOnModuleAndPage(nmiReqDTO);
        } else {
            nmiListResDTOS = nmiService.getChildNmiListBasedOnModuleAndPage(nmiReqDTO);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(nmiListResDTOS);
    }
}
