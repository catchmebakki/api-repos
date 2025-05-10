package com.ssi.ms.fraudreview.dto.lookup.request;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_NULL)
public class IncludeAttributes {
	private String bankRouting;
	private String bankAccountNo;
	private String email;
	private String phoneNo;
	private String licenseId;
	private String employerName;
	private String uiAcctNo;
	private String ipAddress;

}
