package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmeIndividualCmiDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseEntityDAO;
import com.ssi.ms.collecticase.database.dao.CcaseEntityCmeDAO;
import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.EmployerEmpDAO;
import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCollectibleDebtsDAO;
import com.ssi.ms.collecticase.database.dao.CmtNotesCnoDAO;
import com.ssi.ms.collecticase.database.dao.CcaseOrganizationCmoDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCasesCmcDAO;
import com.ssi.ms.collecticase.database.dao.AllowValAlvDAO;
import com.ssi.ms.collecticase.database.dao.StaffStfDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import com.ssi.ms.collecticase.database.dao.RepaymentRpmDAO;
import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import com.ssi.ms.collecticase.database.repository.StateRepository;
import com.ssi.ms.collecticase.database.repository.VwCcaseCountyCtyRepository;
import com.ssi.ms.collecticase.dto.CreateActivityDTO;
import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.dto.WageGarnishmentActivityDTO;
import com.ssi.ms.collecticase.dto.UpdateContactActivityDTO;
import com.ssi.ms.collecticase.dto.PaymentPlanActivityDTO;
import com.ssi.ms.collecticase.dto.FollowupActivityDTO;
import com.ssi.ms.collecticase.dto.StateDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.OrganizationIndividualDTO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import com.ssi.ms.collecticase.dto.CompleteFollowupActivityDTO;
import com.ssi.ms.collecticase.dto.AppendNotesDTO;

import com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO;
import com.ssi.ms.collecticase.dto.AlvDescResDTO;
import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import com.ssi.ms.collecticase.dto.EmployerContactListDTO;
import com.ssi.ms.collecticase.dto.AllowValAlvResDTO;
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
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.collecticase.util.CollectionUtility;
import com.ssi.ms.common.database.dao.UserDAO;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.platform.util.DateUtil;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.Objects;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.*;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.*;
import static com.ssi.ms.collecticase.util.CollecticaseHelper.fornattedActivityNotes;
import static com.ssi.ms.collecticase.util.CollecticaseHelper.splitNotesAndAddlNotes;

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

    public Object searchOrgLookup(@Valid OrgLookupDTO orgLookupDTO) {
        List<GTTForOrgLookupDAO> gttForOrgLookupDAOS;
        if (CollecticaseConstants.EMPLOYER_ENTITY_TYPE.equals(orgLookupDTO.getEntityType())) {
            gttForOrgLookupDAOS = customLookupRepository.processOrgLookupEmpQuery(orgLookupDTO);
        } else if (CollecticaseConstants.ATTORNEY_ENTITY_TYPE.equals(orgLookupDTO.getEntityType()) ||
                CollecticaseConstants.REPRESENTATIVE_ENTITY_TYPE.equals(orgLookupDTO.getEntityType())) {
            gttForOrgLookupDAOS = customLookupRepository.processOrgLookupOrgEmpQuery(orgLookupDTO);
        }


        return null;
    }

    public void completeFollowupActivity(CompleteFollowupActivityDTO completeFollowupActivityDTO)
    {
        Long userId = UtilFunction.stringToLong.apply(completeFollowupActivityDTO.getUserId());
        Long activityId = UtilFunction.stringToLong.apply(completeFollowupActivityDTO.getActivityId());
        UserDAO userDAO = userRepository.findByUserId(UtilFunction.stringToLong.apply(completeFollowupActivityDTO.getUserId()));
        List<StaffStfDAO> staffStfDAOList = staffStfRepository.getStaffInfoByUserId(userId);
        StaffStfDAO staffStfDAO = staffStfDAOList.get(0);

        CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));

        StringBuilder activityNotes = new StringBuilder();
        activityNotes.append(ccaseActivitiesCmaDAO.getCmaActivityNotes());
        activityNotes.append(ccaseActivitiesCmaDAO.getCmaActivityNotesAddl());
        if(!activityNotes.isEmpty()) {
            activityNotes.append(CollecticaseConstants.BREAK);
        }
        AllowValAlvDAO allowValAlvDAO = allowValAlvRepository.findById(ccaseActivitiesCmaDAO.getCmaActivityTypeCd())
                .orElseThrow(() -> new NotFoundException("Invalid ALV ID:" + ccaseActivitiesCmaDAO.getCmaActivityTypeCd(), ALV_ID_NOT_FOUND));

        activityNotes.append(ACTIVITY_FOLLOWUP_NOTES1);
        activityNotes.append(allowValAlvDAO.getAlvShortDecTxt());
        activityNotes.append(ACTIVITY_FOLLOWUP_NOTES2);
        activityNotes.append(DateUtil.dateToString.apply(ccaseActivitiesCmaDAO.getCmaActivityDt()));
        activityNotes.append(ACTIVITY_FOLLOWUP_NOTES3);
        activityNotes.append(staffStfDAO.getStfFirstName());
        activityNotes.append(CollecticaseConstants.SPACE);
        activityNotes.append(staffStfDAO.getStfLastName());
        activityNotes.append(ACTIVITY_FOLLOWUP_NOTES4);
        activityNotes.append(completeFollowupActivityDTO.getActivityCompletedOn());

        if(StringUtils.isNotBlank(completeFollowupActivityDTO.getActivityCompletedNote())){
            activityNotes.append(CollecticaseConstants.BREAK);
            activityNotes.append(CollecticaseConstants.ACTIVITY_SHORT_NOTE);
            activityNotes.append(completeFollowupActivityDTO.getActivityCompletedNote());
            activityNotes.append(ACTIVITY_FOLLOWUP_NOTES3);
            activityNotes.append(staffStfDAO.getStfFirstName());
            activityNotes.append(CollecticaseConstants.SPACE);
            activityNotes.append(staffStfDAO.getStfLastName());
            activityNotes.append(CollecticaseConstants.ACTIVITY_FOLLOWUP_NOTES4);
            activityNotes.append(completeFollowupActivityDTO.getActivityCompletedOn());
        }

        activityNotes.append(fornattedActivityNotes(activityNotes.toString(),staffStfDAO.getStfFirstName(),
                staffStfDAO.getStfLastName()));

        splitNotesAndAddlNotes(activityNotes.toString(), ccaseActivitiesCmaDAO);
        ccaseActivitiesCmaDAO.setCmaLastUpdBy(completeFollowupActivityDTO.getUserId());
        ccaseActivitiesCmaDAO.setCmaLastUpdUsing("CASE_SUMMARY");

        ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDAO);

        if(List.of(ACTIVITY_TYPE_USER_ALERT_DISCHARGE_LIEN, ACTIVITY_TYPE_COMPLETE_DOCKET_MARKING)
                .contains(ccaseActivitiesCmaDAO.getCmaActivityTypeCd()))
        {
            List<VwCcaseCollectibleDebtsDAO> vwCcaseCollectibleDebtsList = null;
            CcaseCasesCmcDAO ccaseCasesCmcDAO = ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO();
            vwCcaseCollectibleDebtsList = vwCcaseCollectibleDebtsRepository.getCollectionDebts(ccaseCasesCmcDAO.getClaimantCmtDAO().getSsn());
            if(CollectionUtils.isNotEmpty(vwCcaseCollectibleDebtsList))
            {
                ccaseCasesCmcDAO.setCmcCaseStatus(
                        CollecticaseConstants.CASE_STATUS_CLOSED);
                ccaseCasesCmcDAO.setCmcLastUpdBy(
                        ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCasesCmcDAO.setCmcLastUpdUsing(
                        ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);

            }
            else {
                CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
                ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(
                        UtilFunction.stringToLong.apply(completeFollowupActivityDTO.getCaseId()),
                        List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));


                ccaseCaseRemedyCmrDAO
                        .setCmrStageCd(CollecticaseConstants.CMR_STAGE_INELIGIBLE);
                ccaseCaseRemedyCmrDAO.setCmrLastUpdBy(ccaseActivitiesCmaDAO
                        .getCmaLastUpdBy());
                ccaseCaseRemedyCmrDAO.setCmrLastUpdUsing(ccaseActivitiesCmaDAO
                        .getCmaLastUpdUsing());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            }
        }
    }

    public void appendNotes(AppendNotesDTO appendNotesDTO)
    {
        List<StaffStfDAO> staffStfDAOList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong.apply(appendNotesDTO.getUserId()));
        StaffStfDAO staffStfDAO = staffStfDAOList.get(0);
        if(StringUtils.isNotBlank(appendNotesDTO.getActivityId()))
        {
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(UtilFunction.stringToLong.apply(appendNotesDTO.getActivityId()))
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + appendNotesDTO.getActivityId(), ACTIVITY_ID_NOT_FOUND));

            StringBuilder notes = new StringBuilder();
            notes.append(ccaseActivitiesCmaDAO.getCmaActivityNotes());
            notes.append(ccaseActivitiesCmaDAO.getCmaActivityNotesAddl());
            if (StringUtils.isNotBlank(notes.toString())) {
                notes.append(CollecticaseConstants.BREAK);
            }
            notes.append(fornattedActivityNotes(appendNotesDTO.getActivityNotes(), staffStfDAO.getStfFirstName(), staffStfDAO.getStfLastName()));
            splitNotesAndAddlNotes(notes.toString(),
                    ccaseActivitiesCmaDAO);
            ccaseActivitiesCmaDAO.setCmaLastUpdBy(appendNotesDTO.getUserId());
            ccaseActivitiesCmaDAO.setCmaLastUpdUsing("CASE_NOTES");
            ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDAO);
        }
    }

    public void createGeneralActivity(GeneralActivityDTO generalActivityDTO) {
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
                    createCollecticaseActivity.get(POUT_SUCCESS) != null &&
                    UtilFunction.compareLongObject.test((Long) createCollecticaseActivity
                            .get(POUT_SUCCESS), 1L)) {
                activityId = (Long) createCollecticaseActivity.get(POUT_CMA_ID);
            } else {
                activityId = null;
            }
            if (activityId != null) {
                currentDate = commonRepository.getCurrentDate();
                currentTimeStamp = commonRepository.getCurrentTimestamp();
                ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                        .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
                activityCreated = true;

                ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository.getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO
                        .getCcaseCasesCmcDAO().getCmcId(), List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));
                if (!(UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_GENERAL)
                        || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_BANKRUPTCY))) {
                    if (!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO
                            .getCcaseRemedyActivityCraDAO().getCraTemplatePage())) {
                        ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(generalActivityDTO.getPropertyLien() != null ?
                                generalActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                    } else {
                        ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(generalActivityDTO.getPropertyLien());
                    }
                }

                if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                        ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
                    if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityRemedyTypeCd(),
                            REMEDY_SECOND_DEMAND_LETTER)) {
                        ccaseActivitiesCmaDAO.setCmaRemedyStageCd(CMR_STAGE_INPROCESS);
                        ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CMR_STATUS_NO_COUNTY);
                        ccaseActivitiesCmaDAO
                                .setCmaRemedyNextStepCd(CMR_NEXT_STEP_SECOND_DEMAND_LETTER);
                    }
                }
                if (!UtilFunction.compareLongObject.test(generalActivityDTO.getPropertyLien(),
                        COUNTY_NONE)) {
                    if (ccaseCaseRemedyCmrDAO.getCmrStatusCd().compareTo(CMR_STATUS_UNKNOWN) == 0
                            || ccaseCaseRemedyCmrDAO.getCmrStatusCd()
                            .compareTo(CMR_STATUS_USER_ALERT_LIEN) == 0
                            || ccaseCaseRemedyCmrDAO.getCmrStatusCd()
                            .compareTo(CMR_STATUS_NO_COUNTY) == 0) {
                        ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CMR_STATUS_COUNTY_SELECTED);
                    }
                }
                //splitEntityValueIdType(ccaseActivitiesCmaDAO, updateContactActivityDTO.getEntityContactId());
                List<VwCcaseEntityDAO> vwCcaseEntityDAOList =
                        vwCcaseEntityRepository.getCaseEntityInfo(UtilFunction.stringToLong
                                        .apply(generalActivityDTO.getActivityEntityContact()),
                                generalActivityDTO.getCaseId(),
                                INDICATOR.Y.name());
                VwCcaseEntityDAO vwCcaseEntityDAO = null;
                if (CollectionUtils.isNotEmpty(vwCcaseEntityDAOList)) {
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
                processCorrespondence(sendNoticeList, resendNoticeList, manualNoticeList,
                        ccaseActivitiesCmaDAO, null);
                processResearchNHProperty(ccaseActivitiesCmaDAO);
                processResearchIB(ccaseActivitiesCmaDAO);
                processReassignCaseToSelf(ccaseActivitiesCmaDAO);
                processAutoCompleteAct(ccaseActivitiesCmaDAO);
            }

        }
    }

    public void processAutoCompleteAct(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO = null;
        CcaseActivitiesCmaDAO activitiesCmaDAO = null;
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaDAOList = null;
        ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository
                .getCaseRemedyActivityInfo(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                        ccaseActivitiesCmaDAO.getCmaRemedyType());
        if (ccaseRemedyActivityCraDAO.getCraAutoComplete() != null) {
            CcaseRemedyActivityCraDAO finalCcaseRemedyActivityCraDAO = ccaseRemedyActivityCraDAO;
            ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository
                    .findById(ccaseRemedyActivityCraDAO.getCraAutoComplete()).orElseThrow(() ->
                            new NotFoundException("Invalid Activity ID:" +
                                    finalCcaseRemedyActivityCraDAO.getCraAutoComplete(), ACTIVITY_ID_NOT_FOUND));
            ccaseActivitiesCmaDAOList = ccaseActivitiesCmaRepository
                    .getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            ccaseRemedyActivityCraDAO.getCraActivityCd(), ccaseRemedyActivityCraDAO.getCraRemedyCd());
            for (CcaseActivitiesCmaDAO autoActivity : ccaseActivitiesCmaDAOList) {
                if (autoActivity.getFkEmpIdWg() == null || (UtilFunction.compareLongObject
                        .test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), autoActivity.getFkEmpIdWg()))) {
                    activitiesCmaDAO = ccaseActivitiesCmaRepository.findById(autoActivity.getCmaId()).orElseThrow(() ->
                            new NotFoundException("Invalid Activity ID:" + autoActivity.getCmaId(),
                                    CASE_REMEDY_ACTIVITY_ID_NOT_FOUND));
                    activitiesCmaDAO.setCmaFollowupComplBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
                    activitiesCmaDAO.setCmaFollowupComplete(INDICATOR.Y.name());
                    activitiesCmaDAO.setCmaFollowCompShNote(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO()
                            .getCraAutoCompleteShNote());
                    activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
                    break;
                }
            }
        }
    }

    private void processReassignCaseToSelf(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<StaffStfDAO> staffList = null;
        StaffStfDAO staffDAO = null;
        CcaseCasesCmcDAO ccaseCasesCmcDAO = null;
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_ASSIGN_TO_SELF,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            staffList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong
                    .apply(ccaseActivitiesCmaDAO.getCmaCreatedBy()));

            if (CollectionUtils.isNotEmpty(staffList)) {
                staffDAO = staffList.get(0);
            }
            ccaseCasesCmcDAO = ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO();
            if (!UtilFunction.compareLongObject.test(staffDAO.getStfId(), ccaseCasesCmcDAO.getStaffStfDAO()
                    .getStfId())) {
                ccaseCasesCmcDAO.setStaffStfDAO(staffDAO);
                ccaseCasesCmcDAO.setCmcAssignedTs(commonRepository.getCurrentTimestamp());
                ccaseCasesCmcDAO.setCmcCaseNewInd(INDICATOR.Y.name());
                ccaseCasesCmcDAO.setCmcLastUpdBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseCasesCmcDAO.setCmcLastUpdUsing(ACTIVITY_DETAILS_GENERAL);
                ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);
            }
        }
    }

    private void processResearchIB(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<StaffStfDAO> staffStfDAOList = null;
        StaffStfDAO staffStfDAO = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_RESEARCH_IB8606)
                && UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcCaseStatus(),
                CASE_STATUS_CLOSED)) {
            staffStfDAOList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong
                    .apply(ccaseActivitiesCmaDAO.getCmaLastUpdBy()));
            if (CollectionUtils.isNotEmpty(staffStfDAOList)) {
                staffStfDAO = staffStfDAOList.get(0);
            }
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseStatus(CASE_STATUS_REOPEN);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCasePriority(CASE_PRIORITY_HI);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcCaseNewInd(INDICATOR.Y.name());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setStaffStfDAO(staffStfDAO);
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcAssignedTs(commonRepository.getCurrentTimestamp());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().setCmcLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
            ccaseCasesCmcRepository.save(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO());

            CreateActivityDTO createActivityDTO = new CreateActivityDTO();
            CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository.
                    getCaseRemedyActivityInfo(ACTIVITY_TYPE_REOPEN_CASE,
                            REMEDY_GENERAL);

            createActivityDTO.setCaseId(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
            if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null) {
                createActivityDTO.setEmployerId(ccaseActivitiesCmaDAO.getFkEmpIdWg());
            }
            createActivityDTO.setRemedyTypeCd(REMEDY_GENERAL);
            createActivityDTO.setActivityTypeCd(ACTIVITY_TYPE_REOPEN_CASE);
            createActivityDTO.setActivityDt(commonRepository.getCurrentDate());
            createActivityDTO.setActivityTime(TIME_FORMAT
                    .format(commonRepository.getCurrentDate()));
            createActivityDTO.setActivitySpecifics(ccaseRemedyActivityCraDAO.getCraActivitySpecifics());

            createActivityDTO.setActivityCasePriority(CASE_PRIORITY_HI);
            createActivityDTO.setCallingUser(ccaseActivitiesCmaDAO.getCmaCreatedBy());
            createActivityDTO.setUsingProgramName(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
            createActivity(createActivityDTO);
        }
    }

    private void processResearchNHProperty(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList = null;
        CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            ccaseActivitiesCmaList = ccaseActivitiesCmaRepository
                    .getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            REMEDY_SECOND_DEMAND_LETTER,
                            ACTIVITY_TYPE_USER_ALERTED_RESEARCH_POT_LIEN);

            if (CollectionUtils.isNotEmpty(ccaseActivitiesCmaList)) {
                CcaseActivitiesCmaDAO activitiesCmaDAO = getCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO,
                        ccaseActivitiesCmaList);
                ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
            }
            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository
                    .getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            List.of(REMEDY_LIEN));
            if (!UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaNHFkCtyCd(),
                    COUNTY_NONE)) {
                ccaseCaseRemedyCmrDAO.setCmrStatusCd(CMR_STATUS_LIEN_FILED);
                ccaseCaseRemedyCmrDAO.setCmrStageCd(CMR_STAGE_INEFFECT);
                ccaseCaseRemedyCmrDAO.setCmrNextStepCd(CMR_NEXT_STEP_NONE);
                ccaseCaseRemedyCmrDAO.setCmrLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCaseRemedyCmrDAO.setCmrLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            } else {
                ccaseCaseRemedyCmrDAO.setCmrStatusCd(CMR_STATUS_UNKNOWN);
                ccaseCaseRemedyCmrDAO.setCmrStageCd(CMR_STAGE_INELIGIBLE);
                ccaseCaseRemedyCmrDAO.setCmrNextStepCd(CMR_NEXT_STEP_NONE);
                ccaseCaseRemedyCmrDAO.setCmrLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseCaseRemedyCmrDAO.setCmrLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCaseRemedyCmrRepository.save(ccaseCaseRemedyCmrDAO);
            }
        }
    }

    @NotNull
    private CcaseActivitiesCmaDAO getCcaseActivitiesCmaDAO(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
                                                           List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList) {
        CcaseActivitiesCmaDAO activitiesCmaDAO = ccaseActivitiesCmaList.get(0);
        activitiesCmaDAO.setCmaFollowupComplBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
        activitiesCmaDAO.setCmaFollowupComplete(INDICATOR.Y.name());
        activitiesCmaDAO.setCmaFollowCompShNote(activitiesCmaDAO
                .getCcaseRemedyActivityCraDAO().getCraAutoCompleteShNote());
        activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        return activitiesCmaDAO;
    }

    public void processCorrespondence(List<Map<String, Object>> sendNoticesList, List<String> resendNoticeList,
                                      List<String> manualNoticeList, CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
                                      Long cwgId) {
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO;
        CorrespondenceCorDAO correspondenceCorDAO;
        Long crcId;
        List<Integer> multipleRecieptList = new ArrayList<Integer>();
        Map<String, Object> createCorrespondence;
        for (Map<String, Object> inputParamMap : sendNoticesList) {
            if (inputParamMap.get(PIN_CRC_ID).equals(NOTICE_OF_CHANGED_WG)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_SUSPENDED_WG)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_GARNISHMENT)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_COURT_ORDERED_WG)) {
                multipleRecieptList.add(COR_RECEIPT_EMPLOYER);
                multipleRecieptList.add(COR_RECEIPT_CLAIMANT);
            } else {
                multipleRecieptList.add(COR_RECEIPT_EMPLOYER);
            }
            for (Integer receiptVal : multipleRecieptList) {
                crcId = (Long) inputParamMap.get(PIN_CRC_ID);
                ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
                ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
                if (Objects.equals(receiptVal, COR_RECEIPT_CLAIMANT)) {
                    inputParamMap.put(PIN_WLP_I720_COR_RECEIP_IFK,
                            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId());
                }
                createCorrespondence = correspondenceCorRepository.createCorrespondence(
                        (Integer) inputParamMap.get(PIN_WLP_I720_RPT_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_CLM_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_EMP_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_CMT_ID),
                        (String) inputParamMap.get(PIN_WLP_I720_COR_COE_IND),
                        (String) inputParamMap.get(PIN_WLP_I720_FORCED_IND),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_STATUS_CD),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_DEC_ID_IFK),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_RECEIP_IFK),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_RECEIP_CD),
                        (Timestamp) inputParamMap.get(PIN_WLP_I720_COR_TS),
                        (String) inputParamMap.get(PIN_WLP_I720_COE_STRING));

                if (createCorrespondence != null
                        && createCorrespondence.get(POUT_WLP_O720_RETURN_CD) != null
                        && NHUIS_RETURN_SUCCESS.equals((Integer) createCorrespondence.get(POUT_WLP_O720_RETURN_CD))) {
                    Long corId = ((Integer) createCorrespondence.get(POUT_WLP_O720_COR_ID)).longValue();
                    correspondenceCorDAO = correspondenceCorRepository.findById(corId)
                            .orElseThrow(() -> new NotFoundException("Invalid COR ID:" + corId, COR_ID_NOT_FOUND));

                    if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(),
                            REMEDY_WAGE_GARNISHMENT)) {
                        if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null &&
                                CollecticaseUtilFunction.greaterThanLongObject
                                        .test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), 0L)) {//SAT25570
                            correspondenceCorDAO.setCorSourceIfk(COR_SOURCE_IFK_CD_FOR_CMC);
                            correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO
                                    .getCcaseCasesCmcDAO().getCmcId());
                            correspondenceCorDAO.setCorReceipIfk(receiptVal.longValue());
                        }
                    } else {
                        correspondenceCorDAO.setCorSourceIfk(COR_SOURCE_IFK_CD_FOR_CMC);
                        correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
                        correspondenceCorDAO.setCorReceipIfk(COR_RECEIPT_CLAIMANT.longValue());
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
            if (UtilFunction.compareLongObject.test(TEMP_REDUCTION_LIEN,
                    UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(),
                        ACTIVITY_TYPE_SENT_TEMP_PP_REDUCTION_LTR,
                        REMEDY_PAYMENT_PLAN, ACTIVITY_SPECIFICS_TEMP_REDUCTION,
                        ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
            if (UtilFunction.compareLongObject.test(TEMP_SUSPENSION_LIEN,
                    UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(),
                        ACTIVITY_TYPE_SENT_TEMP_PP_SUSPENSION_LTR,
                        REMEDY_PAYMENT_PLAN,
                        ACTIVITY_SPECIFICS_TEMP_SUSPENSION,
                        ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
        }
    }

    private void createManualCMN(CcaseActivitiesCmaDAO ccaseActivitiesCma, Long crcId, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCma);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCma.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCma.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCma.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCma.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.N.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(null);
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
        ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    public void createResendCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long cmnId) {
        CcaseCmaNoticesCmnDAO existingCmnDAO = ccaseCmaNoticesCmnRepository.findById(cmnId)
                .orElseThrow(() -> new NotFoundException("Invalid Activity Notices ID:" + cmnId, CMN_ID_NOT_FOUND));
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.Y.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(existingCmnDAO.getCorrespondenceCorDAO());
        ccaseCmaNoticesCmnDAO.setFkCwgId(existingCmnDAO.getFkCwgId());
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(existingCmnDAO.getCcaseCraCorrespondenceCrcDAO());
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }


    private void createCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long corId,
                           CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.N.name());
        CorrespondenceCorDAO correspondenceCorDAO = new CorrespondenceCorDAO();
        correspondenceCorDAO.setCorId(corId);
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(correspondenceCorDAO);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    private void createNHUISNotes(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Timestamp currentTimestamp) {
        if (StringUtils.isNotBlank(ccaseActivitiesCmaDAO.getCmaActivityNotesNhuis())) {
            CmtNotesCnoDAO cmtNotesCnoDAO = new CmtNotesCnoDAO();
            cmtNotesCnoDAO.setClaimantCmtDAO(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO());
            cmtNotesCnoDAO.setCnoEnteredTs(currentTimestamp);
            cmtNotesCnoDAO.setCnoEnteredBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            cmtNotesCnoDAO.setCnoSubjectTxt(NHUIS_NOTES_SUBJECT);
            cmtNotesCnoDAO.setCnoNotesTxt(ccaseActivitiesCmaDAO.getCmaActivityNotesNhuis());
            cmtNotesCnoDAO.setCnoLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
            cmtNotesCnoDAO.setCnoLastUpdTs(currentTimestamp);
            cmtNotesCnoRepository.save(cmtNotesCnoDAO);
        }
    }

    private void processReopenActivity(CcaseCasesCmcDAO ccaseCasesCmcDAO, GeneralActivityDTO generalActivityDTO,
                                       Date currentDate, Timestamp currentTimestamp) {
        StaffStfDAO staffStfDAO = null;
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_REOPEN_CASE)) {
            ccaseCasesCmcDAO.setCmcCasePriority(CASE_PRIORITY_LO);
            ccaseCasesCmcDAO.setCmcCaseStatus(CASE_STATUS_REOPEN);
            ccaseCasesCmcDAO.setCmcCaseOpenDt(currentDate);
            ccaseCasesCmcDAO.setCmcAssignedTs(currentTimestamp);
            List<StaffStfDAO> staffStfDAOList = staffStfRepository.getStaffInfoByUserId(UtilFunction.stringToLong
                    .apply(generalActivityDTO.getCallingUser()));
            ccaseCasesCmcDAO.setStaffStfDAO(staffStfDAOList.get(0));
            ccaseCasesCmcDAO.setCmcCaseNewInd(INDICATOR.Y.name());
            ccaseCasesCmcDAO.setCmcLastUpdBy(generalActivityDTO.getCallingUser());
            ccaseCasesCmcDAO.setCmcLastUpdUsing(generalActivityDTO.getUsingProgramName());
            ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);
        }
    }
    private Map<String, Object> createActivity(GeneralActivityDTO generalActivityDTO) {

        return createCollecticaseActivity(generalActivityDTO.getCaseId(),
                null, generalActivityDTO.getActivityTypeCd(),
                generalActivityDTO.getActivityRemedyTypeCd(), generalActivityDTO.getActivityDate(),
                generalActivityDTO.getActivityTime(), generalActivityDTO.getActivitySpecifics(),
                generalActivityDTO.getActivityNotes(), generalActivityDTO.getActivityAdditionalNotes(),
                generalActivityDTO.getActivityNHUISNotes(), generalActivityDTO.getActivityCommunicationMethod(),
                generalActivityDTO.getActivityCaseCharacteristics(),
                generalActivityDTO.getActivityClaimantRepresentative(),
                generalActivityDTO.getCasePriorityCd(), generalActivityDTO.getActivityFollowupDate(),
                generalActivityDTO.getActivityFollowupShortNote(), null,
                generalActivityDTO.getCallingUser(), generalActivityDTO.getUsingProgramName());
    }

    public String createPaymentPlanActivity(PaymentPlanActivityDTO paymentPlanActivityDTO) {
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
                createCollecticaseActivity.get(POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long) createCollecticaseActivity
                        .get(POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if (activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId,
                            ACTIVITY_ID_NOT_FOUND));
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

            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository
                    .getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            List.of(ccaseActivitiesCmaDAO.getCmaRemedyType()));

            if (!(UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_GENERAL)
                    || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_BANKRUPTCY))) {
                if (!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO
                        .getCcaseRemedyActivityCraDAO().getCraTemplatePage())) {
                    ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(paymentPlanActivityDTO.getPropertyLien() != null ?
                            paymentPlanActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                } else {
                    ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(paymentPlanActivityDTO.getPropertyLien());
                }
            }
            updatePPRemedy(ccaseActivitiesCmaDAO);
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            processCorrespondence(sendNoticeList, resendNoticeList, manualNoticeList,
                    ccaseActivitiesCmaDAO, null);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
            processClosedCasePPActivity(ccaseActivitiesCmaDAO);
        }
        return activityCreated ? CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() :
                CREATE_ACTIVITY_SUCCESSFUL;
    }

    private void updatePPRemedy(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<RepaymentRpmDAO> repaymentRpmList = null;
        List<OpmPayPlanOppDAO> opmPayPlanOppList = null;
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY)) {
            repaymentRpmList = repaymentRpmRepository.checkPaymentSincePPLetter(ccaseActivitiesCmaDAO
                            .getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId(),
                    List.of(ACTIVITY_TYPE_PP_FIXED, ACTIVITY_TYPE_PP_VARIABLE, ACTIVITY_TYPE_PP_OFFSET));
            if (CollectionUtils.isEmpty(repaymentRpmList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStageCd(CMR_STAGE_INPROCESS);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_NO_PMT);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_SECOND_PP_LTR);
            }
        }
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)) {
            opmPayPlanOppList = opmPayPlanOppRepository.getOverpaymentPlanInfo(
                    ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId());
            if (CollectionUtils.isEmpty(opmPayPlanOppList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStageCd(CMR_STAGE_INPROCESS);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_FA_RECIEVED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_REVIEW_FA);
            } else {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_FA_RECIEVED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_REVIEW_FA);
            }
        }
    }


    private void processClosedCasePPActivity(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO =
                ccaseRemedyActivityCraRepository.getCaseRemedyActivityInfo(REMEDY_GENERAL, ACTIVITY_TYPE_REOPEN_CASE);

        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP)
                || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT)) {
            if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcCaseStatus(),
                    CASE_STATUS_CLOSED)) {
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
                        INDICATOR.Y.name());
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

    private void createActivity(CreateActivityDTO createActivityDTO) {

        createCollecticaseActivity(createActivityDTO.getCaseId(),
                null, createActivityDTO.getActivityTypeCd(),
                createActivityDTO.getRemedyTypeCd(), createActivityDTO.getActivityDt(),
                createActivityDTO.getActivityTime(), createActivityDTO.getActivitySpecifics(),
                createActivityDTO.getActivityNotes(), createActivityDTO.getActivityNotesAdditional(),
                createActivityDTO.getActivityNotesNHUIS(), createActivityDTO.getCommunicationMethod(),
                createActivityDTO.getCaseCharacteristics(), createActivityDTO.getActivityCmtRepCd(),
                createActivityDTO.getActivityCasePriority(), createActivityDTO.getFollowupDt(),
                createActivityDTO.getFollowupShortNote(), null,
                createActivityDTO.getCallingUser(), createActivityDTO.getUsingProgramName());
    }

    public Map<String, Object> createCollecticaseActivity(Long caseId, Long employerId, Long activityTypeCd,
                                                          Long remedyTypeCd, Date activityDt, String activityTime, String activitySpecifics,
                                                          String activityNotes, String activityNotesAdditional, String activityNotesNHUIS,
                                                          Long communicationMethod, String caseCharacteristics, Long activityCmtRepCd,
                                                          Long activityCasePriority, Date followupDt, String followupShortNote,
                                                          String followupCompleteShortNote, String callingUser, String usingProgramName) {
        Map<String, Object> createCollecticaseActivity;

        createCollecticaseActivity = ccaseActivitiesCmaRepository.createCollecticaseActivity(caseId, employerId,
                activityTypeCd, remedyTypeCd, activityDt, activityTime, activitySpecifics, activityNotes,
                activityNotesAdditional, activityNotesNHUIS, communicationMethod, caseCharacteristics, activityCmtRepCd,
                activityCasePriority, followupDt, followupShortNote, followupCompleteShortNote, callingUser,
                usingProgramName);

        return createCollecticaseActivity;
    }

    public void addSystemActivity(CcaseCasesCmcDAO ccaseCasesCmcDAO,
                                  Long activityTypeCd, Long remedyCd,
                                  String activitySpecifics, String activityNotes, Long casePriority) {
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        createActivityDTO.setCaseId(ccaseCasesCmcDAO.getCmcId());
        createActivityDTO.setActivityDt(commonRepository.getCurrentDate());
        createActivityDTO.setActivityTime(TIME_FORMAT_INPUT
                .format(commonRepository.getCurrentDate()));
        createActivityDTO.setActivityTypeCd(activityTypeCd);
        createActivityDTO.setRemedyTypeCd(remedyCd);
        createActivityDTO.setCaseCharacteristics(ccaseCasesCmcDAO.getCmcCaseCharacteristics());
        createActivityDTO.setActivitySpecifics(activitySpecifics);
        createActivityDTO.setActivityNotes(activityNotes);
        createActivityDTO.setCommunicationMethod(COMM_METHOD_NOT_APPLICABLE);
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

    public String createWageGarnishmentActivity(WageGarnishmentActivityDTO wageGarnishmentActivityDTO) {
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
                createCollecticaseActivity.get(POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long) createCollecticaseActivity
                        .get(POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if (activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId,
                            ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;

            if (!(UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(), REMEDY_GENERAL)
                    || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO
                    .getCmaRemedyType(), REMEDY_BANKRUPTCY))) {
                if (!GENERAL_ACTIVITY_TEMPLATE.equals(ccaseActivitiesCmaDAO
                        .getCcaseRemedyActivityCraDAO().getCraTemplatePage())) {
                    ccaseActivitiesCmaDAO.setCmaNHFkCtyCd(wageGarnishmentActivityDTO.getPropertyLien() != null ?
                            wageGarnishmentActivityDTO.getPropertyLien() : ccaseCaseRemedyCmrDAO.getCmrGnFkCtyCd());
                } else {
                    ccaseCaseRemedyCmrDAO.setCmrGnFkCtyCd(wageGarnishmentActivityDTO.getPropertyLien());
                }
            }

            if (CollecticaseUtilFunction.lesserThanLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), 0L)) {
                ccaseActivitiesCmaDAO.setFkEmpIdWg(wageGarnishmentActivityDTO.getEmployerId());
            } else if (CollecticaseUtilFunction.lesserThanLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), 0L)) {
                ccaseActivitiesCmaDAO.setCmaEntityConttypeIfk(wageGarnishmentActivityDTO
                        .getEmployerId());
                if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), -1L)) {
                    ccaseActivitiesCmaDAO
                            .setCmaEntityContact(NO_KNOWN_NH_EMPLOYER);
                } else if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(), -2L)) {
                    ccaseActivitiesCmaDAO
                            .setCmaEntityContact(OUT_OF_STATE_EMPLOYER);
                }
                ccaseActivitiesCmaDAO.setFkEmpIdWg(null);
            }
            updateWGRemedy(ccaseActivitiesCmaDAO, wageGarnishmentActivityDTO.getEmployerId());
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            ccaseWageGarnishmentCwgDAO = processWageGarnish(ccaseActivitiesCmaDAO);
            if (ccaseWageGarnishmentCwgDAO != null) {
                cwgId = ccaseWageGarnishmentCwgDAO.getCwgId();
            }
            processCorrespondence(sendNoticeList, resendNoticeList, manualNoticeList, ccaseActivitiesCmaDAO, cwgId);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
        }
        return activityCreated ? CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
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

    private void processSuspendWage(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        List<CcaseWageGarnishmentCwgDAO> ccaseWageGarnishmentCwgDAOList = ccaseWageGarnishmentCwgRepository.
                getWageInfoForOtherEmpWithStage(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                        ccaseActivitiesCmaDAO.getFkEmpIdWg(), List.of(CMR_STAGE_INPROCESS, CMR_STAGE_INEFFECT));
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            if (INDICATOR.Y.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())
                    && CollectionUtils.isEmpty(ccaseWageGarnishmentCwgDAOList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_COURT_SUSPENDED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_SUSPENSION_LTR);
            } else if (INDICATOR.N.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())
                    && CollectionUtils.isEmpty(ccaseWageGarnishmentCwgDAOList)) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_ADMIN_SUSPENDED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_SUSPENSION_LTR);
            }
        }
    }


    private void processChngWgAmt(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            if (INDICATOR.Y.name().equals(ccaseActivitiesCmaDAO
                    .getCmaWgCourtOrdered())) {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_COURT_ORDERED_WG);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_REV_WG_NOTICE);

            } else {
                ccaseActivitiesCmaDAO
                        .setCmaRemedyStatusCd(CMR_STATUS_WG_CHANGED);
                ccaseActivitiesCmaDAO
                        .setCmaRemedyNextStepCd(CMR_NEXT_STEP_REV_WG_NOTICE);
            }
        }
    }

    private void processResearchForEmp(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
                                       Long employerValue) {
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT)) {
            if (CollecticaseUtilFunction.greaterThanLongObject.test(employerValue, 0L)) {
                if (INDICATOR.N.name().equals(ccaseActivitiesCmaDAO
                        .getCmaWgDoNotGarnish())) {
                    ccaseActivitiesCmaDAO.setCmaRemedyStageCd(CMR_STAGE_INPROCESS);
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CMR_STATUS_EMP_FOUND);
                    ccaseActivitiesCmaDAO.setCmaRemedyNextStepCd(CMR_NEXT_STEP_WG_NOTICE);
                } else {
                    ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CMR_STATUS_DO_NOT_GARNISH);
                }
            } else if (employerValue != null && CollecticaseUtilFunction
                    .lesserThanLongObject.test(employerValue, 0L)) {//SAT25570
                ccaseActivitiesCmaDAO.setCmaRemedyStageCd(CMR_STAGE_SUSPENDED);
                ccaseActivitiesCmaDAO.setCmaRemedyStatusCd(CMR_STATUS_NO_EMP);
                ccaseActivitiesCmaDAO.setCmaRemedyNextStepCd(CMR_NEXT_STEP_RESEARCH_EMP);
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
            ccaseCaseRemedyCmrDAO = ccaseCaseRemedyCmrRepository
                    .getCaseRemedyByCaseRemedy(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
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

    private CcaseWageGarnishmentCwgDAO updateWageGarnish(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO,
            CcaseCaseRemedyCmrDAO ccaseCaseRemedyCmrDAO) {
        populateWageData(ccaseWageGarnishmentCwgDAO, ccaseCaseRemedyCmrDAO, ccaseActivitiesCmaDAO);
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseWageGarnishmentCwgDAO.setCwgLastUpdUsing(ccaseActivitiesCmaDAO
                .getCmaLastUpdUsing());
        ccaseWageGarnishmentCwgRepository.save(ccaseWageGarnishmentCwgDAO);
        return ccaseWageGarnishmentCwgDAO;
    }


    private CcaseWageGarnishmentCwgDAO createWageGarnish(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseCaseRemedyCmrDAO caseCaseRemedyCmrDAO) {
        CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO = new CcaseWageGarnishmentCwgDAO();
        populateWageData(ccaseWageGarnishmentCwgDAO, caseCaseRemedyCmrDAO, ccaseActivitiesCmaDAO);
        ccaseWageGarnishmentCwgDAO.setCwgCreatedSource(WAGE_GARNISH_SOURCE);
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
            ccaseWageGarnishmentCwgDAO.setCwgDoNotGarnish(INDICATOR.N.name());
        } else {
            ccaseWageGarnishmentCwgDAO.setCwgDoNotGarnish(ccaseActivitiesCmaDAO
                    .getCmaWgDoNotGarnish());
        }
        ccaseWageGarnishmentCwgDAO.setCwgFreqCd(ccaseActivitiesCmaDAO.getCmaWgFreqCd());
        ccaseWageGarnishmentCwgDAO.setCwgNonComplCd(ccaseActivitiesCmaDAO
                .getCmaWgEmpNonCompCd());
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            ccaseWageGarnishmentCwgDAO.setCwgSuspended(INDICATOR.Y.name());
        } else {
            ccaseWageGarnishmentCwgDAO.setCwgSuspended(INDICATOR.N.name());
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
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(),
                CASE_ENTITY_CONTACT_TYPE_ATTY)
                || UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(),
                CASE_ENTITY_CONTACT_TYPE_REP_CMO)) {
            ccaseWageGarnishmentCwgDAO.setFkCmoIdRep(ccaseActivitiesCmaDAO.getCmaEmpRepTypeIfk());
        }
        if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaEmpRepTypeCd(),
                CASE_ENTITY_CONTACT_TYPE_REP_CMI)) {
            ccaseWageGarnishmentCwgDAO.setFkCmiIdRep(ccaseActivitiesCmaDAO
                    .getCmaEmpRepTypeIfk());
        }
        if (!UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getFkCmiIdWgEmp(), -1L)) {
            ccaseWageGarnishmentCwgDAO.setFkCmiIdEmpCont(ccaseActivitiesCmaDAO
                    .getFkCmiIdWgEmp());
        }
    }

    public String createUpdateContactActivity(UpdateContactActivityDTO updateContactActivityDTO) {
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
                createCollecticaseActivity.get(POUT_SUCCESS) != null &&
                UtilFunction.compareLongObject.test((Long) createCollecticaseActivity.get(POUT_SUCCESS), 1L)) {
            activityId = (Long) createCollecticaseActivity.get(POUT_CMA_ID);
        } else {
            activityId = null;
        }
        if (activityId != null) {
            currentDate = commonRepository.getCurrentDate();
            currentTimeStamp = commonRepository.getCurrentTimestamp();
            ccaseActivitiesCmaDAO = ccaseActivitiesCmaRepository.findById(activityId)
                    .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, ACTIVITY_ID_NOT_FOUND));
            activityCreated = true;
            ccaseActivitiesCmaDAO.setCmaUpdContRepFor(updateContactActivityDTO.getEntityRepresentedFor());
            if (!CLAIMANT_REPRESENTS_FOR.equals(updateContactActivityDTO.getEntityRepresentedFor())) {
                ccaseActivitiesCmaDAO.setCmaUpdContRepFor(EMPLOYER_REPRESENTS_FOR);
                ccaseActivitiesCmaDAO.setFkEmpIdRepUc(UtilFunction.stringToLong
                        .apply(updateContactActivityDTO.getEntityRepresentedFor()));
            }
            ccaseOrganizationCmoDAO = new CcaseOrganizationCmoDAO();
            if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT,
                    ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
                CcaseActivitiesCmaDAO finalCcaseActivitiesCmaDAO = ccaseActivitiesCmaDAO;
                individualCmi = ccaseCmeIndividualCmiRepository.findById(ccaseActivitiesCmaDAO.getFkCmiIdUc())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" +
                                finalCcaseActivitiesCmaDAO.getFkCmiIdUc(), CMI_ID_NOT_FOUND));
                individualCmi.setCmiActiveInd(INDICATOR.N.name());
                individualCmi.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                individualCmi.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                individualCmi.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                individualCmi.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseCmeIndividualCmiRepository.save(individualCmi);

            } else if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE,
                    ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
                CcaseActivitiesCmaDAO finalCcaseActivitiesCmaDAO1 = ccaseActivitiesCmaDAO;
                ccaseEntityCme = ccaseEntityCmeRepository.findById(ccaseActivitiesCmaDAO.getCmaEntityConttypeIfk())
                        .orElseThrow(() -> new NotFoundException("Invalid CME ID:" +
                                finalCcaseActivitiesCmaDAO1.getCmaEntityConttypeIfk(), CME_ID_NOT_FOUND));

                ccaseEntityCme.setCmeActiveInd(INDICATOR.N.name());
                ccaseEntityCme.setCmeCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                ccaseEntityCme.setCmeCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                ccaseEntityCme.setCmeLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                ccaseEntityCme.setCmeLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                ccaseEntityCmeRepository.save(ccaseEntityCme);

                ccaseCmeIndividualCmiDAOList = ccaseCmeIndividualCmiRepository
                        .getCaseEntityIndividualByCaseEntityId(ccaseEntityCme.getCmeId(), INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO ccaseCmeIndividualCmiDAO : ccaseCmeIndividualCmiDAOList) {
                    individualCmi = ccaseCmeIndividualCmiRepository.findById(ccaseCmeIndividualCmiDAO.getCmiId())
                            .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" +
                                    ccaseCmeIndividualCmiDAO.getCmiId(), CMI_ID_NOT_FOUND));
                    individualCmi.setCmiActiveInd(INDICATOR.N.name());
                    individualCmi.setCmiCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                    individualCmi.setCmiCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
                    individualCmi.setCmiLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    individualCmi.setCmiLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseCmeIndividualCmiRepository.save(individualCmi);
                }

            } else if (CollecticaseUtilFunction.greaterThanLongObject.test(
                    UtilFunction.stringToLong.apply(updateContactActivityDTO.getActivityEntityContact()), 0L)) {
                vwCcaseEntityDAOList = vwCcaseEntityRepository.getCaseEntityInfo(UtilFunction.stringToLong
                                .apply(updateContactActivityDTO.getActivityEntityContact()),
                        updateContactActivityDTO.getCaseId(), INDICATOR.Y.name());
                if (CollectionUtils.isNotEmpty(vwCcaseEntityDAOList)) {
                    vwCcaseEntityDAO = vwCcaseEntityDAOList.get(0);
                    VwCcaseEntityDAO finalVwCcaseEntityDAO = vwCcaseEntityDAO;
                    ccaseEntityCme = ccaseEntityCmeRepository.findById(vwCcaseEntityDAO.getCmeId())
                            .orElseThrow(() -> new NotFoundException("Invalid CME ID:" +
                                    finalVwCcaseEntityDAO.getCmeId(), CME_ID_NOT_FOUND));
                    if (UtilFunction.compareLongObject.test(vwCcaseEntityDAO.getEntityId(),
                            CASE_ENTITY_CONTACT_TYPE_REP_CMO) || UtilFunction.compareLongObject
                            .test(vwCcaseEntityDAO.getEntityId(), CASE_ENTITY_CONTACT_TYPE_ATTY)) {
                        ccaseOrganizationCmoDAO = ccaseOrganizationCmoRepository.findById(vwCcaseEntityDAO.getEntityId())
                                .orElseThrow(() -> new NotFoundException("Invalid CMO ID:" +
                                        UtilFunction.stringToLong.apply(updateContactActivityDTO.getActivityEntityContact())
                                        , CMO_ID_NOT_FOUND));
                        populateCaseOrgData(ccaseActivitiesCmaDAO, ccaseOrganizationCmoDAO);
                    }
                    populateCaseEntityData(ccaseActivitiesCmaDAO, ccaseEntityCme, ccaseOrganizationCmoDAO);
                }
            }
            individualCmi = new CcaseCmeIndividualCmiDAO();
            if (CollecticaseUtilFunction.greaterThanLongObject.test(
                    updateContactActivityDTO.getEntityContactId(), 0L)) {
                individualCmi = ccaseCmeIndividualCmiRepository.findById(updateContactActivityDTO.getEntityContactId())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" +
                                updateContactActivityDTO.getEntityContactId(), CMI_ID_NOT_FOUND));
                populateCaseIndData(ccaseActivitiesCmaDAO, ccaseEntityCme, ccaseOrganizationCmoDAO);
            }
            updateCaseActivitiesCma(ccaseActivitiesCmaDAO, ccaseEntityCme, organizationCmo, individualCmi);
            ccaseCaseRemedyCmrRepository.updateCaseRemedy(activityId, null);
            createNHUISNotes(ccaseActivitiesCmaDAO, currentTimeStamp);
            processAutoCompleteAct(ccaseActivitiesCmaDAO);
            processCompleteForWgInitiateEmpNC(ccaseActivitiesCmaDAO);
        }

        return activityCreated ? CommonErrorDetail.CREATE_ACTIVITY_FAILED.getDescription() : CREATE_ACTIVITY_SUCCESSFUL;
    }


    public void processCompleteForWgInitiateEmpNC(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseActivitiesCmaDAO activitiesCmaDAO = null;
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaList = null;
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE,
                ccaseActivitiesCmaDAO.getCmaActivityTypeCd())) {
            ccaseActivitiesCmaList = ccaseActivitiesCmaRepository
                    .getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            REMEDY_WAGE_GARNISHMENT,
                            ACTIVITY_TYPE_USER_ALERT_INITIATE_EMP_NC);
            for (CcaseActivitiesCmaDAO ccaseActivitiesCma : ccaseActivitiesCmaList) {
                if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getFkEmpIdRepUc(),
                        ccaseActivitiesCma.getFkEmpIdWg())) {
                    activitiesCmaDAO = ccaseActivitiesCmaRepository.findById(ccaseActivitiesCma.getCmaId())
                            .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" +
                                    ccaseActivitiesCma.getCmaId(), ACTIVITY_ID_NOT_FOUND));
                    activitiesCmaDAO.setCmaFollowupComplBy(SYSTEM_USER_ID);
                    activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
                    activitiesCmaDAO
                            .setCmaFollowupComplete(INDICATOR.Y.name());
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

    public void updateCaseActivitiesCma(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDB, CcaseEntityCmeDAO ccaseEntityCme,
            CcaseOrganizationCmoDAO organizationCmo, CcaseCmeIndividualCmiDAO individualCmi) {

        if (ccaseEntityCme != null && ccaseEntityCme.getCmeId() != null) {
            ccaseActivitiesCmaDB.setCmaEntityConttypeIfk(ccaseEntityCme.getCmeId());
        }
        if (organizationCmo != null && organizationCmo.getCmoId() != null) {
            ccaseActivitiesCmaDB.setFkCmoIdUc(organizationCmo.getCmoId());
        }
        if (individualCmi != null && individualCmi.getCmiId() != null) {
            ccaseActivitiesCmaDB.setFkCmiIdUc(individualCmi.getCmiId());
        }

        if (CLAIMANT_REPRESENTS_FOR.equals(ccaseActivitiesCmaDB.getCmaUpdContRepFor())) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB.getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(null);
        } else if (StringUtils.isNotBlank(ccaseActivitiesCmaDB.getCmaUpdContRepFor())) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB.getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(ccaseActivitiesCmaDB.getFkEmpIdRepUc());
        }

        // Update CMA Entity Contact when adding New ATTY or New Repo
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDB.getCmaEntityContTypeCd())
                || UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(organizationCmo != null ? organizationCmo.getCmoName() : null);
        }

        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
                ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(individualCmi != null ?
                    (individualCmi.getCmiFirstName() + StringUtils.SPACE + individualCmi.getCmiLastName()) : null);
            // For Activity Rep(I), no possibility from UI (disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            ccaseActivitiesCmaDB.setCmaUpdContPrimary(INDICATOR.Y.name());
        }

        // Clearing the default value for Individual's Country & State when Firstname and lastname is not specified
        if (individualCmi != null && (individualCmi.getCmiFirstName() == null
                && individualCmi.getCmiLastName() == null)) {
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
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + activitiesCma
                                .getFkCmiIdUc(), CMI_ID_NOT_FOUND));
            }
            individualCmi.setCmiIsPrimary(activitiesCma.getCmaUpdContPrimary());

            // For Activity Rep(I), no possibility from UI(disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
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
            individualCmi.setCmiActiveInd(INDICATOR.Y.name());
            activitiesCma.setFkCmiIdUc(individualCmi.getCmiId());
            ccaseCmeIndividualCmiRepository.save(individualCmi);

            // Update Primary Contact for Individuals
            if (INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContPrimary())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository
                        .getCaseEntityIndividualByCaseEntityId
                                (entityCme.getCmeId(), INDICATOR.Y.name());
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
            if (INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContMailingRcpt())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository
                        .getCaseEntityIndividualByCaseEntityId
                                (entityCme.getCmeId(), INDICATOR.Y.name());
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
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_EMP,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_EMP);
            EmployerEmpDAO employer = new EmployerEmpDAO();
            employer.setEmpId(activitiesCma.getFkEmpIdRepUc());
            entityCme.setEmployerEmpDAO(employer);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_ATTY);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_REP_O);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_IND);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_REP_I);
        }
        if (CLAIMANT_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            entityCme.setEmployerEmpDAO(null);
        } else if (EMPLOYER_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            EmployerEmpDAO employer = employerEmpRepository.findById(UtilFunction.stringToLong
                            .apply(activitiesCma.getCmaUpdContRepFor()))
                    .orElseThrow(() -> new NotFoundException("Invalid EMP ID:" + activitiesCma.getCmaUpdContRepFor(),
                            EMP_ID_NOT_FOUND));
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
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())
                || UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())) {
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

                if (ccaseActivitiesCmaDAO.getFkEmpIdRepUc() != null) {
                    EmployerEmpDAO employerEmpDAO = new EmployerEmpDAO();
                    employerEmpDAO.setEmpId(ccaseActivitiesCmaDAO.getFkEmpIdRepUc());
                    caseOrganizationCmoDAO.setEmployerEmpDAO(employerEmpDAO);
                }

                ccaseOrganizationCmoRepository.save(caseOrganizationCmoDAO);
            }
        }
        return caseOrganizationCmoDAO;
    }

    public OrgLookupDTO resetOrgLookup() {
        return OrgLookupDTO.builder().build();
    }

}