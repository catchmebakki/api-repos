package com.ssi.ms.masslayoff.database.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ssi.ms.masslayoff.database.dao.CliamantCmtDAO;

import java.util.List;
/**
 * @author Praveenraja Paramsivam
 * Repository for accessing and managing ClaimantCmtDAO entities.
 */
public interface CliamantRepository extends CrudRepository<CliamantCmtDAO, Long> {
	CliamantCmtDAO findBySsn(String ssn);
	 /**
     * Gets Claimant objects by SSN that are not associated with a specific MLRL.
     * @param ssn  {@link String}  The Social Security Number (SSN) to search for.
     * @param mlrlId {@link Long}  ID of the MLRL (Mass Layoff Request List) to check association with.
     * @return {@link List} list of ClaimantCmtDAO entities not associated with the specified MLRL.
     */
	@Query("""
   			from CliamantCmtDAO cmt
   				join MslEntryCmtMlecDAO mlec
   					on mlec.mlecSsn LIKE cmt.ssn
   			where cmt.ssn like :ssn
   				and mlec.mslRefListMlrlDAO.mlrlId = :mlrlId
			""")
	List<CliamantCmtDAO> findBySsnAndNotAssociatedToMLRL(String ssn, Long mlrlId);
}
