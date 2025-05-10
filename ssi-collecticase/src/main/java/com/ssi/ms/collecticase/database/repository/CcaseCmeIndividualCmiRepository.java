package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmeIndividualCmiDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseCmeIndividualCmiRepository extends CrudRepository<CcaseCmeIndividualCmiDAO, Long> {

    @Query("""          
			from CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO
			            where ccaseCmeIndividualCmiDAO.ccaseEntityCmeDAO.cmeId = :entityId
			            and ccaseCmeIndividualCmiDAO.cmiActiveInd = :cmiActiveInd
			""")
    List<CcaseCmeIndividualCmiDAO> getCaseEntityIndividualByCaseEntityId(Long entityId,
                                                              String cmiActiveInd);


}
