package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OrgLookupValidator {

    public HashMap<String, List<String>> validateOrgLookupDTO(OrgLookupDTO
                                                                      orgLookupDTO) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();

        if (StringUtils.isNotBlank(orgLookupDTO.getOrgName()) || StringUtils.isNotBlank(orgLookupDTO.getUiAcctNbr())
                || StringUtils.isNotBlank(orgLookupDTO.getFein())) {
            errorEnums.add(ErrorMessageConstant.OrgLookupDetail
                    .ORG_LOOKUP_ATLEAST_ONE_SHOULD_BE_SELECTED);
        }

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;

    }
}
