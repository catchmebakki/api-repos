package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_MANAGER_ID_MANDATORY;
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
public class ReseaReassignAllReqDTO {
    @NotNull(message = CASE_MANAGER_ID_MANDATORY)
    Long caseManagerId;
    @NotNull(message = "reassignDt.mandatory")
    @JsonFormat(pattern = "MM/dd/yyyy")
    Date reassignDt;
    @JsonFormat(pattern = "MM/dd/yyyy")
    Date reassignEndDt;
    @NotNull(message = REASSIGN_REASON_MANDATORY)
    Long reassignReasonCd;
    @NotNull(message = "limitOffice.mandatory")
    @EnumValidator(enumClazz = ReseaConstants.INDICATOR.class, message = "limitOffice.invalid")
    String limitOffice;
    @NotNull(message = STAFF_NOTES_MANDATORY)
    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;
}