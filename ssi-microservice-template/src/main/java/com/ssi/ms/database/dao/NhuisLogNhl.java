package com.ssi.ms.database.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NHUIS_LOG_NHL")
public class NhuisLogNhl implements Serializable {

    private static final long serialVersionUID = -5668884057097440351L;

    private Long nhlId;
    private Long nhlLogType;
    private Long nhlApplnName;
    private String nhlApplnVersion;
    private String nhlModuleName;
    private String nhlProgName;
    private String nhlMethodName;
    private String nhlErrTxt;
    private String nhlErrDesc;
    private String nhlAddtnlExceptionData;
    private Long nhlStdErrCd = 0L;
    private Long nhlErrStatusCd = 0L;
    private Long fkCmtId;
    private Long fkClmId;
    private Long fkEmpId;
    private Long fkUsrId;
    private String nhl1IfkType;
    private Long nhl1IfkValue;
    private String nhl2IfkType;
    private Long nhl2IfkValue;
    private String nhl3IfkType;
    private Long nhl3IfkValue;
    private String nhlCreatedBy;
    private String nhlCreatedUsing;
    private String nhlUpdatedBy;
    private String nhlUpdatedUsing;
    private Date nhlCreatedTs;
    private Date nhlLastUpdTs;

    /**
     * @return the nhlId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NHL_ID_SEQ_GEN")
    @SequenceGenerator(name = "NHL_ID_SEQ_GEN", sequenceName = "NHL_ID_SEQ", allocationSize = 1)
    @Column(name = "NHL_ID", unique = true, nullable = false)
    public Long getNhlId() {
        return nhlId;
    }

    /**
     * @param nhlId the nhlId to set
     */
    public void setNhlId(final Long nhlId) {
        this.nhlId = nhlId;
    }

    /**
     * @return the nhlLogType
     */
    @Column(name = "NHL_LOG_TYPE")
    public Long getNhlLogType() {
        return nhlLogType;
    }

    /**
     * @param nhlLogType the nhlLogType to set
     */
    public void setNhlLogType(final Long nhlLogType) {
        this.nhlLogType = nhlLogType;
    }

    /**
     * @return the nhlApplnName
     */
    @Column(name = "NHL_APPLN_NAME")
    public Long getNhlApplnName() {
        return nhlApplnName;
    }

    /**
     * @param nhlApplnName the nhlApplnName to set
     */
    public void setNhlApplnName(final Long nhlApplnName) {
        this.nhlApplnName = nhlApplnName;
    }

    /**
     * @return the nhlApplnVersion
     */
    @Column(name = "NHL_APPLN_VERSION")
    public String getNhlApplnVersion() {
        return nhlApplnVersion;
    }

    /**
     * @param nhlApplnVersion the nhlApplnVersion to set
     */
    public void setNhlApplnVersion(final String nhlApplnVersion) {
        this.nhlApplnVersion = nhlApplnVersion;
    }

    /**
     * @return the nhlModuleName
     */
    @Column(name = "NHL_MODULE_NAME")
    public String getNhlModuleName() {
        return nhlModuleName;
    }

    /**
     * @param nhlModuleName the nhlModuleName to set
     */
    public void setNhlModuleName(final String nhlModuleName) {
        this.nhlModuleName = nhlModuleName;
    }

    /**
     * @return the nhlProgName
     */
    @Column(name = "NHL_PROG_NAME")
    public String getNhlProgName() {
        return nhlProgName;
    }

    /**
     * @param nhlProgName the nhlProgName to set
     */
    public void setNhlProgName(final String nhlProgName) {
        this.nhlProgName = nhlProgName;
    }

    /**
     * @return the nhlMethodName
     */
    @Column(name = "NHL_METHOD_NAME")
    public String getNhlMethodName() {
        return nhlMethodName;
    }

    /**
     * @param nhlMethodName the nhlMethodName to set
     */
    public void setNhlMethodName(final String nhlMethodName) {
        this.nhlMethodName = nhlMethodName;
    }

    /**
     * @return the nhlErrTxt
     */
    @Column(name = "NHL_ERR_TXT")
    public String getNhlErrTxt() {
        return nhlErrTxt;
    }

    /**
     * @param nhlErrTxt the nhlErrTxt to set
     */
    public void setNhlErrTxt(final String nhlErrTxt) {
        this.nhlErrTxt = nhlErrTxt;
    }

    /**
     * @return the nhlErrDesc
     */
    @Column(name = "NHL_ERR_DESC")
    public String getNhlErrDesc() {
        return nhlErrDesc;
    }

    /**
     * @param nhlErrDesc the nhlErrDesc to set
     */
    public void setNhlErrDesc(final String nhlErrDesc) {
        this.nhlErrDesc = nhlErrDesc;
    }

    /**
     * @return the nhlAddtnlExceptionData
     */
    @Column(name = "NHL_ADDTNL_ERR_DATA")
    public String getNhlAddtnlExceptionData() {
        return nhlAddtnlExceptionData;
    }

    /**
     * @param nhlAddtnlExceptionData the nhlAddtnlExceptionData to set
     */
    public void setNhlAddtnlExceptionData(final String nhlAddtnlExceptionData) {
        this.nhlAddtnlExceptionData = nhlAddtnlExceptionData;
    }

    /**
     * @return the nhlStdErrCd
     */
    @Column(name = "NHL_STD_ERROR_CD")
    public Long getNhlStdErrCd() {
        return nhlStdErrCd;
    }

    /**
     * @param nhlStdErrCd the nhlStdErrCd to set
     */
    public void setNhlStdErrCd(final Long nhlStdErrCd) {
        this.nhlStdErrCd = nhlStdErrCd;
    }

    /**
     * @return the nhlErrStatusCd
     */
    @Column(name = "NHL_ERR_STATUS_CD")
    public Long getNhlErrStatusCd() {
        return nhlErrStatusCd;
    }

    /**
     * @param nhlErrStatusCd the nhlErrStatusCd to set
     */
    public void setNhlErrStatusCd(final Long nhlErrStatusCd) {
        this.nhlErrStatusCd = nhlErrStatusCd;
    }

    /**
     * @return the fkCmtId
     */
    @Column(name = "FK_CMT_ID")
    public Long getFkCmtId() {
        return fkCmtId;
    }

    /**
     * @param fkCmtId the fkCmtId to set
     */
    public void setFkCmtId(final Long fkCmtId) {
        this.fkCmtId = fkCmtId;
    }

    /**
     * @return the fkClmId
     */
    @Column(name = "FK_CLM_ID")
    public Long getFkClmId() {
        return fkClmId;
    }

    /**
     * @param fkClmId the fkClmId to set
     */
    public void setFkClmId(final Long fkClmId) {
        this.fkClmId = fkClmId;
    }

    /**
     * @return the fkEmpId
     */
    @Column(name = "FK_EMP_ID")
    public Long getFkEmpId() {
        return fkEmpId;
    }

    /**
     * @param fkEmpId the fkEmpId to set
     */
    public void setFkEmpId(final Long fkEmpId) {
        this.fkEmpId = fkEmpId;
    }

    /**
     * @return the fkUsrId
     */
    @Column(name = "FK_USR_ID")
    public Long getFkUsrId() {
        return fkUsrId;
    }

    /**
     * @param fkUsrId the fkUsrId to set
     */
    public void setFkUsrId(final Long fkUsrId) {
        this.fkUsrId = fkUsrId;
    }

    /**
     * @return the nhl1IfkType
     */
    @Column(name = "NHL_1_IFK_TYPE")
    public String getNhl1IfkType() {
        return nhl1IfkType;
    }

    /**
     * @param nhl1IfkType the nhl1IfkType to set
     */
    public void setNhl1IfkType(final String nhl1IfkType) {
        this.nhl1IfkType = nhl1IfkType;
    }

    /**
     * @return the nhl1IfkValue
     */
    @Column(name = "NHL_1_IFK_VALUE")
    public Long getNhl1IfkValue() {
        return nhl1IfkValue;
    }

    /**
     * @param nhl1IfkValue the nhl1IfkValue to set
     */
    public void setNhl1IfkValue(final Long nhl1IfkValue) {
        this.nhl1IfkValue = nhl1IfkValue;
    }

    /**
     * @return the nhl2IfkType
     */
    @Column(name = "NHL_2_IFK_TYPE")
    public String getNhl2IfkType() {
        return nhl2IfkType;
    }

    /**
     * @param nhl2IfkType the nhl2IfkType to set
     */
    public void setNhl2IfkType(final String nhl2IfkType) {
        this.nhl2IfkType = nhl2IfkType;
    }

    /**
     * @return the nhl2IfkValue
     */
    @Column(name = "NHL_2_IFK_VALUE")
    public Long getNhl2IfkValue() {
        return nhl2IfkValue;
    }

    /**
     * @param nhl2IfkValue the nhl2IfkValue to set
     */
    public void setNhl2IfkValue(final Long nhl2IfkValue) {
        this.nhl2IfkValue = nhl2IfkValue;
    }

    /**
     * @return the nhl3IfkType
     */
    @Column(name = "NHL_3_IFK_TYPE")
    public String getNhl3IfkType() {
        return nhl3IfkType;
    }

    /**
     * @param nhl3IfkType the nhl3IfkType to set
     */
    public void setNhl3IfkType(final String nhl3IfkType) {
        this.nhl3IfkType = nhl3IfkType;
    }

    /**
     * @return the nhl3IfkValue
     */
    @Column(name = "NHL_3_IFK_VALUE")
    public Long getNhl3IfkValue() {
        return nhl3IfkValue;
    }

    /**
     * @param nhl3IfkValue the nhl3IfkValue to set
     */
    public void setNhl3IfkValue(final Long nhl3IfkValue) {
        this.nhl3IfkValue = nhl3IfkValue;
    }

    /**
     * @return the nhlCreatedBy
     */
    @Column(name = "NHL_CREATED_BY")
    public String getNhlCreatedBy() {
        return nhlCreatedBy;
    }

    /**
     * @param nhlCreatedBy the nhlCreatedBy to set
     */
    public void setNhlCreatedBy(final String nhlCreatedBy) {
        this.nhlCreatedBy = nhlCreatedBy;
    }

    /**
     * @return the nhlCreatedUsing
     */
    @Column(name = "NHL_CREATED_USING")
    public String getNhlCreatedUsing() {
        return nhlCreatedUsing;
    }

    /**
     * @param nhlCreatedUsing the nhlCreatedUsing to set
     */
    public void setNhlCreatedUsing(final String nhlCreatedUsing) {
        this.nhlCreatedUsing = nhlCreatedUsing;
    }

    /**
     * @return the nhlUpdatedBy
     */
    @Column(name = "NHL_LAST_UPD_BY")
    public String getNhlUpdatedBy() {
        return nhlUpdatedBy;
    }

    /**
     * @param nhlUpdatedBy the nhlUpdatedBy to set
     */
    public void setNhlUpdatedBy(final String nhlUpdatedBy) {
        this.nhlUpdatedBy = nhlUpdatedBy;
    }

    /**
     * @return the nhlUpdatedUsing
     */
    @Column(name = "NHL_LAST_UPD_USING")
    public String getNhlUpdatedUsing() {
        return nhlUpdatedUsing;
    }

    /**
     * @param nhlUpdatedUsing the nhlUpdatedUsing to set
     */
    public void setNhlUpdatedUsing(final String nhlUpdatedUsing) {
        this.nhlUpdatedUsing = nhlUpdatedUsing;
    }

    /**
     * @return the nhlCreatedTs
     */
    @Column(name = "NHL_CREATED_TS")
    public Date getNhlCreatedTs() {
        return nhlCreatedTs;
    }

    /**
     * @param nhlCreatedTs the nhlCreatedTs to set
     */
    public void setNhlCreatedTs(final Date nhlCreatedTs) {
        this.nhlCreatedTs = nhlCreatedTs;
    }

    /**
     * @return the nhlLastUpdTs
     */
    @Column(name = "NHL_LAST_UPD_TS")
    public Date getNhlLastUpdTs() {
        return nhlLastUpdTs;
    }

    /**
     * @param nhlLastUpdTs the nhlLastUpdTs to set
     */
    public void setNhlLastUpdTs(final Date nhlLastUpdTs) {
        this.nhlLastUpdTs = nhlLastUpdTs;
    }

    @Override
    public final boolean equals(final Object obj) {
        Boolean result = true;
        if (obj == null) {
            result = false;
        }
        if (this != null && this.getClass() != obj.getClass()) {
            result = false;
        }
        final NhuisLogNhl other = (NhuisLogNhl) obj;
        if (this.getNhlId() != other.getNhlId()
                && (this.getNhlId() == null || other.getNhlId() == null || !this.getNhlId().equals(other.getNhlId()))) {
            result = false;
        }
        return result;
    }

    @Override
    public final int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.getNhlId() != null ? this.getNhlId().hashCode() : 0);
        return hash;
    }
}
