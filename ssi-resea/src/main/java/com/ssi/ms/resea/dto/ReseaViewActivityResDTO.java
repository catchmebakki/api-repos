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
public class ReseaViewActivityResDTO {
    Long caseNumber;
    String claimantName;
    @JsonFormat(pattern = DATE_FORMAT)
    Date clmBeginDt, claimByeDt;
    String activityType;
    @JsonFormat(pattern = DATE_FORMAT)
    Date activityDate;
    String activityTime,
            activityDesc,
            activityDetail,
            activitySynopsis,
            activityNote,
            followUpType,
            apptUsageDesc,
            caseStatusDesc,
            caseStageDesc;
    @JsonFormat(pattern = DATE_FORMAT)
    Date followUpDt;
    String followUpNote;
    @JsonFormat(pattern = DATE_FORMAT)
    Date followUpCompleteDt;
    ReseaInterviewResDTO interviewResDTO;
    HeaderDetailsResDTO headerDetailsDTO;
}