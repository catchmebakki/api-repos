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
@Table(name = "RESEA_JOB_REFERRAL_RSJR")
@Data
public class ReseaJobReferralRsjrDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSJR_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSJR_ID_SEQ_GEN", sequenceName = "RSJR_ID_SEQ", allocationSize = 1)
    @Column(name = "RSJR_ID", unique = true, nullable = false, length = 15)
    private Long rsjrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSID_ID")
    private ReseaIntvwDetRsidDAO rsidDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSJR_EFFECTIVE_FROM_DT")
    private Date rsjrEffectiveFromDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSJR_EFFECTIVE_UNTIL_DT")
    private Date rsjrEffectiveUntilDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSJR_SOURCE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsjrSourceCdALV;

    @Column(name = "RSJR_EMP_NAME", length = 100)
    private String rsjrEmpName;

    @Column(name = "RSJR_EXACT_JOB_TITLE", length = 100)
    private String rsjrExactJobTitle;

    @Column(name = "RSJR_PT_FT_IND", length = 1)
    private String rsjrPtFtInd;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "RSJR_HOURLY_PAY_RATE")
    private BigDecimal rsjrHourlyPayRate;

    @Column(name = "RSJR_EMP_WORK_LOC_ST", length = 2)
    private String rsjrEmpWorkLocSt;

    @Column(name = "RSJR_EMP_WORK_LOC_CITY", length = 30)
    private String rsjrEmpWorkLocCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSJR_WORK_MODE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rsjrWorkModeCdALV;

    @Column(name = "RSJR_STAFF_NOTES", length = 4000)
    private String rsjrStaffNotes;

    @Column(name = "RSJR_CREATED_BY", length = 10)
    private String rsjrCreatedBy;

    @Column(name = "RSJR_CREATED_USING", length = 50)
    private String rsjrCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSJR_CREATED_TS")
    private Date rsjrCreatedTs;

    @Column(name = "RSJR_LAST_UPD_BY", length = 10)
    private String rsjrLastUpdBy;

    @Column(name = "RSJR_LAST_UPD_USING", length = 50)
    private String rsjrLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSJR_LAST_UPD_TS")
    private Date rsjrLastUpdTs;

    @Column(name = "FK_EMP_ID")
    private Long fkEmpId;
}
