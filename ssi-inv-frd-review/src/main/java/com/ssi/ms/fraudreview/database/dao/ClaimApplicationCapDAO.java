package com.ssi.ms.fraudreview.database.dao;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@NamedStoredProcedureQuery(
        name  =  "callSEARCH_UI",
        procedureName  =  "PKG_RPT_UI_SEARCH.SEARCH_UI",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_QUERY", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_PAGENUMBER", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_PAGESIZE", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_RECORDS", type = Clob.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_RETURN_FLAG", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_RETURN_MESSAGE", type = String.class),
        }
)

@NamedStoredProcedureQuery(
        name  =  "callASSIGN_USERS",
        procedureName  =  "PKG_RPT_UI_SEARCH.ASSIGN_USERS",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_ASSIGN_LIST", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_return_flag", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_return_message", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_UPDATE_RESPONSE", type = String.class),
        }
)

@Table(name = "CLM_APPLN_CAP")
@Setter
@Getter
public class ClaimApplicationCapDAO {
	@Id
	@Column(name = "CAP_ID")
	private Long capId;

	@Column(name = "FK_CMT_ID")
	private Long fkCmtId;
}
