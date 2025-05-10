package com.ssi.ms.configuration.dto.wscc;

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

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ADDITIONAL_CLAIM_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.COMMENTS_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.INCREMENT_FREQ_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.INCREMENT_VALUE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.INITIAL_CLAIM_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_DATE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.MODIFICATION_TYPE_NOT_VALID_VAL;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WSCC_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWsccSaveReqDTO {

    @NotNull(message = WSCC_ID_MANDATORY)
    private Long wsccId;

    @NotNull(message = MODIFICATION_TYPE_MANDATORY)
    @EnumValidator(enumClazz = ConfigurationConstants.WSCCMODIFICATIONTYPE.class, message = MODIFICATION_TYPE_NOT_VALID_VAL)
    private String modificationType;

    @NotNull(message = INITIAL_CLAIM_MANDATORY)
    private Long initialClaim;

    @NotNull(message = ADDITIONAL_CLAIM_MANDATORY)
    private Long additionalClaim;

    @NotNull(message = INCREMENT_FREQ_MANDATORY)
    private Long incrementFrequency;

    @NotNull(message = INCREMENT_VALUE_MANDATORY)
    private Long incrementVal;

    @NotNull(message = MODIFICATION_DATE_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate modificationDate;

    @NotNull(message = COMMENTS_MANDATORY)
    private String comments;
}
