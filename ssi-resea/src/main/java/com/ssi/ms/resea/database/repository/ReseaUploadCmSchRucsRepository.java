package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchErrorRuseDAO;
import com.ssi.ms.resea.dto.upload.ReseaUploadSummaryResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReseaUploadCmSchRucsRepository extends CrudRepository<ReseaUploadCmSchRucsDAO, Long> {
    @Query("""
            SELECT new com.ssi.ms.resea.dto.upload.ReseaUploadSummaryResDTO(
                rucs.rucsId,
                rucs.stfDAO.userDAO.userId,
                rucs.stfDAO.staffName,
                rucs.lofDAO.lofId,
                rucs.lofDAO.lofName,
                rucs.rucsEffectiveDt,
                rucs.rucsExpirationDt,
                rucs.rucsStatusCdAlv.alvShortDecTxt,
                CASE WHEN COALESCE(rusm.rusmNumErrs, 0) > 0 OR rusmStatusCd.alvId IN ( :rusmStatusCdInd )
                     THEN 'Y' ELSE 'N' END AS ERRORIND,
                CASE WHEN COALESCE(rusm.rusmNumErrs, 0) > 0 THEN 'N'
                     WHEN EXISTS (SELECT 1 FROM ReseaUploadCmSchDtlsRucdDAO rucd
                                        WHERE rucd.rusmDAO.rusmId=rusm.rusmId)
                     THEN 'Y' ELSE 'N' END AS VIEWIND,
                CASE WHEN COALESCE(rusm.rusmNumErrs, 0) > 0 THEN 'N'
                     WHEN EXISTS (SELECT 1 FROM ReseaUploadCmSchDtlsRucdDAO rucd
                                        WHERE rucd.rusmDAO.rusmId=rusm.rusmId)
                             AND rucs.rucsStatusCdAlv.alvId = :rucsStatusCdFinalInd
                     THEN 'Y' ELSE 'N' END AS FINALIND,
                CASE WHEN rucs.rucsStatusCdAlv.alvId NOT IN ( :rucsStatusCdUploadInd ) THEN 'Y' ELSE 'N' END AS UPLOADIND,
                CASE WHEN rucs.rucsStatusCdAlv.alvId NOT IN ( :rucsStatusCdDiscardInd ) THEN 'Y' ELSE 'N' END AS DISCARDIND
            )
            FROM ReseaUploadCmSchRucsDAO rucs
            LEFT JOIN ReseaUploadSchSummaryRusmDAO rusm ON rusm.rucsDAO.rucsId = rucs.rucsId
                AND rusm.rusmReqTs = (SELECT max(rusmA.rusmReqTs) FROM ReseaUploadSchSummaryRusmDAO rusmA WHERE rusmA.rucsDAO.rucsId = rucs.rucsId)
            LEFT JOIN rusm.rusmStatusCdAlv rusmStatusCd
            WHERE rucs.stfDAO.userDAO.userId IN ( :caseManagerId )
            AND rucs.rucsEffectiveDt >= :effectiveStartDt
            AND COALESCE(rucs.rucsExpirationDt, COD('12/31/2099')) <= COALESCE( :effectiveEndDt, COD('12/31/2099'))
            ORDER BY rucs.stfDAO.staffName ASC, rucs.rucsEffectiveDt ASC, rucs.rucsExpirationDt ASC
            """)
    List<ReseaUploadSummaryResDTO> getFileUploadSummary(@Param("caseManagerId") List<Long> caseManagerId,
                                                        @Param("effectiveStartDt") Date effectiveStartDt,
                                                        @Param("effectiveEndDt") Date effectiveEndDt,
                                                        @Param("rucsStatusCdFinalInd") Long rucsStatusCdFinalInd,
                                                        @Param("rucsStatusCdUploadInd") List<Long> rucsStatusCdUploadInd,
                                                        @Param("rucsStatusCdDiscardInd") List<Long> rucsStatusCdDiscardInd,
                                                        @Param("rusmStatusCdInd") List<Long> rusmStatusCdInd);

    @Query("""
            SELECT MAX(rucs.rucsId) FROM ReseaUploadCmSchRucsDAO rucs
            WHERE rucs.stfDAO.userDAO.userId = :userdId
              AND rucs.lofDAO.lofId = :lofId
              AND rucs.rucsStatusCdAlv.alvId NOT IN ( :statusCd )
            """)
    Long checkPendingRucsExists(@Param("userdId") Long userdId,
                                  @Param("lofId") Long lofId,
                                  @Param("statusCd") List<Long> statusCd);

    @Query("""
            FROM ReseaUploadCmSchRucsDAO rucs
            WHERE rucs.stfDAO.stfId = :stfId
              AND rucs.lofDAO.lofId = :lofId
              AND rucs.rucsStatusCdAlv.alvId = :statusCd
              AND COALESCE(rucs.rucsExpirationDt, COD('12/31/2099')) >= :rucsEffectiveDt
            """)
    ReseaUploadCmSchRucsDAO findPrevOpenFinalRucs(@Param("statusCd") Long statusCd,
									    		  @Param("stfId") Long stfId, 
									    		  @Param("lofId") Long lofId, 
									    		  @Param("rucsEffectiveDt") Date rucsEffectiveDt);
}
