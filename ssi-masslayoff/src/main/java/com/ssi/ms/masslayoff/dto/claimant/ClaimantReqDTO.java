package com.ssi.ms.masslayoff.dto.claimant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.SSN_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.SSN_PATTEN_NOT_MATCHING;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantReqDTO class for holding the request data needed to Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ClaimantReqDTO {
    @Pattern(regexp = "^[0-9]{9}$", message = SSN_PATTEN_NOT_MATCHING)
    @NotNull(message = SSN_MANDATORY)
    private String ssn;
    private Long validateMrlrlId;
}
