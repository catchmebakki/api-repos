package com.ssi.ms.resea.service;

import static com.ssi.ms.resea.constant.ReseaConstants.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssi.ms.platform.util.DateUtil;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.repository.IssueDecisionDecRepository;
import com.ssi.ms.resea.database.repository.ReseaJobReferralRsjrRepository;
import com.ssi.ms.resea.database.repository.ReseaReturnToWorkRsrwRepository;
import com.ssi.ms.resea.database.repository.StaffStfRepository;
import com.ssi.ms.resea.dto.kpi.KPIMetricsReqDTO;
import com.ssi.ms.resea.dto.kpi.KPIMetricsResDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KPIMetricsService extends ReseaBaseService{

	@Autowired
	private IssueDecisionDecRepository issueDecisionDecRepository;
	
	public KPIMetricsResDTO getKPIMetrics(KPIMetricsReqDTO kpiMetricsReqDTO) {
		Date metricsStartDate = calculateMetricsStartDate(kpiMetricsReqDTO.getPeriodRange());
		
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		
		List<Long> reseaCaseMgrList = getReseaCaseManagerList(kpiMetricsReqDTO);
		Long totalApptCount = rsicRepo.getApptCountByCaseMgrPeriod(reseaCaseMgrList, metricsStartDate);
		Long completedApptCount = rsicRepo.getApptCountByCaseMgrPeriodStatus(reseaCaseMgrList, metricsStartDate, List.of(RSIC_MTG_STATUS_COMPLETED));
		Long completedRTWApptCount = rsicRepo.getApptCountByCaseMgrPeriodStatus(reseaCaseMgrList, metricsStartDate, List.of(RSIC_MTG_STATUS_COMPLETED_RTW));
		Long noShowRTWCount = rsicRepo.getApptCountByCaseMgrPeriodStatus(reseaCaseMgrList, metricsStartDate, List.of(RSIC_MTG_STATUS_FAILED_RTW));
		Long noShowFailedCount = rsicRepo.getApptCountByCaseMgrPeriodStatus(reseaCaseMgrList, metricsStartDate, List.of(RSIC_MTG_STATUS_FAILED));
		Long scheduled = rsicRepo.getApptCountByCaseMgrPeriodStatus(reseaCaseMgrList, metricsStartDate, List.of(RSIC_MTG_STATUS_SCHEDULED));
		Long noShowRescheduledCount =  rsicRepo.getNoShowResheduledApptCountByCaseMgrPeriod(reseaCaseMgrList, metricsStartDate);
		Long remoteApptCount = rsicRepo.getApptCountByCaseMgrPeriodMtgMode(reseaCaseMgrList, metricsStartDate, ReseaConstants.MEETING_MODE.VIRTUAL.getCode());
		Long inPersonApptCount = rsicRepo.getApptCountByCaseMgrPeriodMtgMode(reseaCaseMgrList, metricsStartDate, ReseaConstants.MEETING_MODE.IN_PERSON.getCode());
		BigDecimal avgWksOfEmployment = rtwRsrwRepo.getAvgRTWWksByCaseMgrPeriod(reseaCaseMgrList, metricsStartDate);
		avgWksOfEmployment = avgWksOfEmployment !=null ? avgWksOfEmployment.setScale(2, RoundingMode.HALF_UP) : avgWksOfEmployment;
		
 		return new KPIMetricsResDTO()
				.withCaseLoad(rscsRepo.getCaseLoadMetricsByUserIds(reseaCaseMgrList))
				.withAvgWksOfEmployment(avgWksOfEmployment)
				.withTotalApptCount(totalApptCount)
				.withCompletedApptCount(totalApptCount)
				.withCompletedApptCount(completedApptCount)
				.withCompletedApptPercent(calcPercentage(completedApptCount,totalApptCount))		
				.withCompletedRTWApptCount(completedRTWApptCount)
				.withCompletedRTWApptPercent(calcPercentage(completedRTWApptCount,totalApptCount))
				.withScheduledCount(scheduled)
				.withScheduledPercent(calcPercentage(scheduled,totalApptCount))
				.withNoShowRTWCount(noShowRTWCount)
				.withNoShowRTWPercent(calcPercentage(noShowRTWCount,totalApptCount))
				.withNoShowFailedCount(noShowFailedCount)
				.withNoShowFailedPercent(calcPercentage(noShowFailedCount,totalApptCount))
				.withNoShowRescheduledCount(noShowRescheduledCount)
				.withNoShowRescheduledPercent(calcPercentage(noShowRescheduledCount,totalApptCount))
				.withRemoteApptCount(remoteApptCount)
				.withRemoteApptPercent(calcPercentage(remoteApptCount,totalApptCount))
				.withInPersonApptCount(inPersonApptCount)
				.withInPersonApptPercent(calcPercentage(inPersonApptCount,totalApptCount))
				.withNoOfInadequateWSCmts(issueDecisionDecRepository.getInadqWSCmtsByCaseMgrAndPeriod(reseaCaseMgrList, metricsStartDate, today))
				.withNoOfInadequateWSWeeks(issueDecisionDecRepository.getInadqWSWksByCaseMgrAndPeriod(reseaCaseMgrList, metricsStartDate, today))
				.withNoOfJobReferralsMade(rsjrRepo.getJobReferralsByCaseMgrAndPeriod(reseaCaseMgrList, metricsStartDate));
	}
	
	private Date calculateMetricsStartDate(String periodRange) {
		Date metricsStartDate = null;
		LocalDate today = LocalDate.now();
		
		metricsStartDate = switch(periodRange) {
			case "THREE_MONTHS" ->  DateUtil.localDateToDate.apply(today.minusMonths(3));
			case "SIX_MONTHS" ->  DateUtil.localDateToDate.apply(today.minusMonths(6));
			case "ONE_YEAR" -> DateUtil.localDateToDate.apply(today.minusYears(1));
			default -> DateUtil.localDateToDate.apply(today.minusYears(1));
		};
		
		return metricsStartDate;
	}
	
	private  List<Long> getReseaCaseManagerList(KPIMetricsReqDTO kpiMetricsReqDTO){
		List<Long> reseaCaseMgrs = null;
		
		if(ReseaConstants.YES_OR_NO_IND_Y.equals(kpiMetricsReqDTO.getAgencySelectedInd())) {
			reseaCaseMgrs = stfRepo.getReseaCaseManagerListByLofId(null);	
			
		}else if(kpiMetricsReqDTO.getLofId() != null) {
			reseaCaseMgrs = stfRepo.getReseaCaseManagerListByLofId(kpiMetricsReqDTO.getLofId());
			
		}else if (kpiMetricsReqDTO.getCaseMgrId() != null) {
				reseaCaseMgrs = List.of(kpiMetricsReqDTO.getCaseMgrId());
		}
		return reseaCaseMgrs;
	}

	
	private BigDecimal calcPercentage(Long part, Long total) {
		BigDecimal percentage = null;
		
		if (total == 0 || part ==0) {
			return null;
		}
		percentage = (new BigDecimal(part).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
		return percentage;
	}
}
