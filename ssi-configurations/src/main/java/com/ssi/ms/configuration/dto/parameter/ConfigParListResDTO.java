package com.ssi.ms.configuration.dto.parameter;


import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigParListResDTO {
    private List<ConfigParListItemResDTO> parameterParList;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
