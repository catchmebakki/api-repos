package com.ssi.ms.resea.database.repository.custom;


import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.CaseLoadSummaryResDTO;
import com.ssi.ms.resea.dto.CaseLookupSummaryDTO;
import com.ssi.ms.resea.dto.LookupCaseSummaryResDTO;
import com.ssi.ms.resea.dto.LookupCaseSummaryReqDTO;
import com.ssi.ms.resea.dto.ReseaCaseLoadViewResDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.getPrevSundayDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.APPOINTMENT_LOOKUP_SORTBY_FIELDMAPPING;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.CASE_LOOKUP_SORTBY_FIELDMAPPING;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.INIT_PENDING_LOOKUP_SORTBY_FIELDMAPPING;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_NO_CCF_WEEKS;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;


/**
 * @author Sitaram Vuppala
 * Custom repository interface for performing specialized lookup operations on RESEA.
 * This interface defines additional methods to retrieve specific data from the RESEA.
 */
public class CustomCaseLookupRepositoryImpl implements CustomCaseLookupRepository {

	@PersistenceContext
	private final EntityManager entityManager;
	@Autowired
	private ParameterParRepository commonParRepository;

	private final String BEYOND_21_DAYS = """
					((rscs.rscsStageCdALV.alvId = 5597 AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt
						   			> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   OR (rscs.rscsStageCdALV.alvId = 5598 AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt
						    		> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   OR (rscs.rscsStageCdALV.alvId = 5599 AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt
						    		> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   )
					""";
	private final BiFunction<EntityManager, String, Long> getTotalItemCount = (entityManagerLocal, criteriaStr) ->
			Optional.of(new StringBuilder("""
						SELECT COUNT(DISTINCT rscs.rscsId)
							FROM ReseaCaseRscsDAO rscs
							JOIN ClaimClmDAO clm ON clm.clmId=rscs.clmDAO.clmId
							JOIN ClaimantCmtDAO cmt on cmt.cmtId=clm.claimantDAO.cmtId
							LEFT JOIN ReseaIntvwerCalRsicDAO rsic ON rscs.rscsId=rsic.rscsDAO.rscsId
							LEFT JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId=rsic.rsisDAO.rsisId
							LEFT JOIN StaffStfDAO stf ON stf.stfId=rsis.stfDAO.stfId
							LEFT JOIN LocalOfficeLofDAO lof ON lof.lofId=rsis.lofDAO.lofId
							LEFT JOIN ReseaReturnToWorkRsrwDAO rswr ON rswr.rsicDao.rsicId = rsic.rsicId
							LEFT JOIN ReseaCaseActivityRscaDAO rsca on rsca.rscsDAO.rscsId = rscs.rscsId and rsca.rscaTypeCdALV = 5584
						""" )
					.append(criteriaStr))
					.map(queryStr -> entityManagerLocal.createQuery(queryStr.toString()).getSingleResult())
					.map(resultObj -> Long.parseLong(resultObj + ""))
					.orElseGet(() -> null);
	private final Function<LookupCaseSummaryReqDTO, String> buildCriteria = lookupReqDTO -> {
		final StringBuilder builder = new StringBuilder();
		if (lookupReqDTO.getOfficeNum() != null && !lookupReqDTO.getOfficeNum().isEmpty()) {
			builder.append(" and lof.lofId IN (")
					.append(lookupReqDTO.getOfficeNum().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getCaseManagerId() != null && !lookupReqDTO.getCaseManagerId().isEmpty()) {
			builder.append(" and stf.userDAO.userId IN (")
					.append(lookupReqDTO.getCaseManagerId().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getCaseStage() != null && !lookupReqDTO.getCaseStage().isEmpty()) {
			builder.append(" and rscs.rscsStageCdALV.alvId IN (")
					.append(lookupReqDTO.getCaseStage().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getCaseStatus() != null && !lookupReqDTO.getCaseStatus().isEmpty()) {
			builder.append(" and rscs.rscsStatusCdALV.alvId IN (")
					.append(lookupReqDTO.getCaseStatus().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (ReseaConstants.INDICATOR.Y.name().equalsIgnoreCase(lookupReqDTO.getHiPriorityInd())) {
			builder.append(" and rscs.rscsPriority = 'HI' ");
		}

		if (ReseaConstants.INDICATOR.Y.name().equalsIgnoreCase(lookupReqDTO.getWaitlisted())) {
			builder.append(" and rscs.rscsOnWaitlistInd = 'Y' ");
		}

		if (lookupReqDTO.getRtwDaysMin() != null && lookupReqDTO.getRtwDaysMin() != 0L) {
			builder.append(" and rswr.rsrwNewEmpStartDt - trunc(rscs.rscsCreatedTs) >= ")
					.append(lookupReqDTO.getRtwDaysMin());
		}

		if (lookupReqDTO.getRtwDaysMax() != null && lookupReqDTO.getRtwDaysMax() != 0L) {
			builder.append(" and rswr.rsrwNewEmpStartDt - trunc(rscs.rscsCreatedTs) <= ")
					.append(lookupReqDTO.getRtwDaysMax());
		}

		if (lookupReqDTO.getCaseScoreMin() != null && lookupReqDTO.getCaseScoreMin() != 0) {
			builder.append(" and rscs.rscsScore >= ").append(lookupReqDTO.getCaseScoreMin());
		}

		if (lookupReqDTO.getCaseScoreMax() != null && lookupReqDTO.getCaseScoreMax() != 0) {
			builder.append(" and rscs.rscsScore <= ").append(lookupReqDTO.getCaseScoreMax());
		}

		if (null != lookupReqDTO.getOrientationStartDt()) {
			builder.append(" and rscs.rscsOrientationDt >= COD ('").append(dateToString.apply(lookupReqDTO.getOrientationStartDt()))
					.append("')");
		}

		if (null != lookupReqDTO.getOrientationEndDt()) {
			builder.append(" and rscs.rscsOrientationDt <= COD ('").append(dateToString.apply(lookupReqDTO.getOrientationEndDt()))
					.append("')");
		}

		if (null != lookupReqDTO.getInitialApptStartDt()) {
			builder.append(" and rscs.rscsInitApptDt >= COD ('").append(dateToString.apply(lookupReqDTO.getInitialApptStartDt()))
					.append("')");
		}
		if (null != lookupReqDTO.getInitialApptEndDt()) {
			builder.append(" and rscs.rscsInitApptDt <= COD ('").append(dateToString.apply(lookupReqDTO.getInitialApptEndDt()))
					.append("')");
		}

		if (null != lookupReqDTO.getRecentApptStartDt()) {
			builder.append(" and rsic.rsicCalEventDt >= COD ('").append(dateToString.apply(lookupReqDTO.getRecentApptStartDt()))
					.append("')");
		}

		if (null != lookupReqDTO.getRecentApptEndDt()) {
			builder.append(" and rsic.rsicCalEventDt <= COD ('").append(dateToString.apply(lookupReqDTO.getRecentApptEndDt()))
					.append("')");
		}

		if (lookupReqDTO.getTerminationReason() != null && !lookupReqDTO.getTerminationReason().isEmpty()) {
			builder.append(" and rscs.rscsClosedReasonCd.alvId IN (")
					.append(lookupReqDTO.getTerminationReason().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (StringUtils.isNotBlank(lookupReqDTO.getClaimantName())) {
			builder.append(" and UPPER(cmt.firstName||' '||cmt.lastName) like '%")
					.append(lookupReqDTO.getClaimantName().toUpperCase())
					.append("%'");
		}
		if (StringUtils.isNotEmpty(lookupReqDTO.getSsn())) {
			builder.append(" and cmt.ssn like '%").append(lookupReqDTO.getSsn()).append("'");
		}

		if (null != lookupReqDTO.getClmByeStartDt()) {
			builder.append(" and clm.clmBenYrEndDt >= COD ('").append(dateToString.apply(lookupReqDTO.getClmByeStartDt()))
					.append("')");
		}
		if (null != lookupReqDTO.getClmByeEndDt()) {
			builder.append(" and clm.clmBenYrEndDt <= COD ('").append(dateToString.apply(lookupReqDTO.getClmByeEndDt()))
					.append("')");
		}

		//builder.append(" and rsic.rsicCalEventDt IN ")
		//		.append("(SELECT MAX(maxevent.rsicCalEventDt) FROM ReseaIntvwerCalRsicDAO maxevent WHERE maxevent.rscsDAO.rscsId=rscs.rscsId)");

		return Optional.of(builder.toString())
				.filter(StringUtils::isNotBlank)
				.map(str -> str.replaceFirst("and", "where"))
				.orElse("");
	};

	@Autowired
	public CustomCaseLookupRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
	}

	@Override
	public LookupCaseSummaryResDTO filterCaseBasedLookupCriteria(LookupCaseSummaryReqDTO lookupReqDTO) {
		List<CaseLookupSummaryDTO> lookupSummaryList;
		final StringBuilder querySb = new StringBuilder();
		final String criteriaStr = buildCriteria.apply(lookupReqDTO);
		Long totalItemCount = null;

		if (lookupReqDTO.getPagination().getNeedTotalCount()) {
			totalItemCount = getTotalItemCount.apply(entityManager, criteriaStr);
			if (totalItemCount > 2000L) {
				final HashMap<String, List<String>> errorMap = new HashMap<>();
				final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
				errorEnums.add(ErrorMessageConstant.LookupErrorDetail.LOOKUP_EXCEED_LIMIT);
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
				throw new CustomValidationException("Lookup Failed", errorMap);
			}
		}


		querySb.append("""
						SELECT new com.ssi.ms.resea.dto.CaseLookupSummaryDTO(
						rscs.rscsId,
						lof.lofName AS officeName,
						stf.stfFirstName||' '||stf.stfLastName AS caseManagerName,
						rscs.rscsStageCdALV.alvShortDecTxt AS stage,
						rscs.rscsStatusCdALV.alvShortDecTxt AS status,
						cmt.firstName||' '||cmt.lastName AS claimantName,
						SUBSTR(cmt.ssn, 6),
						(SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId) AS weeks,
						clm.clmBenYrEndDt AS byeDt,
						followUpAlv.alvShortDecTxt AS followUpType,
						rscaFollowUp.rscaFollowupDt AS followUpDt,
						CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
						     WHEN rscs.rscsPriority = 'HI' THEN rscs.rscsPriority
						     WHEN""")
				.append(BEYOND_21_DAYS)
				.append("""
								THEN 'LATE'
						END AS indicator,
						COALESCE(rscs.rscsSecondSubsApptDt, rscs.rscsFirstSubsApptDt, rscs.rscsInitApptDt, rscs.rscsOrientationDt) AS appointmetDate,
						rscs.clmDAO.clmId,
						CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
						rsca.rscaDetails
						)
						FROM ReseaCaseRscsDAO rscs
						JOIN ClaimClmDAO clm ON clm.clmId=rscs.clmDAO.clmId
						JOIN ClaimantCmtDAO cmt on cmt.cmtId=clm.claimantDAO.cmtId
						LEFT JOIN ReseaIntvwerCalRsicDAO rsic ON rscs.rscsId=rsic.rscsDAO.rscsId AND rsic.rsicCalEventDt IN
							(SELECT MAX(maxevent.rsicCalEventDt) FROM ReseaIntvwerCalRsicDAO maxevent WHERE maxevent.rscsDAO.rscsId=rscs.rscsId)
						LEFT JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId=rsic.rsisDAO.rsisId
						LEFT JOIN StaffStfDAO stf ON stf.stfId=rscs.stfDAO.stfId
						LEFT JOIN LocalOfficeLofDAO lof ON lof.lofId=rsis.lofDAO.lofId
						LEFT JOIN ReseaReturnToWorkRsrwDAO rswr ON rswr.rsicDao.rsicId = rsic.rsicId
						LEFT JOIN ReseaCaseActivityRscaDAO rsca on rsca.rscsDAO.rscsId = rscs.rscsId and rsca.rscaTypeCdALV = 5584
						LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
							AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
						LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
						""");
		querySb.append(criteriaStr);
		if (!"indicator".equals(lookupReqDTO.getSortBy().getField())
				&& !"weeks".equals(lookupReqDTO.getSortBy().getField())
				&& !"followUp".equals(lookupReqDTO.getSortBy().getField())) {
			querySb.append(" order by ")
					.append(CASE_LOOKUP_SORTBY_FIELDMAPPING.getOrDefault(lookupReqDTO.getSortBy().getField(), "clm.clmBenYrEndDt sort_dir, rscs.rscsId sort_dir")
							.replaceAll("sort_dir", StringUtils.trimToEmpty(lookupReqDTO.getSortBy().getDirection())));
		}
		if ("indicator".equals(lookupReqDTO.getSortBy().getField())
				|| "weeks".equals(lookupReqDTO.getSortBy().getField())
				|| "followUp".equals(lookupReqDTO.getSortBy().getField())) {
			List<CaseLookupSummaryDTO> caseLookupSummary = entityManager.createQuery(querySb.toString()).getResultList();
			Comparator<CaseLookupSummaryDTO> caseLoadSort = switch (lookupReqDTO.getSortBy().getField()) {
				case "indicator" -> Comparator.comparing(CaseLookupSummaryDTO::getIndicator, Comparator.nullsLast(Comparator.reverseOrder()));
				case "weeks" -> Comparator.comparing(CaseLookupSummaryDTO::getWeeks, Comparator.nullsLast(Comparator.reverseOrder()));
				case "followUp" -> Comparator.comparing(CaseLookupSummaryDTO::getFollowUpDt, Comparator.nullsLast(Comparator.reverseOrder()))
						.thenComparing(CaseLookupSummaryDTO::getFollowUpType, Comparator.nullsLast(Comparator.reverseOrder()));
				default -> Comparator.comparing(CaseLookupSummaryDTO::getByeDt, Comparator.nullsLast(Comparator.reverseOrder()));
			};
			caseLoadSort.thenComparing(CaseLookupSummaryDTO::getCaseId);
			if ("desc".equals(lookupReqDTO.getSortBy().getDirection())) {
				caseLookupSummary.sort(caseLoadSort.reversed());
			} else {
				caseLookupSummary.sort(caseLoadSort);
			}

			PaginationDTO pageDTO = lookupReqDTO.getPagination();
			int minIndex = pageDTO.getPageSize() * (pageDTO.getPageNumber() - 1);
			int maxIndex = Math.min(pageDTO.getPageSize() * pageDTO.getPageNumber() - 1, caseLookupSummary.size());
			if (minIndex > caseLookupSummary.size()) {
				minIndex = 0;
				maxIndex = Math.min(pageDTO.getPageSize(), caseLookupSummary.size());
			}
			lookupSummaryList = caseLookupSummary.subList(minIndex, maxIndex);
		} else {
			lookupSummaryList = entityManager.createQuery(querySb.toString())
					.setFirstResult((lookupReqDTO.getPagination().getPageNumber() - 1)
							* lookupReqDTO.getPagination().getPageSize())
					.setMaxResults(lookupReqDTO.getPagination().getPageSize())
					.getResultList();
		}
		return LookupCaseSummaryResDTO.builder()
				.pagination(lookupReqDTO.getPagination().withTotalItemCount(totalItemCount))
				.sortBy(lookupReqDTO.getSortBy())
				.summaryDTO(lookupSummaryList).build();
	}


	public LookupCaseSummaryResDTO filterInitialApptPendSchedulingCriteria(LookupCaseSummaryReqDTO lookupReqDTO) {
		List<CaseLookupSummaryDTO> lookupSummaryList;
		final StringBuilder querySb = new StringBuilder();
		final ParameterParDao parDao = commonParRepository.findByParShortName(PAR_RESEA_NO_CCF_WEEKS);
		final String ccaDt = dateToString.apply(localDateToDate.apply(dateToLocalDate.apply(getPrevSundayDate.apply(commonParRepository.getCurrentDate()))
				.minusDays(1 + (parDao.getParNumericValue() * 7))));
		final String criteriaStr = """
					WHERE rsps.rspsItemTypeCd = 5555
					  AND clf.clfActiveInd = 'Y'
					  and clf.localOfficeLofDAO.lofId != 14
					  AND EXISTS (
                            SELECT 1 FROM ReseaPreStepsRspsDAO rsps1
                                WHERE rsps1.rspsItemTypeCd IN (5767, 5768)
                                  AND rsps1.claimClmDAO.clmId = rsps.claimClmDAO.clmId
                      )
					  AND NOT EXISTS (
						SELECT 1 FROM ReseaCaseRscsDAO rscs
							WHERE rscs.clmDAO.clmId = rsps.claimClmDAO.clmId
							  AND (rscs.rscsCreatedTs >= rsps.rspsItemTs OR rscs.rscsStageCdALV.alvId IN (5597, 5598, 5599))
					  )
					  AND EXISTS (
						SELECT 1 FROM CcApplnCcaDAO cca
							WHERE cca.clmDAO.clmId = rsps.claimClmDAO.clmId
							  AND cca.ccaWeekEndingDt >= COD('"""+ccaDt+"'))"
				;
		Long totalItemCount = null;

		if (lookupReqDTO.getPagination().getNeedTotalCount()) {
			totalItemCount = getInitApptPendTotalItemCount.apply(entityManager, criteriaStr);
			/*if (totalItemCount > 2000L) {
				final HashMap<String, List<String>> errorMap = new HashMap<>();
				final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
				errorEnums.add(ErrorMessageConstant.LookupErrorDetail.LOOKUP_EXCEED_LIMIT);
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
				throw new CustomValidationException("Lookup Failed", errorMap);
			}*/
		}


		querySb.append("""
						SELECT new com.ssi.ms.resea.dto.CaseLookupSummaryDTO(lof.lofName AS officeName,
						'Initial Appointment' AS stage,
						'Pending further scheduling by staff' AS status,
						cmt.firstName||' '||cmt.lastName AS claimantName,
						SUBSTR(cmt.ssn, 6),
						(SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = clm.clmId) AS weeks,
						clm.clmBenYrEndDt AS byeDt,
						CASE WHEN trunc(rsps.rspsOrientationDt) != COD('01/01/2000')
						      AND trunc(SYSDATE) - rsps.rspsOrientationDt
						      	BETWEEN FN_INV_GET_PARAMETER_NUMERIC ('RESEA_HI_PRI_DAYS',TRUNC (SYSDATE))
						      		AND FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
						     THEN 'Hi'
						     WHEN trunc(rsps.rspsOrientationDt) != COD('01/01/2000')
						      AND trunc(SYSDATE) - rsps.rspsOrientationDt
						       			> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
							 THEN 'LATE'
						END AS indicator,
						clm.clmId,
						'Y',
						''
						)
						FROM ReseaPreStepsRspsDAO rsps
						JOIN ClaimClmDAO clm ON clm.clmId=rsps.claimClmDAO.clmId
						JOIN ClaimantCmtDAO cmt on cmt.cmtId=clm.claimantDAO.cmtId
						LEFT JOIN ClmLofClfDao clf ON clf.claimClmDAO.clmId = clm.clmId
						LEFT JOIN LocalOfficeLofDAO lof ON lof.lofId=clf.localOfficeLofDAO.lofId 
						""");
		querySb.append(criteriaStr);
		if (!"indicator".equals(lookupReqDTO.getSortBy().getField()) && !"weeks".equals(lookupReqDTO.getSortBy().getField())) {
			querySb.append(" order by ")
					.append(INIT_PENDING_LOOKUP_SORTBY_FIELDMAPPING.getOrDefault(lookupReqDTO.getSortBy().getField(), "rsps.rspsOrientationDt sort_dir, rsps.rspsId sort_dir")
							.replaceAll("sort_dir", StringUtils.trimToEmpty(lookupReqDTO.getSortBy().getDirection())));
		}
		if ("indicator".equals(lookupReqDTO.getSortBy().getField()) || "weeks".equals(lookupReqDTO.getSortBy().getField())) {
			List<CaseLookupSummaryDTO> caseLookupSummary = entityManager.createQuery(querySb.toString()).getResultList();
			Comparator<CaseLookupSummaryDTO> caseLoadSort = switch (lookupReqDTO.getSortBy().getField()) {
				case "indicator" -> Comparator.comparing(CaseLookupSummaryDTO::getIndicator, Comparator.nullsLast(Comparator.reverseOrder()));
				case "weeks" -> Comparator.comparing(CaseLookupSummaryDTO::getWeeks, Comparator.nullsLast(Comparator.reverseOrder()));
				default -> Comparator.comparing(CaseLookupSummaryDTO::getByeDt, Comparator.nullsLast(Comparator.reverseOrder()));
			};
			caseLoadSort.thenComparing(CaseLookupSummaryDTO::getCaseId);
			if ("desc".equals(lookupReqDTO.getSortBy().getDirection())) {
				caseLookupSummary.sort(caseLoadSort.reversed());
			} else {
				caseLookupSummary.sort(caseLoadSort);
			}

			PaginationDTO pageDTO = lookupReqDTO.getPagination();
			int minIndex = pageDTO.getPageSize() * (pageDTO.getPageNumber() - 1);
			int maxIndex = Math.min(pageDTO.getPageSize() * pageDTO.getPageNumber() - 1, caseLookupSummary.size());
			if (minIndex > caseLookupSummary.size()) {
				minIndex = 0;
				maxIndex = Math.min(pageDTO.getPageSize(), caseLookupSummary.size());
			}
			lookupSummaryList = caseLookupSummary.subList(minIndex, maxIndex);
		} else {
			lookupSummaryList = entityManager.createQuery(querySb.toString())
					.setFirstResult((lookupReqDTO.getPagination().getPageNumber() - 1)
							* lookupReqDTO.getPagination().getPageSize())
					.setMaxResults(lookupReqDTO.getPagination().getPageSize())
					.getResultList();
		}
		return LookupCaseSummaryResDTO.builder()
				.pagination(lookupReqDTO.getPagination().withTotalItemCount(totalItemCount))
				.sortBy(lookupReqDTO.getSortBy())
				.summaryDTO(lookupSummaryList).build();
	}

	private final BiFunction<EntityManager, String, Long> getInitApptPendTotalItemCount = (entityManagerLocal, criteriaStr) ->
			Optional.of(new StringBuilder("""
							SELECT COUNT(DISTINCT rsps.rspsId)
								FROM ReseaPreStepsRspsDAO rsps
								LEFT JOIN ClmLofClfDao clf ON clf.claimClmDAO.clmId = rsps.claimClmDAO.clmId
							""" )
							.append(criteriaStr))
					.map(queryStr -> entityManagerLocal.createQuery(queryStr.toString()).getSingleResult())
					.map(resultObj -> Long.parseLong(resultObj + ""))
					.orElseGet(() -> null);
}
