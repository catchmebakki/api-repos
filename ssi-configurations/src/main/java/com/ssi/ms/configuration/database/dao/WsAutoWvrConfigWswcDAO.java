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
@Table(name = "WS_AUTO_WVR_CONFIG_WSWC")
@Data
public class WsAutoWvrConfigWswcDAO {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "WSWC_ID_SEQ", sequenceName = "WSWC_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "WSWC_ID_SEQ")
    @Column(name = "WSWC_ID", unique = true, nullable = false)
    private Long wswcId;

    @ManyToOne
    @JoinColumn(name = "FK_LOF_ID")
    private LocalOfficeLofDAO localOfficeLofDAO;

    @Column(name = "WSWC_SCENARIO_NUM_EVENT_CD", precision = 4)
    private Integer wswcScenarioNumEventCd;

    @Column(name = "WSWC_SCENARIO_DESC", length = 100)
    private String wswcScenarioDesc;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSWC_EFFECTIVE_DT")
    private Date wswcEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSWC_EXPIRATION_DT")
    private Date wswcExpirationDt;

    @ManyToOne
    @JoinColumn(name = "WSWC_REASON_CD")
    private AllowValAlvDAO wswcReasonAlvDAO;

    @Column(name = "WSWC_SYS_OVERWRITE_IND", length = 1)
    private String wswcSysOverwriteInd;

    @Column(name = "WSWC_EVENT", length = 100)
    private String wswcEvent;

    @Column(name = "WSWC_EVENT_SPECIFICS", length = 4000)
    private String wswcEventSpecifics;

    @Column(name = "WSWC_ADDL_REF", length = 4000)
    private String wswcAddlRef;

    @Column(name = "WSWC_SPL_ACTIONS", length = 4000)
    private String wswcSplActions;

    @Column(name = "WSWC_USR_COMMENTS", length = 4000)
    private String wswcUsrComments;

    @Column(name = "WSWC_CREATED_BY", length = 10)
    private String wswcCreatedBy;

    @Column(name = "WSWC_CREATED_USING", length = 50)
    private String wswcCreatedUsing;

    @Column(name = "WSWC_CREATED_TS")
    private Timestamp wswcCreatedTs;

    @Column(name = "WSWC_LAST_UPD_BY", length = 10)
    private String wswcLastUpdBy;

    @Column(name = "WSWC_LAST_UPD_USING", length = 50)
    private String wswcLastUpdUsing;

    @Column(name = "WSWC_LAST_UPD_TS")
    private Timestamp wswcLastUpdTs;
}
