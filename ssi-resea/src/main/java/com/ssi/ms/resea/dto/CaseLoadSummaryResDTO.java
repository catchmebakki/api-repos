package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class CaseLoadSummaryResDTO {
    Long caseNum, claimId;
    String claimantName;
    @JsonFormat(pattern = DATE_FORMAT)
    Date byeDt;
    String stage;
    String status;
    @JsonFormat(pattern = DATE_FORMAT)
    Date statusDt;
    Long ccaWeeks;
    @JsonFormat(pattern = DATE_FORMAT)
    Date followUpDt;
    String followUpType, followUpNote;
    String indicator;
    String partialSsn;
    String scheduleInd, applyWaitlistInd, clearWaitlistInd;
    public CaseLoadSummaryResDTO(Long caseNum, String claimantName, Date byeDt, String stage, String status, Date statusDt,
                                 Long ccaWeeks, Date followUpDt, String followUpType, String followUpNote, String indicator,
                                 String partialSsn, Long claimId, String scheduleInd, String applyWaitlistInd, String clearWaitlistInd){
        this.caseNum = caseNum;
        this.claimantName = claimantName;
        this.byeDt = byeDt;
        this.stage = stage;
        this.status = status;
        this.statusDt = statusDt;
        this.ccaWeeks = ccaWeeks;
        this.followUpDt = followUpDt;
        this.followUpType = followUpType;
        this.followUpNote = followUpNote;
        this.indicator = indicator;
        this.partialSsn = partialSsn;
        this.scheduleInd = scheduleInd;
        this.claimId = claimId;
        this.applyWaitlistInd = applyWaitlistInd;
        this.clearWaitlistInd = clearWaitlistInd;
    }
}
