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
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RESEA_RMT_MTG_INFO_RSRM")
@Data
public class ReseaRmtMtgInfoRsrmDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSRM_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSRM_ID_SEQ_GEN", sequenceName = "RSRM_ID_SEQ", allocationSize = 1)
    @Column(name = "RSRM_ID", unique = true, nullable = false, length = 15)
    private Long rsrmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIS_ID")
    private ReseaIntvwSchRsisDAO rsisDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRM_EFFECTIVE_DT")
    private Date rsrmEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRM_EXPIRATION_DT")
    private Date rsrmExpirationDt;

    @Column(name = "RSRM_MEETING_CATG_CD")
    private Long rsrmMeetingCatgCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSRM_MEETING_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsrmMeetingTypeCdALV;

    @Column(name = "RSRM_MEETING_URL", length = 1000)
    private String rsrmMeetingUrl;

    @Column(name = "RSRM_MEETING_ID", length = 25)
    private String rsrmMeetingId;

    @Column(name = "RSRM_MEETING_PWD", length = 25)
    private String rsrmMeetingPwd;

    @Column(name = "RSRM_MEETING_TELE_1", length = 1000)
    private String rsrmMeetingTele1;

    @Column(name = "RSRM_MEETING_TELE_2", length = 1000)
    private String rsrmMeetingTele2;

    @Column(name = "RSRM_MEETING_OTHER_REF", length = 1000)
    private String rsrmMeetingOtherRef;

    @Column(name = "RSRM_CREATED_BY", length = 10)
    private String rsrmCreatedBy;

    @Column(name = "RSRM_CREATED_USING", length = 50)
    private String rsrmCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRM_CREATED_TS")
    private Date rsrmCreatedTs;

    @Column(name = "RSRM_LAST_UPD_BY", length = 10)
    private String rsrmLastUpdBy;

    @Column(name = "RSRM_LAST_UPD_USING", length = 50)
    private String rsrmLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRM_LAST_UPD_TS")
    private Date rsrmLastUpdTs;


}
