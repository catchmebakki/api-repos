package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.dto.parameter.ConfigParListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListResDTO;

import java.util.Date;

/**
 * @author Praveenraja Paramsivam
 * Custom repository interface for performing specialized lookup operations on MSL reference lists.
 * This interface defines additional methods to retrieve specific data from the MSL reference lists.
 */
public interface CustomParameterParRepository {
	/**
	 * Filters the Parameter list based on the provided lookup criteria and returns the result as a DTO.
	 *
	 * @param parReqDTO  {@link ConfigParListReqDTO} The DTO containing the lookup criteria for filtering the Parameter list.
	 * @param systemDate
	 * @return {@link ConfigParListResDTO} A DTO containing the filtered Parameter list based on the lookup criteria.
	 */
	ConfigParListResDTO filterParameterBasedLookupCriteria(ConfigParListReqDTO parReqDTO, Date systemDate);
}
