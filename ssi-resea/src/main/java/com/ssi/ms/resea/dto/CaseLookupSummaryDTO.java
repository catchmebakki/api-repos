package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseLookupSummaryDTO {
    Long caseId;
    String officeName,
            caseManagerName,
            stage,
            status,
            claimantName;
    String ssnLast4Digits;
    Long weeks;
    @JsonFormat(pattern = DATE_FORMAT)
    Date byeDt;
    String followUpType;
    @JsonFormat(pattern = DATE_FORMAT)
    Date followUpDt;
    String indicator;
    @JsonFormat(pattern = DATE_FORMAT)
    Date appointmentDate;
    Long claimId;
    String scheduleInd;
    String caseActivityDetails;

    public CaseLookupSummaryDTO(String officeName, String stage, String status, String claimantName, String ssnLast4Digits,
                                Long weeks, Date byeDt, String indicator, Long claimId, String scheduleInd, String caseActivityDetails){
        this.officeName = officeName;
        this.stage = stage;
        this.status = status;
        this.claimantName = claimantName;
        this.weeks = weeks;
        this.byeDt = byeDt;
        //this.followUpType = followUpType;
        //this.followUpDt = followUpDt;
        this.indicator = indicator;
        this.ssnLast4Digits = ssnLast4Digits;
        this.claimId = claimId;
        this.scheduleInd = scheduleInd;
        this.caseActivityDetails = caseActivityDetails;
    }
}