package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WS_ACTIVITIES_WSA")
@Data
public class WsActivitiesWsaDAO {
    @Id
    @Column(name = "WSA_ID")
    private Long wsaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CCA_ID")
    private CcApplnCcaDAO ccaDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_WSAC_ID", nullable = true)
    private WsActivitiesConfWsacDAO wsacDAO;

    @Column(name = "WSA_ACTIVITIES_ANS", length = 1)
    private String wsaActivitiesAns;
}
