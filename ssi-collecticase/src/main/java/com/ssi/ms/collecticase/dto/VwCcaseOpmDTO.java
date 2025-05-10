package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class VwCcaseOpmDTO {

    private Long claimantId;

    private Long claimantDtmNBR;

    private Long claimantDtmVersionNBR;

    private Date claimantDtmMailedDt;

    private BigDecimal claimantOpmAmount;

    private BigDecimal claimantOpmBalAmount;

    private String claimantFraudInd;

    private String claimantBankruptcyOpmStatusDesc;

    private Date claimantBankruptcyOpmDate;

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }

    public Long getClaimantDtmNBR() {
        return claimantDtmNBR;
    }

    public void setClaimantDtmNBR(Long claimantDtmNBR) {
        this.claimantDtmNBR = claimantDtmNBR;
    }

    public Long getClaimantDtmVersionNBR() {
        return claimantDtmVersionNBR;
    }

    public void setClaimantDtmVersionNBR(Long claimantDtmVersionNBR) {
        this.claimantDtmVersionNBR = claimantDtmVersionNBR;
    }

    public Date getClaimantDtmMailedDt() {
        return claimantDtmMailedDt;
    }

    public void setClaimantDtmMailedDt(Date claimantDtmMailedDt) {
        this.claimantDtmMailedDt = claimantDtmMailedDt;
    }

    public BigDecimal getClaimantOpmAmount() {
        return claimantOpmAmount;
    }

    public void setClaimantOpmAmount(BigDecimal claimantOpmAmount) {
        this.claimantOpmAmount = claimantOpmAmount;
    }

    public BigDecimal getClaimantOpmBalAmount() {
        return claimantOpmBalAmount;
    }

    public void setClaimantOpmBalAmount(BigDecimal claimantOpmBalAmount) {
        this.claimantOpmBalAmount = claimantOpmBalAmount;
    }

    public String getClaimantFraudInd() {
        return claimantFraudInd;
    }

    public void setClaimantFraudInd(String claimantFraudInd) {
        this.claimantFraudInd = claimantFraudInd;
    }

    public String getClaimantBankruptcyOpmStatusDesc() {
        return claimantBankruptcyOpmStatusDesc;
    }

    public void setClaimantBankruptcyOpmStatusDesc(String claimantBankruptcyOpmStatusDesc) {
        this.claimantBankruptcyOpmStatusDesc = claimantBankruptcyOpmStatusDesc;
    }

    public Date getClaimantBankruptcyOpmDate() {
        return claimantBankruptcyOpmDate;
    }

    public void setClaimantBankruptcyOpmDate(Date claimantBankruptcyOpmDate) {
        this.claimantBankruptcyOpmDate = claimantBankruptcyOpmDate;
    }
}
