package com.ssi.ms.resea.database.dao;

import lombok.Data;

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
@Table(name = "RESEA_WRK_SRCH_WKS_REVIEW_RSWR")
@Data
public class ReseaWrkSrchWksReviewRswrDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RSWR_ID_SEQ_GEN")
    @SequenceGenerator(name = "RSWR_ID_SEQ_GEN", sequenceName = "RSWR_ID_SEQ", allocationSize = 1)
    @Column(name = "RSWR_ID", unique = true, nullable = false, length = 15)
    private Long rswrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSID_ID")
    private ReseaIntvwDetRsidDAO rsidDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CCA_ID")
    private CcApplnCcaDAO ccaDAO;

    @Column(name = "RSWR_JR_REVIEWED_IND", length = 1)
    private String rswrJrReviewedInd;

    @Column(name = "RSWR_CREATED_BY", length = 10)
    private String rswrCreatedBy;

    @Column(name = "RSWR_CREATED_USING", length = 50)
    private String rswrCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSWR_CREATED_TS")
    private Date rswrCreatedTs;

    @Column(name = "RSWR_LAST_UPD_BY", length = 10)
    private String rswrLastUpdBy;

    @Column(name = "RSWR_LAST_UPD_USING", length = 50)
    private String rswrLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSWR_LAST_UPD_TS")
    private Date rswrLastUpdTs;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rswrDAO")
    ReseaIssueIdentifiedRsiiDAO rsiiDAO;
}
