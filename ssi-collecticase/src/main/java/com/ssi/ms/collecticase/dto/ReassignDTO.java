package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.STAFF_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReassignDTO {
    private Long caseId;

    @NotNull(message = STAFF_ID_MANDATORY)
    private Long staffId;
    private String userId;
    private String casePriority;
}
