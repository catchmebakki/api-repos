package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.LofStaffLsfDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LofStfLsfRepository extends CrudRepository<LofStaffLsfDAO, Long> {

    @Query("""
            SELECT lsf.stfDAO.userDAO.userId FROM LofStaffLsfDAO lsf WHERE lsf.lofDAO.lofId = :lofId
            AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999')) >= TRUNC(SYSDATE)
            """)
    List<Long> getLocalOfficeStaffUserList(@Param("lofId") Long lofId);

    @Query("""
            SELECT lof.lofName FROM ReseaIntvwerCalRsicDAO rsic
            JOIN LocalOfficeLofDAO lof ON lof.lofId=rsic.rsisDAO.lofDAO.lofId
            WHERE rsic.rscsDAO.rscsId = :rscsId
                ORDER BY rsic.rsicCalEventDt DESC
            """)
    List<String> getCaseLocalOfficeName(@Param("rscsId") Long rscsId);

    @Query("""
            SELECT rsic.rsisDAO.lofDAO.lofId FROM ReseaIntvwerCalRsicDAO rsic WHERE rsic.rscsDAO.rscsId = :rscsId
                ORDER BY rsic.rsicCalEventDt DESC
            """)
    List<Long> getCaseLocalOfficesId(@Param("rscsId") Long rscsId);
}
