package com.ssi.ms.resea.database.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;



@Entity
@Table(name = "CCA_INPROGRESS_CCAI")
@Data
public class CcaInprogressCcaiDAO {

	@Id
	@Column(name = "CCAI_ID")
	private Long ccaiId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CLM_ID")
	private ClaimClmDAO clmDAO;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CCA_ID")
	private CcApplnCcaDAO ccaDAO;

	@Temporal(TemporalType.DATE)
	@Column(name = "CCAI_WEEK_ENDING_DT")
	private Date ccaiWeekEndingDt;

	@Column(name = "CCAI_FILED_TS")
	private Date ccaiFiledTs;

	@Column(name = "CCAI_TYPE_CD")
	private Integer ccaiTypeCd;

	@Column(name = "CCAI_WS_REQ_NOT_MET_ACK_IND")
	private String ccaiWsReqNotMetAckInd;


	


}
