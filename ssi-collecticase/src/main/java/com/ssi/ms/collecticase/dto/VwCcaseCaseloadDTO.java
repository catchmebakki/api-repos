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
public class VwCcaseCaseloadDTO {
// we should Not change the order of attributes/elements between Query in Repository and this VwCcaseCaseloadDTO - IMPORTANT
    private String claimantBankrupt;
    private String claimantBankruptDesc;
    private String claimantFraud;
    private String claimantFraudDesc;
    private String caseStatus;
    private Long caseNo;
    private String claimantName;
    private String casePriorityDesc;
    private Long caseAge;
    private String mostRecentRemedy;
    private String caseCharacteristics;
    private String nextFollowupRemedyWithDate;
    private String claimantFraudStatus;
    private String nextFollowupDate;
    private String nextFollowupRemedy;

}
