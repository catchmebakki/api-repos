package com.ssi.ms.fraudreview.validator;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FraudReviewStrArrayEnumValidatorImpl implements ConstraintValidator<FraudReviewEnumValidator, String[]> {
    private List<String> enumValueList = null;

    @Override
    public boolean isValid(String[] inputValues, ConstraintValidatorContext context) {
    	final Predicate<String> valueInlist = inputValue -> enumValueList.contains(inputValue.toUpperCase());
    	return inputValues != null ? Arrays.asList(inputValues).stream().allMatch(valueInlist) : true;
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