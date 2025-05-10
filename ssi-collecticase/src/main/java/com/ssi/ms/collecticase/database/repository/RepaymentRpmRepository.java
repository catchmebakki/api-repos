package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import com.ssi.ms.collecticase.database.dao.RepaymentRpmDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RepaymentRpmRepository extends CrudRepository<RepaymentRpmDAO, Long> {

    @Query("""          
			from RepaymentRpmDAO repaymentRpmDAO
			            where repaymentRpmDAO.claimantCmtDAO.cmtId = :claimantId
			            and repaymentRpmDAO.rpmReceivedDt >= TRUNC(SYSDATE) - :noOfDays
			            and repaymentRpmDAO.rpmCheckWageGarnishment = :activeInd
			""")
    List<RepaymentRpmDAO> checkPaymentRecievedInDays(Long claimantId,
                                                  Integer noOfDays, String activeInd);

	@Query("""          
			from RepaymentRpmDAO repaymentRpmDAO
			            where repaymentRpmDAO.claimantCmtDAO.cmtId = :claimantId
			            and repaymentRpmDAO.rpmReceivedDt >= (select MAX(TRUNC(ccaseActivitiesCmaDAO.cmaCreatedTs))
			            					FROM CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO where ccaseActivitiesCmaDAO.cmaActivityTypeCd in (:activityTypeList))
			""")
	List<RepaymentRpmDAO> checkPaymentSincePPLetter(Long claimantId,
													 List<Long> activityTypeList);
}
