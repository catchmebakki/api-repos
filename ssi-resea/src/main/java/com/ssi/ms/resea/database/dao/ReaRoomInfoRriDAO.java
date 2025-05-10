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
@Table(name = "REA_ROOM_INFO_RRI")
@Data
public class ReaRoomInfoRriDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RRI_ID", unique = true, nullable = false)
    private Long rriId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RRI_FK_LOF_ID")
    private LocalOfficeLofDAO lofDAO;
}
