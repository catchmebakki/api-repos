package com.ssi.ms.logingov.apis.exception.custom;

import lombok.Getter;

/**
 * FileProcessingException provides service to custom exception.
 * 
 * @author Munirathnam Surepall
 */
@Getter
public class FileProcessingException extends RuntimeException {

	private final String errorCode;

	public FileProcessingException(String message, String errorCode, Throwable exception) {
		super(message, exception);
		this.errorCode = errorCode;
	}

}