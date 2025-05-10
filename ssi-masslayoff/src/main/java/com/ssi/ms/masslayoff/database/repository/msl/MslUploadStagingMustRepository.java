package com.ssi.ms.masslayoff.database.repository.msl;

import com.ssi.ms.masslayoff.database.dao.MslUploadStagingMustDAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * @author munirathnam.surepall
 *Repository interface for managing MslUploadStagingMustDAO entities.
 */
public interface MslUploadStagingMustRepository extends CrudRepository<MslUploadStagingMustDAO, Long> {
	/**
	 * This function is used for to get msl summary details.
	 * @param musmId {@link Long} The MusmId of the MslUploadSummaryMusmDAO to search for.
     * @return {@link List<MslUploadStagingMustDAO>} A list of MslUploadStagingMustDAO objects that belong to the specified MusmId.
	 */
	List<MslUploadStagingMustDAO> findByMslUploadSummaryMusmDAOMusmId(Long musmId);

}
