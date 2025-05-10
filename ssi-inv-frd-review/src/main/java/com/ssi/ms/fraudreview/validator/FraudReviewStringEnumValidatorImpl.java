package com.ssi.ms.fraudreview.validator;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ssi.ms.platform.validator.EnumValidator;

public class FraudReviewStringEnumValidatorImpl implements ConstraintValidator<FraudReviewEnumValidator, String> {
    private List<String> enumValueList = null;

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
    	return inputValue != null ? enumValueList.contains(inputValue.toUpperCase()) : true;
    }

    /**
     * Initializes the validator with the constraint annotation.
     * @param constraintAnnotation {@link EnumValidator} The EnumValidator annotation instance.
     */
    @Override
    public void initialize(FraudReviewEnumValidator constraintAnnotation) {
        enumValueList = of(constraintAnnotation.enumClazz().getEnumConstants()).map(e -> e.toString().toUpperCase())
                .collect(toList());
    }
}