package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the REPAYMENT_RPM database table.
 * 
 */
@Entity
@Table(name="REPAYMENT_RPM")
@Data
public class RepaymentRpmDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RPM_ID")
	private long rpmId;

	@Column(name="RPM_AG_SRC_CD")
	private BigDecimal rpmAgSrcCd;

	@Column(name="RPM_AMT")
	private BigDecimal rpmAmt;

	@Column(name="RPM_BATCH_COMP_AMT")
	private BigDecimal rpmBatchCompAmt;

	@Column(name="RPM_CHECK_WAGE_GARNISHMENT")
	private String rpmCheckWageGarnishment;

	@Column(name="RPM_CLAIMANT_SSN")
	private String rpmClaimantSsn;

	@Column(name="RPM_COMMENTS_TXT")
	private String rpmCommentsTxt;

	@Column(name="RPM_CORR_RSN_CD")
	private BigDecimal rpmCorrRsnCd;

	@Column(name="RPM_CORR_SSN")
	private String rpmCorrSsn;

	@Column(name="RPM_CORRECTED_TS")
	private Timestamp rpmCorrectedTs;

	@Column(name="RPM_CR_BAL_AMT")
	private BigDecimal rpmCrBalAmt;

	@Column(name="RPM_CR_BAL_TS")
	private Timestamp rpmCrBalTs;

	@Column(name="RPM_CR_PAID_AMT")
	private BigDecimal rpmCrPaidAmt;

	@Column(name="RPM_CREATED_BY")
	private String rpmCreatedBy;

	@Column(name="RPM_ENTER_LOF_IFK")
	private BigDecimal rpmEnterLofIfk;

	@Column(name="RPM_LAST_UPD_TS")
	private Timestamp rpmLastUpdTs;

	@Column(name="RPM_NBR")
	private BigDecimal rpmNbr;

	@Column(name="RPM_POSTED_TS")
	private Timestamp rpmPostedTs;

	@Column(name="RPM_PW_NBR")
	private BigDecimal rpmPwNbr;

	@Column(name="RPM_REASON_TXT")
	private String rpmReasonTxt;

	@Column(name="RPM_RECEIPT_NBR")
	private String rpmReceiptNbr;

	@Temporal(TemporalType.DATE)
	@Column(name="RPM_RECEIVED_DT")
	private Date rpmReceivedDt;

	@Column(name="RPM_REF_NBR")
	private String rpmRefNbr;

	@Column(name="RPM_STATE_CD")
	private String rpmStateCd;

	@Column(name="RPM_STATUS_CD")
	private BigDecimal rpmStatusCd;

	@Column(name="RPM_TEMP_BAL_AMT")
	private BigDecimal rpmTempBalAmt;

	@Column(name="RPM_TEMP_PROC_IND")
	private String rpmTempProcInd;

	@Column(name="RPM_TYPE_CD")
	private BigDecimal rpmTypeCd;

	@Column(name="RPM_UI_ACCT_NBR")
	private String rpmUiAcctNbr;

	@Column(name="RPM_USER_REVERSE")
	private String rpmUserReverse;

	@Column(name="RPM_VERSION_NBR")
	private BigDecimal rpmVersionNbr;

	@Column(name="FK_CCT_ID")
	private Long fkCCTId;

	//bi-directional many-to-one association to claimantCmtDAO
	@ManyToOne
	@JoinColumn(name="FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

	@Column(name="RPM_FK_FAC_ID")
	private Long rpmFkFacId;

	@Column(name="FK_MUC_ID")
	private Long fkMucId;

	@Column(name="FK_PUC_ID")
	private Long fkPucId;

	@Column(name="FK_RBT_ID")
	private Long fkRbtId;
}