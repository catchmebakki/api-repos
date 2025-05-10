package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.WsAutoWvrConfigWswcDAO;
import com.ssi.ms.configuration.database.repository.custom.CustomWswcRepository;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO;
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
public interface WsAutoWvrConfigWswcRepository extends CrudRepository<WsAutoWvrConfigWswcDAO, Long>,
        JpaRepository<WsAutoWvrConfigWswcDAO, Long>, CustomWswcRepository {


    //SELECT wswc.*, RANK () OVER (PARTITION BY FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD
    // ORDER BY WSWC_EFFECTIVE_DT DESC, WSWC_ID DESC) rnk
    //FROM WS_AUTO_WVR_CONFIG_WSWC wswc

    @Query(value = """
    SELECT new com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO(
        WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC,
        WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD, ALV_SHORT_DESC_TXT)
    FROM (
        SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC,
        WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD
        FROM WS_AUTO_WVR_CONFIG_WSWC
        WHERE TRUNC(SYSDATE) BETWEEN WSWC_EFFECTIVE_DT AND COALESCE(WSWC_EXPIRATION_DT, TRUNC(SYSDATE))
        UNION
        SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC,
        WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD FROM (
        SELECT wswc.*, RANK () OVER (PARTITION BY FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD
                ORDER BY WSWC_EFFECTIVE_DT DESC, WSWC_ID DESC) rnk
         FROM WS_AUTO_WVR_CONFIG_WSWC wswc WHERE
         NOT EXISTS(SELECT 1 FROM WS_AUTO_WVR_CONFIG_WSWC wswc1
            WHERE wswc.FK_LOF_ID=wswc1.FK_LOF_ID AND wswc.WSWC_SCENARIO_NUM_EVENT_CD=wswc1.WSWC_SCENARIO_NUM_EVENT_CD
            AND TRUNC(SYSDATE) BETWEEN WSWC_EFFECTIVE_DT AND COALESCE(WSWC_EXPIRATION_DT, TRUNC(SYSDATE)))
    )) JOIN ALLOW_VAL_ALV ON ALV_ID=WSWC_REASON_CD
            """, nativeQuery = true)
    Page<ConfigWswcListItemResDTO> findAllEntries(Pageable pageable);

    @Query("""
            from WsAutoWvrConfigWswcDAO wswc where (
                TRUNC(SYSDATE) BETWEEN wswc.wswcEffectiveDt AND COALESCE(wswc.wswcExpirationDt, TRUNC(SYSDATE))
            )
            """)
    Page<WsAutoWvrConfigWswcDAO> findActiveWswcDaos(Pageable pageable);

    @Query("""
            from WsAutoWvrConfigWswcDAO wswc where wswc.wswcExpirationDt is not null and TRUNC(sysdate) > wswc.wswcExpirationDt
            """)
    Page<WsAutoWvrConfigWswcDAO> findInactiveWswcDaos(Pageable pageable);

    @Query("""
            select new com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO(wswcId, wswcScenarioDesc,
            wswcReasonAlvDAO.alvShortDecTxt, wswcSysOverwriteInd, wswcEffectiveDt, wswcExpirationDt)
            from WsAutoWvrConfigWswcDAO wswc where wswcId != :wswcId
            and wswc.wswcScenarioNumEventCd in (
                select wswcScenarioNumEventCd from WsAutoWvrConfigWswcDAO where wswcId = :wswcId
            )
            and wswc.localOfficeLofDAO.lofId in (
                select wswca.localOfficeLofDAO.lofId from WsAutoWvrConfigWswcDAO wswca where wswca.wswcId = :wswcId
            ) order by wswc.wswcEffectiveDt desc
            """)
    List<ConfigWswcListItemResDTO> findChildListByWswcId(@Param("wswcId") Long wswcId);

    @Query("""
            select new com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO(wswcId, wswcScenarioDesc,
            wswcReasonAlvDAO.alvShortDecTxt, wswcSysOverwriteInd, wswcEffectiveDt, wswcExpirationDt)
            from WsAutoWvrConfigWswcDAO wswc where wswcId != :wswcId
            and wswc.wswcScenarioNumEventCd in (
                select wswcScenarioNumEventCd from WsAutoWvrConfigWswcDAO where wswcId = :wswcId
            )
            and wswc.localOfficeLofDAO.lofId in (
                select wswca.localOfficeLofDAO.lofId from WsAutoWvrConfigWswcDAO wswca where wswca.wswcId = :wswcId
            )
            and TRUNC(SYSDATE) BETWEEN wswc.wswcEffectiveDt AND COALESCE(wswc.wswcExpirationDt, TRUNC(SYSDATE))
            order by wswc.wswcEffectiveDt desc
            """)
    List<ConfigWswcListItemResDTO> findActiveChildListByWswcId(@Param("wswcId") Long wswcId);

    @Query("""
            select new com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO(wswcId, wswcScenarioDesc,
            wswcReasonAlvDAO.alvShortDecTxt, wswcSysOverwriteInd, wswcEffectiveDt, wswcExpirationDt)
            from WsAutoWvrConfigWswcDAO wswc where wswcId != :wswcId
            and wswc.wswcScenarioNumEventCd in (
                select wswcScenarioNumEventCd from WsAutoWvrConfigWswcDAO where wswcId = :wswcId
            )
            and wswc.localOfficeLofDAO.lofId in (
                select wswca.localOfficeLofDAO.lofId from WsAutoWvrConfigWswcDAO wswca where wswca.wswcId = :wswcId
            )
            AND (TRUNC (SYSDATE) > wswc.wswcExpirationDt OR TRUNC (SYSDATE) < wswc.wswcEffectiveDt)
            order by wswc.wswcEffectiveDt desc
            """)
    List<ConfigWswcListItemResDTO> findInactiveChildListByWswcId(@Param("wswcId") Long wswcId);

    @Query("""
            from WsAutoWvrConfigWswcDAO wswc where wswcId != :wswcId
            and wswc.wswcScenarioNumEventCd in (
                select wswcScenarioNumEventCd from WsAutoWvrConfigWswcDAO where wswcId = :wswcId
            )
            and wswc.localOfficeLofDAO.lofId in (
                select wswca.localOfficeLofDAO.lofId from WsAutoWvrConfigWswcDAO wswca where wswca.wswcId = :wswcId
            )
            and wswc.wswcExpirationDt = :wswcExpirationDt
            order by wswc.wswcEffectiveDt desc
            """)
    List<WsAutoWvrConfigWswcDAO> findByWswcCategoryAndExpDate(@Param("wswcId") Long wswcId,
                                                          @Param("wswcExpirationDt") Date wswcExpirationDt);
}
