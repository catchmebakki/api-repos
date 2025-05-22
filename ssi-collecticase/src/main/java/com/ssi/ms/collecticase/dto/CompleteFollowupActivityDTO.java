package com.ssi.ms.collecticase.dto;

import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CompleteFollowupActivityDTO {

    private String caseId;

    private String staffId;

    private String userId;

    private String activityCreatedDate;

    private String activityId;

    @NotNull(message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY)
    @Pattern(regexp = CollecticaseUtilFunction.DATE_PATTERN_MM_DD_YYYY, message =
            ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY)
    private String activityCompletedOn;

    @NotNull(message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_BY_MANDATORY)
    private String activityCompletedBy;

    private String activityCompletedNote;

}