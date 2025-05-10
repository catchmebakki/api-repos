package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.IssueDecisionDecDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonRepository  extends CrudRepository<IssueDecisionDecDAO, Long> {
    @Query(value = "SELECT SYSDATE FROM DUAL", nativeQuery = true)
    java.sql.Timestamp getCurrentTimestamp();

    @Query(value = "SELECT TRUNC(SYSDATE) FROM DUAL", nativeQuery = true)
    java.util.Date getCurrentDate();

   
}
