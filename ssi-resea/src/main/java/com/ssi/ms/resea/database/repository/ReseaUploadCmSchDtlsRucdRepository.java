package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaUploadCmSchDtlsRucdDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseaUploadCmSchDtlsRucdRepository extends CrudRepository<ReseaUploadCmSchDtlsRucdDAO, Long> {
    List<ReseaUploadCmSchDtlsRucdDAO> getAllByRucsDAOAndRusmDAOOrderByRucdDayOfWeekAscRucdStartTimeAsc(
            ReseaUploadCmSchRucsDAO rucsDAO, ReseaUploadSchSummaryRusmDAO rusmDAO);

    @Query("""
            SELECT DISTINCT rucd.rucdDayOfWeek FROM ReseaUploadCmSchDtlsRucdDAO rucd
            WHERE rucd.rucsDAO.rucsId = :rucsId AND rucd.rusmDAO.rusmId = :rusmId
            """)
    List<Integer> getAllDayOfWeeksByRucsIdAndRusmId(@Param("rucsId") Long rucsId, @Param("rusmId") Long rusmId);
}
