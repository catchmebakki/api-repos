package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.repository.custom.CustomCaseLookupRepository;
import com.ssi.ms.resea.database.repository.custom.CustomLookupRepository;
import com.ssi.ms.resea.dto.AvailableClaimantResDTO;
import com.ssi.ms.resea.dto.CaseLoadSummaryResDTO;
import com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@code ReseaCaseRscsRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaCaseRscsDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_CASE_RSCS table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Anand
 */

@Repository
public interface ReseaCaseRscsRepository extends CrudRepository<ReseaCaseRscsDAO, Long>,
        CustomLookupRepository, CustomCaseLookupRepository {

    @Transactional
    @Procedure(name = "reassignAll")
    Map<String, Object> reassignAll(@Param("PIN_STF_ID") Long stfId,
                                    @Param("PIN_LOF_ID") Long lofId,
                                    @Param("PIN_LOF_CHOICE") String lofChoice,
                                    @Param("PIN_REASSIGN_EFFECTIVE_DATE") Date reassignEffDt,
                                    @Param("PIN_REASSIGN_END_DATE") Date reassignEndDt,
                                    @Param("PIN_REASSIGN_REASON_CD") Long reassignReasonCd,
                                    @Param("PIN_REQUESTING_STF") Long requestingStfId,
                                    @Param("PIN_STAFF_NOTE") String staffNote);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsOrientationDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsOrientationDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId )
            AND rscs.rscsStageCdALV.alvId = 5597 AND rscs.rscsStatusCdALV.alvId = 5603
            AND rscs.rscsClosedDt is null
            AND EXISTS (SELECT 1 FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId AND cca.ccaWeekEndingDt >= :ccaDt)
            ORDER BY rscs.rscsOrientationDt
            """)
    List<AvailableClaimantResDTO> getInitialClaimantList(@Param("staffUserId") List<Long> staffUserId,
                                                         @Param("apptDt") Date apptDt,
                                                         @Param("ccaDt") Date ccaDt,
                                                         @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                         @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsOrientationDt != COD('01/01/2000') THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5597
            AND rscs.rscsInitApptDt > rscs.rscsOrientationDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND TRUNC (:apptDt) < rscs.rscsInitApptDt
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            AND rscs.rscsStatusCdALV.alvId = 5601
            AND rscs.rscsOrientationDt != COD('01/01/2000')
            """)
    List<AvailableClaimantResDTO> getInitialApptBeyond21(@Param("staffUserId") List<Long> staffUserId,
                                                         @Param("apptDt") Date apptDt,
                                                         @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                         @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (CASE WHEN rscs.rscsOrientationDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsOrientationDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)) FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId )
            AND rscs.rscsStageCdALV.alvId = 5596
            AND rscs.rscsStatusCdALV.alvId = 5604
            """)
    List<AvailableClaimantResDTO> getNotScheduledInitialAppt(@Param("staffUserId") List<Long> staffUserId,
                                                             @Param("apptDt") Date apptDt);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsInitApptDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsInitApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5598
            AND TRUNC (:apptDt) > rscs.rscsInitApptDt
            AND rscs.rscsStatusCdALV.alvId = 5603
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getFistSubsequentAppt(@Param("staffUserId") List<Long> staffUserId,
                                                        @Param("apptDt") Date apptDt,
                                                        @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                        @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsInitApptDt != COD('01/01/2000') THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5598
            AND rscs.rscsFirstSubsApptDt > rscs.rscsInitApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND TRUNC (:apptDt) < rscs.rscsFirstSubsApptDt
            AND rscs.rscsStatusCdALV.alvId = 5601
            AND rscs.rscsInitApptDt != COD('01/01/2000')
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getFistSubsequentApptBeyond21(@Param("staffUserId") List<Long> staffUserId,
                                                                @Param("apptDt") Date apptDt,
                                                                @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                                @Param("interstateLof") Long interstateLof);


    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsInitApptDt != COD('01/01/2000') THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId )
            AND rscs.rscsStageCdALV.alvId = :caseStage
            AND rscs.rscsStatusCdALV.alvId = :caseStatus
            AND rscs.rscsOnWaitlistInd = 'Y'
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getWaitlistClaimants(@Param("staffUserId") List<Long> staffUserId,
                                                       @Param("apptDt") Date apptDt,
                                                       @Param("caseStage") Long caseStage,
                                                       @Param("caseStatus") Long caseStatus,
                                                       @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                       @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsInitApptDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsInitApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)) FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5598
            AND rscs.rscsStatusCdALV.alvId = 5605
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            AND NOT EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsicIn WHERE rsicIn.claimDAO.clmId = rscs.clmDAO.clmId
                    AND rsicIn.rsicTimeslotUsageCdAlv.alvId = 5633 AND rsicIn.rsicCalEventTypeCdAlv.alvId = 5630
                    AND rsicIn.rsicMtgStatusCdAlv.alvId IN (5640, 5641))
            """)
    List<AvailableClaimantResDTO> getFistSubsequentApptNoShow(@Param("staffUserId") List<Long> staffUserId,
                                                              @Param("apptDt") Date apptDt,
                                                              @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                              @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsInitApptDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsInitApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId )
            AND rscs.rscsStageCdALV.alvId = 5597
            AND rscs.rscsStatusCdALV.alvId = 5604
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getNotScheduledFistSubsequentAppt(@Param("staffUserId") List<Long> staffUserId,
                                                                    @Param("apptDt") Date apptDt,
                                                                    @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                                    @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN TRUNC (:apptDt) > rscs.rscsFirstSubsApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5599
            AND rscs.rscsStatusCdALV.alvId = 5603
            AND TRUNC (:apptDt) > rscs.rscsFirstSubsApptDt
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getSecondSubsequentAppt(@Param("staffUserId") List<Long> staffUserId,
                                                          @Param("apptDt") Date apptDt,
                                                          @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                          @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsFirstSubsApptDt != COD('01/01/2000') THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5599
            AND rscs.rscsSecondSubsApptDt > rscs.rscsFirstSubsApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND TRUNC (:apptDt) < rscs.rscsSecondSubsApptDt
            AND rscs.rscsStatusCdALV.alvId = 5601
            AND rscs.rscsFirstSubsApptDt != COD('01/01/2000')
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getSecondSubsequentApptBeyond21(@Param("staffUserId") List<Long> staffUserId,
                                                                  @Param("apptDt") Date apptDt,
                                                                  @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                                  @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN rscs.rscsFirstSubsApptDt != COD('01/01/2000') AND
            TRUNC (:apptDt) > rscs.rscsFirstSubsApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5599
            AND rscs.rscsStatusCdALV.alvId = 5605
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            AND NOT EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsicIn WHERE rsicIn.claimDAO.clmId = rscs.clmDAO.clmId
                    AND rsicIn.rsicTimeslotUsageCdAlv.alvId = 5634 AND rsicIn.rsicCalEventTypeCdAlv.alvId = 5630
                    AND rsicIn.rsicMtgStatusCdAlv.alvId IN (5640, 5641))
            """)
    List<AvailableClaimantResDTO> getSecondSubsequentApptNoShow(@Param("staffUserId") List<Long> staffUserId,
                                                                @Param("apptDt") Date apptDt,
                                                                @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                                @Param("interstateLof") Long interstateLof);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rscs.clmDAO.clmId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            (SELECT DISTINCT lsf.lofDAO.lofName FROM LofStaffLsfDAO lsf
                  WHERE lsf.stfDAO.stfId = rscs.stfDAO.stfId
                    AND TRUNC(SYSDATE) BETWEEN lsf.lsfEffectiveDt AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999'))
                    AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofId != :interstateLof AND ROWNUM = 1),
            (CASE WHEN TRUNC (:apptDt) > rscs.rscsFirstSubsApptDt + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END))
            FROM ReseaCaseRscsDAO rscs
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId )
            AND rscs.rscsStageCdALV.alvId = 5598
            AND rscs.rscsStatusCdALV.alvId = 5604
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            """)
    List<AvailableClaimantResDTO> getNotScheduledSecondSubsequentAppt(@Param("staffUserId") List<Long> staffUserId,
                                                                      @Param("apptDt") Date apptDt,
                                                                      @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                                      @Param("interstateLof") Long interstateLof);

    @Query("""
            FROM ReseaCaseRscsDAO rscs WHERE rscs.clmDAO.clmId = :claimId
                AND rscs.rscsStageCdALV.alvId IN ( :stage )
                AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            """)
    ReseaCaseRscsDAO findByClaimId(@Param("claimId") Long claimId,
                                   @Param("stage") List<Long> stage,
                                   @Param("status") List<Long> status);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStageCdALV.alvId = :stage AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            """)
    Long getStageCaseLoadMetrics(@Param("userId") Long userId, @Param("stage") Long stage, @Param("status") List<Long> status);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStageCdALV.alvId IN ( :stage ) AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            AND EXISTS (
                SELECT 1 FROM ReseaCaseActivityRscaDAO rsca
                    WHERE rsca.rscsDAO.rscsId = rscs.rscsId
                      AND rsca.rscaFollowupDt is not null
                      AND rsca.rscaFollowupCompDt is null
                      AND COALESCE(rsca.rscaFollowupDoneInd, 'N') = 'N'
            )
            """)
    Long getFolloupCaseLoadMetrics(@Param("userId") Long userId, @Param("stage") List<Long> stage, @Param("status") List<Long> status);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStageCdALV.alvId in ( :initialStage, :firstSubStage, :secondSubStage )
            AND rscs.rscsPriority = 'HI' AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            """)
    Long getHiPriorityCaseLoadMetrics(@Param("userId") Long userId,
                                      @Param("initialStage") Long initialStage,
                                      @Param("firstSubStage") Long firstSubStage,
                                      @Param("secondSubStage") Long secondSubStage,
                                      @Param("status") List<Long> status);

    @Query("""
            SELECT COUNT(*) FROM ReaAttendanceRatDAO rat
            JOIN ReaSessionRssDAO rss ON rss.rssId = rat.ratFkRssId
            JOIN BrieriSessionBssDAO bss ON bss.bssId = rat.ratFkBssId
            WHERE rss.rssSessionType = 3178 AND rat.ratAttendanceCd = 3153
            AND TRUNC(SYSDATE) - TRUNC(bss.bssStartDate) BETWEEN FN_INV_GET_PARAMETER_NUMERIC ('RESEA_HI_PRI_DAYS',TRUNC (SYSDATE))
             AND FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND rss.rriDAO.lofDAO.lofId IN (SELECT lsf.lofDAO.lofId FROM LofStaffLsfDAO lsf WHERE lsf.stfDAO.userDAO.userId = :userId)
            AND NOT EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.claimDAO.clmId = rat.clmDAO.clmId
                AND rsic.rsicTimeslotUsageCdAlv.alvId = 5632 AND rsic.rsicCalEventTypeCdAlv.alvId = 5630
                AND rsic.rsicMtgStatusCdAlv.alvId IN (5640, 5641))
            """)
    Long getHiPriorityInitialCaseLoadMetrics(@Param("userId") Long userId);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStatusCdALV.alvId IN ( :status ) AND rscs.rscsStageCdALV.alvId IN ( :stage )
            """)
    Long getCaseStatusLoadMetrics(@Param("userId") Long userId,
                                  @Param("status") List<Long> status,
                                  @Param("stage") List<Long> stage);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStatusCdALV.alvId IN ( :status )
            AND ((rscs.rscsStageCdALV.alvId = :initialStage AND rscs.rscsOrientationDt != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND rscs.rscsInitApptDt != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND rscs.rscsFirstSubsApptDt != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))))
            """)
    Long getDelayedCaseLoadMetrics(@Param("userId") Long userId, @Param("initialStage") Long initialStage,
                                   @Param("firstSubStage") Long firstSubStage, @Param("secondSubStage") Long secondSubStage,
                                   @Param("status") List<Long> status);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            (CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
            WHEN ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            ) THEN 'LATE'
            WHEN ( rscs.rscsPriority = 'HI' ) THEN 'HI'
            END),
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
                WHERE rscs.stfDAO.userDAO.userId = :userId
                  AND rscs.rscsStageCdALV.alvId IN ( :stage )
                  AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            """)
    List<CaseLoadSummaryResDTO> getStageCaseSummary(@Param("userId") Long userId,
                                                    @Param("stage") List<Long> stage,
                                                    @Param("status") List<Long> status,
                                                    @Param("initialStage") Long initialStage,
                                                    @Param("firstSubStage") Long firstSubStage,
                                                    @Param("secondSubStage") Long secondSubStage,
                                                    @Param("statusSchedule") Long statusSchedule);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            'HI',
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
            WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStageCdALV.alvId IN ( :initialStage, :firstSubStage, :secondSubStage)
            AND rscs.rscsPriority = 'HI'
            AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            """)
    List<CaseLoadSummaryResDTO> getHiPriorityCaseLoadSummary(@Param("userId") Long userId,
                                                             @Param("status") List<Long> status,
                                                             @Param("initialStage") Long initialStage,
                                                             @Param("firstSubStage") Long firstSubStage,
                                                             @Param("secondSubStage") Long secondSubStage,
                                                             @Param("statusSchedule") Long statusSchedule);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            (CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
            WHEN ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            ) THEN 'LATE'
            WHEN ( rscs.rscsPriority = 'HI' ) THEN 'HI'
            END),
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
            WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscaFollowUp.rscaFollowupDt is not NULL
            AND rscs.rscsStatusCdALV.alvId NOT IN ( :status )
            AND rscs.rscsStageCdALV.alvId IN (:initialStage, :firstSubStage, :secondSubStage)
            """)
    List<CaseLoadSummaryResDTO> getFolloupCaseLoadSummary(@Param("userId") Long userId,
                                                          @Param("status") List<Long> status,
                                                          @Param("initialStage") Long initialStage,
                                                          @Param("firstSubStage") Long firstSubStage,
                                                          @Param("secondSubStage") Long secondSubStage,
                                                          @Param("statusSchedule") Long statusSchedule);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            (CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
            WHEN ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            ) THEN 'LATE'
            WHEN ( rscs.rscsPriority = 'HI' ) THEN 'HI'
            END),
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
            WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStatusCdALV.alvId IN ( :status )
            AND rscs.rscsStageCdALV.alvId IN (:initialStage, :firstSubStage, :secondSubStage)
            AND ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))))
            """)
    List<CaseLoadSummaryResDTO> getDelayedCaseLoadSummary(@Param("userId") Long userId,
                                                          @Param("initialStage") Long initialStage,
                                                          @Param("firstSubStage") Long firstSubStage,
                                                          @Param("secondSubStage") Long secondSubStage,
                                                          @Param("status") List<Long> status,
                                                          @Param("statusSchedule") Long statusSchedule);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            (CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
            WHEN ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            ) THEN 'LATE'
            WHEN ( rscs.rscsPriority = 'HI' ) THEN 'HI'
            END),
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
            WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStatusCdALV.alvId IN ( :status )
            AND rscs.rscsStageCdALV.alvId IN ( :initialStage, :firstSubStage, :secondSubStage )
            """)
    List<CaseLoadSummaryResDTO> getCaseStatusLoadSummary(@Param("userId") Long userId,
                                                         @Param("status") List<Long> status,
                                                         @Param("initialStage") Long initialStage,
                                                         @Param("firstSubStage") Long firstSubStage,
                                                         @Param("secondSubStage") Long secondSubStage,
                                                         @Param("statusSchedule") Long statusSchedule);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
            (CASE WHEN :prevStageApptDt != COD('01/01/2000')
             AND rsic.rsicCalEventDt - :prevStageApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)
            ) FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE rsic.rsisDAO.lofDAO.lofId = :oldLofId
            AND rsic.rsisDAO.stfDAO.stfId != :oldStfId
            AND url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getLofReassignCaseManagers(@Param("apptUsage") Long apptUsage,
                                                                   @Param("prevStageApptDt") Date prevStageApptDt,
                                                                   @Param("oldLofId") Long oldLofId,
                                                                   @Param("oldStfId") Long oldStfId,
                                                                   @Param("eventType") Long eventType,
                                                                   @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                   @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                   @Param("clmBenEndDt") Date clmBenEndDt);

    @Query(value = """
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(min(rsic.rsicId),
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' || lof.lofName || ' - ' ||
            ' - Case Load: ' ||
            (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
            (CASE WHEN :prevStageApptDt != COD('01/01/2000')
             AND min(rsic.rsicCalEventDt) - :prevStageApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)
            ) FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE rsic.rsisDAO.lofDAO.lofId = :oldLofId
            AND rsic.rsisDAO.stfDAO.stfId != :oldStfId
            AND url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            GROUP BY rsic.rsisDAO.stfDAO.stfId, rsic.rsisDAO.stfDAO.stfFirstName, rsic.rsisDAO.stfDAO.stfLastName, lof.lofName
            ORDER BY min(rsic.rsicCalEventDt) ASC
            """)
    List<CaseManagerAvailabilityResDTO> getLofReassignCaseManagersNoSch(@Param("apptUsage") Long apptUsage,
                                                                   @Param("prevStageApptDt") Date prevStageApptDt,
                                                                   @Param("oldLofId") Long oldLofId,
                                                                   @Param("oldStfId") Long oldStfId,
                                                                   @Param("eventType") Long eventType,
                                                                   @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                    @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                    @Param("clmBenEndDt") Date clmBenEndDt);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
            (CASE WHEN :prevStageApptDt != COD('01/01/2000')
             AND rsic.rsicCalEventDt - :prevStageApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)
            ) FROM ReseaIntvwerCalRsicDAO rsic JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE rsic.rsisDAO.lofDAO.lofId != :oldLofId
            AND rsic.rsisDAO.stfDAO.stfId != :oldStfId
            AND url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND rsic.rsisDAO.rsisAllowRemoteInd = 'Y'
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getOtherOfficeReassignCaseManagers(@Param("apptUsage") Long apptUsage,
                                                                           @Param("prevStageApptDt") Date prevStageApptDt,
                                                                           @Param("oldLofId") Long oldLofId,
                                                                           @Param("oldStfId") Long oldStfId,
                                                                           @Param("eventType") Long eventType,
                                                                           @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                           @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                           @Param("clmBenEndDt") Date clmBenEndDt);

    @Query(value = """
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(min(rsic.rsicId),
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' || lof.lofName || ' - ' ||
            ' - Case Load: ' ||
            (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
            (CASE WHEN :prevStageApptDt != COD('01/01/2000')
             AND min(rsic.rsicCalEventDt) - :prevStageApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                THEN 'Y' ELSE 'N' END)
            ) FROM ReseaIntvwerCalRsicDAO rsic JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE rsic.rsisDAO.lofDAO.lofId != :oldLofId
            AND rsic.rsisDAO.stfDAO.stfId != :oldStfId
            AND url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND rsic.rsisDAO.rsisAllowRemoteInd = 'Y'
            GROUP BY rsic.rsisDAO.stfDAO.stfId, rsic.rsisDAO.stfDAO.stfFirstName, rsic.rsisDAO.stfDAO.stfLastName, lof.lofName
            ORDER BY min(rsic.rsicCalEventDt) ASC
            """)
    List<CaseManagerAvailabilityResDTO> getOtherOfficeReassignCaseManagersNoSch(@Param("apptUsage") Long apptUsage,
                                                                           @Param("prevStageApptDt") Date prevStageApptDt,
                                                                           @Param("oldLofId") Long oldLofId,
                                                                           @Param("oldStfId") Long oldStfId,
                                                                           @Param("eventType") Long eventType,
                                                                           @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                           @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                            @Param("clmBenEndDt") Date clmBenEndDt);
    
    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId IN ( :caseMgrUserIds )
            AND rscs.rscsStageCdALV.alvId IN (5597,5598,5599) 
            AND rscs.rscsStatusCdALV.alvId NOT IN ( 5611,5612 )
            """)
    Long getCaseLoadMetricsByUserIds(@Param("caseMgrUserIds") List<Long> caseMgrUserIds);






    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
                (CASE WHEN rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                      THEN 'Y' ELSE 'N' END)
            )
            FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE
            url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND  rsic.rsisDAO.lofDAO.lofId = :clmLofId
            AND (rsic.rsisDAO.rsisAllowOnsiteInd = :allowOnsiteIndVal or rsic.rsisDAO.rsisAllowRemoteInd =:allowRemoteIndVal)
            AND rsic.rsicCalEventDt - :bssStartDt < FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND NOT EXISTS (
                                            SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic_in
                                            JOIN StaffUnavaiabilityStunDAO stun ON stun.stfDAO.stfId = rsic_in.rsisDAO.stfDAO.stfId
                                            where rsic_in.rsicId = rsic.rsicId
                                            and stun.stunStatusAlv.alvId = 5661
                                            and rsic_in.rsicCalEventDt between stun.stunStartDt and stun.stunEndDt
                                            and (  
                                                     (stun.stunStartDt != stun.stunEndDt and
                                                         (
                                                            (rsic_in.rsicCalEventDt > stun.stunStartDt and rsic_in.rsicCalEventDt < stun.stunEndDt)
                                                            or (rsic_in.rsicCalEventDt = stun.stunStartDt and rsic_in.rsicCalEventStTime >= stun.stunStartTime)
                                                            or (rsic_in.rsicCalEventDt = stun.stunEndDt and rsic_in.rsicCalEventEndTime <= stun.stunEndTime)
                                                         )
                                                     ) 
                                                     or
                                                        (stun.stunStartDt = stun.stunEndDt AND
                                                          rsic_in.rsicCalEventDt = stun.stunStartDt AND
                                                          rsic_in.rsicCalEventStTime < stun.stunEndTime AND
                                                          rsic_in.rsicCalEventEndTime > stun.stunStartTime
                                                        )
                                                )
                                  )
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getCaseMgrsAvailListForClmLof(@Param("apptUsage") Long apptUsage,
                                                                      @Param("clmLofId") Long clmLofId,
                                                                      @Param("eventType") Long eventType,
                                                                      @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                      @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                      @Param("clmBenEndDt") Date clmBenEndDt,
                                                                      @Param("allowOnsiteIndVal") String allowOnsiteIndVal,
                                                                      @Param("allowRemoteIndVal") String allowRemoteIndVal,
                                                                      @Param("bssStartDt") Date bssStartDt);


    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
                (CASE WHEN rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                      THEN 'Y' ELSE 'N' END)
            )
            FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE
            url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND  rsic.rsisDAO.lofDAO.lofId = :clmLofId
            AND (rsic.rsisDAO.rsisAllowOnsiteInd = :allowOnsiteIndVal or rsic.rsisDAO.rsisAllowRemoteInd =:allowRemoteIndVal)
            AND rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND NOT EXISTS (
                                            SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic_in
                                            JOIN StaffUnavaiabilityStunDAO stun ON stun.stfDAO.stfId = rsic_in.rsisDAO.stfDAO.stfId
                                            where rsic_in.rsicId = rsic.rsicId
                                            and stun.stunStatusAlv.alvId = 5661
                                            and rsic_in.rsicCalEventDt between stun.stunStartDt and stun.stunEndDt
                                            and (  
                                                     (stun.stunStartDt != stun.stunEndDt and
                                                         (
                                                            (rsic_in.rsicCalEventDt > stun.stunStartDt and rsic_in.rsicCalEventDt < stun.stunEndDt)
                                                            or (rsic_in.rsicCalEventDt = stun.stunStartDt and rsic_in.rsicCalEventStTime >= stun.stunStartTime)
                                                            or (rsic_in.rsicCalEventDt = stun.stunEndDt and rsic_in.rsicCalEventEndTime <= stun.stunEndTime)
                                                         )
                                                     ) 
                                                     or
                                                        (stun.stunStartDt = stun.stunEndDt AND
                                                          rsic_in.rsicCalEventDt = stun.stunStartDt AND
                                                          rsic_in.rsicCalEventStTime < stun.stunEndTime AND
                                                          rsic_in.rsicCalEventEndTime > stun.stunStartTime
                                                        )
                                                )
                                        )
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getCaseMgrsAvailListForClmLofBeyondReseaDeadLine(@Param("apptUsage") Long apptUsage,
                                                                      @Param("clmLofId") Long clmLofId,
                                                                      @Param("eventType") Long eventType,
                                                                      @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                      @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                      @Param("clmBenEndDt") Date clmBenEndDt,
                                                                      @Param("allowOnsiteIndVal") String allowOnsiteIndVal,
                                                                      @Param("allowRemoteIndVal") String allowRemoteIndVal,
                                                                      @Param("bssStartDt") Date bssStartDt);



    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
             (CASE WHEN rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                      THEN 'Y' ELSE 'N' END)
            )
            FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE
            url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND rsic.rsisDAO.rsisAllowRemoteInd = 'Y'
            AND rsic.rsicCalEventDt - :bssStartDt < FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND NOT EXISTS (
                                            SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic_in
                                            JOIN StaffUnavaiabilityStunDAO stun ON stun.stfDAO.stfId = rsic_in.rsisDAO.stfDAO.stfId
                                            where rsic_in.rsicId = rsic.rsicId
                                            and stun.stunStatusAlv.alvId = 5661
                                            and rsic_in.rsicCalEventDt between stun.stunStartDt and stun.stunEndDt
                                            and (  
                                                     (stun.stunStartDt != stun.stunEndDt and
                                                         (
                                                            (rsic_in.rsicCalEventDt > stun.stunStartDt and rsic_in.rsicCalEventDt < stun.stunEndDt)
                                                            or (rsic_in.rsicCalEventDt = stun.stunStartDt and rsic_in.rsicCalEventStTime >= stun.stunStartTime)
                                                            or (rsic_in.rsicCalEventDt = stun.stunEndDt and rsic_in.rsicCalEventEndTime <= stun.stunEndTime)
                                                         )
                                                     ) 
                                                     or
                                                        (stun.stunStartDt = stun.stunEndDt AND
                                                          rsic_in.rsicCalEventDt = stun.stunStartDt AND
                                                          rsic_in.rsicCalEventStTime < stun.stunEndTime AND
                                                          rsic_in.rsicCalEventEndTime > stun.stunStartTime
                                                        )
                                                )
                                        )
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getCaseMgrsAvailListForAllOtherLof(@Param("apptUsage") Long apptUsage,
                                                                           @Param("eventType") Long eventType,
                                                                           @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                           @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                           @Param("clmBenEndDt") Date clmBenEndDt,
                                                                           @Param("bssStartDt") Date bssStartDt);


    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO(rsic.rsicId,
            rsic.rsisDAO.stfDAO.stfFirstName || ' ' || rsic.rsisDAO.stfDAO.stfLastName || ' - ' ||
            lof.lofName || ' - ' ||
            TO_CHAR(rsic.rsicCalEventDt, 'mm/dd/yyyy') ||' '||TO_CHAR(TO_TIMESTAMP(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH12:MI PM')
            || ' - Case Load: ' || (SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                AND rscs.rscsStatusCdALV.alvId not in ( :caseLoadStatus ) and rscs.rscsStageCdALV.alvId in ( :caseLoadStage )),
             (CASE WHEN rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
                      THEN 'Y' ELSE 'N' END)
            )
            FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId = rsic.rsisDAO.lofDAO.lofId
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = rsic.rsisDAO.stfDAO.userDAO.userId
            WHERE
            url.fkRolId = 94
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :apptUsage
            AND rsic.rsicCalEventTypeCdAlv.alvId = :eventType
            AND rsic.rsicCalEventDt > SYSDATE
            AND rsic.rsicCalEventDt <= trunc(:clmBenEndDt)
            AND rsic.rsisDAO.rsisAllowRemoteInd = 'Y'
            AND rsic.rsicCalEventDt - :bssStartDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE))
            AND NOT EXISTS (
                                            SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic_in
                                            JOIN StaffUnavaiabilityStunDAO stun ON stun.stfDAO.stfId = rsic_in.rsisDAO.stfDAO.stfId
                                            where rsic_in.rsicId = rsic.rsicId
                                            and stun.stunStatusAlv.alvId = 5661
                                            and rsic_in.rsicCalEventDt between stun.stunStartDt and stun.stunEndDt
                                            and (  
                                                     (stun.stunStartDt != stun.stunEndDt and
                                                         (
                                                            (rsic_in.rsicCalEventDt > stun.stunStartDt and rsic_in.rsicCalEventDt < stun.stunEndDt)
                                                            or (rsic_in.rsicCalEventDt = stun.stunStartDt and rsic_in.rsicCalEventStTime >= stun.stunStartTime)
                                                            or (rsic_in.rsicCalEventDt = stun.stunEndDt and rsic_in.rsicCalEventEndTime <= stun.stunEndTime)
                                                         )
                                                     ) 
                                                     or
                                                        (stun.stunStartDt = stun.stunEndDt AND
                                                          rsic_in.rsicCalEventDt = stun.stunStartDt AND
                                                          rsic_in.rsicCalEventStTime < stun.stunEndTime AND
                                                          rsic_in.rsicCalEventEndTime > stun.stunStartTime
                                                        )
                                                )
                                        )
            ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC
            """)
    List<CaseManagerAvailabilityResDTO> getCaseMgrsAvailListForAllOtherLofBeyondReseaDeadLine(@Param("apptUsage") Long apptUsage,
                                                                           @Param("eventType") Long eventType,
                                                                           @Param("caseLoadStatus") List<Long> caseLoadStatus,
                                                                           @Param("caseLoadStage") List<Long> caseLoadStage,
                                                                           @Param("clmBenEndDt") Date clmBenEndDt,
                                                                           @Param("bssStartDt") Date bssStartDt);


    @Query("""
            SELECT bea.bssDAO.bssStartDate
            FROM BeieriAttendanceBeaDAO bea
            WHERE bea.beaAttendanceCd = 2550
            AND bea.bssDAO.bssStartDate >= COD(FN_INV_GET_PARAMETER_ALPHA ('NEW_RESEA_CUTOFF_DT',TRUNC (SYSDATE)))
            AND bea.bssDAO.bssTypeCd = 2546
            AND bea.bssDAO.bssStatusInd = 'Y'
            AND bea.clmDAO.clmId = :claimId
            AND NOT EXISTS(
                SELECT 1 FROM ReseaCaseRscsDAO rscs WHERE rscs.clmDAO.clmId = bea.clmDAO.clmId
                 and trunc(rscs.rscsCreatedTs) > bea.bssDAO.bssStartDate
            )
            AND NOT EXISTS (
                SELECT 1 FROM ProcessHoldPrhDAO prh WHERE prh.fkCmtId = bea.clmDAO.clmId
                    AND prh.prhStatusCd = 2164 AND prh.prhModuleCd = 2167
            )
            """)
    Date getBssStartDtForInitialAppt(@Param("claimId") Long claimId);

    @Query("""
            SELECT COUNT(*) FROM ReseaCaseRscsDAO rscs WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsStatusCdALV.alvId IN ( :status ) AND rscs.rscsStageCdALV.alvId IN ( :stage )
            AND rscs.rscsOnWaitlistInd = 'Y'
            """)
    Long getWaitlistedCaseLoadMetrics(@Param("userId") Long userId,
                                      @Param("status") List<Long> status,
                                      @Param("stage") List<Long> stage);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.CaseLoadSummaryResDTO(rscs.rscsId,
            trim(rscs.clmDAO.claimantDAO.firstName)||' '||trim(rscs.clmDAO.claimantDAO.lastName),
            rscs.clmDAO.clmBenYrEndDt, trim(rscs.rscsStageCdALV.alvShortDecTxt),
            trim(rscs.rscsStatusCdALV.alvShortDecTxt),
            CASE rscs.rscsStageCdALV.alvId
                WHEN :initialStage THEN rscs.rscsInitApptDt
                WHEN :firstSubStage THEN rscs.rscsFirstSubsApptDt
                WHEN :secondSubStage THEN rscs.rscsSecondSubsApptDt END,
            (SELECT COUNT(*) FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = rscs.clmDAO.clmId),
            rscaFollowUp.rscaFollowupDt, followUpAlv.alvShortDecTxt, '',
            (CASE WHEN rscs.rscsOnWaitlistInd = 'Y' THEN 'WL'
            WHEN ((rscs.rscsStageCdALV.alvId = :initialStage AND trunc(rscs.rscsOrientationDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsInitApptDt, SYSDATE)) - rscs.rscsOrientationDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :firstSubStage AND trunc(rscs.rscsInitApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsFirstSubsApptDt, SYSDATE)) - rscs.rscsInitApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            OR (rscs.rscsStageCdALV.alvId = :secondSubStage AND trunc(rscs.rscsFirstSubsApptDt) != COD('01/01/2000')
                AND trunc(COALESCE(rscs.rscsSecondSubsApptDt, SYSDATE)) - rscs.rscsFirstSubsApptDt > FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
            ) THEN 'LATE'
            WHEN ( rscs.rscsPriority = 'HI' ) THEN 'HI'
            END),
            SUBSTR(rscs.clmDAO.claimantDAO.ssn, 6),
            rscs.clmDAO.clmId,
            CASE WHEN rscs.rscsStatusCdALV.alvId = 5603 THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'N' AND rscs.rscsStatusCdALV.alvId = :statusSchedule
                  AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = rscs.rscsId AND rsic.rsicCalEventDt > SYSDATE)
                THEN 'Y' ELSE 'N' END,
            CASE WHEN COALESCE(rscs.rscsOnWaitlistInd, 'N') = 'Y' THEN 'Y' ELSE 'N' END)
            FROM ReseaCaseRscsDAO rscs
            LEFT JOIN ReseaCaseActivityRscaDAO rscaFollowUp ON rscaFollowUp.rscsDAO.rscsId = rscs.rscsId
                            AND rscaFollowUp.rscaFollowupDt is not null AND rscaFollowUp.rscaFollowupCompDt is null
							AND rscaFollowUp.rscaFollowupDt = (SELECT min(rscaIn.rscaFollowupDt) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND rscaIn.rscaFollowupCompDt is null
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N')
							AND rscaFollowUp.rscaActivtyTs = (SELECT min(rscaIn.rscaActivtyTs) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt)
							AND rscaFollowUp.rscaId = (SELECT min(rscaIn.rscaId) FROM ReseaCaseActivityRscaDAO rscaIn
										WHERE rscaIn.rscaFollowupDt is not null
										AND rscaIn.rscaFollowupCompDt is null
										AND rscaIn.rscsDAO.rscsId = rscaFollowUp.rscsDAO.rscsId
										AND COALESCE(rscaIn.rscaFollowupDoneInd, 'N') = 'N'
										AND rscaIn.rscaFollowupDt = rscaFollowUp.rscaFollowupDt
										AND rscaId.rscaActivtyTs = rscaFollowUp.rscaActivtyTs)
            LEFT JOIN rscaFollowUp.rscaFollowupTypeCdALV followUpAlv
            WHERE rscs.stfDAO.userDAO.userId = :userId
            AND rscs.rscsOnWaitlistInd = 'Y'
            AND rscs.rscsStatusCdALV.alvId IN ( :status )
            AND rscs.rscsStageCdALV.alvId IN ( :initialStage, :firstSubStage, :secondSubStage )
            """)
    List<CaseLoadSummaryResDTO> getWaitlistedCaseStatusLoadSummary(@Param("userId") Long userId,
                                                                   @Param("status") List<Long> status,
                                                                   @Param("initialStage") Long initialStage,
                                                                   @Param("firstSubStage") Long firstSubStage,
                                                                   @Param("secondSubStage") Long secondSubStage,
                                                                   @Param("statusSchedule") Long statusSchedule);
}
