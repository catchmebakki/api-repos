package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.repository.CcaseCraCorrespondenceCrcRepository;
import com.ssi.ms.collecticase.database.repository.VwCcaseCaseloadRepository;
import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class GeneralActivityValidator {

    @Autowired
    CcaseCraCorrespondenceCrcRepository ccaseCraCorrespondenceCrcRepository;

    VwCcaseCaseloadRepository vwCcaseCaseloadRepository;

    public Map<String, List<DynamicErrorDTO>> validateGeneralActivity(GeneralActivityDTO generalActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();

        validateFollowupAndNote(generalActivityDTO, errorEnums);
        validateResearchNHProperty(generalActivityDTO, errorEnums);
        validateEnityContact(generalActivityDTO, errorEnums);
        validateNotices(generalActivityDTO, errorEnums, errorParams);
        validateRepenActivity(generalActivityDTO, errorEnums);

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
        return errorMap;
    }

    private static void validateEnityContact(GeneralActivityDTO generalActivityDTO,
                                             List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_OTHER_ENTITY_CONTACT)) {
            if (StringUtils.isBlank(generalActivityDTO.getActivityEntityContact())) {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.ENTITY_CONTACT_REQUIRED);
            }
        }
    }

    private void validateNotices(GeneralActivityDTO generalActivityDTO, List<CollecticaseErrorEnum> errorEnums,
                                 List<String> errorParams) {
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            if (generalActivityDTO.getActivitySendCorrespondence() != null) {
                List<String> activityCorrespondenceList = Arrays
                        .stream(generalActivityDTO.getActivitySendCorrespondence())
                        .map(String::toUpperCase) // Transformation
                        .toList();
                List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList =
                        ccaseCraCorrespondenceCrcRepository.getSendCorrespondenceForActivityRemedy(
                                List.of(CollecticaseConstants.INDICATOR.Y.name()),
                                List.of(CollecticaseConstants.INDICATOR.N.name()),
                                List.of(generalActivityDTO.getActivityTypeCd()),
                                List.of(generalActivityDTO.getActivityRemedyTypeCd()));

                for (CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO : ccaseCraCorrespondenceCrcDAOList) {
                    if (activityCorrespondenceList.contains(String.valueOf(ccaseCraCorrespondenceCrcDAO
                            .getCrcId()))) {
                        if (CollecticaseConstants.INDICATOR.C.name().equals(ccaseCraCorrespondenceCrcDAO
                                .getCrcEnable())) {
                            if (!UtilFunction.compareLongObject.test(ccaseCraCorrespondenceCrcDAO.getCrcCounty(),
                                    generalActivityDTO.getPropertyLien())) {
                                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail
                                        .CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                            }
                        }
                    }
                }
            }
        }
    }

    private void validateRepenActivity(GeneralActivityDTO generalActivityDTO, List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_REOPEN_CASE)) {
            VwCcaseHeaderDAO vwCcaseHeaderDAO = vwCcaseCaseloadRepository
                    .getCaseHeaderInfoByCaseId(generalActivityDTO.getCaseId());

            if (vwCcaseHeaderDAO != null)
                if (!UtilFunction.compareLongObject.test(vwCcaseHeaderDAO.getCaseStatus(),
                        CollecticaseConstants.CASE_STATUS_CLOSED)) {
                    errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.REOPEN_CASE_CLOSED);
                } else if (vwCcaseHeaderDAO.getOpBal() == null ||
                        vwCcaseHeaderDAO.getOpBal().compareTo(BigDecimal.ZERO) == 0) {
                    errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.REOPEN_OPM_BAL_ZERO);
                }
        }
    }

    private static void validateResearchNHProperty(GeneralActivityDTO generalActivityDTO,
                                                   List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_NH_PROPERTY)) {
            if (generalActivityDTO.getPropertyLien() == null) {
                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.PROPERTY_LIEN_REQUIRED);
            }
        }
    }

    private static void validateFollowupAndNote(GeneralActivityDTO generalActivityDTO,
                                                List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(generalActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_RECORD_NEW_FOLLOW_UP)) {
            if (generalActivityDTO.getActivityFollowupDate() == null) {
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
    }

}