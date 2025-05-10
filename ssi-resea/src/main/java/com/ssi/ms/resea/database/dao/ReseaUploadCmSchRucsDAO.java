package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the RESEA_UPLOAD_CM_SCH_RUCS database table.
 */
@Entity
@Table(name = "RESEA_UPLOAD_CM_SCH_RUCS")
@Data
public class ReseaUploadCmSchRucsDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the RESEA_UPLOAD_CM_SCH_RUCS table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RUCS_ID_SEQ_GEN")
    @SequenceGenerator(name = "RUCS_ID_SEQ_GEN", sequenceName = "RUCS_ID_SEQ", allocationSize = 1)
    @Column(name = "RUCS_ID", unique = true, nullable = false, length = 15)
    private Long rucsId;

    //bi-directional many-to-one association to StaffStf
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    //bi-directional many-to-one association to LocalOfficeLof
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_LOF_ID")
    private LocalOfficeLofDAO lofDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCS_EFFECTIVE_DT")
    private Date rucsEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCS_EXPIRATION_DT")
    private Date rucsExpirationDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUCS_STATUS_Cd")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rucsStatusCdAlv;

    @Column(name = "RUCS_CREATED_BY")
    private String rucsCreatedBy;

    @Column(name = "RUCS_CREATED_USING")
    private String rucsCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCS_CREATED_TS")
    private Date rucsCreatedTs;

    @Column(name = "RUCS_LAST_UPD_BY")
    private String rucsLastUpdBy;

    @Column(name = "RUCS_LAST_UPD_USING")
    private String rucsLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCS_LAST_UPD_TS")
    private Date rucsLastUpdTs;
}