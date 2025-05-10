package com.ssi.ms.common.database.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Praveenraja Paramsivam
 * The persistent class for the NHUIS_LOG_NHL database table.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "NHUIS_LOG_NHL")
public class NhuisLogNhlDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NHL_ID_SEQ_GEN")
    @SequenceGenerator(name = "NHL_ID_SEQ_GEN", sequenceName = "NHL_ID_SEQ", allocationSize = 1)
    @Id
    @Column(name = "NHL_ID", unique = true, nullable = false, precision = 15)
    private Long nhlId;

    @Column(name = "FK_CLM_ID", precision = 15)
    private Long fkClmId;

    @Column(name = "FK_CMT_ID", precision = 15)
    private Long fkCmtId;

    @Column(name = "FK_USR_ID", precision = 15)
    private Long fkUsrId;

    @Column(name = "NHL_1_IFK_TYPE", length = 6)
    private String nhl1IfkType;

    @Column(name = "NHL_1_IFK_VALUE", precision = 15)
    private Long nhl1IfkValue;

    @Column(name = "NHL_2_IFK_TYPE", length = 6)
    private String nhl2IfkType;

    @Column(name = "NHL_2_IFK_VALUE", precision = 15)
    private Long nhl2IfkValue;

    @Column(name = "NHL_3_IFK_TYPE", length = 6)
    private String nhl3IfkType;

    @Column(name = "NHL_3_IFK_VALUE", precision = 15)
    private Long nhl3IfkValue;

    @Lob
    @Column(name = "NHL_ADDTNL_ERR_DATA")
    private String nhlAddtnlErrData;

    @Column(name = "NHL_APPLN_NAME", nullable = false, precision = 4)
    private Integer nhlApplnName;

    @Column(name = "NHL_APPLN_VERSION", nullable = false, length = 16)
    private String nhlApplnVersion;

    @Column(name = "NHL_CREATED_BY", nullable = false, length = 10)
    private String nhlCreatedBy;

    @Column(name = "NHL_CREATED_TS", nullable = false)
    private Timestamp nhlCreatedTs;

    @Column(name = "NHL_CREATED_USING", nullable = false, length = 50)
    private String nhlCreatedUsing;

    @Column(name = "NHL_ERR_DESC", nullable = false, length = 4000)
    private String nhlErrDesc;

    @Column(name = "NHL_ERR_STATUS_CD", nullable = false, precision = 4)
    private Integer nhlErrStatusCd;

    @Column(name = "NHL_ERR_TXT", nullable = false, length = 4000)
    private String nhlErrTxt;

    @Column(name = "NHL_FIX_REMARK_TXT", length = 4000)
    private String nhlFixRemarkTxt;

    @Column(name = "NHL_HARVEST_ITEM_TXT", length = 256)
    private String nhlHarvestItemTxt;

    @Column(name = "NHL_LAST_UPD_BY", nullable = false, length = 10)
    private String nhlLastUpdBy;

    @Column(name = "NHL_LAST_UPD_TS", nullable = false)
    private Timestamp nhlLastUpdTs;

    @Column(name = "NHL_LAST_UPD_USING", nullable = false, length = 50)
    private String nhlLastUpdUsing;

    @Column(name = "NHL_LOG_TYPE", nullable = false, precision = 4)
    private Integer nhlLogType;

    @Column(name = "NHL_METHOD_NAME", nullable = false, length = 100)
    private String nhlMethodName;

    @Column(name = "NHL_MODULE_NAME", nullable = false, length = 100)
    private String nhlModuleName;

    @Column(name = "NHL_PROG_NAME", nullable = false, length = 50)
    private String nhlProgName;

    @Column(name = "NHL_STD_ERROR_CD", nullable = false, precision = 5)
    private Integer nhlStdErrorCd;

    @Column(name = "FK_EMP_ID")
    private Long employerId;

}