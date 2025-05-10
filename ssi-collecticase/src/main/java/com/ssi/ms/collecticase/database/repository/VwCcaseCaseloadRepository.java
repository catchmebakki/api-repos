package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VwCcaseCaseloadRepository extends CrudRepository<VwCcaseCaseloadDAO, Long> {

    @Query("""          
			from VwCcaseCaseloadDAO vwCcaseCaseload where vwCcaseCaseload.staffId = :staffId
			""")
    Page<VwCcaseCaseloadDAO> getCaseLoadByStaffId(Long staffId, Pageable pageable);

    @Query("""          
			from VwCcaseHeaderDAO vwCcaseHeader where vwCcaseHeader.caseNo = :caseId
			""")
    List<VwCcaseHeaderDAO> getCaseHeaderInfoByCaseId(Long caseId);

    @Query("""          
			from VwCcaseOpmDAO vwCcaseOpm
			  JOIN CcaseCasesCmcDAO ccaseCasesCmc
			  ON vwCcaseOpm.fkCmtId = ccaseCasesCmc.claimantCmtDAO.cmtId
			  where ccaseCasesCmc.cmcId = :caseId
			""")
    List<VwCcaseOpmDAO> getClaimantOpmInfoByCaseId(Long caseId);

    @Query("""          
			from VwCcaseRemedyDAO vwCcaseRemedy where vwCcaseRemedy.cmcId = :caseId
			""")
    List<VwCcaseRemedyDAO> getCaseRemedyInfoByCaseId(Long caseId);

    @Query("""          
			from VwCcaseHeaderEntityDAO vwCcaseHeaderEntity where vwCcaseHeaderEntity.caseId = :caseId
			""")
    List<VwCcaseHeaderEntityDAO> getCaseEntityInfoByCaseId(Long caseId);
}
