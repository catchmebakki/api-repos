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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the RESEA_UPLOAD_SCH_STAGING_RUSS database table.
 */
@Entity
@Table(name = "RESEA_UPLOAD_SCH_STAGING_RUSS")
@Data
public class ReseaUploadSchStagingRussDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the RESEA_UPLOAD_SCH_STAGING_RUSS table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RUSS_ID_SEQ_GEN")
    @SequenceGenerator(name = "RUSS_ID_SEQ_GEN", sequenceName = "RUSS_ID_SEQ", allocationSize = 1)
    @Column(name = "RUSS_ID", unique = true, nullable = false, length = 15)
    private Long russId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUSM_ID")
    private ReseaUploadSchSummaryRusmDAO rusmDAO;

    @Column(name = "RUSS_DAY_OF_WEEK", length = 20)
    @Size(max = 20)
    private String russDayOfWeek;

    @Column(name = "RUSS_APPT_TIMEFRAME", length = 50)
    @Size(max = 50)
    private String russApptTimeframe;

    @Column(name = "RUSS_ZOOM_LINK_DETAILS", length = 4000)
    @Size(max = 4000)
    private String russZoomLinkDetails;

    @Column(name = "RUSS_ALLOW_ONSITE_IND", length = 1)
    @Size(max = 1)
    private String russAllowOnsiteInd;

    @Column(name = "RUSS_ALLOW_REMOTE_IND", length = 1)
    @Size(max = 1)
    private String russAllowRemoteInd;

    @Column(name = "RUSS_CREATED_BY", length = 10)
    private String russCreatedBy;

    @Column(name = "RUSS_CREATED_USING", length = 50)
    private String russCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSS_CREATED_TS")
    private Date russCreatedTs;

    @Column(name = "RUSS_LAST_UPD_BY", length = 10)
    private String russLastUpdBy;

    @Column(name = "RUSS_LAST_UPD_USING", length = 50)
    private String russLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSS_LAST_UPD_TS")
    private Date russLastUpdTs;
}