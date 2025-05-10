package com.ssi.ms.configuration.dto.alv;


import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ACTIVE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALC_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigAlvListReqDTO {
    @NotNull(message = ALC_ID_MANDATORY)
    private Long alcId;

    @EnumValidator(enumClazz = ConfigurationConstants.ACTIVE.class, message = ACTIVE_NOT_VALID_VAL)
    private String active;
    @Valid
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
