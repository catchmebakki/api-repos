package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseCraCorrespondenceCrcRepository extends CrudRepository<CcaseCraCorrespondenceCrcDAO, Long> {

    @Query("""          
			from CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrc
			            where ccaseCraCorrespondenceCrc.crcActiveInd IN (:activeCorrespondenceList)
			            and ccaseCraCorrespondenceCrc.crcManual IN (:manualCorrespondenceList)
			""")
    List<CcaseCraCorrespondenceCrcDAO> getAllCaseCorrespondenceTypes(List<String> manualCorrespondenceList,
                                                                     List<String> activeCorrespondenceList);

    @Query("""          
			from CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrc
			            where ccaseCraCorrespondenceCrc.crcActiveInd IN (:activeCorrespondenceList)
			            and ccaseCraCorrespondenceCrc.crcManual IN (:manualCorrespondenceList)
			            and ccaseCraCorrespondenceCrc.ccaseRemedyActivityCraDAO.craActivityCd IN (:activityCdList)
			            and ccaseCraCorrespondenceCrc.ccaseRemedyActivityCraDAO.craRemedyCd IN (:remedyCdList)           
			""")
    List<CcaseCraCorrespondenceCrcDAO> getSendCorrespondenceForActivityRemedy(List<String> activeCorrespondenceList,
																			  List<String> manualCorrespondenceList,
																			  List<Long> activityCdList,
																			  List<Long> remedyCdList);

    @Query("""          
			from CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrc
			            where ccaseCraCorrespondenceCrc.crcActiveInd IN (:activeCorrespondenceList)
			            and ccaseCraCorrespondenceCrc.crcManual IN (:manualCorrespondenceList)			            
			            and ccaseCraCorrespondenceCrc.ccaseRemedyActivityCraDAO.craRemedyCd IN (:remedyCdList)           
			""")
    List<CcaseCraCorrespondenceCrcDAO> getManualCorrespondenceForRemedy(List<String> activeCorrespondenceList,
																		List<String> manualCorrespondenceList,
																		List<Long> remedyCdList);

	@Query("""          
from CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmn
           where ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.ccaseCasesCmcDAO.cmcId = :caseId
           and ccaseCmaNoticesCmn.correspondenceCorDAO is not null
           and ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.cmaRemedyType = :remedyTypeCd
           and ccaseCmaNoticesCmn.cmnResendReq = :activeIndicator
           and (ccaseCmaNoticesCmn.cmnCreatedTs, ccaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcId) IN
            (select max(innerCcaseCmaNoticesCmn.cmnCreatedTs), innerCcaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcId
            from CcaseCmaNoticesCmnDAO innerCcaseCmaNoticesCmn
where innerCcaseCmaNoticesCmn.ccaseActivitiesCmaDAO.ccaseCasesCmcDAO.cmcId = :caseId      
and innerCcaseCmaNoticesCmn.ccaseActivitiesCmaDAO.cmaRemedyType = :remedyTypeCd
group by innerCcaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcId)
           order by ccaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcRptName
           
""")
	List<CcaseCmaNoticesCmnDAO> getResendNoticesByCaseId(Long caseId, Long remedyTypeCd, String activeIndicator);

}
