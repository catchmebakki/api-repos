package com.ssi.ms.masslayoff.database.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
************************************************************************************************
*                     Modification Log                                                        *
************************************************************************************************
*
*  	Date            Developer           Defect  Description of Change
*  	----------      ---------           ------  ---------------------
*	12/22/2023		Sitaram				SV225644 - UD-231222-Mass Layoff status is not updating in NHUIS to incomplete
** 
************************************************************************************************
*/

/**
 * The persistent class for the MSL_REF_LIST_MLRL database table.
 */
@Entity
@Data
@NoArgsConstructor
@NamedStoredProcedureQuery(
        name = "callAJI610",
        procedureName = "AJI610",
        parameters = {
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_emp_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_clm_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_nmi_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_mon_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_dec_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_fk_ldc_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_detection_dt", type = Date.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_status_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_decision_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_decision_dt", type = Date.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_begin_dt", type = Date.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_end_dt", type = Date.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_multi_cmt_ind", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_source_ifk_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_source_ifk", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_origin_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_mrsn_dtm_ifk", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_bam_ind", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_send_ff_ind", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_hard_copy_ind", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_modified_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_turn_point_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_bw_dtm_ifk", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_adjudicated_by", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_adj_lof_ifk", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_assgnd_lof_ifk", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_timestamp", type = Timestamp.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_dtm_txt", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.IN, name="wlp_i610_dec_created_by", type = String.class),
                @StoredProcedureParameter(mode= ParameterMode.OUT, name="wlp_o610_dec_id", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.OUT, name="wlp_o610_return_cd", type = Long.class),
                @StoredProcedureParameter(mode= ParameterMode.OUT, name="wlp_o610_error_msg", type = String.class)
        }
)
@Table(name = "MASS_LAYOFF_MSL")
public class MassLayoffMslDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MSL_ID")
    private Long mslId;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_REPORTED_DT")
    private Date mslReportedDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_SEP_DT")
    private Date mslSepDt;

    @Column(name = "FK_EMP_ID")
    private Long fkEmpId;

    @Column(name = "FK_NMI_ID")
    private Long fkNmiId;

    @Column(name = "MSL_NBR")
    private Long mslNbr;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_RECALL_DT")
    private Date mslRecallDt;

    @Column(name = "MSL_WRN_NOTICE_IND")
    private String mslWrnNoticeInd;

    @Column(name = "MSL_FIPS_CD")
    private Long mslFipsCd;

    @Column(name = "MSL_CATEGORY_CD")
    private Long mslCategoryCd;

    @Column(name = "MSL_LAIDOFF_NBR")
    private Long mslLaidoffNbr;

    @Column(name = "MSL_PACKETS_IND")
    private String mslPacketsInd;

    @Column(name = "MSL_PENSION_IND")
    private String mslPensionInd;

    @Column(name = "MSL_SEVERANCE_IND")
    private String mslSeveranceInd;

    @Column(name = "MSL_HOLIDAY_IND")
    private String mslHolidayInd;

    @Column(name = "MSL_VACATION_IND")
    private String mslVacationInd;

    @Column(name = "MSL_WARNPAY_IND")
    private String mslWarnpayInd;

    @Column(name = "MSL_TALLY_CMT_IFK")
    private Long mslTallyCmtIfk;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_START_HOL_DT")
    private Date mslStartHolDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_END_HOL_DT")
    private Date mslEndHolDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_EFF_START_DT")
    private Date mslEffStartDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "MSL_EFF_END_DT")
    private Date mslEffEndDt;

    @Column(name = "MSL_LAST_UPD_TS")
    private Timestamp mslLastUpdTs;

    @Column(name = "MSL_MC_DESC_TXT")
    private String mslMcDescTxt;

    @Column(name = "MSL_PENSION_TXT")
    private String mslPensionTxt;

    @Column(name = "MSL_SEVERANCE_TXT")
    private String mslSeveranceTxt;

    @Column(name = "MSL_VACATION_TXT")
    private String mslVacationTxt;

    @Column(name = "MSL_HOLIDAY_TXT")
    private String mslHolidayTxt;

    @Column(name = "MSL_WARNPAY_TXT")
    private String mslWarnpayTxt;

    @Column(name = "MSL_DTM_TXT")
    private String mslDtmTxt;

    @Column(name = "MSL_COMMENTS_TXT")
    private String mslCommentsTxt;

    @Column(name = "MSL_STATUS_IND")
    private String mslStatusInd;	//SV225644 

}