package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HeaderIssueDetailsDTO {
    String issueType, issueSubType;
    @JsonFormat(pattern = DATE_FORMAT)
    Date startDt, endDt, createdOn;
    String status, decStatus, decDecision;
    String createdBy;
    @JsonIgnore
    Long rsiiId, decStatusCd, decDecisionCd;
}
