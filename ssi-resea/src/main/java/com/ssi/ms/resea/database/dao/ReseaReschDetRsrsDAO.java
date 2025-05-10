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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RESEA_RESCH_DET_RSRS")
@Data
public class ReseaReschDetRsrsDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSRS_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSRS_ID_SEQ_GEN", sequenceName = "RSRS_ID_SEQ", allocationSize = 1)
    @Column(name = "RSRS_ID", unique = true, nullable = false, length = 15)
    private Long rsrsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIC_ID_OLD")
    private ReseaIntvwerCalRsicDAO oldRsicDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIC_ID_NEW")
    private ReseaIntvwerCalRsicDAO newRsicDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSRS_RESCH_REASON_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsrsReschReasonCdALV;

    @Column(name = "RSRS_PLACEHOLDER_IND", length = 1)
    private String rsrsPlaceHolderInd = "N";

    @Column(name = "RSRS_IN_SCH_WINDOW_IND", length = 1)
    private String rsrsInSchWindowInd;

    @Column(name = "RSRS_LATE_SCH_NOTES", length = 4000)
    private String rsrsLateSchNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSRS_REASON_ENTITY_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsrsReasonEntityTypeCdALV;

    @Column(name = "RSRS_ENTITY_NAME", length = 100)
    private String rsrsEntityName;

    @Column(name = "RSRS_ENTITY_CITY", length = 30)
    private String rsrsEntityCity;

    @Column(name = "RSRS_ENTITY_STATE", length = 2)
    private String rsrsEntityState;

    @Column(name = "RSRS_ENTITY_TELE_NUM", length = 10)
    private String rsrsEntityTeleNum;

    @Column(name = "RSRS_JOB_TITLE", length = 100)
    private String rsrsJobTitle;

    @Column(name = "RSRS_PT_FT_IND", length = 1)
    private String rsrsPtFtInd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRS_APPT_DT")
    @NotFound(action = NotFoundAction.IGNORE)
    private Date rsrsApptDt;

    @Column(name = "RSRS_APPT_TIME", length = 5)
    private String rsrsApptTime;

    @Column(name = "RSRS_STATF_NOTES", length = 4000)
    private String rsrsStatfNotes;

    @Column(name = "RSRS_ISSUES_CNT", length = 3)
    private Integer rsrsIssuesCnt = 0;

    @Column(name = "RSRS_PROC_STATUS_IND", length = 1)
    private String rsrsProcStatusInd;

    @Column(name = "RSRS_CREATED_BY", length = 10)
    private String rsrsCreatedBy;

    @Column(name = "RSRS_CREATED_USING", length = 50)
    private String rsrsCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRS_CREATED_TS")
    private Date rsrsCreatedTs;

    @Column(name = "RSRS_LAST_UPD_BY", length = 10)
    private String rsrsLastUpdBy;

    @Column(name = "RSRS_LAST_UPD_USING", length = 50)
    private String rsrsLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRS_LAST_UPD_TS")
    private Date rsrsLastUpdTs;
}
