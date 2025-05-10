package com.ssi.ms.fraudreview.dto.lookup.request;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.DisplayTemplate;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.Extension;
import com.ssi.ms.fraudreview.validator.FraudReviewEnumValidator;
import com.ssi.ms.platform.validator.EnumValidator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@JsonInclude(Include.NON_NULL)
public class DisplayCriteria {
	@EnumValidator(enumClazz = DisplayTemplate.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String template;
	@FraudReviewEnumValidator(enumClazz = Extension.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String[] includeExtensions;
}