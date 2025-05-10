package com.ssi.ms.collecticase.database.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the CCASE_REMEDY_ACTIVITY_CRA database table.
 * 
 */
@Entity
@Table(name="CCASE_REMEDY_ACTIVITY_CRA")
public class CcaseRemedyActivityCraDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CRA_ID")
	private long craId;

	@Column(name="CRA_ACTIVE_IND")
	private String craActiveInd;

	@Column(name="CRA_ACTIVITY_CD")
	private Long craActivityCd;

	@Column(name="CRA_ACTIVITY_LEVEL")
	private Long craActivityLevel;

	@Column(name="CRA_CREATED_BY")
	private String craCreatedBy;

	@Column(name="CRA_CREATED_TS")
	private Timestamp craCreatedTs;

	@Column(name="CRA_CREATED_USING")
	private String craCreatedUsing;

	@Column(name="CRA_FOLLOW_UP_SH_NOTE")
	private String craFollowUpShNote;
	
	@Column(name="CRA_FOLLOW_UP_DAYS")
	private Long craFollowUpDays;
	
	@Column(name="CRA_AUTO_COMPLETE")
	private Long craAutoComplete;
	
	@Column(name="CRA_AUTO_COMPLETE_SH_NOTE")
	private String craAutoCompleteShNote;
	
	@Column(name="CRA_ACTIVITY_SPECIFICS")
	private String craActivitySpecifics;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CRA_EFFECTIVE_DT")
	private Date craEffectiveDt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CRA_EFF_UNTIL_DT")
	private Date craEffectiveUntilDt;

	@Column(name="CRA_LAST_UPD_BY")
	private String craLastUpdBy;

	@Column(name="CRA_LAST_UPD_TS")
	private Timestamp craLastUpdTs;

	@Column(name="CRA_LAST_UPD_USING")
	private String craLastUpdUsing;

	@Column(name="CRA_NEW_REMEDY_STAGE_CD")
	private Long craNewRemedyStageCd;

	@Column(name="CRA_NEW_REMEDY_STATUS_CD")
	private Long craNewRemedyStatusCd;

	@Column(name="CRA_REMEDY_CD")
	private Long craRemedyCd;

	@Column(name="CRA_REMEDY_NEXT_STEP_CD")
	private Long craRemedyNextStepCd;

	@Column(name="CRA_TEMPLATE_PAGE")
	private String craTemplatePage;

	@Column(name="CRA_USER_INITIATED")
	private String craUserInitiated;
	
	@Column(name="CRA_RESTRICT_ACTIVITY")
	private String craRestrictActivity;
	
	//SAT27237 Start
	@Column(name="CRA_RESTRICT_IF_BILL_DISC")
	private String craRestrictIfBillDisc;
	//SAT27237 End

	@Column(name="FK_CRA_ID_PRE_MUST_1")
	private Long fkCraIdPreMust1;

	@Column(name="FK_CRA_ID_PRE_MUST_2")
	private Long fkCraIdPreMust2;

	@Column(name="FK_CRA_ID_PRE_MUST_3")
	private Long fkCraIdPreMust3;

	@Column(name="FK_CRA_ID_PRE_ONE_OF_1")
	private Long fkCraIdPreOneOf1;

	@Column(name="FK_CRA_ID_PRE_ONE_OF_2")
	private Long fkCraIdPreOneOf2;

	@Column(name="FK_CRA_ID_PRE_ONE_OF_3")
	private Long fkCraIdPreOneOf3;

	public long getCraId() {
		return craId;
	}

	public void setCraId(long craId) {
		this.craId = craId;
	}

	public String getCraActiveInd() {
		return craActiveInd;
	}

	public void setCraActiveInd(String craActiveInd) {
		this.craActiveInd = craActiveInd;
	}

	public Long getCraActivityCd() {
		return craActivityCd;
	}

	public void setCraActivityCd(Long craActivityCd) {
		this.craActivityCd = craActivityCd;
	}

	public Long getCraActivityLevel() {
		return craActivityLevel;
	}

	public void setCraActivityLevel(Long craActivityLevel) {
		this.craActivityLevel = craActivityLevel;
	}

	public String getCraCreatedBy() {
		return craCreatedBy;
	}

	public void setCraCreatedBy(String craCreatedBy) {
		this.craCreatedBy = craCreatedBy;
	}

	public Timestamp getCraCreatedTs() {
		return craCreatedTs;
	}

	public void setCraCreatedTs(Timestamp craCreatedTs) {
		this.craCreatedTs = craCreatedTs;
	}

	public String getCraCreatedUsing() {
		return craCreatedUsing;
	}

	public void setCraCreatedUsing(String craCreatedUsing) {
		this.craCreatedUsing = craCreatedUsing;
	}

	public String getCraFollowUpShNote() {
		return craFollowUpShNote;
	}

	public void setCraFollowUpShNote(String craFollowUpShNote) {
		this.craFollowUpShNote = craFollowUpShNote;
	}

	public Long getCraFollowUpDays() {
		return craFollowUpDays;
	}

	public void setCraFollowUpDays(Long craFollowUpDays) {
		this.craFollowUpDays = craFollowUpDays;
	}

	public Long getCraAutoComplete() {
		return craAutoComplete;
	}

	public void setCraAutoComplete(Long craAutoComplete) {
		this.craAutoComplete = craAutoComplete;
	}

	public String getCraAutoCompleteShNote() {
		return craAutoCompleteShNote;
	}

	public void setCraAutoCompleteShNote(String craAutoCompleteShNote) {
		this.craAutoCompleteShNote = craAutoCompleteShNote;
	}

	public String getCraActivitySpecifics() {
		return craActivitySpecifics;
	}

	public void setCraActivitySpecifics(String craActivitySpecifics) {
		this.craActivitySpecifics = craActivitySpecifics;
	}

	public Date getCraEffectiveDt() {
		return craEffectiveDt;
	}

	public void setCraEffectiveDt(Date craEffectiveDt) {
		this.craEffectiveDt = craEffectiveDt;
	}

	public Date getCraEffectiveUntilDt() {
		return craEffectiveUntilDt;
	}

	public void setCraEffectiveUntilDt(Date craEffectiveUntilDt) {
		this.craEffectiveUntilDt = craEffectiveUntilDt;
	}

	public String getCraLastUpdBy() {
		return craLastUpdBy;
	}

	public void setCraLastUpdBy(String craLastUpdBy) {
		this.craLastUpdBy = craLastUpdBy;
	}

	public Timestamp getCraLastUpdTs() {
		return craLastUpdTs;
	}

	public void setCraLastUpdTs(Timestamp craLastUpdTs) {
		this.craLastUpdTs = craLastUpdTs;
	}

	public String getCraLastUpdUsing() {
		return craLastUpdUsing;
	}

	public void setCraLastUpdUsing(String craLastUpdUsing) {
		this.craLastUpdUsing = craLastUpdUsing;
	}

	public Long getCraNewRemedyStageCd() {
		return craNewRemedyStageCd;
	}

	public void setCraNewRemedyStageCd(Long craNewRemedyStageCd) {
		this.craNewRemedyStageCd = craNewRemedyStageCd;
	}

	public Long getCraNewRemedyStatusCd() {
		return craNewRemedyStatusCd;
	}

	public void setCraNewRemedyStatusCd(Long craNewRemedyStatusCd) {
		this.craNewRemedyStatusCd = craNewRemedyStatusCd;
	}

	public Long getCraRemedyCd() {
		return craRemedyCd;
	}

	public void setCraRemedyCd(Long craRemedyCd) {
		this.craRemedyCd = craRemedyCd;
	}

	public Long getCraRemedyNextStepCd() {
		return craRemedyNextStepCd;
	}

	public void setCraRemedyNextStepCd(Long craRemedyNextStepCd) {
		this.craRemedyNextStepCd = craRemedyNextStepCd;
	}

	public String getCraTemplatePage() {
		return craTemplatePage;
	}

	public void setCraTemplatePage(String craTemplatePage) {
		this.craTemplatePage = craTemplatePage;
	}

	public String getCraUserInitiated() {
		return craUserInitiated;
	}

	public void setCraUserInitiated(String craUserInitiated) {
		this.craUserInitiated = craUserInitiated;
	}

	public String getCraRestrictActivity() {
		return craRestrictActivity;
	}

	public void setCraRestrictActivity(String craRestrictActivity) {
		this.craRestrictActivity = craRestrictActivity;
	}

	public String getCraRestrictIfBillDisc() {
		return craRestrictIfBillDisc;
	}

	public void setCraRestrictIfBillDisc(String craRestrictIfBillDisc) {
		this.craRestrictIfBillDisc = craRestrictIfBillDisc;
	}

	public Long getFkCraIdPreMust1() {
		return fkCraIdPreMust1;
	}

	public void setFkCraIdPreMust1(Long fkCraIdPreMust1) {
		this.fkCraIdPreMust1 = fkCraIdPreMust1;
	}

	public Long getFkCraIdPreMust2() {
		return fkCraIdPreMust2;
	}

	public void setFkCraIdPreMust2(Long fkCraIdPreMust2) {
		this.fkCraIdPreMust2 = fkCraIdPreMust2;
	}

	public Long getFkCraIdPreMust3() {
		return fkCraIdPreMust3;
	}

	public void setFkCraIdPreMust3(Long fkCraIdPreMust3) {
		this.fkCraIdPreMust3 = fkCraIdPreMust3;
	}

	public Long getFkCraIdPreOneOf1() {
		return fkCraIdPreOneOf1;
	}

	public void setFkCraIdPreOneOf1(Long fkCraIdPreOneOf1) {
		this.fkCraIdPreOneOf1 = fkCraIdPreOneOf1;
	}

	public Long getFkCraIdPreOneOf2() {
		return fkCraIdPreOneOf2;
	}

	public void setFkCraIdPreOneOf2(Long fkCraIdPreOneOf2) {
		this.fkCraIdPreOneOf2 = fkCraIdPreOneOf2;
	}

	public Long getFkCraIdPreOneOf3() {
		return fkCraIdPreOneOf3;
	}

	public void setFkCraIdPreOneOf3(Long fkCraIdPreOneOf3) {
		this.fkCraIdPreOneOf3 = fkCraIdPreOneOf3;
	}
}