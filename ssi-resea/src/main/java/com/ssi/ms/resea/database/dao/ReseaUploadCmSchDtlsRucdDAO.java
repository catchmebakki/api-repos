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
 * The persistent class for the RESEA_UPLOAD_CM_SCH_DTLS_RUCD database table.
 */
@Entity
@Table(name = "RESEA_UPLOAD_CM_SCH_DTLS_RUCD")
@Data
public class ReseaUploadCmSchDtlsRucdDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the RESEA_UPLOAD_CM_SCH_DTLS_RUCD table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RUCD_ID_SEQ_GEN")
    @SequenceGenerator(name = "RUCD_ID_SEQ_GEN", sequenceName = "RUCD_ID_SEQ", allocationSize = 1)
    @Column(name = "RUCD_ID", unique = true, nullable = false, length = 15)
    private Long rucdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUCS_ID")
    private ReseaUploadCmSchRucsDAO rucsDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUSM_ID")
    private ReseaUploadSchSummaryRusmDAO rusmDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUSS_ID")
    private ReseaUploadSchStagingRussDAO russDAO;

    @Column(name = "RUCD_DAY_OF_WEEK")
    private Short rucdDayOfWeek;

    @Column(name = "RUCD_START_TIME")
    private String rucdStartTime;

    @Column(name = "RUCD_END_TIME")
    private String rucdEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUCD_INTVW_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rucdIntvwTypeCdAlv;

    @Column(name = "RUCD_ALLOW_ONSITE_IND")
    private String rucdAllowOnsiteInd;

    @Column(name = "RUCD_ALLOW_REMOTE_IND")
    private String rucdAllowRemoteInd;

    @Column(name = "RUCD_MEETING_URL")
    private String rucdMeetingUrl;

    @Column(name = "RUCD_MEETING_ID")
    private String rucdMeetingId;

    @Column(name = "RUCD_MEETING_PWD")
    private String rucdMeetingPwd;

    @Column(name = "RUCD_MEETING_TELE_1")
    private String rucdMeetingTele1;

    @Column(name = "RUCD_MEETING_TELE_2")
    private String rucdMeetingTele2;

    @Column(name = "RUCD_MEETING_OTHER_REF")
    private String rucdMeetingOtherRef;

    @Column(name = "RUCD_CREATED_BY", length = 10)
    private String rucdCreatedBy;

    @Column(name = "RUCD_CREATED_USING", length = 50)
    private String rucdCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCD_CREATED_TS")
    private Date rucdCreatedTs;

    @Column(name = "RUCD_LAST_UPD_BY", length = 10)
    private String rucdLastUpdBy;

    @Column(name = "RUCD_LAST_UPD_USING", length = 50)
    private String rucdLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUCD_LAST_UPD_TS")
    private Date rucdLastUpdTs;
}