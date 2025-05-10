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
 * The persistent class for the CCASE_WAGE_GARNISHMENT_CWG database table.
 * 
 */
@Entity
@Table(name="CCASE_WAGE_GARNISHMENT_CWG")
@Data
public class CcaseWageGarnishmentCwgDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CWG_ID")
	private long cwgId;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_CHANGE_EFF_FROM_DT")
	private Date cwgChangeEffFromDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_CHANGE_EFF_UNTIL_DT")
	private Date cwgChangeEffUntilDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_CHANGE_REQ_DT")
	private Date cwgChangeReqDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_COURT_ORDER_DT")
	private Date cwgCourtOrderDt;

	@Column(name="CWG_COURT_ORDERED")
	private String cwgCourtOrdered;

	@Column(name="CWG_CREATED_BY")
	private String cwgCreatedBy;

	@Column(name="CWG_CREATED_SOURCE")
	private Long cwgCreatedSource;

	@Column(name="CWG_CREATED_TS")
	private Timestamp cwgCreatedTs;

	@Column(name="CWG_CREATED_USING")
	private String cwgCreatedUsing;

	@Column(name="CWG_DO_NOT_GARNISH")
	private String cwgDoNotGarnish;

	@Column(name="CWG_EMP_REP_CD")
	private Long cwgEmpRepCd;

	@Column(name="CWG_FREQ_CD")
	private Long cwgFreqCd;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_LAST_ACTIVITY_DT")
	private Date cwgLastActivityDt;

	@Column(name="CWG_LAST_UPD_BY")
	private String cwgLastUpdBy;

	@Column(name="CWG_LAST_UPD_TS")
	private Timestamp cwgLastUpdTs;

	@Column(name="CWG_LAST_UPD_USING")
	private String cwgLastUpdUsing;

	@Column(name="CWG_NEXT_STEP_CD")
	private Long cwgNextStepCd;

	@Column(name="CWG_NON_COMPL_CD")
	private Long cwgNonComplCd;

	@Temporal(TemporalType.DATE)
	@Column(name="CWG_NOTICE_OF_WG_DT")
	private Date cwgNoticeOfWgDt;

	@Column(name="CWG_STAGE_CD")
	private Long cwgStageCd;

	@Column(name="CWG_STATUS_CD")
	private Long cwgStatusCd;

	@Column(name="CWG_SUSPENDED")
	private String cwgSuspended;

	@Column(name="CWG_WG_AMOUNT")
	private BigDecimal cwgWgAmount;

	//bi-directional many-to-one association to ccaseCaseRemedyCmrDAO
	@ManyToOne
	@JoinColumn(name="FK_CMR_ID")
	private CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO;

	@Column(name="FK_CMI_ID_REP")
	private Long fkCmiIdRep;

	@Column(name="FK_CMI_ID_EMP_CONT")
	private Long fkCmiIdEmpCont;

	@Column(name="FK_CMO_ID_REP")
	private Long fkCmoIdRep;

	@Column(name="FK_CMO_ID_COURT")
	private Long fkCmoIdCourt;

	//bi-directional many-to-one association to EmployerEmpDAO
	@ManyToOne
	@JoinColumn(name="FK_EMP_ID")
	private EmployerEmpDAO employerEmpDAO;


	public long getCwgId() {
		return cwgId;
	}

	public void setCwgId(long cwgId) {
		this.cwgId = cwgId;
	}

	public Date getCwgChangeEffFromDt() {
		return cwgChangeEffFromDt;
	}

	public void setCwgChangeEffFromDt(Date cwgChangeEffFromDt) {
		this.cwgChangeEffFromDt = cwgChangeEffFromDt;
	}

	public Date getCwgChangeEffUntilDt() {
		return cwgChangeEffUntilDt;
	}

	public void setCwgChangeEffUntilDt(Date cwgChangeEffUntilDt) {
		this.cwgChangeEffUntilDt = cwgChangeEffUntilDt;
	}

	public Date getCwgChangeReqDt() {
		return cwgChangeReqDt;
	}

	public void setCwgChangeReqDt(Date cwgChangeReqDt) {
		this.cwgChangeReqDt = cwgChangeReqDt;
	}

	public Date getCwgCourtOrderDt() {
		return cwgCourtOrderDt;
	}

	public void setCwgCourtOrderDt(Date cwgCourtOrderDt) {
		this.cwgCourtOrderDt = cwgCourtOrderDt;
	}

	public String getCwgCourtOrdered() {
		return cwgCourtOrdered;
	}

	public void setCwgCourtOrdered(String cwgCourtOrdered) {
		this.cwgCourtOrdered = cwgCourtOrdered;
	}

	public String getCwgCreatedBy() {
		return cwgCreatedBy;
	}

	public void setCwgCreatedBy(String cwgCreatedBy) {
		this.cwgCreatedBy = cwgCreatedBy;
	}

	public Long getCwgCreatedSource() {
		return cwgCreatedSource;
	}

	public void setCwgCreatedSource(Long cwgCreatedSource) {
		this.cwgCreatedSource = cwgCreatedSource;
	}

	public Timestamp getCwgCreatedTs() {
		return cwgCreatedTs;
	}

	public void setCwgCreatedTs(Timestamp cwgCreatedTs) {
		this.cwgCreatedTs = cwgCreatedTs;
	}

	public String getCwgCreatedUsing() {
		return cwgCreatedUsing;
	}

	public void setCwgCreatedUsing(String cwgCreatedUsing) {
		this.cwgCreatedUsing = cwgCreatedUsing;
	}

	public String getCwgDoNotGarnish() {
		return cwgDoNotGarnish;
	}

	public void setCwgDoNotGarnish(String cwgDoNotGarnish) {
		this.cwgDoNotGarnish = cwgDoNotGarnish;
	}

	public Long getCwgEmpRepCd() {
		return cwgEmpRepCd;
	}

	public void setCwgEmpRepCd(Long cwgEmpRepCd) {
		this.cwgEmpRepCd = cwgEmpRepCd;
	}

	public Long getCwgFreqCd() {
		return cwgFreqCd;
	}

	public void setCwgFreqCd(Long cwgFreqCd) {
		this.cwgFreqCd = cwgFreqCd;
	}

	public Date getCwgLastActivityDt() {
		return cwgLastActivityDt;
	}

	public void setCwgLastActivityDt(Date cwgLastActivityDt) {
		this.cwgLastActivityDt = cwgLastActivityDt;
	}

	public String getCwgLastUpdBy() {
		return cwgLastUpdBy;
	}

	public void setCwgLastUpdBy(String cwgLastUpdBy) {
		this.cwgLastUpdBy = cwgLastUpdBy;
	}

	public Timestamp getCwgLastUpdTs() {
		return cwgLastUpdTs;
	}

	public void setCwgLastUpdTs(Timestamp cwgLastUpdTs) {
		this.cwgLastUpdTs = cwgLastUpdTs;
	}

	public String getCwgLastUpdUsing() {
		return cwgLastUpdUsing;
	}

	public void setCwgLastUpdUsing(String cwgLastUpdUsing) {
		this.cwgLastUpdUsing = cwgLastUpdUsing;
	}

	public Long getCwgNextStepCd() {
		return cwgNextStepCd;
	}

	public void setCwgNextStepCd(Long cwgNextStepCd) {
		this.cwgNextStepCd = cwgNextStepCd;
	}

	public Long getCwgNonComplCd() {
		return cwgNonComplCd;
	}

	public void setCwgNonComplCd(Long cwgNonComplCd) {
		this.cwgNonComplCd = cwgNonComplCd;
	}

	public Date getCwgNoticeOfWgDt() {
		return cwgNoticeOfWgDt;
	}

	public void setCwgNoticeOfWgDt(Date cwgNoticeOfWgDt) {
		this.cwgNoticeOfWgDt = cwgNoticeOfWgDt;
	}

	public Long getCwgStageCd() {
		return cwgStageCd;
	}

	public void setCwgStageCd(Long cwgStageCd) {
		this.cwgStageCd = cwgStageCd;
	}

	public Long getCwgStatusCd() {
		return cwgStatusCd;
	}

	public void setCwgStatusCd(Long cwgStatusCd) {
		this.cwgStatusCd = cwgStatusCd;
	}

	public String getCwgSuspended() {
		return cwgSuspended;
	}

	public void setCwgSuspended(String cwgSuspended) {
		this.cwgSuspended = cwgSuspended;
	}

	public BigDecimal getCwgWgAmount() {
		return cwgWgAmount;
	}

	public void setCwgWgAmount(BigDecimal cwgWgAmount) {
		this.cwgWgAmount = cwgWgAmount;
	}

	public CcaseCaseRemedyCmrDAO getCcaseCaseRemedyCmrDAO() {
		return ccaseCaseRemedyCmrDAO;
	}

	public void setCcaseCaseRemedyCmrDAO(CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO) {
		this.ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrDAO;
	}

	public Long getFkCmiIdRep() {
		return fkCmiIdRep;
	}

	public void setFkCmiIdRep(Long fkCmiIdRep) {
		this.fkCmiIdRep = fkCmiIdRep;
	}

	public Long getFkCmiIdEmpCont() {
		return fkCmiIdEmpCont;
	}

	public void setFkCmiIdEmpCont(Long fkCmiIdEmpCont) {
		this.fkCmiIdEmpCont = fkCmiIdEmpCont;
	}

	public Long getFkCmoIdRep() {
		return fkCmoIdRep;
	}

	public void setFkCmoIdRep(Long fkCmoIdRep) {
		this.fkCmoIdRep = fkCmoIdRep;
	}

	public Long getFkCmoIdCourt() {
		return fkCmoIdCourt;
	}

	public void setFkCmoIdCourt(Long fkCmoIdCourt) {
		this.fkCmoIdCourt = fkCmoIdCourt;
	}

	public EmployerEmpDAO getEmployerEmpDAO() {
		return employerEmpDAO;
	}

	public void setEmployerEmpDAO(EmployerEmpDAO employerEmpDAO) {
		this.employerEmpDAO = employerEmpDAO;
	}
}