package com.ssi.ms.masslayoff.database.repository.msl;

import com.ssi.ms.masslayoff.database.dao.MslUploadErrorMuseDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author munirathnam.surepall
 * Repository interface for managing MslUploadErrorMuseDAO entities.
 */
@Repository
public interface MslUploadErrorMuseRepository extends CrudRepository<MslUploadErrorMuseDAO, Long> {

	/**
	 *This save function will save into Database.
     * @param mslUploadErrorMuseDAO {@link MslUploadErrorMuseDAO The MslUploadErrorMuseDAO object to be saved.
     * @return {@link MslUploadErrorMuseDAO} The saved MslUploadErrorMuseDAO object
	 */
    MslUploadErrorMuseDAO save(MslUploadErrorMuseDAO mslUploadErrorMuseDAO);

    /**
     * This function, to get all upload summary details.
     * @param mslUploadSummaryMusm {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO to filter the results.
     * @return {@link List<MslUploadErrorMuseDAO>} A list of MslUploadErrorMuseDAO objects that belong to the specified MslUploadSummaryMusmDAO.
     */
    List<MslUploadErrorMuseDAO> getAllByMslUploadSummaryMusm(MslUploadSummaryMusmDAO mslUploadSummaryMusm);

}
