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

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CLAIM_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MAX_CHARACTERS;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class WaitlistReqDTO {
    Long eventId, caseId;
    @NotNull(message = "waitlist.mandatory")
    @EnumValidator(enumClazz = ReseaConstants.INDICATOR.class, message = "waitlist.invalid")
    String waitlist;
    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String notes;
    boolean includeThisNoteInCNO;
}