package com.ssi.ms.fraudreview.database.repository.sourceclaims.custom;


import java.util.Map;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssi.ms.fraudreview.database.dao.ClaimApplicationCapDAO;

@Repository
public interface CustomFraudReviewLookupRepository  extends  CrudRepository<ClaimApplicationCapDAO, Long> {

	@Procedure(name = "callSEARCH_UI")
	Map<String, Object> searchClaimApplicationList(@Param("PIN_QUERY") String searchCriteria,
			@Param("PIN_PAGENUMBER") Integer pageNumber, @Param("PIN_PAGESIZE") Integer pageSize);

	@Procedure(name = "callASSIGN_USERS")
	Map<String, Object> assignUserToClaimantFraudReview(@Param("PIN_ASSIGN_LIST") String assignCases);
}
