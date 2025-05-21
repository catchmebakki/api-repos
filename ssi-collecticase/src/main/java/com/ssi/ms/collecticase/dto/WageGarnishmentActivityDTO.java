package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class WageGarnishmentActivityDTO extends GeneralActivityDTO {

    private Long employerId;

    private Long employerContactId;

    private Long employerRepresentativeCd;

    private BigDecimal wageAmount;

    private String doNotGarnishInd;

    private Long wageFrequency;

    private Long wageNonCompliance;

    private Date wageMotionFiledOn;

    private Date wageEffectiveFrom;

    private Date wageEffectiveUntil;

    private Long courtId;

    private String courtOrderedInd;

    private Date courtOrderedDate;

}