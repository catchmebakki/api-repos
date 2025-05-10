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
public class Address {
	private ResidenceAddress residence;
	private MailingAddress mailing;
}