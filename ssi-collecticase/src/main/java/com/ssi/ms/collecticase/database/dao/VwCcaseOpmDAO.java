package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the VW_CCASE_OPM database table.
 * 
 */
@Entity
@Table(name="VW_CCASE_OPM_TBL")
@Data
public class VwCcaseOpmDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date bopdate;

	private String bopstatusdesc;

	@Temporal(TemporalType.DATE)
	@Column(name="DTM_MAILED_DT")
	private Date dtmMailedDt;

	@Id
	@Column(name="DTM_NBR")
	private Long dtmNbr;
	
	@Column(name="FK_CMT_ID")
	private Long fkCmtId;

	@Column(name="OPM_BAL")
	private BigDecimal opmBal;
	
	@Column(name="FRAUD_IND")
	private String fraudInd;

	@Column(name="TOTAL_AMT")
	private BigDecimal totalAmt;

	private Long versionnbr;
}