package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CORRSPND_ELMNT_COE database table.
 * 
 */
@Entity
@Table(name="CORRSPND_ELMNT_COE")
@Data
public class CorrspndElmntCoeDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COE_ID")
	private long coeId;

	@Column(name="COE_ELEMENT_TXT")
	private String coeElementTxt;

	//bi-directional many-to-one association to correspondenceCorDAO
	@ManyToOne
	@JoinColumn(name="FK_COR_ID")
	private CorrespondenceCorDAO correspondenceCorDAO;
}