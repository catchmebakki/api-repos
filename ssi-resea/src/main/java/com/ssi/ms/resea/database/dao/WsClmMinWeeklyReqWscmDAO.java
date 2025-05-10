package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "WS_CLM_MIN_WEEKLY_REQ_WSCM")
@Data
public class WsClmMinWeeklyReqWscmDAO {
    @Id
    @Column(name = "WSCM_ID")
    private Long wscmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO clmDAO;

    @Column(name = "WSCM_MIN_REQ", length = 2)
    private Short wscmMinReq;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSCM_START_DT")
    private Date wscmStartDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSCM_END_DT")
    private Date wscmEndDt;
}
