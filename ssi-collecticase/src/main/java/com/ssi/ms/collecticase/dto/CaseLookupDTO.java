package com.ssi.ms.collecticase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseLookupDTO {
    private Long caseNumber;
    @Pattern(regexp = CollecticaseUtilFunction.NUMERIC_PATTERN, message = ErrorMessageConstant
            .CASE_LOOKUP_CLAIMANT_SSN_INVALID)
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

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_REMEDY_FROM_DATE_FUTURE)
    private Date caseRemedyFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_REMEDY_TO_DATE_FUTURE)
    private Date caseRemedyToDate;
    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_CASE_OPEN_FROM_DATE_FUTURE)
    private Date caseOpenFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_CASE_OPEN_TO_DATE_FUTURE)
    private Date caseOpenToDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_REPAYMENT_FROM_DATE_FUTURE)
    private Date repaymentFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ErrorMessageConstant.CASE_LOOKUP_REPAYMENT_TO_DATE_FUTURE)
    private Date repaymentToDate;
}
