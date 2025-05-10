package com.ssi.ms.configuration.util;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_CMT_EMP;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_CMT_EMP_SET;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_LIST;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_STF_CMT;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_STF_CMT_SET;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_STF_EMP;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_DISPLAY_ON_STF_EMP_SET;

@SuppressWarnings("PMD.FieldNamingConventions")
public interface DisplayOnUtilFunction {
    Function<Set<Long>, Long> GET_DISPLAY_ON_COMBINATION_ID = displayOnSet -> {
        Long displayOnId = null;
            if (displayOnSet.size() == 1) {
                displayOnId = displayOnSet.iterator().next();
            } else {
                displayOnId = ALV_DISPLAY_ON_STF_CMT_SET.containsAll(displayOnSet) ? ALV_DISPLAY_ON_STF_CMT
                        : ALV_DISPLAY_ON_STF_EMP_SET.containsAll(displayOnSet) ? ALV_DISPLAY_ON_STF_EMP
                        : ALV_DISPLAY_ON_CMT_EMP_SET.containsAll(displayOnSet) ? ALV_DISPLAY_ON_CMT_EMP : null;
            }
            return displayOnId;
    };

    Function<Long, List<Long>> getAlvDisplayOnList = displayOn -> Optional.ofNullable(displayOn)
            .map(dto -> ObjectUtils.firstNonNull(ALV_DISPLAY_ON_LIST.get(displayOn),
                    Collections.singletonList(displayOn))).orElse(null);

}
