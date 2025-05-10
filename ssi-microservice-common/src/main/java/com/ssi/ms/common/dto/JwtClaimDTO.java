package com.ssi.ms.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantResDTO class for representing jwt claim data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
@ToString
public class JwtClaimDTO {
    @NotNull
    private Long userId;
    private Long claimantId;
    private Long claimId;
    private String scope;
    private Long roleId;
}
