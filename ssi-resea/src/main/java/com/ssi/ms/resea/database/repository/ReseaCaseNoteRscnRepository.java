package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaCaseNoteRscnDAO;
import com.ssi.ms.resea.database.dao.ReseaIssueIdentifiedRsiiDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseaCaseNoteRscnRepository extends CrudRepository<ReseaCaseNoteRscnDAO, Long> {

    @Query("""
            FROM ReseaCaseNoteRscnDAO rscn
            WHERE rscn.rscaDAO.rscaId = :rscaId
            AND rscn.rscnShowInNhuisInd = 'Y'
            ORDER BY rscn.rscnCreatedTs desc
            """)
    List<ReseaCaseNoteRscnDAO> findByCaseActivity(@Param("rscaId") Long rscaId);

}
