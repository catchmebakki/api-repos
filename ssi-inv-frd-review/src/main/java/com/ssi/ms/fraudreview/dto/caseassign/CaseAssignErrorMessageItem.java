package com.ssi.ms.fraudreview.dto.caseassign;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CaseAssignErrorMessageItem {

	@JsonProperty("cmt_id")
	private Long cmtId;
	private String message;
}
