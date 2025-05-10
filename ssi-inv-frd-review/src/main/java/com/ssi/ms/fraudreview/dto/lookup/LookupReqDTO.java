package com.ssi.ms.fraudreview.dto.lookup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssi.ms.fraudreview.dto.lookup.request.ClaimApplicationSource;
import com.ssi.ms.fraudreview.dto.lookup.request.DisplayCriteria;
import com.ssi.ms.fraudreview.dto.lookup.request.LookupCriteria;

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
public class LookupReqDTO {
	private Integer pageNumber;
	private Integer pageSize;
	@NotNull
	@Valid
	private ClaimApplicationSource source;
	@Valid
	private LookupCriteria lookup;
	@Valid
	private DisplayCriteria display;
}