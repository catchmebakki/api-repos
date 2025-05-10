package com.ssi.ms.masslayoff.dto.claimant;


import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MslClaimantSourceCd;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.CLAIMANT_SOURCE_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.CLAIMANT_SOURCE_NOT_VALID;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.FIRST_NAME_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.LAST_NAME_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MRLR_ID_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.SSN_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.SSN_PATTEN_NOT_MATCHING;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object MrecClaimantReqDTO class for holding the request data needed to Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class MrecClaimantReqDTO {
    @NotNull(message = MRLR_ID_MANDATORY)
    private Long mlrlId;
    @Pattern(regexp = "^[0-9]{9}$", message = SSN_PATTEN_NOT_MATCHING)
    @NotNull(message = SSN_MANDATORY)
    private String ssn;
    @NotNull(message = FIRST_NAME_MANDATORY)
    private String firstName;
    @NotNull(message = LAST_NAME_MANDATORY)
    private String lastName;
    @NotNull(message = CLAIMANT_SOURCE_MANDATORY)
    @EnumValidator(enumClazz = MslClaimantSourceCd.class, message = CLAIMANT_SOURCE_NOT_VALID)
    private String sourceCdValue;
}
