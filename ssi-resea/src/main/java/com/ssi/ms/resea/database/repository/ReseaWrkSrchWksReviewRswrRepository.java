package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaWrkSrchWksReviewRswrDAO;
import com.ssi.ms.resea.dto.ReseaWrkSearchIssuesDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@code ReseaWrkSrchWksReviewRswrRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaWrkSrchWksReviewRswrDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_WRK_SRCH_WKS_REVIEW_RSWR table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Sitaram
 */
@Repository
public interface ReseaWrkSrchWksReviewRswrRepository extends CrudRepository<ReseaWrkSrchWksReviewRswrDAO, Long> {

    @Query("""
            SELECT new com.ssi.ms.resea.dto.ReseaWrkSearchIssuesDTO(
            rsii.nmiDAO.nmiId,
            rswr.ccaDAO.ccaWeekEndingDt,
            rswr.rswrId
            ) FROM ReseaWrkSrchWksReviewRswrDAO rswr
            LEFT JOIN ReseaIssueIdentifiedRsiiDAO rsii ON rsii.rswrDAO.rswrId = rswr.rswrId
            WHERE rswr.rsidDAO.rsidId = :rsidId
            ORDER BY rswr.ccaDAO.ccaWeekEndingDt DESC
            """)
    List<ReseaWrkSearchIssuesDTO> findInterviewWorkSearchIssues(@Param("rsidId") Long rsidId);

    @Query("""
            FROM ReseaWrkSrchWksReviewRswrDAO rswr
            LEFT JOIN ReseaIssueIdentifiedRsiiDAO rsii ON rsii.rswrDAO.rswrId = rswr.rswrId
            WHERE rswr.rsidDAO.rsidId = :rsidId
            ORDER BY rswr.ccaDAO.ccaWeekEndingDt DESC
            """)
    List<ReseaWrkSrchWksReviewRswrDAO> findInterviewWorkSearchIssuesDAO(@Param("rsidId") Long rsidId);
}
