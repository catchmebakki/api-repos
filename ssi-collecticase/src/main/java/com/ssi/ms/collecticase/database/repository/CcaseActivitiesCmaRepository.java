package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.dto.ActivitiesSummaryDTO;
import com.ssi.ms.collecticase.dto.FollowupActivityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CcaseActivitiesCmaRepository extends CrudRepository<CcaseActivitiesCmaDAO, Long> {

    @Query("""          
            from CcaseActivitiesCmaDAO ccaseActivitiesCma
            where ccaseActivitiesCma.ccaseCasesCmcDAO.cmcId = :caseId
            """)
    List<CcaseActivitiesCmaDAO> getCaseNotesByCaseId(Long caseId);

    @Query("""          
            from CcaseActivitiesCmaDAO ccaseActivitiesCma 
            where ccaseActivitiesCma.ccaseCasesCmcDAO.cmcId = :caseId
            and ccaseActivitiesCma.cmaActivityTypeCd = :activityTypeCd
            and ccaseActivitiesCma.cmaRemedyType = :remedyTypeCd
            """)
    List<CcaseActivitiesCmaDAO> getActivityByActivityCdAndRemedyCd(Long caseId, Long activityTypeCd,
                                                                   Long remedyTypeCd);

    @Query("""   
            SELECT new com.ssi.ms.collecticase.dto.ActivitiesSummaryDTO(
            ccaseActivitiesCma.cmaId, ccaseActivitiesCma.cmaId, ccaseActivitiesCma.cmaActivityDt,
            fnInvGetAlvDescription(ccaseActivitiesCma.cmaActivityTypeCd) as activityTypeDesc,
            ccaseActivitiesCma.cmaActivitySpecifics,
            fnInvGetAlvDescription(ccaseActivitiesCma.cmaRemedyType) as remedyTypeDesc,
            fnGetUserName(ccaseActivitiesCma.cmaCreatedBy) as createdByUser,
            ccaseActivitiesCma.cmaFollowUpDt,
            ccaseActivitiesCma.cmaFollowShNote,
            ccaseActivitiesCma.cmaFollowupComplete,
            ccaseActivitiesCma.cmaFollowupComplBy as followupCompletedByUser,
            ccaseActivitiesCma.cmaFollowupComplDt,
            ccaseActivitiesCma.cmaFollowCompShNote
            )
            from CcaseActivitiesCmaDAO ccaseActivitiesCma
            where ccaseActivitiesCma.ccaseCasesCmcDAO.cmcId = :caseId
            """)
    Page<ActivitiesSummaryDTO> getActivitiesDataByCaseId(Long caseId, Pageable pageable);

    @Query("""   
            SELECT fnInvGetAlvDescription(ccaseActivitiesCma.cmaActivityTypeCd) as activityTypeDesc
            from CcaseActivitiesCmaDAO ccaseActivitiesCma
            where ccaseActivitiesCma.ccaseCasesCmcDAO.cmcId = :caseId
            """)
    String getFunctionAlv(Long caseId);

    @Transactional
    @Procedure(name = "createActivity")
    Map<String, Object> createCollecticaseActivity(@Param(CollecticaseConstants.PIN_CMC_ID) Long caseId,
                           @Param(CollecticaseConstants.PIN_EMP_ID) Long employerId,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_TYPE_CD) Long activityTypeCd,
                           @Param(CollecticaseConstants.PIN_REMEDY_TYPE_CD) Long remedyTypeCd,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_DT) Date activityDt,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_TIME) String activityTime,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_SPECIFICS) String activitySpecifics,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_NOTES) String activityNotes,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_NOTES_ADDL) String activityNotesAdditional,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_NOTES_NHUIS) String activityNotesNHUIS,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_COMM_METHOD) Long communicationMethod,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_CASE_CHARACTERISTICS) String caseCharacteristics,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_CMT_REP_CD) Long activityCmtRepCd,
                           @Param(CollecticaseConstants.PIN_ACTIVITY_CASE_PRIORITY) Long activityCasePriority,
                           @Param(CollecticaseConstants.PIN_FOLLOWUP_DT) Date followupDt,
                           @Param(CollecticaseConstants.PIN_FOLLOWUP_SH_NOTE) String followupShortNote,
                           @Param(CollecticaseConstants.PIN_FOLLOWUP_COMP_SH_NOTE) String followupCompleteShortNote,
                           @Param(CollecticaseConstants.PIN_USER) String callingUser,
                           @Param(CollecticaseConstants.PIN_USING) String usingProgramName);

    @Query(""" 
            SELECT new com.ssi.ms.collecticase.dto.FollowupActivityDTO(
            fnInvGetAlvDescription(ccaseActivitiesCma.cmaActivityTypeCd) as activityName,
            TO_CHAR(cmaActivityDt, 'MM/DD/YYYY') as activityCreatedDate)
            from CcaseActivitiesCmaDAO ccaseActivitiesCma
            where ccaseActivitiesCma.cmaId = :activityId
            """)
    FollowupActivityDTO getActivityInfoForFollowup(Long activityId);
}