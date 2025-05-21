package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.dto.CompleteFollowupActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.platform.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CompleteFollowupValidator {

    public HashMap<String, List<String>> validateCaseFollowupDTO(CompleteFollowupActivityDTO completeFollowupActivityDTO) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();

        if (DateUtil.stringToDate.apply(completeFollowupActivityDTO.getActivityCompletedOn()).before(
                DateUtil.stringToDate.apply(completeFollowupActivityDTO.getActivityCreatedDate()))) {
            errorEnums.add(ErrorMessageConstant.CompleteFollowupDetail
                    .COMPLETE_FOLLOWUP_COMPLETED_ON_LESS_THAN_ACTIVITY_DATE);
        }

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;

    }
}
