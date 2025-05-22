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
public class CaseNotesDTO {

    private Long activityId;

    private String activityDate;

    private String activitytypeDesc;

    private String activityRemedyTypeShortDesc;

    private String activityRemedyTypeDesc;

    private String activityNotes;

}
