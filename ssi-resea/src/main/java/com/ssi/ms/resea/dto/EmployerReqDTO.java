package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@Validated
@Getter
public class EmployerReqDTO {
    String empName;
    public EmployerReqDTO(@JsonProperty("empName") String empName) {
        this.empName = empName;
    }
}
