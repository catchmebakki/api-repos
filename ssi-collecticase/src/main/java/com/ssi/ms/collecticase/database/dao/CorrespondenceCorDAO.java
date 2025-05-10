package com.ssi.ms.collecticase.database.dao;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;


/**
 * The persistent class for the CORRESPONDENCE_COR database table.
 * 
 */
@Entity
@Table(name = "CORRESPONDENCE_COR")
@Transactional
@NamedStoredProcedureQuery(
		name  =  "createCorrespondence",
		procedureName  =  CollecticaseConstants.SP_AJI720,
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_RPT_ID, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_CLM_ID, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_EMP_ID, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_CMT_ID, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_COE_IND, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_FORCED_IND, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_STATUS_CD, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_DEC_ID_IFK, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_CD, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COR_TS, type = Timestamp.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_WLP_I720_COE_STRING, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_WLP_O720_COR_ID, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_WLP_O720_RETURN_CD, type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_WLP_O720_RETURN_MSG, type = String.class)
		}
)
@Data
public class CorrespondenceCorDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COR_ID", unique = true)
	private Long corId;

	@Temporal(TemporalType.DATE)
	@Column(name = "COR_ACTION_BY_DT")
	private Date corActionByDt;

	@Column(name = "COR_ARC_STATUS_IND")
	private String corArcStatusInd;

	@Column(name = "COR_COE_EXISTS_IND")
	private String corCoeExistsInd;

	@Column(name = "COR_DEC_ID_IFK")
	private Long corDecIdIfk;

	@Column(name = "COR_E_NOTIFY_CD")
	private BigDecimal corENotifyCd;

	@Column(name = "COR_INBX_STATUS_CD")
	private BigDecimal corInbxStatusCd;

	@Temporal(TemporalType.DATE)
	@Column(name = "COR_MAILED_DT")
	private Date corMailedDt;

	@Column(name = "COR_RECEIP_CD")
	private Long corReceipCd;

	@Column(name = "COR_RECEIP_IFK")
	private Long corReceipIfk;

	@Column(name = "COR_SOURCE_IFK")
	private Long corSourceIfk;

	@Column(name = "COR_SOURCE_IFK_CD")
	private Long corSourceIfkCd;

	@Column(name = "COR_STATUS_CD")
	private Long corStatusCd;

	@Column(name = "COR_TS")
	private Timestamp corTs;

	@Column(name = "COR_TYPE_CD")
	private Long corTypeCd;

	// bi-directional many-to-one association to Claimant
	@ManyToOne
	@JoinColumn(name = "FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

	// bi-directional many-to-one association to Claim
	@ManyToOne
	@JoinColumn(name = "FK_CLM_ID")
	private ClaimClmDAO claimClm;

	// bi-directional many-to-one association to Employer
	@ManyToOne
	@JoinColumn(name = "FK_EMP_ID")
	private EmployerEmpDAO employerEmpDAO;

	// bi-directional many-to-one association to Reports
	@ManyToOne
	@JoinColumn(name = "FK_RPT_ID")
	private ReportsRptDAO reportsRptDAO;

	public Long getCorId() {
		return corId;
	}

	public void setCorId(Long corId) {
		this.corId = corId;
	}

	public Date getCorActionByDt() {
		return corActionByDt;
	}

	public void setCorActionByDt(Date corActionByDt) {
		this.corActionByDt = corActionByDt;
	}

	public String getCorArcStatusInd() {
		return corArcStatusInd;
	}

	public void setCorArcStatusInd(String corArcStatusInd) {
		this.corArcStatusInd = corArcStatusInd;
	}

	public String getCorCoeExistsInd() {
		return corCoeExistsInd;
	}

	public void setCorCoeExistsInd(String corCoeExistsInd) {
		this.corCoeExistsInd = corCoeExistsInd;
	}

	public Long getCorDecIdIfk() {
		return corDecIdIfk;
	}

	public void setCorDecIdIfk(Long corDecIdIfk) {
		this.corDecIdIfk = corDecIdIfk;
	}

	public BigDecimal getCorENotifyCd() {
		return corENotifyCd;
	}

	public void setCorENotifyCd(BigDecimal corENotifyCd) {
		this.corENotifyCd = corENotifyCd;
	}

	public BigDecimal getCorInbxStatusCd() {
		return corInbxStatusCd;
	}

	public void setCorInbxStatusCd(BigDecimal corInbxStatusCd) {
		this.corInbxStatusCd = corInbxStatusCd;
	}

	public Date getCorMailedDt() {
		return corMailedDt;
	}

	public void setCorMailedDt(Date corMailedDt) {
		this.corMailedDt = corMailedDt;
	}

	public Long getCorReceipCd() {
		return corReceipCd;
	}

	public void setCorReceipCd(Long corReceipCd) {
		this.corReceipCd = corReceipCd;
	}

	public Long getCorReceipIfk() {
		return corReceipIfk;
	}

	public void setCorReceipIfk(Long corReceipIfk) {
		this.corReceipIfk = corReceipIfk;
	}

	public Long getCorSourceIfk() {
		return corSourceIfk;
	}

	public void setCorSourceIfk(Long corSourceIfk) {
		this.corSourceIfk = corSourceIfk;
	}

	public Long getCorSourceIfkCd() {
		return corSourceIfkCd;
	}

	public void setCorSourceIfkCd(Long corSourceIfkCd) {
		this.corSourceIfkCd = corSourceIfkCd;
	}

	public Long getCorStatusCd() {
		return corStatusCd;
	}

	public void setCorStatusCd(Long corStatusCd) {
		this.corStatusCd = corStatusCd;
	}

	public Timestamp getCorTs() {
		return corTs;
	}

	public void setCorTs(Timestamp corTs) {
		this.corTs = corTs;
	}

	public Long getCorTypeCd() {
		return corTypeCd;
	}

	public void setCorTypeCd(Long corTypeCd) {
		this.corTypeCd = corTypeCd;
	}

	public ClaimantCmtDAO getClaimantCmtDAO() {
		return claimantCmtDAO;
	}

	public void setClaimantCmtDAO(ClaimantCmtDAO claimantCmtDAO) {
		this.claimantCmtDAO = claimantCmtDAO;
	}

	public ClaimClmDAO getClaimClm() {
		return claimClm;
	}

	public void setClaimClm(ClaimClmDAO claimClm) {
		this.claimClm = claimClm;
	}

	public EmployerEmpDAO getEmployerEmpDAO() {
		return employerEmpDAO;
	}

	public void setEmployerEmpDAO(EmployerEmpDAO employerEmpDAO) {
		this.employerEmpDAO = employerEmpDAO;
	}

	public ReportsRptDAO getReportsRptDAO() {
		return reportsRptDAO;
	}

	public void setReportsRptDAO(ReportsRptDAO reportsRptDAO) {
		this.reportsRptDAO = reportsRptDAO;
	}
}