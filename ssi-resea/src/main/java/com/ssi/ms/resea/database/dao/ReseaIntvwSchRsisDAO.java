package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RESEA_INTVW_SCH_RSIS")
@Data
public class ReseaIntvwSchRsisDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSIS_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSIS_ID_SEQ_GEN", sequenceName = "RSIS_ID_SEQ", allocationSize = 1)
    @Column(name = "RSIS_ID", unique = true, nullable = false, length = 15)
    private Long rsisId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_LOF_ID")
    private LocalOfficeLofDAO lofDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIS_EFFECTIVE_DT")
    private Date rsisEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIS_EXPIRATION_DT")
    private Date rsisExpirationDt;

    @Column(name = "RSIS_DAY_OF_WEEK", length = 1)
    private Integer rsisDayOfWeek;

    @Column(name = "RSIS_START_TIME", length = 5)
    private String rsisStartTime;

    @Column(name = "RSIS_END_TIME", length = 5)
    private String rsisEndTime;

    @Column(name = "RSIS_INTVW_TYPE_CD", length = 4)
    private Long rsisIntvwTypeCd;

    @Column(name = "RSIS_ALLOW_ONSITE_IND", length = 1)
    private String rsisAllowOnsiteInd;

    @Column(name = "RSIS_ALLOW_REMOTE_IND", length = 1)
    private String rsisAllowRemoteInd;

    @Column(name = "RSIS_CREATED_BY", length = 10)
    private String rsisCreatedBy;

    @Column(name = "RSIS_CREATED_USING", length = 50)
    private String rsisCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIS_CREATED_TS")
    private Date rsisCreatedTs;

    @Column(name = "RSIS_LAST_UPD_BY", length = 10)
    private String rsisLastUpdBy;

    @Column(name = "RSIS_LAST_UPD_USING", length = 50)
    private String rsisLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIS_LAST_UPD_TS")
    private Date rsisLastUpdTs;
}
