package com.ssi.ms.resea.database.repository;


import com.ssi.ms.resea.database.dao.ReseaActivityConfigRsacDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReseaActivityConfigRsacRepository extends CrudRepository<ReseaActivityConfigRsacDAO, Long> {
    @Query("""
            SELECT DISTINCT rsacTemplatePage
            FROM ReseaActivityConfigRsacDAO rsac
            WHERE rsac.rsacActivityCd = :rsacActivityCd
              AND TRUNC(SYSDATE) BETWEEN rsac.rsacEffectiveDt and COALESCE(rsac.rsacEffUntilDt, COD('12/31/2999'))
            """)
    Optional<String> getActivityTemplate(@Param("rsacActivityCd") Long rsacActivityCd);
}