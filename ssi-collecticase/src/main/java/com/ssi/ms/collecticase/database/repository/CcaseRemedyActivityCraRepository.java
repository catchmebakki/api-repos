package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCasesCmcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.dto.RemedyActivityDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseRemedyActivityCraRepository extends CrudRepository<CcaseRemedyActivityCraDAO, Long> {

    @Query("""          
			from CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO 
			where ccaseRemedyActivityCraDAO.craActivityCd = :craActivityCd
			and ccaseRemedyActivityCraDAO.craRemedyCd = :craRemedyCd
			""")
    CcaseRemedyActivityCraDAO getCaseRemedyActivityInfo(Long craActivityCd, Long craRemedyCd);

	@Query("""    
            SELECT DISTINCT ccaseRemedyActivityCraDAO.craId
			from CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO
			JOIN CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO
			ON ccaseRemedyActivityCraDAO.craId = ccaseActivitiesCmaDAO.ccaseRemedyActivityCraDAO.craId
			where ccaseActivitiesCmaDAO.ccaseCasesCmcDAO.cmcId = :cmcId
			AND ccaseRemedyActivityCraDAO.craUserInitiated = :activeInd
			AND ccaseRemedyActivityCraDAO.craActiveInd = :activeInd		
			""")
	List<Long> getCaseRemedyActivityByCaseId(Long cmcId, String activeInd);

	@Query("""    
			SELECT new com.ssi.ms.collecticase.dto.RemedyActivityDTO
			(ccaseRemedyActivityCraDAO.craRemedyCd as remedyCd,
			fnInvGetAlvDescription(ccaseRemedyActivityCraDAO.craRemedyCd) as remedyDesc,
			ccaseRemedyActivityCraDAO.craActivityCd as activityTypeCd,
			fnInvGetAlvDescription(ccaseRemedyActivityCraDAO.craActivityCd) as activityTypeDesc)		
			from CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO 
			where (ccaseRemedyActivityCraDAO.fkCraIdPreMust1 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreMust1 IS NOT NULL 
								AND ccaseRemedyActivityCraDAO.fkCraIdPreMust1 in (:caseRemedyId)))
			AND (ccaseRemedyActivityCraDAO.fkCraIdPreMust2 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreMust2 IS NOT NULL 
								AND ccaseRemedyActivityCraDAO.fkCraIdPreMust2 in (:caseRemedyId)))
			AND (ccaseRemedyActivityCraDAO.fkCraIdPreMust3 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreMust3 IS NOT NULL 
								AND ccaseRemedyActivityCraDAO.fkCraIdPreMust3 in (:caseRemedyId)))			
			AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf1 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf1 IS NOT NULL 
								AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf1 in (:caseRemedyId)
										OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf2 in (:caseRemedyId)
										  OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf3 in (:caseRemedyId))))
			AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf2 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf2 IS NOT NULL 
								AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf1 in (:caseRemedyId)
										OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf2 in (:caseRemedyId)
										  OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf3 in (:caseRemedyId))))
			AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf3 IS NULL 
						OR (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf3 IS NOT NULL 
								AND (ccaseRemedyActivityCraDAO.fkCraIdPreOneOf1 in (:caseRemedyId)
										OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf2 in (:caseRemedyId)
										  OR ccaseRemedyActivityCraDAO.fkCraIdPreOneOf3 in (:caseRemedyId))))			
			AND ccaseRemedyActivityCraDAO.craUserInitiated = :activeInd
			AND ccaseRemedyActivityCraDAO.craActiveInd = :activeInd
				order by ccaseRemedyActivityCraDAO.craRemedyCd,ccaseRemedyActivityCraDAO.craActivityCd
			""")
	List<RemedyActivityDTO> getCaseRemedyActivityByCaseRemedyId(List<Long> caseRemedyId, String activeInd);

}
