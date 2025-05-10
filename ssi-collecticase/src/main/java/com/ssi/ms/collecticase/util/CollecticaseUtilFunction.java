package com.ssi.ms.collecticase.util;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

public interface CollecticaseUtilFunction {

    static final String FOUR_DIGIT_TWO_DEICMAL_PATTERN = "^\\d{1,4}(\\.\\d{2})?$";

    static final String UI_ACCT_NBR_PATTERN = "^[0-9]{3,10}$";

    static final String FEIN_NUMERIC_PATTERN = "^[0-9]{3,9}$";

    static final String ALPHANUMERIC_PATTERN = "^[A-Za-z0-9-,'. :;/#()&@$+]*$";

    static final String ALPHA_PATTERN = "^[A-Za-z ]*$";

    static final String PHONE_PATTERN = "^[0-9]{10,10}$";

    static final String PHONE_EXT_PATTERN = "^[0-9]{0,5}$";

    static final String NUMERIC_PATTERN = "^[0-9]*$";

    static final String US_ZIP_CODE_PATTERN = "^\\d{5}(-\\d{4})?$";

    static final String CANADA_ZIP_CODE_PQATTERN = "^[a-zA-Z]\\d[a-zA-Z] \\d[a-zA-Z]\\d$";

    // Email Pattern for multiple email address validation separated by semi-colon
    static final String EMAILS_PATTERN ="(([_a-zA-Z0-9-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*;\\s*|\\s*$))*";

    BiPredicate<BigDecimal, BigDecimal> compareBigDecimalObject = (l1, l2) -> null != l1 && null != l2 && l1.compareTo(l2) == 0;

    BiPredicate<BigDecimal, BigDecimal> lessThanBigDecimalObject = (l1, l2) -> null != l1 && null != l2 && l1.compareTo(l2) < 0;

    BiPredicate<BigDecimal, BigDecimal> greaterThanBigDecimalObject = (l1, l2) -> null != l1 && null != l2 && l1.compareTo(l2) > 0;

    BiPredicate<Long, Long> greaterThanLongObject = (l1, l2) -> null != l1 && null != l2 && l1 > l2;

    BiPredicate<Long, Long> lesserThanLongObject = (l1, l2) -> null != l1 && null != l2 && l1 < l2;

    static BiFunction<Date, Date, Long> daysBetweenStartAndEndTime() {
        return (startDate, endDate) -> {
            long daysBetween = 0;
            if (startDate != null && endDate != null) {
                daysBetween = Duration.between(startDate.toInstant(), endDate.toInstant()).toDays();
            }
            return daysBetween;
        };
    }

    static void updateErrorMap(HashMap<String, List<String>> errorMap, List<CollecticaseErrorEnum> errorEnums) {
        for (final var errorEnum : errorEnums) {
            errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<>());
            errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
                    .add(errorEnum.getFrontendErrorCode());
        }
    }

    static void updateErrorMap(Map<String, List<DynamicErrorDTO>> errorMap,
                               List<CollecticaseErrorEnum> errorEnums, List<String> errorParams) {
        int errorParamStart = 0;
        for (final var errorEnum : errorEnums) {
            errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<DynamicErrorDTO>());
            errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
                    .add(new DynamicErrorDTO(errorEnum.getFrontendErrorCode(),
                            errorEnum.getParams() == 0 ? null : errorParams
                                    .subList(errorParamStart, Math.min(errorParamStart+errorEnum.getParams(), errorParams.size()))));
            errorParamStart += errorEnum.getParams();
        }
    }

    static boolean validateRegExPattern(String pattern, String value) {
        boolean flag = false;
        if(StringUtils.isNotBlank(value))
        {
            flag = Pattern.matches(pattern, value);
        }
        return flag;
    }

    static boolean isValidZip(Long countryCd, String zipCode) {
        boolean zipValid = false;
        if (countryCd != null && StringUtils.isNotBlank(zipCode)) {
            if (UtilFunction.compareLongObject.test(countryCd, CollecticaseConstants.UNITED_STATES)) {
                zipValid = validateUsOrCanadaZip(zipCode, US_ZIP_CODE_PATTERN, 5,
                        10);
            } else {
                zipValid = validateUsOrCanadaZip(zipCode, CANADA_ZIP_CODE_PQATTERN, 7,
                        7);
            }
        }
        return zipValid;
    }

    static Boolean validateUsOrCanadaZip(String zipCode,
                                                 String matchPattern, int minAllowed, int maxAllowed) {
        Boolean zipValid = null;
        if (zipCode.trim().length() < minAllowed
                || zipCode.trim().length() > maxAllowed) {
            zipValid = false;
        } else if (Pattern.matches(matchPattern, zipCode.trim())) {
            zipValid = true;
        }
        return zipValid;
    }
}
