package com.ssi.ms.collecticase.util;

import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.dto.PaymentPlanActivityDTO;
import com.ssi.ms.collecticase.dto.UpdateContactActivityDTO;
import com.ssi.ms.collecticase.dto.WageGarnishmentActivityDTO;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import com.ssi.ms.collecticase.validator.PaymentPlanActivityValidator;
import com.ssi.ms.collecticase.validator.UpdateContactActivityValidator;
import com.ssi.ms.collecticase.validator.WageGarnishmentValidator;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;

import java.util.List;
import java.util.Map;

public class ValidationHelper {

    public static void validateGeneralActivity(GeneralActivityValidator generalActivityValidator,
                                               GeneralActivityDTO generalActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = generalActivityValidator
                .validateGeneralActivity(generalActivityDTO);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException("Activity General Page POST call Errors", errorMap);
        }
    }

    public static void validatePaymentPlanActivity(PaymentPlanActivityValidator paymentPlanActivityValidator,
                                                   PaymentPlanActivityDTO paymentPlanActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = paymentPlanActivityValidator
                .validatePaymentPlanActivity(paymentPlanActivityDTO);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException("Activity PaymentPlan POST call Errors", errorMap);
        }
    }

    public static void validateWageGarnishActivity(WageGarnishmentValidator wageGarnishmentValidator,
                                                   WageGarnishmentActivityDTO wageGarnishmentActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = wageGarnishmentValidator
                .validateWageGarnishmentActivity(wageGarnishmentActivityDTO);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException("Activity WageGarnish POST call Errors", errorMap);
        }
    }

    public static void validateUpdateContactActivity(UpdateContactActivityValidator updateContactActivityValidator,
                                                     UpdateContactActivityDTO updateContactActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = updateContactActivityValidator
                .validateUpdateContactActivity(updateContactActivityDTO);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException("Activity UpdateContact POST call Errors", errorMap);
        }
    }
}
