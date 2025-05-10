package com.ssi.ms.masslayoff.database.repository.msl;

import com.ssi.ms.masslayoff.database.dao.MslEmployeesMleDAO;
import org.springframework.data.repository.CrudRepository;

/**
 * @author munirathnam.surepall
 *Repository interface for managing MslUploadSummaryMusmDAO entities.
 */
public interface MslEmployeesMleRepository extends CrudRepository<MslEmployeesMleDAO, Long> {
    /**
     * This save function will save into Database.
     *
     * @param mslEmployeesDAO {@link mslEmployeesDAO} The MassLayoffMslDAO object to be saved.
     * @return {@link MslEmployeesMleDAO} The saved MassLayoffMslDAO object.
     */
    MslEmployeesMleDAO save(MslEmployeesMleDAO mslEmployeesDAO);
}
