package com.ssi.ms.collecticase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
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
    @Pattern(regexp = CollecticaseUtilFunction.NUMERIC_PATTERN, message = "SSN is not valid and should be a valid numeric value.")
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
    @PastOrPresent(message = "Remedy From Date must not be in the future")
    private Date caseRemedyFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Remedy To Date must not be in the future")
    private Date caseRemedyToDate;
    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Case Open from date must not be in the future")
    private Date caseOpenFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Case Open to date must not be in the future")
    private Date caseOpenToDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Repayment from date must not be in the future")
    private Date repaymentFromDate;

    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Repayment to date must not be in the future")
    private Date repaymentToDate;

    public Long getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Long caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getClaimantSSN() {
        return claimantSSN;
    }

    public void setClaimantSSN(String claimantSSN) {
        this.claimantSSN = claimantSSN;
    }

    public String getClaimantLastName() {
        return claimantLastName;
    }

    public void setClaimantLastName(String claimantLastName) {
        this.claimantLastName = claimantLastName;
    }

    public String getClaimantFirstName() {
        return claimantFirstName;
    }

    public void setClaimantFirstName(String claimantFirstName) {
        this.claimantFirstName = claimantFirstName;
    }

    public String getOpmType() {
        return opmType;
    }

    public void setOpmType(String opmType) {
        this.opmType = opmType;
    }

    public BigDecimal getOpmBalRangeFrom() {
        return opmBalRangeFrom;
    }

    public void setOpmBalRangeFrom(BigDecimal opmBalRangeFrom) {
        this.opmBalRangeFrom = opmBalRangeFrom;
    }

    public BigDecimal getOpBalRangeTo() {
        return opBalRangeTo;
    }

    public void setOpBalRangeTo(BigDecimal opBalRangeTo) {
        this.opBalRangeTo = opBalRangeTo;
    }

    public Long getCasePriority() {
        return casePriority;
    }

    public void setCasePriority(Long casePriority) {
        this.casePriority = casePriority;
    }

    public String getNextFollowup() {
        return nextFollowup;
    }

    public void setNextFollowup(String nextFollowup) {
        this.nextFollowup = nextFollowup;
    }

    public Long getBankruptcyStatus() {
        return bankruptcyStatus;
    }

    public void setBankruptcyStatus(Long bankruptcyStatus) {
        this.bankruptcyStatus = bankruptcyStatus;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getCaseOpen() {
        return caseOpen;
    }

    public void setCaseOpen(String caseOpen) {
        this.caseOpen = caseOpen;
    }

    public Long getCaseRemedy() {
        return caseRemedy;
    }

    public void setCaseRemedy(Long caseRemedy) {
        this.caseRemedy = caseRemedy;
    }

    public Date getCaseRemedyFromDate() {
        return caseRemedyFromDate;
    }

    public void setCaseRemedyFromDate(Date caseRemedyFromDate) {
        this.caseRemedyFromDate = caseRemedyFromDate;
    }

    public Date getCaseRemedyToDate() {
        return caseRemedyToDate;
    }

    public void setCaseRemedyToDate(Date caseRemedyToDate) {
        this.caseRemedyToDate = caseRemedyToDate;
    }

    public Date getCaseOpenFromDate() {
        return caseOpenFromDate;
    }

    public void setCaseOpenFromDate(Date caseOpenFromDate) {
        this.caseOpenFromDate = caseOpenFromDate;
    }

    public Date getCaseOpenToDate() {
        return caseOpenToDate;
    }

    public void setCaseOpenToDate(Date caseOpenToDate) {
        this.caseOpenToDate = caseOpenToDate;
    }

    public Date getRepaymentFromDate() {
        return repaymentFromDate;
    }

    public void setRepaymentFromDate(Date repaymentFromDate) {
        this.repaymentFromDate = repaymentFromDate;
    }

    public Date getRepaymentToDate() {
        return repaymentToDate;
    }

    public void setRepaymentToDate(Date repaymentToDate) {
        this.repaymentToDate = repaymentToDate;
    }
}
