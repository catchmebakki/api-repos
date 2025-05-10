package com.ssi.ms.resea.database.repository;


import com.ssi.ms.resea.database.dao.ReseaReturnToWorkRsrwDAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * {@code ReseaReturnToWorkRsrwRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaReturnToWorkRsrwDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_RTW_DET_RSRW table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Anand
 */
@Repository
public interface ReseaReturnToWorkRsrwRepository extends CrudRepository<ReseaReturnToWorkRsrwDAO, Long> {

	@Query("""
			SELECT AVG(rsrw.rsrwNewEmpStartDt - clm.clmEffectiveDt)/7
			FROM ReseaRtwDetRsrwDAO rsrw
			JOIN ReseaIntvwerCalRsicDAO rsic ON rsic.rsicId = rsrw.rsicDAO.rsicId
			JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId = rsic.rsisDAO.rsisId 
			JOIN ClaimClmDAO clm ON clm.clmId = rsic.claimDAO.clmId
			WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds ) AND rsic.rsicCalEventDt >= :fromDate 
			""")
	BigDecimal getAvgRTWWksByCaseMgrPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate);


}