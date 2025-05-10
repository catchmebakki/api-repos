package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BRIERI_SESSION_BSS")
@Data
public class BrieriSessionBssDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BSS_ID", unique = true, nullable = false)
    private Long bssId;

    @Temporal(TemporalType.DATE)
    @Column(name = "BSS_START_DATE")
    private Date bssStartDate;

    @Column(name = "BSS_STATUS_IND", length = 1, nullable = false)
    private String bssStatusInd;

    @ManyToOne
    @JoinColumn(name = "BSS_FK_BRC_ID")
    private BrieriRoomBrcDAO brcDAO;

    @Column(name = "BSS_TYPE_CD")
    private Long bssTypeCd;
}
