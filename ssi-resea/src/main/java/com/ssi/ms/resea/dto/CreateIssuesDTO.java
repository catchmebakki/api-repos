package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CreateIssuesDTO {
    private Long issueSubType;
    private Date startDt;
    private Date endDt;
}
