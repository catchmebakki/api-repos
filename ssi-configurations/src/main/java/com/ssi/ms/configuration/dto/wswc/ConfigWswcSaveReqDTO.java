package com.ssi.ms.configuration.dto.wswc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.COMMENTS_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_DATE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WSWC_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWswcSaveReqDTO {

    @NotNull(message = WSWC_ID_MANDATORY)
    private Long wswcId;

    @NotNull(message = MODIFICATION_TYPE_MANDATORY)
    @EnumValidator(enumClazz = ConfigurationConstants.WSWCMODIFICATIONTYPE.class, message = MODIFICATION_TYPE_NOT_VALID_VAL)
    private String modificationType;

    @NotNull(message = MODIFICATION_DATE_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate modificationDate;

    @NotNull(message = COMMENTS_MANDATORY)
    private String comments;

    private String autoOverwrite;

    private Long reasonCd;

    private Long businessUnit;
}
