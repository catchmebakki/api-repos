package com.ssi.ms.masslayoff.dto.lookup;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.List;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object LookupListResDTO class for representing a response list in a lookup.
 */
@With
@Builder
@AllArgsConstructor
@Getter
public class LookupListResDTO {
    private PaginationDTO pagination;
    private SortByDTO sortBy;
    private List<LookupItemResDTO> massLayoffList;
}
