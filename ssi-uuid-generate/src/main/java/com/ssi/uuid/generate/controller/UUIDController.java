package com.ssi.uuid.generate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.uuid.generate.service.UUIDService;

/**
 * Controller class for managing UUID-related endpoints.
 */
@RestController
@RequestMapping("/uuid")
public class UUIDController {

	@Autowired
	private UUIDService uuidService;

	public UUIDController(UUIDService uuidService) {
		this.uuidService = uuidService;
	}

	/**
	 * Generates a time-based UUID and returns it as a String.
	 *
	 * @return String representation of the generated time-based UUID.
	 */
	@GetMapping("/generate")
	public String generateTimeBasedUUID() {
		return uuidService.generateTimeBasedUUID();
	}
	
}