package com.ssi.ms.resea.database.repository;

import com.ssi.ms.common.database.dao.UserDAO;
import com.ssi.ms.resea.database.dao.LocalOfficeLofDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalOfficeLofRepository extends CrudRepository<LocalOfficeLofDAO, Long> {

    /**
     * @param lofBuTypeCd {@link Long} The Office Business Type to search for
     * @param lofDisplayInd {@link String}
     * @return {@link LocalOfficeLofDAO} all offices fitting the criteria
     */
    List<LocalOfficeLofDAO> findAllByLofBuTypeCdAndLofDisplayInd(Long lofBuTypeCd, String lofDisplayInd);

    @Query("""
            FROM LocalOfficeLofDAO lof
                WHERE lof.lofBuTypeCd = :lofBuTypeCd
                  AND lof.lofDisplayInd = 'Y'
                  AND lof.lofId != :interstateLof
            """)
    List<LocalOfficeLofDAO> findAllByLofBuTypeCd(@Param("lofBuTypeCd") Long lofBuTypeCd,
                                                 @Param("interstateLof") Long interstateLof);
    /**
     * @param lofBuTypeCd {@link Long} The Office Business Type to search for
     * @return {@link List} all orphaned RESEA offices fitting the criteria
     */
    @Query("""
            SELECT DISTINCT lof.lofId FROM LocalOfficeLofDAO lof
                WHERE lof.lofBuTypeCd = :lofBuTypeCd
                  AND lof.lofDisplayInd = 'Y'
                  AND NOT EXISTS (
                    SELECT 1 FROM LofStaffLsfDAO lsf
                        JOIN UsrRolUrlDAO url ON url.userDAO.userId = lsf.stfDAO.userDAO.userId
                    WHERE lsf.lofDAO.lofId = lof.lofId
                      AND url.fkRolId = :rolId
                      AND url.userDAO.usrStatusCd = :usrStatusCd
                  )
            """)
    List<Long> findAllReseaOrphanedLof(@Param("lofBuTypeCd") Long lofBuTypeCd,
                                       @Param("rolId") Long rolId,
                                       @Param("usrStatusCd") Long usrStatusCd);

    @Query("""
            SELECT lsf.lofDAO
            FROM LofStaffLsfDAO lsf
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = lsf.stfDAO.userDAO.userId
                WHERE lsf.lofDAO.lofDisplayInd = 'Y'
                  AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd
                  AND url.userDAO.userId = :userId
                  AND url.fkRolId = :rolId
                  AND url.userDAO.usrStatusCd = :usrStatusCd
                  AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2999')) >= TRUNC(SYSDATE)
            """)
    List<LocalOfficeLofDAO> findLocalOfficesByStaffRole(@Param("lofBuTypeCd") Long lofBuTypeCd,
                                                        @Param("userId") Long userId,
                                                        @Param("rolId") Long rolId,
                                                        @Param("usrStatusCd") Long usrStatusCd);
}
