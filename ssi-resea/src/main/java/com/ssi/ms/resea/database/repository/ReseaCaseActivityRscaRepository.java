package com.ssi.ms.resea.database.repository;


import com.ssi.ms.resea.database.dao.ReseaCaseActivityRscaDAO;
import com.ssi.ms.resea.dto.CaseActivitySummaryDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@code ReseaCaseActivityRscaRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaCaseActivityRscaDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_CASE_ACTIVITY_RSCA table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Sitaram
 */

@Repository
public interface ReseaCaseActivityRscaRepository extends CrudRepository<ReseaCaseActivityRscaDAO, Long> {
    @Transactional
    @Procedure(name = "createActivty")
    Map<String, Object> createCaseActivity(@Param("PIN_FK_RSCS_ID") Long fkRscsId,
                                                   @Param("PIN_RSCA_STAGE_CD") Long rscaStageCd,
                                                   @Param("PIN_RSCA_STATUS_CD") Long rscaStatusCd,
                                                   @Param("PIN_RSCA_TYPE_CD") Long rscaTypeCd,
                                                   @Param("pin_rsca_activty_ts") Date rscaActivityTs,
                                                   @Param("PIN_RSCA_DESCRIPTION") String rscaDescription,
                                                   @Param("PIN_RSCA_SYNOPSIS_TYPE") String rscaSynopsisType,
                                                   @Param("PIN_RSCA_CASE_SYNOPSIS") String rscaCaseSynopsis,
                                                   @Param("PIN_RSCA_MODE_CD") Long rscaModeCd,
                                                   @Param("PIN_RSCA_FOLLOWUP_TYPE_CD") Long rscaFollowUpTypeCd,
                                                   @Param("PIN_RSCA_FOLLOWUP_DT") Date rscaFollowUpDate,
                                                   @Param("PIN_RSCA_FOLLOWUP_NOTE") String rscaFollowUpNote,
                                                   @Param("PIN_RSCA_FOLLOWUP_DONE_IND") String rscaFollowUpDoneInd,
                                                   @Param("PIN_RSCA_DETAILS") String rscaDetails,
                                                   @Param("PIN_RSCA_REFERENCE_IFK_CD") Long rscaReferenceIfkCd,
                                                   @Param("PIN_RSCA_REFERENCE_IFK") Long rscaReferenceIfk,
                                                   @Param("PIN_RSCN_NOTE_CATEGORY_CD") String rscnNoteCategoryCd,
                                                   @Param("PIN_RSCN_NOTE") String rscnNote,
                                                   @Param("PIN_RSCN_SHOW_IN_NHUIS_IND") String rscnShowInNHUISInd,
                                                   @Param("PIN_CALLING_PROG") String callingProg);

    @Query("""
            select new com.ssi.ms.resea.dto.CaseActivitySummaryDTO(
                rsca.rscaId as activityId,
                rsca.rscaActivtyTs as activityDt,
                rsca.rscaStageCdALV.alvShortDecTxt as stage,
                rsca.rscaTypeCdALV.alvShortDecTxt as activity,
                CASE WHEN usr.usrTypeCd IS NULL OR usr.userId=6190381 THEN 'System'
                     WHEN usr.usrTypeCd = 677 THEN 'Staff'
                     WHEN usr.usrTypeCd = 676 THEN 'Claimant' END as user,
                rsca.rscaDescription as description,
                rsca.rscaDetails as detail,
                rscn.rscnNote as note,
                CASE WHEN :addAccess = 'Y' AND TRUNC(SYSDATE) - trunc(rsca.rscaActivtyTs) > FN_INV_GET_PARAMETER_NUMERIC('RESEA_REOPN_FUT_DAY', TRUNC(SYSDATE))
                    THEN 'Y' ELSE 'N' END as addNoteInd,
                CASE WHEN rsca.rscaFollowupDt IS NULL THEN '--' ELSE COALESCE(rsca.rscaFollowupDoneInd, 'N') END as followUpInd,
                rscaFollowupTypeCd.alvShortDecTxt as followUpType,
                rsca.rscaFollowupDt as followUpDt,
                CASE WHEN :addAccess = 'Y' AND rsca.rscaFollowupDt IS null
                    THEN 'Y' ELSE 'N' END as addFollowUpInd,
                CASE WHEN :addAccess = 'Y' AND rsca.rscaFollowupDt IS NOT null
                        AND COALESCE(rsca.rscaFollowupDoneInd , 'N') = 'N'
                    THEN 'Y' ELSE 'N' END as editFollowUpInd,
                rsca.rscaFollowupNote,
                rscaFollowupTypeCd.alvId
            ) FROM ReseaCaseActivityRscaDAO rsca
            LEFT JOIN ReseaCaseNoteRscnDAO rscn on rscn.rscaDAO.rscaId = rsca.rscaId and rscn.rscnShowInNhuisInd = 'Y'
            LEFT JOIN UserDAO usr ON TO_CHAR(usr.userId) = rsca.rscaCreatedBy
            LEFT JOIN rsca.rscaFollowupTypeCdALV rscaFollowupTypeCd
            WHERE rsca.rscsDAO.rscsId = :caseId
            """)
    List<CaseActivitySummaryDTO> getCaseActivitySummary(@Param("caseId") Long caseId,
                                                        @Param("addAccess") String addAccess,
                                                        Sort by);

    /*@Query("""
            FROM ReseaCaseActivityRscaDAO rsca
            WHERE rsca.rscsDAO.rscsId = :rscsId
              AND rsca.rscaFollowupDt = :rscsNxtFolUpDt
              AND rsca.rscaFollowupTypeCdALV.alvId = :rscsNxtFolUpTypeCd
              AND COALESCE(rsca.rscaFollowupDoneInd , 'N') = 'N'
              AND rsca.rscaId = :rscaId
            ORDER BY rsca.rscaFollowupCompDt ASC
            """)
    List<ReseaCaseActivityRscaDAO> getPreviousFolloupActivity(@Param("rscsId") Long rscsId,
                                                              @Param("rscsNxtFolUpDt") Date rscsNxtFolUpDt,
                                                              @Param("rscsNxtFolUpTypeCd") Long rscsNxtFolUpTypeCd,
                                                              @Param("rscaId") Long rscaId);*/

    @Query("""
            FROM ReseaCaseActivityRscaDAO rsca
            WHERE rsca.rscsDAO.rscsId = :rscsId
              AND rsca.rscaFollowupDt is not null
              AND rsca.rscaFollowupTypeCdALV.alvId is not null
              AND COALESCE(rsca.rscaFollowupDoneInd , 'N') = 'N'
            ORDER BY rsca.rscaFollowupDt ASC, rscaActivtyTs ASC
            """)
    List<ReseaCaseActivityRscaDAO> getNextFolloupActivity(@Param("rscsId") Long rscsId);
}