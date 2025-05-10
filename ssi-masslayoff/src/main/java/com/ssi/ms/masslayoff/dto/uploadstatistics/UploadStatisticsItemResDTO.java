package com.ssi.ms.masslayoff.dto.uploadstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object UploadStatisticsItemResDTO class for representing a response item in upload statistics.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class UploadStatisticsItemResDTO {

    private String mslNumber;
    private String empAccNbr;
    private String empAccLoc;
    private String mslDate;
    private String mslEffDate;
    private String recallDate;

    private String museId;
    private String ssn;
    private String firstName;
    private String lastName;
    private String errorMessage;
}
