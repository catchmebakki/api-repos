package com.ssi.ms.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
/**
 * @author Praveenraja Paramsivam
 *Data Transfer Object ClaimantResDTO class for representing sortby.
 */
@Builder
@AllArgsConstructor
@Validated
@Getter
public class SortByDTO {
    private String field;
    private String direction;
}
