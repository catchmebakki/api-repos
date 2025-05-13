package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseEntityCmeDAO;
import com.ssi.ms.collecticase.dto.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseEntityCmeRepository extends CrudRepository<CcaseEntityCmeDAO, Long> {

    @Query("""  
            
            SELECT new com.ssi.ms.collecticase.dto.EmployerListDTO(
            ccaseEntityCmeDAO.employerEmpDAO.empId as employerId, 
            ccaseEntityCmeDAO.employerEmpDAO.empName as employerName)        
            from CcaseEntityCmeDAO ccaseEntityCmeDAO
                    where ccaseEntityCmeDAO.employerEmpDAO is not null
                    and ccaseEntityCmeDAO.ccaseOrganizationCmoDAO is null
                    and ccaseEntityCmeDAO.ccaseCasesCmcDAO.cmcId = :caseId
                    and ccaseEntityCmeDAO.cmeActiveInd = :activeInd
			""")
    List<EmployerListDTO> getEmployerListForWageGarnish(Long caseId, String activeInd);

    @Query("""  
            SELECT new com.ssi.ms.collecticase.dto.EmployerContactListDTO(
            ccaseCmeIndividualCmiDAO.cmiId as employerContactId, ccaseCmeIndividualCmiDAO.cmiFirstName||' 
            '||ccaseCmeIndividualCmiDAO.cmiLastName  as employerContactName) 
            from CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO 
            where ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.employerEmpDAO is not null
                and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.employerEmpDAO.empId = :employerId
                and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.ccaseOrganizationCmoDAO is null
                and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.ccaseCasesCmcDAO.cmcId = :caseId
                and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeActiveInd = :activeInd
             """)
    List<EmployerContactListDTO> getEmployerContactListForWageGarnish(Long caseId, Long employerId, String activeInd);

    @Query("""   
         SELECT new com.ssi.ms.collecticase.dto.OrganizationIndividualDTO(            
         ccaseEntityCmeDAO.cmeRole||'@'||ccaseEntityCmeDAO.ccaseOrganizationCmoDAO.cmoId as empRepId, 
         fn_inv_get_alv_description(ccaseEntityCmeDAO.cmeRole)||' : '||ccaseEntityCmeDAO.ccaseOrganizationCmoDAO.cmoName as empRepName)       
from CcaseEntityCmeDAO ccaseEntityCmeDAO
where ccaseEntityCmeDAO.ccaseCasesCmcDAO.cmcId = :caseId
and ccaseEntityCmeDAO.cmeActiveInd = :activeInd
and ccaseEntityCmeDAO.cmeRole in (:entityRoleList)
and ccaseEntityCmeDAO.ccaseOrganizationCmoDAO is not null
and ccaseEntityCmeDAO.employerEmpDAO.empId = :employerId
""")
    List<OrganizationIndividualDTO> getOrganizationInfo(Long caseId, String activeInd, List<Long> entityRoleList,
                                                Long employerId);

    @Query(""" 
         SELECT new com.ssi.ms.collecticase.dto.OrganizationIndividualDTO(
         ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeRole||'@'|| ccaseCmeIndividualCmiDAO.cmiId as empRepId, 
         fn_inv_get_alv_description(ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeRole)||' : '||
         ccaseCmeIndividualCmiDAO.cmiFirstName||' '||ccaseCmeIndividualCmiDAO.cmiLastName as empRepName)  
 from CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO
where ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.ccaseCasesCmcDAO.cmcId = :caseId
    and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.employerEmpDAO is null
    and ccaseCmeIndividualCmiDAO.ccaseOrganizationCmoDAO is null
    and ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeActiveInd = :activeInd
    ORDER by ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeId, ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeType, 
            ccaseCmeIndividualCmiDAO.cmiFirstName, ccaseCmeIndividualCmiDAO.cmiLastName
""")
    List<OrganizationIndividualDTO> getIndividualInfo(Long caseId, String activeInd);

}
