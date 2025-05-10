package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class WageGarnishmentActivityDTO extends GeneralActivityDTO{

    private Long employerId;

    private Long employerContactId;

    private Long employerRepresentativeCd;

    private BigDecimal wageAmount;

    private String doNotGarnishInd;

    private Long wageFrequency;

    private Long wageNonCompliance;

    private Date wageMotionFiledOn;

    private Date wageEffectiveFrom;

    private Date wageEffectiveUntil;

    private Long courtId;

    private String courtOrderedInd;

    private Date courtOrderedDate;

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public Long getEmployerContactId() {
        return employerContactId;
    }

    public void setEmployerContactId(Long employerContactId) {
        this.employerContactId = employerContactId;
    }

    public Long getEmployerRepresentativeCd() {
        return employerRepresentativeCd;
    }

    public void setEmployerRepresentativeCd(Long employerRepresentativeCd) {
        this.employerRepresentativeCd = employerRepresentativeCd;
    }

    public BigDecimal getWageAmount() {
        return wageAmount;
    }

    public void setWageAmount(BigDecimal wageAmount) {
        this.wageAmount = wageAmount;
    }

    public String getDoNotGarnishInd() {
        return doNotGarnishInd;
    }

    public void setDoNotGarnishInd(String doNotGarnishInd) {
        this.doNotGarnishInd = doNotGarnishInd;
    }

    public Long getWageFrequency() {
        return wageFrequency;
    }

    public void setWageFrequency(Long wageFrequency) {
        this.wageFrequency = wageFrequency;
    }

    public Long getWageNonCompliance() {
        return wageNonCompliance;
    }

    public void setWageNonCompliance(Long wageNonCompliance) {
        this.wageNonCompliance = wageNonCompliance;
    }

    public Date getWageMotionFiledOn() {
        return wageMotionFiledOn;
    }

    public void setWageMotionFiledOn(Date wageMotionFiledOn) {
        this.wageMotionFiledOn = wageMotionFiledOn;
    }

    public Date getWageEffectiveFrom() {
        return wageEffectiveFrom;
    }

    public void setWageEffectiveFrom(Date wageEffectiveFrom) {
        this.wageEffectiveFrom = wageEffectiveFrom;
    }

    public Date getWageEffectiveUntil() {
        return wageEffectiveUntil;
    }

    public void setWageEffectiveUntil(Date wageEffectiveUntil) {
        this.wageEffectiveUntil = wageEffectiveUntil;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getCourtOrderedInd() {
        return courtOrderedInd;
    }

    public void setCourtOrderedInd(String courtOrderedInd) {
        this.courtOrderedInd = courtOrderedInd;
    }

    public Date getCourtOrderedDate() {
        return courtOrderedDate;
    }

    public void setCourtOrderedDate(Date courtOrderedDate) {
        this.courtOrderedDate = courtOrderedDate;
    }
}
