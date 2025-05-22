package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import java.util.Date;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CcaseCmaNoticesCmnDTO {

    private Long caseCorrespondenceId;

    private Date caseCorrespondenceMailDate;

    private Timestamp caseCorrespondenceTimeStamp;

    private String caseCorrespondenceName;

    private String caseCorrespondenceFormNBR;
}
