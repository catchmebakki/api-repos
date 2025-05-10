package com.ssi.ms.masslayoff.database.dao;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MSL_UPLOAD_STAGING_MUST database table.
 */
@Entity
@Table(name = "MSL_UPLOAD_STAGING_MUST")
public class MslUploadStagingMustDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the MSL_UPLOAD_STAGING_MUST table primary ID.
     */
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MUST_ID")
    private Long mustId;

    @Getter
    @Column(name = "MUST_CREATED_BY")
    private String mustCreatedBy;

    @Getter
    @Column(name = "MUST_CREATED_TS")
    private Timestamp mustCreatedTs;

    @Getter
    @Column(name = "MUST_CREATED_USING")
    private String mustCreatedUsing;

    @Getter
    @Column(name = "MUST_DI_IND")
    private String mustDiInd;

    @Getter
    @Column(name = "MUST_EMP_AC_LOC")
    private String mustEmpAcLoc;

    @Getter
    @Column(name = "MUST_EMP_AC_NUM")
    private String mustEmpAcNum;

    @Getter
    @Column(name = "MUST_FIRST_NAME")
    private String mustFirstName;

    @Getter
    @Column(name = "MUST_LAST_NAME")
    private String mustLastName;

    @Getter
    @Column(name = "MUST_LAST_UPD_BY")
    private String mustLastUpdBy;

    @Getter
    @Column(name = "MUST_LAST_UPD_TS")
    private Timestamp mustLastUpdTs;

    @Getter
    @Column(name = "MUST_LAST_UPD_USING")
    private String mustLastUpdUsing;

    @Getter
    @Column(name = "MUST_MSL_DATE")
    private String mustMslDate;

    @Getter
    @Column(name = "MUST_MSL_EFF_DATE")
    private String mustMslEffDate;

    @Getter
    @Column(name = "MUST_MSL_NUM")
    private String mustMslNum;

    @Getter
    @Column(name = "MUST_RECALL_DATE")
    private String mustRecallDate;

    @Getter
    @Column(name = "MUST_SSN")
    private String mustSsn;

    //bi-directional many-to-one association to MslUploadErrorMuse
    @OneToMany(mappedBy = "mslUploadStagingMust")
    private List<MslUploadErrorMuseDAO> mslUploadErrorMusDAOS;

    //bi-directional many-to-one association to MslUploadSummaryMusm
    @ManyToOne
    @JoinColumn(name = "FK_MUSM_ID", referencedColumnName = "MUSM_ID")
    private MslUploadSummaryMusmDAO mslUploadSummaryMusmDAO;

    public void setMustId(Long mustId) {
        this.mustId = mustId;
    }

    public void setMustCreatedBy(String mustCreatedBy) {
        this.mustCreatedBy = mustCreatedBy;
    }

    public void setMustCreatedTs(Timestamp mustCreatedTs) {
        this.mustCreatedTs = mustCreatedTs;
    }

    public void setMustCreatedUsing(String mustCreatedUsing) {
        this.mustCreatedUsing = mustCreatedUsing;
    }

    public void setMustDiInd(String mustDiInd) {
        this.mustDiInd = mustDiInd;
    }

    public void setMustEmpAcLoc(String mustEmpAcLoc) {
        this.mustEmpAcLoc = mustEmpAcLoc;
    }

    public void setMustEmpAcNum(String mustEmpAcNum) {
        this.mustEmpAcNum = mustEmpAcNum;
    }

    public void setMustFirstName(String mustFirstName) {
        this.mustFirstName = mustFirstName;
    }

    public void setMustLastName(String mustLastName) {
        this.mustLastName = mustLastName;
    }

    public void setMustLastUpdBy(String mustLastUpdBy) {
        this.mustLastUpdBy = mustLastUpdBy;
    }

    public void setMustLastUpdTs(Timestamp mustLastUpdTs) {
        this.mustLastUpdTs = mustLastUpdTs;
    }

    public void setMustLastUpdUsing(String mustLastUpdUsing) {
        this.mustLastUpdUsing = mustLastUpdUsing;
    }

    public void setMustMslDate(String mustMslDate) {
        this.mustMslDate = mustMslDate;
    }

    public void setMustMslEffDate(String mustMslEffDate) {
        this.mustMslEffDate = mustMslEffDate;
    }

    public void setMustMslNum(String mustMslNum) {
        this.mustMslNum = mustMslNum;
    }

    public void setMustRecallDate(String mustRecallDate) {
        this.mustRecallDate = mustRecallDate;
    }

    public void setMustSsn(String mustSsn) {
        this.mustSsn = mustSsn;
    }

    public List<MslUploadErrorMuseDAO> getMslUploadErrorMuses() {
        return this.mslUploadErrorMusDAOS;
    }

    public void setMslUploadErrorMuses(List<MslUploadErrorMuseDAO> mslUploadErrorMusDAOS) {
        this.mslUploadErrorMusDAOS = mslUploadErrorMusDAOS;
    }

    public MslUploadErrorMuseDAO addMslUploadErrorMus(MslUploadErrorMuseDAO mslUploadErrorMus) {
        getMslUploadErrorMuses().add(mslUploadErrorMus);
        mslUploadErrorMus.setMslUploadStagingMust(this);

        return mslUploadErrorMus;
    }

    public MslUploadErrorMuseDAO removeMslUploadErrorMus(MslUploadErrorMuseDAO mslUploadErrorMus) {
        getMslUploadErrorMuses().remove(mslUploadErrorMus);
        mslUploadErrorMus.setMslUploadStagingMust(null);

        return mslUploadErrorMus;
    }

    public MslUploadSummaryMusmDAO getMslUploadSummaryMusm() {
        return this.mslUploadSummaryMusmDAO;
    }

    public void setMslUploadSummaryMusm(MslUploadSummaryMusmDAO mslUploadSummaryMusmDAO) {
        this.mslUploadSummaryMusmDAO = mslUploadSummaryMusmDAO;
    }

    public String toString() {
        final Long mustIdLocal = this.getMustId();
        return "MslUploadStagingMustDAO(mustId=" + mustIdLocal + ", CreatedBy=" + this.getMustCreatedBy() + ", CreatedTs=" +
                this.getMustCreatedTs() + ", mustCreatedUsing=" + this.getMustCreatedUsing() + ", mustDiInd=" +
                this.getMustDiInd() + ", mustEmpAcLoc=" + this.getMustEmpAcLoc() + ", mustEmpAcNum=" + this.getMustEmpAcNum() +
                ", mustFirstName=" + this.getMustFirstName() + ", mustLastName=" + this.getMustLastName() + ", mustLastUpdBy=" +
                this.getMustLastUpdBy() + ", mustLastUpdTs=" + this.getMustLastUpdTs() + ", mustLastUpdUsing=" +
                this.getMustLastUpdUsing() + ", mustMslDate=" + this.getMustMslDate() + ", mustMslEffDate=" +
                this.getMustMslEffDate() + ", mustMslNum=" + this.getMustMslNum() + ", mustRecallDate=" +
                this.getMustRecallDate() + ", mustSsn=" + this.getMustSsn() + ")";
    }

}