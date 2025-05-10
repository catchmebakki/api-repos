package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaUploadSchErrorRuseDAO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatisticsItemResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseaUploadErrorRuseRepository extends CrudRepository<ReseaUploadSchErrorRuseDAO, Long> {

    @Query("""
            FROM ReseaUploadSchErrorRuseDAO ruse
            WHERE ruse.rusmDAO.rusmId = :rusmId
            ORDER BY ruse.ruseId ASC
            """)
    List<ReseaUploadSchErrorRuseDAO> findAllByRusmId(Long rusmId);
}
