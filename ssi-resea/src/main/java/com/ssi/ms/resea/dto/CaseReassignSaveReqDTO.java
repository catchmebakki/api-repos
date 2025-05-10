package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseReassignSaveReqDTO {
    @NotNull(message = CASE_ID_MANDATORY)
    Long caseId;
    Long reassignReason;
    String staffNotes;
}