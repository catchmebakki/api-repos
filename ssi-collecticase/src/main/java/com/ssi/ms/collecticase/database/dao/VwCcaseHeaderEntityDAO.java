package com.ssi.ms.collecticase.database.dao;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

/**
 * The persistent class for the VW_CCASE_HEADER_ENTITY database table.
 * 
 */
@Entity
@Table(name="VW_CCASE_HEADER_ENTITY_TBL")
@Data
public class VwCcaseHeaderEntityDAO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name="CASE_ID")
	private Long caseId;
	
	@Id
	@Column(name="CME_ID")
	private Long cmeId;
	
	@Column(name="CME_ROLE")
	private String cmeRole;

	@Column(name="ENTITY_CONTACT_PHONES")
	private String entityContactPhones;
	
	@Column(name="ENTITY_CONTACT_FAX")
	private String entityContactFax;
	

	@Column(name="ENTITY_ADDRESS")
	private String entityAddress;

	@Column(name="ENTITY_COMM_PREFERENCE")
	private String entityCommPreference;

	@Column(name="ENTITY_CONTACT")
	private String entityContact;

	@Column(name="ENTITY_CONTACT_TITLE")
	private String entityContactTitle;

	@Column(name="ENTITY_EMAILS")
	private String entityEmails;

	@Column(name="ENTITY_NAME")
	private String entityName;

	@Column(name="ENTITY_PHONES")
	private String entityPhones;
	
	@Column(name="ENTITY_FAX")
	private String entityFax;
	

	@Column(name="ENTITY_WEBSITE")
	private String entityWebsite;
}