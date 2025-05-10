package com.ssi.ms.configuration.dto.wscc;


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

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ACTIVE_NOT_VALID_VAL;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWsccListReqDTO {
    @EnumValidator(enumClazz = ConfigurationConstants.ACTIVE.class, message = ACTIVE_NOT_VALID_VAL)
    private String active;
    @Valid
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
