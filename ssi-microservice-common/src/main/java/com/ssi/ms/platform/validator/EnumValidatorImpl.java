package com.ssi.ms.platform.validator;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Praveenraja Paramsivam
 * Custom ConstraintValidator implementation for validating if a string value matches a valid enum constant.
 */
public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
    private List<String> valueList = null;
    /**
     * Validates whether the provided value meets the constraints defined by this validator.
     * @param value {@link String} The value to be validated.
     * @param context {@link ConstraintValidatorContext} The context in which the constraint is evaluated.
     * @return {@link boolean} True if the value is valid according to the constraints, otherwise false.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(value.toUpperCase());
    }
    /**
     * Initializes the validator with the constraint annotation.
     * @param constraintAnnotation {@link EnumValidator} The EnumValidator annotation instance.
     */
    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = of(constraintAnnotation.enumClazz().getEnumConstants()).map(e -> e.toString().toUpperCase())
                .collect(toList());
    }
}