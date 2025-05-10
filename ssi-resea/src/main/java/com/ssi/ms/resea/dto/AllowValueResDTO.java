package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AllowValueResDTO {
    private Long id;
    private String desc;
    @JsonIgnore
    private String decipherCode;
}
