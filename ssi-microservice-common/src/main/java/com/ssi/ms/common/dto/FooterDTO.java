package com.ssi.ms.common.dto;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

/**
 * @author munirathnam.surepall
 *Data Transfer Object ClaimantResDTO class for representing footer data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
@NoArgsConstructor
public class FooterDTO {
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date dbDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date systemDate;
	private String applicationVersion;
}
