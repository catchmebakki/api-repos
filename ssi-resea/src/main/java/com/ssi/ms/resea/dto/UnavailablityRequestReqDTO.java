package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CLAIM_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.END_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.END_TIME_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INFORM_CLAIMANT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.REASON_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.START_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.START_TIME_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UNAVAILABILITY_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class UnavailablityRequestReqDTO {
    @NotNull(message = USR_ID_MANDATORY)
    Long userId;
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