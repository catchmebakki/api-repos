package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class OfficeResDTO {
    private Long officeNum;
    private String officeName;
}
