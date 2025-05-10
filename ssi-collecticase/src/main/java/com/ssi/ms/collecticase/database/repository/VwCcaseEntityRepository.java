package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseEntityDAO;
import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
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

	@Query("""
            SELECT new com.ssi.ms.collecticase.dto.VwCcaseEntityDTO(
            entityId as activityEntityContact, entityName as activityEntityName)
            from VwCcaseEntityDAO vwCcaseEntityDAO
			where vwCcaseEntityDAO.caseId = :caseId
			            and vwCcaseEntityDAO.cmeActiveInd = :activeInd
			            and vwCcaseEntityDAO.entityActiveInd = :activeInd
			""")
	List<VwCcaseEntityDTO> getEntityContactList(Long caseId, String activeInd);
}
