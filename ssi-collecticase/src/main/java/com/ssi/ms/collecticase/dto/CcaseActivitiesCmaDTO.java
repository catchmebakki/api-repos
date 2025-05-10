package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import java.util.Date;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CcaseActivitiesCmaDTO {

    private Long activityId;

    private String activityNotes;

    private String activityAdditionalNotes;

    private String activityActivityCd;

    private String activityRemedyType;

    private Date activityDate;

    private String activityCreatedBy;

}
