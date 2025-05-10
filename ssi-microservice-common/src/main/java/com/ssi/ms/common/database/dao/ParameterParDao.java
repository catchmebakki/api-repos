package com.ssi.ms.common.database.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Praveenraja Paramsivam
 * The persistent class for the PARAMETER_PAR database table.
 */
@Entity
@Table(name = "PARAMETER_PAR")
@Getter
@Setter
public class ParameterParDao {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PAR_ID", unique = true, nullable = false)
	private Long parId;
	@Column(name = "PAR_SHORT_NAME", length = 40)
	private String parShortName;
	@Temporal(TemporalType.DATE)
	@Column(name = "PAR_EFFECTIVE_DT")
	private Date parEffectiveDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "PAR_EXPIRATION_DT")
	private Date parExpirationDate;
	@Column(name = "PAR_NUMERIC_VALUE")
	private Long parNumericValue;
	@Column(name = "PAR_ALPHA_VAL_TXT", length = 100)
	private String parAlphaValText;
	@Column(name = "PAR_LONG_NAME", length = 254)
	private String parLongName;
	@Column(name = "PAR_NAME", length = 60)
	private String parName;
	@Column(name = "PAR_CATEGORY_CD")
	private Long parCategoryCD;
}
