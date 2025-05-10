package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STATUS_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class AvaliableApptReqDTO {
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;
    Long userId; // When selected Local Office, value should be -1
    @NotNull(message = STATUS_MANDATORY)
    String status;
}