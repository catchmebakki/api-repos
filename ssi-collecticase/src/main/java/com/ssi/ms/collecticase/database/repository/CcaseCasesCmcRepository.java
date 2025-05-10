package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCasesCmcDAO;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public interface CcaseCasesCmcRepository extends CrudRepository<CcaseCasesCmcDAO, Long> {

	@Transactional
	@Procedure(name = "createCase")
	Map<String, Object> createCollecticaseCase(@Param(CollecticaseConstants.PIN_CMT) Long claimantId,
											   @Param(CollecticaseConstants.PIN_STAFF_ID) Long staffId,
											   @Param(CollecticaseConstants.PIN_PRIORITY) Long casePriority,
											   @Param(CollecticaseConstants.PIN_REMEDY_CD) Long caseRemedyCd,
											   @Param(CollecticaseConstants.PIN_ACTIVITY_CD) Long caseActivityCd,
											   @Param(CollecticaseConstants.PIN_USER) String callingUser,
											   @Param(CollecticaseConstants.PIN_USING) String usingProgramName);

	}