package com.ssi.ms.fraudreview.dto.lookup.response;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class MailingAddress {
	private String mailAddrLine1;
	private String mailAddrLine2;
	private String mailCity;
	private String mailCountryCd;
	private String mailStateCd;
	private String mailZipCd;
	private String mailPoBox;
}