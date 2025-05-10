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
public class CreateCaseDTO {

    private Long claimantId;

    private Long staffId;

    private Long casePriority;

    private Long caseRemedyCd;

    private Long caseActivityCd;

    private String callingUser;

    private String usingProgramName;
}
