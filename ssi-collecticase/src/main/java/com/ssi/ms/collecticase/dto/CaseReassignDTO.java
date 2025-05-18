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
public class CaseReassignDTO {

    private String claimantName;

    private String claimantCaseNo;

    private String claimantFraudNonFraudDesc;

    private String claimantCaseAssignedStaffName;
}
