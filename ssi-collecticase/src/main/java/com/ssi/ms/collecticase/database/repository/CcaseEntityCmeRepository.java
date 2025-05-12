package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseEntityCmeDAO;
import com.ssi.ms.collecticase.dto.EmployerContactListDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
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

}
