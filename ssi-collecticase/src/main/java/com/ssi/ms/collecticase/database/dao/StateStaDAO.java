package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the STATE_STA database table.
 * 
 */
@Entity
@Table(name = "STATE_STA")
@Data
public class StateStaDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STA_CD")
	private String staCd;

	@Column(name = "STA_ALL_BP_IND")
	private String staAllBpInd;

	@Column(name = "STA_DESC_TXT")
	private String staDescTxt;

	@Column(name = "STA_EB_IND")
	private String staEbInd;

	@Column(name = "STA_FIPS_CD")
	private BigDecimal staFipsCd;

	@Column(name = "STA_IB1_IND")
	private String staIb1Ind;

	@Column(name = "STA_ICON_IND")
	private String staIconInd;

	@Column(name = "STA_IRORA_IND")
	private String staIroraInd;

	@Column(name = "STA_PROVINCE_IND")
	private String staProvinceInd;

	@Column(name = "STA_SORT_ORDER_NBR")
	private BigDecimal staSortOrderNbr;

	public StateStaDAO() {
	}

	public String getStaCd() {
		return this.staCd;
	}

	public void setStaCd(String staCd) {
		this.staCd = staCd;
	}

	public String getStaAllBpInd() {
		return this.staAllBpInd;
	}

	public void setStaAllBpInd(String staAllBpInd) {
		this.staAllBpInd = staAllBpInd;
	}

	public String getStaDescTxt() {
		return this.staDescTxt;
	}

	public void setStaDescTxt(String staDescTxt) {
		this.staDescTxt = staDescTxt;
	}

	public String getStaEbInd() {
		return this.staEbInd;
	}

	public void setStaEbInd(String staEbInd) {
		this.staEbInd = staEbInd;
	}

	public BigDecimal getStaFipsCd() {
		return this.staFipsCd;
	}

	public void setStaFipsCd(BigDecimal staFipsCd) {
		this.staFipsCd = staFipsCd;
	}

	public String getStaIb1Ind() {
		return this.staIb1Ind;
	}

	public void setStaIb1Ind(String staIb1Ind) {
		this.staIb1Ind = staIb1Ind;
	}

	public String getStaIconInd() {
		return this.staIconInd;
	}

	public void setStaIconInd(String staIconInd) {
		this.staIconInd = staIconInd;
	}

	public String getStaIroraInd() {
		return this.staIroraInd;
	}

	public void setStaIroraInd(String staIroraInd) {
		this.staIroraInd = staIroraInd;
	}

	public String getStaProvinceInd() {
		return this.staProvinceInd;
	}

	public void setStaProvinceInd(String staProvinceInd) {
		this.staProvinceInd = staProvinceInd;
	}

	public BigDecimal getStaSortOrderNbr() {
		return this.staSortOrderNbr;
	}

	public void setStaSortOrderNbr(BigDecimal staSortOrderNbr) {
		this.staSortOrderNbr = staSortOrderNbr;
	}

}