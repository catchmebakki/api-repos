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
public class NameDetails {
	private String firstName;
	private String lastName;
	private String loginName;
	private String middleInitial;
	private String last4SSN;
}