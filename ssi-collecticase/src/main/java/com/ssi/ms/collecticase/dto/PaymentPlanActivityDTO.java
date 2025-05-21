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
public class PaymentPlanActivityDTO extends GeneralActivityDTO {

    private Long paymentPlanResponseToCd;

    private String paymentPlanResponseToOther;

    private BigDecimal paymentPlanGuideLineAmount;

    private Date paymentPlanSignedDate;

    private Date paymentPlanFinAffidavitSignedDate;

    private BigDecimal paymentPlanPaymentAmount;

    private Long paymentPlanPaymentCategory;

    private Date paymentPlanEffectiveUntilDate;

    private Long paymentPlanMonths;
}
