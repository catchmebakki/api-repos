package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the CCASE_CME_INDIVIDUAL_CMI database table.
 * 
 */
@Entity
@Table(name="CCASE_CME_INDIVIDUAL_CMI")
@Data
public class CcaseCmeIndividualCmiDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CMI_ID")
	private Long cmiId;

	@Column(name="CMI_ACTIVE_IND")
	private String cmiActiveInd;

	@Column(name="CMI_ADDR_LN1")
	private String cmiAddrLn1;

	@Column(name="CMI_ADDR_LN2")
	private String cmiAddrLn2;

	@Column(name="CMI_CITY")
	private String cmiCity;

	@Column(name="CMI_COMM_PREFERENCE")
	private Long cmiCommPreference;

	@Column(name="CMI_COUNTRY")
	private Long cmiCountry;

	@Column(name="CMI_CREATED_BY")
	private String cmiCreatedBy;

	@Column(name="CMI_CREATED_TS")
	private Timestamp cmiCreatedTs;

	@Column(name="CMI_CREATED_USING")
	private String cmiCreatedUsing;

	@Column(name="CMI_EMAILS")
	private String cmiEmails;

	@Column(name="CMI_FAX")
	private String cmiFax;

	@Column(name="CMI_FIRST_NAME")
	private String cmiFirstName;

	@Column(name="CMI_IS_MAILING_RCPT")
	private String cmiIsMailingRcpt;

	@Column(name="CMI_IS_PRIMARY")
	private String cmiIsPrimary;

	@Column(name="CMI_JOB_TITLE")
	private String cmiJobTitle;

	@Column(name="CMI_LAST_NAME")
	private String cmiLastName;

	@Column(name="CMI_LAST_UPD_BY")
	private String cmiLastUpdBy;

	@Column(name="CMI_LAST_UPD_TS")
	private Timestamp cmiLastUpdTs;

	@Column(name="CMI_LAST_UPD_USING")
	private String cmiLastUpdUsing;

	@Column(name="CMI_MIDDLE_INIT")
	private String cmiMiddleInit;

	@Column(name="CMI_SALUTATION_CD")
	private Long cmiSalutationCd;

	@Column(name="CMI_STATE")
	private String cmiState;

	@Column(name="CMI_TELE_CELL")
	private String cmiTeleCell;

	@Column(name="CMI_TELE_HOME")
	private String cmiTeleHome;

	@Column(name="CMI_TELE_WORK")
	private String cmiTeleWork;

	@Column(name="CMI_TELE_WORK_EXT")
	private String cmiTeleWorkExt;

	@Column(name="CMI_ZIP_POSTAL_CD")
	private String cmiZipPostalCd;

	//bi-directional many-to-one association to ccaseEntityCmeDAO
	@ManyToOne
	@JoinColumn(name="FK_CME_ID")
	private CcaseEntityCmeDAO ccaseEntityCmeDAO;

	//bi-directional many-to-one association to ccaseOrganizationCmoDAO
	@ManyToOne
	@JoinColumn(name="FK_CMO_ID")
	private CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO;

	public Long getCmiId() {
		return cmiId;
	}

	public void setCmiId(Long cmiId) {
		this.cmiId = cmiId;
	}

	public String getCmiActiveInd() {
		return cmiActiveInd;
	}

	public void setCmiActiveInd(String cmiActiveInd) {
		this.cmiActiveInd = cmiActiveInd;
	}

	public String getCmiAddrLn1() {
		return cmiAddrLn1;
	}

	public void setCmiAddrLn1(String cmiAddrLn1) {
		this.cmiAddrLn1 = cmiAddrLn1;
	}

	public String getCmiAddrLn2() {
		return cmiAddrLn2;
	}

	public void setCmiAddrLn2(String cmiAddrLn2) {
		this.cmiAddrLn2 = cmiAddrLn2;
	}

	public String getCmiCity() {
		return cmiCity;
	}

	public void setCmiCity(String cmiCity) {
		this.cmiCity = cmiCity;
	}

	public Long getCmiCommPreference() {
		return cmiCommPreference;
	}

	public void setCmiCommPreference(Long cmiCommPreference) {
		this.cmiCommPreference = cmiCommPreference;
	}

	public Long getCmiCountry() {
		return cmiCountry;
	}

	public void setCmiCountry(Long cmiCountry) {
		this.cmiCountry = cmiCountry;
	}

	public String getCmiCreatedBy() {
		return cmiCreatedBy;
	}

	public void setCmiCreatedBy(String cmiCreatedBy) {
		this.cmiCreatedBy = cmiCreatedBy;
	}

	public Timestamp getCmiCreatedTs() {
		return cmiCreatedTs;
	}

	public void setCmiCreatedTs(Timestamp cmiCreatedTs) {
		this.cmiCreatedTs = cmiCreatedTs;
	}

	public String getCmiCreatedUsing() {
		return cmiCreatedUsing;
	}

	public void setCmiCreatedUsing(String cmiCreatedUsing) {
		this.cmiCreatedUsing = cmiCreatedUsing;
	}

	public String getCmiEmails() {
		return cmiEmails;
	}

	public void setCmiEmails(String cmiEmails) {
		this.cmiEmails = cmiEmails;
	}

	public String getCmiFax() {
		return cmiFax;
	}

	public void setCmiFax(String cmiFax) {
		this.cmiFax = cmiFax;
	}

	public String getCmiFirstName() {
		return cmiFirstName;
	}

	public void setCmiFirstName(String cmiFirstName) {
		this.cmiFirstName = cmiFirstName;
	}

	public String getCmiIsMailingRcpt() {
		return cmiIsMailingRcpt;
	}

	public void setCmiIsMailingRcpt(String cmiIsMailingRcpt) {
		this.cmiIsMailingRcpt = cmiIsMailingRcpt;
	}

	public String getCmiIsPrimary() {
		return cmiIsPrimary;
	}

	public void setCmiIsPrimary(String cmiIsPrimary) {
		this.cmiIsPrimary = cmiIsPrimary;
	}

	public String getCmiJobTitle() {
		return cmiJobTitle;
	}

	public void setCmiJobTitle(String cmiJobTitle) {
		this.cmiJobTitle = cmiJobTitle;
	}

	public String getCmiLastName() {
		return cmiLastName;
	}

	public void setCmiLastName(String cmiLastName) {
		this.cmiLastName = cmiLastName;
	}

	public String getCmiLastUpdBy() {
		return cmiLastUpdBy;
	}

	public void setCmiLastUpdBy(String cmiLastUpdBy) {
		this.cmiLastUpdBy = cmiLastUpdBy;
	}

	public Timestamp getCmiLastUpdTs() {
		return cmiLastUpdTs;
	}

	public void setCmiLastUpdTs(Timestamp cmiLastUpdTs) {
		this.cmiLastUpdTs = cmiLastUpdTs;
	}

	public String getCmiLastUpdUsing() {
		return cmiLastUpdUsing;
	}

	public void setCmiLastUpdUsing(String cmiLastUpdUsing) {
		this.cmiLastUpdUsing = cmiLastUpdUsing;
	}

	public String getCmiMiddleInit() {
		return cmiMiddleInit;
	}

	public void setCmiMiddleInit(String cmiMiddleInit) {
		this.cmiMiddleInit = cmiMiddleInit;
	}

	public Long getCmiSalutationCd() {
		return cmiSalutationCd;
	}

	public void setCmiSalutationCd(Long cmiSalutationCd) {
		this.cmiSalutationCd = cmiSalutationCd;
	}

	public String getCmiState() {
		return cmiState;
	}

	public void setCmiState(String cmiState) {
		this.cmiState = cmiState;
	}

	public String getCmiTeleCell() {
		return cmiTeleCell;
	}

	public void setCmiTeleCell(String cmiTeleCell) {
		this.cmiTeleCell = cmiTeleCell;
	}

	public String getCmiTeleHome() {
		return cmiTeleHome;
	}

	public void setCmiTeleHome(String cmiTeleHome) {
		this.cmiTeleHome = cmiTeleHome;
	}

	public String getCmiTeleWork() {
		return cmiTeleWork;
	}

	public void setCmiTeleWork(String cmiTeleWork) {
		this.cmiTeleWork = cmiTeleWork;
	}

	public String getCmiTeleWorkExt() {
		return cmiTeleWorkExt;
	}

	public void setCmiTeleWorkExt(String cmiTeleWorkExt) {
		this.cmiTeleWorkExt = cmiTeleWorkExt;
	}

	public String getCmiZipPostalCd() {
		return cmiZipPostalCd;
	}

	public void setCmiZipPostalCd(String cmiZipPostalCd) {
		this.cmiZipPostalCd = cmiZipPostalCd;
	}

	public CcaseEntityCmeDAO getCcaseEntityCmeDAO() {
		return ccaseEntityCmeDAO;
	}

	public void setCcaseEntityCmeDAO(CcaseEntityCmeDAO ccaseEntityCmeDAO) {
		this.ccaseEntityCmeDAO = ccaseEntityCmeDAO;
	}

	public CcaseOrganizationCmoDAO getCcaseOrganizationCmoDAO() {
		return ccaseOrganizationCmoDAO;
	}

	public void setCcaseOrganizationCmoDAO(CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO) {
		this.ccaseOrganizationCmoDAO = ccaseOrganizationCmoDAO;
	}
}