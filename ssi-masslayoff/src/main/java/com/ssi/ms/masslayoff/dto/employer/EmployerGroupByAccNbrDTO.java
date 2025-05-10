package com.ssi.ms.masslayoff.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.List;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object EmployerGroupByAccNbrDTO class for holding employer detail information.
 */
@With
@Builder
@AllArgsConstructor
@Getter
@Setter
public class EmployerGroupByAccNbrDTO {
    private String empUiAcctNbr;
    private List<EmployerDetailDTO> details;
}

