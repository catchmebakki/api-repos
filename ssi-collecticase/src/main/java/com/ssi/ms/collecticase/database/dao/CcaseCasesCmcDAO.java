package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="CCASE_CASES_CMC")
@Transactional
@NamedStoredProcedureQueries({
@NamedStoredProcedureQuery(
		name  =  "createCase",
		procedureName  =  "pkg_collect.ccase_create_case",
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CMT, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_STAFF_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_PRIORITY, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_REMEDY_CD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_CD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_USER, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_USING, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_SUCCESS, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_CMC_ID, type = Long.class)
		}),
@NamedStoredProcedureQuery(
		name  =  "getCaseLoadSummary",
		procedureName  =  "pkg_collect.get_caseload_summary",
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_STF_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_NOT_STARTED, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_HIGH_PRIORITY, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_OVERDUE, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_BANKRUPTCY_CASES, type = Long.class)
		}),
@NamedStoredProcedureQuery(
		name  =  "caseLookup",
		procedureName  =  "pkg_collect.case_lookup",
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CASE_NUM, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_SSN, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_LAST_NAME, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_FIRST_NAME, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_OP_TYPE, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_OP_BAL_RANGE_FROM, type = BigDecimal.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_OP_BAL_RANGE_TO, type = BigDecimal.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CASE_PRIORITY, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_NEXT_FOLLOW_UP, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_BKT_STATUS, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ASSIGNED_TO, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_TELE_NUM, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CASE_OPEN, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_REMEDY, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_REMEDY_ST_FROM_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_REMEDY_ST_TO_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CASE_OPEN_FROM_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CASE_OPEN_TO_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_RPM_FROM_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_RPM_TO_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_SQL_STRING, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_SUCCESS, type = Long.class)
		})
})
@Data
public class CcaseCasesCmcDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CMC_SEQ_GEN")
	@SequenceGenerator(name = "CMC_SEQ_GEN", sequenceName = "CCASE_CMC_ID_SEQ", allocationSize = 1)
	@Column(name="CMC_ID")
	private Long cmcId;

	@Column(name="CMC_ASSIGNED_TS")
	private Timestamp cmcAssignedTs;

	@Column(name="CMC_CASE_CHARACTERISTICS")
	private String cmcCaseCharacteristics;

	@Temporal(TemporalType.DATE)
	@Column(name="CMC_CASE_OPEN_DT")
	private Date cmcCaseOpenDt;

	@Column(name="CMC_CASE_PRIORITY")
	private Long cmcCasePriority;

	@Column(name="CMC_CASE_STATUS")
	private Long cmcCaseStatus;

	@Column(name="CMC_CMT_REP_TYPE_CD")
	private Long cmcCmtRepTypeCd;
	
	@Column(name="CMC_CASE_NEW_IND")
	private String cmcCaseNewInd;
	
	@Column(name="CMC_CASE_LAST_REMEDY_CD")
	private Long cmcCaseLastRemedyCd;
	
	@Column(name="CMC_CASE_LAST_ACTIVITY_CD")
	private Long cmcCaseLastActivityCd;
	
	@Column(name="CMC_CASE_ORIG_OPEN_DT")
	private Date cmcCaseOrigOpenDt;	
	
	@Column(name="CMC_ANNUAL_DMD_LTR_DT")
	private Date cmcAnnualDmdLtrDt;	

	@Column(name="CMC_CREATED_BY")
	private String cmcCreatedBy;

	@Column(name="CMC_CREATED_TS")
	private Timestamp cmcCreatedTs;

	@Column(name="CMC_CREATED_USING")
	private String cmcCreatedUsing;

	@Column(name="CMC_LAST_UPD_BY")
	private String cmcLastUpdBy;

	@Column(name="CMC_LAST_UPD_TS")
	private Timestamp cmcLastUpdTs;

	@Column(name="CMC_LAST_UPD_USING")
	private String cmcLastUpdUsing;


	//bi-directional many-to-one association to CcaseCasesCmcDAO
	@ManyToOne
	@JoinColumn(name="FK_CMC_ID")
	private CcaseCasesCmcDAO ccaseCasesCmcDao;

	//bi-directional many-to-one association to CcaseEntityCmeDAO
	@ManyToOne
	@JoinColumn(name="FK_CME_ID_CMT_REP")
	private CcaseEntityCmeDAO ccaseEntityCmeDAO;

	//bi-directional many-to-one association to ClaimantCmtDAO
	@ManyToOne
	@JoinColumn(name="FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

	//bi-directional many-to-one association to StaffStfDAO
	@ManyToOne
	@JoinColumn(name="FK_STF_ID")
	private StaffStfDAO staffStfDAO;

	public Long getCmcId() {
		return cmcId;
	}

	public void setCmcId(Long cmcId) {
		this.cmcId = cmcId;
	}

	public Timestamp getCmcAssignedTs() {
		return cmcAssignedTs;
	}

	public void setCmcAssignedTs(Timestamp cmcAssignedTs) {
		this.cmcAssignedTs = cmcAssignedTs;
	}

	public String getCmcCaseCharacteristics() {
		return cmcCaseCharacteristics;
	}

	public void setCmcCaseCharacteristics(String cmcCaseCharacteristics) {
		this.cmcCaseCharacteristics = cmcCaseCharacteristics;
	}

	public Date getCmcCaseOpenDt() {
		return cmcCaseOpenDt;
	}

	public void setCmcCaseOpenDt(Date cmcCaseOpenDt) {
		this.cmcCaseOpenDt = cmcCaseOpenDt;
	}

	public Long getCmcCasePriority() {
		return cmcCasePriority;
	}

	public void setCmcCasePriority(Long cmcCasePriority) {
		this.cmcCasePriority = cmcCasePriority;
	}

	public Long getCmcCaseStatus() {
		return cmcCaseStatus;
	}

	public void setCmcCaseStatus(Long cmcCaseStatus) {
		this.cmcCaseStatus = cmcCaseStatus;
	}

	public Long getCmcCmtRepTypeCd() {
		return cmcCmtRepTypeCd;
	}

	public void setCmcCmtRepTypeCd(Long cmcCmtRepTypeCd) {
		this.cmcCmtRepTypeCd = cmcCmtRepTypeCd;
	}

	public String getCmcCaseNewInd() {
		return cmcCaseNewInd;
	}

	public void setCmcCaseNewInd(String cmcCaseNewInd) {
		this.cmcCaseNewInd = cmcCaseNewInd;
	}

	public Long getCmcCaseLastRemedyCd() {
		return cmcCaseLastRemedyCd;
	}

	public void setCmcCaseLastRemedyCd(Long cmcCaseLastRemedyCd) {
		this.cmcCaseLastRemedyCd = cmcCaseLastRemedyCd;
	}

	public Long getCmcCaseLastActivityCd() {
		return cmcCaseLastActivityCd;
	}

	public void setCmcCaseLastActivityCd(Long cmcCaseLastActivityCd) {
		this.cmcCaseLastActivityCd = cmcCaseLastActivityCd;
	}

	public Date getCmcCaseOrigOpenDt() {
		return cmcCaseOrigOpenDt;
	}

	public void setCmcCaseOrigOpenDt(Date cmcCaseOrigOpenDt) {
		this.cmcCaseOrigOpenDt = cmcCaseOrigOpenDt;
	}

	public Date getCmcAnnualDmdLtrDt() {
		return cmcAnnualDmdLtrDt;
	}

	public void setCmcAnnualDmdLtrDt(Date cmcAnnualDmdLtrDt) {
		this.cmcAnnualDmdLtrDt = cmcAnnualDmdLtrDt;
	}

	public String getCmcCreatedBy() {
		return cmcCreatedBy;
	}

	public void setCmcCreatedBy(String cmcCreatedBy) {
		this.cmcCreatedBy = cmcCreatedBy;
	}

	public Timestamp getCmcCreatedTs() {
		return cmcCreatedTs;
	}

	public void setCmcCreatedTs(Timestamp cmcCreatedTs) {
		this.cmcCreatedTs = cmcCreatedTs;
	}

	public String getCmcCreatedUsing() {
		return cmcCreatedUsing;
	}

	public void setCmcCreatedUsing(String cmcCreatedUsing) {
		this.cmcCreatedUsing = cmcCreatedUsing;
	}

	public String getCmcLastUpdBy() {
		return cmcLastUpdBy;
	}

	public void setCmcLastUpdBy(String cmcLastUpdBy) {
		this.cmcLastUpdBy = cmcLastUpdBy;
	}

	public Timestamp getCmcLastUpdTs() {
		return cmcLastUpdTs;
	}

	public void setCmcLastUpdTs(Timestamp cmcLastUpdTs) {
		this.cmcLastUpdTs = cmcLastUpdTs;
	}

	public String getCmcLastUpdUsing() {
		return cmcLastUpdUsing;
	}

	public void setCmcLastUpdUsing(String cmcLastUpdUsing) {
		this.cmcLastUpdUsing = cmcLastUpdUsing;
	}

	public CcaseCasesCmcDAO getCcaseCasesCmcDao() {
		return ccaseCasesCmcDao;
	}

	public void setCcaseCasesCmcDao(CcaseCasesCmcDAO ccaseCasesCmcDao) {
		this.ccaseCasesCmcDao = ccaseCasesCmcDao;
	}

	public CcaseEntityCmeDAO getCcaseEntityCmeDAO() {
		return ccaseEntityCmeDAO;
	}

	public void setCcaseEntityCmeDAO(CcaseEntityCmeDAO ccaseEntityCmeDAO) {
		this.ccaseEntityCmeDAO = ccaseEntityCmeDAO;
	}

	public ClaimantCmtDAO getClaimantCmtDAO() {
		return claimantCmtDAO;
	}

	public void setClaimantCmtDAO(ClaimantCmtDAO claimantCmtDAO) {
		this.claimantCmtDAO = claimantCmtDAO;
	}

	public StaffStfDAO getStaffStfDAO() {
		return staffStfDAO;
	}

	public void setStaffStfDAO(StaffStfDAO staffStfDAO) {
		this.staffStfDAO = staffStfDAO;
	}
}