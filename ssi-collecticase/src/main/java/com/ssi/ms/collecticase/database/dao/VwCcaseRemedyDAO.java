package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name="VW_CCASE_REMEDY")
@Data
public class VwCcaseRemedyDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name="CMC_ID")
	private Long cmcId;

	@Column(name="COLLECTED_AMT")
	private BigDecimal collectedAmt;

	@Column(name="ELIGIBLE_AMT")
	private BigDecimal eligibleAmt;

	@Column(name="NEXT_STEP_DESC")
	private String nextStepDesc;

	@Id
	@Column(name="REMEDY_DESC")
	private String remedyDesc;

	@Temporal(TemporalType.DATE)
	@Column(name="REMEDY_DT")
	private Date remedyDt;

	@Column(name="STAGE_DESC")
	private String stageDesc;

	@Column(name="STATUS_DESC")
	private String statusDesc;
}