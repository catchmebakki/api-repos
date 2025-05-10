package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReaAttendanceRatDAO;
import com.ssi.ms.resea.dto.AvailableClaimantResDTO;
import com.ssi.ms.resea.dto.IdNameResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReseaRatRepository extends CrudRepository<ReaAttendanceRatDAO, Long> {
    @Query("""
            FROM ReaAttendanceRatDAO rat WHERE rat.ratId in ( :ratId )
            """)
    Iterable<ReaAttendanceRatDAO> getReseaByRatId(Long ratId);

    @Query("""
                  SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(rsps.claimClmDAO.clmId,
                  rsps.claimClmDAO.claimantDAO.firstName||' '||rsps.claimClmDAO.claimantDAO.lastName,
                  clf.localOfficeLofDAO.lofName,
                  (CASE WHEN rsps.rspsOrientationDt != COD('01/01/2000') AND
                  TRUNC( :apptDt ) > TRUNC(rsps.rspsOrientationDt + FN_INV_GET_PARAMETER_NUMERIC('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
                      THEN 'Y' ELSE 'N' END))
                  FROM ReseaPreStepsRspsDAO rsps
                  JOIN ClmLofClfDao clf ON clf.claimClmDAO.clmId = rsps.claimClmDAO.clmId
                      WHERE  rsps.rspsItemTypeCd = 5555
                        AND clf.clfActiveInd = 'Y'
                        AND EXISTS (
                            SELECT 1 FROM ReseaPreStepsRspsDAO rsps1
                                WHERE rsps1.rspsItemTypeCd IN (5767, 5768)
                                  AND rsps1.claimClmDAO.clmId = rsps.claimClmDAO.clmId
                        )
                        AND NOT EXISTS (
                            SELECT 1 FROM ReseaCaseRscsDAO rscs
                                WHERE rscs.clmDAO.clmId = rsps.claimClmDAO.clmId
                                  AND (rscs.rscsCreatedTs >= rsps.rspsItemTs OR rscs.rscsStageCdALV.alvId IN (5597, 5598, 5599))
                        )
                        AND clf.localOfficeLofDAO.lofId IN ( :lofIds )
                        AND EXISTS (
                          SELECT 1 FROM CcApplnCcaDAO cca
                              WHERE cca.clmDAO.clmId = rsps.claimClmDAO.clmId
                                AND cca.ccaWeekEndingDt >= :ccaDt
                        )
                      ORDER BY rsps.rspsOrientationDt asc, rsps.rspsId asc
                  """)
    List<AvailableClaimantResDTO> getInitialClaimantList(@Param("apptDt") Date apptDt,
                                                         @Param("ccaDt") Date ccaDt,
                                                         @Param("lofIds") List<Long> lofIds);

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
            WHERE rscs.stfDAO.userDAO.userId in ( :staffUserId ) AND rscs.rscsStageCdALV.alvId = 5597
            AND rscs.rscsStatusCdALV.alvId = 5605
            AND TRUNC (:apptDt) <= rscs.clmDAO.clmBenYrEndDt
            AND NOT EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsicIn WHERE rsicIn.claimDAO.clmId = rscs.clmDAO.clmId
                    AND rsicIn.rsicTimeslotUsageCdAlv.alvId = 5632 AND rsicIn.rsicCalEventTypeCdAlv.alvId = 5630
                    AND rsicIn.rsicMtgStatusCdAlv.alvId IN (5640, 5641))
            """)
    List<AvailableClaimantResDTO> getNoShowInitialClaimantList(@Param("apptDt") Date apptDt,
                                                               @Param("staffUserId") List<Long> staffUserId,
                                                               @Param("lofBuTypeCd") Long lofBuTypeCd,
                                                               @Param("interstateLof") Long interstateLof);

    /*@Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(bea.clmDAO.clmId,
            bea.clmDAO.claimantDAO.firstName||' '||bea.clmDAO.claimantDAO.lastName,
            (CASE WHEN bea.bssDAO.bssStartDate != COD('01/01/2000') AND
            TRUNC( :apptDt ) > TRUNC(bea.bssDAO.bssStartDate + FN_INV_GET_PARAMETER_NUMERIC('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
                THEN 'Y' ELSE 'N' END))
            FROM BeieriAttendanceBeaDAO bea
            JOIN ClmLofClfDao clf ON clf.claimClmDAO.clmId = bea.clmDAO.clmId
                WHERE bea.beaAttendanceCd = 2550
                  AND bea.bssDAO.bssStartDate != COD('01/01/2000')
                  AND bea.bssDAO.bssTypeCd = 2546
                  AND bea.bssDAO.bssStatusInd = 'Y'
                  AND bea.clmDAO.clmBenYrEndDt > :apptDt
                  AND FN_NUMERIC(bea.clmDAO.claimantDAO.ssn) = 'Y'
                  AND clf.localOfficeLofDAO.lofId IN ( :lofIds )
                  AND bea.bssDAO.bssStartDate >= COD(FN_INV_GET_PARAMETER_ALPHA ('NEW_RESEA_CUTOFF_DT', TRUNC (SYSDATE)))
                  AND TRUNC(bea.bssDAO.bssStartDate + FN_INV_GET_PARAMETER_NUMERIC ('REMINDER_LTR_DELAY', TRUNC (SYSDATE))) <= TRUNC( :apptDt )
                  AND TRUNC( :apptDt ) > TRUNC(bea.bssDAO.bssStartDate + FN_INV_GET_PARAMETER_NUMERIC('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
                  AND NOT EXISTS(
                      SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic
                          WHERE rsic.claimDAO.clmId = bea.clmDAO.clmId
                  )
                  AND NOT EXISTS (
                      SELECT 1 FROM ProcessHoldPrhDAO prh
                          WHERE prh.fkCmtId = bea.clmDAO.clmId
                            AND prh.prhStatusCd = 2164
                            AND prh.prhModuleCd = 2167
                  )
                  AND EXISTS (
                    SELECT 1 FROM CcApplnCcaDAO cca
                        WHERE cca.clmDAO.clmId = bea.clmDAO.clmId
                          AND cca.ccaWeekEndingDt >= :ccaDt)
            ORDER BY bea.bssDAO.bssStartDate
            """)
    List<AvailableClaimantResDTO> getBeyound21InitialClaimantList(@Param("apptDt") Date apptDt,
                                                         @Param("ccaDt") Date ccaDt,
                                                         @Param("lofIds") List<Long> lofIds);*/

    /*@Query("""
            SELECT new com.ssi.ms.resea.dto.AvailableClaimantResDTO(bea.clmDAO.clmId,
            bea.clmDAO.claimantDAO.firstName||' '||bea.clmDAO.claimantDAO.lastName,
            (CASE WHEN TRUNC(:apptDt) > TRUNC(bea.bssDAO.bssStartDate + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
                THEN 'Y' ELSE 'N' END))
            FROM BeieriAttendanceBeaDAO bea
            WHERE bea.beaAttendanceCd = 2550
            AND bea.bssDAO.bssStartDate >= COD(FN_INV_GET_PARAMETER_ALPHA ('NEW_RESEA_CUTOFF_DT',TRUNC (SYSDATE)))
            AND bea.bssDAO.bssTypeCd = 2546
            AND bea.bssDAO.brcDAO.brcFkLofId IN (SELECT lsf.lofDAO.lofId FROM LofStaffLsfDAO lsf WHERE lsf.stfDAO.userDAO.userId = :userId)
            AND TRUNC(:apptDt) > TRUNC(bea.bssDAO.bssStartDate)
            AND bea.bssDAO.bssStatusInd = 'Y' AND NOT EXISTS(
                SELECT 1 FROM ReseaCaseRscsDAO rscs WHERE rscs.clmDAO.clmId = bea.clmDAO.clmId
                and rscs.rscsStageCdALV.alvId != 5600
            )
            AND NOT EXISTS (
                SELECT 1 FROM ProcessHoldPrhDAO prh WHERE prh.fkCmtId = bea.clmDAO.clmId
                    AND prh.prhStatusCd = 2164 AND prh.prhModuleCd = 2167
            )
            AND EXISTS (SELECT 1 FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.claimDAO.clmId = bea.clmDAO.clmId
                AND rsic.rsicTimeslotUsageCdAlv.alvId = 5632 AND rsic.rsicCalEventTypeCdAlv.alvId = 5630
                AND rsic.rsicMtgStatusCdAlv.alvId IN (5640))
            AND EXISTS (SELECT 1 FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = bea.clmDAO.clmId AND cca.ccaWeekEndingDt >= :ccaDt)
            """)
    List<AvailableClaimantResDTO> getScheduledInitialClaimantList(@Param("apptDt") Date apptDt,
                                                                  @Param("userId") Long userId, @Param("ccaDt") Date ccaDt);*/

    @Query("""
            SELECT (CASE WHEN TRUNC(:apptDt) > TRUNC(bea.bssDAO.bssStartDate + FN_INV_GET_PARAMETER_NUMERIC ('RESEA_DEADLINE_DAYS',TRUNC (SYSDATE)))
                THEN 'Y' ELSE 'N' END)
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
    String checkInitialApptBeyond21(@Param("claimId") Long claimId,
                                    @Param("apptDt") Date apptDt);
}
