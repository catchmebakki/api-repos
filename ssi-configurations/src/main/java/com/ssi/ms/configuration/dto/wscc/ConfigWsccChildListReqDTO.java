package com.ssi.ms.configuration.dto.wscc;


import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ACTIVE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WSCC_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWsccChildListReqDTO {
    @NotNull(message = WSCC_ID_MANDATORY)
    private Long wsccId;
    @EnumValidator(enumClazz = ConfigurationConstants.ACTIVE.class, message = ACTIVE_NOT_VALID_VAL)
    private String active;
}
