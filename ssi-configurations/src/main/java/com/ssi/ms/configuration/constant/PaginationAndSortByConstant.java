package com.ssi.ms.configuration.constant;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.ssi.ms.constant.CommonConstant.DEFAULT;

public interface PaginationAndSortByConstant {
    String ASCENDING = "ASC";

    Supplier<PaginationDTO> GET_DEFAULT_PAGINATION = () -> PaginationDTO.builder()
            .pageNumber(1)
            .pageSize(Integer.MAX_VALUE)
            .needTotalCount(true)
            .build();

    Function<Map<String, String>, SortByDTO> GET_DEFAULT_SORT_BY = (mapping) -> SortByDTO.builder()
            .field(mapping.get(DEFAULT))
            .direction(ASCENDING)
            .build();

    Map<String, String> PARAMETER_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "PAR_NAME"),
            Map.entry("name", "PAR_NAME"));
    Map<String, String> WSWC_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("wswcId", "WSWC_ID"));

    Map<String, String> WSWCC_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("wsccId", "WSWCC_ID"));
}
