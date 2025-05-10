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
public class ResidenceAddress {
	private String resAddrLine1;
	private String resAddrLine2;
	private String resCity;
	private String resCountryCd;
	private String resStateCd;
	private String resZipCd;
	private String resPoBox;
}