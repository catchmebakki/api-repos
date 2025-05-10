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
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LOF_STAFF_LSF")
@Data
public class LofStaffLsfDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LSF_ID", unique = true, nullable = false)
    private Long lsfId;

    @Temporal(TemporalType.DATE)
    @Column(name = "LSF_EFFECTIVE_DT")
    Date lsfEffectiveDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "LSF_EXPIRATION_DT")
    Date lsfExpirationDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_STF_ID")
    private StaffStfDAO stfDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_LOF_ID")
    private LocalOfficeLofDAO lofDAO;
}
