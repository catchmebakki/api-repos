package com.ssi.ms.configuration.dto.alc;

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
public class AllowCatAlcResDTO {
    private Long alcId;
    private String alcName;
    private String alcDescTxt;
    private String alcDecipherLabel;
}
