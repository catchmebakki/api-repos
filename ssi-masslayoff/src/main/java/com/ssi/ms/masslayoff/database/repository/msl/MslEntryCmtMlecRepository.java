package com.ssi.ms.masslayoff.database.repository.msl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
/**
 * Repository interface for managing MslEntryCmtMlecDAO entities.
 */
@Repository
public interface MslEntryCmtMlecRepository extends CrudRepository<MslEntryCmtMlecDAO, Long> {

    Page<MslEntryCmtMlecDAO> findByMslRefListMlrlDAO(MslRefListMlrlDAO mslRefListMlrlDAO, Pageable pageable);

    /**
     * This function will retun list of Cliamant records for the provided ssn and mrlrId.
     * @param ssn
     * @param mlrlId {@link Long} The Mass Layoff Request ID to filter the results.
     * @return {@link Page<MslEntryCmtMlecDAO>} A paged list of MslEntryCmtMlecDAO objects that belong to the specified
     * Mass Layoff Request.
     */
    @Query("""
            from MslEntryCmtMlecDAO mlec
            where mlec.mslRefListMlrlDAO.mlrlId= :mlrlId
            and mlec.mlecSsn LIKE CONCAT('%',:mlecSsn,'%')
           """)
    Page<MslEntryCmtMlecDAO> findByMrlIdAndSsn(@Param("mlrlId") Long mlrlId, @Param("mlecSsn") String ssn, Pageable pageable);

    /**
     * This function using for find the msl ref status.
     * @param mlecIds {@link List<Long>}
     * @param mlrlStatusCd {@link Integer}
     * @return {@link List<Long>}
     */
    @Query("""
            select mlec.mlecId from MslEntryCmtMlecDAO mlec
            where mlec.mlecId in (:mlecId)
            and mlec.mslRefListMlrlDAO.mlrlStatusCd = :mlrlStatusCd
           """)
    List<Long> findByMslRefStatus(@Param("mlecId") List<Long> mlecIds, @Param("mlrlStatusCd") Integer mlrlStatusCd);
    /**
     * This function is to update StatusCd.
     * @param mlecIdS {@link List<Long>}
     * @param statusCd {@link Integer}
     */
    @Transactional
    @Modifying
    @Query("UPDATE MslEntryCmtMlecDAO SET mlecStatusCd= :statusCd  WHERE mlecId IN :mlecIdS")
    void updateStatusCdByIds(List<Long> mlecIdS, Integer statusCd);

	/**
	 * This function get the mlrl statuscd and mlec statuscd.
	 * @param mlrlId {@link Long} The Mass Layoff Request ID to filter the results.
	 * @return {@link List<Object[]>} A list of Object arrays containing counts grouped by MLEC status code.
     * Each array contains two elements: MLEC status code and its corresponding count.
	 */
	@Query(value = """
			SELECT count(mlec.MLEC_ID),
			    mlec.MLEC_STATUS_CD
			FROM MSL_ENTRY_CMT_MLEC mlec
			WHERE mlec.FK_MLRL_ID = :mlrlId
			GROUP BY mlec.MLEC_STATUS_CD""", nativeQuery = true)
    List<Object[]> getCountByMlrlStatusCdGroupByMlecStatusCd(@Param("mlrlId") Long mlrlId);

    /**
     * This method, to find SSN number is there or not in Mlrl table.
     * @param ssnList {@link List<String>} The list of Social Security Numbers (SSNs) to search for.
     * @param mlrlId {@link Long} The Mass Layoff Request ID to filter the results.
     * @return {@link List<String>} A list of SSNs that match the provided criteria.
     */
    @Query("""
            select distinct mlec.mlecSsn from MslEntryCmtMlecDAO mlec
            where mlec.mlecSsn in (:ssnList)
            and mlec.mslRefListMlrlDAO.mlrlId = :mlrlId
           """)
    List<String> findSsnBySsnListAndMlrlId(List<String> ssnList, Long mlrlId);

    /**
     * This function, to get list of Cliamant records particular mlrlId.
     * @param mlrlId {@link Long} The Mass Layoff Request ID to filter the results.
     * @return {@link List<MslEntryCmtMlecDAO>} A list of MslEntryCmtMlecDAO objects that belong to the specified
     * Mass Layoff Request.
     */
    List<MslEntryCmtMlecDAO> findAllByMslRefListMlrlDAOMlrlId(@Param("mlrlId") Long mlrlId);

    /**
     * This function will retun list of Cliamant records for the provided ssn and mlrlId.
     * @param ssn
     * @param mlrlId {@link Long} The Mass Layoff Request ID to filter the results.
     * @return {@link List<MslEntryCmtMlecDAO>} A list of MslEntryCmtMlecDAO objects that belong to the specified
     * Mass Layoff Request.
     */
    @Query(" from MslEntryCmtMlecDAO mlec where mlec.mlecSsn = :ssn and mlec.mslRefListMlrlDAO.mlrlId = :mlrlId ")
    List<MslEntryCmtMlecDAO> findBySsnAndMlrlId(String ssn, Long mlrlId);

}