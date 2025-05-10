package com.ssi.ms.masslayoff.database.repository.msl;

import com.ssi.ms.masslayoff.database.dao.MassLayoffMslDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author munirathnam.surepall
 *Repository interface for managing MslUploadSummaryMusmDAO entities.
 */
public interface MassLayoffMslRepository extends CrudRepository<MassLayoffMslDAO, Long> {
    /**
     * This save function will save into Database.
     *
     * @param massLayOffDAO {@link MslUploadSummaryMusmDAO} The MassLayoffMslDAO object to be saved.
     * @return {@link MassLayoffMslDAO} The saved MassLayoffMslDAO object.
     */
    MassLayoffMslDAO save(MassLayoffMslDAO massLayOffDAO);

    @Procedure(name = "callAJI610")
    Map<String, Object> callAJI610(@Param("wlp_i610_fk_emp_id") Long fkEmpId,
                                   @Nullable @Param("wlp_i610_fk_clm_id") Long fkClmId,
                                   @Param("wlp_i610_fk_nmi_id") Long fkNmiId,
                                   @Param("wlp_i610_fk_mon_id") Long fkMonId,
                                   @Param("wlp_i610_fk_dec_id") Long fkDecId,
                                   @Param("wlp_i610_fk_ldc_id") Long fkLdcId,
                                   @Param("wlp_i610_dec_detection_dt") Date decDetectionDt,
                                   @Param("wlp_i610_dec_status_cd") Long decStatusCd,
                                   @Param("wlp_i610_dec_decision_cd") Long decDecisionCd,
                                   @Param("wlp_i610_dec_decision_dt") Date decDecisionDt,
                                   @Param("wlp_i610_dec_begin_dt") Date decBeginDt,
                                   @Param("wlp_i610_dec_end_dt") Date decEndDt,
                                   @Param("wlp_i610_dec_multi_cmt_ind") String decMultiCmtInd,
                                   @Param("wlp_i610_dec_source_ifk_cd") Long decSourceIfkCd,
                                   @Param("wlp_i610_dec_source_ifk") Long decSourceIfk,
                                   @Param("wlp_i610_dec_origin_cd") Long decOriginCd,
                                   @Param("wlp_i610_dec_mrsn_dtm_ifk") Long decMrsnDtmIfk,
                                   @Param("wlp_i610_dec_bam_ind") String decBamInd,
                                   @Param("wlp_i610_dec_send_ff_ind") String decSendFfInd,
                                   @Param("wlp_i610_dec_hard_copy_ind") String decHardCopyInd,
                                   @Param("wlp_i610_dec_modified_cd") Long decModifiedCd,
                                   @Param("wlp_i610_dec_turn_point_cd") Long decTurnPointCd,
                                   @Param("wlp_i610_dec_bw_dtm_ifk") Long decBwDtmIfk,
                                   @Param("wlp_i610_dec_adjudicated_by") String decAdjudicatedBy,
                                   @Param("wlp_i610_dec_adj_lof_ifk") Long decAdjLofIfk,
                                   @Param("wlp_i610_dec_assgnd_lof_ifk") Long decAssgndLofIfk,
                                   @Param("wlp_i610_timestamp") Timestamp timestamp,
                                   @Param("wlp_i610_dec_dtm_txt") String decDtmTxt,
                                   @Param("wlp_i610_dec_created_by") String decCreatedBy);

    MassLayoffMslDAO findByMslNbr(Long mslNbr);

/*    @Procedure("callAJI610")
    int callAJI610(Long fkEmpId, Long fkClmId, Long fkNmiId, Long fkMonId, Long fkDecId, Long fkLdcId, Date decDetectionDt,
                   Long decStatusCd, Long decDecisionCd, Date decDecisionDt, Date decBeginDt, Date decEndDt, String decMultiCmtInd,
                   Long decSourceIfkCd, Long decSourceIfk, Long decOriginCd, Long decMrsnDtmIfk, String decBamInd, String decSendFfInd,
                   String decHardCopyInd, Long decModifiedCd, Long decTurnPointCd, Long decBwDtmIfk, String decAdjudicatedBy,
                   Long decAdjLofIfk, Long decAssgndLofIfk, Timestamp timestamp, String decDtmTxt, String decCreatedBy,
                   Long decId, Long returnCd, String errorMsg);*/
}
