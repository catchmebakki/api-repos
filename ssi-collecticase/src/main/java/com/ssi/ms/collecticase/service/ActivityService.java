package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.repository.VwCcaseCountyCtyRepository;
import com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO;
import com.ssi.ms.collecticase.dto.AlvDescResDTO;
import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.EmployerContactListDTO;
import com.ssi.ms.collecticase.factorybean.ResponseFactory;
import com.ssi.ms.collecticase.factorybean.ResponseTypes;
import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
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

@Slf4j
@Service
public class ActivityService extends CollecticaseBaseService {

    private final Map<ResponseTypes, ResponseFactory<?>> factoryMap = new HashMap<>();

    @Autowired
    VwCcaseCountyCtyRepository vwCcaseCountyCtyRepository;

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
/// //////
        List<Long> signedOffsetPPList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY);

        if (signedOffsetPPList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanSignedDate(caseRemedyByCaseRemedy.getCmrPpPpSignedDt());
        } else {
            activityPaymentPlanPageResponse.setDisablePPSignedDate(true);
        }
/// /////
        activityPaymentPlanPageResponse.setPaymentPlanFinAffidavitSignedDate(caseRemedyByCaseRemedy.getCmrPpFaSignedDt());
        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)) {
            activityPaymentPlanPageResponse.setDisablePPFASignedDate(true);
        }
/// /////
        List<Long> finAffList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT);

        List<Long> guideLineDecFinAffList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP,
                CollecticaseConstants.ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT);

        List<Long> signedOffsetList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PAYMENT_NO_SIGNED_PP,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET);

        if (finAffList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setDisablePPPaymentAmount(true);
            activityPaymentPlanPageResponse.setDisablePPPaymentCategory(true);
            activityPaymentPlanPageResponse.setDisablePPEffUntilDate(true);
        }
        if (guideLineDecFinAffList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanPaymentAmount(BigDecimal.ZERO);
        } else if (signedOffsetList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanPaymentAmount(caseRemedyByCaseRemedy.getCmrPpPaymentAmt());
            activityPaymentPlanPageResponse.setDisablePPPaymentAmount(true);
        }
/// ////
        List<Long> ppSignedOffsetList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PAYMENT_NO_SIGNED_PP,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET);

        if (ppSignedOffsetList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanPaymentCategory(caseRemedyByCaseRemedy.getCmrPpPaymentCatgCd());
            activityPaymentPlanPageResponse.setPaymentPlanEffectiveUntilDate(caseRemedyByCaseRemedy.getCmrPpEffUntilDt());
        }
/// ////

        return activityPaymentPlanPageResponse;
    }

    public ActivityWageGarnishmentPageResponse getWageGarnishmentActivityPage(Long caseId, Long activityRemedyCd,
                                                                              Long activityTypeCd) {

        ActivityGeneralPageResponse activityGeneralPageResponse =
                getGeneralActivityPage(caseId, activityRemedyCd, activityTypeCd);
        ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse =
                getResponse(ResponseTypes.ActivityWageGarnishmentPageResponse);
        activityWageGarnishmentPageResponse.setActivityGeneralPageResponse(activityGeneralPageResponse);

        List<EmployerListDTO> employerListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerListForWageGarnish(caseId, CollecticaseConstants.YES);
        List<EmployerContactListDTO> employerContactListForWageGarnish = ccaseEntityCmeRepository
                .getEmployerContactListForWageGarnish(caseId, 2028137L, CollecticaseConstants.YES);


        CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy = ccaseWageGarnishmentCwgRepository
                .getWageInfoForCaseEmployerRemedy(caseId, 2028137L, activityRemedyCd);

//
        activityWageGarnishmentPageResponse.setWageAmount(wageInfoForCaseEmployerRemedy
                .getCwgWgAmount());
        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT)) {
            activityWageGarnishmentPageResponse.setDisableWageAmount(true);
        }
//
        activityWageGarnishmentPageResponse.setDoNotGarnishInd(wageInfoForCaseEmployerRemedy.getCwgDoNotGarnish());
        activityWageGarnishmentPageResponse.setDisableDoNotGarnishInd(true);

        List<Long> emplmntWGAmtChangeList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE);

        if (emplmntWGAmtChangeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setDisableDoNotGarnishInd(false);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT) &&
                activityWageGarnishmentPageResponse.getDoNotGarnishInd() != null) {
            activityWageGarnishmentPageResponse.setDisableDoNotGarnishInd(false);
        }

        if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT)) {
            activityWageGarnishmentPageResponse.setDisableDoNotGarnishInd(true);
            activityWageGarnishmentPageResponse.setDoNotGarnishInd(CollecticaseConstants.YES);
        }
//
        activityWageGarnishmentPageResponse.setWageFrequency(wageInfoForCaseEmployerRemedy
                .getCwgFreqCd());
//
        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE)) {
            activityWageGarnishmentPageResponse.setDisableWageNonCompliance(true);
        }
//
        List<Long> wageAmtSuspendWageList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT);

        if (wageAmtSuspendWageList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setWageMotionFiledOn(null);
        } else if (UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT)) {
            activityWageGarnishmentPageResponse
                    .setWageMotionFiledOn(wageInfoForCaseEmployerRemedy
                            .getCwgChangeReqDt());
        }

        List<Long> wageAmtChangeSuspendWageList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT);

        if (wageAmtChangeSuspendWageList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setDisableWageMotionFiledOn(false);
        }
//
        List<Long> changeWGSuspendWageList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT);

        if (changeWGSuspendWageList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setWageEffectiveFrom(null);
            activityWageGarnishmentPageResponse.setWageEffectiveUntil(null);

        } else {
            activityWageGarnishmentPageResponse.setDisableWageEffectiveFrom(true);
            activityWageGarnishmentPageResponse.setDisableWageEffectiveUntil(true);
        }
//
        activityWageGarnishmentPageResponse.setCourtId(wageInfoForCaseEmployerRemedy.getFkCmoIdCourt());

        List<Long> motionChangeSendWageNoticeList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SENT_NOTICE_OF_WG);
        if (CollecticaseConstants.YES.equals(wageInfoForCaseEmployerRemedy.getCwgCourtOrdered())
                && motionChangeSendWageNoticeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setCourtId(wageInfoForCaseEmployerRemedy
                    .getFkCmoIdCourt());
        }
        if (activityWageGarnishmentPageResponse.getCourtId() == null) {
            activityWageGarnishmentPageResponse
                    .setCourtId(CollecticaseConstants.MERRIMACK_COUNTY_DISTRICT_COURT);
        }
//
        List<Long> changeSendWageNoticeList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SENT_NOTICE_OF_WG);
        if (!changeSendWageNoticeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setDisableCourtOrderedInd(true);
        }
        if (changeSendWageNoticeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setCourtOrderedInd(null);
        }
//
        activityWageGarnishmentPageResponse.setDisableCourtOrderedDate(true);

        List<Long> sendWageNoticeChangeWGList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_SENT_NOTICE_OF_WG,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT);

        if (activityWageGarnishmentPageResponse.getCourtId() != null
                && CollecticaseConstants.YES.equals(activityWageGarnishmentPageResponse.getCourtOrderedInd())
                && sendWageNoticeChangeWGList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setCourtOrderedDate(null);
            activityWageGarnishmentPageResponse.setDisableCourtOrderedDate(false);
        } else if (sendWageNoticeChangeWGList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setCourtOrderedDate(null);
            activityWageGarnishmentPageResponse.setDisableCourtOrderedDate(false);
        }
//
        return activityWageGarnishmentPageResponse;
    }
}