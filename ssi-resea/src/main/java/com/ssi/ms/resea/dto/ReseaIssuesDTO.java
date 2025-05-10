package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaIssuesDTO {
    private boolean selected, editable;
    private Long issueType, issueSubType;
    @JsonFormat(pattern = DATE_FORMAT)
    private Date startDt, endDt;
    private Long issueId, otherIssueId;
}
