package com.ssi.ms.fraudreview.database.dao;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ID_THEFT_ACTIONS_ITA")
@Setter
@Getter
public class IdTheftActionsITADAO {
		private static final long serialVersionUID = 7965000665524822494L;

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITA_SEQ_GEN")
		@SequenceGenerator(name = "ITA_SEQ_GEN", sequenceName = "ITA_ID_SEQ", allocationSize = 1)
		@Column(name = "ITA_ID", unique = true)
		private Long itaId;

		@Column(name = "ITA_ACTION_CD")
		private Long itaActionCd;

		@Column(name = "ITA_SOURCE_CD")
		private Long itaSourceCd;

		@Column(name = "FK_STF_ID")
		private Long fkStaffId;

		@Column(name = "FK_CAP_ID")
		private Long fkCapId;

		@Column(name = "FK_EMP_ID")
		private Long fkEmpId;

		@Column(name = "FK_CMT_ID")
		private Long fkCmtId;

		@Column(name = "FK_CMTI_ID")
		private Long fkCmtiId;

		@Column(name = "FK_CLM_ID")
		private Long fkClmId;

		@Column(name = "FK_ITL_ID")
		private Long fkItlId;

		@Column(name = "FK_SRS_ID")
		private Long fkSrsId;

		@Column(name = "FK_WRR_ID")
		private Long fkWrrId;

		@Column(name = "FK_ESR_ID")
		private Long fkEsrId;

		@Column(name = "FK_IRS_ID")
		private Long fkIrsId;

		@Column(name = "ITA_COMM_STR_1")
		private String itaCommStr1;

		@Column(name = "ITA_COMM_STR_2")
		private String itaCommStr2;

		@Column(name = "ITA_COMM_STR_3")
		private String itaCommStr3;

		@Column(name = "ITA_ID_THEFT_TYPE")
		private String itaIdTheftType;

		@Temporal(TemporalType.DATE)
		@Column(name = "ITA_ID_THEFT_DT")
		private Date itaIdTheftDt;

		@Column(name = "ITA_STATUS_IND")
		private String itaStatusInd;

		@Column(name = "ITA_PROCESS_STATUS")
		private String itaProcessStatus;

		@Column(name = "ITA_CREATED_BY")
		private String itaCreatedBy;

		@Column(name = "ITA_CREATED_TS")
		private Timestamp itaCreatedTs;

		@Column(name = "ITA_CREATED_USING")
		private String itaCreatedUsing;

		@Column(name = "ITA_LAST_UPD_BY")
		private String itaLastUpdBy;

		@Column(name = "ITA_LAST_UPD_TS")
		private Timestamp itaLastUpdTs;

		@Column(name = "ITA_LAST_UPD_USING")
		private String itaLastUpdUsing;
}
