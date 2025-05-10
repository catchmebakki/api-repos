package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.CcApplnCcaDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CcApplnCcaRepository extends CrudRepository<CcApplnCcaDAO, Long> {

    @Query("""
            FROM CcApplnCcaDAO cca WHERE cca.clmDAO.clmId = :clmId AND cca.ccaWeekEndingDt = :weekEndingDt
            """)
    CcApplnCcaDAO findByClaimIdWeekEndingDt(@Param("clmId") Long clmId, @Param("weekEndingDt") Date weekEndingDt);
}
