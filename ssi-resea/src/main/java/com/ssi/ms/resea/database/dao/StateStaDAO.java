package com.ssi.ms.resea.database.dao;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "STATE_STA")
@Data
public class StateStaDAO implements Serializable {

	@Id
	@Column(name = "STA_CD")
	private String staCd;
	@Column(name = "STA_FIPS_CD")
	private Long staFipsCd;
	@Column(name = "STA_DESC_TXT")
	private String staDescTxt;
	@Column(name = "STA_IRORA_IND")
	private String staIroraInd;
	@Column(name = "STA_IB1_IND")
	private String staIb1Ind;
	@Column(name = "STA_SORT_ORDER_NBR")
	private Long staSortOrderNbr;
	@Column(name = "STA_ICON_IND")
	private String staIconInd;
	@Column(name = "STA_ALL_BP_IND")
	private String staAllBpInd;
	@Column(name = "STA_PROVINCE_IND")
	private String staProvinceInd;
	@Column(name = "STA_EB_IND")
	private String staEbInd;
}
