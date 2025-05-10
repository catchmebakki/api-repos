package com.ssi.ms.resea.dto.kpi;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class KPIMetricsResDTO {
	Long caseLoad;
	BigDecimal avgWksOfEmployment;
	Long totalApptCount;
	Long completedApptCount;
	BigDecimal completedApptPercent;
	Long completedRTWApptCount;
	BigDecimal completedRTWApptPercent;
	Long scheduledCount;
	BigDecimal scheduledPercent;
	Long noShowRTWCount;
	BigDecimal noShowRTWPercent;
	Long noShowRescheduledCount;
	BigDecimal noShowRescheduledPercent;
	Long noShowFailedCount;
	BigDecimal noShowFailedPercent;
	Long remoteApptCount;
	BigDecimal remoteApptPercent;
	Long inPersonApptCount;
	BigDecimal inPersonApptPercent;
	Long noOfInadequateWSCmts;
	Long noOfInadequateWSWeeks;
	Long noOfJobReferralsMade;
}
