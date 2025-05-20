package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ActivitiesSummaryDTO {
    private Long id;
    private Long activityId;
    private Date activityDate;
    private String activityTypeDesc;
    private String activitySpecifics;
    private String activityRemedyDesc;
    private String activityCreatedByUser;
    private Date activityFollowupDate;
    private String activityFollowupShortNote;
    private String activityFollowupComplete;
    private String activityFollowupCompletedBy;
    private Date activityFollowupCompleteDate;
    private String activityFollowupCompleteShortNote;
}
