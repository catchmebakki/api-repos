package com.ssi.ms.resea.dto;


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
public class ReseaCaseLoadViewResDTO {
    List<CaseLoadSummaryResDTO> caseLoadSummaryList;
    private boolean reassignInd;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
