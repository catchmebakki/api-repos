package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaIntvwDetRsidDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * {@code ReseaIntvwDetRsidRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaIntvwDetRsidDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_INTVW_DET_RSID table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Sitaram
 */

@Repository
public interface ReseaIntvwDetRsidRepository extends CrudRepository<ReseaIntvwDetRsidDAO, Long> {
    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic WHERE
                rsic.rsisDAO.stfDAO.userDAO.userId = :userId
                and rsic.rsicCalEventDt between :startDt and :endDt
                and rsic.rsicCalEventDispInd = 'Y'
                ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC, rsic.rsicId ASC
            """)
    List<ReseaIntvwDetRsidDAO> getInterviewDetails(@Param("userId") Long userId,
                                                   @Param("startDt") Date startDt,
                                                   @Param("endDt") Date endDt);
}
