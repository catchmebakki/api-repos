package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.StaffStfDAO;
import com.ssi.ms.resea.dto.IdNameResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffStfRepository extends CrudRepository<StaffStfDAO, Long> {

    @Query("""
            SELECT new com.ssi.ms.resea.dto.IdNameResDTO(
            stf.userDAO.userId, stf.stfFirstName||' '||stf.stfLastName)
            FROM StaffStfDAO stf
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = stf.userDAO.userId
            WHERE stf.userDAO.usrStatusCd = 1724 AND url.fkRolId = 94
            ORDER BY stf.stfFirstName asc, stf.stfLastName asc
            """)
    List<IdNameResDTO> getReseaCaseManagerList();

    @Query("""
            SELECT DISTINCT lsf.stfDAO.userDAO.userId
            FROM LofStaffLsfDAO lsf
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = lsf.stfDAO.userDAO.userId
            WHERE lsf.stfDAO.userDAO.usrStatusCd = 1724
              AND url.fkRolId = 94
              AND lsf.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsf.lofDAO.lofDisplayInd = 'Y'
              AND COALESCE(lsf.lsfExpirationDt, COD('12/31/2099')) > SYSDATE
              AND lsf.lofDAO.lofId IN (
                SELECT lsfCM.lofDAO.lofId FROM LofStaffLsfDAO lsfCM
                  JOIN UsrRolUrlDAO urlCM ON urlCM.userDAO.userId = lsfCM.stfDAO.userDAO.userId
                    WHERE lsfCM.stfDAO.userDAO.userId = :caseManagerId
                      AND lsfCM.lofDAO.lofBuTypeCd = :lofBuTypeCd AND lsfCM.lofDAO.lofDisplayInd = 'Y'
                      AND lsfCM.stfDAO.userDAO.usrStatusCd = 1724 AND urlCM.fkRolId = 54
                      AND COALESCE(lsfCM.lsfExpirationDt, COD('12/31/2099')) > SYSDATE
              )
            """)
    List<Long> getReseaCaseManagerIdListByLocalOfficeManager(@Param("caseManagerId") Long caseManagerId,
                                                             @Param("lofBuTypeCd") Long lofBuTypeCd);

    @Query("""
            SELECT new com.ssi.ms.resea.dto.IdNameResDTO(
            lsf.stfDAO.userDAO.userId, lsf.stfDAO.stfFirstName||' '||lsf.stfDAO.stfLastName)
            FROM LofStaffLsfDAO lsf
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = lsf.stfDAO.userDAO.userId
            WHERE lsf.stfDAO.userDAO.usrStatusCd = 1724 AND url.fkRolId = 94
            AND lsf.lofDAO.lofId = :lofId
            AND lsf.stfDAO.stfId != :staffId
            """)
    List<IdNameResDTO> getReseaCaseManagerList(@Param("lofId") Long lofId,
                                               @Param("staffId") Long staffId);

    @Query("""
            FROM StaffStfDAO stf WHERE
            stf.userDAO.userId = :userId
            """)
    StaffStfDAO findByUserId(@Param("userId") Long userId);
    
    
    /*
     * This query returns all the Resea Case Managers for a given Lof Id.
     * If Lof Id is passed as null, all the Resea managers of the entire agency will be fetched.
     */
    @Query("""
            SELECT DISTINCT lsf.stfDAO.userDAO.userId
            FROM LofStaffLsfDAO lsf
            JOIN UsrRolUrlDAO url ON url.userDAO.userId = lsf.stfDAO.userDAO.userId
            WHERE lsf.stfDAO.userDAO.usrStatusCd = 1724 AND url.fkRolId = 94
            AND lsf.lofDAO.lofId = (CASE WHEN :lofId IS NULL THEN lsf.lofDAO.lofId ELSE :lofId END)
            """)
    List<Long> getReseaCaseManagerListByLofId(@Param("lofId") Long lofId);

    @Query("""
            SELECT DISTINCT stf
            FROM 
            ReseaIntvwerCalRsicDAO rsic
            join ReseaIntvwSchRsisDAO rsis on rsis.rsisId = rsic.rsisDAO.rsisId
            join StaffStfDAO stf on rsis.stfDAO.stfId = stf.stfId
            WHERE 
            rsic.claimDAO.clmId != null
            ORDER BY stf.stfFirstName asc, stf.stfLastName asc
            """)
    List<StaffStfDAO> getAllApptReseaCaseManagerList();

}
