package com.ssi.ms.logingov.apis.dto;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

/**
 * LoginGovResDTO represents the response data transfer object (DTO) for
 * LoginGov authentication. This class encapsulates the data returned from the
 * LoginGov authentication process.
 *
 * @Author: munirathnam.surepall
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
@NoArgsConstructor
public class UspsIppResDTO {
	@JsonProperty("swa_xid")
	private String swaXID;
	@JsonProperty("records")
	private List<SwaRecordsResDTO> swaRecordsResDTO;
	@JsonProperty("error_message")
	private String errorMessage;

}
