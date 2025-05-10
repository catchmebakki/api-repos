package com.ssi.ms.fraudreview.dto;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;

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
public class IdTheftHijackReqItemDTO {
	private boolean hasActiveBenefitYear;
	@NotNull(message = ErrorMessageConstants.CLAIMANT_ID_MANDATORY)
	private Long cmtId;
	private Long capId;
	@NotNull
	private Long clmId;
	private Long cmtiId;
}