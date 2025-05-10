package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the VW_CCASE_HEADER database table.
 *
 */
@Entity
@Table(name="vw_ccase_header_tbl")
@Data
public class VwCcaseHeaderDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name="BKT_STATUS_N_DT")
    private String bktStatusNDt;

    @Column(name="CASE_AGE")
    private Long caseAge;

    @Column(name="CASE_CHARACTERISTICS")
    private String caseCharacteristics;

    @Id
    @Column(name="CASE_NO")
    private Long caseNo;

    @Temporal(TemporalType.DATE)
    @Column(name="CASE_OPEN_DT")
    private Date caseOpenDt;

    @Temporal(TemporalType.DATE)
    @Column(name="CASE_ORIG_OPEN_DT")
    private Date caseOrigOpenDt;

    @Column(name="CASE_PRIORITY")
    private Long casePriority;

    @Column(name="CASE_PRIORITY_DESC")
    private String casePriorityDesc;

    @Column(name="CASE_STATUS")
    private Long caseStatus;

    @Column(name="CASE_STATUS_DESC")
    private String caseStatusDesc;

    @Column(name="CMT_ADDRESS")
    private String cmtAddress;

    @Column(name="CMT_EMAIL_ADDRESS")
    private String cmtEmailAddress;

    @Column(name="CMT_ID")
    private Long cmtId;

    @Column(name="CMT_NAME")
    private String cmtName;

    @Column(name="CMT_PHONES")
    private String cmtPhones;

    @Column(name="CURR_FILING")
    private String currFiling;

    private String felony;

    private String frd;

    @Column(name="FULL_SSN")
    private String fullSsn;

    @Temporal(TemporalType.DATE)
    @Column(name="LAST_COLL_DT")
    private Date lastCollDt;

    @Column(name="APPEAL")
    private String appeal;

    @Column(name="NEXT_FOLLOWUP_DATE")
    private String nextFollowupDate;

    @Column(name="NON_FRD")
    private String nonFrd;

    @Column(name="NON_FRD_EARNINGS")
    private String nonFrdEarnings;

    @Column(name="OP_BAL")
    private BigDecimal opBal;

    @Column(name="OP_COLLECTED")
    private BigDecimal opCollected;

    @Column(name="SSN_LAST_4")
    private String ssnLast4;

    @Column(name="STAFF_ID")
    private Long staffId;

    @Column(name="STAFF_NAME")
    private String staffName;
}