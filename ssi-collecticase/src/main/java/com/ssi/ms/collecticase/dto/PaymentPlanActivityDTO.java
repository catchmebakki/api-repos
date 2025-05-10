package com.ssi.ms.collecticase.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class PaymentPlanActivityDTO extends GeneralActivityDTO{

    private Long paymentPlanReponseToCd;

    private String paymentPlanReponseToOther;

    private BigDecimal paymentPlanGuideLineAmount;

    private Date paymentPlanSignedDate;

    private Date paymentPlanFinAffidavitSignedDate;

    private BigDecimal paymentPlanPaymentAmount;

    private Long paymentPlanPaymentCategory;

    private Date paymentPlanEffectiveUntilDate;

    private Long paymentPlanMonths;

    public Long getPaymentPlanReponseToCd() {
        return paymentPlanReponseToCd;
    }

    public void setPaymentPlanReponseToCd(Long paymentPlanReponseToCd) {
        this.paymentPlanReponseToCd = paymentPlanReponseToCd;
    }

    public String getPaymentPlanReponseToOther() {
        return paymentPlanReponseToOther;
    }

    public void setPaymentPlanReponseToOther(String paymentPlanReponseToOther) {
        this.paymentPlanReponseToOther = paymentPlanReponseToOther;
    }

    public BigDecimal getPaymentPlanGuideLineAmount() {
        return paymentPlanGuideLineAmount;
    }

    public void setPaymentPlanGuideLineAmount(BigDecimal paymentPlanGuideLineAmount) {
        this.paymentPlanGuideLineAmount = paymentPlanGuideLineAmount;
    }

    public Date getPaymentPlanSignedDate() {
        return paymentPlanSignedDate;
    }

    public void setPaymentPlanSignedDate(Date paymentPlanSignedDate) {
        this.paymentPlanSignedDate = paymentPlanSignedDate;
    }

    public Date getPaymentPlanFinAffidavitSignedDate() {
        return paymentPlanFinAffidavitSignedDate;
    }

    public void setPaymentPlanFinAffidavitSignedDate(Date paymentPlanFinAffidavitSignedDate) {
        this.paymentPlanFinAffidavitSignedDate = paymentPlanFinAffidavitSignedDate;
    }

    public BigDecimal getPaymentPlanPaymentAmount() {
        return paymentPlanPaymentAmount;
    }

    public void setPaymentPlanPaymentAmount(BigDecimal paymentPlanPaymentAmount) {
        this.paymentPlanPaymentAmount = paymentPlanPaymentAmount;
    }

    public Long getPaymentPlanPaymentCategory() {
        return paymentPlanPaymentCategory;
    }

    public void setPaymentPlanPaymentCategory(Long paymentPlanPaymentCategory) {
        this.paymentPlanPaymentCategory = paymentPlanPaymentCategory;
    }

    public Date getPaymentPlanEffectiveUntilDate() {
        return paymentPlanEffectiveUntilDate;
    }

    public void setPaymentPlanEffectiveUntilDate(Date paymentPlanEffectiveUntilDate) {
        this.paymentPlanEffectiveUntilDate = paymentPlanEffectiveUntilDate;
    }

    public Long getPaymentPlanMonths() {
        return paymentPlanMonths;
    }

    public void setPaymentPlanMonths(Long paymentPlanMonths) {
        this.paymentPlanMonths = paymentPlanMonths;
    }
}
