package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class VwCcaseRemedyDTO {

    private Long caseId;
    private String caseRemedyDesc;
    private Date caseRemedyDate;
    private String caseRemedyStageDesc;

    private String caseRemedyStatusDesc;

    private BigDecimal caseRemedyEligibleAmt;

    private BigDecimal caseRemedyCollectedAmt;

    private String caseRemedyNextStepDesc;

}
