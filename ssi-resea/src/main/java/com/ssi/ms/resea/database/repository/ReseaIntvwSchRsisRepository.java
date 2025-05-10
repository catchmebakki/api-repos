package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaIntvwSchRsisDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReseaIntvwSchRsisRepository extends CrudRepository<ReseaIntvwSchRsisDAO, Long> {


    @Query("""
            FROM ReseaIntvwSchRsisDAO rsis
            WHERE rsis.stfDAO.stfId = :stfId
              AND rsis.lofDAO.lofId = :lofId
              AND COALESCE(rsis.rsisExpirationDt, COD('12/31/2099')) >= COALESCE( :expirationDt , COD('12/31/2099'))
            """)
    List<ReseaIntvwSchRsisDAO> getEffectiveRsisDAOList(@Param("stfId") Long stfId,
                                                       @Param("lofId") Long lofId,
                                                       @Param("expirationDt") Date expirationDt);
}
