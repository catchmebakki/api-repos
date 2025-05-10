package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;


import java.util.List;
import java.util.Date;

@Entity
@Table(name = "RESEA_CASE_ACTIVITY_RSCA")
@Transactional
@NamedStoredProcedureQuery(
        name  =  "createActivty",
        procedureName  =  "PKG_RESEA.CREATE_ACTIVITY",
        parameters  =  {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_FK_RSCS_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_STAGE_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_STATUS_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_TYPE_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "pin_rsca_activty_ts", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_DESCRIPTION", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_SYNOPSIS_TYPE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_CASE_SYNOPSIS", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_MODE_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_FOLLOWUP_TYPE_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_FOLLOWUP_DT", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_FOLLOWUP_NOTE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_FOLLOWUP_DONE_IND", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_DETAILS", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_REFERENCE_IFK_CD", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCA_REFERENCE_IFK", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCN_NOTE_CATEGORY_CD", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCN_NOTE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_RSCN_SHOW_IN_NHUIS_IND", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PIN_CALLING_PROG", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_RSCA_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_NHL_ID", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_ERROR_MSG", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "POUT_SUCCESS", type = Long.class)
        }
)
@Data
public class ReseaCaseActivityRscaDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSCA_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSCA_ID_SEQ_GEN", sequenceName = "RSCA_ID_SEQ", allocationSize = 1)
    @Column(name = "RSCA_ID", unique = true, nullable = false, length = 15)
    private Long rscaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCS_ID")
    private ReseaCaseRscsDAO rscsDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO oldRscaDAO;

    /*@JoinColumn(name = "FK_RSAF_ID")
    private RsafDAO rsafDAO;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCA_STAGE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscaStageCdALV;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCA_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscaStatusCdALV;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCA_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscaTypeCdALV;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCA_ACTIVTY_TS")
    private Date rscaActivtyTs;

    @Column(name = "RSCA_DESCRIPTION", length = 100)
    private String rscaDescription;

    @Column(name = "RSCA_CASE_SYNOPSIS", length = 200)
    private String rscaCaseSynopsis;

    @Column(name = "RSCA_MODE_CD")
    private String rscaModeCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCA_FOLLOWUP_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscaFollowupTypeCdALV;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCA_FOLLOWUP_DT")
    private Date rscaFollowupDt;

    @Column(name = "RSCA_FOLLOWUP_NOTE", length = 200)
    private String rscaFollowupNote;

    @Column(name = "RSCA_FOLLOWUP_DONE_IND", length = 1)
    private String rscaFollowupDoneInd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCA_FOLLOWUP_COMP_DT")
    private Date rscaFollowupCompDt;

    @Column(name = "RSCA_DETAILS", length = 4000)
    private String rscaDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSCA_REFERENCE_IFK_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rscaReferenceIfkCd;

    @Column(name = "RSCA_REFERENCE_IFK", length = 4)
    private Long rscaReferenceIfk;

    @Column(name = "RSCA_CREATED_BY", length = 10)
    private String rscaCreatedBy;

    @Column(name = "RSCA_CREATED_USING", length = 50)
    private String rscaCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCA_CREATED_TS")
    private Date rscaCreatedTs;

    @Column(name = "RSCA_LAST_UPD_BY", length = 10)
    private String rscaLastUpdBy;

    @Column(name = "RSCA_LAST_UPD_USING", length = 50)
    private String rscaLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCA_LAST_UPD_TS")
    private Date rscaLastUpdTs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rscaDAO")
    private List<ReseaJobReferralRsjrDAO> rsjrDAOList;
}
