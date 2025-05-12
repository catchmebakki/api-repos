package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.*;
import com.ssi.ms.collecticase.database.dao.CmtNotesCnoDAO;
import com.ssi.ms.collecticase.dto.*;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.*;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.*;


@Slf4j
@Service
public class CaseService extends CollecticaseBaseService {

    @Autowired
    private GeneralActivityValidator generalActivityValidator;

    public List<VwCcaseHeaderDTO> getCaseHeaderInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseHeaderInfoByCaseId(caseId).stream().map(dao -> vwCcaseHeaderMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseOpmDTO> getClaimantOpmInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getClaimantOpmInfoByCaseId(caseId).stream().map(dao -> vwCcaseOpmMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseRemedyDTO> getCaseRemedyInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseRemedyInfoByCaseId(caseId).stream().map(dao -> vwCcaseRemedyMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseHeaderEntityDTO> getCaseEntityInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseEntityInfoByCaseId(caseId).stream().map(dao -> vwCcaseHeaderEntityMapper.daoToDto(dao)).toList();
    }

    public List<CcaseActivitiesCmaDTO> getCaseNotesByCaseId(Long caseId) {
        return ccaseActivitiesCmaRepository.getCaseNotesByCaseId(caseId).stream().map(dao -> ccaseActivitiesCmaMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseCaseloadDTO> getCaseLoadByStaffId(Long staffId, int page, int size, String sortBy, boolean ascending) {

        // Create a Sort object based on the provided sort field and direction (ascending or descending)
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create the Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        // Use stream() and map() to transform the User entities to UserDTOs
        List<VwCcaseCaseloadDTO> vwCcaseCaseloadDTOs = vwCcaseCaseloadRepository.getCaseLoadByStaffId(staffId, pageable)
                .stream().map(dao -> vwCcaseCaseloadMapper.daoToDto(dao)).collect(Collectors.toList());


        return vwCcaseCaseloadDTOs;
    }

    public List<ActivitiesSummaryDTO> getActivitiesDataByCaseId(Long caseId, int page, int size, String sortBy, boolean ascending) {

        // Create a Sort object based on the provided sort field and direction (ascending or descending)
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create the Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        List<ActivitiesSummaryDTO> activitiesSummaryDTOs = ccaseActivitiesCmaRepository.getActivitiesDataByCaseId(caseId)
                .stream().collect(Collectors.toList());

        return activitiesSummaryDTOs;
    }



    public Map<String, Object> createCollecticaseCase(Long claimantId, Long staffId, Long casePriority, Long caseRemedyCd, Long caseActivityCd, String callingUser, String usingProgramName) {
        Map<String, Object> createCollecticaseActivity;

        createCollecticaseActivity = ccaseCasesCmcRepository.createCollecticaseCase(claimantId, staffId, casePriority, caseRemedyCd, caseActivityCd, callingUser, usingProgramName);

        return createCollecticaseActivity;
    }

    public Map<String, Object> createCollecticaseActivity(Long caseId, Long employerId, Long activityTypeCd, Long remedyTypeCd, Date activityDt, String activityTime, String activitySpecifics, String activityNotes, String activityNotesAdditional, String activityNotesNHUIS, Long communicationMethod, String caseCharacteristics, Long activityCmtRepCd, Long activityCasePriority, Date followupDt, String followupShortNote, String followupCompleteShortNote, String callingUser, String usingProgramName) {
        Map<String, Object> createCollecticaseActivity;

        createCollecticaseActivity = ccaseActivitiesCmaRepository.createCollecticaseActivity(caseId, employerId, activityTypeCd, remedyTypeCd, activityDt, activityTime, activitySpecifics, activityNotes, activityNotesAdditional, activityNotesNHUIS, communicationMethod, caseCharacteristics, activityCmtRepCd, activityCasePriority, followupDt, followupShortNote, followupCompleteShortNote, callingUser, usingProgramName);

        return createCollecticaseActivity;
    }

    public String createGeneralActivity(GeneralActivityDTO generalActivityDTO)
    {
        boolean activityCreated = false;
        Map<String, Object> createCollecticaseActivity = null;
        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        Long activityId;
        Date currentDate = null;
        Timestamp currentTimeStamp = null;
        List<Map<String, Object>> sendNoticeList = new ArrayList<>();
        List<String> resendNoticeList = new ArrayList<>();
        List<String> manualNoticeList = new ArrayList<>();
        createCollecticaseActivity = createActivity(generalActivityDTO);
        if (createCollecticaseActivity != null &&
                createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long)createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if(activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;

            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));
            if(! (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(),  REMEDY_GENERAL)
                    || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_BANKRUPTCY)))
            {
                if(!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO().getCraTemplatePage()))
                {
                    ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(generalActivityDTO.getPropertyLien() != null ?
                            generalActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                }
                else{
                    ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(generalActivityDTO.getPropertyLien());
                }
            }

            if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(), CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
                if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityRemedyTypeCd(), CollecticaseConstants.REMEDY_SECOND_DEMAND_LETTER)) {
                    ccaseActivitiesCmaDAO.setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_INPROCESS);
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_NO_COUNTY);
                    ccaseActivitiesCmaDAO.setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_SECOND_DEMAND_LETTER);
                }
            }
            if (!UtilFunction.compareLongObject.test(generalActivityDTO.getPropertyLien(), CollecticaseConstants.COUNTY_NONE)) {
                if (ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_UNKNOWN) == 0
                        || ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_USER_ALERT_LIEN) == 0
                        || ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_NO_COUNTY) == 0) {
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_COUNTY_SELECTED);
                }
            }
            //splitEntityValueIdType(ccaseActivitiesCmaDAO, updateContactActivityDTO.getEntityContactId());
            List<VwCcaseEntityDAO> vwCcaseEntityDAOList =
                    vwCcaseEntityRepository.getCaseEntityInfo(UtilFunction.stringToLong.apply(generalActivityDTO.getActivityEntityContact()),
                            generalActivityDTO.getCaseId(),
                    CollecticaseConstants.INDICATOR.Y.name());
            VwCcaseEntityDAO vwCcaseEntityDAO = null;
            if(CollectionUtils.isNotEmpty(vwCcaseEntityDAOList))
            {
                vwCcaseEntityDAO = vwCcaseEntityDAOList.get(0);
                ccaseActivitiesCmaDAO.setCmaEntityContTypeCd(vwCcaseEntityDAO.getCmeRole());
                ccaseActivitiesCmaDAO.setCmaEntityConttypeIfk(vwCcaseEntityDAO.getEntityId());
                ccaseActivitiesCmaDAO.setCmaEntityContact(vwCcaseEntityDAO.getEntityName());
            }
            ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDAO);
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            processReopenActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(),
                    generalActivityDTO, currentDate, currentTimeStamp);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            processCorrespondence(sendNoticeList, resendNoticeList,  manualNoticeList, ccaseActivitiesCmaDAO, null);
            processResearchNHProperty(ccaseActivitiesCmaDAO);
            processResearchIB(ccaseActivitiesCmaDAO);
            processReassignCaseToSelf(ccaseActivitiesCmaDAO);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
        }

        return activityCreated ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }

    public String createPaymentPlanActivity(PaymentPlanActivityDTO paymentPlanActivityDTO)
    {
        boolean activityCreated = false;
        Map<String, Object> createCollecticaseActivity = null;
        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        Long activityId;
        Date currentDate = null;
        Timestamp currentTimeStamp = null;
        List<Map<String, Object>> sendNoticeList = new ArrayList<>();
        List<String> resendNoticeList = new ArrayList<>();
        List<String> manualNoticeList = new ArrayList<>();
        createCollecticaseActivity = createActivity(paymentPlanActivityDTO);
        if (createCollecticaseActivity != null &&
                createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long)createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if(activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;

            ccaseActivitiesCmaDAO.setCmaPpRespToCd(paymentPlanActivityDTO.getPaymentPlanReponseToCd());
            ccaseActivitiesCmaDAO.setCmaPpRespToOther(paymentPlanActivityDTO.getPaymentPlanReponseToOther());
            ccaseActivitiesCmaDAO.setCmaPpGuidelineAmt(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount());
            ccaseActivitiesCmaDAO.setCmaPpSignedDt(paymentPlanActivityDTO.getPaymentPlanSignedDate());
            ccaseActivitiesCmaDAO.setCmaPpFaSignedDt(paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate());
            ccaseActivitiesCmaDAO.setCmaPpPaymentAmt(paymentPlanActivityDTO.getPaymentPlanPaymentAmount());
            ccaseActivitiesCmaDAO.setCmaPpPaymentCatgCd(paymentPlanActivityDTO.getPaymentPlanPaymentCategory());
            ccaseActivitiesCmaDAO.setCmaPpEffectiveUntil(paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate());
            ccaseActivitiesCmaDAO.setCmaPpEffectiveMonths(paymentPlanActivityDTO.getPaymentPlanMonths());

            ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDAO);

            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));

            if(! (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(),  REMEDY_GENERAL)
                    || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_BANKRUPTCY)))
            {
                if(!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO().getCraTemplatePage()))
                {
                    ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(paymentPlanActivityDTO.getPropertyLien() != null ?
                            paymentPlanActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                }
                else {
                    ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(paymentPlanActivityDTO.getPropertyLien());
                }
            }
            updatePPRemedy(ccaseActivitiesCmaDAO);
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            processCorrespondence(sendNoticeList, resendNoticeList,  manualNoticeList, ccaseActivitiesCmaDAO, null);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
            processClosedCasePPActivity(ccaseActivitiesCmaDAO);
        }
        return activityCreated ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }

    public String createWageGarnishmentActivity(WageGarnishmentActivityDTO wageGarnishmentActivityDTO)
    {
        boolean activityCreated = false;
        Map<String, Object> createCollecticaseActivity = null;
        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO = null;
        Long cwgId = null;
        Long activityId;
        Date currentDate = null;
        Timestamp currentTimeStamp = null;
        List<Map<String, Object>> sendNoticeList = new ArrayList<>();
        List<String> resendNoticeList = new ArrayList<>();
        List<String> manualNoticeList = new ArrayList<>();

        createCollecticaseActivity = createActivity(wageGarnishmentActivityDTO);
        if (createCollecticaseActivity != null &&
                createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long)createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if(activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;

            if(! (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(),  REMEDY_GENERAL)
                    || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_BANKRUPTCY)))
            {
                if(!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO().getCraTemplatePage()))
                {
                    ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(wageGarnishmentActivityDTO.getPropertyLien() != null ?
                            wageGarnishmentActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                }
                else {
                    ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(wageGarnishmentActivityDTO.getPropertyLien());
                }
            }

            if(CollecticaseUtilFunction.lesserThanLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), 0L))
            {
                ccaseActivitiesCmaDAO.setFkEmpIdWg(wageGarnishmentActivityDTO.getEmployerId());
            }
            else if(CollecticaseUtilFunction.lesserThanLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), 0L))
            {
                ccaseActivitiesCmaDAO.setCmaEntityConttypeIfk(wageGarnishmentActivityDTO
                        .getEmployerId());
                if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), -1L)) {
                    ccaseActivitiesCmaDAO
                            .setCmaEntityContact(CollecticaseConstants.NO_KNOWN_NH_EMPLOYER);
                } else if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), -2L)) {
                    ccaseActivitiesCmaDAO
                            .setCmaEntityContact(CollecticaseConstants.OUT_OF_STATE_EMPLOYER);
                }
                ccaseActivitiesCmaDAO.setFkEmpIdWg(null);
            }
            updateWGRemedy(ccaseActivitiesCmaDAO, wageGarnishmentActivityDTO.getEmployerId());
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            ccaseWageGarnishmentCwgDAO = processWageGarnish(ccaseActivitiesCmaDAO);
            if(ccaseWageGarnishmentCwgDAO != null)
            {
                cwgId = ccaseWageGarnishmentCwgDAO.getCwgId();
            }
            processCorrespondence(sendNoticeList, resendNoticeList,  manualNoticeList, ccaseActivitiesCmaDAO, cwgId);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
        }
        return activityCreated ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }

    public String createUpdateContactActivity(UpdateContactActivityDTO updateContactActivityDTO)
    {
        boolean activityCreated = false;
        Map<String, Object> createCollecticaseActivity = null;
        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        CcaseEntityCmeDAO ccaseEntityCme = new CcaseEntityCmeDAO();
        CcaseOrganizationCmoDAO organizationCmo = new CcaseOrganizationCmoDAO();
        CcaseCmeIndividualCmiDAO individualCmi = new CcaseCmeIndividualCmiDAO();
        List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiDAOList = null;
        Long activityId;
        Date currentDate = null;
        Timestamp currentTimeStamp = null;
        List<Map<String, Object>> sendNoticeList = new ArrayList<>();
        List<String> resendNoticeList = new ArrayList<>();
        List<String> manualNoticeList = new ArrayList<>();
        List<VwCcaseEntityDAO> vwCcaseEntityDAOList = null;
        VwCcaseEntityDAO vwCcaseEntityDAO = null;
        CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO = null;

        createCollecticaseActivity = createActivity(updateContactActivityDTO);
        if (createCollecticaseActivity != null &&
                createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long)createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if(activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;
            ccaseActivitiesCmaDAO.setCmaUpdContRepFor(updateContactActivityDTO.getEntityRepresentedFor());
            if(!CollecticaseConstants.CLAIMANT_REPRESENTS_FOR.equals(updateContactActivityDTO.getEntityRepresentedFor()))
            {
                ccaseActivitiesCmaDAO.setCmaUpdContRepFor(EMPLOYER_REPRESENTS_FOR);
                ccaseActivitiesCmaDAO.setFkEmpIdRepUc(UtilFunction.stringToLong.apply(updateContactActivityDTO.getEntityRepresentedFor()));
            }
            ccaseOrganizationCmoDAO = new CcaseOrganizationCmoDAO();
            if(UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
                CcaseActivitiesCmaDAO finalCcaseActivitiesCmaDAO = ccaseActivitiesCmaDAO;
                individualCmi = ccaseCmeIndividualCmiRepository.findById(ccaseActivitiesCmaDAO.getFkCmiIdUc())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + finalCcaseActivitiesCmaDAO.getFkCmiIdUc(), CMI_ID_NOT_FOUND));
                individualCmi.setCmiActiveInd(INDICATOR.N.name());
                individualCmi.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                individualCmi.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                individualCmi.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                individualCmi.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCmeIndividualCmiRepository.save(individualCmi);

            }
            else if(UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())){
                CcaseActivitiesCmaDAO finalCcaseActivitiesCmaDAO1 = ccaseActivitiesCmaDAO;
                ccaseEntityCme = ccaseEntityCmeRepository.findById (ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk())
                        .orElseThrow(() -> new NotFoundException("Invalid CME ID:" + finalCcaseActivitiesCmaDAO1.getCmaEntityConttypeIfk(), CME_ID_NOT_FOUND));

                ccaseEntityCme.setCmeActiveInd(INDICATOR.N.name());
                ccaseEntityCme.setCmeCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseEntityCme.setCmeCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                ccaseEntityCme.setCmeLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseEntityCme.setCmeLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseEntityCmeRepository.save(ccaseEntityCme);

                ccaseCmeIndividualCmiDAOList = ccaseCmeIndividualCmiRepository.getCaseEntityIndividualByCaseEntityId(ccaseEntityCme.getCmeId(),
                        INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO: ccaseCmeIndividualCmiDAOList) {
                    individualCmi = ccaseCmeIndividualCmiRepository.findById(ccaseCmeIndividualCmiDAO.getCmiId())
                            .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + ccaseCmeIndividualCmiDAO.getCmiId(), CMI_ID_NOT_FOUND));
                    individualCmi.setCmiActiveInd(INDICATOR.N.name());
                    individualCmi.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                    individualCmi.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                    individualCmi.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    individualCmi.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseCmeIndividualCmiRepository.save(individualCmi);
                }

            }
            else if(CollecticaseUtilFunction.greaterThanLongObject.test(
                        UtilFunction.stringToLong.apply(updateContactActivityDTO.getActivityEntityContact()), 0L))
            {
                vwCcaseEntityDAOList = vwCcaseEntityRepository.getCaseEntityInfo(UtilFunction.stringToLong.apply(updateContactActivityDTO.getActivityEntityContact()),
                        updateContactActivityDTO.getCaseId(), CollecticaseConstants.INDICATOR.Y.name());
                if(CollectionUtils.isNotEmpty(vwCcaseEntityDAOList))
                {
                    vwCcaseEntityDAO = vwCcaseEntityDAOList.get(0);
                    VwCcaseEntityDAO finalVwCcaseEntityDAO = vwCcaseEntityDAO;
                    ccaseEntityCme = ccaseEntityCmeRepository.findById(vwCcaseEntityDAO.getCmeId())
                            .orElseThrow(() -> new NotFoundException("Invalid CME ID:" + finalVwCcaseEntityDAO.getCmeId()
                                    , CME_ID_NOT_FOUND));
                    if(UtilFunction.compareLongObject.test(vwCcaseEntityDAO.getEntityId(), CASE_ENTITY_CONTACT_TYPE_REP_CMO)
                        || UtilFunction.compareLongObject.test(vwCcaseEntityDAO.getEntityId(), CASE_ENTITY_CONTACT_TYPE_ATTY))
                    {
                        ccaseOrganizationCmoDAO = ccaseOrganizationCmoRepository.findById(vwCcaseEntityDAO.getEntityId())
                                .orElseThrow(() -> new NotFoundException("Invalid CMO ID:" + UtilFunction.stringToLong.apply(updateContactActivityDTO.getActivityEntityContact())
                                        , CMO_ID_NOT_FOUND));
                        populateCaseOrgData(ccaseActivitiesCmaDAO, ccaseOrganizationCmoDAO);
                    }
                    populateCaseEntityData(ccaseActivitiesCmaDAO,ccaseEntityCme,ccaseOrganizationCmoDAO);
                }
            }
            individualCmi = new CcaseCmeIndividualCmiDAO();
            if(CollecticaseUtilFunction.greaterThanLongObject.test(
                    updateContactActivityDTO.getEntityContactId(), 0L))
            {
                individualCmi = ccaseCmeIndividualCmiRepository.findById(updateContactActivityDTO.getEntityContactId())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + updateContactActivityDTO.getEntityContactId()
                                , CMI_ID_NOT_FOUND));
                populateCaseIndData(ccaseActivitiesCmaDAO, ccaseEntityCme, ccaseOrganizationCmoDAO);
            }
            updateCaseActivitiesCma(ccaseActivitiesCmaDAO, ccaseEntityCme, organizationCmo, individualCmi);
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
            processCompleteForWgInitiateEmpNC(ccaseActivitiesCmaDAO);
        }

        return activityCreated ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }

    private void updateWGRemedy(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
                                Long employerValue) {
        processResearchForEmp(ccaseActivitiesCmaDAO, employerValue);
        if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null) {
            processChngWgAmt(ccaseActivitiesCmaDAO);
            processSuspendWage(ccaseActivitiesCmaDAO);
            //processReinstateWG(activitiesCma);
        }
    }


    public String createGeneralActivityOld(GeneralActivityDTO generalActivityDTO) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = generalActivityValidator.validateGeneralActivity(generalActivityDTO);
        boolean activity_failed = true;
        Date currentDate = commonRepository.getCurrentDate();
        Timestamp currentTimestamp = commonRepository.getCurrentTimestamp();
        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO;
        Long activityId;
        if (errorMap.isEmpty()) {
            CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(generalActivityDTO.getCaseId(), List.of(generalActivityDTO.getActivityRemedyTypeCd()));
            CcaseCasesCmcDAO ccaseCasesCmcDAO = ccaseCaseRemedyCmrDAO.getCcaseCasesCmcDAO();
            if (generalActivityDTO.getPropertyLien() != null) {
                ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(generalActivityDTO.getPropertyLien());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            }

            final Map<String, Object> createCollecticaseActivity = createCollecticaseActivity(generalActivityDTO.getCaseId(), null, generalActivityDTO.getActivityTypeCd(), generalActivityDTO.getActivityRemedyTypeCd(), generalActivityDTO.getActivityDate(), generalActivityDTO.getActivityTime(), generalActivityDTO.getActivitySpecifics(), generalActivityDTO.getActivityNotes(), generalActivityDTO.getActivityAdditionalNotes(), generalActivityDTO.getActivityNHUISNotes(), generalActivityDTO.getActivityCommunicationMethod(), generalActivityDTO.getActivityCaseCharacteristics(), generalActivityDTO.getActivityClaimantRepresentative(), generalActivityDTO.getCasePriorityCd(), generalActivityDTO.getActivityFollowupDate(), generalActivityDTO.getActivityFollowupShortNote(), null, generalActivityDTO.getCallingUser(), generalActivityDTO.getUsingProgramName());

            if (createCollecticaseActivity != null && createCollecticaseActivity.get(CollecticaseConstants.POUT_SUCCESS) != null && createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID) != null) {
                activityId = (Long) createCollecticaseActivity.get(CollecticaseConstants.POUT_CMA_ID);
                ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId).orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            } else {
                ccaseActivitiesCmaDAO = null;
                activityId = null;
            }
            ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
            if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(), CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
                if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityRemedyTypeCd(), CollecticaseConstants.REMEDY_SECOND_DEMAND_LETTER)) {
                    ccaseActivitiesCmaDAO.setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_INPROCESS);
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_NO_COUNTY);
                    ccaseActivitiesCmaDAO.setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_SECOND_DEMAND_LETTER);
                }
            }
            if (UtilFunction.compareLongObject.test(generalActivityDTO.getPropertyLien(), CollecticaseConstants.COUNTY_NONE)) {
                if (ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_UNKNOWN) == 0 || ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_USER_ALERT_LIEN) == 0 || ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CollecticaseConstants.CMR_STATUS_NO_COUNTY) == 0) {
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_COUNTY_SELECTED);
                }
            }
            AllowValAlvDAO allowValAlvDAO = allowValAlvRepository.findById(generalActivityDTO.getActivityRemedyTypeCd()).orElseThrow(() -> new NotFoundException("Invalid ALV ID:" + generalActivityDTO.getActivityRemedyTypeCd(), ALV_ID_NOT_FOUND));
            if (!NOT_REMEDY.equals(allowValAlvDAO.getAlvDecipherCode())) {
                ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            }
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimestamp);
            if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null) {
                processChngWgAmt(ccaseActivitiesCmaDAO);
                processSuspendWage(ccaseActivitiesCmaDAO);
                //processReinstateWG(activitiesCma);
            }
            CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmi = null;
            CcaseCmeIndividualCmiDAO caseCmeIndividualCmiDAO = null;
            CcaseEntityCmeDAO ccaseEntityCme = null;
            CcaseOrganizationCmoDAO caseOrganizationCmoDAO = null;
            List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiDAOList = null;
            if(UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
                ccaseCmeIndividualCmi = ccaseCmeIndividualCmiRepository.findById(ccaseActivitiesCmaDAO.getFkCmiIdUc())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + ccaseActivitiesCmaDAO.getFkCmiIdUc(), CMI_ID_NOT_FOUND));
                ccaseCmeIndividualCmi.setCmiActiveInd(INDICATOR.N.name());
                ccaseCmeIndividualCmi.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseCmeIndividualCmi.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                ccaseCmeIndividualCmi.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCmeIndividualCmi.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCmeIndividualCmiRepository.save(ccaseCmeIndividualCmi);

            }
            else if(UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())){
                ccaseEntityCme = ccaseEntityCmeRepository.findById (ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk())
                        .orElseThrow(() -> new NotFoundException("Invalid CME ID:" + ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk(), CME_ID_NOT_FOUND));

                ccaseEntityCme.setCmeActiveInd(INDICATOR.N.name());
                ccaseEntityCme.setCmeCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseEntityCme.setCmeCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                ccaseEntityCme.setCmeLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseEntityCme.setCmeLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseEntityCmeRepository.save(ccaseEntityCme);

                ccaseCmeIndividualCmiDAOList = ccaseCmeIndividualCmiRepository.getCaseEntityIndividualByCaseEntityId(ccaseEntityCme.getCmeId(),
                        INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO: ccaseCmeIndividualCmiDAOList) {
                    caseCmeIndividualCmiDAO = ccaseCmeIndividualCmiRepository.findById(ccaseCmeIndividualCmi.getCmiId())
                            .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + ccaseCmeIndividualCmiDAO.getCmiId(), CMI_ID_NOT_FOUND));
                    caseCmeIndividualCmiDAO.setCmiActiveInd(INDICATOR.N.name());
                    caseCmeIndividualCmiDAO.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                    caseCmeIndividualCmiDAO.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                    caseCmeIndividualCmiDAO.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    caseCmeIndividualCmiDAO.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseCmeIndividualCmiRepository.save(caseCmeIndividualCmiDAO);
                }

            }
            else {
                // Get Entity Data
                ccaseEntityCme = ccaseEntityCmeRepository.findById(ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk())
                        .orElseThrow(() -> new NotFoundException("Invalid CME ID:" + ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk(), CME_ID_NOT_FOUND));
                // Populate Organization Data
                caseOrganizationCmoDAO = populateCaseOrgData(ccaseActivitiesCmaDAO,
                        caseOrganizationCmoDAO);
                // Populate Entity Data
                ccaseEntityCme = populateCaseEntityData(ccaseActivitiesCmaDAO,
                        ccaseEntityCme, caseOrganizationCmoDAO);
                // Populate Individual Data
                ccaseCmeIndividualCmi = populateCaseIndData(ccaseActivitiesCmaDAO,
                        ccaseEntityCme, caseOrganizationCmoDAO);
                // Update Activity with Entity and Organization and Individual
                updateCaseActivitiesCma(ccaseActivitiesCmaDAO, ccaseEntityCme,
                        caseOrganizationCmoDAO, ccaseCmeIndividualCmi);
            }
            activity_failed = false;
        } else {
            ccaseActivitiesCmaDAO = null;
            activityId = null;
            throw new DynamicValidationException(GENERAL_ACTIVITY_FAILED, errorMap);
        }
        return activity_failed ? ErrorMessageConstant.CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }

    public void updateCaseActivitiesCma(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDB,
            CcaseEntityCmeDAO ccaseEntityCme,
            CcaseOrganizationCmoDAO organizationCmo,
            CcaseCmeIndividualCmiDAO individualCmi) {

        if (ccaseEntityCme != null && ccaseEntityCme.getCmeId() != null) {
            ccaseActivitiesCmaDB.setCmaEntityConttypeIfk(ccaseEntityCme
                    .getCmeId());
        }
        if (organizationCmo != null && organizationCmo.getCmoId() != null) {
            ccaseActivitiesCmaDB.setFkCmoIdUc(organizationCmo.getCmoId());
        }
        if (individualCmi != null && individualCmi.getCmiId() != null) {
            ccaseActivitiesCmaDB.setFkCmiIdUc(individualCmi.getCmiId());
        }

        if (CollecticaseConstants.CLAIMANT_REPRESENTS_FOR.equals(ccaseActivitiesCmaDB.getCmaUpdContRepFor()) ) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB
                    .getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(null);
        } else if (StringUtils.isNotBlank(ccaseActivitiesCmaDB.getCmaUpdContRepFor())) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB
                    .getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(ccaseActivitiesCmaDB.getFkEmpIdRepUc());
        }

        // Update CMA Entity Contact when adding New ATTY or New Repo
        if (UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDB.getCmaEntityContTypeCd())
            || UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O, ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(organizationCmo.getCmoName());
        }

        if(UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_I, ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(individualCmi.getCmiFirstName() + StringUtils.SPACE +
                    individualCmi.getCmiLastName());
            // For Activity Rep(I), no possibility from UI(disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            ccaseActivitiesCmaDB.setCmaUpdContPrimary(CollecticaseConstants.INDICATOR.Y.name());
        }

        // Clearing the default value for Individual's Country & State when Firstname and lastname is not specified
        if(individualCmi.getCmiFirstName() == null && individualCmi.getCmiLastName() == null) {
            ccaseActivitiesCmaDB.setCmaUpdContIndState(null);
            ccaseActivitiesCmaDB.setCmaUpdContIndCntry(null);
        }

        ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDB);
    }

    public CcaseCmeIndividualCmiDAO populateCaseIndData(
            CcaseActivitiesCmaDAO activitiesCma, CcaseEntityCmeDAO entityCme,
            CcaseOrganizationCmoDAO organizationCmo) {
        CcaseCmeIndividualCmiDAO individualCmi = new CcaseCmeIndividualCmiDAO();
        if (StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndAddr1())
                && StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndFName())
                && StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndLName())) {
            if (activitiesCma.getFkCmiIdUc() != null) {
                individualCmi = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + activitiesCma.getFkCmiIdUc(), CMI_ID_NOT_FOUND));
            }
            individualCmi.setCmiIsPrimary(activitiesCma.getCmaUpdContPrimary());

            // For Activity Rep(I), no possibility from UI(disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            if(UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_I,
                    activitiesCma
                            .getCmaEntityContTypeCd())) {
                individualCmi.setCmiIsPrimary(INDICATOR.Y.name());
            }
            individualCmi.setCmiIsMailingRcpt(activitiesCma.getCmaUpdContMailingRcpt());
            individualCmi.setCmiJobTitle(activitiesCma.getCmaUpdContJobTitle());
            individualCmi
                    .setCmiFirstName(activitiesCma.getCmaUpdContIndFName());
            individualCmi.setCmiLastName(activitiesCma.getCmaUpdContIndLName());
            individualCmi.setCmiSalutationCd(activitiesCma
                    .getCmaUpdContIndSalCd());
            individualCmi.setCmiAddrLn1(activitiesCma.getCmaUpdContIndAddr1());
            individualCmi.setCmiAddrLn2(activitiesCma.getCmaUpdContIndAddr2());
            individualCmi.setCmiCity(activitiesCma.getCmaUpdContIndCity());
            individualCmi.setCmiCountry(activitiesCma
                    .getCmaUpdContIndCntry());
            individualCmi.setCmiState(activitiesCma.getCmaUpdContIndState());
            individualCmi
                    .setCmiZipPostalCd(activitiesCma.getCmaUpdContIndZip());
            individualCmi.setCmiTeleWork(activitiesCma.getCmaUpdContIndTeleW());
            individualCmi.setCmiTeleWorkExt(activitiesCma
                    .getCmaUpdContIndWExt());
            individualCmi.setCmiTeleHome(activitiesCma.getCmaUpdContIndTeleH());
            individualCmi.setCmiTeleCell(activitiesCma.getCmaUpdContIndTeleC());
            individualCmi.setCmiFax(activitiesCma.getCmaUpdContIndFax());
            individualCmi.setCmiEmails(activitiesCma.getCmaUpdContIndEmails());
            individualCmi.setCmiCommPreference(activitiesCma
                    .getCmaUpdContIndPrefCd());
            individualCmi.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
            individualCmi
                    .setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
            individualCmi.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
            individualCmi
                    .setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
            individualCmi.setCcaseEntityCmeDAO(entityCme);
            individualCmi.setCcaseOrganizationCmoDAO(organizationCmo);
            individualCmi.setCmiActiveInd(CollecticaseConstants.INDICATOR.Y.name());
            activitiesCma.setFkCmiIdUc(individualCmi.getCmiId());
            ccaseCmeIndividualCmiRepository.save(individualCmi);

            // Update Primary Contact for Individuals
            if (CollecticaseConstants.INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContPrimary())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository.getCaseEntityIndividualByCaseEntityId
                        (entityCme.getCmeId(), CollecticaseConstants.INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO cmi : ccaseCmeIndividualCmiList) {
                    CcaseCmeIndividualCmiDAO ind = new CcaseCmeIndividualCmiDAO();
                    if (!UtilFunction.compareLongObject.test(cmi.getCmiId(), activitiesCma.getFkCmiIdUc())) {
                        ind = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                                .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + cmi.getCmiId(), CMI_ID_NOT_FOUND));
                        ind.setCmiIsPrimary(StringUtils.EMPTY);
                        ind.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
                        ind.setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
                        ind.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
                        ind.setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
                        ccaseCmeIndividualCmiRepository.save(ind);
                    }
                }
            }

            // Update Mailing Recipient for Individuals
            if (CollecticaseConstants.INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContMailingRcpt())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository.getCaseEntityIndividualByCaseEntityId
                        (entityCme.getCmeId(), CollecticaseConstants.INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO cmi : ccaseCmeIndividualCmiList) {
                    CcaseCmeIndividualCmiDAO ind = new CcaseCmeIndividualCmiDAO();
                    if (!UtilFunction.compareLongObject.test(cmi.getCmiId(), activitiesCma.getFkCmiIdUc())) {
                        ind = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                                .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + cmi.getCmiId(), CMI_ID_NOT_FOUND));
                        ind.setCmiIsMailingRcpt(StringUtils.EMPTY);
                        ind.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
                        ind.setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
                        ind.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
                        ind.setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
                        ccaseCmeIndividualCmiRepository.save(ind);
                    }
                }
            }


        }
        return individualCmi;
    }

    public CcaseEntityCmeDAO populateCaseEntityData(
            CcaseActivitiesCmaDAO activitiesCma, CcaseEntityCmeDAO entityCme,
            CcaseOrganizationCmoDAO organizationCmo) {
        entityCme.setCcaseCasesCmcDAO(activitiesCma.getCcaseCasesCmcDAO());
        if (UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_EMP,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCmeType(CollecticaseConstants.CME_TYPE_ORG);
            entityCme.setCmeRole(CollecticaseConstants.ENTITY_CONTACT_TYPE_EMP);
            EmployerEmpDAO employer = new EmployerEmpDAO();
            employer.setEmpId(activitiesCma.getFkEmpIdRepUc());
            entityCme.setEmployerEmpDAO(employer);
        } else if (UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CollecticaseConstants.CME_TYPE_ORG);
            entityCme.setCmeRole(CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY);
        } else if (UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CollecticaseConstants.CME_TYPE_ORG);
            entityCme.setCmeRole(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O);
        } else if (UtilFunction.compareLongObject.test(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_I,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CollecticaseConstants.CME_TYPE_IND);
            entityCme.setCmeRole(CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_I);
        }
        if (CollecticaseConstants.CLAIMANT_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            entityCme.setEmployerEmpDAO(null);
        } else if (EMPLOYER_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            EmployerEmpDAO employer = employerEmpRepository.findById(UtilFunction.stringToLong.apply(activitiesCma.getCmaUpdContRepFor()))
                    .orElseThrow(() -> new NotFoundException("Invalid EMP ID:" + activitiesCma.getCmaUpdContRepFor(), EMP_ID_NOT_FOUND));
            entityCme.setEmployerEmpDAO(employer);
        }
        entityCme.setCmeEmpRemarks(null);
        entityCme.setCmeActiveInd(INDICATOR.Y.name());
        entityCme.setCmeCreatedBy(activitiesCma.getCmaCreatedBy());
        entityCme.setCmeCreatedUsing(activitiesCma.getCmaCreatedUsing());
        entityCme.setCmeLastUpdBy(activitiesCma.getCmaLastUpdBy());
        entityCme.setCmeLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
        ccaseEntityCmeRepository.save(entityCme);

        return entityCme;
    }

    public CcaseOrganizationCmoDAO populateCaseOrgData(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseOrganizationCmoDAO caseOrganizationCmoDAO) {
        if (UtilFunction.compareLongObject.test( ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())
            || UtilFunction.compareLongObject.test( ENTITY_CONTACT_TYPE_REP_O, ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())) {
            if (StringUtils.isNotBlank(ccaseActivitiesCmaDAO.getCmaUpdContOrgName())) {
                caseOrganizationCmoDAO
                        .setCmoName(ccaseActivitiesCmaDAO.getCmaUpdContOrgName());
                caseOrganizationCmoDAO
                        .setCmoUIAcctNbr(ccaseActivitiesCmaDAO.getCmaUIAcctNbr());
                caseOrganizationCmoDAO.setCmoFEINNbr(ccaseActivitiesCmaDAO.getCmaFeinNbr());
                caseOrganizationCmoDAO.setCmoAddrLn1(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgAddr1());
                caseOrganizationCmoDAO.setCmoAddrLn2(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgAddr2());
                caseOrganizationCmoDAO
                        .setCmoCity(ccaseActivitiesCmaDAO.getCmaUpdContOrgCity());
                caseOrganizationCmoDAO.setCmoCountry(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgCntry());
                caseOrganizationCmoDAO.setCmoState(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgState());
                caseOrganizationCmoDAO.setCmoZipPostalCd(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgZip());
                caseOrganizationCmoDAO.setCmoTeleNum(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgTele());
                caseOrganizationCmoDAO.setCmoFax(ccaseActivitiesCmaDAO.getCmaUpdContOrgFax());
                caseOrganizationCmoDAO.setCmoCommPreference(ccaseActivitiesCmaDAO.getCmaUpdContOrgPrefCd());
                caseOrganizationCmoDAO.setCmoWebsite(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgWebsite());
                caseOrganizationCmoDAO.setCmoRemarks(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgRemark());
                caseOrganizationCmoDAO
                        .setCmoCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                caseOrganizationCmoDAO.setCmoCreatedUsing(ccaseActivitiesCmaDAO
                        .getCmaCreatedUsing());
                caseOrganizationCmoDAO
                        .setCmoLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                caseOrganizationCmoDAO.setCmoLastUpdUsing(ccaseActivitiesCmaDAO
                        .getCmaLastUpdUsing());

                if(ccaseActivitiesCmaDAO.getFkEmpIdRepUc() != null) {
                    EmployerEmpDAO employerEmpDAO = new EmployerEmpDAO();
                    employerEmpDAO.setEmpId(ccaseActivitiesCmaDAO.getFkEmpIdRepUc());
                    caseOrganizationCmoDAO.setEmployerEmpDAO(employerEmpDAO);
                }

                ccaseOrganizationCmoRepository.save(caseOrganizationCmoDAO);
            }
        }
        return caseOrganizationCmoDAO;
    }

    private void splitEntityValueIdType(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, String entityContact) {
        if (StringUtils.isNotBlank(entityContact)) {
            String[] splitArray = entityContact.split(CollecticaseConstants.SEPARATOR);
            if (splitArray.length > 1) {
                String role = splitArray[0];
                ccaseActivitiesCmaDAO.setCmaEntityContTypeCd(convertEntityRoleIntoRoleCd(role));
                ccaseActivitiesCmaDAO.setCmaEntityContact(splitArray[1]);
                ccaseActivitiesCmaDAO.setCmaEntityConttypeIfk(UtilFunction.stringToLong.apply(splitArray[2]));
            } else {
                ccaseActivitiesCmaDAO.setCmaEntityContTypeCd(UtilFunction.stringToLong.apply(ccaseActivitiesCmaDAO.getCmaEntityContact()));
                ccaseActivitiesCmaDAO.setCmaEntityContact(null);
            }
        }
    }

    private void splitEmpRepValueIdType(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, String empRepresentative) {
        if (StringUtils.isNotBlank(empRepresentative)) {
            String[] splitArray = empRepresentative.split(CollecticaseConstants.SEPARATOR);
            if (splitArray.length > 1) {
                String role = splitArray[0];
                ccaseActivitiesCmaDAO.setCmaEmpRepTypeCd(convertEntityRoleIntoRoleCd(role));
                ccaseActivitiesCmaDAO.setCmaEmpRepresentative(splitArray[1]);
                ccaseActivitiesCmaDAO.setCmaEmpRepTypeIfk(UtilFunction.stringToLong.apply(splitArray[2]));
            } else {
                ccaseActivitiesCmaDAO.setCmaEmpRepTypeCd(UtilFunction.stringToLong.apply(empRepresentative));
                ccaseActivitiesCmaDAO.setCmaEmpRepresentative(null);
            }
        }
    }

    private Long convertEntityRoleIntoRoleCd(String role) {
        Long entityRoleType = null;
        if (CollecticaseConstants.ENTITY_EMP_SHORT_FORM.equals(role)) {
            entityRoleType = CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP;
        } /*else if (ActivityConstants.ENTITY_COURT_SHORT_FORM.equals(role)) {
			entityRoleType = AllowableConstants.CASE_ENTITY_CONTACT_TYPE_COURT;
		}*/ else if (CollecticaseConstants.ENTITY_ATTY_SHORT_FORM.equals(role) || CollecticaseConstants.ENTITY_NEW_ATTY_SHORT_FORM.equals(role)) {
            entityRoleType = CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY;
        } else if (CollecticaseConstants.ENTITY_REP_IND_SHORT_FORM.equals(role) || CollecticaseConstants.ENTITY_NEW_REP_IND_SHORT_FORM.equals(role)) {
            entityRoleType = CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMI;
        } else if (CollecticaseConstants.ENTITY_REP_ORG_SHORT_FORM.equals(role) || CollecticaseConstants.ENTITY_NEW_REP_ORG_SHORT_FORM.equals(role)) {
            entityRoleType = CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO;
        }
        return entityRoleType;
    }

    private void processReopenActivity(CcaseCasesCmcDAO ccaseCasesCmcDAO, GeneralActivityDTO generalActivityDTO, Date currentDate, Timestamp currentTimestamp) {
        StaffStfDAO staffStfDAO = null;
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(), CollecticaseConstants.ACTIVITY_TYPE_REOPEN_CASE)) {
            ccaseCasesCmcDAO.setCmcCasePriority(CollecticaseConstants.CASE_PRIORITY_LO);
            ccaseCasesCmcDAO.setCmcCaseStatus(CollecticaseConstants.CASE_STATUS_REOPEN);
            ccaseCasesCmcDAO.setCmcCaseOpenDt(currentDate);
            ccaseCasesCmcDAO.setCmcAssignedTs(currentTimestamp);
            List<StaffStfDAO> staffStfDAOList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong.apply(generalActivityDTO.getCallingUser()));
            ccaseCasesCmcDAO.setStaffStfDAO(staffStfDAOList.get(0));
            ccaseCasesCmcDAO.setCmcCaseNewInd(CollecticaseConstants.INDICATOR.Y.name());
            ccaseCasesCmcDAO.setCmcLastUpdBy(generalActivityDTO.getCallingUser());
            ccaseCasesCmcDAO.setCmcLastUpdUsing(generalActivityDTO.getUsingProgramName());
            ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);
        }
    }

    private void createNHUISNotes(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Timestamp currentTimestamp) {
        if (StringUtils.isNotBlank(ccaseActivitiesCmaDAO.getCmaActivityNotesNhuis())) {
            CmtNotesCnoDAO cmtNotesCnoDAO = new CmtNotesCnoDAO();
            cmtNotesCnoDAO.setClaimantCmtDAO(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO());
            cmtNotesCnoDAO.setCnoEnteredTs(currentTimestamp);
            cmtNotesCnoDAO.setCnoEnteredBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            cmtNotesCnoDAO.setCnoSubjectTxt(CollecticaseConstants.NHUIS_NOTES_SUBJECT);
            cmtNotesCnoDAO.setCnoNotesTxt(ccaseActivitiesCmaDAO.getCmaActivityNotesNhuis());
            cmtNotesCnoDAO.setCnoLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            cmtNotesCnoDAO.setCnoLastUpdTs(currentTimestamp);
            cmtNotesCnoRepository.save(cmtNotesCnoDAO);
        }
    }

    public void processCorrespondence(List<Map<String, Object>> sendNoticesList, List<String> resendNoticeList, List<String> manualNoticeList, CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long cwgId) {
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO;
        CorrespondenceCorDAO correspondenceCorDAO;
        Long crcId;
        List<Integer> multipleRecieptList = new ArrayList<Integer>();
        Map<String, Object> createCorrespondence;
        for (Map<String, Object> inputParamMap : sendNoticesList) {
            if (inputParamMap.get(CollecticaseConstants.PIN_CRC_ID).equals(CollecticaseConstants.NOTICE_OF_CHANGED_WG)
                    || inputParamMap.get(CollecticaseConstants.PIN_CRC_ID).equals(CollecticaseConstants.NOTICE_OF_SUSPENDED_WG)
                    || inputParamMap.get(CollecticaseConstants.PIN_CRC_ID).equals(CollecticaseConstants.NOTICE_OF_GARNISHMENT)
                    || inputParamMap.get(CollecticaseConstants.PIN_CRC_ID).equals(CollecticaseConstants.NOTICE_OF_COURT_ORDERED_WG)) {
                multipleRecieptList.add(CollecticaseConstants.COR_RECEIPT_EMPLOYER);
                multipleRecieptList.add(CollecticaseConstants.COR_RECEIPT_CLAIMANT);
            } else {
                multipleRecieptList.add(CollecticaseConstants.COR_RECEIPT_EMPLOYER);
            }
            for (Integer receiptVal : multipleRecieptList) {
                crcId = (Long) inputParamMap.get(CollecticaseConstants.PIN_CRC_ID);
                ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
                ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
                if (Objects.equals(receiptVal, COR_RECEIPT_CLAIMANT)) {
                    inputParamMap.put(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK,
                            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId());
                }
                createCorrespondence = correspondenceCorRepository.createCorrespondence(
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_RPT_ID),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_CLM_ID),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_EMP_ID),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_CMT_ID),
                        (String) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_COE_IND),
                        (String) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_FORCED_IND),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_STATUS_CD),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_DEC_ID_IFK),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK),
                        (Integer) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_CD),
                        (Timestamp) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COR_TS),
                        (String) inputParamMap.get(CollecticaseConstants.PIN_WLP_I720_COE_STRING));

                if (createCorrespondence != null
                        && createCorrespondence.get(POUT_WLP_O720_RETURN_CD) != null
                        && NHUIS_RETURN_SUCCESS.equals((Integer) createCorrespondence.get(POUT_WLP_O720_RETURN_CD))) {
                    Long corId = ((Integer) createCorrespondence.get(POUT_WLP_O720_COR_ID)).longValue();
                    correspondenceCorDAO = correspondenceCorRepository.findById(corId)
                            .orElseThrow(() -> new NotFoundException("Invalid COR ID:" + corId, COR_ID_NOT_FOUND));

                    if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), CollecticaseConstants.REMEDY_WAGE_GARNISHMENT)) {
                        if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null &&
                                CollecticaseUtilFunction.greaterThanLongObject.test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), 0L)) {//SAT25570
                                correspondenceCorDAO.setCorSourceIfk(CollecticaseConstants.COR_SOURCE_IFK_CD_FOR_CMC);
                                correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
                                correspondenceCorDAO.setCorReceipIfk(receiptVal.longValue());
                        }
                    } else {
                        correspondenceCorDAO.setCorSourceIfk(CollecticaseConstants.COR_SOURCE_IFK_CD_FOR_CMC);
                        correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
                        correspondenceCorDAO.setCorReceipIfk(CollecticaseConstants.COR_RECEIPT_CLAIMANT.longValue());
                    }
                    correspondenceCorRepository.save(correspondenceCorDAO);
                    createCMN(ccaseActivitiesCmaDAO, corId, ccaseCraCorrespondenceCrcDAO, cwgId);
                }
            }
        }
        for (String cmnId : resendNoticeList) {
            createResendCMN(ccaseActivitiesCmaDAO, UtilFunction.stringToLong.apply(cmnId));
        }
        for (String manualCrcId : manualNoticeList) {
            createManualCMN(ccaseActivitiesCmaDAO, UtilFunction.stringToLong.apply(manualCrcId), cwgId);
            if (UtilFunction.compareLongObject.test(CollecticaseConstants.TEMP_REDUCTION_LIEN, UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(), CollecticaseConstants.ACTIVITY_TYPE_SENT_TEMP_PP_REDUCTION_LTR,
                        CollecticaseConstants.REMEDY_PAYMENT_PLAN, CollecticaseConstants.ACTIVITY_SPECIFICS_TEMP_REDUCTION, ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
            if (UtilFunction.compareLongObject.test(CollecticaseConstants.TEMP_SUSPENSION_LIEN, UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(), CollecticaseConstants.ACTIVITY_TYPE_SENT_TEMP_PP_SUSPENSION_LTR,
                        CollecticaseConstants.REMEDY_PAYMENT_PLAN, CollecticaseConstants.ACTIVITY_SPECIFICS_TEMP_SUSPENSION, ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
        }
    }

    public void addSystemActivity(CcaseCasesCmcDAO ccaseCasesCmcDAO,
                                                   Long activityTypeCd, Long remedyCd,
                                                   String activitySpecifics, String activityNotes, Long casePriority) {
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        createActivityDTO.setCaseId(ccaseCasesCmcDAO.getCmcId());
        createActivityDTO.setActivityDt(commonRepository.getCurrentDate());
        createActivityDTO.setActivityTime(CollecticaseConstants.TIME_FORMAT_INPUT.format(commonRepository.getCurrentDate()));
        createActivityDTO.setActivityTypeCd(activityTypeCd);
        createActivityDTO.setRemedyTypeCd(remedyCd);
        createActivityDTO.setCaseCharacteristics(ccaseCasesCmcDAO.getCmcCaseCharacteristics());
        createActivityDTO.setActivitySpecifics(activitySpecifics);
        createActivityDTO.setActivityNotes(activityNotes);
        createActivityDTO.setCommunicationMethod(CollecticaseConstants.COMM_METHOD_NOT_APPLICABLE);
        createActivityDTO.setActivityCmtRepCd(ccaseCasesCmcDAO.getCmcCmtRepTypeCd());
        if (casePriority == null) {
            createActivityDTO.setActivityCasePriority(ccaseCasesCmcDAO.getCmcCasePriority());
        } else {
            createActivityDTO.setActivityCasePriority(casePriority);
        }
        createActivityDTO.setCallingUser(ccaseCasesCmcDAO.getCmcLastUpdBy());
        createActivityDTO.setUsingProgramName(ccaseCasesCmcDAO.getCmcLastUpdUsing());

        createActivity(createActivityDTO);
    }

    private Map<String, Object> createActivity(CreateActivityDTO createActivityDTO) {
        Map<String, Object> createCollecticaseActivity = createCollecticaseActivity(createActivityDTO.getCaseId(),
                null, createActivityDTO.getActivityTypeCd(),
                createActivityDTO.getRemedyTypeCd(), createActivityDTO.getActivityDt(),
                createActivityDTO.getActivityTime(), createActivityDTO.getActivitySpecifics(),
                createActivityDTO.getActivityNotes(), createActivityDTO.getActivityNotesAdditional(),
                createActivityDTO.getActivityNotesNHUIS(), createActivityDTO.getCommunicationMethod(),
                createActivityDTO.getCaseCharacteristics(), createActivityDTO.getActivityCmtRepCd(),
                createActivityDTO.getActivityCasePriority(), createActivityDTO.getFollowupDt(),
                createActivityDTO.getFollowupShortNote(), null,
                createActivityDTO.getCallingUser(), createActivityDTO.getUsingProgramName());

        return createCollecticaseActivity;
    }

    private Map<String, Object> createActivity(GeneralActivityDTO generalActivityDTO) {
        Map<String, Object> createCollecticaseActivity = createCollecticaseActivity(generalActivityDTO.getCaseId(),
                null, generalActivityDTO.getActivityTypeCd(),
                generalActivityDTO.getActivityRemedyTypeCd(), generalActivityDTO.getActivityDate(),
                generalActivityDTO.getActivityTime(), generalActivityDTO.getActivitySpecifics(),
                generalActivityDTO.getActivityNotes(), generalActivityDTO.getActivityAdditionalNotes(),
                generalActivityDTO.getActivityNHUISNotes(), generalActivityDTO.getActivityCommunicationMethod(),
                generalActivityDTO.getActivityCaseCharacteristics(), generalActivityDTO.getActivityClaimantRepresentative(),
                generalActivityDTO.getCasePriorityCd(), generalActivityDTO.getActivityFollowupDate(),
                generalActivityDTO.getActivityFollowupShortNote(), null,
                generalActivityDTO.getCallingUser(), generalActivityDTO.getUsingProgramName());

        return createCollecticaseActivity;
    }

    private void createCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long corId, CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(CollecticaseConstants.USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(CollecticaseConstants.INDICATOR.N.name());
        CorrespondenceCorDAO correspondenceCorDAO = new CorrespondenceCorDAO();
        correspondenceCorDAO.setCorId(corId);
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(correspondenceCorDAO);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    public void createResendCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long cmnId) {
        CcaseCmaNoticesCmnDAO existingCmnDAO = ccaseCmaNoticesCmnRepository.findById(cmnId)
                .orElseThrow(() -> new NotFoundException("Invalid Activity Notices ID:" + cmnId, CMN_ID_NOT_FOUND));
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(CollecticaseConstants.USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(CollecticaseConstants.INDICATOR.Y.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(existingCmnDAO.getCorrespondenceCorDAO());
        ccaseCmaNoticesCmnDAO.setFkCwgId(existingCmnDAO.getFkCwgId());
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(existingCmnDAO.getCcaseCraCorrespondenceCrcDAO());
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    private void createManualCMN(CcaseActivitiesCmaDAO ccaseActivitiesCma, Long crcId, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCma);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(CollecticaseConstants.USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCma.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCma.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCma.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCma.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(CollecticaseConstants.INDICATOR.N.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(null);
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
        ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    private void processResearchNHProperty(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            ccaseActivitiesCmaList = ccaseActivitiesCmaRepository.getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(), CollecticaseConstants.REMEDY_SECOND_DEMAND_LETTER, CollecticaseConstants.ACTIVITY_TYPE_USER_ALERTED_RESEARCH_POT_LIEN);

            if (CollectionUtils.isNotEmpty(ccaseActivitiesCmaList)) {
                CcaseActivitiesCmaDAO activitiesCmaDAO = getCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO, ccaseActivitiesCmaList);
                ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
            }
            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    List.of(CollecticaseConstants.REMEDY_LIEN));
            if (!UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaNHFkCtyCd(), CollecticaseConstants.COUNTY_NONE)) {
                ccaseCaseRemedyCmrDAO.setCmrStatusCd(CollecticaseConstants.CMR_STATUS_LIEN_FILED);
                ccaseCaseRemedyCmrDAO.setCmrStageCd(CollecticaseConstants.CMR_STAGE_INEFFECT);
                ccaseCaseRemedyCmrDAO.setCmrNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_NONE);
                ccaseCaseRemedyCmrDAO.setCmrLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCaseRemedyCmrDAO.setCmrLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            } else {
                ccaseCaseRemedyCmrDAO.setCmrStatusCd(CollecticaseConstants.CMR_STATUS_UNKNOWN);
                ccaseCaseRemedyCmrDAO.setCmrStageCd(CollecticaseConstants.CMR_STAGE_INELIGIBLE);
                ccaseCaseRemedyCmrDAO.setCmrNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_NONE);
                ccaseCaseRemedyCmrDAO.setCmrLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCaseRemedyCmrDAO.setCmrLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            }
        }
    }

    @NotNull
    private CcaseActivitiesCmaDAO getCcaseActivitiesCmaDAO(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList) {
        CcaseActivitiesCmaDAO activitiesCmaDAO = ccaseActivitiesCmaList.get(0);
        activitiesCmaDAO.setCmaFollowupComplBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
        activitiesCmaDAO.setCmaFollowupComplete(INDICATOR.Y.name());
        activitiesCmaDAO.setCmaFollowCompShNote(activitiesCmaDAO.getCcaseRemedyActivityCraDAO().getCraAutoCompleteShNote());
        activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        return activitiesCmaDAO;
    }

    private void processIroraSubmission(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaDAOList;
        if (UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_IRORA_SUBMISSION, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            ccaseActivitiesCmaDAOList = ccaseActivitiesCmaRepository.getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    CollecticaseConstants.REMEDY_IRORA, CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_IB8606);
            if (CollectionUtils.isNotEmpty(ccaseActivitiesCmaDAOList)) {
                CcaseActivitiesCmaDAO activitiesCmaDAO = getCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO, ccaseActivitiesCmaDAOList);
                ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
            }
        }
    }

    private void processResearchIB(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<StaffStfDAO> staffStfDAOList = null;
        StaffStfDAO staffStfDAO = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_IB8606)
                && UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcCaseStatus(), CollecticaseConstants.CASE_STATUS_CLOSED)) {
            staffStfDAOList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong.apply(ccaseActivitiesCmaDAO.getCmaLastUpdBy()));
            if (CollectionUtils.isNotEmpty(staffStfDAOList)) {
                staffStfDAO = staffStfDAOList.get(0);
            }
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseStatus(CollecticaseConstants.CASE_STATUS_REOPEN);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCasePriority(CollecticaseConstants.CASE_PRIORITY_HI);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseNewInd(INDICATOR.Y.name());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setStaffStfDAO(staffStfDAO);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcAssignedTs(commonRepository.getCurrentTimestamp());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
            ccaseCasesCmcRepository.save(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO());

            CreateActivityDTO createActivityDTO = new CreateActivityDTO();
            CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository.
                    getCaseRemedyActivityInfo(CollecticaseConstants.ACTIVITY_TYPE_REOPEN_CASE, CollecticaseConstants.REMEDY_GENERAL);

            createActivityDTO.setCaseId(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
            if(ccaseActivitiesCmaDAO.getFkEmpIdWg() != null) {
                createActivityDTO.setEmployerId(ccaseActivitiesCmaDAO.getFkEmpIdWg());
            }
            createActivityDTO.setRemedyTypeCd(CollecticaseConstants.REMEDY_GENERAL);
            createActivityDTO.setActivityTypeCd(CollecticaseConstants.ACTIVITY_TYPE_REOPEN_CASE);
            createActivityDTO.setActivityDt(commonRepository.getCurrentDate());
            createActivityDTO.setActivityTime(CollecticaseConstants.TIME_FORMAT.format(commonRepository.getCurrentDate()));
            createActivityDTO.setActivitySpecifics(ccaseRemedyActivityCraDAO.getCraActivitySpecifics());

            createActivityDTO.setActivityCasePriority(CollecticaseConstants.CASE_PRIORITY_HI);
            createActivityDTO.setCallingUser(ccaseActivitiesCmaDAO.getCmaCreatedBy());
            createActivityDTO.setUsingProgramName(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
            createActivity(createActivityDTO);
        }
    }

    private void processReassignCaseToSelf(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<StaffStfDAO> staffList = null;
        StaffStfDAO staffDAO = null;
        CcaseCasesCmcDAO ccaseCasesCmcDAO = null;
        if (UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_ASSIGN_TO_SELF, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            staffList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong.apply(ccaseActivitiesCmaDAO.getCmaCreatedBy()));

            if (CollectionUtils.isNotEmpty(staffList)) {
                staffDAO = staffList.get(0);
            }
            ccaseCasesCmcDAO = ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO();
            if (!UtilFunction.compareLongObject.test(staffDAO.getStfId(), ccaseCasesCmcDAO.getStaffStfDAO().getStfId())) {
                ccaseCasesCmcDAO.setStaffStfDAO(staffDAO);
                ccaseCasesCmcDAO.setCmcAssignedTs(commonRepository.getCurrentTimestamp());
                ccaseCasesCmcDAO.setCmcCaseNewInd(CollecticaseConstants.INDICATOR.Y.name());
                ccaseCasesCmcDAO.setCmcLastUpdBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseCasesCmcDAO.setCmcLastUpdUsing(CollecticaseConstants.ACTIVITY_DETAILS_GENERAL);
                ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);
            }
        }
    }

    public void processAutoCompleteAct(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO = null;
        CcaseActivitiesCmaDAO activitiesCmaDAO = null;
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaDAOList = null;
        ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository.getCaseRemedyActivityInfo(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ccaseActivitiesCmaDAO.getCmaRemedyType());
        if (ccaseRemedyActivityCraDAO.getCraAutoComplete() != null) {
            CcaseRemedyActivityCraDAO finalCcaseRemedyActivityCraDAO = ccaseRemedyActivityCraDAO;
            ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository.findById(ccaseRemedyActivityCraDAO.getCraAutoComplete()).orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + finalCcaseRemedyActivityCraDAO.getCraAutoComplete(), ACTIVITY_ID_NOT_FOUND));
            ccaseActivitiesCmaDAOList = ccaseActivitiesCmaRepository.getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(), ccaseRemedyActivityCraDAO.getCraActivityCd(), ccaseRemedyActivityCraDAO.getCraRemedyCd());
            for (CcaseActivitiesCmaDAO autoActivity : ccaseActivitiesCmaDAOList) {
                if (autoActivity.getFkEmpIdWg() == null || (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), autoActivity.getFkEmpIdWg()))) {
                    activitiesCmaDAO = ccaseActivitiesCmaRepository.findById(autoActivity.getCmaId()).orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + autoActivity.getCmaId(), CASE_REMEDY_ACTIVITY_ID_NOT_FOUND));
                    activitiesCmaDAO.setCmaFollowupComplBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
                    activitiesCmaDAO.setCmaFollowupComplete(INDICATOR.Y.name());
                    activitiesCmaDAO.setCmaFollowCompShNote(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO().getCraAutoCompleteShNote());
                    activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
                    break;
                }
            }
        }
    }

    private void updatePPRemedy(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<RepaymentRpmDAO> repaymentRpmList = null;
        List<OpmPayPlanOppDAO> opmPayPlanOppList = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY)) {
            repaymentRpmList = repaymentRpmRepository.checkPaymentSincePPLetter(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId(),
                    List.of(ACTIVITY_TYPE_PP_FIXED, ACTIVITY_TYPE_PP_VARIABLE, ACTIVITY_TYPE_PP_OFFSET));
            if (CollectionUtils.isEmpty(repaymentRpmList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_INPROCESS);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_NO_PMT);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_SECOND_PP_LTR);
            }
        }
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)) {
            opmPayPlanOppList = opmPayPlanOppRepository.getOverpaymentPlanInfo(
                    ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId());
            if (CollectionUtils.isEmpty(opmPayPlanOppList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_INPROCESS);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_FA_RECIEVED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_REVIEW_FA);
            } else {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_FA_RECIEVED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_REVIEW_FA);
            }
        }
    }

    private void processClosedCasePPActivity(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO =
                ccaseRemedyActivityCraRepository.getCaseRemedyActivityInfo(REMEDY_GENERAL, ACTIVITY_TYPE_REOPEN_CASE);

        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP)
            || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT))
        {
            if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcCaseStatus(), CASE_STATUS_CLOSED)) {
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseStatus(
                        CASE_STATUS_REOPEN);
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCasePriority(
                        CASE_PRIORITY_HI);
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseOpenDt(
                        commonRepository.getCurrentDate());
                // ccaseActivitiesCma.getCcaseCasesCmc().setStaffStf("Not
                // Known");
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcAssignedTs(
                        commonRepository.getCurrentTimestamp());
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseNewInd(
                        CollecticaseConstants.INDICATOR.Y.name());
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdBy(
                        ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdUsing(
                        ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCasesCmcRepository.save(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO());


                createActivityDTO.setCaseId(ccaseActivitiesCmaDAO
                        .getCcaseCasesCmcDAO().getCmcId());
                createActivityDTO.setCallingUser(ccaseActivitiesCmaDAO
                        .getCmaCreatedBy());
                createActivityDTO.setUsingProgramName(ccaseActivitiesCmaDAO
                        .getCmaCreatedUsing());
                createActivityDTO.setActivityTypeCd(REMEDY_GENERAL);
                createActivityDTO.setRemedyTypeCd(ACTIVITY_TYPE_REOPEN_CASE);
                createActivityDTO.setActivitySpecifics(ccaseRemedyActivityCraDAO
                        .getCraActivitySpecifics());
                createActivity(createActivityDTO);
                ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDAO);
            }
        }
    }

    private void processResearchForEmp(CcaseActivitiesCmaDAO  ccaseActivitiesCmaDAO,
                                       Long employerValue) {
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT))
        {
            if (CollecticaseUtilFunction.greaterThanLongObject.test(employerValue, 0L)) {
                if (CollecticaseConstants.INDICATOR.N.name().equals(ccaseActivitiesCmaDAO
                        .getCmaWgDoNotGarnish())) {
                    ccaseActivitiesCmaDAO
                            .setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_INPROCESS);
                    ccaseActivitiesCmaDAO
                            .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_EMP_FOUND);
                    ccaseActivitiesCmaDAO
                            .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_WG_NOTICE);
                } else {
                    ccaseActivitiesCmaDAO
                            .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_DO_NOT_GARNISH);
                }
            } else if (employerValue != null && CollecticaseUtilFunction.lesserThanLongObject.test(employerValue, 0L)) {//SAT25570
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStageCd(CollecticaseConstants.CMR_STAGE_SUSPENDED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_NO_EMP);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_RESEARCH_EMP);
            }
        }
    }

    private void processChngWgAmt(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT, ccaseActivitiesCmaDAO.getCmaActivityTypeCd()))
        {
            if (CollecticaseConstants.INDICATOR.Y.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_COURT_ORDERED_WG);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_REV_WG_NOTICE);

            } else {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_WG_CHANGED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_REV_WG_NOTICE);
            }
        }
    }

    private void processSuspendWage(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<CcaseWageGarnishmentCwgDAO> ccaseWageGarnishmentCwgDAOList = ccaseWageGarnishmentCwgRepository.
                getWageInfoForOtherEmpWithStage(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                ccaseActivitiesCmaDAO.getFkEmpIdWg(), List.of(CMR_STAGE_INPROCESS, CMR_STAGE_INEFFECT));
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT, ccaseActivitiesCmaDAO.getCmaActivityTypeCd()))
        {
            if (CollecticaseConstants.INDICATOR.Y.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())
                    && CollectionUtils.isEmpty(ccaseWageGarnishmentCwgDAOList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_COURT_SUSPENDED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_SUSPENSION_LTR);
            } else if (CollecticaseConstants.INDICATOR.N.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())
                    && CollectionUtils.isEmpty(ccaseWageGarnishmentCwgDAOList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CollecticaseConstants.CMR_STATUS_ADMIN_SUSPENDED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CollecticaseConstants.CMR_NEXT_STEP_SUSPENSION_LTR);
            }
        }
    }

    private CcaseWageGarnishmentCwgDAO processWageGarnish(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseWageGarnishmentCwgDAO existingCcaseWageGarnishmentCwg = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO = null;
        Map<String, Object> paramValueMap = new HashMap<String, Object>();
        if (CollecticaseUtilFunction.greaterThanLongObject.test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), 0L)) {
            existingCcaseWageGarnishmentCwg = ccaseWageGarnishmentCwgRepository.getWageInfoForCaseEmployerRemedy(
                    ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(), ccaseActivitiesCmaDAO.getFkEmpIdWg(),
                    ccaseActivitiesCmaDAO.getCmaRemedyType());
            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));
            if (existingCcaseWageGarnishmentCwg == null) {
                //  Create Wage Garnish Data
                ccaseWageGarnishmentCwgDAO = createWageGarnish(ccaseActivitiesCmaDAO,
                        ccaseCaseRemedyCmrDAO);
            } else {
                //  Update Wage Garnish Data
                ccaseWageGarnishmentCwgDAO = updateWageGarnish(ccaseActivitiesCmaDAO,
                        existingCcaseWageGarnishmentCwg, ccaseCaseRemedyCmrDAO);
            }
        }
        return ccaseWageGarnishmentCwgDAO;
    }

    private CcaseWageGarnishmentCwgDAO createWageGarnish(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseCaseRemedyCmrDAO caseCaseRemedyCmrDAO) {
        CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO = new CcaseWageGarnishmentCwgDAO();
        populateWageData(ccaseWageGarnishmentCwgDAO, caseCaseRemedyCmrDAO, ccaseActivitiesCmaDAO);
        ccaseWageGarnishmentCwgDAO.setCwgCreatedSource(CollecticaseConstants.WAGE_GARNISH_SOURCE);
        ccaseWageGarnishmentCwgDAO.setCwgCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseWageGarnishmentCwgDAO.setCwgCreatedUsing(ccaseActivitiesCmaDAO
                .getCmaCreatedUsing());
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdUsing(ccaseActivitiesCmaDAO
                .getCmaLastUpdUsing());
        ccaseWageGarnishmentCwgRepository.save(ccaseWageGarnishmentCwgDAO);
        return ccaseWageGarnishmentCwgDAO;
    }

    private void populateWageData(CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO,
                                  CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO,
                                  CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        ccaseWageGarnishmentCwgDAO.setCcaseCaseRemedyCmrDAO(ccaseCaseRemedyCmrDAO);
        EmployerEmpDAO employerEmpDAO = new EmployerEmpDAO();
        employerEmpDAO.setEmpId(ccaseActivitiesCmaDAO.getFkEmpIdWg());
        ccaseWageGarnishmentCwgDAO.setEmployerEmpDAO(employerEmpDAO);
        ccaseWageGarnishmentCwgDAO.setCwgEmpRepCd(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd());
		/*if (ccaseActivitiesCmaDB.getCmaWgAmt() == null) {
			wageGarnish
					.setCwgWgAmount(new BigDecimal(CollectionConstants.ZERO));
		} else {
			wageGarnish.setCwgWgAmount(ccaseActivitiesCmaDB.getCmaWgAmt());
		}*/
        ccaseWageGarnishmentCwgDAO.setCwgWgAmount(ccaseActivitiesCmaDAO.getCmaWgAmt());
        if (ccaseActivitiesCmaDAO.getCmaWgDoNotGarnish() == null) {
            ccaseWageGarnishmentCwgDAO.setCwgDoNotGarnish(CollecticaseConstants.INDICATOR.N.name());
        } else {
            ccaseWageGarnishmentCwgDAO.setCwgDoNotGarnish(ccaseActivitiesCmaDAO
                    .getCmaWgDoNotGarnish());
        }
        ccaseWageGarnishmentCwgDAO.setCwgFreqCd(ccaseActivitiesCmaDAO.getCmaWgFreqCd());
        ccaseWageGarnishmentCwgDAO.setCwgNonComplCd(ccaseActivitiesCmaDAO
                .getCmaWgEmpNonCompCd());
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT, ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            ccaseWageGarnishmentCwgDAO.setCwgSuspended(CollecticaseConstants.INDICATOR.Y.name());
        } else {
            ccaseWageGarnishmentCwgDAO.setCwgSuspended(CollecticaseConstants.INDICATOR.N.name());
        }
        ccaseWageGarnishmentCwgDAO.setCwgChangeReqDt(ccaseActivitiesCmaDAO
                .getCmaWgMotionFiledOn());
        ccaseWageGarnishmentCwgDAO.setCwgChangeEffFromDt(ccaseActivitiesCmaDAO
                .getCmaWgEffectiveFrom());
        ccaseWageGarnishmentCwgDAO.setCwgChangeEffUntilDt(ccaseActivitiesCmaDAO
                .getCmaWgEffectiveUntil());
        ccaseWageGarnishmentCwgDAO.setFkCmoIdCourt(ccaseActivitiesCmaDAO.getFkCmoIdCourtWg());
        ccaseWageGarnishmentCwgDAO.setCwgCourtOrdered(ccaseActivitiesCmaDAO
                .getCmaWgCourtOrdered());
        ccaseWageGarnishmentCwgDAO.setCwgCourtOrderDt(ccaseActivitiesCmaDAO
                .getCmaWgCourtOrderDt());
        ccaseWageGarnishmentCwgDAO.setCwgLastActivityDt(commonRepository.getCurrentDate());
        ccaseWageGarnishmentCwgDAO.setCwgStatusCd(ccaseActivitiesCmaDAO.getCmaRemedyStatusCd());
        ccaseWageGarnishmentCwgDAO.setCwgStageCd(ccaseActivitiesCmaDAO.getCmaRemedyStageCd());
        ccaseWageGarnishmentCwgDAO.setCwgNextStepCd(ccaseActivitiesCmaDAO
                .getCmaRemedyNextStepCd());
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(), CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY)
               || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(), CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO)) {
            ccaseWageGarnishmentCwgDAO.setFkCmoIdRep(ccaseActivitiesCmaDAO
                    .getCmaEmpRepTypeIfk());
        }
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(), CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMI)) {
            ccaseWageGarnishmentCwgDAO.setFkCmiIdRep(ccaseActivitiesCmaDAO
                    .getCmaEmpRepTypeIfk());
        }
        if (!UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getFkCmiIdWgEmp(), -1L)) {
            ccaseWageGarnishmentCwgDAO.setFkCmiIdEmpCont(ccaseActivitiesCmaDAO
                    .getFkCmiIdWgEmp());
        }
    }

    private CcaseWageGarnishmentCwgDAO updateWageGarnish(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO ,
            CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO) {
        populateWageData(ccaseWageGarnishmentCwgDAO, ccaseCaseRemedyCmrDAO, ccaseActivitiesCmaDAO);
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdUsing(ccaseActivitiesCmaDAO
                .getCmaLastUpdUsing());
        ccaseWageGarnishmentCwgRepository.save(ccaseWageGarnishmentCwgDAO);
        return ccaseWageGarnishmentCwgDAO;
    }

    public void processCompleteForWgInitiateEmpNC(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseActivitiesCmaDAO activitiesCmaDAO = null;
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList = null;
        if(UtilFunction.compareLongObject.test(CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())){
            ccaseActivitiesCmaList = ccaseActivitiesCmaRepository.getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                    CollecticaseConstants.REMEDY_WAGE_GARNISHMENT,
                    CollecticaseConstants.ACTIVITY_TYPE_USER_ALERT_INITIATE_EMP_NC);
            for (CcaseActivitiesCmaDAO ccaseActivitiesCma : ccaseActivitiesCmaList) {
                if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getFkEmpIdRepUc(), ccaseActivitiesCma.getFkEmpIdWg())) {
                    activitiesCmaDAO = ccaseActivitiesCmaRepository.findById(ccaseActivitiesCma.getCmaId())
                            .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + ccaseActivitiesCma.getCmaId(), ACTIVITY_ID_NOT_FOUND));;
                    activitiesCmaDAO.setCmaFollowupComplBy(CollecticaseConstants.SYSTEM_USER_ID);
                    activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
                    activitiesCmaDAO
                            .setCmaFollowupComplete(CollecticaseConstants.INDICATOR.Y.name());
                    activitiesCmaDAO.setCmaFollowCompShNote(EMP_DIS_ASSOCIATE_SHORT_NOTE);
                    activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO
                            .getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO
                            .getCmaLastUpdUsing());
                    ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
                }
            }
        }
    }

    public List<Map<String, Object>> prepareCorrespondenceMapFromDTO(
            GeneralActivityDTO generalActivityDTO) {
        List<Map<String, Object>> correspMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO = null;
        if (generalActivityDTO.getActivitySendCorrespondence() != null) {
            for (String sendNotice : generalActivityDTO.getActivitySendCorrespondence()) {
                paramMap = new HashMap<String, Object>();
                ccaseCraCorrespondenceCrcDAO = ccaseCraCorrespondenceCrcRepository.findById(UtilFunction.stringToLong.apply(sendNotice))
                    .orElseThrow(() -> new NotFoundException("Invalid CRC ID:" + UtilFunction.stringToLong.apply(sendNotice), CRC_ID_NOT_FOUND));
                paramMap.put(CollecticaseConstants.PIN_CRC_ID,ccaseCraCorrespondenceCrcDAO.getCrcId());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_RPT_ID,
                        ccaseCraCorrespondenceCrcDAO.getReportsRptDAO().getRptId().intValue());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_CLM_ID,
                        clmLofClfRepository.getClaimLocalOfficeByClaimantId(generalActivityDTO.getClaimantId(),
                                INDICATOR.Y.name()));
                if(generalActivityDTO.getActivityEntityContact() != null
                        && CollecticaseUtilFunction.greaterThanLongObject.test(
                                UtilFunction.stringToLong.apply(generalActivityDTO.getActivityEntityContact()),0L))//SAT25570
                {
                    paramMap.put(CollecticaseConstants.PIN_EMP_ID,
                            UtilFunction.stringToLong.apply(generalActivityDTO.getActivityEntityContact()));
                }
                else
                {
                    paramMap.put(CollecticaseConstants.PIN_EMP_ID,null);
                }
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_CMT_ID,
                        generalActivityDTO.getCaseId());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_COR_COE_IND,
                        INDICATOR.Y.name());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_FORCED_IND,
                        INDICATOR.N.name());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_COR_STATUS_CD,
                        CollecticaseConstants.COR_STATUS_NOT_PROCESSED);
                paramMap.put(
                        CollecticaseConstants.PIN_WLP_I720_COR_DEC_ID_IFK,
                        null);
                paramMap.put(
                        CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK,
                        generalActivityDTO.getClaimantId());
                if (UtilFunction.compareLongObject.test(CollecticaseConstants.REMEDY_WAGE_GARNISHMENT, generalActivityDTO.getActivityRemedyTypeCd())) {
                    if (generalActivityDTO.getActivityEntityContact() != null
                            && CollecticaseUtilFunction.greaterThanLongObject.test(
                            UtilFunction.stringToLong.apply(generalActivityDTO.getActivityEntityContact()),0L)) {
                        paramMap.put(
                                CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK,
                                UtilFunction.stringToLong.apply(generalActivityDTO.getActivityEntityContact()));
                    }
                }
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_CD,
                        CollecticaseConstants.COR_RECEIPT_CLAIMANT);
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_COR_TS,
                        commonRepository.getCurrentTimestamp());
                paramMap.put(CollecticaseConstants.PIN_WLP_I720_COE_STRING,
                        processCOEString(generalActivityDTO));
                paramMap.put(CollecticaseConstants.POUT_WLP_O720_COR_ID, 0L);
                paramMap
                        .put(CollecticaseConstants.POUT_WLP_O720_RETURN_CD, 0L);
                paramMap.put(CollecticaseConstants.POUT_WLP_O720_RETURN_MSG,
                        null);
                correspMapList.add(paramMap);
            }
        }
        return correspMapList;
    }

    public String processCOEString(
            GeneralActivityDTO generalActivityDTO) {
        String coeString = StringUtils.EMPTY;
        CaseCollectibleDebtsDTO caseCollectibleDebtsDTO = null;
        Map<String, Object> paramValueMap = new HashMap<String, Object>();
        BigDecimal opBalAmt = new BigDecimal("0.0");
        BigDecimal opFrdBalAmt = new BigDecimal("0.0");
        BigDecimal opNFBalAmt = new BigDecimal("0.0");
        BigDecimal opIntAmt = new BigDecimal("0.0");
        caseCollectibleDebtsDTO = vwCcaseCollectibleDebtsRepository.getCollectibleDebtsAmount(generalActivityDTO.getClaimantId(),
                BigDecimal.ZERO);
        coeString = coeString + CollecticaseConstants.ONLINE_COE_TXT;
        if (caseCollectibleDebtsDTO != null) {
            opBalAmt = caseCollectibleDebtsDTO.getOverpaymentBalanceAmount();
            opFrdBalAmt = caseCollectibleDebtsDTO.getOverpaymentFraudBalanceAmount();
            opNFBalAmt = caseCollectibleDebtsDTO.getOverpaymentNonFraudBalanceAmount();
            opIntAmt = caseCollectibleDebtsDTO.getOverpaymentInterestBalanceAmount();

        }
        coeString = coeString
                + CollecticaseConstants.TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opBalAmt)
                + CollecticaseConstants.TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opFrdBalAmt)
                + CollecticaseConstants.TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opNFBalAmt)
                + CollecticaseConstants.TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opIntAmt);
        return coeString;
    }

    public List<Long> getCaseRemedyActivityByCaseId(
            Long caseId, String activeIndicator) {
        return ccaseRemedyActivityCraRepository.getCaseRemedyActivityByCaseId(caseId, activeIndicator);
    }

    public List<RemedyActivityDTO> getRemedyActivityByCaseRemedyId(List<Long> caseRemedyId, String activeIndicator)
    {
        return ccaseRemedyActivityCraRepository.getCaseRemedyActivityByCaseRemedyId(caseRemedyId, activeIndicator);
    }

}
