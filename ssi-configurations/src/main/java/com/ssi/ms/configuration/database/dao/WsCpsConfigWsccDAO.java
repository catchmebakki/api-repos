package com.ssi.ms.configuration.database.dao;

import lombok.Data;

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
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "WS_CPS_CONFIG_WSCC")
@Data
public class WsCpsConfigWsccDAO {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "WSCC_ID_SEQ", sequenceName = "WSCC_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "WSCC_ID_SEQ")
    @Column(name = "WSCC_ID", precision = 15, unique = true, nullable = false)
    private Long wsccId;

    @ManyToOne
    @JoinColumn(name = "FK_CPS_ID")
    private ClmProgSpecCpsDAO clmProgSpecCpsDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSCC_EFFECTIVE_DT")
    private Date wsccEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSCC_EXPIRATION_DT")
    private Date wsccExpirationDt;

    @Column(name = "WSCC_MIN_IC_WS_REQ", precision = 2)
    private Long wsccMinIcWsReq;

    @Column(name = "WSCC_MIN_AC_WS_REQ", precision = 2)
    private Long wsccMinAcWsReq;

    @Column(name = "WSCC_MIN_WS_INCR_FREQ", precision = 2)
    private Long wsccMinWsIncrFreq;

    @Column(name = "WSCC_MIN_WS_INCR_VAL", precision = 2)
    private Long wsccMinWsIncrVal;

    @Column(name = "WSCC_WS_LANG_EN", length = 4000)
    private String wsccWsLangEn;

    @Column(name = "WSCC_WS_LANG_ES", length = 4000)
    private String wsccWsLangEs;

    @Column(name = "WSCC_CREATED_BY", length = 10)
    private String wsccCreatedBy;

    @Column(name = "WSCC_CREATED_USING", length = 50)
    private String wsccCreatedUsing;

    @Column(name = "WSCC_CREATED_TS")
    private Timestamp wsccCreatedTs;

    @Column(name = "WSCC_LAST_UPD_BY", length = 10)
    private String wsccLastUpdBy;

    @Column(name = "WSCC_LAST_UPD_USING", length = 50)
    private String wsccLastUpdUsing;

    @Column(name = "WSCC_LAST_UPD_TS")
    private Timestamp wsccLastUpdTs;

    @Column(name = "WSCC_COMMENTS", length = 4000)
    private String wsccComments;
}
