package com.ssi.ms.resea.dto;

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
public class AvailableClaimantResDTO {
    private Long id;
    private String name;
    private String officeName;
    private String beyondReseaDeadline;

    public AvailableClaimantResDTO(Long id, String name, String beyondReseaDeadline) {
        this.id = id;
        this.name = name;
        this.beyondReseaDeadline = beyondReseaDeadline;
    }
}
