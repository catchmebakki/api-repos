package com.ssi.ms.masslayoff.dto.claimant;


import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MRLR_ID_MANDATORY;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantListReqDTO class for holding the request data needed to Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ClaimantListReqDTO {
    @NotNull(message = MRLR_ID_MANDATORY)
    private Long mrlrId;
    private String ssnNumber;
    @Valid
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
