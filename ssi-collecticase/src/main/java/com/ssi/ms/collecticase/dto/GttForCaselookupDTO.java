package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class GttForCaselookupDTO {

    private String assignedTo;
    private Long caseNum;
    private String caseStatusDesc;
    private String cmtName;
    private String cmtSsn;
    private String lastRemedy;
    private String nextFollowUp;
    private String opType;
    private String priority;
}
