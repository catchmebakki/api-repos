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
@Table(name = "RESEA_INTVW_DET_RSID")
@Data
public class ReseaIntvwDetRsidDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSID_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSID_ID_SEQ_GEN", sequenceName = "RSID_ID_SEQ", allocationSize = 1)
    @Column(name = "RSID_ID", unique = true, nullable = false, length = 15)
    private Long rsidId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSIC_ID")
    private ReseaIntvwerCalRsicDAO rsicDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @Column(name = "RSID_JMS_102_IND", length = 1)
    private String rsidJms102Ind = "N";

    @Column(name = "RSID_JMS_106_IND", length = 1)
    private String rsidJms106Ind = "N";

    @Column(name = "RSID_JMS_107_IND", length = 1)
    private String rsidJms107Ind = "N";

    @Column(name = "RSID_JMS_123_IND", length = 1)
    private String rsidJms123Ind = "N";

    @Column(name = "RSID_JMS_153_IND", length = 1)
    private String rsidJms153Ind = "N";

    @Column(name = "RSID_JMS_160_IND", length = 1)
    private String rsidJms160Ind = "N";

    @Column(name = "RSID_JMS_179_IND", length = 1)
    private String rsidJms179Ind = "N";

    @Column(name = "RSID_JMS_205_IND", length = 1)
    private String rsidJms205Ind = "N";

    @Column(name = "RSID_JMS_209_IND", length = 1)
    private String rsidJms209Ind = "N";

    @Column(name = "RSID_JMS_500_IND", length = 1)
    private String rsidJms500Ind = "N";

    @Column(name = "RSID_JMS_SELF_CM_IND", length = 1)
    private String rsidJmsSelfCmInd = "N";

    @Column(name = "RSID_JMS_CASE_NOTES_IND", length = 1)
    private String rsidJmsCaseNotesInd = "N";

    @Column(name = "RSID_JMS_REG_COMPLT_IND", length = 1)
    private String rsidJmsRegCompltInd = "N";

    @Column(name = "RSID_JMS_REG_INCOMP_IND", length = 1)
    private String rsidJmsRegIncompInd = "N";

    @Column(name = "RSID_JMS_RESUME_IND", length = 1)
    private String rsidJmsResumeInd = "N";

    @Temporal(TemporalType.DATE)
    @Column(name = "RSID_JMS_RESUME_EXP_DT")
    private Date rsidJmsResumeExpDt;

    @Column(name = "RSID_JMS_V_RECRUITER_IND", length = 1)
    private String rsidJmsVRecruiterInd = "N";

    @Temporal(TemporalType.DATE)
    @Column(name = "RSID_JMS_V_RECRT_EXP_DT")
    private Date rsidJmsVRecrtExpDt;

    @Column(name = "RSID_JMS_WP_APPL_IND", length = 1)
    private String rsidJmsWpApplInd = "N";

    @Column(name = "RSID_JMS_WP_APPL_SIG_IND", length = 1)
    private String rsidJmsWpApplSigInd = "N";

    @Column(name = "RSID_JMS_IEP_SIG_IND", length = 1)
    private String rsidJmsIepSigInd = "N";

    @Column(name = "RSID_JMS_VR_DHHS_IND", length = 1)
    private String rsidJmsVrDhhsInd = "N";

    @Column(name = "RSID_JMS_CLSE_GOALS_IND", length = 1)
    private String rsidJmsClseGoalsInd = "N";

    @Column(name = "RSID_JMS_CLSE_IEP_IND", length = 1)
    private String rsidJmsClseIepInd = "N";

    @Column(name = "RSID_JMS_EPCKLST_UPL_IND", length = 1)
    private String rsidJmsEpcklstUplInd = "N";

    @Column(name = "RSID_MRP_RVWD_IND", length = 1)
    private String rsidMrpRvwdInd = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSID_MRP_RVWD_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsidMrpRvwdCdALV;

    @Column(name = "RSID_MRP_ASSGND_IND", length = 1)
    private String rsidMrpAssgndInd = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSID_MRP_ASSGND_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsidMrpAssgndCdALV;

    @Column(name = "RSID_ID_VERIFIED_IND", length = 1)
    private String rsidIdVerifiedInd = "N";

    @Column(name = "RSID_SLF_SCH_RMDR_IND", length = 1)
    private String rsidSlfSchRmdrInd = "N";

    @Temporal(TemporalType.DATE)
    @Column(name = "RSID_SLF_SCH_BY_DT")
    private Date rsidSlfSchByDt;

    @Column(name = "RSID_PREV_REFRL_CCF_IND", length = 1)
    private String rsidPrevRefrlCcfInd = "N";

    @Column(name = "RSID_ES_CONFIRM_IND", length = 1)
    private String rsidEsConfirmInd = "N";

    @Column(name = "RSID_STATF_NOTES", length = 4000)
    private String rsidStatfNotes;

    @Column(name = "RSID_JOB_REFERRAL_CNT", length = 3)
    private Integer rsidJobReferralCnt = 0;

    @Column(name = "RSID_WS_ISSUES_CNT", length = 3)
    private Integer rsidWsIssuesCnt = 0;

    @Column(name = "RSID_OTH_ISSUES_CNT", length = 3)
    private Integer rsidOthIssuesCnt = 0;

    @Column(name = "RSID_PROC_STATUS_IND", length = 1)
    private String rsidProcStatusInd = "U";

    @Column(name = "RSID_CREATED_BY", length = 10)
    private String rsidCreatedBy;

    @Column(name = "RSID_CREATED_USING", length = 50)
    private String rsidCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSID_CREATED_TS")
    private Date rsidCreatedTs;

    @Column(name = "RSID_LAST_UPD_BY", length = 10)
    private String rsidLastUpdBy;

    @Column(name = "RSID_LAST_UPD_USING", length = 50)
    private String rsidLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSID_LAST_UPD_TS")
    private Date rsidLastUpdTs;
}
