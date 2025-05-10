package com.ssi.ms.configuration.dto.alv;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALV_ID_MANDATORY;


@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class ConfigAlvReorderReqDTO {
    @NotNull(message = ALV_ID_MANDATORY)
    private List<ConfigAlvReorderItemsDTO> reorderAlvList;
}
