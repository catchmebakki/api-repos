package com.ssi.ms.collecticase.dto;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class CreateCorrespondenceDTO {

    private Integer reportId;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public Integer getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Integer claimantId) {
        this.claimantId = claimantId;
    }

    public String getCorCoeInd() {
        return corCoeInd;
    }

    public void setCorCoeInd(String corCoeInd) {
        this.corCoeInd = corCoeInd;
    }

    public String getCorForcedInd() {
        return corForcedInd;
    }

    public void setCorForcedInd(String corForcedInd) {
        this.corForcedInd = corForcedInd;
    }

    public Integer getCorStatusCd() {
        return corStatusCd;
    }

    public void setCorStatusCd(Integer corStatusCd) {
        this.corStatusCd = corStatusCd;
    }

    public Integer getCorDecId() {
        return corDecId;
    }

    public void setCorDecId(Integer corDecId) {
        this.corDecId = corDecId;
    }

    public Integer getCorReceipientIfk() {
        return corReceipientIfk;
    }

    public void setCorReceipientIfk(Integer corReceipientIfk) {
        this.corReceipientIfk = corReceipientIfk;
    }

    public Integer getCorReceipientCd() {
        return corReceipientCd;
    }

    public void setCorReceipientCd(Integer corReceipientCd) {
        this.corReceipientCd = corReceipientCd;
    }

    public Timestamp getCorTimeStamp() {
        return corTimeStamp;
    }

    public void setCorTimeStamp(Timestamp corTimeStamp) {
        this.corTimeStamp = corTimeStamp;
    }

    public String getCorCoeString() {
        return corCoeString;
    }

    public void setCorCoeString(String corCoeString) {
        this.corCoeString = corCoeString;
    }

    private Integer claimId;
    private Integer employerId;
    private Integer claimantId;
    private String corCoeInd;
    private String corForcedInd;
    private Integer corStatusCd;
    private Integer corDecId;
    private Integer corReceipientIfk;
    private Integer corReceipientCd;
    private Timestamp corTimeStamp;
    private String corCoeString;
}
