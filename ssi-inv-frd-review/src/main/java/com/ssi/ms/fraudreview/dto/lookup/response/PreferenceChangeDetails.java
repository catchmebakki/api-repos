package com.ssi.ms.fraudreview.dto.lookup.response;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PreferenceChangeDetails {
	private String bankAccountAfter;
	private String bankAccountBefore;
	private String bankRoutingAfter;
	private String bankRoutingBefore;
	private String bankStateAfter;
	private String bankStateBefore;
	private String paymentPrefBefore;
	private String paymentPrefAfter;
	private String taxWithholdPrefAfter;
	private String taxWithholdPrefBefore;
	private String correspondencePrefAfter;
	private String correspondencePrefBefore;
}