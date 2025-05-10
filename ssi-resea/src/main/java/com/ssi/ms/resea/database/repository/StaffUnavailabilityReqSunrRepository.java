package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.StaffUnavailabilityReqSunrDAO;
import com.ssi.ms.resea.dto.UnavailablitySummaryResDTO;
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
 * {@code SsStfUnavailSunRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code SsStfUnavailSunDAO} entity.
 *
 * @author Sitaram
 */

@Repository
public interface StaffUnavailabilityReqSunrRepository extends CrudRepository<StaffUnavailabilityReqSunrDAO, Long> {
    @Transactional
    @Procedure(name = "processUnavailReq")
    Map<String, Object> processUnavailReq(@Param("pin_sunr_id") Long stunId,
                                          @Param("pin_stf_id_approver") Long stfIdApprover,
                                          @Param("pin_calling_pgm") String callingProg);

    @Transactional
    @Procedure(name = "processWithdrawSunr")
    Map<String, Object>  processWithdrawSunr(@Param("pin_sunr_id") Long stunId,
                                             @Param("pin_wd_start_dt") Date withdrawStartDt,
                                             @Param("pin_wd_start_time") String withdrawStartTime,
                                             @Param("pin_calling_pgm") String callingProg);
    @Query("""
            SELECT new com.ssi.ms.resea.dto.UnavailablitySummaryResDTO(
            sunr.stfDAO.staffName AS staffName,
            sunr.sunrId AS unavailabilityId,
            CASE WHEN sunr.sunrTypeInd = 'R' THEN 'Reccuring' ELSE 'One-Time' END AS TYPE,
            CASE WHEN sunr.sunrTypeInd = 'R' THEN
                 CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(TO_CHAR(sunr.sunrStartDt, 'MM/DD/YYYY'),' to '), TO_CHAR(sunr.sunrEndDt, 'MM/DD/YYYY')), ' from '),
                  TO_CHAR(TO_DATE(sunr.sunrStartTime, 'HH24:MI'), 'HH:MI AM')), '    to   '), TO_CHAR(TO_DATE(sunr.sunrEndTime, 'HH24:MI'), 'HH:MI AM'))
               ELSE
                 CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(TO_CHAR(sunr.sunrStartDt, 'MM/DD/YYYY'), ' at '), TO_CHAR(TO_DATE(sunr.sunrStartTime, 'HH24:MI'), 'HH:MI AM')), ' to '),
                  TO_CHAR(sunr.sunrEndDt, 'MM/DD/YYYY')), ' at '), TO_CHAR(TO_DATE(sunr.sunrEndTime, 'HH24:MI'), 'HH:MI AM'))
            END AS PERIOD,
            sunr.sunrReasonTypeCdAlv.alvShortDecTxt AS REASON,
            sunr.sunrStatusCdAlv.alvShortDecTxt AS STATUS,
            sunr.sunrNote AS NOTES,
            CASE WHEN sunr.sunrStatusCdAlv.alvId = 5660 THEN 'Y' ELSE 'N' END AS EDITABLE,
            CASE WHEN sunr.sunrStatusCdAlv.alvId = 5660 THEN 'Y' ELSE 'N' END AS REQUEST_IND,
            sunr.sunrStatusCdAlv.alvId AS STATUS_ID,
            sunr.sunrTypeInd AS TYPE_IND,
            TO_DATE(TO_CHAR(sunr.sunrStartDt, 'MM/DD/YYYY')||' '||sunr.sunrStartTime, 'MM/DD/YYYY HH24:MI') AS START_DATE_TIME,
            TO_DATE(TO_CHAR(sunr.sunrEndDt, 'MM/DD/YYYY')||' '||sunr.sunrEndTime, 'MM/DD/YYYY HH24:MI') AS END_DATE_TIME
            ) FROM StaffUnavailabilityReqSunrDAO sunr
            WHERE sunr.stfDAO.userDAO.userId IN ( :userId )
              AND sunr.sunrStartDt >= :startDt
              AND sunr.sunrEndDt <= COALESCE(:endDt, COD('12/31/2999'))
              AND (COALESCE(:status, 0) = 0 OR sunr.sunrStatusCdAlv.alvId = :status)
            """)
    List<UnavailablitySummaryResDTO> getStaffSunrSummary(@Param("userId") List<Long> userId,
                                                         @Param("startDt") Date startDt,
                                                         @Param("endDt") Date endDt,
                                                         @Param("status") Long status,
                                                         Sort by);

    @Query("""
            SELECT 'Y' FROM ReseaIntvwerCalRsicDAO rsic
                    JOIN StaffUnavaiabilityStunDAO stun ON stun.stfDAO.stfId = rsic.rsisDAO.stfDAO.stfId
                    where rsic.rsicId = :eventId
                    and stun.stunStatusAlv.alvId = 5661
                    and rsic.rsicCalEventDt between stun.stunStartDt and stun.stunEndDt
                    and (
                             (stun.stunStartDt != stun.stunEndDt and
                                 (
                                    (rsic.rsicCalEventDt > stun.stunStartDt and rsic.rsicCalEventDt < stun.stunEndDt)
                                    or (rsic.rsicCalEventDt = stun.stunStartDt and rsic.rsicCalEventStTime >= stun.stunStartTime)
                                    or (rsic.rsicCalEventDt = stun.stunEndDt and rsic.rsicCalEventEndTime <= stun.stunEndTime)
                                 )
                             )
                             or
                                (stun.stunStartDt = stun.stunEndDt AND
                                  rsic.rsicCalEventDt = stun.stunStartDt AND
                                  rsic.rsicCalEventStTime < stun.stunEndTime AND
                                  rsic.rsicCalEventEndTime > stun.stunStartTime
                                )
                        )
            """)
    String checkStaffUnavailabilityForAvailableslot(@Param("eventId") Long eventId);

    /*@Query("""
            SELECT sunr.sunrId FROM StaffUnavailabilityReqSunrDAO sunr
            WHERE sunr.stfDAO.userDAO.userId = :userId
              AND sunr.sunrId != :sunrId
              AND (:startDt between sunr.sunrStartDt AND sunr.sunrEndDt
                OR sunr.sunrStartDt BETWEEN :startDt AND :endDt)
              AND (:startTime BETWEEN sunr.sunrStartTime AND sunr.sunrEndTime
                OR sunr.sunrStartTime BETWEEN :startTime AND :endTime)
              AND sunr.sunrTypeInd IN ( :sunrTypeInd )
            """)
    List<Long> checkOverlappingOneTimeSunrExists(@Param("sunrId") Long sunrId,
                                                 @Param("userId") Long userId,
                                                 @Param("startDt") Date startDt,
                                                 @Param("startTime") String startTime,
                                                 @Param("endDt") Date endDt,
                                                 @Param("endTime") String endTime,
                                                 @Param("sunrTypeInd") List<String> sunrTypeInd);

    @Query("""
            SELECT sunr.sunrId FROM StaffUnavailabilityReqSunrDAO sunr
            WHERE sunr.stfDAO.userDAO.userId = :userId
              AND sunr.sunrId != :sunrId
              AND sunr.sunrStatusCdAlv.alvId IN (:sunrStatusCd)
              AND (:startDt between sunr.sunrStartDt AND sunr.sunrEndDt
                  OR sunr.sunrStartDt BETWEEN :startDt AND :endDt)
              AND (
                  (
                    sunr.sunrTypeInd = :sunrTypeIndOneTime
                    AND (
                            (
                                sunr.sunrStartDt = sunr.sunrEndDt
                                AND (:startTime BETWEEN sunr.sunrStartTime AND sunr.sunrEndTime OR sunr.sunrStartTime BETWEEN :startTime AND :endTime)
                                AND TO_NUMBER(TO_CHAR(sunr.sunrStartTime, 'D')) IN ( :recurDays )
                            )
                            OR
                            (
                                sunr.sunrStartDt != sunr.sunrEndDt
                            )
                     )
                  )
               OR (sunr.sunrTypeInd = :sunrTypeIndRecur
                    AND ((COALESCE(sunr.sunrMondayInd, 'N') = 'Y' AND 2 IN ( :recurDays ))
                      OR (COALESCE(sunr.sunrTuesdayInd, 'N') = 'Y' AND 3 IN ( :recurDays ))
                      OR (COALESCE(sunr.sunrWednesdayInd, 'N') = 'Y' AND 4 IN ( :recurDays ))
                      OR (COALESCE(sunr.sunrThursdayInd, 'N') = 'Y' AND 5 IN ( :recurDays ))
                      OR (COALESCE(sunr.sunrFridayInd, 'N') = 'Y' AND 6 IN ( :recurDays )))
                    AND (:startTime BETWEEN sunr.sunrStartTime AND sunr.sunrEndTime
                      OR sunr.sunrStartTime BETWEEN :startTime AND :endTime))
              )
            """)
    List<Long> checkOverlappingRecurringSunrExists(@Param("sunrId") Long sunrId,
                                                   @Param("userId") Long userId,
                                                   @Param("startDt") Date startDt,
                                                   @Param("startTime") String startTime,
                                                   @Param("endDt") Date endDt,
                                                   @Param("endTime") String endTime,
                                                   @Param("sunrTypeIndOneTime") String sunrTypeIndOneTime,
                                                   @Param("sunrTypeIndRecur") String sunrTypeIndRecur,
                                                   @Param("recurDays") List<Integer> recurDays,
                                                   @Param("sunrStatusCd") List<Long> sunrStatusCd);*/
}
