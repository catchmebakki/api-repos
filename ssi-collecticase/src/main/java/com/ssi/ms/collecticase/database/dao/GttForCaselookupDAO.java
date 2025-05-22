package com.ssi.ms.collecticase.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

/**
 * The persistent class for the GTT_FOR_CASELOOKUP database table.
 */
@Entity
@Table(name = "GTT_FOR_CASELOOKUP")
@Data
public class GttForCaselookupDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ASSIGNED_TO")
    private String assignedTo;

    @Id
    @Column(name = "CASE_NUM")
    private Long caseNum;

    @Column(name = "CASE_STATUS_DESC")
    private String caseStatusDesc;

    @Column(name = "CMT_NAME")
    private String cmtName;

    @Column(name = "CMT_SSN")
    private String cmtSsn;

    @Column(name = "LAST_REMEDY")
    private String lastRemedy;

    @Column(name = "NEXT_FOLLOW_UP")
    private String nextFollowUp;

    @Column(name = "OP_TYPE")
    private String opType;

    private String priority;
}