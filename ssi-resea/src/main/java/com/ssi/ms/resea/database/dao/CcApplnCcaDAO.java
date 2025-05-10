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
import java.util.Date;

@Entity
@Table(name = "CC_APPLN_CCA")
@Data
public class CcApplnCcaDAO {
    /**
     * Primary key representing the claimant's ID.
     */
    @Id
    @Column(name = "CCA_ID")
    private Long ccaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO clmDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "CCA_WEEK_ENDING_DT")
    private Date ccaWeekEndingDt;
}
