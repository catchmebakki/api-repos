package com.ssi.ms.resea.dto;

import com.ssi.ms.platform.validator.EnumValidator;
import com.ssi.ms.resea.constant.ReseaConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.ACTIVITY_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_COMPLETE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_COMP_BY_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_COMP_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_TYPE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseActivityFollowupEditReqDTO {
    @NotNull(message = ACTIVITY_ID_MANDATORY)
    Long activityId;
    @NotNull(message = FOLLOW_UP_COMPLETE_MANDATORY)
    @EnumValidator(enumClazz = ReseaConstants.INDICATOR.class, message = "complete.invalid")
    String complete;
    //@NotNull(message = FOLLOW_UP_COMP_DT_MANDATORY)
    Date completionDt;
    @NotNull(message = STAFF_NOTES_MANDATORY)
    String notes;
}