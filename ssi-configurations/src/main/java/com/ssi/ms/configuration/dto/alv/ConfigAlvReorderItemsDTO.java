package com.ssi.ms.configuration.dto.alv;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALV_ID_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALV_SORT_ORDER_MANDATORY;


@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigAlvReorderItemsDTO {
    @NotNull(message = ALV_ID_MANDATORY)
    private Long alvId;
    @NotNull(message = ALV_SORT_ORDER_MANDATORY)
    private Long alvSortOrderNbr;
}
