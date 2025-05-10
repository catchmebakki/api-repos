package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReseaUploadSchSummaryRusmRepository extends CrudRepository<ReseaUploadSchSummaryRusmDAO, Long> {

    Optional<ReseaUploadSchSummaryRusmDAO> findTopByRucsDAOOrderByRusmReqTsDescRusmIdDesc(ReseaUploadCmSchRucsDAO rucsDAO);

    @Query("""
            SELECT rusm.rusmId FROM ReseaUploadSchSummaryRusmDAO rusm
            WHERE rusm.rucsDAO.rucsId = :rucsId
            ORDER BY rusm.rusmReqTs DESC, rusm.rusmId DESC
            """)
    List<Long> findAllRusmIdsByRucsDAO(Long rucsId);

    @Query(value = "SELECT RUSM_ID_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getRusmNextVal();
}
