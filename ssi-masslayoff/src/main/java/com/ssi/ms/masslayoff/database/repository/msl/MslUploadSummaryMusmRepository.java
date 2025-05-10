package com.ssi.ms.masslayoff.database.repository.msl;

import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
/**
 * @author munirathnam.surepall
 *Repository interface for managing MslUploadSummaryMusmDAO entities.
 */
public interface MslUploadSummaryMusmRepository extends CrudRepository<MslUploadSummaryMusmDAO, Long> {
	/**
	 *This save function will save into Database.
	 *@param uploadMassLayOffReqDTO {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object to be saved.
     *@return {@link MslUploadSummaryMusmDAO} The saved MslUploadSummaryMusmDAO object.
	 */
    MslUploadSummaryMusmDAO save(MslUploadSummaryMusmDAO uploadMassLayOffReqDTO);
    /**
     * Retrieve a list of MslUploadSummaryMusmDAO instances associated with the given MSL reference list ID.
     * @param mlrlId {@link long} The ID of the MSL reference list to retrieve associated upload summaries for.
     * @return {@link List<MslUploadSummaryMusmDAO>} List of MslUploadSummaryMusmDAO instances associated with the provided MSL reference list ID.
     */
    @Query(" from MslUploadSummaryMusmDAO musmDao where musmDao.mslRefListMlrlDAO.mlrlId = :mlrlId order by musmCreatedBy ")
    List<MslUploadSummaryMusmDAO> findAllByMslRefListMlrlDAO(long mlrlId);
}
