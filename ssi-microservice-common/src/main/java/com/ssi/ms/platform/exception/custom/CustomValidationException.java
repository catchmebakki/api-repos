package com.ssi.ms.platform.exception.custom;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Praveenraja Paramsivam
 * CustomValidationException provides service to custom exception.
 */
@Getter
public class CustomValidationException extends RuntimeException {
    private final Map<String, List<String>> errorDetails;
    public CustomValidationException(String message, Map<String, List<String>> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }

}
