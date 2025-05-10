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
public class StateResDTO {
    private String  staCd;
    private Long staFipsCd;
    private String staDescTxt;
    private Long staSortOrderNbr;
    private String staProvinceInd;
}
