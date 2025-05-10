package com.ssi.ms.resea.dto;

import com.ssi.ms.platform.validator.EnumValidator;
import com.ssi.ms.resea.constant.ReseaConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FOLLOW_UP_COMPLETE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseTrainingDetailsDTO {
    String progName;
    String providerName;
    Date startDate;
    Date endDate;
    String exitResea;
}