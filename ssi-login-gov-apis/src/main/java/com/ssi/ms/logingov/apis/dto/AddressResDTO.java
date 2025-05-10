package com.ssi.ms.logingov.apis.dto;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * AddressResDTO represents the response data transfer object for addresses.
 * This class encapsulates address-related information to be sent as a response.
 * @Author: munirathnam.surepall
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class AddressResDTO {
	@JsonProperty("address1")
	private String address1;
	@JsonProperty("address2")
	private String address2;
	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;
	@JsonProperty("zipcode")
	private String zipCode;
}
