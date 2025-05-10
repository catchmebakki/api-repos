package com.ssi.ms.fraudreview.dto.lookup.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewEnumConstant.LookupExcludeItems;
import com.ssi.ms.fraudreview.validator.FraudReviewEnumValidator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class LookupCriteria {
	@NotNull
	@Valid
	private LookupDateTimeRange occured;
	private boolean itemsAssignedToMe;
	private String assignedTo;
	@Valid
	private IncludeAttributes includeAttributes;
	@Valid
	private LimitTo limitTo;
	@Valid
	private Discrepancies discrepancies;
	@FraudReviewEnumValidator(enumClazz = LookupExcludeItems.class, message = ErrorMessageConstants.VALUE_NOT_IN_PREDEFINED_LIST)
	private String[] exclude;
}