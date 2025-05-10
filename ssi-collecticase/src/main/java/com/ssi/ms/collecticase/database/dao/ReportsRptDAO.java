package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the REPORTS_RPT database table.
 * 
 */
@Entity
@Table(name = "REPORTS_RPT")
@Data
public class ReportsRptDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RPT_ID")
	private Long rptId;

	@Column(name = "RPT_ACTION_DAY_CD")
	private BigDecimal rptActionDayCd;

	@Column(name = "RPT_ACTION_DAY_NBR")
	private BigDecimal rptActionDayNbr;

	@Column(name = "RPT_ADJUD_IND")
	private String rptAdjudInd;

	@Column(name = "RPT_CATEGORY_CD")
	private BigDecimal rptCategoryCd;

	@Column(name = "RPT_CLAIMS_INT_IND")
	private String rptClaimsIntInd;

	@Column(name = "RPT_CSF_RDL_NBR")
	private String rptCsfRdlNbr;

	@Column(name = "RPT_DISPLAY_CD")
	private BigDecimal rptDisplayCd;

	@Column(name = "RPT_DTM_GRP_NBR")
	private BigDecimal rptDtmGrpNbr;

	@Column(name = "RPT_DTM_HEADER_TXT")
	private String rptDtmHeaderTxt;

	@Column(name = "RPT_DTM_TYPE_CD")
	private BigDecimal rptDtmTypeCd;

	@Column(name = "RPT_EXT_FULL_IND")
	private String rptExtFullInd;

	@Column(name = "RPT_EXT_LTD_IND")
	private String rptExtLtdInd;

	@Column(name = "RPT_FINANCE_IND")
	private String rptFinanceInd;

	@Column(name = "RPT_FORM_NBR")
	private String rptFormNbr;

	@Column(name = "RPT_FREQUENCY_CD")
	private BigDecimal rptFrequencyCd;

	@Column(name = "RPT_INIT_INBX_CD")
	private BigDecimal rptInitInbxCd;

	@Column(name = "RPT_INV_AUD_IND")
	private String rptInvAudInd;

	@Temporal(TemporalType.DATE)
	@Column(name = "RPT_LAST_REV_DT")
	private Date rptLastRevDt;

	@Column(name = "RPT_LOCATION")
	private String rptLocation;

	@Column(name = "RPT_NAME")
	private String rptName;

	@Column(name = "RPT_ONE_STOP_IND")
	private String rptOneStopInd;

	@Column(name = "RPT_PURPOSE")
	private String rptPurpose;

	@Column(name = "RPT_RECEIP_CD")
	private BigDecimal rptReceipCd;

	@Column(name = "RPT_RETENTION")
	private BigDecimal rptRetention;

	@Column(name = "RPT_SUPER_USER_IND")
	private String rptSuperUserInd;

	@Column(name = "RPT_SUPPORT_IND")
	private String rptSupportInd;

	@Column(name = "RPT_SYS_ADMIN_IND")
	private String rptSysAdminInd;

	@Column(name = "RPT_TAX_IND")
	private String rptTaxInd;

	@Column(name = "RPT_TYPE_CD")
	private BigDecimal rptTypeCd;

}