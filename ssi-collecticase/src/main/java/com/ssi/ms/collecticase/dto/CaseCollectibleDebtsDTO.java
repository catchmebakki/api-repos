package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseCollectibleDebtsDTO {

    BigDecimal overpaymentBalanceAmount;

    BigDecimal overpaymentFraudBalanceAmount;

    BigDecimal overpaymentNonFraudBalanceAmount;

    BigDecimal overpaymentInterestBalanceAmount;

    Long claimantId;

    public BigDecimal getOverpaymentBalanceAmount() {
        return overpaymentBalanceAmount;
    }

    public void setOverpaymentBalanceAmount(BigDecimal overpaymentBalanceAmount) {
        this.overpaymentBalanceAmount = overpaymentBalanceAmount;
    }

    public BigDecimal getOverpaymentFraudBalanceAmount() {
        return overpaymentFraudBalanceAmount;
    }

    public void setOverpaymentFraudBalanceAmount(BigDecimal overpaymentFraudBalanceAmount) {
        this.overpaymentFraudBalanceAmount = overpaymentFraudBalanceAmount;
    }

    public BigDecimal getOverpaymentNonFraudBalanceAmount() {
        return overpaymentNonFraudBalanceAmount;
    }

    public void setOverpaymentNonFraudBalanceAmount(BigDecimal overpaymentNonFraudBalanceAmount) {
        this.overpaymentNonFraudBalanceAmount = overpaymentNonFraudBalanceAmount;
    }

    public BigDecimal getOverpaymentInterestBalanceAmount() {
        return overpaymentInterestBalanceAmount;
    }

    public void setOverpaymentInterestBalanceAmount(BigDecimal overpaymentInterestBalanceAmount) {
        this.overpaymentInterestBalanceAmount = overpaymentInterestBalanceAmount;
    }

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }
}
