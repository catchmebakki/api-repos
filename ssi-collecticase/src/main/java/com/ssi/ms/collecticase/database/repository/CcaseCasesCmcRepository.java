package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCasesCmcDAO;
import com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
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

	@Transactional
	@Procedure(name = "getCaseLoadSummary")
	Map<String, Object> getCaseLoadSummary(@Param(CollecticaseConstants.PIN_STF_ID) Long staffId);

	@Transactional
	@Procedure(name = "caseLookup")
	Map<String, Object> getCaseLookupData(@Param(CollecticaseConstants.PIN_CASE_NUM) Long caseNo,
										  @Param(CollecticaseConstants.PIN_SSN) String claimantSSN,
										  @Param(CollecticaseConstants.PIN_LAST_NAME) String claimantLastName,
										  @Param(CollecticaseConstants.PIN_FIRST_NAME) String claimantFirstName,
										  @Param(CollecticaseConstants.PIN_OP_TYPE) String opmType,
										  @Param(CollecticaseConstants.PIN_OP_BAL_RANGE_FROM) BigDecimal opmBalRangeFrom,
										  @Param(CollecticaseConstants.PIN_OP_BAL_RANGE_TO) BigDecimal opmBalRangeTo,
										  @Param(CollecticaseConstants.PIN_CASE_PRIORITY) Long casePriority,
										  @Param(CollecticaseConstants.PIN_NEXT_FOLLOW_UP) String nextFollowup,
										  @Param(CollecticaseConstants.PIN_BKT_STATUS) Long bankruptcyStatus,
										  @Param(CollecticaseConstants.PIN_ASSIGNED_TO) Long assignedTo,
										  @Param(CollecticaseConstants.PIN_TELE_NUM) String telephoneNumber,
										  @Param(CollecticaseConstants.PIN_CASE_OPEN) String caseOpen,
										  @Param(CollecticaseConstants.PIN_REMEDY) Long caseRemedy,
										  @Param(CollecticaseConstants.PIN_REMEDY_ST_FROM_DT) Date caseRemedyFromDate,
										  @Param(CollecticaseConstants.PIN_REMEDY_ST_TO_DT) Date caseRemedyToDate,
										  @Param(CollecticaseConstants.PIN_CASE_OPEN_FROM_DT) Date caseOpenFromDate,
										  @Param(CollecticaseConstants.PIN_CASE_OPEN_TO_DT) Date caseOpenToDate,
										  @Param(CollecticaseConstants.PIN_RPM_FROM_DT) Date repaymentFromDate,
										  @Param(CollecticaseConstants.PIN_RPM_TO_DT) Date repaymentToDate
	);


	@Query("""
            SELECT new com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO(
            cmcCaseCharacteristics as caseCharacteristics, cmcCmtRepTypeCd as cmtRepTypeCd)
            from CcaseCasesCmcDAO cCaseCasesCmcDAO
			where cCaseCasesCmcDAO.cmcId = :caseId
			""")
	CcaseCasesCmcDTO getCaseCmcByCaseId(Long caseId);

	}