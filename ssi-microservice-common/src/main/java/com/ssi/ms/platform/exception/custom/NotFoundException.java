package com.ssi.ms.platform.exception.custom;

import lombok.Getter;

/**
 * @author munirathnam.surepall
 * NotFoundException provides service to custom exception.
 */
@Getter
public class NotFoundException extends RuntimeException {

    private final String errorCode;
    public NotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
