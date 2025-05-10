package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.*;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ScheduleInitialApptSaveReqDTO {
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;
    @NotNull(message = CLAIM_ID_MANDATORY)
    Long claimId;
    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;

}