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
public class Demographics {
	private String citizenInd;
	private String disability;
	private String education;
	private String ethnicity;
	private String gender;
	private String raceCode;
}