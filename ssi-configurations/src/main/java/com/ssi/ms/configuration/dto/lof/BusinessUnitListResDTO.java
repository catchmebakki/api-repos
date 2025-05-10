package com.ssi.ms.configuration.dto.lof;

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
public class BusinessUnitListResDTO {
    private Long businessUnitCd;
    private String businessUnitName;
}
