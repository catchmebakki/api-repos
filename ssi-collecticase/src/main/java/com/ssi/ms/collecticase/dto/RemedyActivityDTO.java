package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class RemedyActivityDTO {

    private Long activityTypeCd;

    private String activityTypeDesc;

    private Long remedyCd;

    private String remedyDesc;
}
