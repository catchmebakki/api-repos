package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CLAIM_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INFORM_CLAIMANT_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class AvaliableApptSaveReqDTO {
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;
    @NotNull(message = CLAIM_ID_MANDATORY)
    Long claimId;
    @NotNull(message = INFORM_CLAIMANT_MANDATORY)
    String informedCmtInd;
    List<String> informedConveyedBy;
    String staffNotes;
    String lateStaffNote;
    boolean includeThisNoteInCNO;
}