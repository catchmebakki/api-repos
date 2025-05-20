package com.ssi.ms.collecticase.dto;

import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.STAFF_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.USER_ID_NOT_FOUND;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReassignDTO {

    @NotNull(message = CASE_ID_NOT_FOUND)
    private Long caseId;

    @NotNull(message = STAFF_ID_NOT_FOUND)
    private Long staffId;

    @NotNull(message = USER_ID_NOT_FOUND)
    private String userId;
    private String casePriority;
}
