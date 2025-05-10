package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.IssueDecisionDecDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IssueDecisionDecRepository extends CrudRepository<IssueDecisionDecDAO, Long> {
    @Query("""
            FROM IssueDecisionDecDAO dec LEFT JOIN dec.decStatusCdAlv decStatusCd LEFT JOIN dec.decDecisionCdAlv decDecisionCd
            WHERE dec.clmDAO.clmId = :clmId
            AND dec.decId = dec.decNbr AND dec.fkMonId IS NULL
            AND dec.nmiDAO.nmiDisplayInd = 'Y'
            AND (decStatusCd.alvId = 281 or (decStatusCd.alvId = 283 AND decDecisionCd.alvId = 270) or (dec.decEndDt is null AND decDecisionCd.alvId = 270))
            AND dec.decCreatedTs <= :rsicCreateDt
            ORDER BY dec.decDetectionDt asc, dec.decId asc
            """)
    List<IssueDecisionDecDAO> getOpenDenyNonMonIssue(@Param("clmId") Long clmId,
                                                     @Param("rsicCreateDt") Date rsicCreateDt);


    
    @Query("""
    		SELECT COUNT(*)
            FROM IssueDecisionDecDAO dec LEFT JOIN dec.decStatusCdAlv decStatusCd LEFT JOIN dec.decDecisionCdAlv decDecisionCd
            JOIN ReseaIntvwerCalRsicDAO rsic ON rsic.claimDAO.clmId = dec.clmDAO.clmId
            JOIN ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId 
            WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds ) AND rsic.rsicCalEventDt >= :fromDate         
            AND dec.decId = dec.decNbr AND dec.nmiDAO.nmiId = 2667
            AND (decStatusCd.alvId = 281 OR (decStatusCd.alvId = 283 AND decDecisionCd.alvId = 270) OR (dec.decEndDt is null AND decDecisionCd.alvId = 270))
            AND dec.decEndDt <= :today AND dec.decBeginDt >=  :fromDate 
            """)
    Long getInadqWSWksByCaseMgrAndPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds,
                                         @Param("fromDate") Date fromDate,
                                         @Param("today") Date today);
    
    @Query("""
    		SELECT COUNT(distinct clm.claimantDAO.cmtId)
            FROM IssueDecisionDecDAO dec LEFT JOIN dec.decStatusCdAlv decStatusCd LEFT JOIN dec.decDecisionCdAlv decDecisionCd
            JOIN ReseaIntvwerCalRsicDAO rsic ON rsic.claimDAO.clmId = dec.clmDAO.clmId
            JOIN ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId 
            JOIN ClaimClmDAO clm ON dec.clmDAO.clmId = clm.clmId
            WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds ) AND rsic.rsicCalEventDt >= :fromDate         
            AND dec.decId = dec.decNbr AND dec.nmiDAO.nmiId = 2667
            AND (decStatusCd.alvId = 281 OR (decStatusCd.alvId = 283 AND decDecisionCd.alvId = 270) OR (dec.decEndDt is null AND decDecisionCd.alvId = 270))
            AND dec.decEndDt <= :today AND dec.decBeginDt >=  :fromDate 
            """)
    Long getInadqWSCmtsByCaseMgrAndPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds,
                                          @Param("fromDate") Date fromDate,
                                          @Param("today") Date today);

    @Query("""
            FROM IssueDecisionDecDAO dec LEFT
            JOIN dec.decStatusCdAlv decStatusCd
            LEFT JOIN dec.decDecisionCdAlv decDecisionCd
            WHERE dec.clmDAO.clmId = :clmId
              AND dec.decId = dec.decNbr AND dec.fkMonId IS NULL
              AND dec.nmiDAO.nmiDisplayInd = 'Y'
              AND (decStatusCd.alvId = 281 or (decStatusCd.alvId = 283 AND decDecisionCd.alvId = 270) or (dec.decEndDt is null AND decDecisionCd.alvId = 270))
              AND dec.decBeginDt = :decBeginDt
              AND COALESCE(dec.decEndDt, COD('12/31/2999')) >= COALESCE(:decEndDt, COD('12/31/2999'))
              AND dec.nmiDAO.nmiId = :nmiId
              AND NOT EXISTS (
                SELECT 1 FROM ReseaIssueIdentifiedRsiiDAO rsii WHERE rsii.issueDecisionDecDAO.decId = dec.decId
              )
            ORDER BY dec.decDetectionDt asc, dec.decId asc
            """)
    List<IssueDecisionDecDAO> checkIssueExists(@Param("clmId") Long clmId,
                                               @Param("nmiId") Long nmiId,
                                               @Param("decBeginDt") Date decBeginDt,
                                               @Param("decEndDt") Date decEndDt);
}
