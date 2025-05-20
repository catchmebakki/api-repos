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
    Boolean disablePPResponseToCd;

    // paymentPlanResponseToOther showInd
    Boolean disablePPResponseToOther;

    // paymentPlanGuideLineAmount showInd
    Boolean disablePPGuideLineAmount;

    // paymentPlanSignedDate showInd
    Boolean disablePPSignedDate;

    // paymentPlanFinAffidavitSignedDate showInd
    Boolean disablePPFASignedDate;

    // paymentPlanPaymentAmount showInd
    Boolean disablePPPaymentAmount;

    // paymentPlanPaymentCategory showInd
    Boolean disablePPPaymentCategory;

    // paymentPlanEffectiveUntilDate showInd
    Boolean disablePPEffUntilDate;
}
