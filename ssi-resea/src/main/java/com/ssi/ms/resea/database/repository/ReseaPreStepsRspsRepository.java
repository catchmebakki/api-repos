package com.ssi.ms.resea.database.repository;


import com.ssi.ms.resea.database.dao.ReseaPreStepsRspsDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@code ReseaPreStepsRspsRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaPreStepsRspsDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_PRE_STEP_RSPS table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Sitaram
 */
@Repository
public interface ReseaPreStepsRspsRepository extends CrudRepository<ReseaPreStepsRspsDAO, Long> {

    @Query("""
            FROM ReseaPreStepsRspsDAO rsps
            WHERE rsps.claimClmDAO.clmId = :clmId
              AND rsps.rspsItemTypeCd = :itemTypeCd
            order by rsps.rspsId desc
            """)
    ReseaPreStepsRspsDAO getRspsByClmAndItemType(Long clmId, Long itemTypeCd);
}