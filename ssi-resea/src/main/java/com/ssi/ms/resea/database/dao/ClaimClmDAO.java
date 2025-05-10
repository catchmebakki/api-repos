package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CLAIM_CLM")
@Data
public class ClaimClmDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the claimant's ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CLM_ID_SEQ_GEN")
    @SequenceGenerator(name = "CLM_ID_SEQ_GEN", sequenceName = "CLM_ID_SEQ", allocationSize = 1)
    @Column(name = "CLM_ID", unique = true, nullable = false, length = 15)
    private Long clmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CMT_ID")
    private ClaimantCmtDAO claimantDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "CLM_BEN_YR_END_DT")
    private Date clmBenYrEndDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "CLM_EFFECTIVE_DT")
    private Date clmEffectiveDt;
}
