package com.ssi.ms.resea.database.repository.custom;

import com.ssi.ms.resea.dto.LookupSummaryReqDTO;
import com.ssi.ms.resea.dto.LookupSummaryResDTO;

/**
 * @author Sitaram Vuppala
 * Custom repository interface for performing specialized lookup operations on RESEA.
 * This interface defines additional methods to retrieve specific data from RESEA.
 */
public interface CustomLookupRepository {
	/**
	 * Filters the RESEA Case list based on the provided lookup criteria and returns the result as a DTO.
	 *
	 * @param lookupDTO  {@link LookupSummaryReqDTO} The DTO containing the lookup criteria for filtering the RESEA Case list.
	 * @return {@link LookupSummaryResDTO} A DTO containing the filtered RESEA Case list based on the lookup criteria.
	 */
	LookupSummaryResDTO filterAppointmentBasedLookupCriteria(LookupSummaryReqDTO lookupDTO);
}
