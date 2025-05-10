package com.ssi.ms.masslayoff.database.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * @author Praveenraja Paramsivam
 * The persistent class for the EMPLOYER_EMP database table.
 * 
 */
@Entity
@Table(name="EMPLOYER_EMP")
@Getter
@Setter
public class EmployerEmpDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	 /**
     * Primary key representing the Employer's ID.
     */
	@Id
	@Column(name="EMP_ID", unique=true, nullable=false, precision=15)
	private Long empId;

	@Column(name="EMP_BREAKDOWN_IND", length=1)
	private String empBreakdownInd;

	@Column(name="EMP_CATEGORY_CD", nullable=false, precision=4)
	private Integer empCategoryCd;

	@Column(name="EMP_CREATED_BY", length=10)
	private String empCreatedBy;

	@Column(name="EMP_CREATED_TS")
	private Timestamp empCreatedTs;

	@Column(name="EMP_DBA_NAME", length=100)
	private String empDbaName;

	@Column(name="EMP_DELETE_IND", length=1)
	private String empDeleteInd;

	@Column(name="EMP_DELIVERY_CD", nullable=false, precision=4)
	private Integer empDeliveryCd;

	@Column(name="EMP_DISPLAY_CD", precision=4)
	private Integer empDisplayCd;

	@Column(name="EMP_EMAIL_ADDRESS", length=60)
	private String empEmailAddress;

	@Column(name="EMP_FED_DEST_NBR", length=6)
	private String empFedDestNbr;

	@Column(name="EMP_FED_ID_CODE", length=6)
	private String empFedIdCode;

	@Column(name="EMP_FEIN_NBR", precision=9)
	private Long empFeinNbr;

	@Column(name="EMP_GOOD_STANDING_IND", length=1)
	private String empGoodStandingInd;

	@Temporal(TemporalType.DATE)
	@Column(name="EMP_KILLED_DT")
	private Date empKilledDt;

	@Column(name="EMP_LAST_UPD_BY", nullable=false, length=10)
	private String empLastUpdBy;

	@Column(name="EMP_LAST_UPD_TS", nullable=false)
	private Timestamp empLastUpdTs;

	@Temporal(TemporalType.DATE)
	@Column(name="EMP_LIABILITY_DT")
	private Date empLiabilityDt;

	@Column(name="EMP_NAICS_CD", length=7)
	private String empNaicsCd;

	@Column(name="EMP_NAME", nullable=false, length=100)
	private String empName;

	@Column(name="EMP_NEGATIVE_RATED_IND", length=1)
	private String empNegativeRatedInd;

	@Column(name="EMP_REG_RESEND_IND", nullable=false, length=1)
	private String empRegResendInd;

	@Column(name="EMP_REGISTER_NBR", precision=15)
	private Long empRegisterNbr;

	@Column(name="EMP_SOURCE_CD", nullable=false, precision=4)
	private Integer empSourceCd;

	@Column(name="EMP_STATE_CD", length=2)
	private String empStateCd;

	@Column(name="EMP_STD_EMP_NAME_STR", length=250)
	private String empStdEmpNameStr;

	@Column(name="EMP_TAX_STATUS_CD", nullable=false, precision=4)
	private Integer empTaxStatusCd;

	@Column(name="EMP_TAXABLE_IND", length=1)
	private String empTaxableInd;

	@Column(name="EMP_TO_DT_CHG_AMT", nullable=false, precision=13, scale=2)
	private BigDecimal empToDtChgAmt;

	@Temporal(TemporalType.DATE)
	@Column(name="EMP_TPA_REL_DT")
	private Date empTpaRelDt;

	@Column(name="EMP_TPA_REL_IND", length=1)
	private String empTpaRelInd;

	@Column(name="EMP_TYPE_CD", nullable=false, precision=4)
	private Integer empTypeCd;

	@Column(name="EMP_UI_ACCT_LOC", length=3)
	private String empUiAcctLoc;

	@Column(name="EMP_UI_ACCT_NBR", length=10)
	private String empUiAcctNbr;

	@Column(name="EMP_YTD_CHG_AMT", nullable=false, precision=13, scale=2)
	private BigDecimal empYtdChgAmt;

	//bi-directional many-to-one association to EmployerEmp
	@ManyToOne
	@JoinColumn(name="FK_TPA_EMP_ID")
	private EmployerEmpDAO employerEmp1;

	//bi-directional many-to-one association to EmployerEmp
	@OneToMany(mappedBy= "employerEmp1")
	private List<EmployerEmpDAO> employerEmps1;

	//bi-directional many-to-one association to EmployerEmp
	@ManyToOne
	@JoinColumn(name="FK_PLANT_EMP_ID")
	private EmployerEmpDAO employerEmp2;

	//bi-directional many-to-one association to EmployerEmp
	@OneToMany(mappedBy= "employerEmp2")
	private List<EmployerEmpDAO> employerEmps2;

	public EmployerEmpDAO() {
	}

	public EmployerEmpDAO(Long empId, String empName, String empUiAcctLoc){
		this.empId = empId;
		this.empName = empName;
		this.empUiAcctLoc = empUiAcctLoc;
	}

/*	public long getEmpId() {
		return this.empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getEmpBreakdownInd() {
		return this.empBreakdownInd;
	}

	public void setEmpBreakdownInd(String empBreakdownInd) {
		this.empBreakdownInd = empBreakdownInd;
	}

	public BigDecimal getEmpCategoryCd() {
		return this.empCategoryCd;
	}

	public void setEmpCategoryCd(BigDecimal empCategoryCd) {
		this.empCategoryCd = empCategoryCd;
	}

	public String getEmpCreatedBy() {
		return this.empCreatedBy;
	}

	public void setEmpCreatedBy(String empCreatedBy) {
		this.empCreatedBy = empCreatedBy;
	}

	public Timestamp getEmpCreatedTs() {
		return this.empCreatedTs;
	}

	public void setEmpCreatedTs(Timestamp empCreatedTs) {
		this.empCreatedTs = empCreatedTs;
	}

	public String getEmpDbaName() {
		return this.empDbaName;
	}

	public void setEmpDbaName(String empDbaName) {
		this.empDbaName = empDbaName;
	}

	public String getEmpDeleteInd() {
		return this.empDeleteInd;
	}

	public void setEmpDeleteInd(String empDeleteInd) {
		this.empDeleteInd = empDeleteInd;
	}

	public BigDecimal getEmpDeliveryCd() {
		return this.empDeliveryCd;
	}

	public void setEmpDeliveryCd(BigDecimal empDeliveryCd) {
		this.empDeliveryCd = empDeliveryCd;
	}

	public BigDecimal getEmpDisplayCd() {
		return this.empDisplayCd;
	}

	public void setEmpDisplayCd(BigDecimal empDisplayCd) {
		this.empDisplayCd = empDisplayCd;
	}

	public String getEmpEmailAddress() {
		return this.empEmailAddress;
	}

	public void setEmpEmailAddress(String empEmailAddress) {
		this.empEmailAddress = empEmailAddress;
	}

	public String getEmpFedDestNbr() {
		return this.empFedDestNbr;
	}

	public void setEmpFedDestNbr(String empFedDestNbr) {
		this.empFedDestNbr = empFedDestNbr;
	}

	public String getEmpFedIdCode() {
		return this.empFedIdCode;
	}

	public void setEmpFedIdCode(String empFedIdCode) {
		this.empFedIdCode = empFedIdCode;
	}

	public BigDecimal getEmpFeinNbr() {
		return this.empFeinNbr;
	}

	public void setEmpFeinNbr(BigDecimal empFeinNbr) {
		this.empFeinNbr = empFeinNbr;
	}

	public String getEmpGoodStandingInd() {
		return this.empGoodStandingInd;
	}

	public void setEmpGoodStandingInd(String empGoodStandingInd) {
		this.empGoodStandingInd = empGoodStandingInd;
	}

	public Date getEmpKilledDt() {
		return this.empKilledDt;
	}

	public void setEmpKilledDt(Date empKilledDt) {
		this.empKilledDt = empKilledDt;
	}

	public String getEmpLastUpdBy() {
		return this.empLastUpdBy;
	}

	public void setEmpLastUpdBy(String empLastUpdBy) {
		this.empLastUpdBy = empLastUpdBy;
	}

	public Timestamp getEmpLastUpdTs() {
		return this.empLastUpdTs;
	}

	public void setEmpLastUpdTs(Timestamp empLastUpdTs) {
		this.empLastUpdTs = empLastUpdTs;
	}

	public Date getEmpLiabilityDt() {
		return this.empLiabilityDt;
	}

	public void setEmpLiabilityDt(Date empLiabilityDt) {
		this.empLiabilityDt = empLiabilityDt;
	}

	public String getEmpNaicsCd() {
		return this.empNaicsCd;
	}

	public void setEmpNaicsCd(String empNaicsCd) {
		this.empNaicsCd = empNaicsCd;
	}

	public String getEmpName() {
		return this.empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpNegativeRatedInd() {
		return this.empNegativeRatedInd;
	}

	public void setEmpNegativeRatedInd(String empNegativeRatedInd) {
		this.empNegativeRatedInd = empNegativeRatedInd;
	}

	public String getEmpRegResendInd() {
		return this.empRegResendInd;
	}

	public void setEmpRegResendInd(String empRegResendInd) {
		this.empRegResendInd = empRegResendInd;
	}

	public BigDecimal getEmpRegisterNbr() {
		return this.empRegisterNbr;
	}

	public void setEmpRegisterNbr(BigDecimal empRegisterNbr) {
		this.empRegisterNbr = empRegisterNbr;
	}

	public BigDecimal getEmpSourceCd() {
		return this.empSourceCd;
	}

	public void setEmpSourceCd(BigDecimal empSourceCd) {
		this.empSourceCd = empSourceCd;
	}

	public String getEmpStateCd() {
		return this.empStateCd;
	}

	public void setEmpStateCd(String empStateCd) {
		this.empStateCd = empStateCd;
	}

	public String getEmpStdEmpNameStr() {
		return this.empStdEmpNameStr;
	}

	public void setEmpStdEmpNameStr(String empStdEmpNameStr) {
		this.empStdEmpNameStr = empStdEmpNameStr;
	}

	public BigDecimal getEmpTaxStatusCd() {
		return this.empTaxStatusCd;
	}

	public void setEmpTaxStatusCd(BigDecimal empTaxStatusCd) {
		this.empTaxStatusCd = empTaxStatusCd;
	}

	public String getEmpTaxableInd() {
		return this.empTaxableInd;
	}

	public void setEmpTaxableInd(String empTaxableInd) {
		this.empTaxableInd = empTaxableInd;
	}

	public BigDecimal getEmpToDtChgAmt() {
		return this.empToDtChgAmt;
	}

	public void setEmpToDtChgAmt(BigDecimal empToDtChgAmt) {
		this.empToDtChgAmt = empToDtChgAmt;
	}

	public Date getEmpTpaRelDt() {
		return this.empTpaRelDt;
	}

	public void setEmpTpaRelDt(Date empTpaRelDt) {
		this.empTpaRelDt = empTpaRelDt;
	}

	public String getEmpTpaRelInd() {
		return this.empTpaRelInd;
	}

	public void setEmpTpaRelInd(String empTpaRelInd) {
		this.empTpaRelInd = empTpaRelInd;
	}

	public BigDecimal getEmpTypeCd() {
		return this.empTypeCd;
	}

	public void setEmpTypeCd(BigDecimal empTypeCd) {
		this.empTypeCd = empTypeCd;
	}

	public String getEmpUiAcctLoc() {
		return this.empUiAcctLoc;
	}

	public void setEmpUiAcctLoc(String empUiAcctLoc) {
		this.empUiAcctLoc = empUiAcctLoc;
	}

	public String getEmpUiAcctNbr() {
		return this.empUiAcctNbr;
	}

	public void setEmpUiAcctNbr(String empUiAcctNbr) {
		this.empUiAcctNbr = empUiAcctNbr;
	}

	public String getEmpUiAcctNbr1() {
		return this.empUiAcctNbr1;
	}

	public void setEmpUiAcctNbr1(String empUiAcctNbr1) {
		this.empUiAcctNbr1 = empUiAcctNbr1;
	}

	public String getEmpUiAcctNbrloc() {
		return this.empUiAcctNbrloc;
	}

	public void setEmpUiAcctNbrloc(String empUiAcctNbrloc) {
		this.empUiAcctNbrloc = empUiAcctNbrloc;
	}

	public BigDecimal getEmpYtdChgAmt() {
		return this.empYtdChgAmt;
	}

	public void setEmpYtdChgAmt(BigDecimal empYtdChgAmt) {
		this.empYtdChgAmt = empYtdChgAmt;
	}

	public model.EmployerEmpDao getEmployerEmp1() {
		return this.employerEmpDao1;
	}

	public void setEmployerEmp1(model.EmployerEmpDao employerEmpDao1) {
		this.employerEmpDao1 = employerEmpDao1;
	}
*/
/*
	public List<model.EmployerEmpDao> getEmployerEmps1() {
		return this.employerEmps1;
	}

	public void setEmployerEmps1(List<model.EmployerEmpDao> employerEmps1Dao) {
		this.employerEmps1 = employerEmps1Dao;
	}
*/

	public EmployerEmpDAO addEmployerEmps1(EmployerEmpDAO employerEmps1Dao) {
		getEmployerEmps1().add(employerEmps1Dao);
		employerEmps1Dao.setEmployerEmp1(this);

		return employerEmps1Dao;
	}

	public EmployerEmpDAO removeEmployerEmps1(EmployerEmpDAO employerEmps1Dao) {
		getEmployerEmps1().remove(employerEmps1Dao);
		employerEmps1Dao.setEmployerEmp1(null);

		return employerEmps1Dao;
	}

	public EmployerEmpDAO getEmployerEmp2() {
		return this.employerEmp2;
	}

	public void setEmployerEmp2(EmployerEmpDAO employerEmpDao2) {
		this.employerEmp2 = employerEmpDao2;
	}

	public List<EmployerEmpDAO> getEmployerEmps2() {
		return this.employerEmps2;
	}

	public void setEmployerEmps2(List<EmployerEmpDAO> employerEmps2Dao) {
		this.employerEmps2 = employerEmps2Dao;
	}

	public EmployerEmpDAO addEmployerEmps2(EmployerEmpDAO employerEmps2Dao) {
		getEmployerEmps2().add(employerEmps2Dao);
		employerEmps2Dao.setEmployerEmp2(this);

		return employerEmps2Dao;
	}

	public EmployerEmpDAO removeEmployerEmps2(EmployerEmpDAO employerEmps2Dao) {
		getEmployerEmps2().remove(employerEmps2Dao);
		employerEmps2Dao.setEmployerEmp2(null);

		return employerEmps2Dao;
	}

}