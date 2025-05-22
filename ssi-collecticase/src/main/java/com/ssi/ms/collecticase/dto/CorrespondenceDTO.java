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
public class CorrespondenceDTO {

    private Long caseCorrespondenceId;

    private String caseCorrespondenceName;

    private String caseCorrespondenceMailDate;

    private String caseCorrespondenceActivityName;

    private String caseCorrespondenceRemedyName;

    private String caseCorrespondenceFilePathName;
}
