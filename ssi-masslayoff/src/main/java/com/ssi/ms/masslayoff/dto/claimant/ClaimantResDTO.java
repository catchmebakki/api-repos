package com.ssi.ms.masslayoff.dto.claimant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantResDTO class for representing claimant response data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ClaimantResDTO {

    private Long cmtId;
    private String ssn;
    private String firstName;
    private String lastName;

}
