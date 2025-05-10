package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaIssueIdentifiedRsiiDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@code ReseaIssueIdentifiedRsiiRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaIssueIdentifiedRsiiDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_ISSUE_IDENTIFIED_RSII table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Sitaram
 */
@Repository
public interface ReseaIssueIdentifiedRsiiRepository extends CrudRepository<ReseaIssueIdentifiedRsiiDAO, Long> {
    @Query("""
        FROM ReseaIssueIdentifiedRsiiDAO rsii WHERE rsii.rsicDAO.rsicId = :rsicId
        AND rsii.rsiiSourceIfkCd.alvId = :sourceIfk
        ORDER BY rsii.rsiiIssueEndDt DESC
    """)
    List<ReseaIssueIdentifiedRsiiDAO> getInterviewIssues(@Param("rsicId") Long rsicId,
                                                         @Param("sourceIfk") Long sourceIfk);
}
