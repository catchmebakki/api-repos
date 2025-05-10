package com.ssi.ms.fraudreview.database.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Table(name = "STAFF_STF")
@Setter
@Getter
public class StaffStfDAO {
	@Id
	@Column(name = "STF_ID", unique = true)
	private Long stfId;

	@Column(name = "STF_APL_LEVEL_CD", precision = 4)
	private BigDecimal stfAplLevelCd;

	@Column(name = "STF_DIFF_LVL_CD", precision = 4)
	private BigDecimal stfDiffLvlCd;

	@Column(name = "STF_EMAIL_ADDRESS", length = 60)
	private String stfEmailAddress;

	@Column(name = "STF_FIRST_NAME", length = 25)
	private String stfFirstName;

	@Column(name = "STF_HOME_AREA_CODE", length = 3)
	private String stfHomeAreaCode;

	@Column(name = "STF_HOME_PHONE", length = 7)
	private String stfHomePhone;

	@Column(name = "STF_JOB_CD", precision = 4)
	private BigDecimal stfJobCd;

	@Column(name = "STF_LAST_NAME", length = 25)
	private String stfLastName;

	@Column(name = "STF_LAST_UPD_TS")
	private Timestamp stfLastUpdTs;

	@Column(name = "STF_MAX_CLMS_NBR", precision = 9)
	private BigDecimal stfMaxClmsNbr;

	@Column(name = "STF_MIDDLE_INITIAL", length = 1)
	private String stfMiddleInitial;

	@Column(name = "STF_SSN", length = 9)
	private String stfSsn;

	@Column(name = "STF_WORK_AREA_CODE", length = 3)
	private String stfWorkAreaCode;

	@Column(name = "STF_WORK_PHONE", length = 7)
	private String stfWorkPhone;

	@Column(name = "STF_WORK_PHONE_EXT", length = 5)
	private String stfWorkPhoneExt;

	@Column(name = "FK_USR_ID", precision = 4)
	private Long fkUserId;

}
