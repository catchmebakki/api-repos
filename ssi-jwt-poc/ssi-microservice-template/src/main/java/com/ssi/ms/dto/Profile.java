package com.ssi.ms.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;


@With
@Builder
@AllArgsConstructor
@Validated
public class Profile {
        @Min(1) @NotNull
        private Long id;
        @Size(
                min = 3,
                max = 14,
                message = "The name '${validatedValue}' must be between {min} and {max} characters long"
        )
        @Pattern(regexp = "[a-zA-Z ]+")
        private String name;
        @Email
        private String email;
        private LocalDate dateOfBirth;
}
