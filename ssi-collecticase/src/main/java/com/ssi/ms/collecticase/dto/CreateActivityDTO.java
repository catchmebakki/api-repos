package com.ssi.ms.collecticase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
@Getter
@Setter
public class CreateActivityDTO {

    private Long caseId;
    private Long employerId;
    private Long activityTypeCd;
    private Long remedyTypeCd;
    private Date activityDt;
    private String activityTime;
    private String activitySpecifics;
    private String activityNotes;
    private String activityNotesAdditional;
    private String activityNotesNHUIS;
    private Long communicationMethod;
    private String caseCharacteristics;
    private Long activityCmtRepCd;
    private Long activityCasePriority;
    private Date followupDt;
    private String followupShortNote;
    private String followupCompleteShortNote;
    private String callingUser;
    private String usingProgramName;
}

