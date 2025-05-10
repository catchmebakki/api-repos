package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseEntityDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwCcaseEntityRepository extends CrudRepository<VwCcaseEntityDAO, Long> {

    @Query("""          
			from VwCcaseEntityDAO vwCcaseEntityDAO
			            where vwCcaseEntityDAO.entityId = :entityId
			            and vwCcaseEntityDAO.caseId = :caseId
			            and vwCcaseEntityDAO.cmeActiveInd = :activeInd
			            and vwCcaseEntityDAO.entityActiveInd = :activeInd
			""")
    List<VwCcaseEntityDAO> getCaseEntityInfo(Long entityId, Long caseId, String activeInd);
}
