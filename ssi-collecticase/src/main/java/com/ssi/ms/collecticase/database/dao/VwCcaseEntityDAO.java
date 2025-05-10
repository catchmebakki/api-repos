package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the VW_CCASE_ENTITY database table.
 * 
 */
@Entity
@Table(name="VW_CCASE_ENTITY")
@Data
public class VwCcaseEntityDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name="CME_ACTIVE_IND")
	private String cmeActiveInd;

	@Column(name="CME_ID")
	private Long cmeId;

	@Column(name="CME_ROLE")
	private Long cmeRole;

	@Column(name="CME_TYPE")
	private String cmeType;

	@Column(name="ENTITY_ACTIVE_IND")
	private String entityActiveInd;

	@Id
	@Column(name="ENTITY_ID")
	private Long entityId;

	@Column(name="ENTITY_NAME")
	private String entityName;

	@Column(name="FK_CMC_ID")
	private Long caseId;

	public String getCmeActiveInd() {
		return cmeActiveInd;
	}

	public void setCmeActiveInd(String cmeActiveInd) {
		this.cmeActiveInd = cmeActiveInd;
	}

	public Long getCmeId() {
		return cmeId;
	}

	public void setCmeId(Long cmeId) {
		this.cmeId = cmeId;
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

	public String getEntityActiveInd() {
		return entityActiveInd;
	}

	public void setEntityActiveInd(String entityActiveInd) {
		this.entityActiveInd = entityActiveInd;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
}