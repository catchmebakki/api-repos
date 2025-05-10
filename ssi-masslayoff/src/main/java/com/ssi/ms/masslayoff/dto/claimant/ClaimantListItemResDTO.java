package com.ssi.ms.masslayoff.dto.claimant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantListItemResDTO class for representing claimant response data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ClaimantListItemResDTO {
    private Long mlecId;
    private String firstName;
    private String lastName;
    private String ssnNumber;
    private Integer sourceCd;
    private Integer statusCd;
    private String sourceCdValue;
    private String statusCdValue;
}
