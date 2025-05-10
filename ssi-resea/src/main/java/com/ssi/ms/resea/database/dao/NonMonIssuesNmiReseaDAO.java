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
import java.sql.Timestamp;

@Entity
@Table(name = "NON_MON_ISSUES_NMI")
@Data
public class NonMonIssuesNmiReseaDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NMI_ID", unique = true, nullable = false, precision = 15, scale = 0)
    private Long nmiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_NMI_ID")
    private NonMonIssuesNmiReseaDAO parentNmiDAO;

    @Column(name = "NMI_TALLY_IND")
    private String nmiTallyInd;

    @Column(name = "NMI_SHORT_DESC_TXT")
    private String nmiShortDescTxt;

    @Column(name = "NMI_FF_REQ_IND")
    private String nmiFfReqInd;

    @Column(name = "NMI_EMPLOYER_IND")
    private String nmiEmployerInd;

    @Column(name = "NMI_SORT_ORDER")
    private Short nmiSortOrder;

    @Column(name = "NMI_AFFCTS_PMT_IND")
    private String nmiAffctsPmtInd;

    @Column(name = "NMI_DISPLAY_IND")
    private String nmiDisplayInd;

    @Column(name = "NMI_SEND_DTM_IND")
    private String nmiSendDtmInd;

    @Column(name = "NMI_REQUAL_REQ_CD")
    private Long nmiRequalReqCd;

    @Column(name = "NMI_ELIGBILITY_IND")
    private String nmiEligbilityInd;

    @Column(name = "NMI_SEP_IND")
    private String nmiSepInd;

    @Column(name = "NMI_DENY_APP_IND")
    private String nmiDenyAppInd;

    @Column(name = "NMI_HOLD_DTM_IND")
    private String nmiHoldDtmInd;

    @Column(name = "NMI_UI_IND")
    private String nmiUiInd;

    @Column(name = "NMI_TRA_IND")
    private String nmiTraInd;

    @Column(name = "NMI_ADD_TRA_IND")
    private String nmiAddTraInd;

    @Column(name = "NMI_EB_IND")
    private String nmiEbInd;

    @Column(name = "NMI_DUA_IND")
    private String nmiDuaInd;

    @Column(name = "NMI_TEUC_IND")
    private String nmiTeucInd;

    @Column(name = "NMI_EUC_IND")
    private String nmiEucInd;

    @Column(name = "NMI_TAA_IND")
    private String nmiTaaInd;

    @Column(name = "NMI_DUDA_IND")
    private String nmiDudaInd;

    @Column(name = "NMI_BTQ_NBR")
    private Long nmiBtqNbr;

    @Column(name = "NMI_CSF_BLOCK_NBR")
    private String nmiCsfBlockNbr;

    @Column(name = "NMI_DECISION_CD")
    private Long nmiDecisionCd;

    @Column(name = "NMI_PAY_CD")
    private Long nmiPayCd;

    @Column(name = "NMI_ROUTING_CD")
    private Long nmiRoutingCd;

    @Column(name = "NMI_DTM_BLOCK_CD")
    private Long nmiDtmBlockCd;

    @Column(name = "NMI_DTM_ALONE_CD")
    private Long nmiDtmAloneCd;

    @Column(name = "NMI_CHG_DT_CD")
    private Long nmiChgDtCd;

    @Column(name = "NMI_LONG_DESC_TXT")
    private String nmiLongDescTxt;

    @Column(name = "NMI_HELP_TXT")
    private String nmiHelpTxt;

    @Column(name = "NMI_EMP_FF_REQ_IND")
    private String nmiEmpFfReqInd;

    @Column(name = "NMI_MAN_CREATE_IND")
    private String nmiManCreateInd;

    @Column(name = "NMI_TEUC_AIR_IND")
    private String nmiTeucAirInd;

    @Column(name = "NMI_CREATED_BY")
    private String nmiCreatedBy;

    @Column(name = "NMI_CREATED_TS")
    private Timestamp nmiCreatedTs;

    @Column(name = "NMI_LAST_UPD_BY")
    private String nmiLastUpdBy;

    @Column(name = "NMI_LAST_UPD_TS")
    private Timestamp nmiLastUpdTs;

    @Column(name = "NMI_COMMENTS_TXT")
    private String nmiCommentsTxt;

    @Column(name = "NMI_SP_SHORT_DESC_TXT")
    private String nmiSpShortDescTxt;

    @Column(name = "NMI_DIFF_LVL_CD")
    private Short nmiDiffLvlCd;

    @Column(name = "NMI_ACTIVE_IND")
    private String nmiActiveInd;

    @Column(name = "NMI_CTRA_IND")
    private String nmiCtraInd;

    @Column(name = "NMI_FF_PHRASE")
    private String nmiFfPhrase;

    @Column(name = "NMI_FF_SP_PHRASE")
    private String nmiFfSpPhrase;

    @Column(name = "NMI_RT_FF_IND")
    private String nmiRtFfInd;

    @Column(name = "NMI_DISPLAY_ON")
    private Long nmiDisplayOn;

    @Column(name = "NMI_MAT_SEP_IND")
    private String nmiMatSepInd;

    @Column(name = "NMI_DOC_TYPE_CD")
    private Long nmiDocTypeCd;
}
