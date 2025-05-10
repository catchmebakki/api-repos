package com.ssi.ms.resea.dto;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class LookupCaseSummaryReqDTO {
    List<Long> officeNum;
    List<Long> caseManagerId;
    List<Long> caseStage, caseStatus;
    String waitlisted, hiPriorityInd;
    Long rtwDaysMin, rtwDaysMax;
    Double caseScoreMin, caseScoreMax;
    Date orientationStartDt, orientationEndDt,
            initialApptStartDt, initialApptEndDt,
            recentApptStartDt, recentApptEndDt;
    List<Long> terminationReason;
    String claimantName;
    String ssn;
    Date clmByeStartDt, clmByeEndDt;
    PaginationDTO pagination;
    SortByDTO sortBy;
}