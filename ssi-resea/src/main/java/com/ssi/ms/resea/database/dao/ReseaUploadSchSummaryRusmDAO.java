package com.ssi.ms.resea.database.dao;

import com.ssi.ms.common.database.dao.UserDAO;
import lombok.Data;
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
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the RESEA_UPLOAD_SCH_SUMMARY_RUSM database table.
 */
@Entity
@Table(name = "RESEA_UPLOAD_SCH_SUMMARY_RUSM")
@Data
public class ReseaUploadSchSummaryRusmDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the RESEA_UPLOAD_SCH_SUMMARY_RUSM table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RUSM_ID_SEQ_GEN")
    @SequenceGenerator(name = "RUSM_ID_SEQ_GEN", sequenceName = "RUSM_ID_SEQ", allocationSize = 1)
    @Column(name = "RUSM_ID", unique = true, nullable = false, length = 15)
    private Long rusmId;

    //bi-directional many-to-one association to StaffStf
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_USR_ID")
    private UserDAO userDAO;

    //bi-directional many-to-one association to LocalOfficeLof
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_RUCS_ID")
    private ReseaUploadCmSchRucsDAO rucsDAO;

    @Column(name = "RUSM_FILENAME")
    private String rusmFilename;

    @Column(name = "RUSM_SYS_FILENAME")
    private String rusmSysFilename;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_REQ_TS")
    private Date rusmReqTs;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_START_TS")
    private Date rusmStartTs;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_END_TS")
    private Date rusmEndTs;

    @Column(name = "RUSM_CASE_MANAGER", length = 60)
    private String rusmCaseManager;

    @Column(name = "RUSM_LOCAL_OFFICE", length = 40)
    private String rusmLocalOffice;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_EFFECTIVE_DT")
    private Date rusmEffectiveDt;

    @Column(name = "RUSM_NUM_RECS")
    private Integer rusmNumRecs;

    @Column(name = "RUSM_NUM_ERRS")
    private Integer rusmNumErrs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUSM_STATUS_CD")
    @NotFound(action = NotFoundAction.IGNORE)
    private AllowValAlvDAO rusmStatusCdAlv;

    @Column(name = "RUSM_ERROR_DESC")
    private String rusmErrorDesc;

    @Column(name = "RUSM_CREATED_BY")
    private String rusmCreatedBy;

    @Column(name = "RUSM_CREATED_USING")
    private String rusmCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_CREATED_TS")
    private Date rusmCreatedTs;

    @Column(name = "RUSM_LAST_UPD_BY")
    private String rusmLastUpdBy;

    @Column(name = "RUSM_LAST_UPD_USING")
    private String rusmLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUSM_LAST_UPD_TS")
    private Date rusmLastUpdTs;
}