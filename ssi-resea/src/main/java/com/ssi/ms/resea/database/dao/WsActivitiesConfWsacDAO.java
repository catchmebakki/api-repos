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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "WS_ACTIVITIES_CONF_WSAC")
@Data
public class WsActivitiesConfWsacDAO {
    @Id
    @Column(name = "WSAC_ID")
    private Long wsacId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WSAC_TYPE_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO wsacTypeCdALV;

    @Column(name = "FK_WSAC_ID", length = 15)
    private Long fkWsacId;

    @Column(name = "WSAC_ACTIVITIES_DESC_TXT", length = 200)
    private String wsacActivitiesDescTxt;

    @Column(name = "WSAC_ACTIVITIES_SP_DESC_TXT", length = 200)
    private String wsacActivitiesSpDescTxt;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSAC_EFF_FROM_DT")
    private Date wsacEffFromDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "WSAC_EFF_TO_DT")
    private Date wsacEffToDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WSAC_ACTIVITY_LEVEL_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO wsacActivityLevelCdALV;
}
