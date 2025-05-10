package com.ssi.ms.resea.database.repository;


import com.ssi.ms.resea.database.dao.ReseaReschDetRsrsDAO;
import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * {@code ReseaRescheduleRequestRsrsRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaReschDetRsrsDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_RESCH_DET_RSRS table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Anand
 */

@Repository
public interface ReseaRescheduleRequestRsrsRepository extends CrudRepository<ReseaReschDetRsrsDAO, Long> {


    @Query("""
            SELECT new com.ssi.ms.resea.dto.HeaderWorkSrchDetailsDTO(cca.ccaWeekEndingDt, COUNT(wws.wwsId) AS CONTACTS,
            COUNT(CASE WHEN wsac.wsacActivityLevelCdALV.alvId = 4704 OR wsac.wsacTypeCdALV.alvId = 4707 THEN 1 END) AS GOLD,
            COUNT(CASE WHEN wsac.wsacActivityLevelCdALV.alvId = 4706 THEN 1 END) AS BRONZE, wscm.wscmMinReq,
            (CASE WHEN COUNT(wws.wwsId) < wscm.wscmMinReq THEN 'Yes' ELSE 'No' END) AS INADEQUATE_WS, cca.ccaId, 'Y')
            FROM CcApplnCcaDAO cca
            LEFT JOIN WeeklyWorkSearchWwsDAO wws ON wws.ccaDAO.ccaId = cca.ccaId
            LEFT JOIN WsClmMinWeeklyReqWscmDAO wscm ON wscm.clmDAO.clmId = cca.clmDAO.clmId
            LEFT JOIN WsActivitiesWsaDAO wsa ON wsa.ccaDAO.ccaId = cca.ccaId
            LEFT JOIN WsActivitiesConfWsacDAO wsac ON wsac.wsacId = wsa.wsacDAO.wsacId
            WHERE cca.clmDAO.clmId = :clmId
            AND cca.ccaWeekEndingDt between COD('01/01/2024') AND TRUNC(:rsicCreateDt)
            AND cca.ccaWeekEndingDt BETWEEN wsac.wsacEffFromDt AND COALESCE(wsac.wsacEffToDt, COD('12/31/2099'))
            GROUP BY wws.cmtDAO.cmtId, cca.ccaWeekEndingDt, wscm.wscmMinReq, cca.ccaId
            ORDER BY cca.ccaWeekEndingDt DESC
            """)
    List<ReseaRescheduleGetAvailableSlotsResDTO> getWorkSearchDetails(@Param("clmId") Long clmId,
                                                                      @Param("rsicCreateDt") Date rsicCreateDt);


}