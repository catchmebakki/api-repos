package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * The persistent class for the CCASE_CMA_NOTICES_CMN database table.
 * 
 */
@Entity
@Table(name="CCASE_CMA_NOTICES_CMN")
@Data
public class CcaseCmaNoticesCmnDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CMN_SEQ_GEN")
	@SequenceGenerator(name = "CMN_SEQ_GEN", sequenceName = "CCASE_CMN_ID_SEQ", allocationSize = 1)
	@Column(name="CMN_ID")
	private long cmnId;

	@Column(name="CMN_AUTO_REQ_UI")
	private String cmnAutoReqUi;

	@Column(name="CMN_CREATED_BY")
	private String cmnCreatedBy;

	@Column(name="CMN_CREATED_TS")
	private Timestamp cmnCreatedTs;

	@Column(name="CMN_CREATED_USING")
	private String cmnCreatedUsing;

	@Column(name="CMN_LAST_UPD_BY")
	private String cmnLastUpdBy;

	@Column(name="CMN_LAST_UPD_TS")
	private Timestamp cmnLastUpdTs;

	@Column(name="CMN_LAST_UPD_USING")
	private String cmnLastUpdUsing;

	@Column(name="CMN_RESEND_REQ")
	private String cmnResendReq;

	//bi-directional many-to-one association to Correspondence
	@ManyToOne
	@JoinColumn(name="FK_CMA_ID")
	private CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO;
	
	//bi-directional many-to-one association to Correspondence
	@ManyToOne
	@JoinColumn(name="FK_CRC_ID")
	private CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO;

	@Column(name="FK_CWG_ID")
	private Long fkCwgId;

	//bi-directional many-to-one association to Correspondence
	@ManyToOne
	@JoinColumn(name="FK_COR_ID")
	private CorrespondenceCorDAO correspondenceCorDAO;

	public long getCmnId() {
		return cmnId;
	}

	public void setCmnId(long cmnId) {
		this.cmnId = cmnId;
	}

	public String getCmnAutoReqUi() {
		return cmnAutoReqUi;
	}

	public void setCmnAutoReqUi(String cmnAutoReqUi) {
		this.cmnAutoReqUi = cmnAutoReqUi;
	}

	public String getCmnCreatedBy() {
		return cmnCreatedBy;
	}

	public void setCmnCreatedBy(String cmnCreatedBy) {
		this.cmnCreatedBy = cmnCreatedBy;
	}

	public Timestamp getCmnCreatedTs() {
		return cmnCreatedTs;
	}

	public void setCmnCreatedTs(Timestamp cmnCreatedTs) {
		this.cmnCreatedTs = cmnCreatedTs;
	}

	public String getCmnCreatedUsing() {
		return cmnCreatedUsing;
	}

	public void setCmnCreatedUsing(String cmnCreatedUsing) {
		this.cmnCreatedUsing = cmnCreatedUsing;
	}

	public String getCmnLastUpdBy() {
		return cmnLastUpdBy;
	}

	public void setCmnLastUpdBy(String cmnLastUpdBy) {
		this.cmnLastUpdBy = cmnLastUpdBy;
	}

	public Timestamp getCmnLastUpdTs() {
		return cmnLastUpdTs;
	}

	public void setCmnLastUpdTs(Timestamp cmnLastUpdTs) {
		this.cmnLastUpdTs = cmnLastUpdTs;
	}

	public String getCmnLastUpdUsing() {
		return cmnLastUpdUsing;
	}

	public void setCmnLastUpdUsing(String cmnLastUpdUsing) {
		this.cmnLastUpdUsing = cmnLastUpdUsing;
	}

	public String getCmnResendReq() {
		return cmnResendReq;
	}

	public void setCmnResendReq(String cmnResendReq) {
		this.cmnResendReq = cmnResendReq;
	}

	public CcaseActivitiesCmaDAO getCcaseActivitiesCmaDAO() {
		return ccaseActivitiesCmaDAO;
	}

	public void setCcaseActivitiesCmaDAO(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
		this.ccaseActivitiesCmaDAO = ccaseActivitiesCmaDAO;
	}

	public CcaseCraCorrespondenceCrcDAO getCcaseCraCorrespondenceCrcDAO() {
		return ccaseCraCorrespondenceCrcDAO;
	}

	public void setCcaseCraCorrespondenceCrcDAO(CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO) {
		this.ccaseCraCorrespondenceCrcDAO = ccaseCraCorrespondenceCrcDAO;
	}

	public Long getFkCwgId() {
		return fkCwgId;
	}

	public void setFkCwgId(Long fkCwgId) {
		this.fkCwgId = fkCwgId;
	}

	public CorrespondenceCorDAO getCorrespondenceCorDAO() {
		return correspondenceCorDAO;
	}

	public void setCorrespondenceCorDAO(CorrespondenceCorDAO correspondenceCorDAO) {
		this.correspondenceCorDAO = correspondenceCorDAO;
	}
}