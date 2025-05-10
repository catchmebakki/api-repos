package com.ssi.ms.collecticase.database.dao;


import com.ssi.ms.collecticase.database.dao.ClaimantCmtDAO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the CMT_NOTES_CNO database table.
 * 
 */
@Entity
@Table(name="CMT_NOTES_CNO")
@Data
public class CmtNotesCnoDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CNO_SEQ_GEN")
	@SequenceGenerator(name = "CNO_SEQ_GEN", sequenceName = "CNO_ID_SEQ")
	@Column(name="CNO_ID")
	private long cnoId;

	@Column(name="CNO_COR_STATUS_CD")
	private Long cnoCorStatusCd;

	@Column(name="CNO_ENTERED_BY")
	private String cnoEnteredBy;

	@Column(name="CNO_ENTERED_TS")
	private Timestamp cnoEnteredTs;

	@Column(name="CNO_LAST_UPD_BY")
	private String cnoLastUpdBy;

	@Column(name="CNO_LAST_UPD_TS")
	private Timestamp cnoLastUpdTs;

	@Column(name="CNO_NOTES_TXT")
	private String cnoNotesTxt;

	@Column(name="CNO_SUBJECT_TXT")
	private String cnoSubjectTxt;

	//bi-directional many-to-one association to ClaimantCmtDAO
	@ManyToOne
	@JoinColumn(name="FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

	public long getCnoId() {
		return cnoId;
	}

	public void setCnoId(long cnoId) {
		this.cnoId = cnoId;
	}

	public Long getCnoCorStatusCd() {
		return cnoCorStatusCd;
	}

	public void setCnoCorStatusCd(Long cnoCorStatusCd) {
		this.cnoCorStatusCd = cnoCorStatusCd;
	}

	public String getCnoEnteredBy() {
		return cnoEnteredBy;
	}

	public void setCnoEnteredBy(String cnoEnteredBy) {
		this.cnoEnteredBy = cnoEnteredBy;
	}

	public Timestamp getCnoEnteredTs() {
		return cnoEnteredTs;
	}

	public void setCnoEnteredTs(Timestamp cnoEnteredTs) {
		this.cnoEnteredTs = cnoEnteredTs;
	}

	public String getCnoLastUpdBy() {
		return cnoLastUpdBy;
	}

	public void setCnoLastUpdBy(String cnoLastUpdBy) {
		this.cnoLastUpdBy = cnoLastUpdBy;
	}

	public Timestamp getCnoLastUpdTs() {
		return cnoLastUpdTs;
	}

	public void setCnoLastUpdTs(Timestamp cnoLastUpdTs) {
		this.cnoLastUpdTs = cnoLastUpdTs;
	}

	public String getCnoNotesTxt() {
		return cnoNotesTxt;
	}

	public void setCnoNotesTxt(String cnoNotesTxt) {
		this.cnoNotesTxt = cnoNotesTxt;
	}

	public String getCnoSubjectTxt() {
		return cnoSubjectTxt;
	}

	public void setCnoSubjectTxt(String cnoSubjectTxt) {
		this.cnoSubjectTxt = cnoSubjectTxt;
	}

	public ClaimantCmtDAO getClaimantCmtDAO() {
		return claimantCmtDAO;
	}

	public void setClaimantCmtDAO(ClaimantCmtDAO claimantCmtDAO) {
		this.claimantCmtDAO = claimantCmtDAO;
	}
}