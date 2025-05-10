package com.ssi.ms.configuration.dto.alv;


import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.platform.validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.HashSet;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALV_ID_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.COMMENTS_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.DISPLAY_ON_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.NAME_MANDATORY;


@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigAlvSaveReqDTO {
    @NotNull(message = ALV_ID_MANDATORY)
    private Long alvId;

    @NotNull(message = MODIFICATION_TYPE_MANDATORY)
    @EnumValidator(enumClazz = ConfigurationConstants.ALVMODIFICATIONTYPE.class, message = MODIFICATION_TYPE_NOT_VALID_VAL)
    private String modificationType;

    @NotNull(message = NAME_MANDATORY)
    private String name;

    private String spanishName;

    private String alvDecipherCd;

    @NotNull(message = DISPLAY_ON_MANDATORY)
    private HashSet<Long> displayOnList;

    private String description;

    @NotNull(message = COMMENTS_MANDATORY)
    private String comments;
}
