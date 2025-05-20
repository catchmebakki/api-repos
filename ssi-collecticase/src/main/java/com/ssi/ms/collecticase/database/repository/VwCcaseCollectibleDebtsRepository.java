package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCollectibleDebtsDAO;
import com.ssi.ms.collecticase.dto.CaseCollectibleDebtsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VwCcaseCollectibleDebtsRepository extends CrudRepository<VwCcaseCollectibleDebtsDAO, Long> {

    @Query("""   
            SELECT new com.ssi.ms.collecticase.dto.CaseCollectibleDebtsDTO(
            SUM(vwCcaseCollectibleDebtsDAO.opmBalAmt) AS overpaymentBalanceAmount, SUM(vwCcaseCollectibleDebtsDAO.opmFrdBalAmt) AS overpaymentFraudBalanceAmount,
            SUM(vwCcaseCollectibleDebtsDAO.opmNfBalAmt) AS overpaymentNonFraudBalanceAmount, SUM(vwCcaseCollectibleDebtsDAO.opmIntBalAmt) AS overpaymentInterestBalanceAmount,
            vwCcaseCollectibleDebtsDAO.claimantCmtDAO.cmtId AS claimantId)   
    		from VwCcaseCollectibleDebtsDAO vwCcaseCollectibleDebtsDAO 
			where vwCcaseCollectibleDebtsDAO.claimantCmtDAO.cmtId = :claimantId
			and vwCcaseCollectibleDebtsDAO.opmBalAmt > :overpaymentBalanceAmount
			GROUP BY vwCcaseCollectibleDebtsDAO.claimantCmtDAO.cmtId
			""")
    CaseCollectibleDebtsDTO getCollectibleDebtsAmount(Long claimantId, BigDecimal overpaymentBalanceAmount);

	@Query("""          
       from VwCcaseCollectibleDebtsDAO vwCcaseCollectibleDebtsDAO
       where vwCcaseCollectibleDebtsDAO.claimantCmtDAO.ssn = :claimantSsn
       ORDER BY vwCcaseCollectibleDebtsDAO.claimantCmtDAO.ssn
       """)
	List<VwCcaseCollectibleDebtsDAO> getCollectionDebts(String claimantSsn);
}
