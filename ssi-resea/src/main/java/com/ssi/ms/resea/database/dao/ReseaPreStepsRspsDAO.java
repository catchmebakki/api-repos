package com.ssi.ms.resea.database.dao;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "RESEA_PRE_STEP_RSPS")
@Data
@Getter
@Setter
public class ReseaPreStepsRspsDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RSPS_SEQ_GEN")
    @SequenceGenerator(name = "RSPS_SEQ_GEN", sequenceName = "RSPS_ID_SEQ", allocationSize = 1)
    @Column(name = "RSPS_ID", unique = true)
    private Long rspsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_CLM_ID")
    private ClaimClmDAO claimClmDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @Column(name = "FK_WSCM_ID", length = 15)
    private Long fkWscmId;

    @Column(name = "FK_BEA_ID", length = 15)
    private Long fkBeaId;

    @Column(name = "RSPS_ITEM_TYPE_CD")
    private Long rspsItemTypeCd;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSPS_ITEM_TS")
    private Date rspsItemTs;

    @Column(name = "RSPS_SCORE")
    private Double rspsScore;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSPS_ORIENTATION_DT")
    private Date rspsOrientationDt;

    @Column(name = "RSPS_SYS_NOTES", length = 2000)
    private String rspsSysNotes;

    @Column(name = "RSPS_CREATED_BY", length = 10)
    private String rspsCreatedBy;

    @Column(name = "RSPS_CREATED_USING", length = 50)
    private String rspsCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSPS_CREATED_TS")
    private Date rspsCreatedTs;

    @Column(name = "RSPS_LAST_UPD_BY", length = 10)
    private String rspsLastUpdBy;

    @Column(name = "RSPS_LAST_UPD_USING", length = 50)
    private String rspsLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSPS_LAST_UPD_TS")
    private Date rspsLastUpdTs;

}
