package com.ssi.ms.logingov.apis.exception.custom;

import lombok.Getter;

/**
 * NotFoundException provides service to custom exception.
 * @author munirathnam.surepall
 */
@Getter
public class NotFoundException extends RuntimeException {

	private final String errorCode;

	public NotFoundException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}