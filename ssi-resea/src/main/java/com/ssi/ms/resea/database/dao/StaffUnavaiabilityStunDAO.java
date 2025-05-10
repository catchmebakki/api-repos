package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "STAFF_UNAVAILABILITY_STUN")
@Transactional
@NamedStoredProcedureQuery(
        name  =  "applyUnavailibility",
        procedureName  =  "PKG_RESEA.apply_unavailibility",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_boundary_date", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_stun_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_calling_pgm", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_nhl_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_error_msg", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_SUCCESS", type = Long.class)
        }
)
@Data
public class StaffUnavaiabilityStunDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STUN_ID_SEQ_GEN")
    @SequenceGenerator(name = "STUN_ID_SEQ_GEN", sequenceName = "STUN_ID_SEQ", allocationSize = 1)
    @Column(name = "STUN_ID", unique = true, nullable = false, length = 15)
    private Long stunId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID_APPROVER")
    private StaffStfDAO stfApproverDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "STUN_START_DT")
    private Date stunStartDt;

    @Column(name = "STUN_START_TIME")
    private String stunStartTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "STUN_END_DT")
    private Date stunEndDt;

    @Column(name = "STUN_END_TIME")
    private String stunEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUN_REASON_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    AllowValAlvDAO stunReasonTypeAlv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUN_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    AllowValAlvDAO stunStatusAlv;

    @Column(name = "STUN_NOTE", length = 250)
    private String stunNote;

    @Column(name = "STUN_CREATED_BY", length = 10)
    private String stunCreatedBy;

    @Column(name = "STUN_CREATED_USING", length = 50)
    private String stunCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "STUN_CREATED_TS")
    private Date stunCreatedTs;

    @Column(name = "STUN_LAST_UPD_BY", length = 10)
    private String stunLastUpdBy;

    @Column(name = "STUN_LAST_UPD_USING", length = 50)
    private String stunLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "STUN_LAST_UPD_TS")
    private Date stunLastUpdTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_SUNR_ID")
    private StaffUnavailabilityReqSunrDAO sunrDAO;
}
