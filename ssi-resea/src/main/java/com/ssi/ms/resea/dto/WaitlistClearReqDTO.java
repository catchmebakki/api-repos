package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MAX_CHARACTERS;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class WaitlistClearReqDTO {
    Long eventId, caseId;
    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String notes;
    boolean includeThisNoteInCNO;
}