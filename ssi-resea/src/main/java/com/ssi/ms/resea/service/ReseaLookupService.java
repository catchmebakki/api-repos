package com.ssi.ms.resea.service;

import com.ssi.ms.platform.dto.SortByDTO;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.dto.LookupCaseSummaryReqDTO;
import com.ssi.ms.resea.dto.LookupCaseSummaryResDTO;
import com.ssi.ms.resea.dto.LookupSummaryReqDTO;
import com.ssi.ms.resea.dto.LookupSummaryResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.ASCENDING;
import static com.ssi.ms.resea.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;

/**
 * {@code ReseaLookupService} is a service component in the application responsible for
 * handling business logic related to Lookup Summary functionality.
 *
 * @author Anand
 */
@Service
@Slf4j
public class ReseaLookupService extends ReseaBaseService {
	@Autowired
	private ReseaCaseRscsRepository rscsRepository;
	@PersistenceContext
	private final EntityManager entityManager;

	@Autowired
	public ReseaLookupService(EntityManager entityManager) {
		this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
	}

	public LookupSummaryResDTO getApptLookupSummary(LookupSummaryReqDTO lookupReqDTO) {
		if (null == lookupReqDTO.getPagination()) {
			lookupReqDTO = lookupReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
		}
		if (null == lookupReqDTO.getSortBy()) {
			lookupReqDTO = lookupReqDTO.withSortBy(SortByDTO.builder()
					.field("default")
					.direction(ASCENDING)
					.build());
		}
		return rscsRepository.filterAppointmentBasedLookupCriteria(lookupReqDTO);
	}

	public LookupCaseSummaryResDTO getCaseLookupSummary(LookupCaseSummaryReqDTO lookupReqDTO) {
		if (null == lookupReqDTO.getPagination()) {
			lookupReqDTO = lookupReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
		}
		if (null == lookupReqDTO.getSortBy()) {
			lookupReqDTO = lookupReqDTO.withSortBy(SortByDTO.builder()
					.field("default")
					.direction(ASCENDING)
					.build());
		}
		return rscsRepository.filterCaseBasedLookupCriteria(lookupReqDTO);
	}

	public LookupCaseSummaryResDTO getInitApptPendingSummary(LookupCaseSummaryReqDTO lookupReqDTO) {
		if (null == lookupReqDTO.getPagination()) {
			lookupReqDTO = lookupReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
		}
		if (null == lookupReqDTO.getSortBy()) {
			lookupReqDTO = lookupReqDTO.withSortBy(SortByDTO.builder()
					.field("default")
					.direction(ASCENDING)
					.build());
		}
		return rscsRepository.filterInitialApptPendSchedulingCriteria(lookupReqDTO);
	}
}