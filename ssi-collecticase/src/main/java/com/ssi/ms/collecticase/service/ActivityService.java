package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.repository.VwCcaseCountyCtyRepository;
import com.ssi.ms.collecticase.dto.CcaseCasesCmcDTO;
import com.ssi.ms.collecticase.dto.AlvDescResDTO;
import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import com.ssi.ms.collecticase.factorybean.ResponseFactory;
import com.ssi.ms.collecticase.factorybean.ResponseTypes;
import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import com.ssi.ms.collecticase.util.CollectionUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

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

    public ActivityGeneralPageResponse getGeneralActivityPage(Long caseId, Long activityRemedyCd, Long activityTypeCd) {
        // Initialize
        ActivityGeneralPageResponse activityGeneralPageResponse = getResponse(ResponseTypes.ActivityGeneralPageResponse);
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
        // Setting in Response
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
        ActivityPropertyLienResponse activityPropertyLienResponse = getResponse(ResponseTypes.ActivityPropertyLienResponse);
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

    public ActivitySendReSendResponse getSendReSendActivityPage(Long caseId, Long activityRemedyCd, Long activityTypeCd) {
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
        List<CcaseCraCorrespondenceCrcDTO> resendCorrespondenceForActivityList = ccaseCraCorrespondenceCrcDAOList.stream()
                .map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao)).toList();

        // Setting the Response
        activitySendReSendResponse.setSendNoticesCrcList(sendCorrespondenceForActivityList);
        activitySendReSendResponse.setManualNoticesCrcList(manualCorrespondenceForActivityList);
        activitySendReSendResponse.setResendNoticesCrcList(resendCorrespondenceForActivityList);

        return activitySendReSendResponse;
    }

}