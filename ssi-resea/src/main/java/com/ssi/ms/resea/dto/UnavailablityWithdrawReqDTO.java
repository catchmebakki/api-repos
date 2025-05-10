package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.UNAVAILABLILITY_DT_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class UnavailablityWithdrawReqDTO {
    @NotNull(message = UNAVAILABLILITY_DT_MANDATORY)
    Long unavailabilityId;
    Date withdrawDt;
    String withdrawTime;
    String notes;
}