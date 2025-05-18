package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.repository.StateRepository;
import com.ssi.ms.collecticase.database.repository.VwCcaseCountyCtyRepository;
import com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO;
import com.ssi.ms.collecticase.dto.AlvDescResDTO;
import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.EmployerContactListDTO;
import com.ssi.ms.collecticase.dto.OrganizationIndividualDTO;
import com.ssi.ms.collecticase.dto.FollowupActivityDTO;
import com.ssi.ms.collecticase.dto.AllowValAlvResDTO;
import com.ssi.ms.collecticase.dto.StateDTO;
import com.ssi.ms.collecticase.factorybean.ResponseFactory;
import com.ssi.ms.collecticase.factorybean.ResponseTypes;
import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityUpdateContactPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
import com.ssi.ms.collecticase.util.CollecticaseHelper;
import com.ssi.ms.collecticase.util.CollectionUtility;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class ActivityService extends CollecticaseBaseService {

    private final Map<ResponseTypes, ResponseFactory<?>> factoryMap = new HashMap<>();

    @Autowired
    VwCcaseCountyCtyRepository vwCcaseCountyCtyRepository;

    @Autowired
    StateRepository stateRepository;

    public ActivityService(List<ResponseFactory<?>> factories) {
        for (ResponseFactory<?> factory : factories) {
            factoryMap.put(factory.getType(), factory);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getResponse(ResponseTypes type) {
        ResponseFactory<T> factory = (ResponseFactory<T>) factoryMap.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for type: " + type);
        }
        return factory.createResponse();
    }

    public ActivityGeneralPageResponse getGeneralActivityPage(Long caseId, Long activityRemedyCd,
                                                              Long activityTypeCd) {
        // Initialize
        ActivityGeneralPageResponse activityGeneralPageResponse =
                getResponse(ResponseTypes.ActivityGeneralPageResponse);
        String activityHeaderName = null;

        // Characteristics Details
        CcaseCasesCmcDTO ccaseCasesCmcDTO = ccaseCasesCmcRepository.getCaseCmcByCaseId(caseId);
        // Activity short desc
        AlvDescResDTO shortDescByActivityCd = allowValAlvRepository.getShortDescByAlc(activityTypeCd);
        // Remedy short desc
        AlvDescResDTO shortDescByRemedyCd = allowValAlvRepository.getShortDescByAlc(activityRemedyCd);

        if (shortDescByActivityCd != null && shortDescByRemedyCd != null) {
            activityHeaderName = shortDescByRemedyCd.getDesc() + " - " + shortDescByActivityCd.getDesc();
        }

        // Follow up - Short Note call
        CcaseRemedyActivityCraDAO caseRemedyActivityInfo = ccaseRemedyActivityCraRepository
                .getCaseRemedyActivityInfo(activityTypeCd, activityRemedyCd);
        // Property Lien
        CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy = ccaseCaseRemedyCmrRepository
                .getCaseRemedyByCaseRemedy(caseId, List.of(activityRemedyCd));

        // Setting in Response
        if (caseRemedyActivityInfo != null) {
            if (caseRemedyActivityInfo.getCraFollowUpDays() != null
                    && caseRemedyActivityInfo.getCraFollowUpDays() > 0) {
                Date currDate = commonRepository.getCurrentDate();
                activityGeneralPageResponse.setActivityFollowupDate(CollectionUtility
                        .addDaysToDate(currDate,
                                caseRemedyActivityInfo.getCraFollowUpDays()));
            }
            activityGeneralPageResponse.setActivityFollowupShortNote(caseRemedyActivityInfo
                    .getCraFollowUpShNote());
        }
        if (CollectionUtility.compareLongValue(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_IB8606)) {
            activityGeneralPageResponse.setDisableFollowupDate(true);
            activityGeneralPageResponse.setDisableFollowupShNote(true);
        }
        activityGeneralPageResponse.setPropertyLien(caseRemedyByCaseRemedy != null ?
                caseRemedyByCaseRemedy.getCmrGnFkCtyCd() : null);
        activityGeneralPageResponse.setActivityHeaderName(activityHeaderName);
        activityGeneralPageResponse.setActivityDate(commonRepository.getCurrentDate());
        activityGeneralPageResponse.setActivityTime(CollecticaseConstants.TIME_FORMAT
                .format(commonRepository.getCurrentDate()));
        activityGeneralPageResponse.setActivityCaseCharacteristics(ccaseCasesCmcDTO.getCaseCharacteristics());
        activityGeneralPageResponse.setActivityClaimantRepresentative(ccaseCasesCmcDTO.getCmtRepTypeCd());

        return activityGeneralPageResponse;
    }

    public ActivityEntityContactResponse getEntityContactActivityPage(Long caseId, Long activityTypeCd) {
        // Initialize
        ActivityEntityContactResponse activityEntityContactResponse =
                getResponse(ResponseTypes.ActivityEntityContactResponse);
        // Entity Contact Details
        List<VwCcaseEntityDTO> entityContactList = vwCcaseEntityRepository.getEntityContactList(caseId,
                CollecticaseConstants.YES);
        // Setting in Response
        activityEntityContactResponse.setEntityList(entityContactList);
        activityEntityContactResponse.setDisableEntityContact(true);
        if (CollectionUtility.compareLongValue(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_OTHER_ENTITY_CONTACT)) {
            activityEntityContactResponse.setDisableEntityContact(false);
        }

        return activityEntityContactResponse;
    }

    public ActivityPropertyLienResponse getPropertyLienActivityPage(Long activityRemedyCd, Long caseId) {
        // Initialize
        ActivityPropertyLienResponse activityPropertyLienResponse =
                getResponse(ResponseTypes.ActivityPropertyLienResponse);
        // Property Lien Details
        List<CcaseCountyCtyDTO> propertyLienActivityPage = vwCcaseCountyCtyRepository
                .getPropertyLienActivityPage(CollecticaseConstants.STATE_OF_NEW_HAMPSHIRE);

        CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy = ccaseCaseRemedyCmrRepository
                .getCaseRemedyByCaseRemedy(caseId, List.of(activityRemedyCd));

        // Setting in Response
        activityPropertyLienResponse.setCountyCodesCtyList(propertyLienActivityPage);
        activityPropertyLienResponse.setPropertyLien(caseRemedyByCaseRemedy != null ?
                caseRemedyByCaseRemedy.getCmrGnFkCtyCd() : null);

        return activityPropertyLienResponse;
    }

    public ActivityFollowUpShortNoteResponse getFollowUpShortNoteActivityPage(Long activityTypeCd,
                                                                              Long activityRemedyCd) {
        // Initialize
        ActivityFollowUpShortNoteResponse activityFollowUpShortNoteResponse =
                getResponse(ResponseTypes.ActivityFollowUpShortNoteResponse);
        // Follow up - Short Note call
        CcaseRemedyActivityCraDAO caseRemedyActivityInfo = ccaseRemedyActivityCraRepository
                .getCaseRemedyActivityInfo(activityTypeCd, activityRemedyCd);

        // Setting in Response
        if (caseRemedyActivityInfo != null) {
            if (caseRemedyActivityInfo.getCraFollowUpDays() != null
                    && caseRemedyActivityInfo.getCraFollowUpDays() > 0) {
                Date currDate = commonRepository.getCurrentDate();
                activityFollowUpShortNoteResponse.setActivityFollowupDate(CollectionUtility
                        .addDaysToDate(currDate,
                                caseRemedyActivityInfo.getCraFollowUpDays()));
            }
            activityFollowUpShortNoteResponse.setActivityFollowupShortNote(caseRemedyActivityInfo
                    .getCraFollowUpShNote());
        }
        if (CollectionUtility.compareLongValue(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_IB8606)) {
            activityFollowUpShortNoteResponse.setDisableFollowupDate(true);
            activityFollowUpShortNoteResponse.setDisableFollowupShNote(true);
        }

        return activityFollowUpShortNoteResponse;
    }

    public ActivitySendReSendResponse getSendReSendActivityPage(Long caseId, Long activityRemedyCd,
                                                                Long activityTypeCd) {
        // Initialize
        ActivitySendReSendResponse activitySendReSendResponse = getResponse(ResponseTypes.ActivitySendReSendResponse);
        // Setting for Send Correspondence
        List<String> activeCorrespondenceList = List.of(CollecticaseConstants.YES);
        List<String> manualCorrespondenceList = List.of(CollecticaseConstants.NO);

        // Setting the value from Request
        List<Long> activityCdList = List.of(activityTypeCd);
        List<Long> remedyCdList = List.of(activityRemedyCd);

        List<CcaseCraCorrespondenceCrcDTO> sendCorrespondenceForActivityList = ccaseCraCorrespondenceCrcRepository
                .getSendCorrespondenceForActivityRemedy(activeCorrespondenceList, manualCorrespondenceList,
                        activityCdList, remedyCdList).stream()
                .map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao)).toList();

        // Setting/Overwrite for Manual Correspondence
        manualCorrespondenceList = List.of(CollecticaseConstants.YES);

        List<CcaseCraCorrespondenceCrcDTO> manualCorrespondenceForActivityList = ccaseCraCorrespondenceCrcRepository
                .getManualCorrespondenceForRemedy(activeCorrespondenceList, manualCorrespondenceList,
                        remedyCdList).stream()
                .map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao)).toList();

        List<CcaseCmaNoticesCmnDAO> ccaseCmaNoticesCmnDAOList = ccaseCraCorrespondenceCrcRepository
                .getResendNoticesByCaseId(caseId, activityRemedyCd, CollecticaseConstants.YES);

        List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList = new ArrayList<>();
        for (CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO : ccaseCmaNoticesCmnDAOList) {
            ccaseCraCorrespondenceCrcDAOList.add(ccaseCmaNoticesCmnDAO.getCcaseCraCorrespondenceCrcDAO());
        }
        List<CcaseCraCorrespondenceCrcDTO> resendCorrespondenceForActivityList = ccaseCraCorrespondenceCrcDAOList
                .stream().map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao))
                .toList();

        // Setting the Response
        activitySendReSendResponse.setSendNoticesCrcList(sendCorrespondenceForActivityList);
        activitySendReSendResponse.setManualNoticesCrcList(manualCorrespondenceForActivityList);
        activitySendReSendResponse.setResendNoticesCrcList(resendCorrespondenceForActivityList);

        return activitySendReSendResponse;
    }

    public ActivityPaymentPlanPageResponse getPaymentPlanActivityPage(Long caseId, Long activityRemedyCd,
                                                                      Long activityTypeCd) {

        ActivityGeneralPageResponse activityGeneralPageResponse = getGeneralActivityPage(caseId, activityRemedyCd,
                activityTypeCd);

        ActivityPaymentPlanPageResponse activityPaymentPlanPageResponse =
                getResponse(ResponseTypes.ActivityPaymentPlanPageResponse);
        activityPaymentPlanPageResponse.setActivityGeneralPageResponse(activityGeneralPageResponse);

        CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy = ccaseCaseRemedyCmrRepository
                .getCaseRemedyByCaseRemedy(caseId, List.of(activityRemedyCd));

        List<Long> guideLineFinAffList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP,
                CollecticaseConstants.ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT);


        if (guideLineFinAffList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanGuideLineAmount(BigDecimal.ZERO);
        } else {
            activityPaymentPlanPageResponse.setPaymentPlanResponseToCd(caseRemedyByCaseRemedy.getCmrPpRespToCd());
            activityPaymentPlanPageResponse.setPaymentPlanResponseToOther(caseRemedyByCaseRemedy.getCmrPpRespToOther());
            activityPaymentPlanPageResponse.setPaymentPlanGuideLineAmount(caseRemedyByCaseRemedy.getCmrPpGuidelineAmt());
            activityPaymentPlanPageResponse.setDisablePPResponseToCd(true);
            activityPaymentPlanPageResponse.setDisablePPResponseToOther(true);
            activityPaymentPlanPageResponse.setDisablePPGuideLineAmount(true);
        }
        if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)) {
            if (caseRemedyByCaseRemedy.getCmrPpGuidelineAmt() == null) {
                activityPaymentPlanPageResponse.setPaymentPlanGuideLineAmount(BigDecimal.ZERO);
            }
            activityPaymentPlanPageResponse.setDisablePPGuideLineAmount(false);
        }
        //populateEnableDisablePPSignedDt
        CollecticaseHelper.populateEnableDisablePPSignedDt(activityTypeCd, activityPaymentPlanPageResponse,
                caseRemedyByCaseRemedy);
        //populateEnableDisableFASignedOn
        CollecticaseHelper.populateEnableDisableFASignedOn(activityTypeCd, activityPaymentPlanPageResponse,
                caseRemedyByCaseRemedy);
        //populateEnableDisablePPPaymentAmt
        CollecticaseHelper.populateEnableDisablePPPaymentAmt(activityTypeCd, activityPaymentPlanPageResponse,
                caseRemedyByCaseRemedy);
        //populatePPPaymentCatCd
        CollecticaseHelper.populatePPPaymentCatCd(activityTypeCd, activityPaymentPlanPageResponse,
                caseRemedyByCaseRemedy);

        return activityPaymentPlanPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getWageGarnishmentActivityPage(Long caseId, Long activityRemedyCd,
                                                                              Long activityTypeCd) {
        // getGeneralActivityPage call
        ActivityGeneralPageResponse activityGeneralPageResponse =
                getGeneralActivityPage(caseId, activityRemedyCd, activityTypeCd);
        // ActivityWageGarnishmentPageResponse get bean object
        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);

        // Employer call
        List<EmployerListDTO> employerListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);

        // Setting in Response
        activityWageGarnishmentPageResponse.setActivityGeneralPageResponse(activityGeneralPageResponse);
        activityWageGarnishmentPageResponse.setEmployerList(employerListForWageGarnish);

        return activityWageGarnishmentPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getEmployerContactWageGarnish(Long caseId, Long employerId) {

        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);

        List<EmployerContactListDTO> employerContactListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerContactListForWageGarnish(caseId, employerId, CollecticaseConstants.YES);

        activityWageGarnishmentPageResponse.setEmployerContactList(employerContactListForWageGarnish);

        return activityWageGarnishmentPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getEmployerRepWageGarnish(Long caseId, Long employerId) {

        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);

        List<OrganizationIndividualDTO> organizationInfoList = ccaseEntityCmeRepository.getOrganizationInfoByEmpId(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO), employerId);

        List<OrganizationIndividualDTO> inidividualInfoList = ccaseEntityCmeRepository.getIndividualInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMI,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO));

        List<AllowValAlvResDTO> alvValList = allowValAlvRepository.getActiveAlvsByAlc(
                CollecticaseConstants.CATEGORY_CCASE_EMPLOYER_REP).stream()
                .map(dao -> allowValAlvMapper.daoToShortDescDto(dao)).toList();

        List<OrganizationIndividualDTO> empRepList = new ArrayList<>();
        empRepList.addAll(organizationInfoList);
        empRepList.addAll(inidividualInfoList);
        for (AllowValAlvResDTO allowValAlvResDTO : alvValList) {
            empRepList.add(new OrganizationIndividualDTO
                    (allowValAlvResDTO.getConstId().toString(), allowValAlvResDTO.getConstShortDesc()));
        }

        activityWageGarnishmentPageResponse.setEmployerRepList(empRepList);

        return activityWageGarnishmentPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getEmployerWageGarnish(Long caseId) {

        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);

        List<EmployerListDTO> employerListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);

        activityWageGarnishmentPageResponse.setEmployerList(employerListForWageGarnish);
        return activityWageGarnishmentPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getWageGarnishOther(Long caseId, Long employerId, Long activityTypeCd,
                                                                   Long activityRemedyCd) {

        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);

        CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy = ccaseWageGarnishmentCwgRepository
                .getWageInfoForCaseEmployerRemedy(caseId, employerId, activityRemedyCd);

        if (wageInfoForCaseEmployerRemedy != null) {
            //populateEnableDisableWGAmount
            CollecticaseHelper.populateEnableDisableWGAmount(activityTypeCd, activityWageGarnishmentPageResponse,
                        wageInfoForCaseEmployerRemedy);
            //populateEnableDisableDoNotGarnish
            CollecticaseHelper.populateEnableDisableDoNotGarnish(activityTypeCd, activityWageGarnishmentPageResponse,
                        wageInfoForCaseEmployerRemedy);
            //populateEnableDisableWGFreq
            CollecticaseHelper.populateEnableDisableWGFreq(activityWageGarnishmentPageResponse,
                        wageInfoForCaseEmployerRemedy);
            //populateEnableDisableNonComp
            CollecticaseHelper.populateEnableDisableNonComp(activityTypeCd, activityWageGarnishmentPageResponse);
            //populateEnableDisableReqDate
            CollecticaseHelper.populateEnableDisableReqDate(activityTypeCd, activityWageGarnishmentPageResponse,
                        wageInfoForCaseEmployerRemedy);
            //populateEnableDisableEffDate
            CollecticaseHelper.populateEnableDisableEffDate(activityTypeCd, activityWageGarnishmentPageResponse);
            //populateEnableDisableCourt
            CollecticaseHelper.populateEnableDisableCourt(activityTypeCd, activityWageGarnishmentPageResponse,
                        wageInfoForCaseEmployerRemedy);
            //populateEnableDisableCourtOrder
            CollecticaseHelper.populateEnableDisableCourtOrder(activityTypeCd, activityWageGarnishmentPageResponse);
            //populateEnableDisableCourtOrderDt
            CollecticaseHelper.populateEnableDisableCourtOrderDt(activityTypeCd, activityWageGarnishmentPageResponse);
        }

        return activityWageGarnishmentPageResponse;
    }

    public ActivityUpdateContactPageResponse getUpdateContactActivityPage(Long caseId, Long activityRemedyCd,
                                                                          Long activityTypeCd) {
        // Bak TODO need to decide and push all the calls here - static drop down list
        return null;
    }

    public FollowupActivityDTO getActivityInfoForFollowup(Long activityId) {
        return ccaseActivitiesCmaRepository.getActivityInfoForFollowup(activityId);
    }

    public List<Map<String, String>> getUpdateContactCountry() {
        return CollecticaseHelper.getUpdateContactCountry();
    }

    public List<StateDTO> getUpdateContactState(Long countryId) {
        String countryValue = CollecticaseConstants.NO;
        if (CollectionUtility.compareLongValue(
                CollecticaseConstants.UNITED_STATES, countryId)) {
            countryValue = CollecticaseConstants.NO;
        } else if (CollectionUtility.compareLongValue(
                CollecticaseConstants.CANANDA, countryId)) {
            countryValue = CollecticaseConstants.YES;
        }
        return stateRepository.getStates(countryValue);
    }

    public Map<String, String> getUpdateContactEntity(Long caseId, Long activityTypeCd) {

        List<EmployerListDTO> employerListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);

        Map<String, String> entityMap = new LinkedHashMap<>();

        if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_ADD_UPD_EMP_CONTACT)) {
            populateEntityForEmp(employerListForWageGarnish, entityMap);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_ADD_UPD_ATTY_CONTACT)) {
            populateEntityForAtty(caseId, entityMap);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_ADD_UPD_OTHER_REP_CONTACT)) {
            populateEntityForRep(caseId, entityMap);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE)) {
            populateEntityForCaseDisassociate(caseId, entityMap);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT)) {
            populateEntityForOrgDisassociate(caseId, entityMap);
        }

        return entityMap;
    }

    public List<EmployerListDTO> getUpdateContactRep(Long caseId) {
        return ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);
    }

    public List<OrganizationIndividualDTO> getUpdateContactContacts(Long caseId, Long entityId) {
        return ccaseEntityCmeRepository.getIndividualInfoByEntityId(caseId, CollecticaseConstants.YES, entityId);
    }

    public Map<String, String> getGeneralActivityGo(Long activityTypeCd, Long activityRemedyCd) {
        Map<String, String> templateMap = new HashMap<>();
        CcaseRemedyActivityCraDAO caseRemedyActivityInfo = ccaseRemedyActivityCraRepository
                                                        .getCaseRemedyActivityInfo(activityTypeCd, activityRemedyCd);
        if (caseRemedyActivityInfo != null) {
            templateMap.put(CollecticaseConstants.ACTIVITY_TEMPLATE_NAME, caseRemedyActivityInfo.getCraTemplatePage());
        }
        return templateMap;
    }

    private void populateEntityForOrgDisassociate(Long caseId, Map<String, String> entityMap) {
        //        DISASSOCIATE_ORGANIZATIONAL_CONTACT_ACTIVITY
        List<EmployerListDTO> employerListGarnish = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);
        List<OrganizationIndividualDTO> orgInfoList = ccaseEntityCmeRepository.getOrganizationInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO));

        for (EmployerListDTO employerListDTO : employerListGarnish) {
            entityMap.put(CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.NON_REGEX_SEPARATOR
                            + employerListDTO.getEmployerName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + employerListDTO.getEmployerId(),
                    CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.COLON
                            + employerListDTO.getEmployerName());
        }
        for (OrganizationIndividualDTO organizationDTO : orgInfoList) {
            entityMap.put(organizationDTO.getEmpRepName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + organizationDTO.getEmpRepId(),
                    organizationDTO.getEmpRepName());
        }
    }

    private void populateEntityForCaseDisassociate(Long caseId, Map<String, String> entityMap) {
        //        ACT_TYPE_DISASSOCIATE_ORG_FROM_CASE
        List<EmployerListDTO> employerListForWageGarnishDis = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP);
        List<OrganizationIndividualDTO> organizationInfoListDis = ccaseEntityCmeRepository.getOrganizationInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO));
        List<OrganizationIndividualDTO> iniInfoListDis = ccaseEntityCmeRepository.getIndividualInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_EMP,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMI,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO));
        for (EmployerListDTO employerListDTO : employerListForWageGarnishDis) {
            entityMap.put(CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.NON_REGEX_SEPARATOR
                            + employerListDTO.getEmployerName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + employerListDTO.getEmployerId(),
                    CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.COLON
                            + employerListDTO.getEmployerName());
        }
        for (OrganizationIndividualDTO organizationIndividualDTO : organizationInfoListDis) {
            entityMap.put(
                    organizationIndividualDTO.getEmpRepName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + organizationIndividualDTO.getEmpRepId(),
                    organizationIndividualDTO.getEmpRepName());
        }
        for (OrganizationIndividualDTO organizationDTO : iniInfoListDis) {
            entityMap.put(
                    organizationDTO.getEmpRepName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + organizationDTO.getEmpRepId(),
                    organizationDTO.getEmpRepName());
        }
    }

    private void populateEntityForRep(Long caseId, Map<String, String> entityMap) {
        //        ACT_TYPE_ADD_UPD_OTHER_REP_CONTACT
        List<OrganizationIndividualDTO> organizationInfoList = ccaseEntityCmeRepository.getOrganizationInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO));
        List<OrganizationIndividualDTO> inidividualInfoList = ccaseEntityCmeRepository.getIndividualInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMO,
                        CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_REP_CMI));

        for (OrganizationIndividualDTO organizationIndividualDTO : organizationInfoList) {
            entityMap.put(
                    organizationIndividualDTO.getEmpRepName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + organizationIndividualDTO.getEmpRepId(),
                    organizationIndividualDTO.getEmpRepName());
        }

        for (OrganizationIndividualDTO organizationIndividualDTO : inidividualInfoList) {
            entityMap.put(
                    organizationIndividualDTO.getEmpRepName() + CollecticaseConstants.NON_REGEX_SEPARATOR
                            + organizationIndividualDTO.getEmpRepId(),
                    organizationIndividualDTO.getEmpRepName());
        }
    }

    private void populateEntityForAtty(Long caseId, Map<String, String> entityMap) {
        //        ACT_TYPE_ADD_UPD_ATTY_CONTACT
        List<OrganizationIndividualDTO> attorneyInfoList = ccaseEntityCmeRepository.getOrganizationInfo(caseId,
                CollecticaseConstants.YES, List.of(CollecticaseConstants.CASE_ENTITY_CONTACT_TYPE_ATTY));
        for (OrganizationIndividualDTO organizationIndividualDTO : attorneyInfoList) {
            entityMap.put(
                    organizationIndividualDTO.getEmpRepName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR
                            + organizationIndividualDTO.getEmpRepId(),
                    organizationIndividualDTO.getEmpRepName());
        }
    }

    private static void populateEntityForEmp(List<EmployerListDTO> employerListForWageGarnish,
                                             Map<String, String> entityMap) {
        //ACT_TYPE_ADD_UPD_EMP_CONTACT
        for (EmployerListDTO employerListDTO : employerListForWageGarnish) {
            entityMap.put(CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.NON_REGEX_SEPARATOR
                            + employerListDTO.getEmployerName()
                            + CollecticaseConstants.NON_REGEX_SEPARATOR + employerListDTO.getEmployerId(),
                    CollecticaseConstants.EMP_STRING
                            + CollecticaseConstants.COLON
                            + employerListDTO.getEmployerName());
        }
    }
}