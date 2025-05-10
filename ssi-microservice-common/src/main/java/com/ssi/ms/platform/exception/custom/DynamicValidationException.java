package com.ssi.ms.platform.exception.custom;

import com.ssi.ms.platform.dto.DynamicErrorDTO;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Praveenraja Paramsivam
 * CustomValidationException provides service to custom exception.
 */
@Getter
public class DynamicValidationException extends RuntimeException {
    private final Map<String, List<DynamicErrorDTO>> errorDetails;
    public DynamicValidationException(String message, Map<String, List<DynamicErrorDTO>> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }

}
