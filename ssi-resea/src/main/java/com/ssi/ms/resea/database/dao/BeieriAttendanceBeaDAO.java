package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "BRIERI_ATTENDANCE_BEA")
@Data
public class BeieriAttendanceBeaDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BEA_ID", unique = true, nullable = false)
    private Long beaId;

    @ManyToOne
    @JoinColumn(name = "BEA_FK_BSS_ID")
    private BrieriSessionBssDAO bssDAO;

    @ManyToOne
    @JoinColumn(name = "BEA_FK_CLM_ID")
    private ClaimClmDAO clmDAO;

    @Column(name = "BEA_STATUS_IND", length = 1)
    private String beaStatusInd;

    @Column(name = "BEA_ATTENDANCE_CD")
    private Long beaAttendanceCd;
}
