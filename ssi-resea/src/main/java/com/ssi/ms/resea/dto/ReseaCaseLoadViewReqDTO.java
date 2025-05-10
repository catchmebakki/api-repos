package com.ssi.ms.resea.dto;


import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaCaseLoadViewReqDTO {
    String metric;
    @NotNull(message = USR_ID_MANDATORY)
    Long userId;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
