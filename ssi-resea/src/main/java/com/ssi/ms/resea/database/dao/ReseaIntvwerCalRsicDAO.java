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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RESEA_INTVWER_CAL_RSIC")
@Data
public class ReseaIntvwerCalRsicDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSIC_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSIC_ID_SEQ_GEN", sequenceName = "RSIC_ID_SEQ", allocationSize = 1)
    @Column(name = "RSIC_ID", unique = true, nullable = false, length = 15)
    private Long rsicId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIS_ID")
    private ReseaIntvwSchRsisDAO rsisDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STUN_ID")
    private StaffUnavaiabilityStunDAO stunDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO claimDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCS_ID")
    private ReseaCaseRscsDAO rscsDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_CAL_EVENT_DT")
    private Date rsicCalEventDt;

    @Column(name = "RSIC_CAL_EVENT_ST_TIME", length = 5)
    private String rsicCalEventStTime;

    @Column(name = "RSIC_CAL_EVENT_END_TIME", length = 5)
    private String rsicCalEventEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_CAL_EVENT_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicCalEventTypeCdAlv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_TIMESLOT_USAGE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicTimeslotUsageCdAlv;

    @Column(name = "RSIC_CAL_EVENT_DISP_IND", length = 1)
    private String rsicCalEventDispInd;

    @Column(name = "RSIC_IN_SCH_WINDOW_IND", length = 1)
    private String rsicInSchWindowInd;

    @Column(name = "RSIC_LATE_SCH_NOTES", length = 4000)
    private String rsicLateSchNotes;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_SCHEDULED_ON_TS")
    private Date rsicScheduledOnTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_SCHEDULED_BY_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicScheduledByCdAlv;

    @Column(name = "RSIC_SCHEDULED_BY_USR", length = 15)
    private Long rsicScheduledByUsr;

    @Column(name = "RSIC_MTG_MODE_IND", length = 1)
    private String rsicMtgModeInd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_MODE_CHG_RSN_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicModeChgRsnCdAlv;

    @Column(name = "RSIC_MODE_CHG_RSN_TXT", length = 2000)
    private String rsicModeChgRsnTxt;

    @Column(name = "RSIC_MTGMODE_SWCH_CNT", length = 2)
    private Integer rsicMtgmodeSwchCnt;

    @Column(name = "RSIC_MTG_RESCH_CNT", length = 2)
    private Integer rsicMtgReschCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_MTG_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicMtgStatusCdAlv;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_MTG_STATUS_UPD_TS")
    private Date rsicMtgStatusUpdTs;

    @Column(name = "RSIC_MTG_STATUS_UPD_BY", length = 15)
    private Long rsicMtgStatusUpdBy;

    @Column(name = "RSIC_STAFF_NOTES", length = 4000)
    private String rsicStaffNotes;

    @Column(name = "RSIC_TIMESLOT_SYS_NOTES", length = 4000)
    private String rsicTimeslotSysNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSIC_NOTICE_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsicNoticeStatusCdAlv;

    @Column(name = "RSIC_PROC_STATUS_IND", length = 1)
    private String rsicProcStatusInd;

    @Column(name = "RSIC_CREATED_BY", length = 10)
    private String rsicCreatedBy;

    @Column(name = "RSIC_CREATED_USING", length = 50)
    private String rsicCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_CREATED_TS")
    private Date rsicCreatedTs;

    @Column(name = "RSIC_LAST_UPD_BY", length = 10)
    private String rsicLastUpdBy;

    @Column(name = "RSIC_LAST_UPD_USING", length = 50)
    private String rsicLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_LAST_UPD_TS")
    private Date rsicLastUpdTs;

    @Column(name = "RSIC_REOPEN_ALLOWED_IND", length = 1)
    private String rsicReopenAllowedInd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSIC_REOPEN_ALLOWED_TS")
    private Date rsicReopenAllowedTs;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rsicDAO")
    private ReseaIntvwDetRsidDAO rsidDAO;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rsicDao")
    private ReseaReturnToWorkRsrwDAO rsrwDao;
}
