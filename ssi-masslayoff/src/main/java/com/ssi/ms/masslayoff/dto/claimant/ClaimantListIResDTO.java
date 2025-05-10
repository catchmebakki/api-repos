package com.ssi.ms.masslayoff.dto.claimant;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.List;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantListIResDTO class for representing claimant response data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ClaimantListIResDTO {
    private List<ClaimantListItemResDTO> claimantList;
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
