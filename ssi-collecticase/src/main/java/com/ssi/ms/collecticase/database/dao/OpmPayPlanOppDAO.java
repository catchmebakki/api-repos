package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the OPM_PAY_PLAN_OPP database table.
 * 
 */
@Entity
@Table(name="OPM_PAY_PLAN_OPP")
@Data
public class OpmPayPlanOppDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OPP_ID")
	private long oppId;

	@Column(name="OPP_COMMENTS_TXT")
	private String oppCommentsTxt;

	@Column(name="OPP_CREATED_BY")
	private String oppCreatedBy;

	@Column(name="OPP_CREATED_TS")
	private Timestamp oppCreatedTs;

	@Column(name="OPP_CREATED_USING")
	private String oppCreatedUsing;

	@Temporal(TemporalType.DATE)
	@Column(name="OPP_END_DT")
	private Date oppEndDt;

	@Column(name="OPP_LAST_UPD_BY")
	private String oppLastUpdBy;

	@Column(name="OPP_LAST_UPD_TS")
	private Timestamp oppLastUpdTs;

	@Column(name="OPP_LAST_UPD_USING")
	private String oppLastUpdUsing;

	@Column(name="OPP_OFFSET_AMT")
	private BigDecimal oppOffsetAmt;

	@Column(name="OPP_PAYMENT_AMT")
	private BigDecimal oppPaymentAmt;

	@Column(name="OPP_PAYMENT_METHOD")
	private BigDecimal oppPaymentMethod;

	@Temporal(TemporalType.DATE)
	@Column(name="OPP_START_DT")
	private Date oppStartDt;

	@Column(name="OPP_STATUS_CD")
	private BigDecimal oppStatusCd;

	//bi-directional many-to-one association to claimantCmtDAO
	@ManyToOne
	@JoinColumn(name="OPP_FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;
}