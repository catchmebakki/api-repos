package com.ssi.ms.masslayoff.database.repository.msl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.database.repository.msl.custom.CustomRepositoryMslReferenceListLookup;

/**
 * @author munirathnam.surepall
 *Repository interface for managing MslRefListMlrlDAO entities.
 */
@Repository
public interface MslReferenceListMlrlRepository
        extends JpaRepository<MslRefListMlrlDAO, Long>, CustomRepositoryMslReferenceListLookup {
	/**
	 * This save function will save into Database.
	 * @param massLayOffReqDTO {@link MslRefListMlrlDAO} The Mass Layoff Request DTO object to be saved.
     * @return {@link MslRefListMlrlDAO} The saved Mass Layoff Request DTO object.
	 */
	MslRefListMlrlDAO save(MslRefListMlrlDAO massLayOffReqDTO);
	/**
	 * This function used for find MslNum there or not.
	 * @param massLayOffNum {@link long} The Mass Layoff Number to search for.
     * @return {@link List<MslRefListMlrlDAO>} A list of Mass Layoff Request DTO objects that match the provided Mass Layoff Number.
	 */
	List<MslRefListMlrlDAO> findByMlrlMslNum(long massLayOffNo);
}