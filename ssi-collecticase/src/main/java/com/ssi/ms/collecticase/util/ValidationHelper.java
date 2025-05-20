package com.ssi.ms.collecticase.util;

import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;

import java.util.List;
import java.util.Map;

public class ValidationHelper {

    public static void validateGeneralActivity(GeneralActivityValidator generalActivityValidator,
                                               GeneralActivityDTO generalActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = generalActivityValidator
                .validateGeneralActivity(generalActivityDTO);
        if (!errorMap.isEmpty()) {
            throw new DynamicValidationException("GeneralActivity POST call Errors", errorMap);
        }
    }
}
