package com.ssi.ms.collecticase.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;


@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class OrgLookupDTO {

    private String orgName;
    private String uiAcctNbr;
    private String fein;
    private String entityType;
    private Long caseId;


}
