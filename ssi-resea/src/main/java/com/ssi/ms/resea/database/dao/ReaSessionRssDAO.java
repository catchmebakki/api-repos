package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "REA_SESSION_RSS")
@Data
public class ReaSessionRssDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RSS_ID", unique = true, nullable = false)
    private Long rssId;

    @Column(name = "RSS_SESSION_TYPE", length = 4, nullable = false)
    private Integer rssSessionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RSS_FK_RRI_ID")
    private ReaRoomInfoRriDAO rriDAO;
}
