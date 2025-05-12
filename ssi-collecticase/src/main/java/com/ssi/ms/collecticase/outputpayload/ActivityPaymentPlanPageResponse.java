package com.ssi.ms.collecticase.outputpayload;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivityPaymentPlanPageResponse {

    ActivityGeneralPageResponse activityGeneralPageResponse;

    private Long paymentPlanResponseToCd;

    private String paymentPlanResponseToOther;

    private BigDecimal paymentPlanGuideLineAmount;

    private Date paymentPlanSignedDate;

    private Date paymentPlanFinAffidavitSignedDate;

    private BigDecimal paymentPlanPaymentAmount;

    private Long paymentPlanPaymentCategory;

    private Date paymentPlanEffectiveUntilDate;

    private Long paymentPlanMonths;

    // paymentPlanResponseToCd showInd
    boolean disablePPResponseToCd;

    // paymentPlanResponseToOther showInd
    boolean disablePPResponseToOther;

    // paymentPlanGuideLineAmount showInd
    boolean disablePPGuideLineAmount;

    // paymentPlanSignedDate showInd
    boolean disablePPSignedDate;

    // paymentPlanFinAffidavitSignedDate showInd
    boolean disablePPFASignedDate;

    // paymentPlanPaymentAmount showInd
    boolean disablePPPaymentAmount;

    // paymentPlanPaymentCategory showInd
    boolean disablePPPaymentCategory;

    // paymentPlanEffectiveUntilDate showInd
    boolean disablePPEffUntilDate;
}
