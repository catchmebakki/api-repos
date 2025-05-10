package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class CaseSummaryResDTO {
    String claimantName, caseManagerName;
    @JsonFormat(pattern = DATE_FORMAT)
    Date clmBeginDt, byeDt;
    private Long caseId;
    String stage, status;
    Integer weeksFiled;
    Double profileScore;
    String orientationDt;
    @JsonFormat(pattern = DATE_FORMAT)
    Date initialAppttDt, firstSubsequentApptDt, secondSubsequentApptDt;
    Short orientatonRschCnt, initialAppttRschCnt, firstSubsequentApptRschCnt, secondSubsequentApptRschCnt;
    String indicators,followupsIndicator, jobReferral, synopsis;
    String earliestFollowUpType;
    @JsonFormat(pattern = DATE_FORMAT)
    Date earliestFollowUpDt;
    @JsonFormat(pattern = DATE_FORMAT)
    Date closedOnDt;
    String closedReason;
    String ssnLast4Digits;
    List<HeaderWorkSrchDetailsDTO> workSearch;
    List<HeaderIssueDetailsDTO> issues;
    List<HeaderJobRefDetailsDTO> jobReferrals;
    List<CaseActivitySummaryDTO> activitySummary;
}
