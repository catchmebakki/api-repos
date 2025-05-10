package com.ssi.ms.platform.exception.custom;

import lombok.Getter;

/**
 * @author Praveenraja Paramsivam
 * FileProcessingException provides service to custom exception.
 */
@Getter
public class FileProcessingException extends RuntimeException {

    private final String errorCode;
    public FileProcessingException(String message, String errorCode, Throwable exception) {
        super(message, exception);
        this.errorCode = errorCode;
    }

}
