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
@Table(name = "STAFF_UNAVAILABILITY_REQ_SUNR")
@Data
@Transactional
@NamedStoredProcedureQuery(
        name  =  "processUnavailReq",
        procedureName  =  "PKG_RESEA.approve_unavailability",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_sunr_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_stf_id_approver", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_calling_pgm", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_nhl_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_success", type = Long.class)
        }
)
@NamedStoredProcedureQuery(
        name  =  "processWithdrawSunr",
        procedureName  =  "PKG_RESEA.withdraw_unavailability",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_sunr_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_wd_start_dt", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_wd_start_time", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_calling_pgm", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_nhl_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_error_msg", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pout_success", type = Long.class)
        }
)
public class StaffUnavailabilityReqSunrDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SUNR_ID_SEQ_GEN")
    @SequenceGenerator(name = "SUNR_ID_SEQ_GEN", sequenceName = "SUNR_ID_SEQ", allocationSize = 1)
    @Column(name = "SUNR_ID", unique = true, nullable = false, length = 15)
    private Long sunrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID_APPROVER")
    private StaffStfDAO approverStfDAO;

    @Column(name = "SUNR_TYPE_IND")
    private String sunrTypeInd;

    @Temporal(TemporalType.DATE)
    @Column(name = "SUNR_START_DT")
    private Date sunrStartDt;

    @Column(name = "SUNR_START_TIME")
    private String sunrStartTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "SUNR_END_DT")
    private Date sunrEndDt;

    @Column(name = "SUNR_END_TIME")
    private String sunrEndTime;

    @Column(name = "SUNR_MONDAY_IND")
    private String sunrMondayInd;

    @Column(name = "SUNR_TUESDAY_IND")
    private String sunrTuesdayInd;

    @Column(name = "SUNR_WEDNESDAY_IND")
    private String sunrWednesdayInd;

    @Column(name = "SUNR_THURSDAY_IND")
    private String sunrThursdayInd;

    @Column(name = "SUNR_FRIDAY_IND")
    private String sunrFridayInd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUNR_REASON_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    AllowValAlvDAO sunrReasonTypeCdAlv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUNR_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    AllowValAlvDAO sunrStatusCdAlv;

    @Column(name = "SUNR_NOTE", length = 4000)
    private String sunrNote;

    @Column(name = "SUNR_CREATED_BY", length = 10)
    private String sunrCreatedBy;

    @Column(name = "SUNR_CREATED_USING", length = 50)
    private String sunrCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "SUNR_CREATED_TS")
    private Date sunrCreatedTs;

    @Column(name = "SUNR_LAST_UPD_BY", length = 10)
    private String sunrLastUpdBy;

    @Column(name = "SUNR_LAST_UPD_USING", length = 50)
    private String sunrLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "SUNR_LAST_UPD_TS")
    private Date sunrLastUpdTs;
}
