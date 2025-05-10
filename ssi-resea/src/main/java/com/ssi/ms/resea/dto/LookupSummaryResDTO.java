package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class LookupSummaryResDTO {
    List<LookupSummaryDTO> summaryDTO;
    PaginationDTO pagination;
    SortByDTO sortBy;
}