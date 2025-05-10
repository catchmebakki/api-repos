package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the CLM_LOF_CLF database table.
 * 
 */
@Entity
@Table(name="CLM_LOF_CLF")
@Data
public class ClmLofClfDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CLF_ID")
	private long clfId;

	@Column(name="CLF_ACTIVE_IND")
	private String clfActiveInd;

	@Column(name="CLF_ASSIGNED_TS")
	private Timestamp clfAssignedTs;

	@Column(name="FK_CLM_ID")
	private Long fkClmId;

	@Column(name="FK_LOF_ID")
	private Long fkLofId;
}