package com.ssi.ms.common.database.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author munirathnam.surepall 
 * The persistent class for the SCR_ROL_SRL database table.
 */
@Entity
@Table(name = "SCR_ROL_SRL")
@Getter
@Setter
public class ScreenRolesDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SRL_ID", nullable = false)
	private Long srlId;

	@Column(name = "FK_SCR_ID", nullable = false)
	private Long screenId;

	@Column(name = "FK_ROL_ID", nullable = false)
	private Integer rollId;
	
	@Column(name = "SRL_ACCESS_CD", nullable = false)
	private Long srlAccessCode;
}
