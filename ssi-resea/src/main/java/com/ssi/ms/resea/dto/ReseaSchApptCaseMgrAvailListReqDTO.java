package com.ssi.ms.resea.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CLAIM_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class ReseaSchApptCaseMgrAvailListReqDTO {


    @NotNull(message = CLAIM_ID_MANDATORY)
    Long clmId;
    String clmLofInd;
    String showBeyondReseaDeadlineInd;
    String meetingModeInperson;
    String meetingModeVirtual;

}
