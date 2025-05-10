package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaRmtMtgInfoRsrmDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReseaRmtMtgInfoRsrmRepository extends CrudRepository<ReseaRmtMtgInfoRsrmDAO, Long> {
    @Query("""
            FROM ReseaRmtMtgInfoRsrmDAO rsrm WHERE rsrm.rsisDAO.rsisId = :rsisId
                AND :apptDt between rsrm.rsrmEffectiveDt AND COALESCE(rsrmExpirationDt, COD('12/31/2099'))
            """)
    ReseaRmtMtgInfoRsrmDAO getMeetingInfo(@Param("rsisId") Long rsisId,
                                          @Param("apptDt") Date apptDt);

    @Query("""
            FROM ReseaRmtMtgInfoRsrmDAO rsrm
            WHERE rsrm.rsisDAO.rsisId IN (:rsisId)
              AND :apptDt between rsrm.rsrmEffectiveDt AND COALESCE(rsrm.rsrmExpirationDt, COD('12/31/2099'))
            """)
    List<ReseaRmtMtgInfoRsrmDAO> getEffectiveRsrmDAO(@Param("rsisId") List<Long> rsisId,
                                                     @Param("apptDt") Date apptDt);
}
