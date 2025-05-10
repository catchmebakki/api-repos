package com.ssi.ms.fraudreview.database.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CLAIM_CLM")
@Setter
@Getter
public class ClaimClmDAO {
	@Id
	@Column(name = "CLM_ID", unique = true)
	private Long clmId;

	@Column(name = "CLM_1ST_FULPAY_IND", length = 1)
	private String clm1stFulpayInd;

	@Column(name = "CLM_1ST_PAY_COMP_DT")
	private Date clm1stPayCompDt;

	@Column(name = "CLM_1ST_PAY_COMP_IND", length = 1)
	private String clm1stPayCompInd;

	@Column(name = "CLM_1ST_PAY_DUE_DT")
	private Date clm1stPayDueDt;

	@Column(name = "CLM_1ST_PAY_ISS_DT")
	private Date clm1stPayIssDt;

	@Column(name = "CLM_1ST_PAY_WE_DT")
	private Date clm1stPayWeDt;

	@Column(name = "CLM_1ST_PAY_WK_IND", length = 1)
	private String clm1stPayWkInd;

	@Column(name = "CLM_ABY_IND", length = 1)
	private String clmAbyInd;

	@Column(name = "CLM_ACTUAL_DEP_CD", precision = 4)
	private BigDecimal clmActualDepCd;

	@Column(name = "CLM_AFFIDAVIT_IND", length = 1)
	private String clmAffidavitInd;

	@Column(name = "CLM_APPLICATION_DT")
	private Date clmApplicationDt;

	@Column(name = "CLM_ASSIGN_LOF_IFK")
	private Long clmAssignLofIfk;

	@Column(name = "CLM_ATAA_IND", length = 1)
	private String clmAtaaInd;

	@Column(name = "CLM_AWW_AMT", precision = 9, scale = 2)
	private BigDecimal clmAwwAmt;

	@Column(name = "CLM_BACK_DT_RSN_CD", precision = 4)
	private BigDecimal clmBackDtRsnCd;

	@Column(name = "CLM_BEN_YR_END_DT")
	private Date clmBenYrEndDt;

	@Column(name = "CLM_BIC_DT")
	private Date clmBicDt;

	@Column(name = "CLM_BP_BEGIN_DT")
	private Date clmBpBeginDt;

	@Column(name = "CLM_BP_END_DT")
	private Date clmBpEndDt;

	@Column(name = "CLM_CC_1ST_WK_IND", length = 1)
	private String clmCc1stWkInd;

	@Column(name = "CLM_CCC_MAIL_DT")
	private Date clmCccMailDt;

	@Column(name = "CLM_CCC_REPROC_IND", length = 1)
	private String clmCccReprocInd;

	@Column(name = "CLM_COMMUTER_IND", length = 1)
	private String clmCommuterInd;

	@Column(name = "CLM_CONVERSION_IND", length = 1)
	private String clmConversionInd;

	@Column(name = "CLM_CWC_CHOICE_CD", precision = 4)
	private BigDecimal clmCwcChoiceCd;

	@Column(name = "CLM_CWC_IND", length = 1)
	private String clmCwcInd;

	@Column(name = "CLM_CWC_REQ_COUNT", precision = 4)
	private BigDecimal clmCwcReqCount;

	@Column(name = "CLM_CWCT_IND", length = 1)
	private String clmCwctInd;

	@Column(name = "CLM_CYBER_ISN", precision = 10)
	private BigDecimal clmCyberIsn;

	@Column(name = "CLM_CYCLE_CD", precision = 4)
	private BigDecimal clmCycleCd;

	@Column(name = "CLM_DUA_BP_BEG_DT")
	private Date clmDuaBpBegDt;

	@Column(name = "CLM_DUA_BP_END_DT")
	private Date clmDuaBpEndDt;

	@Column(name = "CLM_EFFECTIVE_DT")
	private Date clmEffectiveDt;

	@Column(name = "CLM_ESTABLISHED_TS")
	private Timestamp clmEstablishedTs;

	@Column(name = "CLM_FIN_PAY_WE_DT")
	private Date clmFinPayWeDt;

	@Column(name = "CLM_FUNC_DEP_CD", precision = 4)
	private BigDecimal clmFuncDepCd;

	@Column(name = "CLM_IB1_IND", length = 1)
	private String clmIb1Ind;

	@Column(name = "CLM_INTER_AGN_IND", length = 1)
	private String clmInterAgnInd;

	@Column(name = "CLM_INTER_LIAB_IND", length = 1)
	private String clmInterLiabInd;

	@Column(name = "CLM_LAST_UPD_TS")
	private Timestamp clmLastUpdTs;

	@Column(name = "CLM_LIAB_STATE_CD", length = 2)
	private String clmLiabStateCd;

	@Column(name = "CLM_NXT_CC_TYPE_CD", precision = 4)
	private BigDecimal clmNxtCcTypeCd;

	@Column(name = "CLM_REG_IND", length = 1)
	private String clmRegInd;

	@Column(name = "CLM_SCHOOL_IND", length = 1)
	private String clmSchoolInd;

	@Column(name = "CLM_SEASONAL_IND", length = 1)
	private String clmSeasonalInd;

	@Column(name = "CLM_STATUS_CD", precision = 4)
	private BigDecimal clmStatusCd;

	@Column(name = "CLM_STATUS_UPD_TS")
	private Timestamp clmStatusUpdTs;

	@Column(name = "CLM_TAA_IND", length = 1)
	private String clmTaaInd;

	@Column(name = "CLM_UCFE_IND", length = 1)
	private String clmUcfeInd;

	@Column(name = "CLM_UCFE_REQ_COUNT", precision = 4)
	private BigDecimal clmUcfeReqCount;

	@Column(name = "CLM_UCX_IND", length = 1)
	private String clmUcxInd;

	@Column(name = "CLM_UCX_REQ_COUNT", precision = 4)
	private BigDecimal clmUcxReqCount;

	@Column(name = "CLM_WAITING_WK_DT")
	private Date clmWaitingWkDt;

	@Column(name = "FK_CMT_ID")
	private Long claimantCmt;

}
