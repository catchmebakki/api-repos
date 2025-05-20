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
public class CaseLookupDTO {
    private Long caseNumber;
    private String claimantSSN;
    private String claimantLastName;
    private String claimantFirstName;
    private String opmType;
    private BigDecimal opmBalRangeFrom;
    private BigDecimal opBalRangeTo;
    private Long casePriority;
    private String nextFollowup;
    private Long bankruptcyStatus;
    private Long assignedTo;
    private String telephoneNumber;
    private String caseOpen;
    private Long caseRemedy;
    private Date caseRemedyFromDate;
    private Date caseRemedyToDate;
    private Date caseOpenFromDate;
    private Date caseOpenToDate;
    private Date repaymentFromDate;
    private Date repaymentToDate;
}
