package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OpmPayPlanOppRepository extends CrudRepository<OpmPayPlanOppDAO, Long> {

    @Query("""          
			from OpmPayPlanOppDAO opmPayPlanOpp
			            where opmPayPlanOpp.oppEndDt is null
			            and opmPayPlanOpp.claimantCmtDAO.cmtId = :claimantId
			            order by opmPayPlanOpp.oppId desc
			""")
    List<OpmPayPlanOppDAO> getOverpaymentPlanInfo(Long claimantId);
}
