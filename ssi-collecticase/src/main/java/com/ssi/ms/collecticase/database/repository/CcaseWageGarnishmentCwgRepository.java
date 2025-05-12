package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseWageGarnishmentCwgRepository extends CrudRepository<CcaseWageGarnishmentCwgDAO, Long> {

    @Query("""          
			from CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO 
			where ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.ccaseCasesCmcDAO.cmcId = :caseId
			and ccaseWageGarnishmentCwgDAO.employerEmpDAO.empId = :employerId
			and ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.cmrStageCd IN (:stageList)
			""")
    List<CcaseWageGarnishmentCwgDAO> getWageInfoForCaseEmployerStage(Long caseId, Long employerId,
                                                                     List<Long> stageList);

	@Query("""          
			from CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO 
			where ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.ccaseCasesCmcDAO.cmcId = :caseId			
			and ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.cmrStageCd IN (:stageList)
			""")
	List<CcaseWageGarnishmentCwgDAO> getWageInfoForCaseStage(Long caseId, List<Long> stageList);
	@Query("""          
			from CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO 
			where ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.ccaseCasesCmcDAO.cmcId = :caseId
			and ccaseWageGarnishmentCwgDAO.employerEmpDAO.empId = :employerId
			and ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.cmrRemedyCd = :remedy
			ORDER BY ccaseWageGarnishmentCwgDAO.cwgId
			""")
	CcaseWageGarnishmentCwgDAO getWageInfoForCaseEmployerRemedy(Long caseId, Long employerId,
																	 Long remedy);

	@Query("""          
			from CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO 
			where ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.ccaseCasesCmcDAO.cmcId = :caseId
			and ccaseWageGarnishmentCwgDAO.employerEmpDAO.empId <> :employerId
			and ccaseWageGarnishmentCwgDAO.ccaseCaseRemedyCmrDAO.cmrStageCd IN (:stageList)
			ORDER BY ccaseWageGarnishmentCwgDAO.cwgId
			""")
	List<CcaseWageGarnishmentCwgDAO> getWageInfoForOtherEmpWithStage(Long caseId, Long employerId,
																	  List<Long> stageList);



}
