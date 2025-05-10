package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseCmaNoticesCmnRepository extends CrudRepository<CcaseCmaNoticesCmnDAO, Long> {

    @Query("""          
			from CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmn
			            where ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.ccaseCasesCmcDAO.cmcId
			             			= :caseId
			            and ccaseCmaNoticesCmn.correspondenceCorDAO.corStatusCd IN (:correspondenceStatusList)
			            and ccaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcId = :caseCorrespondenceCrcId
			""")
    List<CcaseCmaNoticesCmnDAO> getCaseCorrespondenceByCaseId(Long caseId,
                                                              List<Long> correspondenceStatusList,
                                                              Long caseCorrespondenceCrcId);
}

