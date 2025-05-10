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
@Table(name = "RESEA_CASE_NOTE_RSCN")
@Data
public class ReseaCaseNoteRscnDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RSCN_ID", unique = true, nullable = false)
    private Long rscnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCS_ID")
    private ReseaCaseRscsDAO rscsDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RSCA_ID")
    private ReseaCaseActivityRscaDAO rscaDAO;

    @Column(name = "RSCN_NOTE_CATEGORY_CD", length = 4)
    private Long rscnNoteCategoryCd;

    @Column(name = "RSCN_NOTE", length = 4000)
    private String rscnNote;

    @Column(name = "RSCN_SHOW_IN_NHUIS_IND", length = 1)
    private String rscnShowInNhuisInd;

    @Column(name = "RSCN_CREATED_BY", length = 10)
    private String rscnCreatedBy;

    @Column(name = "RSCN_CREATED_USING", length = 50)
    private String rscnCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCN_CREATED_TS")
    private Date rscnCreatedTs;

    @Column(name = "RSCN_LAST_UPD_BY", length = 10)
    private String rscnLastUpdBy;

    @Column(name = "RSCN_LAST_UPD_USING", length = 50)
    private String rscnLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSCN_LAST_UPD_TS")
    private Date rscnLastUpdTs;
}
