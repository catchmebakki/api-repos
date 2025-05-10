package com.ssi.ms.configuration.database.dao;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "PARAMETER_PAR")
@Data
public class ParameterParDAO {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PAR_ID_SEQ", sequenceName = "PAR_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PAR_ID_SEQ")
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
    private BigDecimal parNumericValue;
    @Column(name = "PAR_ALPHA_VAL_TXT", length = 100)
    private String parAlphaValText;
    @Column(name = "PAR_LONG_NAME", length = 254)
    private String parLongName;
        @Column(name = "PAR_NAME", length = 150)
    private String parName;

    @ManyToOne
    @JoinColumn(name = "PAR_CATEGORY_CD")
    private AllowValAlvDAO parCategoryCdAlvDAO;

    @Column(name = "PAR_REMARKS", length = 4000)
    private String parRemarks;

    @Column(name = "PAR_CREATED_BY")
    private String parCreatedBy;

    @Column(name = "PAR_CREATED_TS")
    private Timestamp parCreatedTs;

    @Column(name = "PAR_CREATED_USING")
    private String parCreatedUsing;

    @Column(name = "PAR_LAST_UPD_BY")
    private String parLastUpdBy;

    @Column(name = "PAR_LAST_UPD_TS")
    private Timestamp parLastUpdTs;

    @Column(name = "PAR_LAST_UPD_USING")
    private String parLastUpdUsing;

    public String getParName() {
        return StringUtils.trimToNull(parName);
    }
    public String getParAlphaValText() {
        return StringUtils.trimToNull(parAlphaValText);
    }
}
