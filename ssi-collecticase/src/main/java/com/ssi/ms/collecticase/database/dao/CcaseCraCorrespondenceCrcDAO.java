package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * The persistent class for the CCASE_CRA_CORRESPONDENCE_CRC database table.
 * 
 */
@Entity
@Table(name="CCASE_CRA_CORRESPONDENCE_CRC")
@Data
public class CcaseCraCorrespondenceCrcDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CRC_ID")
	private Long crcId;

	@Column(name="CRC_ACTIVE_IND")
	private String crcActiveInd;

	@Column(name="CRC_AUTO_SET")
	private String crcAutoSet;
	
	@Column(name="CRC_MANUAL")
	private String crcManual;

	@Column(name="CRC_CREATED_BY")
	private String crcCreatedBy;

	@Column(name="CRC_CREATED_TS")
	private Timestamp crcCreatedTs;

	@Column(name="CRC_CREATED_USING")
	private String crcCreatedUsing;
	
	@Column(name="CRC_RPT_NAME")
	private String crcRptName;
	
	@Column(name="CRC_ENABLE")
	private String crcEnable;
	
	@Column(name="CRC_PMT_CATEGORY")
	private Long crcPmtCategory;
	
	@Column(name="CRC_CURR_FILING")
	private String crcCurrFiling;
	
	@Column(name="CRC_DO_NOT_GARNISH")
	private String crcDoNotGarnish;
	
	@Column(name="CRC_COURT_ORDERED")
	private String crcCourtOrdered;
	
	@Column(name="CRC_COUNTY")
	private Long crcCounty;
	
	@Column(name="CRC_LAST_UPD_BY")
	private String crcLastUpdBy;

	@Column(name="CRC_LAST_UPD_TS")
	private Timestamp crcLastUpdTs;

	@Column(name="CRC_LAST_UPD_USING")
	private String crcLastUpdUsing;

	//bi-directional many-to-one association to reportsRptDAO
	@ManyToOne
	@JoinColumn(name="FK_RPT_ID")
	private ReportsRptDAO reportsRptDAO;

	//bi-directional many-to-one association to ccaseRemedyActivityCraDAO
	@ManyToOne
	@JoinColumn(name="FK_CRA_ID")
	private CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO;

	public Long getCrcId() {
		return crcId;
	}

	public void setCrcId(Long crcId) {
		this.crcId = crcId;
	}

	public String getCrcActiveInd() {
		return crcActiveInd;
	}

	public void setCrcActiveInd(String crcActiveInd) {
		this.crcActiveInd = crcActiveInd;
	}

	public String getCrcAutoSet() {
		return crcAutoSet;
	}

	public void setCrcAutoSet(String crcAutoSet) {
		this.crcAutoSet = crcAutoSet;
	}

	public String getCrcManual() {
		return crcManual;
	}

	public void setCrcManual(String crcManual) {
		this.crcManual = crcManual;
	}

	public String getCrcCreatedBy() {
		return crcCreatedBy;
	}

	public void setCrcCreatedBy(String crcCreatedBy) {
		this.crcCreatedBy = crcCreatedBy;
	}

	public Timestamp getCrcCreatedTs() {
		return crcCreatedTs;
	}

	public void setCrcCreatedTs(Timestamp crcCreatedTs) {
		this.crcCreatedTs = crcCreatedTs;
	}

	public String getCrcCreatedUsing() {
		return crcCreatedUsing;
	}

	public void setCrcCreatedUsing(String crcCreatedUsing) {
		this.crcCreatedUsing = crcCreatedUsing;
	}

	public String getCrcRptName() {
		return crcRptName;
	}

	public void setCrcRptName(String crcRptName) {
		this.crcRptName = crcRptName;
	}

	public String getCrcEnable() {
		return crcEnable;
	}

	public void setCrcEnable(String crcEnable) {
		this.crcEnable = crcEnable;
	}

	public Long getCrcPmtCategory() {
		return crcPmtCategory;
	}

	public void setCrcPmtCategory(Long crcPmtCategory) {
		this.crcPmtCategory = crcPmtCategory;
	}

	public String getCrcCurrFiling() {
		return crcCurrFiling;
	}

	public void setCrcCurrFiling(String crcCurrFiling) {
		this.crcCurrFiling = crcCurrFiling;
	}

	public String getCrcDoNotGarnish() {
		return crcDoNotGarnish;
	}

	public void setCrcDoNotGarnish(String crcDoNotGarnish) {
		this.crcDoNotGarnish = crcDoNotGarnish;
	}

	public String getCrcCourtOrdered() {
		return crcCourtOrdered;
	}

	public void setCrcCourtOrdered(String crcCourtOrdered) {
		this.crcCourtOrdered = crcCourtOrdered;
	}

	public Long getCrcCounty() {
		return crcCounty;
	}

	public void setCrcCounty(Long crcCounty) {
		this.crcCounty = crcCounty;
	}

	public String getCrcLastUpdBy() {
		return crcLastUpdBy;
	}

	public void setCrcLastUpdBy(String crcLastUpdBy) {
		this.crcLastUpdBy = crcLastUpdBy;
	}

	public Timestamp getCrcLastUpdTs() {
		return crcLastUpdTs;
	}

	public void setCrcLastUpdTs(Timestamp crcLastUpdTs) {
		this.crcLastUpdTs = crcLastUpdTs;
	}

	public String getCrcLastUpdUsing() {
		return crcLastUpdUsing;
	}

	public void setCrcLastUpdUsing(String crcLastUpdUsing) {
		this.crcLastUpdUsing = crcLastUpdUsing;
	}

	public ReportsRptDAO getReportsRptDAO() {
		return reportsRptDAO;
	}

	public void setReportsRptDAO(ReportsRptDAO reportsRptDAO) {
		this.reportsRptDAO = reportsRptDAO;
	}

	public CcaseRemedyActivityCraDAO getCcaseRemedyActivityCraDAO() {
		return ccaseRemedyActivityCraDAO;
	}

	public void setCcaseRemedyActivityCraDAO(CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO) {
		this.ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraDAO;
	}
}