package com.ssi.ms.masslayoff.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object EmployerDetailDTO class for holding employer detail information.
 */
@With
@Builder
@AllArgsConstructor
@Getter
@Setter
public class EmployerDetailDTO {
    private Long empId;
    private String name;
    private String loc;
}
