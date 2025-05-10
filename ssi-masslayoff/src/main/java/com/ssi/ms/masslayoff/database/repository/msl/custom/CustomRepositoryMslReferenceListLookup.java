package com.ssi.ms.masslayoff.database.repository.msl.custom;

import com.ssi.ms.masslayoff.dto.lookup.LookupListResDTO;
import com.ssi.ms.masslayoff.dto.lookup.LookupReqDTO;
/**
 * @author Praveenraja Paramsivam
 * Custom repository interface for performing specialized lookup operations on MSL reference lists.
 * This interface defines additional methods to retrieve specific data from the MSL reference lists.
 */
public interface CustomRepositoryMslReferenceListLookup {
	/**
	 * Filters the MSL reference list based on the provided lookup criteria and returns the result as a DTO.
	 * @param lookupReqDTO {@link LookupReqDTO} The DTO containing the lookup criteria for filtering the MSL reference list.
	 * @return {@link LookupListResDTO} A DTO containing the filtered MSL reference list based on the lookup criteria.
	 */
    LookupListResDTO filterMslReferenceListBasedLookupCriteria(LookupReqDTO lookupReqDTO);
}
