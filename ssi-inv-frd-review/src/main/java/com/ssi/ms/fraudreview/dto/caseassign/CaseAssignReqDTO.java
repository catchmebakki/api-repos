package com.ssi.ms.fraudreview.dto.caseassign;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;

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
@Validated
@Getter
@Setter
public class CaseAssignReqDTO {
	@NotNull(message = ErrorMessageConstants.ASSIGN_TO_MANDATORY)
	private Long assignTo;
	private String[] lookupIds;
	@NotNull(message = ErrorMessageConstants.CLAIMANT_LIST_MANDATORY)
	private Long[] cmtList;
}