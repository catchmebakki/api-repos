package com.ssi.ms.masslayoff.database.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Praveenraja Paramsivam
 * Entity representing the MSL_ENTRY_CMT_MLEC table in the database.
 */
@Entity
@Table(name = "MSL_ENTRY_CMT_MLEC")
public class MslEntryCmtMlecDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * Primary key representing the MSL_ENTRY_CMT_MLEC table primary ID.
     */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "MLEC_ID")
	private Long mlecId;

	@Column(name = "MLEC_CREATED_BY")
	private String mlecCreatedBy;

	@Column(name = "MLEC_CREATED_TS")
	private Timestamp mlecCreatedTs;

	@Column(name = "MLEC_CREATED_USING")
	private String mlecCreatedUsing;

	@Column(name = "MLEC_LAST_UPD_BY")
	private String mlecLastUpdBy;

	@Column(name = "MLEC_LAST_UPD_TS")
	private Timestamp mlecLastUpdTs;

	@Column(name = "MLEC_LAST_UPD_USING")
	private String mlecLastUpdUsing;

	@Column(name = "MLEC_SOURCE_CD")
	private Integer mlecSourceCd;

	@Column(name = "MLEC_SSN")
	private String mlecSsn;

	@Column(name = "MLEC_FIRST_NAME")
	private String mlecFirstName;

	@Column(name = "MLEC_LAST_NAME")
	private String mlecLastName;

	@Column(name = "MLEC_STATUS_CD")
	private Integer mlecStatusCd;

	// bi-directional many-to-one association to MslRefListMlrl
	@ManyToOne
	@JoinColumn(name = "FK_MLRL_ID")
	private MslRefListMlrlDAO mslRefListMlrlDAO;

	public Long getMlecId() {
		return this.mlecId;
	}

	public void setMlecId(Long mlecId) {
		this.mlecId = mlecId;
	}

	public String getMlecCreatedBy() {
		return this.mlecCreatedBy;
	}

	public void setMlecCreatedBy(String mlecCreatedBy) {
		this.mlecCreatedBy = mlecCreatedBy;
	}

	public Timestamp getMlecCreatedTs() {
		return this.mlecCreatedTs;
	}

	public void setMlecCreatedTs(Timestamp mlecCreatedTs) {
		this.mlecCreatedTs = mlecCreatedTs;
	}

	public String getMlecCreatedUsing() {
		return this.mlecCreatedUsing;
	}

	public void setMlecCreatedUsing(String mlecCreatedUsing) {
		this.mlecCreatedUsing = mlecCreatedUsing;
	}

	public String getMlecLastUpdBy() {
		return this.mlecLastUpdBy;
	}

	public void setMlecLastUpdBy(String mlecLastUpdBy) {
		this.mlecLastUpdBy = mlecLastUpdBy;
	}

	public Timestamp getMlecLastUpdTs() {
		return this.mlecLastUpdTs;
	}

	public void setMlecLastUpdTs(Timestamp mlecLastUpdTs) {
		this.mlecLastUpdTs = mlecLastUpdTs;
	}

	public String getMlecLastUpdUsing() {
		return this.mlecLastUpdUsing;
	}

	public void setMlecLastUpdUsing(String mlecLastUpdUsing) {
		this.mlecLastUpdUsing = mlecLastUpdUsing;
	}

	public Integer getMlecSourceCd() {
		return this.mlecSourceCd;
	}

	public void setMlecSourceCd(Integer mlecSourceCd) {
		this.mlecSourceCd = mlecSourceCd;
	}

	public String getMlecSsn() {
		return this.mlecSsn;
	}

	public void setMlecSsn(String mlecSsn) {
		this.mlecSsn = mlecSsn;
	}

	public Integer getMlecStatusCd() {
		return this.mlecStatusCd;
	}

	public void setMlecStatusCd(Integer mlecStatusCd) {
		this.mlecStatusCd = mlecStatusCd;
	}

	public MslRefListMlrlDAO getMslRefListMlrl() {
		return this.mslRefListMlrlDAO;
	}

	public void setMslRefListMlrl(MslRefListMlrlDAO mslRefListMlrlDAO) {
		this.mslRefListMlrlDAO = mslRefListMlrlDAO;
	}

	public String getMlecFirstName() {
		return mlecFirstName;
	}

	public void setMlecFirstName(String mlecFirstName) {
		this.mlecFirstName = mlecFirstName;
	}

	public String getMlecLastName() {
		return mlecLastName;
	}

	public void setMlecLastName(String mlecLastName) {
		this.mlecLastName = mlecLastName;
	}
}