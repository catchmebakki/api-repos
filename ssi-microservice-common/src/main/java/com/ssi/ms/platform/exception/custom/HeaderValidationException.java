package com.ssi.ms.platform.exception.custom;

/**
 * @author munirathnam.surepall
 * HeaderValidationException provides service to custom exception.
 */
public class HeaderValidationException extends RuntimeException {

    public HeaderValidationException(String message) {
        super(message);
    }
}
