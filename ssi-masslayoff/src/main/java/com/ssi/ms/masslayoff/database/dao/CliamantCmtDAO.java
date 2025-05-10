package com.ssi.ms.masslayoff.database.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Praveenraja Paramsivam
 * Entity representing the CLAIMANT_CMT table in the database.
 */
@Entity
@Table(name = "CLAIMANT_CMT")
@Setter
@Getter
public class CliamantCmtDAO {
	 /**
     * Primary key representing the claimant's ID.
     */
	@Id
	@Column(name = "CMT_ID")
	private Long cmtId;
	
	@Column(name = "CMT_SSN")
	private String ssn;
	
	@Column(name = "CMT_FIRST_NAME")
	private String firstName;
	
	@Column(name = "CMT_LAST_NAME")
	private String lastName;

}
