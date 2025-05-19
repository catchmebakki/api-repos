package com.ssi.ms.collecticase.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReassignDTO {
    private Long caseId;
    private Long staffId;
    private String userId;
    private String casePriority;
}
