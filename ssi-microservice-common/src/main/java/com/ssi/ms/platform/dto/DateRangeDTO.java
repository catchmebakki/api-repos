package com.ssi.ms.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantResDTO class for representing date range.
 */
@Builder
@AllArgsConstructor
@Validated
@Getter
public class DateRangeDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
