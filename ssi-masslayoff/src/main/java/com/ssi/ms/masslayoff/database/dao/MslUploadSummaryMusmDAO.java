package com.ssi.ms.masslayoff.database.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the MSL_UPLOAD_SUMMARY_MUSM database table.
 */
@Entity
@Table(name = "MSL_UPLOAD_SUMMARY_MUSM")
public class MslUploadSummaryMusmDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the MSL_UPLOAD_SUMMARY_MUSM table primary ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MUSM_ID")
    private Long musmId;

    @Column(name = "FK_USR_ID")
    private Long fkUsrId;

    @Column(name = "MUSM_CREATED_BY")
    private String musmCreatedBy;

    @Column(name = "MUSM_CREATED_TS")
    private Timestamp musmCreatedTs;

    @Column(name = "MUSM_CREATED_USING")
    private String musmCreatedUsing;

	@Column(name="MUSM_DI_IND")
	private String musmDiInd;

	@Column(name="MUSM_EMP_AC_LOC")
	private String musmEmpAcLoc;

	@Column(name="MUSM_EMP_AC_NUM")
	private String musmEmpAcNum;
    @Column(name = "MUSM_END_TS")
    private Timestamp musmEndTs;
	@Column(name="MUSM_ERROR_DESC")
	private String musmErrorDesc;

    @Column(name = "MUSM_FILENAME")
    private String musmFilename;

    @Column(name = "MUSM_LAST_UPD_BY")
    private String musmLastUpdBy;

    @Column(name = "MUSM_LAST_UPD_TS")
    private Timestamp musmLastUpdTs;

    @Column(name = "MUSM_LAST_UPD_USING")
    private String musmLastUpdUsing;

	@Temporal(TemporalType.DATE)
	@Column(name="MUSM_MSL_DATE")
	private Date musmMslDate;

	@Temporal(TemporalType.DATE)
	@Column(name="MUSM_MSL_EFF_DATE")
	private Date musmMslEffDate;
    @Column(name = "MUSM_MSL_NUM")
    private Long musmMslNum;

    @Column(name = "MUSM_NUM_ERRS")
    private Integer musmNumErrs;

    @Column(name = "MUSM_NUM_RECS")
    private Integer musmNumRecs;

	@Temporal(TemporalType.DATE)
	@Column(name="MUSM_RECALL_DATE")
	private Date musmRecallDate;
    @Column(name = "MUSM_REQ_TS")
    private Timestamp musmReqTs;

    @Column(name = "MUSM_START_TS")
    private Timestamp musmStartTs;

	@Column(name="MUSM_STATUS_CD")
	private Integer musmStatusCd;

	@Column(name="MUSM_SYS_FILENAME")
	private String musmSysFilename;


    //bi-directional many-to-one association to MslUploadErrorMuse
    @OneToMany(mappedBy = "mslUploadSummaryMusm")
    private List<MslUploadErrorMuseDAO> mslUploadErrorMusDAOS;


    @ManyToOne
    @JoinColumn(name = "FK_MLRL_ID")
    private MslRefListMlrlDAO  mslRefListMlrlDAO;


    //bi-directional many-to-one association to MslUploadStagingMust
    @OneToMany(mappedBy = "mslUploadSummaryMusmDAO")
    private List<MslUploadStagingMustDAO> mslUploadStagingMustDAOS;

    public Long getMusmId() {
        return this.musmId;
    }

    public void setMusmId(Long musmId) {
        this.musmId = musmId;
    }

    public Long getFkUsrId() {
        return this.fkUsrId;
    }

    public void setFkUsrId(Long fkUsrId) {
        this.fkUsrId = fkUsrId;
    }

    public String getMusmCreatedBy() {
        return this.musmCreatedBy;
    }

    public void setMusmCreatedBy(String musmCreatedBy) {
        this.musmCreatedBy = musmCreatedBy;
    }

    public Timestamp getMusmCreatedTs() {
        return this.musmCreatedTs;
    }

    public void setMusmCreatedTs(Timestamp musmCreatedTs) {
        this.musmCreatedTs = musmCreatedTs;
    }

    public String getMusmCreatedUsing() {
        return this.musmCreatedUsing;
    }

    public void setMusmCreatedUsing(String musmCreatedUsing) {
        this.musmCreatedUsing = musmCreatedUsing;
    }

	public String getMusmDiInd() {
		return this.musmDiInd;
	}

	public void setMusmDiInd(String musmDiInd) {
		this.musmDiInd = musmDiInd;
	}

	public String getMusmEmpAcLoc() {
		return this.musmEmpAcLoc;
	}

	public void setMusmEmpAcLoc(String musmEmpAcLoc) {
		this.musmEmpAcLoc = musmEmpAcLoc;
	}

	public String getMusmEmpAcNum() {
		return this.musmEmpAcNum;
	}

	public void setMusmEmpAcNum(String musmEmpAcNum) {
		this.musmEmpAcNum = musmEmpAcNum;
	}
    public Timestamp getMusmEndTs() {
        return this.musmEndTs;
    }

    public void setMusmEndTs(Timestamp musmEndTs) {
        this.musmEndTs = musmEndTs;
    }
	public String getMusmErrorDesc() {
		return this.musmErrorDesc;
	}

	public void setMusmErrorDesc(String musmErrorDesc) {
		this.musmErrorDesc = musmErrorDesc;
	}

    public String getMusmFilename() {
        return this.musmFilename;
    }

    public void setMusmFilename(String musmFilename) {
        this.musmFilename = musmFilename;
    }

    public String getMusmLastUpdBy() {
        return this.musmLastUpdBy;
    }

    public void setMusmLastUpdBy(String musmLastUpdBy) {
        this.musmLastUpdBy = musmLastUpdBy;
    }

    public Timestamp getMusmLastUpdTs() {
        return this.musmLastUpdTs;
    }

    public void setMusmLastUpdTs(Timestamp musmLastUpdTs) {
        this.musmLastUpdTs = musmLastUpdTs;
    }

    public String getMusmLastUpdUsing() {
        return this.musmLastUpdUsing;
    }

    public void setMusmLastUpdUsing(String musmLastUpdUsing) {
        this.musmLastUpdUsing = musmLastUpdUsing;
    }

	public Date getMusmMslDate() {
		return this.musmMslDate;
	}

	public void setMusmMslDate(Date musmMslDate) {
		this.musmMslDate = musmMslDate;
	}

	public Date getMusmMslEffDate() {
		return this.musmMslEffDate;
	}

	public void setMusmMslEffDate(Date musmMslEffDate) {
		this.musmMslEffDate = musmMslEffDate;
	}
    public Long getMusmMslNum() {
        return this.musmMslNum;
    }

    public void setMusmMslNum(Long musmMslNum) {
        this.musmMslNum = musmMslNum;
    }

    public Integer getMusmNumErrs() {
        return this.musmNumErrs;
    }

    public void setMusmNumErrs(Integer musmNumErrs) {
        this.musmNumErrs = musmNumErrs;
    }

    public Integer getMusmNumRecs() {
        return this.musmNumRecs;
    }

    public void setMusmNumRecs(Integer musmNumRecs) {
        this.musmNumRecs = musmNumRecs;
    }

	public Date getMusmRecallDate() {
		return this.musmRecallDate;
	}

	public void setMusmRecallDate(Date musmRecallDate) {
		this.musmRecallDate = musmRecallDate;
	}
    public Timestamp getMusmReqTs() {
        return this.musmReqTs;
    }

    public void setMusmReqTs(Timestamp musmReqTs) {
        this.musmReqTs = musmReqTs;
    }

    public Timestamp getMusmStartTs() {
        return this.musmStartTs;
    }

    public void setMusmStartTs(Timestamp musmStartTs) {
        this.musmStartTs = musmStartTs;
    }

	public Integer getMusmStatusCd() {
		return this.musmStatusCd;
	}

	public void setMusmStatusCd(Integer musmStatusCd) {
		this.musmStatusCd = musmStatusCd;
	}

	public String getMusmSysFilename() {
		return this.musmSysFilename;
	}

	public void setMusmSysFilename(String musmSysFilename) {
		this.musmSysFilename = musmSysFilename;
	}
    public List<MslUploadErrorMuseDAO> getMslUploadErrorMuses() {
        return this.mslUploadErrorMusDAOS;
    }

    public void setMslUploadErrorMuses(List<MslUploadErrorMuseDAO> mslUploadErrorMusDAOS) {
        this.mslUploadErrorMusDAOS = mslUploadErrorMusDAOS;
    }

    public MslUploadErrorMuseDAO addMslUploadErrorMus(MslUploadErrorMuseDAO mslUploadErrorMus) {
        getMslUploadErrorMuses().add(mslUploadErrorMus);
        mslUploadErrorMus.setMslUploadSummaryMusm(this);

        return mslUploadErrorMus;
    }

    public MslUploadErrorMuseDAO removeMslUploadErrorMus(MslUploadErrorMuseDAO mslUploadErrorMus) {
        getMslUploadErrorMuses().remove(mslUploadErrorMus);
        mslUploadErrorMus.setMslUploadSummaryMusm(null);

        return mslUploadErrorMus;
    }

    public List<MslUploadStagingMustDAO> getMslUploadStagingMusts() {
        return this.mslUploadStagingMustDAOS;
    }

    public void setMslUploadStagingMusts(List<MslUploadStagingMustDAO> mslUploadStagingMustDAOS) {
        this.mslUploadStagingMustDAOS = mslUploadStagingMustDAOS;
    }

    public MslUploadStagingMustDAO addMslUploadStagingMust(MslUploadStagingMustDAO mslUploadStagingMustDAO) {
        getMslUploadStagingMusts().add(mslUploadStagingMustDAO);
        mslUploadStagingMustDAO.setMslUploadSummaryMusm(this);

        return mslUploadStagingMustDAO;
    }

    public MslUploadStagingMustDAO removeMslUploadStagingMust(MslUploadStagingMustDAO mslUploadStagingMustDAO) {
        getMslUploadStagingMusts().remove(mslUploadStagingMustDAO);
        mslUploadStagingMustDAO.setMslUploadSummaryMusm(null);

        return mslUploadStagingMustDAO;
    }

    public MslRefListMlrlDAO getMslRefListMlrlDAO() {
        return mslRefListMlrlDAO;
    }

    public void setMslRefListMlrlDAO(MslRefListMlrlDAO mslRefListMlrlDAO) {
        this.mslRefListMlrlDAO = mslRefListMlrlDAO;
    }

}