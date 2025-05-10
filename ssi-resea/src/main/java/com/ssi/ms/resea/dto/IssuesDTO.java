package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class IssuesDTO {
    private Long issueId, otherIssueId;
    private Date startDt, endDt;
}
