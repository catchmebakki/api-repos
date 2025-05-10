package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.util.ConfigErrorEnum;
import com.ssi.ms.configuration.util.ConfigUtilFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WorkSearchConfigErrorDetail.CONFIG_COMMENT_INVALID;

public class ConfigValidator {
    public void validateGeneratedComments(String comments, HashMap<String, List<String>> errorMap) {
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        final int commentLength = 4000;
        if (StringUtils.isNotBlank(comments) && comments.length() > commentLength) {
            errorEnums.add(CONFIG_COMMENT_INVALID);
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
    }
}
