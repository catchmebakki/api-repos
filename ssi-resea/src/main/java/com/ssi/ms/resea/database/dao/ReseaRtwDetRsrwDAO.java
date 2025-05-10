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
import javax.validation.constraints.Digits;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RESEA_RTW_DET_RSRW")
@Data
public class ReseaRtwDetRsrwDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSRW_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSRW_ID_SEQ_GEN", sequenceName = "RSRW_ID_SEQ", allocationSize = 1)
    @Column(name = "RSRW_ID", unique = true, nullable = false, length = 15)
    private Long rsrwId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIC_ID")
    private ReseaIntvwerCalRsicDAO rsicDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRW_NEW_EMP_START_DT")
    private Date rsrwNewEmpStartDt;

    @Column(name = "RSRW_EMP_NAME", length = 100)
    private String rsrwEmpName;

    @Column(name = "RSRW_EXACT_JOB_TITLE", length = 100)
    private String rsrwExactJobTitle;

    @Column(name = "RSRW_PT_FT_IND", length = 1)
    private String rsrwPtFtInd;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "RSRW_HOURLY_PAY_RATE")
    private BigDecimal rsrwHourlyPayRate;

    @Column(name = "RSRW_EMP_WORK_LOC_ST", length = 2)
    private String rsrwEmpWorkLocSt;

    @Column(name = "RSRW_EMP_WORK_LOC_CITY", length = 30)
    private String rsrwEmpWorkLocCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSRW_WORK_MODE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsrwWorkModeCdALV;

    @Column(name = "RSRW_STAFF_NOTES", length = 4000)
    private String rsrwStaffNotes;

    @Column(name = "RSRW_JMS_890_IND", length = 1)
    private String rsrwJms890Ind;

    @Column(name = "RSRW_JMS_REFERRAL_IND", length = 1)
    private String rsrwJmsReferralInd;

    @Column(name = "RSRW_JMS_CLSE_GOALS_IND", length = 1)
    private String rsrwJmsClseGoalsInd;

    @Column(name = "RSRW_JMS_CLSE_IEP_IND", length = 1)
    private String rsrwJmsClseIepInd;

    @Column(name = "RSRW_JMS_CASE_NOTES_IND", length = 1)
    private String rsrwJmsCaseNotesInd;

    @Column(name = "RSRW_JMS_RESUME_OFF_IND", length = 1)
    private String rsrwJmsResumeOffInd;

    @Column(name = "RSRW_PROC_STATUS_IND", length = 1)
    private String rsrwProcStatusInd;

    @Column(name = "RSRW_CREATED_BY", length = 10)
    private String rsrwCreatedBy;

    @Column(name = "RSRW_CREATED_USING", length = 50)
    private String rsrwCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRW_CREATED_TS")
    private Date rsrwCreatedTs;

    @Column(name = "RSRW_LAST_UPD_BY", length = 10)
    private String rsrwLastUpdBy;

    @Column(name = "RSRW_LAST_UPD_USING", length = 50)
    private String rsrwLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSRW_LAST_UPD_TS")
    private Date rsrwLastUpdTs;
}
