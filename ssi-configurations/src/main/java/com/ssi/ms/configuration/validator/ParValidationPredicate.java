package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.constant.ConfigurationConstants;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("PMD.FieldNamingConventions")
public interface ParValidationPredicate {
    Predicate<BigDecimal> isNumericTypeForParameter = numericValue -> Optional.ofNullable(numericValue)
            .isPresent();
    Predicate<String> isTextTypeForParameter = textValue -> Optional.ofNullable(textValue)
            .map(str -> !str.matches(ConfigurationConstants.PAR_DATE_FORMAT_VALIDATOR)).orElseGet(() -> false);
    Predicate<String> isDateTypeForParameter = dateValue -> Optional.ofNullable(dateValue)
            .map(str -> str.matches(ConfigurationConstants.PAR_DATE_FORMAT_VALIDATOR)).orElseGet(() -> false);

}
