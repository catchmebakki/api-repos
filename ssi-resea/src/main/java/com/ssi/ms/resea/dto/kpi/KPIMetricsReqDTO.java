package com.ssi.ms.resea.dto.kpi;

import org.springframework.validation.annotation.Validated;

import com.ssi.ms.platform.validator.EnumValidator;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class KPIMetricsReqDTO {
	Long caseMgrId;
	Long lofId;
	String agencySelectedInd;
	
	@EnumValidator(enumClazz = ReseaConstants.KPI_PERIOD_RANGE.class, message = ErrorMessageConstant.KPI_PERIOD_RANGE_INVALID)
	String periodRange; // (e.g) ENUM Values THREE_MONTHS,SIX_MONTHS,ONE_YEAR
}
