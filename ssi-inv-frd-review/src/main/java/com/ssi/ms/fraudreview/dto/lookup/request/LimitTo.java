package com.ssi.ms.fraudreview.dto.lookup.request;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.AutoCreatedSpideringIssues;
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
public class LimitTo {
	@Valid
	private ChangesToKeyInfo changesSinceLastFiling;
	private boolean outOfStateBanks;
	private boolean outOfStatePhoneNo;
	private boolean outOfStateLicense;
	private boolean outOfStateAddress;
	private String checkAWWGMultiplesOf;
	@FraudReviewEnumValidator(enumClazz = AutoCreatedSpideringIssues.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String[] autoCreatedSpideringIssue;
}