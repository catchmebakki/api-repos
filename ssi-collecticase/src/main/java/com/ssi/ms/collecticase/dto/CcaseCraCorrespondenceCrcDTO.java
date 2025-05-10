package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CcaseCraCorrespondenceCrcDTO {

    private Long remedyActivityCorrId;
    private String remedyActivityCorrName;
    private String remedyActivityCorrEnable;
    private Long remedyActivityCorrCounty;
    private String remedyActivityCorrAutoSet;
    private String remedyActivityCorrCurrentFiling;
    private Long remedyActivityCorrPaymentCategory;
    private String remedyActivityCorrDoNotGarnish;
    private String remedyActivityCorrCourtOrdered;

    public Long getRemedyActivityCorrId() {
        return remedyActivityCorrId;
    }

    public void setRemedyActivityCorrId(Long remedyActivityCorrId) {
        this.remedyActivityCorrId = remedyActivityCorrId;
    }

    public String getRemedyActivityCorrName() {
        return remedyActivityCorrName;
    }

    public void setRemedyActivityCorrName(String remedyActivityCorrName) {
        this.remedyActivityCorrName = remedyActivityCorrName;
    }

    public String getRemedyActivityCorrEnable() {
        return remedyActivityCorrEnable;
    }

    public void setRemedyActivityCorrEnable(String remedyActivityCorrEnable) {
        this.remedyActivityCorrEnable = remedyActivityCorrEnable;
    }

    public Long getRemedyActivityCorrCounty() {
        return remedyActivityCorrCounty;
    }

    public void setRemedyActivityCorrCounty(Long remedyActivityCorrCounty) {
        this.remedyActivityCorrCounty = remedyActivityCorrCounty;
    }

    public String getRemedyActivityCorrAutoSet() {
        return remedyActivityCorrAutoSet;
    }

    public void setRemedyActivityCorrAutoSet(String remedyActivityCorrAutoSet) {
        this.remedyActivityCorrAutoSet = remedyActivityCorrAutoSet;
    }

    public String getRemedyActivityCorrCurrentFiling() {
        return remedyActivityCorrCurrentFiling;
    }

    public void setRemedyActivityCorrCurrentFiling(String remedyActivityCorrCurrentFiling) {
        this.remedyActivityCorrCurrentFiling = remedyActivityCorrCurrentFiling;
    }

    public Long getRemedyActivityCorrPaymentCategory() {
        return remedyActivityCorrPaymentCategory;
    }

    public void setRemedyActivityCorrPaymentCategory(Long remedyActivityCorrPaymentCategory) {
        this.remedyActivityCorrPaymentCategory = remedyActivityCorrPaymentCategory;
    }

    public String getRemedyActivityCorrDoNotGarnish() {
        return remedyActivityCorrDoNotGarnish;
    }

    public void setRemedyActivityCorrDoNotGarnish(String remedyActivityCorrDoNotGarnish) {
        this.remedyActivityCorrDoNotGarnish = remedyActivityCorrDoNotGarnish;
    }

    public String getRemedyActivityCorrCourtOrdered() {
        return remedyActivityCorrCourtOrdered;
    }

    public void setRemedyActivityCorrCourtOrdered(String remedyActivityCorrCourtOrdered) {
        this.remedyActivityCorrCourtOrdered = remedyActivityCorrCourtOrdered;
    }
}
