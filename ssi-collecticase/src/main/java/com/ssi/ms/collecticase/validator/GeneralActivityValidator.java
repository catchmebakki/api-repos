package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseOpmDAO;
import com.ssi.ms.collecticase.database.repository.CcaseCraCorrespondenceCrcRepository;
import com.ssi.ms.collecticase.database.repository.VwCcaseCaseloadRepository;
import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class GeneralActivityValidator {

    @Autowired
    CcaseCraCorrespondenceCrcRepository ccaseCraCorrespondenceCrcRepository;

    VwCcaseCaseloadRepository vwCcaseCaseloadRepository;

    public HashMap<String, List<DynamicErrorDTO>> validateGeneralActivity(GeneralActivityDTO generalActivityDTO) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();

        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RECORD_NEW_FOLLOW_UP)) {
            if(generalActivityDTO.getActivityFollowupDate() == null) {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.FOLLOWUP_DT_REQUIRED);
            }
        }
        if (StringUtils.isNotBlank(generalActivityDTO.getActivityFollowupShortNote())) {
            if (generalActivityDTO.getActivityFollowupDate() == null) {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.FOLLOWUP_SHORT_NOTE_NA);
            }
        }
        if (generalActivityDTO.getActivityFollowupDate() != null) {
            if (StringUtils.isBlank(generalActivityDTO.getActivityFollowupShortNote())) {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.FOLLOWUP_SHORT_NOTE_REQUIRED);
            }
        }

        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            if(generalActivityDTO.getPropertyLien() == null)
            {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.PROPERTY_LIEN_REQUIRED);
            }
        }

        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_OTHER_ENTITY_CONTACT)) {
            if(StringUtils.isBlank(generalActivityDTO.getActivityEntityContact()))
            {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.ENTITY_CONTACT_REQUIRED);
            }
        }

        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            if(generalActivityDTO.getActivitySendCorrespondence() != null)
            {
                List<String> activityCorrespondenceList = Arrays
                        .stream(generalActivityDTO.getActivitySendCorrespondence())
                        .map(String::toUpperCase) // Transformation
                        .toList();
                List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList = ccaseCraCorrespondenceCrcRepository.getSendCorrespondenceForActivityRemedy(
                        List.of(CollecticaseConstants.INDICATOR.Y.name()),
                        List.of(CollecticaseConstants.INDICATOR.N.name()),
                        List.of(generalActivityDTO.getActivityTypeCd()),
                        List.of(generalActivityDTO.getActivityRemedyTypeCd()));

                for(CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO : ccaseCraCorrespondenceCrcDAOList){
                        if(activityCorrespondenceList.contains(String.valueOf(ccaseCraCorrespondenceCrcDAO.getCrcId()))){
                            if (CollecticaseConstants.INDICATOR.C.name().equals(ccaseCraCorrespondenceCrcDAO.getCrcEnable())) {
                                if (!UtilFunction.compareLongObject.test(ccaseCraCorrespondenceCrcDAO.getCrcCounty(),
                                        generalActivityDTO.getPropertyLien())) {
                                    errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN);
                                    errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                                }
                            }
                    }
                }
            }
        }

        if(UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_REOPEN_CASE))
        {
            List<VwCcaseOpmDAO> vwCcaseOpmDAOList = vwCcaseCaseloadRepository.getClaimantOpmInfoByCaseId(generalActivityDTO.getCaseId());
            VwCcaseOpmDAO vwCcaseOpmDAO = null;
            if(CollectionUtils.isNotEmpty(vwCcaseOpmDAOList)) {
                vwCcaseOpmDAO = vwCcaseOpmDAOList.get(0);
            }

            List<VwCcaseHeaderDAO> vwCcaseHeaderDAOList = vwCcaseCaseloadRepository.getCaseHeaderInfoByCaseId(generalActivityDTO.getCaseId());
            VwCcaseHeaderDAO vwCcaseHeaderDAO = null;
            if(CollectionUtils.isNotEmpty(vwCcaseHeaderDAOList)) {
                vwCcaseHeaderDAO = vwCcaseHeaderDAOList.get(0);
            }

            if(vwCcaseHeaderDAO != null && !UtilFunction.compareLongObject.test(vwCcaseHeaderDAO.getCaseStatus(),
                    CollecticaseConstants.CASE_STATUS_CLOSED))
            {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.REOPEN_CASE_CLOSED);
            }

            if(vwCcaseOpmDAO != null &&
                    vwCcaseOpmDAO.getOpmBal() == null ||
                    (vwCcaseOpmDAO.getOpmBal().compareTo(BigDecimal.ZERO) == 0))
            {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.REOPEN_OPM_BAL_ZERO);
            }
        }

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
        return errorMap;
    }
}
