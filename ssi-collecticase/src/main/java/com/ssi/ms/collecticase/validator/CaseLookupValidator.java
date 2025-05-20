package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.dto.CaseLookupDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CaseLookupValidator {

    public HashMap<String, List<String>> validateCaseLookupDTO(CaseLookupDTO caseLookupDTO){
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();

        if(caseLookupDTO == null) {
            errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_INPUT_INVALID);
        }
        if(caseLookupDTO != null)
        {
            if(caseLookupDTO.getCaseRemedyFromDate() != null && caseLookupDTO.getCaseRemedyToDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_REMEDY_FROM_TO_DATE);
            }
            else if(caseLookupDTO.getCaseRemedyToDate() != null && caseLookupDTO.getCaseRemedyFromDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_REMEDY_FROM_TO_DATE);
            }

            if(caseLookupDTO.getCaseOpenFromDate() != null && caseLookupDTO.getCaseOpenToDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_CASE_OPEN_FROM_TO_DATE);
            }
            else if(caseLookupDTO.getCaseOpenToDate() != null && caseLookupDTO.getCaseOpenFromDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_CASE_OPEN_FROM_TO_DATE);
            }

            if(caseLookupDTO.getRepaymentFromDate() != null && caseLookupDTO.getRepaymentToDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_RPM_FROM_TO_DATE);
            }
            else if(caseLookupDTO.getRepaymentToDate() != null && caseLookupDTO.getRepaymentFromDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_LOOKUP_RPM_FROM_TO_DATE);
            }

            if(caseLookupDTO.getCaseRemedyFromDate() != null && caseLookupDTO.getCaseRemedyToDate() != null)
            {
                if(caseLookupDTO.getCaseRemedyFromDate().after(caseLookupDTO.getCaseRemedyToDate()))
                {
                    errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.REMEDY_FROM_GREATER_THAN_TO_DATE);
                }
            }

            if(caseLookupDTO.getCaseOpenFromDate() != null && caseLookupDTO.getCaseOpenToDate() != null)
            {
                if(caseLookupDTO.getCaseOpenFromDate().after(caseLookupDTO.getCaseOpenToDate()))
                {
                    errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.CASE_OPEN_FROM_GREATER_THAN_TO_DATE);
                }
            }

            if(caseLookupDTO.getRepaymentFromDate() != null && caseLookupDTO.getRepaymentToDate() != null)
            {
                if(caseLookupDTO.getRepaymentFromDate().after(caseLookupDTO.getRepaymentToDate()))
                {
                    errorEnums.add(ErrorMessageConstant.CaseLookupErrorDetail.RPM_FROM_GREATER_THAN_TO_DATE);
                }
            }
        }
        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;

    }
}
