package com.ssi.ms.collecticase.outputpayload;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivityWageGarnishmentPageResponse {

    ActivityGeneralPageResponse activityGeneralPageResponse;

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

    //wageAmount - showInd
    boolean disableWageAmount;

    //doNotGarnishInd - showInd
    boolean disableDoNotGarnishInd;

    //wageNonCompliance - showInd
    boolean disableWageNonCompliance;

    //wageMotionFiledOn - showInd
    boolean disableWageMotionFiledOn;

    //wageEffectiveFrom - showInd
    boolean disableWageEffectiveFrom;

    //wageEffectiveUntil - showInd
    boolean disableWageEffectiveUntil;

    //courtOrderedInd - showInd
    boolean disableCourtOrderedInd;

    //courtOrderedDate - showInd
    boolean disableCourtOrderedDate;
}
