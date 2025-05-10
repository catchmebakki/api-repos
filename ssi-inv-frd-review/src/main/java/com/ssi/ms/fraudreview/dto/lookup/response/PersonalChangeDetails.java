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
public class PersonalChangeDetails {
	private String firstNameAfter;
	private String firstNameBefore;
	private String lastNameAfter;
	private String lastNameBefore;
	private String middleInitialAfter;
	private String middleInitialBefore;
	private String phoneAfter;
	private String phoneBefore;
	private String emailAfter;
	private String emailBefore;
	private String genderAfter;
	private String genderBefore;
	private String raceCodeAfter;
	private String raceCodeBefore;
	private String educationAfter;
	private String educationBefore;
	private String ethnicityAfter;
	private String ethnicityBefore;
	private String disabilityAfter;
	private String disabilityBefore;
	private String citizenIndAfter;
	private String citizenIndBefore;
	private String licenseIdAfter;
	private String licenseIdBefore;
	private String licenseStateAfter;
	private String licenseStateBefore;
	private String mailAddrLine1After;
	private String mailAddrLine1Before;
	private String mailAddrLine2After;
	private String mailAddrLine2Before;
	private String mailCityAfter;
	private String mailCityBefore;
	private String mailStateAfter;
	private String mailStateBefore;
	private Long mailCountryCdAfter;
	private Long mailCountryCdBefore;
	private String mailPoBoxAfter;
	private String mailPoBoxBefore;
	private String mailZipCdAfter;
	private String mailZipCdBefore;
	private String resAddrLine1After;
	private String resAddrLine1Before;
	private String resAddrLine2After;
	private String resAddrLine2Before;
	private String resCityAfter;
	private String resCityBefore;
	private String resStateAfter;
	private String resStateBefore;
	private Long resCountryCdAfter;
	private Long resCountryCdBefore;
	private String resPoBoxAfter;
	private String resPoBoxBefore;
	private String resZipCdAfter;
	private String resZipCdBefore;
}
