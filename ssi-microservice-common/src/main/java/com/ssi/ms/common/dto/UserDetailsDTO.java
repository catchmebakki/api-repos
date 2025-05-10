package com.ssi.ms.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import com.ssi.ms.constant.AlvLogConstant.SRLAccessCd;
import com.ssi.ms.platform.validator.EnumValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.ssi.ms.constant.CommonConstant.ACCESSCDVALUE_NOT_VALID;

import java.time.LocalDate;


/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object ClaimantResDTO class for representing user details data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class UserDetailsDTO {
    @Min(1)
    @NotNull
    private Long userId;
    @Size(
            min = 3,
            max = 14,
            message = "The name '${validatedValue}' must be between {min} and {max} characters long"
    )
    @Pattern(regexp = "[a-zA-Z ]+")
    private String firstName;
    @Pattern(regexp = "[a-zA-Z ]+")
    private String lastName;
    @Email
    private String email;
    private LocalDate dateOfBirth;
    @EnumValidator(enumClazz = SRLAccessCd.class, message = ACCESSCDVALUE_NOT_VALID)
    private String srlAccessCdValue;
}
