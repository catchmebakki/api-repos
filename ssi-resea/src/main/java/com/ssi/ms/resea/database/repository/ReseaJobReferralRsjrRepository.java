package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ReseaJobReferralRsjrDAO;
import com.ssi.ms.resea.dto.EmployerResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

@Repository
public interface ReseaJobReferralRsjrRepository extends CrudRepository<ReseaJobReferralRsjrDAO, Long> {

    @Query("""
        FROM ReseaJobReferralRsjrDAO rsjr WHERE rsjr.rscaDAO.rscsDAO.rscsId = :rscsId
        AND trunc(rsjr.rsjrCreatedTs) <= :apptDt ORDER BY rsjr.rsjrCreatedTs DESC
    """)
    List<ReseaJobReferralRsjrDAO> getJobReferralsList(@Param("rscsId") Long rscsId,
                                                      @Param("apptDt") Date apptDt);

    @Query("""
        FROM ReseaJobReferralRsjrDAO rsjr WHERE rsjr.rsidDAO.rsidId = :rsidId
        AND rsjr.rsjrSourceCdALV.alvId = :sourceCd ORDER BY rsjr.rsjrCreatedTs DESC
    """)
    List<ReseaJobReferralRsjrDAO> getInterviewJobReferralsList(@Param("rsidId") Long rsidId,
                                                      @Param("sourceCd") Long sourceCd);
    
    @Query("""
    		SELECT COUNT(distinct rsjr.rsjrId)
            FROM ReseaJobReferralRsjrDAO rsjr
            WHERE rsjr.rsidDAO.rsicDAO.rsisDAO.stfDAO.userDAO.userId IN ( :caseMgrUserIds)
            AND TRUNC(rsjr.rsjrCreatedTs) > :fromDate
        """)
        Long getJobReferralsByCaseMgrAndPeriod(@Param("caseMgrUserIds") List<Long> caseMgrUserIds, @Param("fromDate") Date fromDate);

    @Query(value = """
            SELECT E.EMP_ID,
                   E.EMP_NAME,
                   E.EMP_DBA_NAME,
                   E.EMP_UI_ACCT_NBR
              FROM (SELECT A.EMP_ID,
                           RPAD(CASE WHEN A.EMP_NAME IS NULL THEN '' ELSE SUBSTR(A.EMP_NAME, 0, 50) END, 50, ' ') AS EMP_NAME,
                           RPAD(CASE WHEN A.EMP_DBA_NAME IS NULL THEN ' ' ELSE SUBSTR(' DBA: '||A.EMP_DBA_NAME, 0, 35) END, 35, ' ') AS EMP_DBA_NAME,
                           CASE WHEN A.EMP_UI_ACCT_NBR IS NULL THEN '' ELSE ' - # '||A.EMP_UI_ACCT_NBR END AS EMP_UI_ACCT_NBR,
                           A.EMP_LIABILITY_DT
                      FROM EMP_ADDRESS_EAD B,
                           (EMP_DBA_NAMES_EDB M RIGHT OUTER JOIN EMPLOYER_EMP A ON M.FK_EMP_ID = A.EMP_ID)
                           LEFT OUTER JOIN EMPLOYER_EMP G ON A.FK_TPA_EMP_ID = G.EMP_ID
                     WHERE UPPER (REGEXP_REPLACE (A.EMP_NAME, '[^a-zA-Z]', '')) LIKE
                              (SELECT UPPER (REGEXP_REPLACE (:empName, '[^a-zA-Z%]', '')) FROM DUAL)
                        AND (A.EMP_DELETE_IND = 'N' OR A.EMP_DELETE_IND IS NULL)
                        AND B.EAD_TYPE_CD= :eadTypeCd AND A.EMP_SOURCE_CD IN( :empSourceCd )
                        AND B.FK_EMP_ID =A.EMP_ID AND B.EAD_PARENT_EAD_IFK IS NULL
                        AND A.EMP_UI_ACCT_NBR IS NOT NULL AND (A.EMP_KILLED_DT IS NULL OR A.EMP_KILLED_DT > SYSDATE)
                        AND B.FK_CLE_ID IS NULL
                    UNION
                    SELECT F.EMP_ID,
                           RPAD(CASE WHEN F.EMP_NAME IS NULL THEN '' ELSE SUBSTR(F.EMP_NAME, 0, 50) END, 50, ' ') AS EMP_NAME,
                           RPAD(CASE WHEN F.EMP_DBA_NAME IS NULL THEN ' ' ELSE SUBSTR(' DBA: '||F.EMP_DBA_NAME, 0, 35) END, 35, ' ') AS EMP_DBA_NAME,
                           CASE WHEN F.EMP_UI_ACCT_NBR IS NULL THEN '' ELSE ' - # '||F.EMP_UI_ACCT_NBR END AS EMP_UI_ACCT_NBR,
                           F.EMP_LIABILITY_DT
                    FROM EMP_ADDRESS_EAD H,
                    (EMP_DBA_NAMES_EDB C RIGHT OUTER JOIN EMPLOYER_EMP F ON C.FK_EMP_ID =F.EMP_ID)
                    LEFT OUTER JOIN EMPLOYER_EMP J ON F.FK_TPA_EMP_ID =J.EMP_ID
                    WHERE H.EAD_TYPE_CD= :eadTypeCd AND F.EMP_SOURCE_CD IN ( :empSourceCd )
                    AND F.EMP_UI_ACCT_NBR IS NOT NULL AND (F.EMP_KILLED_DT IS NULL OR F.EMP_KILLED_DT > SYSDATE)
                    AND H.EAD_PARENT_EAD_IFK IS NULL AND H.FK_CLE_ID IS NULL
                    AND H.FK_EMP_ID =F.EMP_ID AND H.FK_EMP_ID = C.FK_EMP_ID
                    AND (F.EMP_DELETE_IND = 'N' OR F.EMP_DELETE_IND IS NULL)
                    AND UPPER(REGEXP_REPLACE(C.EDB_DBA_NAME, '[^a-zA-Z]', '')) LIKE
                        (SELECT UPPER(REGEXP_REPLACE(:empName, '[^a-zA-Z%]', '')) FROM DUAL)
                    ) E
            ORDER BY EMP_NAME, EMP_LIABILITY_DT DESC FETCH FIRST 15 ROWS ONLY
            """, nativeQuery = true)
    List<Tuple> getEmployerSearchByName(@Param("empName") String empName,
                                        @Param("eadTypeCd") Long eadTypeCd,
                                        @Param("empSourceCd") List<Long> empSourceCd);
}
