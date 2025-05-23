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
public class GTTForOrgLookupDTO {
    private Long entityId;
    private String entityName;
    private String entityDBAName;
    private String entityUIAccNbr;
    private String entityOrigin;
    private String entitySource;
    private String entityFEINNbr;
    private String entityType;
    private String entityStatus;
}
