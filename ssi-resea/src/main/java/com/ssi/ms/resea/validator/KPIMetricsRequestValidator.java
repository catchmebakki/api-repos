package com.ssi.ms.resea.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.kpi.KPIMetricsReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class KPIMetricsRequestValidator{

	public HashMap<String, List<String>> validateKPIMetricsReqDTO(KPIMetricsReqDTO kpiMetricsReqDTO){
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		
		if(kpiMetricsReqDTO == null || 
				(kpiMetricsReqDTO.getCaseMgrId() == null && kpiMetricsReqDTO.getLofId() == null &&
				(kpiMetricsReqDTO.getAgencySelectedInd() == null || !ReseaConstants.YES_OR_NO_IND_Y.equals(kpiMetricsReqDTO.getAgencySelectedInd())) )) {
			errorEnums.add(ErrorMessageConstant.KPIMetricsRequestErrorDetail.KPI_INPUT_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
		
	}

}
