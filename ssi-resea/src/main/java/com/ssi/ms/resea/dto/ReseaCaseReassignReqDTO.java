package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_OFFICE_IND_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.REASSIGN_REASON_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MAX_CHARACTERS;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaCaseReassignReqDTO {
    @NotNull(message = CASE_OFFICE_IND_MANDATORY)
    String caseOffice;
    @NotNull(message = CASE_ID_MANDATORY)
    Long caseId;
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;
    @NotNull(message = REASSIGN_REASON_MANDATORY)
    Long reassignReasonCd;
    @NotNull(message = STAFF_NOTES_MANDATORY)
    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;
    boolean includeThisNoteInCNO;
}