package com.ssi.ms.fraudreview.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

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
public class IdTheftHijackReqDTO {
	@NotNull
	@Valid
	private List<IdTheftHijackReqItemDTO> idTheftRequestItems;
}