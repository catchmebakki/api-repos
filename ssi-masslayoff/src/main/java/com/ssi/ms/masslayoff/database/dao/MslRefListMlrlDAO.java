package com.ssi.ms.masslayoff.database.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MSL_REF_LIST_MLRL database table.
 */
@Entity
@Table(name = "MSL_REF_LIST_MLRL")
public class MslRefListMlrlDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the MSL_REF_LIST_MLRL table primary ID.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MLRL_ID")
    private Long mlrlId;

    @Column(name = "MLRL_CREATED_BY")
    private String mlrlCreatedBy;

    @Column(name = "MLRL_CREATED_TS")
    private Timestamp mlrlCreatedTs;

    @Column(name = "MLRL_CREATED_USING")
    private String mlrlCreatedUsing;

    @Column(name = "MLRL_DI_IND")
    private String mlrlDiInd;

    @Column(name = "MLRL_EMP_AC_LOC")
    private String mlrlEmpAcLoc;

    @Column(name = "MLRL_EMP_AC_NUM")
    private String mlrlEmpAcNum;

    @Column(name = "MLRL_LAST_UPD_BY")
    private String mlrlLastUpdBy;

    @Column(name = "MLRL_LAST_UPD_TS")
    private Timestamp mlrlLastUpdTs;

    @Column(name = "MLRL_LAST_UPD_USING")
    private String mlrlLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "MLRL_MSL_DATE")
    private Date mlrlMslDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "MLRL_MSL_EFF_DATE")
    private Date mlrlMslEffDate;

    @Column(name = "MLRL_MSL_NUM")
    private Long mlrlMslNum;

    @Temporal(TemporalType.DATE)
    @Column(name = "MLRL_RECALL_DATE")
    private Date mlrlRecallDate;

	@Column(name="MLRL_REMARK_TXT")
	private String mlrlRemarkTxt;

	@Column(name="MLRL_STATUS_CD")
	private Integer mlrlStatusCd;
	
	@OneToMany
	private List<MslUploadSummaryMusmDAO> musmDAOs;

    //bi-directional many-to-one association to MslEntryCmtMlec
    @OneToMany(mappedBy = "mslRefListMlrlDAO")
    private List<MslEntryCmtMlecDAO> mslEntryCmtMlecDAOS;

    public MslRefListMlrlDAO() {
    }

    public MslRefListMlrlDAO(Long mlrlMslNum) {
		this.mlrlMslNum = mlrlMslNum;
	}

	public Long getMlrlId() {
        return this.mlrlId;
    }

    public void setMlrlId(Long mlrlId) {
        this.mlrlId = mlrlId;
    }

    public String getMlrlCreatedBy() {
        return this.mlrlCreatedBy;
    }

    public void setMlrlCreatedBy(String mlrlCreatedBy) {
        this.mlrlCreatedBy = mlrlCreatedBy;
    }

    public Timestamp getMlrlCreatedTs() {
        return this.mlrlCreatedTs;
    }

    public void setMlrlCreatedTs(Timestamp mlrlCreatedTs) {
        this.mlrlCreatedTs = mlrlCreatedTs;
    }

    public String getMlrlCreatedUsing() {
        return this.mlrlCreatedUsing;
    }

    public void setMlrlCreatedUsing(String mlrlCreatedUsing) {
        this.mlrlCreatedUsing = mlrlCreatedUsing;
    }

    public String getMlrlDiInd() {
        return this.mlrlDiInd;
    }

    public void setMlrlDiInd(String mlrlDiInd) {
        this.mlrlDiInd = mlrlDiInd;
    }

    public String getMlrlEmpAcLoc() {
        return this.mlrlEmpAcLoc;
    }

    public void setMlrlEmpAcLoc(String mlrlEmpAcLoc) {
        this.mlrlEmpAcLoc = mlrlEmpAcLoc;
    }

    public String getMlrlEmpAcNum() {
        return this.mlrlEmpAcNum;
    }

    public void setMlrlEmpAcNum(String mlrlEmpAcNum) {
        this.mlrlEmpAcNum = mlrlEmpAcNum;
    }

    public String getMlrlLastUpdBy() {
        return this.mlrlLastUpdBy;
    }

    public void setMlrlLastUpdBy(String mlrlLastUpdBy) {
        this.mlrlLastUpdBy = mlrlLastUpdBy;
    }

    public Timestamp getMlrlLastUpdTs() {
        return this.mlrlLastUpdTs;
    }

    public void setMlrlLastUpdTs(Timestamp mlrlLastUpdTs) {
        this.mlrlLastUpdTs = mlrlLastUpdTs;
    }

    public String getMlrlLastUpdUsing() {
        return this.mlrlLastUpdUsing;
    }

    public void setMlrlLastUpdUsing(String mlrlLastUpdUsing) {
        this.mlrlLastUpdUsing = mlrlLastUpdUsing;
    }

    public Date getMlrlMslDate() {
        return this.mlrlMslDate;
    }

    public void setMlrlMslDate(Date mlrlMslDate) {
        this.mlrlMslDate = mlrlMslDate;
    }

    public Date getMlrlMslEffDate() {
        return this.mlrlMslEffDate;
    }

    public void setMlrlMslEffDate(Date mlrlMslEffDate) {
        this.mlrlMslEffDate = mlrlMslEffDate;
    }

    public Long getMlrlMslNum() {
        return this.mlrlMslNum;
    }

    public void setMlrlMslNum(long mlrlMslNum) {
        this.mlrlMslNum = mlrlMslNum;
    }

    public Date getMlrlRecallDate() {
        return this.mlrlRecallDate;
    }

    public void setMlrlRecallDate(Date mlrlRecallDate) {
        this.mlrlRecallDate = mlrlRecallDate;
    }

	public String getMlrlRemarkTxt() {
		return this.mlrlRemarkTxt;
	}

	public void setMlrlRemarkTxt(String mlrlRemarkTxt) {
		this.mlrlRemarkTxt = mlrlRemarkTxt;
	}
    public Integer getMlrlStatusCd() {
        return this.mlrlStatusCd;
    }

    public void setMlrlStatusCd(Integer mlrlStatusCd) {
        this.mlrlStatusCd = mlrlStatusCd;
    }

    public List<MslEntryCmtMlecDAO> getMslEntryCmtMlecs() {
        return this.mslEntryCmtMlecDAOS;
    }

    public void setMslEntryCmtMlecs(List<MslEntryCmtMlecDAO> mslEntryCmtMlecDAOS) {
        this.mslEntryCmtMlecDAOS = mslEntryCmtMlecDAOS;
    }

    public MslEntryCmtMlecDAO addMslEntryCmtMlec(MslEntryCmtMlecDAO mslEntryCmtMlecDao) {
        getMslEntryCmtMlecs().add(mslEntryCmtMlecDao);
        mslEntryCmtMlecDao.setMslRefListMlrl(this);

        return mslEntryCmtMlecDao;
    }

    public MslEntryCmtMlecDAO removeMslEntryCmtMlec(MslEntryCmtMlecDAO mslEntryCmtMlecDao) {
        getMslEntryCmtMlecs().remove(mslEntryCmtMlecDao);
        mslEntryCmtMlecDao.setMslRefListMlrl(null);

        return mslEntryCmtMlecDao;
    }

	public List<MslUploadSummaryMusmDAO> getMusmDAO() {
		return musmDAOs;
	}

	public void setMusmDAO(List<MslUploadSummaryMusmDAO> musmDAO) {
		this.musmDAOs = musmDAO;
	}

}