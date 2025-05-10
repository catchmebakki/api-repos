package com.ssi.ms.configuration.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@SuppressWarnings("PMD.FieldNamingConventions")
public interface ConfigUtilFunction {
    BiPredicate<Date, Date> IS_CONFIG_ACTIVE = (expirationDate, systemDate) -> expirationDate == null || !systemDate.after(expirationDate);

    BiPredicate<Date, Date> IS_CONFIG_NOT_REINSTATABLE = (expirationDate, systemDate) -> expirationDate == null || systemDate.before(expirationDate);

    BiFunction <Object, Object, Object> coalesce = (obj1, obj2) -> obj1 != null ? obj1 : obj2;

    static void updateErrorMap(HashMap<String, List<String>> errorMap, List<ConfigErrorEnum> errorEnums) {
        for (final var errorEnum : errorEnums) {
            errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<>());
            errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
                    .add(errorEnum.getFrontendErrorCode());
        }
    }


}
