package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class VwCcaseCaseloadDTO {

    private String caseStatus;

    private Long caseNo;

    private String claimantName;

    private String casePriorityDesc;

    private String caseAge;

    private String mostRecentRemedy;

    private String caseCharacteristics;

    private String nextFollowupDate;
}
