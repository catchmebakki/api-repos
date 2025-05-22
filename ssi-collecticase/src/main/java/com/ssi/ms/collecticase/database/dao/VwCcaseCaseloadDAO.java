package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the VW_CCASE_CASELOAD database table.
 */
@Entity
@Table(name = "VW_CCASE_CASELOAD")
@Data
public class VwCcaseCaseloadDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "CASE_AGE")
    private Long caseAge;

    @Column(name = "CASE_CHARACTERISTICS")
    private String caseCharacteristics;

    @Id
    @Column(name = "CASE_NO")
    private Long caseNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "CASE_OPEN_DT")
    private Date caseOpenDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "CASE_ORIG_OPEN_DT")
    private Date caseOrigOpenDt;

    @Column(name = "CASE_PRIORITY")
    private Long casePriority;

    @Column(name = "CASE_PRIORITY_DESC")
    private String casePriorityDesc;

    @Column(name = "CLAIMANT_NAME")
    private String claimantName;

    @Column(name = "CMT_ID")
    private Long cmtId;

    @Column(name = "CMT_SSN")
    private String cmtSsn;

    private String frd;

    @Column(name = "MOST_RECENT_REMEDY")
    private String mostRecentRemedy;

    @Column(name = "NEW_CASE")
    private String newCase;

    @Column(name = "NEXT_FOLLOWUP_DATE")
    private String nextFollowupDate;

    @Column(name = "NF_EARNINGS")
    private String nfEarnings;

    @Column(name = "FRD_NF_EARNING")
    private String frdNfEarnings;

    @Column(name = "NON_FRD")
    private String nonFrd;

    @Column(name = "BANKRUPT")
    private String bankrupt;

    @Column(name = "OP_BAL")
    private BigDecimal opBal;

    @Column(name = "OVERDUE")
    private String overDue;

    @Column(name = "OVERDUE_ONE_WEEK")
    private String overDueOneWeek;

    @Column(name = "OVERDUE_ONE_MONTH")
    private String overDueOneMonth;

    @Column(name = "DUE_TODAY")
    private String dueToday;

    @Column(name = "DUE_WITHIN_WEEK")
    private String dueWithinWeek;

    @Column(name = "DUE_WITHIN_MONTH")
    private String dueWithinMonth;

    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "STAFF_NAME")
    private String staffName;

    @Column(name = "ALERT_ACTIVITY")
    private String alertActivity;
}