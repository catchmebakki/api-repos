package com.ssi.ms.configuration.dto.alv;


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
public class ConfigAlvListResDTO {
    private List<AllowValAlvResDTO> allowValAlvList;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
