package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CcaseCaseRemedyCmrRepository extends CrudRepository<CcaseCaseRemedyCmrDAO, Long> {

    @Query("""          
			from CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO
			            where ccaseCaseRemedyCmrDAO.ccaseCasesCmcDAO.cmcId
			             			= :caseId
			            and ccaseCaseRemedyCmrDAO.cmrRemedyCd IN (:cmrRemedyCdList)			            
			""")
    CcaseCaseRemedyCmrDAO getCaseRemedyByCaseRemedy(Long caseId,
                                                              List<Long> cmrRemedyCdList);
	@Transactional
	@Procedure(name = "updateCaseRemedy")
	Map<String, Object> updateCaseRemedy(@Param(CollecticaseConstants.PIN_CMA_ID) Long activityId,
												   @Param(CollecticaseConstants.PIN_EMP_ID) Long employerId);
}
