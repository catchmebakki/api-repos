package com.ssi.ms.fraudreview.dto.caseassign;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CaseAssignErrorMessage {
	private CaseAssignErrorMessageItem[] messages;
	private CaseAssignErrorMessageItem[] exceptions;
}
