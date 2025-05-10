package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.ClmLofClfDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClmLofClfRepository extends CrudRepository<ClmLofClfDAO, Long> {

    @Query("""          
			from ClmLofClfDAO clmLofClfDAO
			            where clmLofClfDAO.fkClmId
			            in (select MAX(claimClmDAO.clmId) from ClaimClmDAO claimClmDAO where claimClmDAO.claimantCmtDAO.cmtId = :claimantId)
			            and clmLofClfDAO.clfActiveInd = :activeInd	            
			""")
    List<ClmLofClfDAO> getClaimLocalOfficeByClaimantId(Long claimantId,
                                                    String activeInd);
}
