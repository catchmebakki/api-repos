package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * {@code ReseaIntvwerCalRsicRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code ReseaIntvwerCalRsicDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the RESEA_INTVWER_CAL_RSIC table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Anand
 *
 * 10/28/2024		Anand			AnD249239 	UE-241007-RESEA Rewrite-3
 * 11/6/2024		Anand			AnD250842   UE-241025-RESEA Rewrite-4
 */
@Repository
public interface ReseaIntvwerCalRsicRepository extends CrudRepository<ReseaIntvwerCalRsicDAO, Long> {
    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic
                LEFT JOIN rsic.rsisDAO rsis LEFT JOIN rsis.stfDAO stf
                LEFT JOIN rsic.stunDAO stun LEFT JOIN stun.stfDAO stunstf
                LEFT JOIN rsic.rsicTimeslotUsageCdAlv rsicTimeslotUsageCd
                WHERE (stunstf.userDAO.userId = :userId OR stf.userDAO.userId = :userId OR rsicTimeslotUsageCd.alvId = 5636)
                and rsic.rsicCalEventDt between :startDt and :endDt
                and rsic.rsicCalEventDispInd = 'Y'
                ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC, rsic.rsicId ASC
            """)
    List<ReseaIntvwerCalRsicDAO> getInterviewerCal(@Param("userId") Long userId,
                                                   @Param("startDt") Date startDt,
                                                   @Param("endDt") Date endDt);


    @Query("""
                select new com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsResDTO(
                rsic.rsicId, TO_CHAR(TO_DATE(rsic.rsicCalEventDt, 'MM/DD/YYYY'), 'FMMonth  DD, YYYY'),
                TO_CHAR(TO_DATE(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH:MI AM'),
                TO_CHAR(TO_DATE(rsic.rsicCalEventEndTime, 'HH24:MI'), 'HH:MI AM'),
                (case when rsic.rsicCalEventDt > :orientationDt then 'Y' else 'N' end))
                FROM ReseaIntvwerCalRsicDAO rsic
                join ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId
                WHERE
                rsic.rsicCalEventTypeCdAlv.alvId = :rsicCalEventTypeCd
                and rsic.rsicTimeslotUsageCdAlv.alvId = :rsicTimeslotUsageCd
                and (rsis.rsisAllowOnsiteInd = :rsicAllowOnsiteIndVal or rsis.rsisAllowRemoteInd =:rsicAllowRemoteIndVal)
                and rsic.claimDAO.clmId is null 
                and rsic.rsicCalEventDt >= sysdate
                and rsis.stfDAO.stfId = :staffId
                order by rsic.rsicCalEventDt,rsic.rsicCalEventStTime  asc
            """)
    List<ReseaRescheduleGetAvailableSlotsResDTO> getAvailableSlotsForReschInitialAppt(Date orientationDt,
                                                                   Long rsicTimeslotUsageCd,
                                                                   Long rsicCalEventTypeCd,
                                                                   String rsicAllowOnsiteIndVal,
                                                                   String rsicAllowRemoteIndVal, Long staffId); //AnD249239  //AnD250842


    @Query("""
                select new com.ssi.ms.resea.dto.ReseaRescheduleGetAvailableSlotsResDTO(
                rsic.rsicId, TO_CHAR(TO_DATE(rsic.rsicCalEventDt, 'MM/DD/YYYY'), 'FMMonth  DD, YYYY'),
                TO_CHAR(TO_DATE(rsic.rsicCalEventStTime, 'HH24:MI'), 'HH:MI AM'),
                TO_CHAR(TO_DATE(rsic.rsicCalEventEndTime, 'HH24:MI'), 'HH:MI AM'),
                (case when rsic.rsicCalEventDt > :orientationDt then 'Y' else 'N' end))
                FROM ReseaIntvwerCalRsicDAO rsic
                join ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId 
                WHERE
                rsic.rsicCalEventTypeCdAlv.alvId = :rsicCalEventTypeCd
                and rsic.rsicTimeslotUsageCdAlv.alvId = :rsicTimeslotUsageCd
                and (rsis.rsisAllowOnsiteInd = :rsicAllowOnsiteIndVal or rsis.rsisAllowRemoteInd =:rsicAllowRemoteIndVal)
                and rsic.claimDAO.clmId is null 
                and rsic.rsicCalEventDt >= sysdate
                and rsis.stfDAO.stfId = :staffId
                order by rsic.rsicCalEventDt,rsic.rsicCalEventStTime asc
            """)
    List<ReseaRescheduleGetAvailableSlotsResDTO> getAvailableSlotsForReschSubsequentAppt(Date orientationDt,
                                                                                          Long rsicTimeslotUsageCd,
                                                                                          Long rsicCalEventTypeCd,
                                                                                          String rsicAllowOnsiteIndVal,
                                                                                          String rsicAllowRemoteIndVal,
                                                                                          Long staffId);


    /*@Query("""
            select rscs
            FROM ReseaIntvwerCalRsicDAO rsic
                join ReseaCaseRscsDAO rscs on rscs.rscsId = rsic.rscsDAO.rscsId
                WHERE
                rsic.rsicId = :rsicId
            """)
    ReseaCaseRscsDAO getCaseDetailsByRsicId(Long rsicId);*/

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.claimDAO.clmId = :claimId
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :usageCd AND rsic.rsicCalEventTypeCdAlv.alvId = 5630 AND rsic.rsicMtgStatusCdAlv.alvId = 5640
            AND rsic.rsicCalEventDt >= TRUNC(SYSDATE)
    """)
    ReseaIntvwerCalRsicDAO findByScheduledByClaimId(@Param("claimId") Long claimId, @Param("usageCd") Long usageCd);

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.claimDAO.clmId = :claimId
            AND rsic.rsicTimeslotUsageCdAlv.alvId = :usageCd AND rsic.rsicCalEventTypeCdAlv.alvId = 5630 AND rsic.rsicMtgStatusCdAlv.alvId = 5640
            AND TO_DATE(TO_CHAR(rsic.rsicCalEventDt, 'MM/DD/YYYY')||' '||rsic.rsicCalEventEndTime, 'MM/DD/YYYY HH24:MI') >= SYSDATE
    """)
    ReseaIntvwerCalRsicDAO findByScheduledTimeByClaimId(@Param("claimId") Long claimId, @Param("usageCd") Long usageCd);
    
    @Query("""
            SELECT COUNT(*) FROM ReseaIntvwerCalRsicDAO rsic 
            JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId = rsic.rsisDAO.rsisId 
            WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds) AND rsic.rsicCalEventDispInd = 'Y'
            AND rsic.rsicCalEventDt >= :fromDate AND rsic.rsicCalEventTypeCdAlv.alvId IN (5630)
            """)
    Long getApptCountByCaseMgrPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate);
    
    @Query("""
            SELECT COUNT(*) FROM ReseaIntvwerCalRsicDAO rsic 
            JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId = rsic.rsisDAO.rsisId 
            WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds) AND rsic.rsicCalEventTypeCdAlv.alvId IN (5630)
            AND rsic.rsicCalEventDt >= :fromDate AND rsic.rsicMtgStatusCdAlv.alvId IN (:status) AND rsic.rsicCalEventDispInd = 'Y'
            """)
    Long getApptCountByCaseMgrPeriodStatus(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate,@Param("status") List<Long> status);

    @Query("""
            SELECT COUNT(*) FROM ReseaIntvwerCalRsicDAO rsic 
            JOIN ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId 
            WHERE rsis.stfDAO.userDAO.userId IN ( :caseMgrUserIds ) AND rsic.rsicCalEventTypeCdAlv.alvId IN (5630)
            AND rsic.rsicCalEventDt >= :fromDate AND rsic.rsicMtgModeInd IN ( :meetingMode ) AND rsic.rsicCalEventDispInd = 'Y'
            """)
    Long getApptCountByCaseMgrPeriodMtgMode(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate,@Param("meetingMode") String meetingMode);
    
    @Query("""
            SELECT COUNT(*) FROM ReseaIntvwerCalRsicDAO rsic1 
            JOIN ReseaIntvwSchRsisDAO rsis ON rsis.rsisId = rsic1.rsisDAO.rsisId 
            JOIN ReseaIntvwerCalRsicDAO rsic2 ON rsic1.rscsDAO.rscsId = rsic2.rscsDAO.rscsId
            WHERE rsis.stfDAO.userDAO.userId IN( :caseMgrUserIds) AND rsic1.rsicCalEventDt >= :fromDate
            AND rsic1.rsicTimeslotUsageCdAlv.alvId = rsic2.rsicTimeslotUsageCdAlv.alvId
            AND rsic1.rsicMtgStatusCdAlv.alvId = 5642 AND rsic1.rsicCreatedTs < rsic2.rsicCreatedTs
            AND rsic1.rsicCalEventDispInd = 'Y' AND rsic2.rsicCalEventDispInd = 'Y'
            """)
    Long getNoShowResheduledApptCountByCaseMgrPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate);

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic LEFT JOIN rsic.rsisDAO rsis LEFT JOIN rsis.stfDAO stf
                LEFT JOIN rsic.stunDAO stun LEFT JOIN stun.stfDAO stunstf
                WHERE (stunstf.userDAO.userId = :userId OR stf.userDAO.userId = :userId OR rsic.rsicTimeslotUsageCdAlv.alvId = 5636)
                and rsic.rsicCalEventDt between :startDt and :endDt
                and (rsic.rsicCalEventDt != :startDt OR rsic.rsicCalEventEndTime > :startTime)
                and (rsic.rsicCalEventDt != :endDt OR rsic.rsicCalEventStTime < :endTime)
                and rsic.rsicCalEventDispInd = 'Y'
                and rsic.rsicCalEventTypeCdAlv.alvId = 5630
                ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC, rsic.rsicId ASC
            """)
    List<ReseaIntvwerCalRsicDAO> getOneTimeInUseInterviewerCal(@Param("userId") Long userId,
                                                               @Param("startDt") Date startDt,
                                                               @Param("startTime") String startTime,
                                                               @Param("endDt") Date endDt,
                                                               @Param("endTime") String endTime);

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic LEFT JOIN rsic.rsisDAO rsis LEFT JOIN rsis.stfDAO stf
                LEFT JOIN rsic.stunDAO stun LEFT JOIN stun.stfDAO stunstf
                WHERE (stunstf.userDAO.userId = :userId OR stf.userDAO.userId = :userId OR rsic.rsicTimeslotUsageCdAlv.alvId = 5636)
                and rsic.rsicCalEventDt between :startDt and :endDt
                and rsic.rsicCalEventEndTime > :startTime and rsic.rsicCalEventStTime < :endTime
                and rsic.rsicCalEventDispInd = 'Y'
                and rsic.rsicCalEventTypeCdAlv.alvId = 5630
                and MOD(TO_CHAR(rsic.rsicCalEventDt, 'J'), 7)+2 IN ( :days )
                ORDER BY rsic.rsicCalEventDt ASC, rsic.rsicCalEventStTime ASC, rsic.rsicId ASC
            """)
    List<ReseaIntvwerCalRsicDAO> getRecurringInUseInterviewerCal(@Param("userId") Long userId,
                                                                 @Param("startDt") Date startDt,
                                                                 @Param("startTime") String startTime,
                                                                 @Param("endDt") Date endDt,
                                                                 @Param("endTime") String endTime,
                                                                 @Param("days") List<Integer> days);

    @Query("""
            SELECT COUNT(*) FROM ReseaIntvwerCalRsicDAO rsic
            WHERE rsic.rsisDAO.stfDAO.userDAO.userId = :caseManagerId
              AND rsic.rsisDAO.lofDAO.lofId = :lofId
              AND rsic.rsicCalEventDispInd = 'Y'
              AND rsic.rsicCalEventTypeCdAlv.alvId = :eventTypeCd
              AND rsic.rsicMtgStatusCdAlv.alvId = :mtgStatusCD
              AND rsic.rsicCalEventDt between :startDt and COALESCE(:endDt, COD('12/31/2099'))
            """)
    Long checkCaseManagerScheduledAppointmentsExists(@Param("caseManagerId") Long caseManagerId,
                                                     @Param("lofId") Long lofId,
                                                     @Param("startDt") Date startDt,
                                                     @Param("endDt") Date endDt,
                                                     @Param("eventTypeCd") Long eventTypeCd,
                                                     @Param("mtgStatusCD") Long mtgStatusCD);

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic
            WHERE rsic.rsicCalEventDispInd = 'Y'
              AND rsic.rsicCalEventTypeCdAlv.alvId = :eventTypeCd
              AND rsic.rsicCalEventDt between :startDt and COALESCE(:endDt, COD('12/31/2099'))
              AND rsic.rsisDAO.rsisId IN ( :rsisId )
            """)
    List<ReseaIntvwerCalRsicDAO> getAllAvailableAppointment(@Param("startDt") Date startDt,
                                                            @Param("endDt") Date endDt,
                                                            @Param("eventTypeCd") Long eventTypeCd,
                                                            @Param("rsisId") List<Long> rsisId);

    @Query("""
            FROM ReseaIntvwerCalRsicDAO rsic
            WHERE rsic.rscsDAO.rscsId = :rscsId
              AND rsic.rsicTimeslotUsageCdAlv.alvId = :rsicTimeslotUsageCd
              AND rsic.rsicMtgStatusCdAlv.alvId = :rsicMtgStatusCd
            """)
    ReseaIntvwerCalRsicDAO getCompletedApptByCaseStage(Long rscsId, Long rsicTimeslotUsageCd, Long rsicMtgStatusCd);
}
