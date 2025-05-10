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
@Table(name = "REA_ATTENDANCE_RAT")
@Data
public class ReaAttendanceRatDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RAT_ID", unique = true, nullable = false)
    private Long ratId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RAT_FK_CLM_ID")
    private ClaimClmDAO clmDAO;

    @Column(name = "RAT_ATTENDANCE_CD", length = 30)
    private Long ratAttendanceCd;

    @Column(name = "RAT_FK_BSS_ID", length = 15)
    private Long ratFkBssId;

    @Column(name = "RAT_FK_RSS_ID", length = 15, nullable = false)
    private Long ratFkRssId;
}
