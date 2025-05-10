package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "BRIERI_ROOM_BRC")
@Data
public class BrieriRoomBrcDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BRC_ID", unique = true, nullable = false)
    private Long brcId;

    @Column(name = "BRC_FK_LOF_ID", length = 15, nullable = false)
    private Long brcFkLofId;

}
