package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the CLAIM_CLM database table.
 * 
 */
@Entity
@Table(name = "CLAIM_CLM")
@Data
public class ClaimClmDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLM_ID")
	private Long clmId;

	@Column(name = "CLM_1ST_FULPAY_IND")
	private String clm1stFulpayInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_1ST_PAY_COMP_DT")
	private Date clm1stPayCompDt;

	@Column(name = "CLM_1ST_PAY_COMP_IND")
	private String clm1stPayCompInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_1ST_PAY_DUE_DT")
	private Date clm1stPayDueDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_1ST_PAY_ISS_DT")
	private Date clm1stPayIssDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_1ST_PAY_WE_DT")
	private Date clm1stPayWeDt;

	@Column(name = "CLM_1ST_PAY_WK_IND")
	private String clm1stPayWkInd;

	@Column(name = "CLM_ABY_IND")
	private String clmAbyInd;

	@Column(name = "CLM_ACTUAL_DEP_CD")
	private BigDecimal clmActualDepCd;

	@Column(name = "CLM_AFFIDAVIT_IND")
	private String clmAffidavitInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_APPLICATION_DT")
	private Date clmApplicationDt;

	@Column(name = "CLM_ASSIGN_LOF_IFK")
	private Long clmAssignLofIfk;

	@Column(name = "CLM_ATAA_IND")
	private String clmAtaaInd;

	@Column(name = "CLM_AWW_AMT")
	private BigDecimal clmAwwAmt;

	@Column(name = "CLM_BACK_DT_RSN_CD")
	private BigDecimal clmBackDtRsnCd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_BEN_YR_END_DT")
	private Date clmBenYrEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_BIC_DT")
	private Date clmBicDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_BP_BEGIN_DT")
	private Date clmBpBeginDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_BP_END_DT")
	private Date clmBpEndDt;

	@Column(name = "CLM_CC_1ST_WK_IND")
	private String clmCc1stWkInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_CCC_MAIL_DT")
	private Date clmCccMailDt;

	@Column(name = "CLM_CCC_REPROC_IND")
	private String clmCccReprocInd;

	@Column(name = "CLM_COMMUTER_IND")
	private String clmCommuterInd;

	@Column(name = "CLM_CONVERSION_IND")
	private String clmConversionInd;

	@Column(name = "CLM_CWC_CHOICE_CD")
	private BigDecimal clmCwcChoiceCd;

	@Column(name = "CLM_CWC_IND")
	private String clmCwcInd;

	@Column(name = "CLM_CWC_REQ_COUNT")
	private BigDecimal clmCwcReqCount;

	@Column(name = "CLM_CWCT_IND")
	private String clmCwctInd;

	@Column(name = "CLM_CYBER_ISN")
	private BigDecimal clmCyberIsn;

	@Column(name = "CLM_CYCLE_CD")
	private BigDecimal clmCycleCd;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_DUA_BP_BEG_DT")
	private Date clmDuaBpBegDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_DUA_BP_END_DT")
	private Date clmDuaBpEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_EFFECTIVE_DT")
	private Date clmEffectiveDt;

	@Column(name = "CLM_ESTABLISHED_TS")
	private Timestamp clmEstablishedTs;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_FIN_PAY_WE_DT")
	private Date clmFinPayWeDt;

	@Column(name = "CLM_FUNC_DEP_CD")
	private BigDecimal clmFuncDepCd;

	@Column(name = "CLM_IB1_IND")
	private String clmIb1Ind;

	@Column(name = "CLM_INTER_AGN_IND")
	private String clmInterAgnInd;

	@Column(name = "CLM_INTER_LIAB_IND")
	private String clmInterLiabInd;

	@Column(name = "CLM_LAST_UPD_TS")
	private Timestamp clmLastUpdTs;

	@Column(name = "CLM_LIAB_STATE_CD")
	private String clmLiabStateCd;

	@Column(name = "CLM_NXT_CC_TYPE_CD")
	private BigDecimal clmNxtCcTypeCd;

	@Column(name = "CLM_REG_IND")
	private String clmRegInd;

	@Column(name = "CLM_SCHOOL_IND")
	private String clmSchoolInd;

	@Column(name = "CLM_SEASONAL_IND")
	private String clmSeasonalInd;

	@Column(name = "CLM_STATUS_CD")
	private BigDecimal clmStatusCd;

	@Column(name = "CLM_STATUS_UPD_TS")
	private Timestamp clmStatusUpdTs;

	@Column(name = "CLM_TAA_IND")
	private String clmTaaInd;

	@Column(name = "CLM_UCFE_IND")
	private String clmUcfeInd;

	@Column(name = "CLM_UCFE_REQ_COUNT")
	private BigDecimal clmUcfeReqCount;

	@Column(name = "CLM_UCX_IND")
	private String clmUcxInd;

	@Column(name = "CLM_UCX_REQ_COUNT")
	private BigDecimal clmUcxReqCount;

	@Temporal(TemporalType.DATE)
	@Column(name = "CLM_WAITING_WK_DT")
	private Date clmWaitingWkDt;

	// bi-directional many-to-one association to Claimant
	@ManyToOne
	@JoinColumn(name = "FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

}