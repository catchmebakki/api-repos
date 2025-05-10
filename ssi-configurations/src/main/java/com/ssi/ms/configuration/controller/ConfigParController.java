package com.ssi.ms.configuration.controller;

import com.ssi.ms.configuration.dto.parameter.ConfigParChildListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParSaveReqDTO;
import com.ssi.ms.configuration.service.ConfigParService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

@RestController
@RequestMapping("/par")
@Validated
@Slf4j
@CrossOrigin
public class ConfigParController {
    @Autowired
    private ConfigParService configParService;

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @PostMapping(path = "/search", produces = "application/json")
    public ResponseEntity getParameters(@Valid @RequestBody final ConfigParListReqDTO parReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configParService.getParametersByCategory(parReqDTO));
    }

    @GetMapping(path = "/parId/{parId}", produces = "application/json")
    public ResponseEntity getParDetails(
            @Valid
            @NotNull(message = "parId.mandatory")
            @PathVariable("parId") Long parId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(configParService.getParDetails(parId));

    }

    @PostMapping(path = "/save", produces = "application/json")
    public ResponseEntity saveParDetails(@Valid @RequestBody final ConfigParSaveReqDTO parSaveDTO,
                                         HttpServletRequest request) {
        configParService.saveParDetails(parSaveDTO, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/delete/{parId}", produces = "application/json")
    public ResponseEntity deleteParameter(@Valid
                                              @NotNull(message = "parId.mandatory")
                                              @PathVariable("parId") Long parId,
                                          HttpServletRequest request) {
        configParService.deleteParameter(parId, getLoggedInStaffId.apply(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/childlist", produces = "application/json")
    public ResponseEntity getParChildList(@Valid @RequestBody final ConfigParChildListReqDTO parReqDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configParService.getChildParametersById(parReqDTO));
    }

    @GetMapping(path = "/nameList/categoryCd/{parCategoryCd}", produces = "application/json")
    public ResponseEntity getParNameList(
            @Valid
            @NotNull(message = "parCategoryCd.mandatory")
            @PathVariable("parCategoryCd") Long parCategoryCd) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                configParService.getParNameListByCategory(parCategoryCd));
    }
}
