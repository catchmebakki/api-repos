package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaUploadSchStagingRussDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseaUploadSchStagingRussRepository extends CrudRepository<ReseaUploadSchStagingRussDAO, Long> {
    @Query("""
            FROM ReseaUploadSchStagingRussDAO russ
            WHERE russ.rusmDAO.rusmId = :rusmId
            ORDER BY russ.russId ASC
            """)
    List<ReseaUploadSchStagingRussDAO> findByRussDAORusmId(@Param("rusmId") Long rusmId);

    @Query("""
            SELECT russ.russId FROM ReseaUploadSchStagingRussDAO russ
            WHERE russ.rusmDAO.rusmId = :rusmId
            AND EXISTS (
                SELECT 1 FROM ReseaUploadSchStagingRussDAO overlapRuss
                WHERE overlapRuss.rusmDAO.rusmId = russ.rusmDAO.rusmId
                AND overlapRuss.russId <> russ.russId
                AND UPPER(overlapRuss.russDayOfWeek) = UPPER(russ.russDayOfWeek)
                AND LPAD(SUBSTR(overlapRuss.russApptTimeframe, 0,REGEXP_INSTR(overlapRuss.russApptTimeframe, '-')-1),5, '0')
                    < LPAD(SUBSTR(russ.russApptTimeframe, REGEXP_INSTR(russ.russApptTimeframe, '-')+1),5, '0')
                AND LPAD(SUBSTR(overlapRuss.russApptTimeframe, REGEXP_INSTR(overlapRuss.russApptTimeframe, '-')+1),5, '0')
                    > LPAD(SUBSTR(russ.russApptTimeframe, 0,REGEXP_INSTR(russ.russApptTimeframe, '-')-1),5, '0')
            )
            """)
    List<Long> getOverlappingTimeSlots(@Param("rusmId") Long rusmId);
}
