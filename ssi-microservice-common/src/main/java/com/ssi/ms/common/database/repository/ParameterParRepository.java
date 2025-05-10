package com.ssi.ms.common.database.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssi.ms.common.database.dao.ParameterParDao;

/**
 * @author Praveenraja Paramsivam
 *Repository interface for managing ParameterParDao entities.
 */
@Repository
public interface ParameterParRepository extends CrudRepository<ParameterParDao, Long> {

	/**
	 * This function, giving input as parShortName and find maxMonth value.
	* @param parShortName {@link String} The short name to search for.
    * @return {@link ParameterParDao} The retrieved ParameterParDao instance matching the short name and date criteria.
    */
	@Query("""
			from ParameterParDao par where upper(par.parShortName) like upper(:parShortName)
			and TRUNC(sysdate) between par.parEffectiveDate and COALESCE(par.parExpirationDate, TRUNC (SYSDATE))
			""")
	ParameterParDao findByParShortName(String parShortName);

	/**
	 * Retrieve the current timestamp from the database.
	 *
	 * @return {@link Timestamp} The current timestamp as a java.sql.Timestamp instance.
	 */
	@Query(value = "SELECT SYSDATE FROM DUAL", nativeQuery = true)
	java.sql.Timestamp getCurrentTimestamp();

	@Query(value = "SELECT TRUNC(SYSDATE) FROM DUAL", nativeQuery = true)
	java.util.Date getCurrentDate();
}
