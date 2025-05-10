package com.ssi.ms.masslayoff.database.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the MSL_UPLOAD_ERROR_MUSE database table.
 */
@Entity
@Table(name = "MSL_UPLOAD_ERROR_MUSE")
public class MslUploadErrorMuseDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the MSL_UPLOAD_ERROR_MUSE table primary ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MUSE_ID")
    private Long museId;

    @Column(name = "MUSE_CREATED_BY")
    private String museCreatedBy;

    @Column(name = "MUSE_CREATED_TS")
    private Timestamp museCreatedTs;

    @Column(name = "MUSE_CREATED_USING")
    private String museCreatedUsing;

    @Column(name = "MUSE_ERR_DESC")
    private String museErrDesc;

    @Column(name = "MUSE_ERR_NUM")
    private Integer museErrNum;

    @Column(name = "MUSE_LAST_UPD_BY")
    private String museLastUpdBy;

    @Column(name = "MUSE_LAST_UPD_TS")
    private Timestamp museLastUpdTs;

    @Column(name = "MUSE_LAST_UPD_USING")
    private String museLastUpdUsing;

    //bi-directional many-to-one association to MslUploadStagingMust
    @ManyToOne
    @JoinColumn(name = "FK_MUST_ID")
    private MslUploadStagingMustDAO mslUploadStagingMust;

    //bi-directional many-to-one association to MslUploadSummaryMusm
    @ManyToOne
    @JoinColumn(name = "FK_MUSM_ID")
    private MslUploadSummaryMusmDAO mslUploadSummaryMusm;

    public Long getMuseId() {
        return this.museId;
    }

    public void setMuseId(Long museId) {
        this.museId = museId;
    }

    public String getMuseCreatedBy() {
        return this.museCreatedBy;
    }

    public void setMuseCreatedBy(String museCreatedBy) {
        this.museCreatedBy = museCreatedBy;
    }

    public Timestamp getMuseCreatedTs() {
        return this.museCreatedTs;
    }

    public void setMuseCreatedTs(Timestamp museCreatedTs) {
        this.museCreatedTs = museCreatedTs;
    }

    public String getMuseCreatedUsing() {
        return this.museCreatedUsing;
    }

    public void setMuseCreatedUsing(String museCreatedUsing) {
        this.museCreatedUsing = museCreatedUsing;
    }

    public String getMuseErrDesc() {
        return this.museErrDesc;
    }

    public void setMuseErrDesc(String museErrDesc) {
        this.museErrDesc = museErrDesc;
    }

    public Integer getMuseErrNum() {
        return this.museErrNum;
    }

    public void setMuseErrNum(Integer museErrNum) {
        this.museErrNum = museErrNum;
    }

    public String getMuseLastUpdBy() {
        return this.museLastUpdBy;
    }

    public void setMuseLastUpdBy(String museLastUpdBy) {
        this.museLastUpdBy = museLastUpdBy;
    }

    public Timestamp getMuseLastUpdTs() {
        return this.museLastUpdTs;
    }

    public void setMuseLastUpdTs(Timestamp museLastUpdTs) {
        this.museLastUpdTs = museLastUpdTs;
    }

    public String getMuseLastUpdUsing() {
        return this.museLastUpdUsing;
    }

    public void setMuseLastUpdUsing(String museLastUpdUsing) {
        this.museLastUpdUsing = museLastUpdUsing;
    }

    public MslUploadStagingMustDAO getMslUploadStagingMust() {
        return this.mslUploadStagingMust;
    }

    public void setMslUploadStagingMust(MslUploadStagingMustDAO mslUploadStagingMustDAO) {
        this.mslUploadStagingMust = mslUploadStagingMustDAO;
    }

    public MslUploadSummaryMusmDAO getMslUploadSummaryMusm() {
        return this.mslUploadSummaryMusm;
    }

    public void setMslUploadSummaryMusm(MslUploadSummaryMusmDAO mslUploadSummaryMusmDAO) {
        this.mslUploadSummaryMusm = mslUploadSummaryMusmDAO;
    }

}