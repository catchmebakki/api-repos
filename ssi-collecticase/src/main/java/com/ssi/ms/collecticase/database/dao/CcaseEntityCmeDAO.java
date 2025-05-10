package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.sql.Timestamp;


/**
 * The persistent class for the CCASE_ENTITY_CME database table.
 * 
 */
@Entity
@Table(name="CCASE_ENTITY_CME")
@Data
public class CcaseEntityCmeDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CME_SEQ_GEN")
	@SequenceGenerator(name = "CME_SEQ_GEN", sequenceName = "CCASE_CME_ID_SEQ", allocationSize = 1)
	@Column(name="CME_ID")
	private Long cmeId;

	@Column(name="CME_ACTIVE_IND")
	private String cmeActiveInd;

	@Column(name="CME_CREATED_BY")
	private String cmeCreatedBy;

	@Column(name="CME_CREATED_TS")
	private Timestamp cmeCreatedTs;

	@Column(name="CME_CREATED_USING")
	private String cmeCreatedUsing;

	@Column(name="CME_EMP_REMARKS")
	private String cmeEmpRemarks;

	@Column(name="CME_LAST_UPD_BY")
	private String cmeLastUpdBy;

	@Column(name="CME_LAST_UPD_TS")
	private Timestamp cmeLastUpdTs;

	@Column(name="CME_LAST_UPD_USING")
	private String cmeLastUpdUsing;

	@Column(name="CME_REPRESENTS")
	private String cmeRepresents;

	@Column(name="CME_ROLE")
	private Long cmeRole;

	@Column(name="CME_TYPE")
	private String cmeType;

	//bi-directional many-to-one association to CcaseCasesCmcDAO
	@ManyToOne
	@JoinColumn(name="FK_CMC_ID")
	private CcaseCasesCmcDAO ccaseCasesCmcDAO;

	//bi-directional many-to-one association to CcaseOrganizationCmoDAO
	@ManyToOne
	@JoinColumn(name="FK_CMO_ID")
	private CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO;

	//bi-directional many-to-one association to EmployerEmpDAO
	@ManyToOne
	@JoinColumn(name="FK_EMP_ID")
	private EmployerEmpDAO employerEmpDAO;

	public Long getCmeId() {
		return cmeId;
	}

	public void setCmeId(Long cmeId) {
		this.cmeId = cmeId;
	}

	public String getCmeActiveInd() {
		return cmeActiveInd;
	}

	public void setCmeActiveInd(String cmeActiveInd) {
		this.cmeActiveInd = cmeActiveInd;
	}

	public String getCmeCreatedBy() {
		return cmeCreatedBy;
	}

	public void setCmeCreatedBy(String cmeCreatedBy) {
		this.cmeCreatedBy = cmeCreatedBy;
	}

	public Timestamp getCmeCreatedTs() {
		return cmeCreatedTs;
	}

	public void setCmeCreatedTs(Timestamp cmeCreatedTs) {
		this.cmeCreatedTs = cmeCreatedTs;
	}

	public String getCmeCreatedUsing() {
		return cmeCreatedUsing;
	}

	public void setCmeCreatedUsing(String cmeCreatedUsing) {
		this.cmeCreatedUsing = cmeCreatedUsing;
	}

	public String getCmeEmpRemarks() {
		return cmeEmpRemarks;
	}

	public void setCmeEmpRemarks(String cmeEmpRemarks) {
		this.cmeEmpRemarks = cmeEmpRemarks;
	}

	public String getCmeLastUpdBy() {
		return cmeLastUpdBy;
	}

	public void setCmeLastUpdBy(String cmeLastUpdBy) {
		this.cmeLastUpdBy = cmeLastUpdBy;
	}

	public Timestamp getCmeLastUpdTs() {
		return cmeLastUpdTs;
	}

	public void setCmeLastUpdTs(Timestamp cmeLastUpdTs) {
		this.cmeLastUpdTs = cmeLastUpdTs;
	}

	public String getCmeLastUpdUsing() {
		return cmeLastUpdUsing;
	}

	public void setCmeLastUpdUsing(String cmeLastUpdUsing) {
		this.cmeLastUpdUsing = cmeLastUpdUsing;
	}

	public String getCmeRepresents() {
		return cmeRepresents;
	}

	public void setCmeRepresents(String cmeRepresents) {
		this.cmeRepresents = cmeRepresents;
	}

	public Long getCmeRole() {
		return cmeRole;
	}

	public void setCmeRole(Long cmeRole) {
		this.cmeRole = cmeRole;
	}

	public String getCmeType() {
		return cmeType;
	}

	public void setCmeType(String cmeType) {
		this.cmeType = cmeType;
	}

	public CcaseCasesCmcDAO getCcaseCasesCmcDAO() {
		return ccaseCasesCmcDAO;
	}

	public void setCcaseCasesCmcDAO(CcaseCasesCmcDAO ccaseCasesCmcDAO) {
		this.ccaseCasesCmcDAO = ccaseCasesCmcDAO;
	}

	public CcaseOrganizationCmoDAO getCcaseOrganizationCmoDAO() {
		return ccaseOrganizationCmoDAO;
	}

	public void setCcaseOrganizationCmoDAO(CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO) {
		this.ccaseOrganizationCmoDAO = ccaseOrganizationCmoDAO;
	}

	public EmployerEmpDAO getEmployerEmpDAO() {
		return employerEmpDAO;
	}

	public void setEmployerEmpDAO(EmployerEmpDAO employerEmpDAO) {
		this.employerEmpDAO = employerEmpDAO;
	}
}