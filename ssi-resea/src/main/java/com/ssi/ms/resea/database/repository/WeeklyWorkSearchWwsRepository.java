package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.WeeklyWorkSearchWwsDAO;
import com.ssi.ms.resea.dto.HeaderWorkSrchDetailsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeeklyWorkSearchWwsRepository extends CrudRepository<WeeklyWorkSearchWwsDAO, Long> {
    @Query("""
            SELECT new com.ssi.ms.resea.dto.HeaderWorkSrchDetailsDTO(cca.ccaWeekEndingDt, COUNT(distinct wws.wwsId) AS CONTACTS,
            COUNT(distinct CASE WHEN wsacActivityLevel.alvId = 4704 OR wsacType.alvId = 4707 THEN wsac.wsacId END) AS GOLD,
            COUNT(distinct CASE WHEN wsacActivityLevel.alvId = 4706 THEN wsac.wsacId END) AS BRONZE, wscm.wscmMinReq,
            (CASE WHEN ccai.ccaiWsReqNotMetAckInd = 'Y' THEN 'Yes' ELSE 'No' END) AS INADEQUATE_WS, cca.ccaId, 'Y')
            FROM CcApplnCcaDAO cca
            LEFT JOIN WeeklyWorkSearchWwsDAO wws ON wws.ccaDAO.ccaId = cca.ccaId
            LEFT JOIN CcaInprogressCcaiDAO ccai on ccai.ccaDAO.ccaId = cca.ccaId
            LEFT JOIN WsClmMinWeeklyReqWscmDAO wscm ON wscm.clmDAO.clmId = cca.clmDAO.clmId
            AND cca.ccaWeekEndingDt BETWEEN wscm.wscmStartDt AND COALESCE(wscm.wscmEndDt, COD('12/31/2099'))
            LEFT JOIN WsActivitiesWsaDAO wsa ON wsa.ccaDAO.ccaId = cca.ccaId
            LEFT JOIN WsActivitiesConfWsacDAO wsac ON wsac.wsacId = wsa.wsacDAO.wsacId
            AND cca.ccaWeekEndingDt BETWEEN wsac.wsacEffFromDt AND COALESCE(wsac.wsacEffToDt, COD('12/31/2099'))
            LEFT JOIN wsac.wsacActivityLevelCdALV wsacActivityLevel
            LEFT JOIN wsac.wsacTypeCdALV wsacType
            WHERE cca.clmDAO.clmId = :clmId
            AND cca.ccaWeekEndingDt <= TRUNC(:rsicCreateDt)
            GROUP BY wws.cmtDAO.cmtId, cca.ccaWeekEndingDt, wscm.wscmMinReq, cca.ccaId, ccai.ccaiWsReqNotMetAckInd
            ORDER BY cca.ccaWeekEndingDt DESC
            """)
    List<HeaderWorkSrchDetailsDTO> getWorkSearchDetails(@Param("clmId") Long clmId,
                                                        @Param("rsicCreateDt") Date rsicCreateDt);
    @Query("""
        SELECT cca.ccaWeekEndingDt FROM CcApplnCcaDAO cca
        LEFT JOIN WeeklyWorkSearchWwsDAO wws ON wws.ccaDAO.ccaId = cca.ccaId
        LEFT JOIN WsClmMinWeeklyReqWscmDAO wscm ON wscm.clmDAO.clmId = cca.clmDAO.clmId
        AND cca.ccaWeekEndingDt BETWEEN wscm.wscmStartDt AND COALESCE(wscm.wscmEndDt, COD('12/31/2099'))
        LEFT JOIN WsActivitiesWsaDAO wsa ON wsa.ccaDAO.ccaId = cca.ccaId
        LEFT JOIN WsActivitiesConfWsacDAO wsac ON wsac.wsacId = wsa.wsacDAO.wsacId
        AND cca.ccaWeekEndingDt BETWEEN wsac.wsacEffFromDt AND COALESCE(wsac.wsacEffToDt, COD('12/31/2099'))
        WHERE cca.clmDAO.clmId = :clmId
        AND cca.ccaWeekEndingDt <= TRUNC(:rsicCreateDt)
        GROUP BY wws.cmtDAO.cmtId, cca.ccaWeekEndingDt, wscm.wscmMinReq
        ORDER BY cca.ccaWeekEndingDt DESC
    """)
    List<Date> getLatestCCA(@Param("clmId") Long clmId,
                            @Param("rsicCreateDt") Date rsicCreateDt);


}
