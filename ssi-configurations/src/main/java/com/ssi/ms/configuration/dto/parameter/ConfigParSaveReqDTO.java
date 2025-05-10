package com.ssi.ms.configuration.dto.parameter;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_DT_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.NAME_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_ID_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_REMARKS_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigParSaveReqDTO {
    @NotNull(message = PAR_ID_MANDATORY)
    private Long parId;

    @NotNull(message = MODIFICATION_TYPE_MANDATORY)
    @EnumValidator(enumClazz = ConfigurationConstants.PARMODIFICATIONTYPE.class, message = MODIFICATION_TYPE_NOT_VALID_VAL)
    private String modificationType;

    @NotNull(message = MODIFICATION_DT_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate modificationDt;

    @NotNull(message = NAME_MANDATORY)
    private String name;

    private BigDecimal numericValue;

    private String textValue;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateValue;

    @NotNull(message = PAR_REMARKS_MANDATORY)
    private String remarks;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate effectiveUntilDt;
}
