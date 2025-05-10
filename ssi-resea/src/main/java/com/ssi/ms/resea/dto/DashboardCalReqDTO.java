package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.END_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.START_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class DashboardCalReqDTO {
    @NotNull(message = USR_ID_MANDATORY)
    Long userId;
    @NotNull(message = START_DT_MANDATORY)
    Date startDt;
    @NotNull(message = END_DT_MANDATORY)
    Date endDt;
}
