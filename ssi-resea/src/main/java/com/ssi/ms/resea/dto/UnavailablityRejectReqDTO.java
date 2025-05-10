package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.END_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.END_TIME_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.REASON_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.START_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.START_TIME_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class UnavailablityRejectReqDTO {
    Long unavailabilityId;
    boolean recurring;
    @NotNull(message = REASON_MANDATORY)
    Long reason;
    @NotNull(message = START_DT_MANDATORY)
    Date startDt;
    @NotNull(message = END_DT_MANDATORY)
    Date endDt;
    @NotNull(message = START_TIME_MANDATORY)
    String startTime;
    @NotNull(message = END_TIME_MANDATORY)
    String endTime;
    String notes;
    List<Integer> days;
}