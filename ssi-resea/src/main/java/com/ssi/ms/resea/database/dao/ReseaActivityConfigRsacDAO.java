package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RESEA_ACTIVITY_CONFIG_RSAC")
@Data
public class ReseaActivityConfigRsacDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RSAC_ID", unique = true, nullable = false, length = 15)
    private Long rsacId;

    @Column(name = "RSAC_ACTIVITY_CD")
    private Long rsacActivityCd;

    @Column(name = "RSAC_EFFECTIVE_DT")
    private Date rsacEffectiveDt;

    @Column(name = "RSAC_EFF_UNTIL_DT")
    private Date rsacEffUntilDt;

    @Column(name = "RSAC_TEMPLATE_PAGE", length = 3)
    private String rsacTemplatePage;

    @Column(name = "RSAC_USER_INITIATED_CD")
    private Long rsacUserInitiatedCd;

    @Column(name = "RSAC_NEW_CASE_STAGE_CD")
    private Long rsacNewCaseStageCd;

    @Column(name = "RSAC_NEW_CASE_STATUS_CD")
    private Long rsacNewCaseStatusCd;

    @Column(name = "RSAC_ALLOW_FOLLOW_UP_IND", length = 1)
    private String rsacAllowFollowUpInd;

    @Column(name = "RSAC_ALLOW_NOTICE_IND", length = 1)
    private String rsacAllowNoticeInd;

    @Column(name = "RSAC_ALLOW_ON_TERM_IND", length = 1)
    private String rsacAllowOnTermInd;

    @Column(name = "RSAC_ACTIVITY_IFK_CD", length = 4)
    private Long rsacActivityIfkCd;

    @Column(name = "RSAC_CREATED_BY", length = 10)
    private String rsacCreatedBy;

    @Column(name = "RSAC_CREATED_USING", length = 50)
    private String rsacCreatedUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSAC_CREATED_TS")
    private Date rsacCreatedTs;

    @Column(name = "RSAC_LAST_UPD_BY", length = 10)
    private String rsacLastUpdBy;

    @Column(name = "RSAC_LAST_UPD_USING", length = 50)
    private String rsacLastUpdUsing;

    @Temporal(TemporalType.DATE)
    @Column(name = "RSAC_LAST_UPD_TS")
    private Date rsacLastUpdTs;
}
