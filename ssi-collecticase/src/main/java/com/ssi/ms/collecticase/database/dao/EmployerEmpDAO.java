package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the EMPLOYER_EMP database table.
 * 
 */
@Entity
@Table(name = "EMPLOYER_EMP")
@Data
public class EmployerEmpDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMP_ID")
	private Long empId;

	@Column(name = "EMP_BREAKDOWN_IND")
	private String empBreakdownInd;

	@Column(name = "EMP_CATEGORY_CD")
	private Long empCategoryCd;

	@Column(name = "EMP_DBA_NAME")
	private String empDbaName;

	@Column(name = "EMP_DELETE_IND")
	private String empDeleteInd;

	@Column(name = "EMP_DELIVERY_CD")
	private Long empDeliveryCd;

	@Column(name = "EMP_DISPLAY_CD")
	private Long empDisplayCd;

	@Column(name = "EMP_EMAIL_ADDRESS")
	private String empEmailAddress;

	@Column(name = "EMP_FED_DEST_NBR")
	private String empFedDestNbr;

	@Column(name = "EMP_FED_ID_CODE")
	private String empFedIdCode;

	@Column(name = "EMP_FEIN_NBR")
	private Long empFeinNbr;

	@Column(name = "EMP_GOOD_STANDING_IND")
	private String empGoodStandingInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "EMP_KILLED_DT")
	private Date empKilledDt;

	@Column(name = "EMP_LAST_UPD_BY")
	private String empLastUpdBy;

	@Column(name = "EMP_LAST_UPD_TS")
	private Timestamp empLastUpdTs;

	@Temporal(TemporalType.DATE)
	@Column(name = "EMP_LIABILITY_DT")
	private Date empLiabilityDt;

	@Column(name = "EMP_NAICS_CD")
	private String empNaicsCd;

	@Column(name = "EMP_NAME")
	private String empName;

	@Column(name = "EMP_NEGATIVE_RATED_IND")
	private String empNegativeRatedInd;

	@Column(name = "EMP_REGISTER_NBR")
	private Long empRegisterNbr;

	@Column(name = "EMP_SOURCE_CD")
	private Long empSourceCd;

	@Column(name = "EMP_STATE_CD")
	private String empStateCd;

	@Column(name = "EMP_TAX_STATUS_CD")
	private Long empTaxStatusCd;

	@Column(name = "EMP_TAXABLE_IND")
	private String empTaxableInd;

	@Column(name = "EMP_TO_DT_CHG_AMT")
	private Long empToDtChgAmt;

	@Temporal(TemporalType.DATE)
	@Column(name = "EMP_TPA_REL_DT")
	private Date empTpaRelDt;

	@Column(name = "EMP_TPA_REL_IND")
	private String empTpaRelInd;

	@Column(name = "EMP_TYPE_CD")
	private Long empTypeCd;

	@Column(name = "EMP_UI_ACCT_LOC")
	private String empUiAcctLoc;

	@Column(name = "EMP_UI_ACCT_NBR")
	private String empUiAcctNbr;

//	@Column(name = "EMP_UI_ACCT_NBR1")
//	private String empUiAcctNbr1;
//
//	@Column(name = "EMP_UI_ACCT_NBRLOC")
//	private String empUiAcctNbrloc;

	@Column(name = "EMP_YTD_CHG_AMT")
	private Long empYtdChgAmt;

	// bi-directional many-to-one association to Employer
	@ManyToOne
	@JoinColumn(name = "FK_TPA_EMP_ID")
	private EmployerEmpDAO employerEmp1;

	// bi-directional many-to-one association to Employer
	@ManyToOne
	@JoinColumn(name = "FK_PLANT_EMP_ID")
	private EmployerEmpDAO employerEmp2;

}