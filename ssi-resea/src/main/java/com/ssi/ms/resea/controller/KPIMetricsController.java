package com.ssi.ms.resea.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.dto.kpi.KPIMetricsReqDTO;
import com.ssi.ms.resea.service.KPIMetricsService;
import com.ssi.ms.resea.validator.KPIMetricsRequestValidator;

@RestController
@RequestMapping("/kpi-summary")
@Validated
@CrossOrigin
public class KPIMetricsController {
	
	@Autowired
	public KPIMetricsService kpiMetricsService;
	
	@Autowired
	public KPIMetricsRequestValidator kpiMetricsRequestValidator;
	
	@PostMapping(produces = "application/json")
	public ResponseEntity getKPIMetrics( @Valid @RequestBody final KPIMetricsReqDTO kpiMetricsReqDTO, HttpServletRequest request) {
		final HashMap<String, List<String>> errorMap = kpiMetricsRequestValidator.validateKPIMetricsReqDTO(kpiMetricsReqDTO);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException(ErrorMessageConstant.GET_AVAILABLE_SLOTS_FOR_RESCHEDULE_FAILED, errorMap);
		} else {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(kpiMetricsService.getKPIMetrics(kpiMetricsReqDTO));
		}
	}
}
