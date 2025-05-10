package com.ssi.ms.fraudreview.dto.lookup.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class LookupDateTimeRange {
	@NotNull(message = ErrorMessageConstants.LOOKUP_OCCURED_START_DATETIME_MANDATORY)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd HH:mm")
	private LocalDateTime start;

	@NotNull(message = ErrorMessageConstants.LOOKUP_OCCURED_END_DATETIME_MANDATORY)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd HH:mm")
	private LocalDateTime end;
}