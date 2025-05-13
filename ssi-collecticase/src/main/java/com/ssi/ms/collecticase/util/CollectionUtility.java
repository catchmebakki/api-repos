package com.ssi.ms.collecticase.util;

import java.util.Calendar;
import java.util.Date;

public class CollectionUtility {

    public static Date addDaysToDate(Date passDate,
                                     Long days) {
        Calendar computedDate = Calendar.getInstance();
        if (passDate != null && days != null) {
            computedDate.setTime(passDate);
            computedDate.add(Calendar.DATE, days.intValue());
        }
        return computedDate.getTime();
    }

    public static boolean compareLongValue(Long compareValue, Long compareWith) {
        boolean compareFlag = false;
        if (compareValue != null && compareWith != null
                && (compareValue.compareTo(compareWith) == 0)) {
            compareFlag = true;
        }
        return compareFlag;
    }
}
