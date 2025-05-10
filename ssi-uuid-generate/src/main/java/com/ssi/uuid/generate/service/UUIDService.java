package com.ssi.uuid.generate.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;

/**
 * Service class responsible for generating UUIDs.
 */
@Service
public class UUIDService {
	/**
	 * Generates a time-based UUID using the java-uuid-generator library.
	 * 
	 * @return String representation of the generated time-based UUID.
	 */
	public String generateTimeBasedUUID() {
		// Generate a time-based UUID
		final UUID timeBasedUuid = Generators.timeBasedGenerator().generate();
		return timeBasedUuid.toString();
	}
}
