package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="CCASE_CASE_REMEDY_CMR")
@Transactional
@NamedStoredProcedureQuery(
		name  =  "updateCaseRemedy",
		procedureName  =  "pkg_collect.update_case_remedy",
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CMA_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_EMP_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_SUCCESS, type = Long.class)
		}
)
@Data
public class CcaseCaseRemedyCmrDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CMR_SEQ_GEN")
	@SequenceGenerator(name = "CMR_SEQ_GEN", sequenceName = "CCASE_CMR_ID_SEQ", allocationSize = 1)
	@Column(name="CMR_ID")
	private Long cmrId;

	@Column(name="CMR_CREATED_BY")
	private String cmrCreatedBy;

	@Column(name="CMR_CREATED_TS")
	private Timestamp cmrCreatedTs;

	@Column(name="CMR_CREATED_USING")
	private String cmrCreatedUsing;	

	@Temporal(TemporalType.DATE)
	@Column(name="CMR_LAST_ACTIVITY_DT")
	private Date cmrLastActivityDt;

	@Column(name="CMR_LAST_UPD_BY")
	private String cmrLastUpdBy;

	@Column(name="CMR_LAST_UPD_TS")
	private Timestamp cmrLastUpdTs;

	@Column(name="CMR_LAST_UPD_USING")
	private String cmrLastUpdUsing;

	@Column(name="CMR_NEXT_STEP_CD")
	private Long cmrNextStepCd;
	
	@Column(name="CMR_GN_FK_CTY_CD")
	private Long cmrGnFkCtyCd;	

	@Column(name="CMR_PP_EFF_MONTHS")
	private Long cmrPpEffMonths;

	@Temporal(TemporalType.DATE)
	@Column(name="CMR_PP_EFF_UNTIL_DT")
	private Date cmrPpEffUntilDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CMR_PP_FA_SIGNED_DT")
	private Date cmrPpFaSignedDt;

	@Column(name="CMR_PP_GUIDELINE_AMT")
	private BigDecimal cmrPpGuidelineAmt;

	@Column(name="CMR_PP_PAYMENT_AMT")
	private BigDecimal cmrPpPaymentAmt;

	@Column(name="CMR_PP_PAYMENT_CATG_CD")
	private Long cmrPpPaymentCatgCd;

	@Temporal(TemporalType.DATE)
	@Column(name="CMR_PP_PP_SIGNED_DT")
	private Date cmrPpPpSignedDt;

	@Column(name="CMR_PP_RESP_TO_CD")
	private Long cmrPpRespToCd;

	@Column(name="CMR_PP_RESP_TO_OTHER")
	private String cmrPpRespToOther;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CMR_FIRST_INITIATED_DT")
	private Date cmrFirstInitiatedDt;

	@Column(name="CMR_REMARKS")
	private String cmrRemarks;

	@Column(name="CMR_REMEDY_CD")
	private Long cmrRemedyCd;

	@Column(name="CMR_STAGE_CD")
	private Long cmrStageCd;

	@Column(name="CMR_STATUS_CD")
	private Long cmrStatusCd;

	//	bi-directional many-to-one association to ccaseCasesCmcDAO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FK_CMC_ID")
	private CcaseCasesCmcDAO ccaseCasesCmcDAO;

	public Long getCmrId() {
		return cmrId;
	}

	public void setCmrId(Long cmrId) {
		this.cmrId = cmrId;
	}

	public String getCmrCreatedBy() {
		return cmrCreatedBy;
	}

	public void setCmrCreatedBy(String cmrCreatedBy) {
		this.cmrCreatedBy = cmrCreatedBy;
	}

	public Timestamp getCmrCreatedTs() {
		return cmrCreatedTs;
	}

	public void setCmrCreatedTs(Timestamp cmrCreatedTs) {
		this.cmrCreatedTs = cmrCreatedTs;
	}

	public String getCmrCreatedUsing() {
		return cmrCreatedUsing;
	}

	public void setCmrCreatedUsing(String cmrCreatedUsing) {
		this.cmrCreatedUsing = cmrCreatedUsing;
	}

	public Date getCmrLastActivityDt() {
		return cmrLastActivityDt;
	}

	public void setCmrLastActivityDt(Date cmrLastActivityDt) {
		this.cmrLastActivityDt = cmrLastActivityDt;
	}

	public String getCmrLastUpdBy() {
		return cmrLastUpdBy;
	}

	public void setCmrLastUpdBy(String cmrLastUpdBy) {
		this.cmrLastUpdBy = cmrLastUpdBy;
	}

	public Timestamp getCmrLastUpdTs() {
		return cmrLastUpdTs;
	}

	public void setCmrLastUpdTs(Timestamp cmrLastUpdTs) {
		this.cmrLastUpdTs = cmrLastUpdTs;
	}

	public String getCmrLastUpdUsing() {
		return cmrLastUpdUsing;
	}

	public void setCmrLastUpdUsing(String cmrLastUpdUsing) {
		this.cmrLastUpdUsing = cmrLastUpdUsing;
	}

	public Long getCmrNextStepCd() {
		return cmrNextStepCd;
	}

	public void setCmrNextStepCd(Long cmrNextStepCd) {
		this.cmrNextStepCd = cmrNextStepCd;
	}

	public Long getCmrGnFkCtyCd() {
		return cmrGnFkCtyCd;
	}

	public void setCmrGnFkCtyCd(Long cmrGnFkCtyCd) {
		this.cmrGnFkCtyCd = cmrGnFkCtyCd;
	}

	public Long getCmrPpEffMonths() {
		return cmrPpEffMonths;
	}

	public void setCmrPpEffMonths(Long cmrPpEffMonths) {
		this.cmrPpEffMonths = cmrPpEffMonths;
	}

	public Date getCmrPpEffUntilDt() {
		return cmrPpEffUntilDt;
	}

	public void setCmrPpEffUntilDt(Date cmrPpEffUntilDt) {
		this.cmrPpEffUntilDt = cmrPpEffUntilDt;
	}

	public Date getCmrPpFaSignedDt() {
		return cmrPpFaSignedDt;
	}

	public void setCmrPpFaSignedDt(Date cmrPpFaSignedDt) {
		this.cmrPpFaSignedDt = cmrPpFaSignedDt;
	}

	public BigDecimal getCmrPpGuidelineAmt() {
		return cmrPpGuidelineAmt;
	}

	public void setCmrPpGuidelineAmt(BigDecimal cmrPpGuidelineAmt) {
		this.cmrPpGuidelineAmt = cmrPpGuidelineAmt;
	}

	public BigDecimal getCmrPpPaymentAmt() {
		return cmrPpPaymentAmt;
	}

	public void setCmrPpPaymentAmt(BigDecimal cmrPpPaymentAmt) {
		this.cmrPpPaymentAmt = cmrPpPaymentAmt;
	}

	public Long getCmrPpPaymentCatgCd() {
		return cmrPpPaymentCatgCd;
	}

	public void setCmrPpPaymentCatgCd(Long cmrPpPaymentCatgCd) {
		this.cmrPpPaymentCatgCd = cmrPpPaymentCatgCd;
	}

	public Date getCmrPpPpSignedDt() {
		return cmrPpPpSignedDt;
	}

	public void setCmrPpPpSignedDt(Date cmrPpPpSignedDt) {
		this.cmrPpPpSignedDt = cmrPpPpSignedDt;
	}

	public Long getCmrPpRespToCd() {
		return cmrPpRespToCd;
	}

	public void setCmrPpRespToCd(Long cmrPpRespToCd) {
		this.cmrPpRespToCd = cmrPpRespToCd;
	}

	public String getCmrPpRespToOther() {
		return cmrPpRespToOther;
	}

	public void setCmrPpRespToOther(String cmrPpRespToOther) {
		this.cmrPpRespToOther = cmrPpRespToOther;
	}

	public Date getCmrFirstInitiatedDt() {
		return cmrFirstInitiatedDt;
	}

	public void setCmrFirstInitiatedDt(Date cmrFirstInitiatedDt) {
		this.cmrFirstInitiatedDt = cmrFirstInitiatedDt;
	}

	public String getCmrRemarks() {
		return cmrRemarks;
	}

	public void setCmrRemarks(String cmrRemarks) {
		this.cmrRemarks = cmrRemarks;
	}

	public Long getCmrRemedyCd() {
		return cmrRemedyCd;
	}

	public void setCmrRemedyCd(Long cmrRemedyCd) {
		this.cmrRemedyCd = cmrRemedyCd;
	}

	public Long getCmrStageCd() {
		return cmrStageCd;
	}

	public void setCmrStageCd(Long cmrStageCd) {
		this.cmrStageCd = cmrStageCd;
	}

	public Long getCmrStatusCd() {
		return cmrStatusCd;
	}

	public void setCmrStatusCd(Long cmrStatusCd) {
		this.cmrStatusCd = cmrStatusCd;
	}

	public CcaseCasesCmcDAO getCcaseCasesCmcDAO() {
		return ccaseCasesCmcDAO;
	}

	public void setCcaseCasesCmcDAO(CcaseCasesCmcDAO ccaseCasesCmcDAO) {
		this.ccaseCasesCmcDAO = ccaseCasesCmcDAO;
	}
}