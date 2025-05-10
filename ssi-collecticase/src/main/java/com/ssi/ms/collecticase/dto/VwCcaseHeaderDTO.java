package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class VwCcaseHeaderDTO {
    private Long claimantId;

    private String completeSSN;

    private String last4SSN;

    private String claimantName;

    private String claimantAddress;

    private String claimantEmailAddress;

    private String claimantPhoneNbrs;

    private String claimantCurrFiling;

    private String claimantFelony;

    private BigDecimal claimantOpCollected;

    private Date claimantLastCollectionDt;

    private String claimantAppeal;

    private String bankruptcyStatusDt;

    private String claimantFraudInd;

    private String claimantNonFraudInd;

    private String claimantNonFraudEarnInd;

    private BigDecimal claimantOpBalance;

    private Long claimantCaseNo;

    private Long claimantCasePriority;

    private String claimantCasePriorityDesc;

    private Long claimantCaseStatus;

    private String claimantCaseStatusDesc;

    private Date claimantCaseOpenDt;

    private Date claimantCaseOrigOpenDt;

    private Long claimantCaseAge;

    private String claimantCaseCharacteristics;

    private String claimantCaseNextFollowupDate;

    private Long claimantCaseAssignedStaffId;

    private String claimantCaseAssignedStaffName;

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }

    public String getCompleteSSN() {
        return completeSSN;
    }

    public void setCompleteSSN(String completeSSN) {
        this.completeSSN = completeSSN;
    }

    public String getLast4SSN() {
        return last4SSN;
    }

    public void setLast4SSN(String last4SSN) {
        this.last4SSN = last4SSN;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantAddress() {
        return claimantAddress;
    }

    public void setClaimantAddress(String claimantAddress) {
        this.claimantAddress = claimantAddress;
    }

    public String getClaimantEmailAddress() {
        return claimantEmailAddress;
    }

    public void setClaimantEmailAddress(String claimantEmailAddress) {
        this.claimantEmailAddress = claimantEmailAddress;
    }

    public String getClaimantPhoneNbrs() {
        return claimantPhoneNbrs;
    }

    public void setClaimantPhoneNbrs(String claimantPhoneNbrs) {
        this.claimantPhoneNbrs = claimantPhoneNbrs;
    }

    public String getClaimantCurrFiling() {
        return claimantCurrFiling;
    }

    public void setClaimantCurrFiling(String claimantCurrFiling) {
        this.claimantCurrFiling = claimantCurrFiling;
    }

    public String getClaimantFelony() {
        return claimantFelony;
    }

    public void setClaimantFelony(String claimantFelony) {
        this.claimantFelony = claimantFelony;
    }

    public BigDecimal getClaimantOpCollected() {
        return claimantOpCollected;
    }

    public void setClaimantOpCollected(BigDecimal claimantOpCollected) {
        this.claimantOpCollected = claimantOpCollected;
    }

    public Date getClaimantLastCollectionDt() {
        return claimantLastCollectionDt;
    }

    public void setClaimantLastCollectionDt(Date claimantLastCollectionDt) {
        this.claimantLastCollectionDt = claimantLastCollectionDt;
    }

    public String getClaimantAppeal() {
        return claimantAppeal;
    }

    public void setClaimantAppeal(String claimantAppeal) {
        this.claimantAppeal = claimantAppeal;
    }

    public String getBankruptcyStatusDt() {
        return bankruptcyStatusDt;
    }

    public void setBankruptcyStatusDt(String bankruptcyStatusDt) {
        this.bankruptcyStatusDt = bankruptcyStatusDt;
    }

    public String getClaimantFraudInd() {
        return claimantFraudInd;
    }

    public void setClaimantFraudInd(String claimantFraudInd) {
        this.claimantFraudInd = claimantFraudInd;
    }

    public String getClaimantNonFraudInd() {
        return claimantNonFraudInd;
    }

    public void setClaimantNonFraudInd(String claimantNonFraudInd) {
        this.claimantNonFraudInd = claimantNonFraudInd;
    }

    public String getClaimantNonFraudEarnInd() {
        return claimantNonFraudEarnInd;
    }

    public void setClaimantNonFraudEarnInd(String claimantNonFraudEarnInd) {
        this.claimantNonFraudEarnInd = claimantNonFraudEarnInd;
    }

    public BigDecimal getClaimantOpBalance() {
        return claimantOpBalance;
    }

    public void setClaimantOpBalance(BigDecimal claimantOpBalance) {
        this.claimantOpBalance = claimantOpBalance;
    }

    public Long getClaimantCaseNo() {
        return claimantCaseNo;
    }

    public void setClaimantCaseNo(Long claimantCaseNo) {
        this.claimantCaseNo = claimantCaseNo;
    }

    public Long getClaimantCasePriority() {
        return claimantCasePriority;
    }

    public void setClaimantCasePriority(Long claimantCasePriority) {
        this.claimantCasePriority = claimantCasePriority;
    }

    public String getClaimantCasePriorityDesc() {
        return claimantCasePriorityDesc;
    }

    public void setClaimantCasePriorityDesc(String claimantCasePriorityDesc) {
        this.claimantCasePriorityDesc = claimantCasePriorityDesc;
    }

    public Long getClaimantCaseStatus() {
        return claimantCaseStatus;
    }

    public void setClaimantCaseStatus(Long claimantCaseStatus) {
        this.claimantCaseStatus = claimantCaseStatus;
    }

    public String getClaimantCaseStatusDesc() {
        return claimantCaseStatusDesc;
    }

    public void setClaimantCaseStatusDesc(String claimantCaseStatusDesc) {
        this.claimantCaseStatusDesc = claimantCaseStatusDesc;
    }

    public Date getClaimantCaseOpenDt() {
        return claimantCaseOpenDt;
    }

    public void setClaimantCaseOpenDt(Date claimantCaseOpenDt) {
        this.claimantCaseOpenDt = claimantCaseOpenDt;
    }

    public Date getClaimantCaseOrigOpenDt() {
        return claimantCaseOrigOpenDt;
    }

    public void setClaimantCaseOrigOpenDt(Date claimantCaseOrigOpenDt) {
        this.claimantCaseOrigOpenDt = claimantCaseOrigOpenDt;
    }

    public Long getClaimantCaseAge() {
        return claimantCaseAge;
    }

    public void setClaimantCaseAge(Long claimantCaseAge) {
        this.claimantCaseAge = claimantCaseAge;
    }

    public String getClaimantCaseCharacteristics() {
        return claimantCaseCharacteristics;
    }

    public void setClaimantCaseCharacteristics(String claimantCaseCharacteristics) {
        this.claimantCaseCharacteristics = claimantCaseCharacteristics;
    }

    public String getClaimantCaseNextFollowupDate() {
        return claimantCaseNextFollowupDate;
    }

    public void setClaimantCaseNextFollowupDate(String claimantCaseNextFollowupDate) {
        this.claimantCaseNextFollowupDate = claimantCaseNextFollowupDate;
    }

    public Long getClaimantCaseAssignedStaffId() {
        return claimantCaseAssignedStaffId;
    }

    public void setClaimantCaseAssignedStaffId(Long claimantCaseAssignedStaffId) {
        this.claimantCaseAssignedStaffId = claimantCaseAssignedStaffId;
    }

    public String getClaimantCaseAssignedStaffName() {
        return claimantCaseAssignedStaffName;
    }

    public void setClaimantCaseAssignedStaffName(String claimantCaseAssignedStaffName) {
        this.claimantCaseAssignedStaffName = claimantCaseAssignedStaffName;
    }
}


