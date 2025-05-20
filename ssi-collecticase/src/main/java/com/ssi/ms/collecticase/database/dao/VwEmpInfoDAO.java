package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;


/**
 * The persistent class for the VW_EMP_INFO database table.
 * 
 */
@Entity
@Table(name="VW_EMP_INFO")
@Data
public class VwEmpInfoDAO implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;

	@Column
	private String addr1;

	@Column
	private String addr2;

	@Column
	private String addr3;

	@Column
	private String altPhone;

	@Column
	private String city;

	@Column
	private Long country;

	@Column
	private String email;

	@Column
	private String empDbaName;

	@Id
	@Column
	private Long empId;

	@Column
	private String empName;

	@Column
	private String ext;

	@Column
	private String fax;

	@Column
	private Long fein;

	@Column
	private String mainPhone;

	@Column
	private String state;

	@Column
	private String uiAcct;

	@Column	private String zip;

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getAddr3() {
		return addr3;
	}

	public void setAddr3(String addr3) {
		this.addr3 = addr3;
	}

	public String getAltPhone() {
		return altPhone;
	}

	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getCountry() {
		return country;
	}

	public void setCountry(Long country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmpDbaName() {
		return empDbaName;
	}

	public void setEmpDbaName(String empDbaName) {
		this.empDbaName = empDbaName;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Long getFein() {
		return fein;
	}

	public void setFein(Long fein) {
		this.fein = fein;
	}

	public String getMainPhone() {
		return mainPhone;
	}

	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUiAcct() {
		return uiAcct;
	}

	public void setUiAcct(String uiAcct) {
		this.uiAcct = uiAcct;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}