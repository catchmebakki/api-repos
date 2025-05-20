package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GTTForOrgLookupRepository extends CrudRepository<GTTForOrgLookupDAO, Long> {

    @Modifying
    @Transactional
    @Query(value = """  
            insert into GTT_FOR_ORGLOOKUP
            select distinct empl.emp_Id, UPPER(empl.emp_Name), UPPER(empl.EMP_DBA_NAME), empl.EMP_UI_ACCT_NBR ,
            'EMP' AS ORIGIN,'NHUIS' AS SOURCE,TO_CHAR(empl.EMP_FEIN_NBR ),'EMP' as ENTITY_TYPE,
            CASE WHEN empl.EMP_KILLED_DT is null THEN 'A' ELSE 'T' END AS STATUS
            from EMPLOYER_EMP empl join EMP_ADDRESS_EAD ead on empl.emp_id = ead.fk_emp_id
            left outer join CCASE_ORGANIZATION_CMO cmo on ead.FK_EMP_ID != cmo.FK_EMP_ID
            where  COALESCE(empl.EMP_DELETE_IND, 'N') = 'N' and empl.EMP_UI_ACCT_NBR is not null
                    and empl.EMP_ID not in (SELECT cme.fk_emp_id FROM CCASE_ENTITY_CME  cme
                                    WHERE cme.fk_emp_id IS NOT NULL AND cme.fk_cmc_id = :caseId and cme.cme_Active_Ind = :activeInd)
            and ead.EAD_TYPE_CD  in (:eadTypeList)
""", nativeQuery = true)
    public int processGTTLookupForEmployer(Long caseId, List<Long> eadTypeList,String activeInd);

    @Modifying
    @Transactional
    @Query(value = """  
            insert into GTT_FOR_ORGLOOKUP
            select distinct empl.emp_Id, UPPER(empl.emp_Name), UPPER(empl.EMP_DBA_NAME),empl.EMP_UI_ACCT_NBR ,
            'CMO' AS ORIGIN,'NHUIS' AS SOURCE,TO_CHAR(empl.EMP_FEIN_NBR ),'EMP' as ENTITY_TYPE,
            CASE WHEN empl.EMP_KILLED_DT is null THEN 'A' ELSE 'T' END AS STATUS
            from EMPLOYER_EMP empl join EMP_ADDRESS_EAD ead on empl.emp_id = ead.fk_emp_id
            left outer join CCASE_ORGANIZATION_CMO cmo on ead.FK_EMP_ID != cmo.FK_EMP_ID
                where COALESCE(empl.EMP_DELETE_IND, 'N') = 'N' and empl.EMP_UI_ACCT_NBR  is not null
                and empl.EMP_ID not in
                (SELECT cme.fk_emp_id FROM CCASE_ENTITY_CME  cme WHERE cme.fk_emp_id IS NOT NULL AND cme.fk_cmc_id = :caseId and cme.cme_Active_Ind = :activeInd) 
                and ead.EAD_TYPE_CD in (:eadTypeList) 
            UNION 
            SELECT DISTINCT CMO_ID,CMO_NAME,'' AS DBA_NAME,CMO_UI_ACCT_NBR,'CMO' AS ORIGIN, 
            'COLLECTICASE:'|| CASE WHEN CMO.FK_EMP_ID is not null THEN 'ATTY/REP, EMP' ELSE 'ATTY/REP' END AS SOURCE,
            CMO_FEIN_NBR,fn_inv_get_alv_description(CME_ROLE) AS ENTITY_TYPE,'' AS STATUS 
            FROM CCASE_ENTITY_CME CME JOIN CCASE_ORGANIZATION_CMO CMO ON CMO.CMO_ID = CME.FK_CMO_ID and CME.CME_ROLE in(:cmeRoleList) 
            and CMO.CMO_ID not in 
            (select DISTINCT FK_CMO_ID from CCASE_ENTITY_CME where FK_CMC_ID = :caseId and FK_CMO_ID is not null and cme.cme_Active_Ind = :activeInd)           
""", nativeQuery = true)
    public int processGTTLookupForOrganisation(Long caseId, List<Long> eadTypeList,List<Long> cmeRoleList, String activeInd);

}
