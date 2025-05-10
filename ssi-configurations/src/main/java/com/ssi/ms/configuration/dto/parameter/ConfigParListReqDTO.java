package com.ssi.ms.configuration.dto.parameter;


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
import java.util.List;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ACTIVE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_CATEGORY_CD_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigParListReqDTO {
    @NotNull(message = PAR_CATEGORY_CD_MANDATORY)
    private List<Long> parCategoryCd;
    @EnumValidator(enumClazz = ConfigurationConstants.ACTIVE.class, message = ACTIVE_NOT_VALID_VAL)
    private String active;
    @Valid
    private PaginationDTO pagination;
    private String byName;
    private SortByDTO sortBy;
}
