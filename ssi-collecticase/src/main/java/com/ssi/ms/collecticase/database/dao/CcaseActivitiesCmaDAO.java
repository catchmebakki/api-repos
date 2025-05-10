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


/**
 * The persistent class for the CCASE_ACTIVITIES_CMA database table.
 *
 */
@Entity
@Table(name="CCASE_ACTIVITIES_CMA")
@Transactional
@NamedStoredProcedureQuery(
		name  =  "createActivity",
		procedureName  =  "pkg_collect.add_ccase_base_activity",
		parameters  =  {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_CMC_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_EMP_ID, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_TYPE_CD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_REMEDY_TYPE_CD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_TIME, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_SPECIFICS, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_NOTES, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_NOTES_ADDL, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_NOTES_NHUIS, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_COMM_METHOD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_CASE_CHARACTERISTICS, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_CMT_REP_CD, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_ACTIVITY_CASE_PRIORITY, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_FOLLOWUP_DT, type = Date.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_FOLLOWUP_SH_NOTE, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_FOLLOWUP_COMP_SH_NOTE, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_USER, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = CollecticaseConstants.PIN_USING, type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_SUCCESS, type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = CollecticaseConstants.POUT_CMA_ID, type = Long.class)
		}
)
@Data
public class CcaseActivitiesCmaDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CMA_SEQ_GEN")
	@SequenceGenerator(name = "CMA_SEQ_GEN", sequenceName = "CCASE_CMA_ID_SEQ", allocationSize = 1)
	@Column(name="CMA_ID")
	private Long cmaId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CMA_ACTIVITY_DT")
	private Date cmaActivityDt;

	@Column(name="CMA_ACTIVITY_NOTES")
	private String cmaActivityNotes;

	@Column(name="CMA_ACTIVITY_NOTES_ADDL")
	private String cmaActivityNotesAddl;

	@Column(name="CMA_ACTIVITY_NOTES_NHUIS")
	private String cmaActivityNotesNhuis;

	@Column(name="CMA_ACTIVITY_SPECIFICS")
	private String cmaActivitySpecifics;

	@Column(name="CMA_ACTIVITY_TIME")
	private String cmaActivityTime;

	@Column(name="CMA_ACTIVITY_TYPE_CD")
	private Long cmaActivityTypeCd;
	
	@Column(name="CMA_REMEDY_TYPE")
	private Long cmaRemedyType;

	@Column(name="CMA_CASE_CHARACTERISTICS")
	private String cmaCaseCharacteristics;

	@Column(name="CMA_CMT_REP_CD")
	private Long cmaCmtRepCd;

	@Column(name="CMA_COMM_METHOD")
	private Long cmaCommMethod;

	@Column(name="CMA_CREATED_BY")
	private String cmaCreatedBy;	

	@Column(name="CMA_CREATED_USING")
	private String cmaCreatedUsing;
	
	@Column(name="CMA_CREATED_TS")
	private Timestamp cmaCreatedTs;

	@Column(name="CMA_EMP_REP_TYPE_CD")
	private Long cmaEmpRepTypeCd;

	@Column(name="CMA_EMP_REP_TYPE_IFK")
	private Long cmaEmpRepTypeIfk;

	@Column(name="CMA_EMP_REPRESENTATIVE")
	private String cmaEmpRepresentative;

	@Column(name="CMA_ENTITY_CONT_TYPE_CD")
	private Long cmaEntityContTypeCd;

	@Column(name="CMA_ENTITY_CONTACT")
	private String cmaEntityContact;

	@Column(name="CMA_ENTITY_CONTTYPE_IFK")
	private Long cmaEntityConttypeIfk;
	
	@Column(name="CMA_NH_FK_CTY_CD")
	private Long cmaNHFkCtyCd;

	@Column(name="CMA_FOLLOW_SH_NOTE")
	private String cmaFollowShNote;
	
	@Column(name="CMA_FUP_COMP_SH_NOTE")
	private String cmaFollowCompShNote;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_FOLLOW_UP_DT")
	private Date cmaFollowUpDt;

	@Column(name="CMA_LAST_UPD_BY")
	private String cmaLastUpdBy;	

	@Column(name="CMA_LAST_UPD_USING")
	private String cmaLastUpdUsing;

	@Column(name="CMA_PP_EFFECTIVE_MONTHS")
	private Long cmaPpEffectiveMonths;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_PP_EFFECTIVE_UNTIL")
	private Date cmaPpEffectiveUntil;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_PP_FA_SIGNED_DT")
	private Date cmaPpFaSignedDt;

	@Column(name="CMA_PP_GUIDELINE_AMT")
	private BigDecimal cmaPpGuidelineAmt;

	@Column(name="CMA_PP_PAYMENT_AMT")
	private BigDecimal cmaPpPaymentAmt;

	@Column(name="CMA_PP_PAYMENT_CATG_CD")
	private Long cmaPpPaymentCatgCd;

	@Column(name="CMA_PP_RESP_TO_CD")
	private Long cmaPpRespToCd;

	@Column(name="CMA_PP_RESP_TO_OTHER")
	private String cmaPpRespToOther;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_PP_SIGNED_DT")
	private Date cmaPpSignedDt;

	@Column(name="CMA_REMEDY_NEXT_STEP_CD")
	private Long cmaRemedyNextStepCd;

	@Column(name="CMA_REMEDY_STAGE_CD")
	private Long cmaRemedyStageCd;

	@Column(name="CMA_REMEDY_STATUS_CD")
	private Long cmaRemedyStatusCd;
	
	@Column(name="CMA_CASE_PRIORITY")
	private Long cmaPriority;	

	@Column(name="CMA_UPD_CONT_IND_ADDR_1")
	private String cmaUpdContIndAddr1;

	@Column(name="CMA_UPD_CONT_IND_ADDR_2")
	private String cmaUpdContIndAddr2;

	@Column(name="CMA_UPD_CONT_IND_CITY")
	private String cmaUpdContIndCity;

	@Column(name="CMA_UPD_CONT_IND_CNTRY")
	private Long cmaUpdContIndCntry;

	@Column(name="CMA_UPD_CONT_IND_EMAILS")
	private String cmaUpdContIndEmails;

	@Column(name="CMA_UPD_CONT_IND_F_NAME")
	private String cmaUpdContIndFName;

	@Column(name="CMA_UPD_CONT_IND_FAX")
	private String cmaUpdContIndFax;

	@Column(name="CMA_UPD_CONT_IND_L_NAME")
	private String cmaUpdContIndLName;

	@Column(name="CMA_UPD_CONT_IND_MI")
	private String cmaUpdContIndMi;

	@Column(name="CMA_UPD_CONT_IND_PREF_CD")
	private Long cmaUpdContIndPrefCd;

	@Column(name="CMA_UPD_CONT_IND_SAL_CD")
	private Long cmaUpdContIndSalCd;

	@Column(name="CMA_UPD_CONT_IND_STATE")
	private String cmaUpdContIndState;

	@Column(name="CMA_UPD_CONT_IND_TELE_C")
	private String cmaUpdContIndTeleC;

	@Column(name="CMA_UPD_CONT_IND_TELE_H")
	private String cmaUpdContIndTeleH;

	@Column(name="CMA_UPD_CONT_IND_TELE_W")
	private String cmaUpdContIndTeleW;

	@Column(name="CMA_UPD_CONT_IND_W_EXT")
	private String cmaUpdContIndWExt;

	@Column(name="CMA_UPD_CONT_IND_ZIP")
	private String cmaUpdContIndZip;

	@Column(name="CMA_UPD_CONT_JOB_TITLE")
	private String cmaUpdContJobTitle;
		
	@Column(name="CMA_UI_ACCT_NBR") 
	private String cmaUIAcctNbr;
	
	@Column(name="CMA_FEIN_NBR")
	private String cmaFeinNbr;

	@Column(name="CMA_UPD_CONT_ORG_ADDR_1")
	private String cmaUpdContOrgAddr1;

	@Column(name="CMA_UPD_CONT_ORG_ADDR_2")
	private String cmaUpdContOrgAddr2;

	@Column(name="CMA_UPD_CONT_ORG_CITY")
	private String cmaUpdContOrgCity;

	@Column(name="CMA_UPD_CONT_ORG_CNTRY")
	private Long cmaUpdContOrgCntry;

	@Column(name="CMA_UPD_CONT_ORG_FAX")
	private String cmaUpdContOrgFax;

	@Column(name="CMA_UPD_CONT_ORG_NAME")
	private String cmaUpdContOrgName;

	@Column(name="CMA_UPD_CONT_ORG_PREF_CD")
	private Long cmaUpdContOrgPrefCd;

	@Column(name="CMA_UPD_CONT_ORG_REMARK")
	private String cmaUpdContOrgRemark;

	@Column(name="CMA_UPD_CONT_ORG_STATE")
	private String cmaUpdContOrgState;

	@Column(name="CMA_UPD_CONT_ORG_TELE")
	private String cmaUpdContOrgTele;

	@Column(name="CMA_UPD_CONT_ORG_WEBSITE")
	private String cmaUpdContOrgWebsite;

	@Column(name="CMA_UPD_CONT_ORG_ZIP")
	private String cmaUpdContOrgZip;

	@Column(name="CMA_UPD_CONT_PRIMARY")
	private String cmaUpdContPrimary;

	@Column(name="CMA_UPD_CONT_REP_FOR")
	private String cmaUpdContRepFor;

	@Column(name="CMA_WG_AMT")
	private BigDecimal cmaWgAmt;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_WG_COURT_ORDER_DT")
	private Date cmaWgCourtOrderDt;

	@Column(name="CMA_WG_COURT_ORDERED")
	private String cmaWgCourtOrdered;

	@Column(name="CMA_WG_DO_NOT_GARNISH")
	private String cmaWgDoNotGarnish;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_WG_EFFECTIVE_FROM")
	private Date cmaWgEffectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_WG_EFFECTIVE_UNTIL")
	private Date cmaWgEffectiveUntil;

	@Column(name="CMA_WG_EMP_NON_COMP_CD")
	private Long cmaWgEmpNonCompCd;

	@Column(name="CMA_WG_FREQ_CD")
	private Long cmaWgFreqCd;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_WG_MOTION_FILED_ON")
	private Date cmaWgMotionFiledOn;
	
	@Column(name="CMA_FOLLOW_UP_COMPLETE")
	private String cmaFollowupComplete;
	
	@Column(name="CMA_FOLLOW_UP_COMPL_BY")
	private String cmaFollowupComplBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CMA_FOLLOW_UP_COMPL_DT")
	private Date cmaFollowupComplDt;

	@Column(name="FK_CMI_ID_UC")
	private Long fkCmiIdUc;

	@Column(name="FK_CMI_ID_WG_EMP")
	private Long fkCmiIdWgEmp;

	@Column(name="FK_CMO_ID_COURT_WG")
	private Long fkCmoIdCourtWg;

	@Column(name="FK_CMO_ID_UC")
	private Long fkCmoIdUc;

	@Column(name="FK_EMP_ID_REP_UC")
	private Long fkEmpIdRepUc;

	@Column(name="FK_EMP_ID_WG")
	private Long fkEmpIdWg;
	
	@Column(name="CMA_UPD_CONT_MAIL_RCPT")
	private String cmaUpdContMailingRcpt;

	//bi-directional many-to-one association to CcaseCasesCmc
	@ManyToOne
	@JoinColumn(name="FK_CMC_ID")
	private CcaseCasesCmcDAO ccaseCasesCmcDAO;

	//bi-directional many-to-one association to CcaseRemedyActivityCra
	@ManyToOne
	@JoinColumn(name="FK_CRA_ID")
	private CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO;

	public Long getCmaId() {
		return cmaId;
	}

	public void setCmaId(Long cmaId) {
		this.cmaId = cmaId;
	}

	public Date getCmaActivityDt() {
		return cmaActivityDt;
	}

	public void setCmaActivityDt(Date cmaActivityDt) {
		this.cmaActivityDt = cmaActivityDt;
	}

	public String getCmaActivityNotes() {
		return cmaActivityNotes;
	}

	public void setCmaActivityNotes(String cmaActivityNotes) {
		this.cmaActivityNotes = cmaActivityNotes;
	}

	public String getCmaActivityNotesAddl() {
		return cmaActivityNotesAddl;
	}

	public void setCmaActivityNotesAddl(String cmaActivityNotesAddl) {
		this.cmaActivityNotesAddl = cmaActivityNotesAddl;
	}

	public String getCmaActivityNotesNhuis() {
		return cmaActivityNotesNhuis;
	}

	public void setCmaActivityNotesNhuis(String cmaActivityNotesNhuis) {
		this.cmaActivityNotesNhuis = cmaActivityNotesNhuis;
	}

	public String getCmaActivitySpecifics() {
		return cmaActivitySpecifics;
	}

	public void setCmaActivitySpecifics(String cmaActivitySpecifics) {
		this.cmaActivitySpecifics = cmaActivitySpecifics;
	}

	public String getCmaActivityTime() {
		return cmaActivityTime;
	}

	public void setCmaActivityTime(String cmaActivityTime) {
		this.cmaActivityTime = cmaActivityTime;
	}

	public Long getCmaActivityTypeCd() {
		return cmaActivityTypeCd;
	}

	public void setCmaActivityTypeCd(Long cmaActivityTypeCd) {
		this.cmaActivityTypeCd = cmaActivityTypeCd;
	}

	public Long getCmaRemedyType() {
		return cmaRemedyType;
	}

	public void setCmaRemedyType(Long cmaRemedyType) {
		this.cmaRemedyType = cmaRemedyType;
	}

	public String getCmaCaseCharacteristics() {
		return cmaCaseCharacteristics;
	}

	public void setCmaCaseCharacteristics(String cmaCaseCharacteristics) {
		this.cmaCaseCharacteristics = cmaCaseCharacteristics;
	}

	public Long getCmaCmtRepCd() {
		return cmaCmtRepCd;
	}

	public void setCmaCmtRepCd(Long cmaCmtRepCd) {
		this.cmaCmtRepCd = cmaCmtRepCd;
	}

	public Long getCmaCommMethod() {
		return cmaCommMethod;
	}

	public void setCmaCommMethod(Long cmaCommMethod) {
		this.cmaCommMethod = cmaCommMethod;
	}

	public String getCmaCreatedBy() {
		return cmaCreatedBy;
	}

	public void setCmaCreatedBy(String cmaCreatedBy) {
		this.cmaCreatedBy = cmaCreatedBy;
	}

	public String getCmaCreatedUsing() {
		return cmaCreatedUsing;
	}

	public void setCmaCreatedUsing(String cmaCreatedUsing) {
		this.cmaCreatedUsing = cmaCreatedUsing;
	}

	public Timestamp getCmaCreatedTs() {
		return cmaCreatedTs;
	}

	public void setCmaCreatedTs(Timestamp cmaCreatedTs) {
		this.cmaCreatedTs = cmaCreatedTs;
	}

	public Long getCmaEmpRepTypeCd() {
		return cmaEmpRepTypeCd;
	}

	public void setCmaEmpRepTypeCd(Long cmaEmpRepTypeCd) {
		this.cmaEmpRepTypeCd = cmaEmpRepTypeCd;
	}

	public Long getCmaEmpRepTypeIfk() {
		return cmaEmpRepTypeIfk;
	}

	public void setCmaEmpRepTypeIfk(Long cmaEmpRepTypeIfk) {
		this.cmaEmpRepTypeIfk = cmaEmpRepTypeIfk;
	}

	public String getCmaEmpRepresentative() {
		return cmaEmpRepresentative;
	}

	public void setCmaEmpRepresentative(String cmaEmpRepresentative) {
		this.cmaEmpRepresentative = cmaEmpRepresentative;
	}

	public Long getCmaEntityContTypeCd() {
		return cmaEntityContTypeCd;
	}

	public void setCmaEntityContTypeCd(Long cmaEntityContTypeCd) {
		this.cmaEntityContTypeCd = cmaEntityContTypeCd;
	}

	public String getCmaEntityContact() {
		return cmaEntityContact;
	}

	public void setCmaEntityContact(String cmaEntityContact) {
		this.cmaEntityContact = cmaEntityContact;
	}

	public Long getCmaEntityConttypeIfk() {
		return cmaEntityConttypeIfk;
	}

	public void setCmaEntityConttypeIfk(Long cmaEntityConttypeIfk) {
		this.cmaEntityConttypeIfk = cmaEntityConttypeIfk;
	}

	public Long getCmaNHFkCtyCd() {
		return cmaNHFkCtyCd;
	}

	public void setCmaNHFkCtyCd(Long cmaNHFkCtyCd) {
		this.cmaNHFkCtyCd = cmaNHFkCtyCd;
	}

	public String getCmaFollowShNote() {
		return cmaFollowShNote;
	}

	public void setCmaFollowShNote(String cmaFollowShNote) {
		this.cmaFollowShNote = cmaFollowShNote;
	}

	public String getCmaFollowCompShNote() {
		return cmaFollowCompShNote;
	}

	public void setCmaFollowCompShNote(String cmaFollowCompShNote) {
		this.cmaFollowCompShNote = cmaFollowCompShNote;
	}

	public Date getCmaFollowUpDt() {
		return cmaFollowUpDt;
	}

	public void setCmaFollowUpDt(Date cmaFollowUpDt) {
		this.cmaFollowUpDt = cmaFollowUpDt;
	}

	public String getCmaLastUpdBy() {
		return cmaLastUpdBy;
	}

	public void setCmaLastUpdBy(String cmaLastUpdBy) {
		this.cmaLastUpdBy = cmaLastUpdBy;
	}

	public String getCmaLastUpdUsing() {
		return cmaLastUpdUsing;
	}

	public void setCmaLastUpdUsing(String cmaLastUpdUsing) {
		this.cmaLastUpdUsing = cmaLastUpdUsing;
	}

	public Long getCmaPpEffectiveMonths() {
		return cmaPpEffectiveMonths;
	}

	public void setCmaPpEffectiveMonths(Long cmaPpEffectiveMonths) {
		this.cmaPpEffectiveMonths = cmaPpEffectiveMonths;
	}

	public Date getCmaPpEffectiveUntil() {
		return cmaPpEffectiveUntil;
	}

	public void setCmaPpEffectiveUntil(Date cmaPpEffectiveUntil) {
		this.cmaPpEffectiveUntil = cmaPpEffectiveUntil;
	}

	public Date getCmaPpFaSignedDt() {
		return cmaPpFaSignedDt;
	}

	public void setCmaPpFaSignedDt(Date cmaPpFaSignedDt) {
		this.cmaPpFaSignedDt = cmaPpFaSignedDt;
	}

	public BigDecimal getCmaPpGuidelineAmt() {
		return cmaPpGuidelineAmt;
	}

	public void setCmaPpGuidelineAmt(BigDecimal cmaPpGuidelineAmt) {
		this.cmaPpGuidelineAmt = cmaPpGuidelineAmt;
	}

	public BigDecimal getCmaPpPaymentAmt() {
		return cmaPpPaymentAmt;
	}

	public void setCmaPpPaymentAmt(BigDecimal cmaPpPaymentAmt) {
		this.cmaPpPaymentAmt = cmaPpPaymentAmt;
	}

	public Long getCmaPpPaymentCatgCd() {
		return cmaPpPaymentCatgCd;
	}

	public void setCmaPpPaymentCatgCd(Long cmaPpPaymentCatgCd) {
		this.cmaPpPaymentCatgCd = cmaPpPaymentCatgCd;
	}

	public Long getCmaPpRespToCd() {
		return cmaPpRespToCd;
	}

	public void setCmaPpRespToCd(Long cmaPpRespToCd) {
		this.cmaPpRespToCd = cmaPpRespToCd;
	}

	public String getCmaPpRespToOther() {
		return cmaPpRespToOther;
	}

	public void setCmaPpRespToOther(String cmaPpRespToOther) {
		this.cmaPpRespToOther = cmaPpRespToOther;
	}

	public Date getCmaPpSignedDt() {
		return cmaPpSignedDt;
	}

	public void setCmaPpSignedDt(Date cmaPpSignedDt) {
		this.cmaPpSignedDt = cmaPpSignedDt;
	}

	public Long getCmaRemedyNextStepCd() {
		return cmaRemedyNextStepCd;
	}

	public void setCmaRemedyNextStepCd(Long cmaRemedyNextStepCd) {
		this.cmaRemedyNextStepCd = cmaRemedyNextStepCd;
	}

	public Long getCmaRemedyStageCd() {
		return cmaRemedyStageCd;
	}

	public void setCmaRemedyStageCd(Long cmaRemedyStageCd) {
		this.cmaRemedyStageCd = cmaRemedyStageCd;
	}

	public Long getCmaRemedyStatusCd() {
		return cmaRemedyStatusCd;
	}

	public void setCmaRemedyStatusCd(Long cmaRemedyStatusCd) {
		this.cmaRemedyStatusCd = cmaRemedyStatusCd;
	}

	public Long getCmaPriority() {
		return cmaPriority;
	}

	public void setCmaPriority(Long cmaPriority) {
		this.cmaPriority = cmaPriority;
	}

	public String getCmaUpdContIndAddr1() {
		return cmaUpdContIndAddr1;
	}

	public void setCmaUpdContIndAddr1(String cmaUpdContIndAddr1) {
		this.cmaUpdContIndAddr1 = cmaUpdContIndAddr1;
	}

	public String getCmaUpdContIndAddr2() {
		return cmaUpdContIndAddr2;
	}

	public void setCmaUpdContIndAddr2(String cmaUpdContIndAddr2) {
		this.cmaUpdContIndAddr2 = cmaUpdContIndAddr2;
	}

	public String getCmaUpdContIndCity() {
		return cmaUpdContIndCity;
	}

	public void setCmaUpdContIndCity(String cmaUpdContIndCity) {
		this.cmaUpdContIndCity = cmaUpdContIndCity;
	}

	public Long getCmaUpdContIndCntry() {
		return cmaUpdContIndCntry;
	}

	public void setCmaUpdContIndCntry(Long cmaUpdContIndCntry) {
		this.cmaUpdContIndCntry = cmaUpdContIndCntry;
	}

	public String getCmaUpdContIndEmails() {
		return cmaUpdContIndEmails;
	}

	public void setCmaUpdContIndEmails(String cmaUpdContIndEmails) {
		this.cmaUpdContIndEmails = cmaUpdContIndEmails;
	}

	public String getCmaUpdContIndFName() {
		return cmaUpdContIndFName;
	}

	public void setCmaUpdContIndFName(String cmaUpdContIndFName) {
		this.cmaUpdContIndFName = cmaUpdContIndFName;
	}

	public String getCmaUpdContIndFax() {
		return cmaUpdContIndFax;
	}

	public void setCmaUpdContIndFax(String cmaUpdContIndFax) {
		this.cmaUpdContIndFax = cmaUpdContIndFax;
	}

	public String getCmaUpdContIndLName() {
		return cmaUpdContIndLName;
	}

	public void setCmaUpdContIndLName(String cmaUpdContIndLName) {
		this.cmaUpdContIndLName = cmaUpdContIndLName;
	}

	public String getCmaUpdContIndMi() {
		return cmaUpdContIndMi;
	}

	public void setCmaUpdContIndMi(String cmaUpdContIndMi) {
		this.cmaUpdContIndMi = cmaUpdContIndMi;
	}

	public Long getCmaUpdContIndPrefCd() {
		return cmaUpdContIndPrefCd;
	}

	public void setCmaUpdContIndPrefCd(Long cmaUpdContIndPrefCd) {
		this.cmaUpdContIndPrefCd = cmaUpdContIndPrefCd;
	}

	public Long getCmaUpdContIndSalCd() {
		return cmaUpdContIndSalCd;
	}

	public void setCmaUpdContIndSalCd(Long cmaUpdContIndSalCd) {
		this.cmaUpdContIndSalCd = cmaUpdContIndSalCd;
	}

	public String getCmaUpdContIndState() {
		return cmaUpdContIndState;
	}

	public void setCmaUpdContIndState(String cmaUpdContIndState) {
		this.cmaUpdContIndState = cmaUpdContIndState;
	}

	public String getCmaUpdContIndTeleC() {
		return cmaUpdContIndTeleC;
	}

	public void setCmaUpdContIndTeleC(String cmaUpdContIndTeleC) {
		this.cmaUpdContIndTeleC = cmaUpdContIndTeleC;
	}

	public String getCmaUpdContIndTeleH() {
		return cmaUpdContIndTeleH;
	}

	public void setCmaUpdContIndTeleH(String cmaUpdContIndTeleH) {
		this.cmaUpdContIndTeleH = cmaUpdContIndTeleH;
	}

	public String getCmaUpdContIndTeleW() {
		return cmaUpdContIndTeleW;
	}

	public void setCmaUpdContIndTeleW(String cmaUpdContIndTeleW) {
		this.cmaUpdContIndTeleW = cmaUpdContIndTeleW;
	}

	public String getCmaUpdContIndWExt() {
		return cmaUpdContIndWExt;
	}

	public void setCmaUpdContIndWExt(String cmaUpdContIndWExt) {
		this.cmaUpdContIndWExt = cmaUpdContIndWExt;
	}

	public String getCmaUpdContIndZip() {
		return cmaUpdContIndZip;
	}

	public void setCmaUpdContIndZip(String cmaUpdContIndZip) {
		this.cmaUpdContIndZip = cmaUpdContIndZip;
	}

	public String getCmaUpdContJobTitle() {
		return cmaUpdContJobTitle;
	}

	public void setCmaUpdContJobTitle(String cmaUpdContJobTitle) {
		this.cmaUpdContJobTitle = cmaUpdContJobTitle;
	}

	public String getCmaUIAcctNbr() {
		return cmaUIAcctNbr;
	}

	public void setCmaUIAcctNbr(String cmaUIAcctNbr) {
		this.cmaUIAcctNbr = cmaUIAcctNbr;
	}

	public String getCmaFeinNbr() {
		return cmaFeinNbr;
	}

	public void setCmaFeinNbr(String cmaFeinNbr) {
		this.cmaFeinNbr = cmaFeinNbr;
	}

	public String getCmaUpdContOrgAddr1() {
		return cmaUpdContOrgAddr1;
	}

	public void setCmaUpdContOrgAddr1(String cmaUpdContOrgAddr1) {
		this.cmaUpdContOrgAddr1 = cmaUpdContOrgAddr1;
	}

	public String getCmaUpdContOrgAddr2() {
		return cmaUpdContOrgAddr2;
	}

	public void setCmaUpdContOrgAddr2(String cmaUpdContOrgAddr2) {
		this.cmaUpdContOrgAddr2 = cmaUpdContOrgAddr2;
	}

	public String getCmaUpdContOrgCity() {
		return cmaUpdContOrgCity;
	}

	public void setCmaUpdContOrgCity(String cmaUpdContOrgCity) {
		this.cmaUpdContOrgCity = cmaUpdContOrgCity;
	}

	public Long getCmaUpdContOrgCntry() {
		return cmaUpdContOrgCntry;
	}

	public void setCmaUpdContOrgCntry(Long cmaUpdContOrgCntry) {
		this.cmaUpdContOrgCntry = cmaUpdContOrgCntry;
	}

	public String getCmaUpdContOrgFax() {
		return cmaUpdContOrgFax;
	}

	public void setCmaUpdContOrgFax(String cmaUpdContOrgFax) {
		this.cmaUpdContOrgFax = cmaUpdContOrgFax;
	}

	public String getCmaUpdContOrgName() {
		return cmaUpdContOrgName;
	}

	public void setCmaUpdContOrgName(String cmaUpdContOrgName) {
		this.cmaUpdContOrgName = cmaUpdContOrgName;
	}

	public Long getCmaUpdContOrgPrefCd() {
		return cmaUpdContOrgPrefCd;
	}

	public void setCmaUpdContOrgPrefCd(Long cmaUpdContOrgPrefCd) {
		this.cmaUpdContOrgPrefCd = cmaUpdContOrgPrefCd;
	}

	public String getCmaUpdContOrgRemark() {
		return cmaUpdContOrgRemark;
	}

	public void setCmaUpdContOrgRemark(String cmaUpdContOrgRemark) {
		this.cmaUpdContOrgRemark = cmaUpdContOrgRemark;
	}

	public String getCmaUpdContOrgState() {
		return cmaUpdContOrgState;
	}

	public void setCmaUpdContOrgState(String cmaUpdContOrgState) {
		this.cmaUpdContOrgState = cmaUpdContOrgState;
	}

	public String getCmaUpdContOrgTele() {
		return cmaUpdContOrgTele;
	}

	public void setCmaUpdContOrgTele(String cmaUpdContOrgTele) {
		this.cmaUpdContOrgTele = cmaUpdContOrgTele;
	}

	public String getCmaUpdContOrgWebsite() {
		return cmaUpdContOrgWebsite;
	}

	public void setCmaUpdContOrgWebsite(String cmaUpdContOrgWebsite) {
		this.cmaUpdContOrgWebsite = cmaUpdContOrgWebsite;
	}

	public String getCmaUpdContOrgZip() {
		return cmaUpdContOrgZip;
	}

	public void setCmaUpdContOrgZip(String cmaUpdContOrgZip) {
		this.cmaUpdContOrgZip = cmaUpdContOrgZip;
	}

	public String getCmaUpdContPrimary() {
		return cmaUpdContPrimary;
	}

	public void setCmaUpdContPrimary(String cmaUpdContPrimary) {
		this.cmaUpdContPrimary = cmaUpdContPrimary;
	}

	public String getCmaUpdContRepFor() {
		return cmaUpdContRepFor;
	}

	public void setCmaUpdContRepFor(String cmaUpdContRepFor) {
		this.cmaUpdContRepFor = cmaUpdContRepFor;
	}

	public BigDecimal getCmaWgAmt() {
		return cmaWgAmt;
	}

	public void setCmaWgAmt(BigDecimal cmaWgAmt) {
		this.cmaWgAmt = cmaWgAmt;
	}

	public Date getCmaWgCourtOrderDt() {
		return cmaWgCourtOrderDt;
	}

	public void setCmaWgCourtOrderDt(Date cmaWgCourtOrderDt) {
		this.cmaWgCourtOrderDt = cmaWgCourtOrderDt;
	}

	public String getCmaWgCourtOrdered() {
		return cmaWgCourtOrdered;
	}

	public void setCmaWgCourtOrdered(String cmaWgCourtOrdered) {
		this.cmaWgCourtOrdered = cmaWgCourtOrdered;
	}

	public String getCmaWgDoNotGarnish() {
		return cmaWgDoNotGarnish;
	}

	public void setCmaWgDoNotGarnish(String cmaWgDoNotGarnish) {
		this.cmaWgDoNotGarnish = cmaWgDoNotGarnish;
	}

	public Date getCmaWgEffectiveFrom() {
		return cmaWgEffectiveFrom;
	}

	public void setCmaWgEffectiveFrom(Date cmaWgEffectiveFrom) {
		this.cmaWgEffectiveFrom = cmaWgEffectiveFrom;
	}

	public Date getCmaWgEffectiveUntil() {
		return cmaWgEffectiveUntil;
	}

	public void setCmaWgEffectiveUntil(Date cmaWgEffectiveUntil) {
		this.cmaWgEffectiveUntil = cmaWgEffectiveUntil;
	}

	public Long getCmaWgEmpNonCompCd() {
		return cmaWgEmpNonCompCd;
	}

	public void setCmaWgEmpNonCompCd(Long cmaWgEmpNonCompCd) {
		this.cmaWgEmpNonCompCd = cmaWgEmpNonCompCd;
	}

	public Long getCmaWgFreqCd() {
		return cmaWgFreqCd;
	}

	public void setCmaWgFreqCd(Long cmaWgFreqCd) {
		this.cmaWgFreqCd = cmaWgFreqCd;
	}

	public Date getCmaWgMotionFiledOn() {
		return cmaWgMotionFiledOn;
	}

	public void setCmaWgMotionFiledOn(Date cmaWgMotionFiledOn) {
		this.cmaWgMotionFiledOn = cmaWgMotionFiledOn;
	}

	public String getCmaFollowupComplete() {
		return cmaFollowupComplete;
	}

	public void setCmaFollowupComplete(String cmaFollowupComplete) {
		this.cmaFollowupComplete = cmaFollowupComplete;
	}

	public String getCmaFollowupComplBy() {
		return cmaFollowupComplBy;
	}

	public void setCmaFollowupComplBy(String cmaFollowupComplBy) {
		this.cmaFollowupComplBy = cmaFollowupComplBy;
	}

	public Date getCmaFollowupComplDt() {
		return cmaFollowupComplDt;
	}

	public void setCmaFollowupComplDt(Date cmaFollowupComplDt) {
		this.cmaFollowupComplDt = cmaFollowupComplDt;
	}

	public Long getFkCmiIdUc() {
		return fkCmiIdUc;
	}

	public void setFkCmiIdUc(Long fkCmiIdUc) {
		this.fkCmiIdUc = fkCmiIdUc;
	}

	public Long getFkCmiIdWgEmp() {
		return fkCmiIdWgEmp;
	}

	public void setFkCmiIdWgEmp(Long fkCmiIdWgEmp) {
		this.fkCmiIdWgEmp = fkCmiIdWgEmp;
	}

	public Long getFkCmoIdCourtWg() {
		return fkCmoIdCourtWg;
	}

	public void setFkCmoIdCourtWg(Long fkCmoIdCourtWg) {
		this.fkCmoIdCourtWg = fkCmoIdCourtWg;
	}

	public Long getFkCmoIdUc() {
		return fkCmoIdUc;
	}

	public void setFkCmoIdUc(Long fkCmoIdUc) {
		this.fkCmoIdUc = fkCmoIdUc;
	}

	public Long getFkEmpIdRepUc() {
		return fkEmpIdRepUc;
	}

	public void setFkEmpIdRepUc(Long fkEmpIdRepUc) {
		this.fkEmpIdRepUc = fkEmpIdRepUc;
	}

	public Long getFkEmpIdWg() {
		return fkEmpIdWg;
	}

	public void setFkEmpIdWg(Long fkEmpIdWg) {
		this.fkEmpIdWg = fkEmpIdWg;
	}

	public String getCmaUpdContMailingRcpt() {
		return cmaUpdContMailingRcpt;
	}

	public void setCmaUpdContMailingRcpt(String cmaUpdContMailingRcpt) {
		this.cmaUpdContMailingRcpt = cmaUpdContMailingRcpt;
	}

	public CcaseCasesCmcDAO getCcaseCasesCmcDAO() {
		return ccaseCasesCmcDAO;
	}

	public void setCcaseCasesCmcDAO(CcaseCasesCmcDAO ccaseCasesCmcDAO) {
		this.ccaseCasesCmcDAO = ccaseCasesCmcDAO;
	}

	public CcaseRemedyActivityCraDAO getCcaseRemedyActivityCraDAO() {
		return ccaseRemedyActivityCraDAO;
	}

	public void setCcaseRemedyActivityCraDAO(CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO) {
		this.ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraDAO;
	}
}