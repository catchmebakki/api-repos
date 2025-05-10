package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

@Repository
public interface CorrespondenceCorRepository extends CrudRepository<CorrespondenceCorDAO, Long> {
	@Transactional
	@Procedure(name = "createCorrespondence")
	Map<String, Object> createCorrespondence(@Param(CollecticaseConstants.PIN_WLP_I720_RPT_ID) Integer reportId,
											   @Param(CollecticaseConstants.PIN_WLP_I720_CLM_ID) Integer claimId,
											   @Param(CollecticaseConstants.PIN_WLP_I720_EMP_ID) Integer employerId,
											   @Param(CollecticaseConstants.PIN_WLP_I720_CMT_ID) Integer claimantId,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_COE_IND) String corCoeInd,
											   @Param(CollecticaseConstants.PIN_WLP_I720_FORCED_IND) String corForcedInd,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_STATUS_CD) Integer corStatusCd,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_DEC_ID_IFK) Integer corDecId,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK) Integer corReceipientIfk,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_CD) Integer corReceipientCd,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COR_TS) Timestamp corTimeStamp,
											   @Param(CollecticaseConstants.PIN_WLP_I720_COE_STRING) String corCoeString);
    }
