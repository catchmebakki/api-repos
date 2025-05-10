package com.ssi.ms.resea.database.repository.custom;


import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.LookupSummaryDTO;
import com.ssi.ms.resea.dto.LookupSummaryReqDTO;
import com.ssi.ms.resea.dto.LookupSummaryResDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.APPOINTMENT_LOOKUP_SORTBY_FIELDMAPPING;


/**
 * @author Sitaram Vuppala
 * Custom repository interface for performing specialized lookup operations on RESEA.
 * This interface defines additional methods to retrieve specific data from the RESEA.
 */
public class CustomLookupRepositoryImpl implements CustomLookupRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	private final String BEYOND_21_DAYS = """
					((rscsStage.alvId = 5597 AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt
						   			> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   OR (rscsStage.alvId = 5598 AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt
						    		> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   OR (rscsStage.alvId = 5599 AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
						   AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt
						    		> FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
					   )
					""";
	private final BiFunction<EntityManager, String, Long> getTotalItemCount = (entityManagerLocal, criteriaStr) ->
			Optional.of(new StringBuilder("""
                            SELECT COUNT(DISTINCT rsic.rsicId)
                            	FROM ReseaIntvwerCalRsicDAO rsic
                            	LEFT JOIN ClaimClmDAO clm ON clm.clmId=rsic.claimDAO.clmId
                            	LEFT JOIN ClaimantCmtDAO cmt on cmt.cmtId=clm.claimantDAO.cmtId
                            	LEFT JOIN ReseaCaseRscsDAO rscs ON rscs.rscsId=rsic.rscsDAO.rscsId
                            	LEFT JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId=rsic.rsisDAO.rsisId
                            	LEFT JOIN StaffUnavaiabilityStunDAO stun ON stun.stunId = rsic.stunDAO.stunId
                            	JOIN StaffStfDAO stf ON stf.stfId=coalesce(rsis.stfDAO.stfId, stun.stfDAO.stfId)
                            	LEFT JOIN LofStaffLsfDAO lsf ON rsis.lofDAO.lofId is null and lsf.stfDAO.stfId = stf.stfId
                            	JOIN LocalOfficeLofDAO lof ON lof.lofId=coalesce(rsis.lofDAO.lofId, lsf.lofDAO.lofId) AND lof.lofBuTypeCd =""")
							.append(ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode()).append(" ")
							.append("""
                                    	LEFT JOIN rscs.rscsStageCdALV rscsStage
                                    	LEFT JOIN rsic.rsicCalEventTypeCdAlv rsicEventType
                                    	LEFT JOIN rsic.rsicTimeslotUsageCdAlv rsicUsage
                                    	LEFT JOIN rsic.rsicMtgStatusCdAlv rsicMtgStatus
                                    	LEFT JOIN rsic.rsicScheduledByCdAlv rsicSchBy
                                    """)
					.append(criteriaStr))
					.map(queryStr -> entityManagerLocal.createQuery(queryStr.toString()).getSingleResult())
					.map(resultObj -> Long.parseLong(resultObj + ""))
					.orElseGet(() -> null);
	private final Function<LookupSummaryReqDTO, String> buildCriteria = lookupReqDTO -> {
		final StringBuilder builder = new StringBuilder();
		builder.append(" where rsic.rsicCalEventDispInd = 'Y' ");

		if (lookupReqDTO.getOfficeNum() != null && !lookupReqDTO.getOfficeNum().isEmpty()) {
			builder.append(" and lof.lofId IN (")
					.append(lookupReqDTO.getOfficeNum().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getCaseManagerId() != null && !lookupReqDTO.getCaseManagerId().isEmpty()) {
			builder.append(" and stf.userDAO.userId in (")
					.append(lookupReqDTO.getCaseManagerId().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getTimeslotTypeCd() != null && !lookupReqDTO.getTimeslotTypeCd().isEmpty()) {
			builder.append(" and rsicEventType.alvId in (")
					.append(lookupReqDTO.getTimeslotTypeCd().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (lookupReqDTO.getTimeslotUsageCd() != null && !lookupReqDTO.getTimeslotUsageCd().isEmpty()) {
			builder.append(" and rsicUsage.alvId in (")
					.append(lookupReqDTO.getTimeslotUsageCd().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (null != lookupReqDTO.getApptStartDt()) {
			builder.append(" and rsic.rsicCalEventDt >= COD ('").append(dateToString.apply(lookupReqDTO.getApptStartDt()))
					.append("')");
		}
		if (null != lookupReqDTO.getApptEndDt()) {
			builder.append(" and rsic.rsicCalEventDt <= COD ('").append(dateToString.apply(lookupReqDTO.getApptEndDt()))
					.append("')");
		}

		if (lookupReqDTO.getMeetingStatusCd() != null && !lookupReqDTO.getMeetingStatusCd().isEmpty()) {
			builder.append(" and rsicMtgStatus.alvId IN (")
					.append(lookupReqDTO.getMeetingStatusCd().stream()
							.map(Object::toString)
							.collect(Collectors.joining(", ")))
					.append(")");
		}

		if (ReseaConstants.INDICATOR.Y.name().equalsIgnoreCase(lookupReqDTO.getHiPriorityInd())) {
			builder.append(" and rscs.rscsPriority = 'HI' ");
		}

		if (ReseaConstants.INDICATOR.Y.name().equalsIgnoreCase(lookupReqDTO.getBeyond21DaysInd())) {
			builder.append(" and ").append(BEYOND_21_DAYS);
		}

		if (lookupReqDTO.getScheduledBy() != null && !lookupReqDTO.getScheduledBy().isEmpty()) {
			builder.append(" and rsicSchBy.alvId IN (")
					.append(lookupReqDTO.getScheduledBy().stream()
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

		/*builder.append(" and rsic.rsicCalEventDt IN ")
				.append("(SELECT MAX(maxevent.rsicCalEventDt) FROM ReseaIntvwerCalRsicDAO maxevent WHERE maxevent.rscsDAO.rscsId=rscs.rscsId)");*/

		return Optional.of(builder.toString())
				.filter(StringUtils::isNotBlank)
				//.map(str -> str.replaceFirst("and", "where"))
				.orElseGet(() -> "");
	};

	@Autowired
	public CustomLookupRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
	}

	@Override
	public LookupSummaryResDTO filterAppointmentBasedLookupCriteria(LookupSummaryReqDTO lookupReqDTO) {
		List<LookupSummaryDTO> lookupSummaryList;
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
                        SELECT new com.ssi.ms.resea.dto.LookupSummaryDTO(rsic.rsicId, lof.lofName AS officeName, stf.stfFirstName||' '||stf.stfLastName AS caseManagerName,
                        TO_CHAR(rsic.rsicCalEventDt, 'MM/DD/YYYY')
                        	||' '||TO_CHAR(TO_DATE(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH:MI AM')
                        	||' - '||TO_CHAR(TO_DATE(rsic.rsicCalEventEndTime, 'HH24:MI'), 'HH:MI AM') AS eventDateTime,
                        rsicEventType.alvShortDecTxt AS eventType,
                        rsicUsage.alvShortDecTxt AS eventUsage,
                        rsicMtgStatus.alvShortDecTxt AS meetingStatus,
                        cmt.firstName||' '||cmt.lastName AS claimantName,
                        CASE WHEN CMT_SSN IS NOT NULL THEN SUBSTR(CMT_SSN,-4) ELSE '' END AS lastFourDigitsOfSSN,
                        clm.clmBenYrEndDt AS byeDt,
                        CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
                             WHEN rscs.rscsPriority = 'HI' THEN rscs.rscsPriority
                             WHEN """)
                .append(BEYOND_21_DAYS)
				.append("""
                        				THEN 'LATE'
                        			END AS indicator
                        )
                        FROM ReseaIntvwerCalRsicDAO rsic
                        LEFT JOIN ClaimClmDAO clm ON clm.clmId=rsic.claimDAO.clmId
                        LEFT JOIN ClaimantCmtDAO cmt on cmt.cmtId=clm.claimantDAO.cmtId
                        LEFT JOIN ReseaCaseRscsDAO rscs ON rscs.rscsId=rsic.rscsDAO.rscsId
                        LEFT JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId=rsic.rsisDAO.rsisId
                        LEFT JOIN StaffUnavaiabilityStunDAO stun ON stun.stunId = rsic.stunDAO.stunId
                        JOIN StaffStfDAO stf ON stf.stfId=coalesce(rsis.stfDAO.stfId, stun.stfDAO.stfId)
                        LEFT JOIN LofStaffLsfDAO lsf ON rsis.lofDAO.lofId is null and lsf.stfDAO.stfId = stf.stfId
                        JOIN LocalOfficeLofDAO lof ON lof.lofId=coalesce(rsis.lofDAO.lofId, lsf.lofDAO.lofId) AND lof.lofBuTypeCd =""")
				.append(ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode()).append(" ")
				.append("""
                        LEFT JOIN rscs.rscsStageCdALV rscsStage
                        LEFT JOIN rsic.rsicCalEventTypeCdAlv rsicEventType
                        LEFT JOIN rsic.rsicTimeslotUsageCdAlv rsicUsage
                        LEFT JOIN rsic.rsicMtgStatusCdAlv rsicMtgStatus
                        LEFT JOIN rsic.rsicScheduledByCdAlv rsicSchBy
                        """);
		querySb.append(criteriaStr);
		if (!"indicator".equals(lookupReqDTO.getSortBy().getField())) {
			querySb.append(" order by ")
					.append(APPOINTMENT_LOOKUP_SORTBY_FIELDMAPPING.getOrDefault(lookupReqDTO.getSortBy().getField(),
									"rsic.rsicCalEventDt sort_dir, rsic.rsicCalEventStTime sort_dir, rsic.rsicId sort_dir")
							.replaceAll("sort_dir", StringUtils.trimToEmpty(lookupReqDTO.getSortBy().getDirection())));
		}
		if ("indicator".equals(lookupReqDTO.getSortBy().getField())) {
			List<LookupSummaryDTO> caseLookupSummary = entityManager.createQuery(querySb.toString()).getResultList();
			Comparator<LookupSummaryDTO> caseLoadSort = Comparator.comparing(LookupSummaryDTO::getIndicator, Comparator.nullsLast(Comparator.reverseOrder()));
			caseLoadSort.thenComparing(LookupSummaryDTO::getEventId);
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
		return LookupSummaryResDTO.builder()
				.pagination(lookupReqDTO.getPagination().withTotalItemCount(totalItemCount))
				.sortBy(lookupReqDTO.getSortBy())
				.summaryDTO(lookupSummaryList).build();
	}
}
