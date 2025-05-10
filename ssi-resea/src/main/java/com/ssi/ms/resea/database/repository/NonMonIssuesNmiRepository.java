package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.NonMonIssuesNmiDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonMonIssuesNmiRepository extends CrudRepository<NonMonIssuesNmiDAO, Long> {

	@Query("""
			from NonMonIssuesNmiDAO nmi where nmi.parentNmiDAO.nmiId = :nmiId
			and nmi.nmiDisplayInd = 'Y'
			ORDER BY nmi.nmiShortDescTxt asc
			""")
	List<NonMonIssuesNmiDAO> getChildNmiList(@Param("nmiId") Long nmiId);

	@Query("""
			from NonMonIssuesNmiDAO nmi where nmi.parentNmiDAO.nmiId is null
			and nmi.nmiDisplayInd = 'Y'
			ORDER BY nmi.nmiShortDescTxt asc
			""")
	List<NonMonIssuesNmiDAO> getParentNmiList();


	@Query("""
			from NonMonIssuesNmiDAO nmi where nmi.parentNmiDAO.nmiId is null
			and nmi.nmiId in (:modulePageSpecificNmiList)
			ORDER BY nmi.nmiShortDescTxt asc
			""")
	List<NonMonIssuesNmiDAO> getParentNmiListBasedOnModuleAndPage(@Param("modulePageSpecificNmiList") List<Long> modulePageSpecificNmiList);
}
