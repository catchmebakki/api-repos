package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.ACTIVITY_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_COMP_BY_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_TYPE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseActivityFollowupAddReqDTO {
    @NotNull(message = ACTIVITY_ID_MANDATORY)
    Long activityId;
    @NotNull(message = FOLLOW_UP_TYPE_MANDATORY)
    Long followUpType;
    @NotNull(message = FOLLOW_UP_COMP_BY_DT_MANDATORY)
    Date followUpCompByDt;
    @NotNull(message = STAFF_NOTES_MANDATORY)
    String notes;
}