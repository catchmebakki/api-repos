package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name="VW_CCASE_COUNTY_CTY_CCY")
@Data
public class VwCcaseCountyCtyDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CTY_ID")
	private Long ctyId;

	@Column(name="CTY_CITY")
	private String ctyCity;

	@Column(name="CTY_FIPS_CD")
	private BigDecimal ctyFipsCd;

	@Column(name="CTY_NAME")
	private String ctyName;

	//bi-directional many-to-one association to State
	@ManyToOne
	@JoinColumn(name="FK_STA_CD")
	private StateStaDAO stateSta;

	@Column(name="FK_WFC_ID")
	private Long workForceWfc;

	public VwCcaseCountyCtyDAO() {
	}

	public Long getCtyId() {
		return this.ctyId;
	}

	public void setCtyId(Long ctyId) {
		this.ctyId = ctyId;
	}

	public String getCtyCity() {
		return this.ctyCity;
	}

	public void setCtyCity(String ctyCity) {
		this.ctyCity = ctyCity;
	}

	public BigDecimal getCtyFipsCd() {
		return this.ctyFipsCd;
	}

	public void setCtyFipsCd(BigDecimal ctyFipsCd) {
		this.ctyFipsCd = ctyFipsCd;
	}

	public String getCtyName() {
		return this.ctyName;
	}

	public void setCtyName(String ctyName) {
		this.ctyName = ctyName;
	}

	public StateStaDAO getStateSta() {
		return this.stateSta;
	}

	public void setStateSta(StateStaDAO stateSta) {
		this.stateSta = stateSta;
	}

	public Long getWorkForceWfc() {
		return workForceWfc;
	}

	public void setWorkForceWfc(Long workForceWfc) {
		this.workForceWfc = workForceWfc;
	}
}