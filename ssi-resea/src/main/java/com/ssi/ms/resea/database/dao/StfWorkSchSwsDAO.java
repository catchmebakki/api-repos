package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "STF_WORK_SCH_SWS")
@Data
public class StfWorkSchSwsDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SWS_ID", unique = true, nullable = false)
    private Long swsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "SWS_EFFECTIVE_DT")
    private Date swsEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "SWS_EXPIRATION_DT")
    private Date swsExpirationDt;

    @Column(name = "SWS_DAY_OF_WEEK", length = 1)
    private Short swsDayOfWeek;

    @Column(name = "SWS_START_TIME", length = 5)
    private String swsStartTime;

    @Column(name = "SWS_END_TIME", length = 5)
    private String swsEndTime;

    @Column(name = "SWS_WORK_MODE_IND", length = 1)
    private String swsWorkModeInd;

    @Column(name = "SWS_CREATED_BY", length = 10)
    private String swsCreatedBy;

    @Column(name = "SWS_CREATED_USING", length = 50)
    private String swsCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "SWS_CREATED_TS")
    private Date swsCreatedTs;

    @Column(name = "SWS_LAST_UPD_BY", length = 10)
    private String swsLastUpdBy;

    @Column(name = "SWS_LAST_UPD_USING", length = 50)
    private String swsLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "SWS_LAST_UPD_TS")
    private Date swsLastUpdTs;
}
