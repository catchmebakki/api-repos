package com.ssi.ms.logingov.apis.dto;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Represents the response DTO for getting decrypted claims. This DTO contains
 * the decrypted claim data.
 * 
 * @Author: munirathnam.surepall
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class ClaimsResDTO {
	@JsonProperty("id")
	private String claimId;
	@JsonProperty("swa_xid")
	private String swaXid;
	@JsonProperty("claimant_id")
	private String claimantId;
	@JsonProperty("identity_provider")
	private String identityProvider;
	@JsonProperty("identity_assurance_level")
	private int identityAssuranceLevel;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("ssn")
	private String ssn;
	@JsonProperty("birthdate")
	private String birthdate;
	@JsonProperty("address")
	private AddressResDTO addressResDTO;
	@JsonProperty("phone")
	private String phone;
	@JsonProperty("swa_code")
	private String swaCode;
	@JsonProperty("email")
	private String email;
	@JsonProperty("all_emails")
	private String allEmails[];
	@JsonProperty("validated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date validatedAt;
	@JsonProperty("verified_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date verifiedAt;
	@JsonProperty("$schema")
	private String schema;

}
