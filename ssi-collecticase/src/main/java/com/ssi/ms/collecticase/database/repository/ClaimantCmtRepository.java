package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.ClaimantCmtDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimantCmtRepository extends CrudRepository<ClaimantCmtDAO, Long> {

    @Query("""  
            SELECT claimantCmtDAO.cmtId
          from ClaimantCmtDAO claimantCmtDAO
                      where claimantCmtDAO.ssn = :claimantSSN
          """)
    Long getClaimantByClaimantSSN(String claimantSSN);
}