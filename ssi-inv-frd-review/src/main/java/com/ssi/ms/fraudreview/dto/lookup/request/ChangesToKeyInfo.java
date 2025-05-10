package com.ssi.ms.fraudreview.dto.lookup.request;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.PaymentPreference;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.PersonalInfoChanges;
import com.ssi.ms.fraudreview.validator.FraudReviewEnumValidator;

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
public class ChangesToKeyInfo {
	@FraudReviewEnumValidator(enumClazz = PersonalInfoChanges.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String[] personal;
	@FraudReviewEnumValidator(enumClazz = PaymentPreference.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String[] preference;
	private Boolean primaryOccup;
}