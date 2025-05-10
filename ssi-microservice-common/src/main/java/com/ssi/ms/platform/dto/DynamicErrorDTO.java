package com.ssi.ms.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author
 * Data Transfer Object DynamicErrorDTO class for custom errors.
 */
@Builder
@AllArgsConstructor
@Validated
@Getter
public class DynamicErrorDTO {
    private String errorCode;
    private List<String> errorParams;
}
