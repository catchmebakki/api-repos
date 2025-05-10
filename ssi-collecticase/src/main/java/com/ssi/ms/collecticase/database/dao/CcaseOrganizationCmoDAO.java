package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.sql.Timestamp;

/**
 * The persistent class for the CCASE_ORGANIZATION_CMO database table.
 * 
 */
@Entity
@Table(name="CCASE_ORGANIZATION_CMO")
@Data
public class CcaseOrganizationCmoDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CMO_SEQ_GEN")
	@SequenceGenerator(name = "CMO_SEQ_GEN", sequenceName = "CCASE_CMO_ID_SEQ", allocationSize = 1)
	@Column(name="CMO_ID")
	private Long cmoId;

	@Column(name="CMO_ADDR_LN1")
	private String cmoAddrLn1;

	@Column(name="CMO_ADDR_LN2")
	private String cmoAddrLn2;

	@Column(name="CMO_CITY")
	private String cmoCity;

	@Column(name="CMO_COMM_PREFERENCE")
	private Long cmoCommPreference;

	@Column(name="CMO_COUNTRY")
	private Long cmoCountry;

	@Column(name="CMO_CREATED_BY")
	private String cmoCreatedBy;

	@Column(name="CMO_CREATED_TS")
	private Timestamp cmoCreatedTs;

	@Column(name="CMO_CREATED_USING")
	private String cmoCreatedUsing;

	@Column(name="CMO_FAX")
	private String cmoFax;

	@Column(name="CMO_LAST_UPD_BY")
	private String cmoLastUpdBy;

	@Column(name="CMO_LAST_UPD_TS")
	private Timestamp cmoLastUpdTs;

	@Column(name="CMO_LAST_UPD_USING")
	private String cmoLastUpdUsing;

	@Column(name="CMO_NAME")
	private String cmoName;

	@Column(name="CMO_REMARKS")
	private String cmoRemarks;

	@Column(name="CMO_STATE")
	private String cmoState;

	@Column(name="CMO_TELE_NUM")
	private String cmoTeleNum;

	@Column(name="CMO_WEBSITE")
	private String cmoWebsite;

	@Column(name="CMO_ZIP_POSTAL_CD")
	private String cmoZipPostalCd;
	
	@Column(name="CMO_UI_ACCT_NBR")
	private String cmoUIAcctNbr;
	
	@Column(name="CMO_FEIN_NBR")
	private String cmoFEINNbr;
	
	//bi-directional many-to-one association to EmployerEmpDAO
	@ManyToOne
	@JoinColumn(name="FK_EMP_ID")
	private EmployerEmpDAO employerEmpDAO;

	public Long getCmoId() {
		return cmoId;
	}

	public void setCmoId(Long cmoId) {
		this.cmoId = cmoId;
	}

	public String getCmoAddrLn1() {
		return cmoAddrLn1;
	}

	public void setCmoAddrLn1(String cmoAddrLn1) {
		this.cmoAddrLn1 = cmoAddrLn1;
	}

	public String getCmoAddrLn2() {
		return cmoAddrLn2;
	}

	public void setCmoAddrLn2(String cmoAddrLn2) {
		this.cmoAddrLn2 = cmoAddrLn2;
	}

	public String getCmoCity() {
		return cmoCity;
	}

	public void setCmoCity(String cmoCity) {
		this.cmoCity = cmoCity;
	}

	public Long getCmoCommPreference() {
		return cmoCommPreference;
	}

	public void setCmoCommPreference(Long cmoCommPreference) {
		this.cmoCommPreference = cmoCommPreference;
	}

	public Long getCmoCountry() {
		return cmoCountry;
	}

	public void setCmoCountry(Long cmoCountry) {
		this.cmoCountry = cmoCountry;
	}

	public String getCmoCreatedBy() {
		return cmoCreatedBy;
	}

	public void setCmoCreatedBy(String cmoCreatedBy) {
		this.cmoCreatedBy = cmoCreatedBy;
	}

	public Timestamp getCmoCreatedTs() {
		return cmoCreatedTs;
	}

	public void setCmoCreatedTs(Timestamp cmoCreatedTs) {
		this.cmoCreatedTs = cmoCreatedTs;
	}

	public String getCmoCreatedUsing() {
		return cmoCreatedUsing;
	}

	public void setCmoCreatedUsing(String cmoCreatedUsing) {
		this.cmoCreatedUsing = cmoCreatedUsing;
	}

	public String getCmoFax() {
		return cmoFax;
	}

	public void setCmoFax(String cmoFax) {
		this.cmoFax = cmoFax;
	}

	public String getCmoLastUpdBy() {
		return cmoLastUpdBy;
	}

	public void setCmoLastUpdBy(String cmoLastUpdBy) {
		this.cmoLastUpdBy = cmoLastUpdBy;
	}

	public Timestamp getCmoLastUpdTs() {
		return cmoLastUpdTs;
	}

	public void setCmoLastUpdTs(Timestamp cmoLastUpdTs) {
		this.cmoLastUpdTs = cmoLastUpdTs;
	}

	public String getCmoLastUpdUsing() {
		return cmoLastUpdUsing;
	}

	public void setCmoLastUpdUsing(String cmoLastUpdUsing) {
		this.cmoLastUpdUsing = cmoLastUpdUsing;
	}

	public String getCmoName() {
		return cmoName;
	}

	public void setCmoName(String cmoName) {
		this.cmoName = cmoName;
	}

	public String getCmoRemarks() {
		return cmoRemarks;
	}

	public void setCmoRemarks(String cmoRemarks) {
		this.cmoRemarks = cmoRemarks;
	}

	public String getCmoState() {
		return cmoState;
	}

	public void setCmoState(String cmoState) {
		this.cmoState = cmoState;
	}

	public String getCmoTeleNum() {
		return cmoTeleNum;
	}

	public void setCmoTeleNum(String cmoTeleNum) {
		this.cmoTeleNum = cmoTeleNum;
	}

	public String getCmoWebsite() {
		return cmoWebsite;
	}

	public void setCmoWebsite(String cmoWebsite) {
		this.cmoWebsite = cmoWebsite;
	}

	public String getCmoZipPostalCd() {
		return cmoZipPostalCd;
	}

	public void setCmoZipPostalCd(String cmoZipPostalCd) {
		this.cmoZipPostalCd = cmoZipPostalCd;
	}

	public String getCmoUIAcctNbr() {
		return cmoUIAcctNbr;
	}

	public void setCmoUIAcctNbr(String cmoUIAcctNbr) {
		this.cmoUIAcctNbr = cmoUIAcctNbr;
	}

	public String getCmoFEINNbr() {
		return cmoFEINNbr;
	}

	public void setCmoFEINNbr(String cmoFEINNbr) {
		this.cmoFEINNbr = cmoFEINNbr;
	}

	public EmployerEmpDAO getEmployerEmpDAO() {
		return employerEmpDAO;
	}

	public void setEmployerEmpDAO(EmployerEmpDAO employerEmpDAO) {
		this.employerEmpDAO = employerEmpDAO;
	}
}