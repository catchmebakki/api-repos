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
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RESEA_CASE_RSCS")
@Transactional
@NamedStoredProcedureQuery(
        name  =  "reassignAll",
        procedureName  =  "PKG_RESEA.REASSIGN_ALL",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_STF_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_LOF_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_LOF_CHOICE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_REASSIGN_EFFECTIVE_DATE", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_REASSIGN_END_DATE", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_REASSIGN_REASON_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_REQUESTING_STF", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_STAFF_NOTE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_EXECUTION_RESULT", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_EXECUTION_MSG", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_NHL_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_ERROR_MSG", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_SUCCESS", type = Long.class)
        }
)
@Data
public class ReseaCaseRscsDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSCS_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSCS_ID_SEQ_GEN", sequenceName = "RSCS_ID_SEQ", allocationSize = 1)
    @Column(name = "RSCS_ID", unique = true, nullable = false, length = 15)
    private Long rscsId;

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "rscsDAO")
    private List<ReseaCaseActivityRscaDAO> rscaDAOList;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO clmDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @Digits(integer = 12, fraction = 10)
    @Column(name = "RSCS_SCORE")
    private BigDecimal rscsScore;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_SCORED_DT")
    private Date rscsScoredDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_POOLED_DT")
    private Date rscsPooledDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_SELECTED_DT")
    private Date rscsSelectedDt;

    @Column(name = "RSCS_NUM_SCH_ATTMPTS", length = 2)
    private Short rscsNumSchAttmpts;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_ORIENTATION_DT")
    private Date rscsOrientationDt;

    @Column(name = "RSCS_NUM_ORNTN_RESCHS", length = 2)
    private Short rscsNumOrntnReschs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCS_STAGE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscsStageCdALV;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCS_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscsStatusCdALV;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_INIT_APPT_DT")
    private Date rscsInitApptDt;

    @Column(name = "RSCS_NUM_INIT_RESCHS", length = 2)
    private Short rscsNumInitReschs;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_ASSIGNED_TS")
    private Date rscsAssignedTs;

    @Column(name = "RSCS_PRIORITY", length = 2)
    private String rscsPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCS_NXT_FOL_UP_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscsNxtFolUpTypeCdALV;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_NXT_FOL_UP_DT")
    private Date rscsNxtFolUpDt;

    @Column(name = "RSCS_SYNOPSIS", length = 200)
    private String rscsSynopsis;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_FIRST_SUBS_APPT_DT")
    private Date rscsFirstSubsApptDt;

    @Column(name = "RSCS_NUM_FST_SUB_RESCHS", length = 2)
    private Short rscsNumFstSubReschs;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_SECOND_SUBS_APPT_DT")
    private Date rscsSecondSubsApptDt;

    @Column(name = "RSCS_NUM_SEC_SUB_RESCHS", length = 2)
    private Short rscsNumSecSubReschs;

    @Column(name = "RSCS_ON_WAITLIST_IND", length = 1)
    private String rscsOnWaitlistInd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_ON_WAITLIST_DT")
    private Date rscsOnWaitlistDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_ON_WAITLIST_CLEAR_DT")
    private Date rscsOnWaitlistClearDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_TRNG_REFERRAL_DT")
    private Date rscsTrngReferralDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_TRNG_APPROVAL_DT")
    private Date rscsTrngApprovalDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_TRNG_REPORTED_DT")
    private Date rscsTrngReportedDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_REASSIGNED_DT")
    private Date rscsReassignedDt;

    @Column(name = "RSCS_REASSIGNED_BY", length = 15)
    private Long rscsReassignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCS_REASSIGN_REASON_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscsReassignReasonCd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_CLOSED_DT")
    private Date rscsClosedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCS_CLOSED_REASON_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscsClosedReasonCd;

    @Column(name = "RSCS_CREATED_BY", length = 10)
    private String rscsCreatedBy;

    @Column(name = "RSCS_CREATED_USING", length = 50)
    private String rscsCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_CREATED_TS")
    private Date rscsCreatedTs;

    @Column(name = "RSCS_LAST_UPD_BY", length = 10)
    private String rscsLastUpdBy;

    @Column(name = "RSCS_LAST_UPD_USING", length = 50)
    private String rscsLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCS_LAST_UPD_TS")
    private Date rscsLastUpdTs;

    @Column(name = "RSCS_ON_WAITLIST_AUTO_SCH_IND")
    String rscsOnWaitlistAutoSchInd;

}
