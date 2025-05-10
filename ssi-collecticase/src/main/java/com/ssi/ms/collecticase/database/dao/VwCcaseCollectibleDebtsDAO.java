package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the VW_CCASE_COLLECTIBLE_DEBTS database table.
 * 
 */
@Entity
@Table(name="VW_CCASE_COLLECTIBLE_DEBTS")
@Data
public class VwCcaseCollectibleDebtsDAO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to ClaimantCmtDAO
	@ManyToOne
	@JoinColumn(name="FK_CMT_ID")
	private ClaimantCmtDAO claimantCmtDAO;

	@Id
	@Column(name="OPM_ID")
	private String opmId;

	@Column(name="OPM_BAL_AMT")
	private BigDecimal opmBalAmt;

	@Column(name="OPM_FRD_BAL_AMT")
	private BigDecimal opmFrdBalAmt;

	@Column(name="OPM_NF_BAL_AMT")
	private BigDecimal opmNfBalAmt;

	@Column(name="OPM_INT_BAL_AMT")
	private BigDecimal opmIntBalAmt;

	@Temporal(TemporalType.DATE)
	@Column(name="OPM_DEMAND_LTR_DT")
	private Date opmDemandLtrDt;
}