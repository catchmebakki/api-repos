package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "RESEA_ISSUE_IDENTIFIED_RSII")
@Data
public class ReseaIssueIdentifiedRsiiDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSII_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSII_ID_SEQ_GEN", sequenceName = "RSII_ID_SEQ", allocationSize = 1)
    @Column(name = "RSII_ID", unique = true, nullable = false, length = 15)
    private Long rsiiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_NMI_ID")
    private NonMonIssuesNmiDAO nmiDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_DEC_ID")
    private IssueDecisionDecDAO issueDecisionDecDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIC_ID")
    private ReseaIntvwerCalRsicDAO rsicDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSWR_ID")
    private ReseaWrkSrchWksReviewRswrDAO rswrDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSII_ISSUE_EFF_DT")
    private Date rsiiIssueEffDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSII_ISSUE_END_DT")
    private Date rsiiIssueEndDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSII_ISSUE_ID_DURING_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsiiIssueIdDuringCdALV;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSII_WEEK_ENDING_DT")
    private Date rsiiWeekEndingDt;

    @Column(name = "RSII_ISSUE_IDENTIFIED_BY", length = 15)
    private Long rsiiIssueIdentifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSII_SOURCE_IFK_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsiiSourceIfkCd;

    @Column(name = "RSII_SOURCE_IFK", length = 15)
    private Long rsiiSourceIfk;

    @Column(name = "RSII_STAFF_NOTES", length = 4000)
    private String rsiiStaffNotes;

    @Column(name = "RSII_PROC_STATUS_IND", length = 1)
    @ColumnDefault("U")
    private String rsiiProcStatusInd = "U";

    @Column(name = "RSII_CREATED_BY", length = 10)
    private String rsiiCreatedBy;

    @Column(name = "RSII_CREATED_USING", length = 50)
    private String rsiiCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSII_CREATED_TS")
    private Date rsiiCreatedTs;

    @Column(name = "RSII_LAST_UPD_BY", length = 10)
    private String rsiiLastUpdBy;

    @Column(name = "RSII_LAST_UPD_USING", length = 50)
    private String rsiiLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSII_LAST_UPD_TS")
    private Date rsiiLastUpdTs;

    @Column(name = "RSII_DEC_DETECT_DT_IND", length = 1)
    private String rsiiDecDetectDtInd;

}
