package com.ssi.ms.configuration.dto.parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static com.ssi.ms.configuration.validator.ParValidationPredicate.isDateTypeForParameter;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigParListItemResDTO {
    private Long parId;
    private String name;
    private String parShortName;
    private String numericValue, textValue, dateValue;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;
    private int childListCount;
    private boolean editFlag, deleteFlag, reinstateFlag;

    public void setNumericValue(String numericValue) {
        this.numericValue = numericValue;
    }
    public ConfigParListItemResDTO(Long parId, String name, String parShortName, BigDecimal numericValue, String alphaValue,
                                   Date startDate, Date endDate, int childListCount, int reinstate) {
        this.parId = parId;
        this.name = name;
        this.parShortName = parShortName;
        if (numericValue != null) {
            this.numericValue = String.valueOf(numericValue);
        }
        if (isDateTypeForParameter.test(alphaValue)) {
            this.dateValue = alphaValue;
        } else {
            this.textValue = StringUtils.trimToNull(alphaValue);
        }
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
        this.childListCount = childListCount;
        this.reinstateFlag = reinstate == 0;
    }

    /*
     * This constructor is used in getting ChildList, this is not an unused constructor, please do not delete.
     * */
    public ConfigParListItemResDTO(Long parId, String name, String parShortName, BigDecimal numericValue,
                                   String alphaValue, Date startDate, Date endDate) {
        this.parId = parId;
        this.name = name;
        this.parShortName = parShortName;
        if (numericValue != null) {
            this.numericValue = String.valueOf(numericValue);
        }
        if (isDateTypeForParameter.test(alphaValue)) {
            this.dateValue = alphaValue;
        } else {
            this.textValue = StringUtils.trimToNull(alphaValue);
        }
        this.dateValue = isDateTypeForParameter.test(alphaValue) ? alphaValue : null;
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
    }
}
