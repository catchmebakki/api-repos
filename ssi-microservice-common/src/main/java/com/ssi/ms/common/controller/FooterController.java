package com.ssi.ms.common.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.common.dto.FooterDTO;
import com.ssi.ms.common.service.ParameterParService;

/**
 * @author munirathnam.surepall
 * FooterController provide services to system date, DB date and application version.
 */
@CrossOrigin
@RestController
@RequestMapping("/footer")
public class FooterController {

	@Value("${application.app-version:0.0.0}")
	private String appVersion;
	@Autowired
	private ParameterParService parameterParService;

	/**
	 * This function, get footer values like system date, DB date and application version.
	 * @return {@link ResponseEntity<Object>} A ResponseEntity containing the footer information as the response body.
	 */
	@GetMapping(path = "/", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getFooter() {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(FooterDTO.builder().dbDate(parameterParService.getDBDate())
						.systemDate(new Date(System.currentTimeMillis())).applicationVersion(appVersion).build());
	}

}
