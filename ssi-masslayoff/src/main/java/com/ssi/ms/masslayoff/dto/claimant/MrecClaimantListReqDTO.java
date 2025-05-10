package com.ssi.ms.masslayoff.dto.claimant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.CLAIMANT_LIST_MANDATORY;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object MrecClaimantListReqDTO class for holding the request data needed to Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class MrecClaimantListReqDTO {
    @NotNull(message = CLAIMANT_LIST_MANDATORY)
    private List<Long> mlecIdList;
}
