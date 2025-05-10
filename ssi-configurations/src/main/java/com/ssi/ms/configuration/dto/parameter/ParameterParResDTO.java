package com.ssi.ms.configuration.dto.parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_ID_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_MAME_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_REMARKS_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ParameterParResDTO {
    @NotNull(message = PAR_ID_MANDATORY)
    private Long parId;

    @NotNull(message = PAR_MAME_MANDATORY)
    private String parLongName;

    private Integer parCategoryCd;

    private String parCategoryCdValue;

    private boolean numericType, textType, dateType;

    private BigDecimal numericValue;

    private String textValue, dateValue;

    private String parShortName;


    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate parEffectiveDate;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate parExpirationDate;

    @NotNull(message = PAR_REMARKS_MANDATORY)
    private String parRemarks;

    private boolean modTypeChangeFlag, modTypeEndDateFlag, modTypeReinstateFlag, openEndedExistFlag;
}
