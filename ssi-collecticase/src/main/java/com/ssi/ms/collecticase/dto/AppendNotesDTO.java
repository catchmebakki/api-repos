package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class AppendNotesDTO {

    private String caseId;
    private String staffId;
    private String userId;
    private String activityId;
    private String activityNotes;

}
