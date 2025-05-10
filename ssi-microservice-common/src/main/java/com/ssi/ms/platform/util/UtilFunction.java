package com.ssi.ms.platform.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author Praveenraja Paramsivam
 * Interface for utility functions.
 * Suppresses PMD warnings related to field naming conventions.
 */
@SuppressWarnings("PMD.FieldNamingConventions")
public interface UtilFunction {

    BiPredicate<Long, Long> compareLongObject = (l1, l2) -> null != l1 && null != l2 && l1.longValue() == l2.longValue();
    BiPredicate<Integer, Integer> CompareIntegerObject = (l1, l2) -> null != l1 && null != l2 && l1.intValue() == l2.intValue();
    Function<String, Long> stringToLong = str ->
            StringUtils.isNotBlank(str) && StringUtils.isNumeric(str) ? Long.parseLong(str) : null;

}
