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
public class BankDetails {
	private String bankAccount;
	private String bankRouting;
	private String bankState;
}