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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the RESEA_UPLOAD_SCH_ERROR_RUSE database table.
 */
@Entity
@Table(name = "RESEA_UPLOAD_SCH_ERROR_RUSE")
@Data
public class ReseaUploadSchErrorRuseDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the RESEA_UPLOAD_SCH_ERROR_RUSE table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RUSE_ID_SEQ_GEN")
    @SequenceGenerator(name = "RUSE_ID_SEQ_GEN", sequenceName = "RUSE_ID_SEQ", allocationSize = 1)
    @Column(name = "RUSE_ID", unique = true, nullable = false, length = 15)
    private Long ruseId;

    //bi-directional many-to-one association to ReseaUploadStagingRust
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUSM_ID")
    private ReseaUploadSchSummaryRusmDAO rusmDAO;

    //bi-directional many-to-one association to ReseaUploadSchStagingRuss
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUSS_ID")
    private ReseaUploadSchStagingRussDAO russDAO;

    @Column(name = "RUSE_ERR_DESC")
    private String ruseErrDesc;

    @Column(name = "RUSE_ERR_NUM")
    private Integer ruseErrNum;

    @Column(name = "RUSE_CREATED_BY")
    private String ruseCreatedBy;

    @Column(name = "RUSE_CREATED_USING")
    private String ruseCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSE_CREATED_TS")
    private Date ruseCreatedTs;

    @Column(name = "RUSE_LAST_UPD_BY")
    private String ruseLastUpdBy;

    @Column(name = "RUSE_LAST_UPD_USING")
    private String ruseLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSE_LAST_UPD_TS")
    private Date ruseLastUpdTs;
}