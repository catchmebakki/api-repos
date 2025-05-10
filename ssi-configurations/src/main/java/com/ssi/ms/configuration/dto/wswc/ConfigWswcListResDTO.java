package com.ssi.ms.configuration.dto.wswc;


import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.With;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWswcListResDTO {
    private List<ConfigWswcListItemResDTO> wswcSummaryList;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
