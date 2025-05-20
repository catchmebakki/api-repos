package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="GTT_FOR_ORGLOOKUP")
@Data
public class GTTForOrgLookupDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ENTITY_ID")
	private Long entityId;
	
	@Column(name="ENTITY_NAME")
	private String entityName;
	
	@Column(name="ENTITY_DBA_NAME")
	private String entityDBAName;
	
	@Column(name="ENTITY_UI_ACCT_NBR")
	private String entityUIAccNbr;
	
	@Column(name="ENTITY_ORIGIN")
	private String entityOrigin;
	
	@Column(name="ENTITY_SOURCE")
	private String entitySource;
	
	@Column(name="ENTITY_FEIN_NBR")
	private String entityFEINNbr;
	
	@Column(name="ENTITY_TYPE")
	private String entityType;
	
	@Column(name="ENTITY_STATUS")
	private String entityStatus;
	
}