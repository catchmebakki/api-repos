package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.WsCpsConfigWsccDAO;
import com.ssi.ms.configuration.database.repository.custom.CustomWsccRepository;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WsCpsConfigWsccRepository extends CrudRepository<WsCpsConfigWsccDAO, Long>,
        JpaRepository<WsCpsConfigWsccDAO, Long>, CustomWsccRepository {
    Page<WsCpsConfigWsccDAO> findAll(Pageable pageable);

    @Query("""
            from WsCpsConfigWsccDAO wscc where
            TRUNC(SYSDATE) BETWEEN wscc.wsccEffectiveDt AND COALESCE(wscc.wsccExpirationDt, TRUNC(SYSDATE))
            """)
    Page<WsCpsConfigWsccDAO> findActiveWsccDaos(Pageable pageable);

    @Query("""
            from WsCpsConfigWsccDAO wscc where
            wscc.wsccExpirationDt is not null and TRUNC(sysdate) > wscc.wsccExpirationDt
            """)
    Page<WsCpsConfigWsccDAO> findInactiveWsccDaos(Pageable pageable);

    @Query("""
            select new com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO(wsccId,
            clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt,
            wsccMinIcWsReq, wsccMinAcWsReq, wsccMinWsIncrFreq, wsccMinWsIncrVal, wsccEffectiveDt, wsccExpirationDt)
            from WsCpsConfigWsccDAO wscc where wsccId != :wsccId
            and wscc.clmProgSpecCpsDAO.cpsId in (
                select wscca.clmProgSpecCpsDAO.cpsId from WsCpsConfigWsccDAO wscca where wscca.wsccId = :wsccId
            ) order by wscc.wsccEffectiveDt desc
            """)
    List<ConfigWsccListItemResDTO> findChildListByWsccId(@Param("wsccId") Long wsccId);

    @Query("""
            select new com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO(wsccId,
            clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt,
            wsccMinIcWsReq, wsccMinAcWsReq, wsccMinWsIncrFreq, wsccMinWsIncrVal, wsccEffectiveDt, wsccExpirationDt)
            from WsCpsConfigWsccDAO wscc where wsccId != :wsccId
            and wscc.clmProgSpecCpsDAO.cpsId in (
                select wscca.clmProgSpecCpsDAO.cpsId from WsCpsConfigWsccDAO wscca where wscca.wsccId = :wsccId
            )
            and TRUNC(SYSDATE) BETWEEN wscc.wsccEffectiveDt AND COALESCE(wscc.wsccExpirationDt, TRUNC(SYSDATE))
            order by wscc.wsccEffectiveDt desc
            """)
    List<ConfigWsccListItemResDTO> findActiveChildListByWsccId(@Param("wsccId") Long wsccId);

    @Query("""
            select new com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO(wsccId,
            clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt,
            wsccMinIcWsReq, wsccMinAcWsReq, wsccMinWsIncrFreq, wsccMinWsIncrVal, wsccEffectiveDt, wsccExpirationDt)
            from WsCpsConfigWsccDAO wscc where wsccId != :wsccId
            and wscc.clmProgSpecCpsDAO.cpsId in (
                select wscca.clmProgSpecCpsDAO.cpsId from WsCpsConfigWsccDAO wscca where wscca.wsccId = :wsccId
            )
            AND (TRUNC (SYSDATE) > wscc.wsccExpirationDt OR TRUNC (SYSDATE) < wscc.wsccEffectiveDt)
            order by wscc.wsccEffectiveDt desc
            """)
    List<ConfigWsccListItemResDTO> findInactiveChildListByWsccId(@Param("wsccId") Long wsccId);

    @Query("""
            from WsCpsConfigWsccDAO wscc where wsccId != :wsccId
            and wscc.clmProgSpecCpsDAO.cpsId in (
                select wscca.clmProgSpecCpsDAO.cpsId from WsCpsConfigWsccDAO wscca where wscca.wsccId = :wsccId
            )
            and wscc.wsccExpirationDt = :wsccExpirationDt
            order by wscc.wsccEffectiveDt desc
            """)
    List<WsCpsConfigWsccDAO> findByWsccCategoryAndExpDate(@Param("wsccId") Long wsccId,
                                                       @Param("wsccExpirationDt") Date wsccExpirationDt);
}
