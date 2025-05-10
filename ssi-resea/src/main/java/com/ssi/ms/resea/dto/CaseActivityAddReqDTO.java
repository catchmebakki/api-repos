package com.ssi.ms.resea.dto;

import com.ssi.ms.platform.validator.EnumValidator;
import com.ssi.ms.resea.constant.ReseaConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseActivityAddReqDTO {
    @NotNull(message = "activityType.mandatory")
    Long activityType;
    @NotNull(message = CASE_ID_MANDATORY)
    Long caseId;
    @NotNull(message = "addDate.mandatory")
    Date addDate;
    @NotNull(message = "addTime.mandatory")
    String addTime;
    @NotNull(message = "shortDesc.mandatory")
    @Size(max = 100, message = "shortDesc.size.tooLong")
    String shortDesc;
    @NotNull(message = "details.mandatory")
    @Size(max = 4000, message = "details.size.tooLong")
    String details;
    @NotNull(message = "synopsis.mandatory")
    @Size(max = 200, message = "synopsis.size.tooLong")
    String synopsis;
    CaseTrainingDetailsDTO trainingDetails;
    CreateIssuesDTO issueDetail;
    Long terminationDetail;
    GenerateCorrespondanceDTO correspondence;
    @Size(max = 4000, message = "staffNotes.size.tooLong")
    String staffNotes;
    @EnumValidator(enumClazz = ReseaConstants.INDICATOR.class, message = "viewNoteNonReseaInd.invalid")
    String viewNoteNonReseaInd;
}