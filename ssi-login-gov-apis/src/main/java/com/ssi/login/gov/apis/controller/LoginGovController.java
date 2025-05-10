package com.ssi.login.gov.apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.login.gov.apis.service.LoginGovService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Validated
@Slf4j
@CrossOrigin
public class LoginGovController {

	@Autowired
	private LoginGovService loginGovService;

	@GetMapping(path = "/swa", produces = "application/json")
	public ResponseEntity<String> getSWARecords1() {
		ResponseEntity<String> uuid = loginGovService.getSWARecords();
		return uuid;
	}

}
