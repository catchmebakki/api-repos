package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ISSUE_DECISION_DEC")
@Data
public class IssueDecisionDecDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "issueDecisionDecDAO")
    ReseaIssueIdentifiedRsiiDAO rsiiDAO;
    @Id
    @Column(name = "DEC_ID", unique = true, nullable = false)
    private Long decId;
    @Column(name = "DEC_NBR", length = 15)
    private Long decNbr;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO clmDAO;
    @Temporal(TemporalType.DATE)
    @Column(name = "DEC_BEGIN_DT")
    private Date decBeginDt;
    @Temporal(TemporalType.DATE)
    @Column(name = "DEC_END_DT")
    private Date decEndDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_NMI_ID")
    private NonMonIssuesNmiReseaDAO nmiDAO;
    @Column(name = "FK_MON_ID", length = 15)
    private Long fkMonId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEC_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO decStatusCdAlv;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEC_DECISION_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO decDecisionCdAlv;
    @Temporal(TemporalType.DATE)
    @Column(name = "DEC_DETECTION_DT")
    private Date decDetectionDt;
    @Temporal(TemporalType.DATE)
    @Column(name = "DEC_CREATED_TS")
    private Date decCreatedTs;
}
