package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.database.dao.RepaymentRpmDAO;
import com.ssi.ms.collecticase.database.repository.CcaseActivitiesCmaRepository;
import com.ssi.ms.collecticase.database.repository.CcaseCraCorrespondenceCrcRepository;
import com.ssi.ms.collecticase.database.repository.CcaseWageGarnishmentCwgRepository;
import com.ssi.ms.collecticase.database.repository.RepaymentRpmRepository;
import com.ssi.ms.collecticase.dto.WageGarnishmentActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_CMT_NO_LONGER_EMPLOYED;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_FIN_AFF_EMPLOYED_CMT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_SENT_NOTICE_OF_WG;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.CMR_STAGE_INEFFECT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.CMR_STAGE_INPROCESS;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.EMP_NON_COMP_FAIL_IMP_GARNISH;

@Component
@AllArgsConstructor
@Slf4j
public class WageGarnishmentValidator {

    @Autowired
    CcaseWageGarnishmentCwgRepository ccaseWageGarnishmentCwgDAORepo;

    @Autowired
    RepaymentRpmRepository repaymentRpmRepo;

    @Autowired
    CcaseActivitiesCmaRepository ccaseActivitiesCmaRepo;

    @Autowired
    private ParameterParRepository commonParRepository;

    private CcaseCraCorrespondenceCrcRepository ccaseCraCorrespondenceCrcRepository;

    public Map<String, List<DynamicErrorDTO>> validateWageGarnishmentActivity(WageGarnishmentActivityDTO
                                                                                      wageGarnishmentActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        Date currentDate = commonParRepository.getCurrentDate();

        validateEmployer(wageGarnishmentActivityDTO, errorEnums);
        validateNoKnownNHEmp(wageGarnishmentActivityDTO, errorEnums);
        validateContactAndRep(wageGarnishmentActivityDTO, errorEnums);
        validateWageAmtAndWageFrq(wageGarnishmentActivityDTO, errorEnums);
        validateDecimalWgAmt(wageGarnishmentActivityDTO, errorEnums);
        validateDonotGarnish(wageGarnishmentActivityDTO, errorEnums);
        validateWgFreq(wageGarnishmentActivityDTO, errorEnums);
        validateEmpNonCompliance(wageGarnishmentActivityDTO, errorEnums);
        validateMotionDate(wageGarnishmentActivityDTO, errorEnums, currentDate);
        validateEffDate(wageGarnishmentActivityDTO, errorEnums);
        validateCourtOrder(wageGarnishmentActivityDTO, errorEnums);
        validateCourtOrdered(wageGarnishmentActivityDTO, errorEnums);
        validateForNotFutureDate(wageGarnishmentActivityDTO, currentDate, errorEnums);
        validateNotices(wageGarnishmentActivityDTO, errorEnums);

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
        return errorMap;
    }

    private static void validateWageAmtAndWageFrq(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                                  List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT)) {
            if (CollecticaseConstants.INDICATOR.Y.name().equals(wageGarnishmentActivityDTO.getCourtOrderedInd())) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_AMOUNT_REQUIRED);
            }
            if (wageGarnishmentActivityDTO.getWageFrequency() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_FREQUENCY_REQUIRED);
            }
        }
        if (wageGarnishmentActivityDTO.getEmployerId() == null && wageGarnishmentActivityDTO.getWageAmount() != null) {
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_AMOUNT_VALID_EMPLOYER_INVALID);
        }
        if (wageGarnishmentActivityDTO.getWageAmount() != null
                && !(wageGarnishmentActivityDTO.getWageAmount().compareTo(BigDecimal.ZERO) > 0)) {
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_AMOUNT_POSITIVE_NUMBER);
        }
    }

    private void validateNotices(WageGarnishmentActivityDTO wageGarnishmentActivityDTO, List<CollecticaseErrorEnum>
            errorEnums) {
        if (wageGarnishmentActivityDTO.getActivitySendCorrespondence() != null) {
            List<String> activityCorrespondenceList = Arrays
                    .stream(wageGarnishmentActivityDTO.getActivitySendCorrespondence())
                    .map(String::toUpperCase) // Transformation
                    .toList();
            List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList = ccaseCraCorrespondenceCrcRepository
                    .getManualCorrespondenceForRemedy(
                            List.of(CollecticaseConstants.INDICATOR.Y.name()),
                            List.of(CollecticaseConstants.INDICATOR.N.name()),
                            List.of(wageGarnishmentActivityDTO.getActivityRemedyTypeCd()));

            for (CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO : ccaseCraCorrespondenceCrcDAOList) {
                if (activityCorrespondenceList.contains(String.valueOf(ccaseCraCorrespondenceCrcDAO.getCrcId()))) {
                    if (CollecticaseConstants.INDICATOR.C.name().equals(ccaseCraCorrespondenceCrcDAO.getCrcEnable())) {
                        if (ccaseCraCorrespondenceCrcDAO.getCrcDoNotGarnish() != null) {
                            if (wageGarnishmentActivityDTO.getDoNotGarnishInd() == null ||
                                    !ccaseCraCorrespondenceCrcDAO.getCrcDoNotGarnish().equals(wageGarnishmentActivityDTO
                                            .getDoNotGarnishInd())) {
                                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                        .WG_CORR_NA_DO_NOT_GARNISH);
                            }
                        }
                        if (ccaseCraCorrespondenceCrcDAO.getCrcCourtOrdered() != null) {
                            if (wageGarnishmentActivityDTO.getCourtOrderedInd() == null ||
                                    !ccaseCraCorrespondenceCrcDAO.getCrcCourtOrdered().equals(wageGarnishmentActivityDTO
                                            .getCourtOrderedInd())) {
                                if (CollecticaseConstants.INDICATOR.Y.name().equals(ccaseCraCorrespondenceCrcDAO
                                        .getCrcCourtOrdered())) {
                                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                            .WG_CORR_NA_COURT_ORDERED_CHECKED);
                                } else {
                                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                            .WG_CORR_NA_COURT_ORDERED_NOT_CHECKED);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    private void validateNoKnownNHEmp(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                      List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(),
                -1L)) {
            if (!UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getActivityTypeCd(),
                    ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_NA_FOR_UNKNOWN);
            }
            List<CcaseWageGarnishmentCwgDAO> ccaseWageGarnishmentCwgDAOList =
                    ccaseWageGarnishmentCwgDAORepo.getWageInfoForCaseStage(wageGarnishmentActivityDTO.getCaseId(),
                            List.of(CMR_STAGE_INPROCESS, CMR_STAGE_INEFFECT));
            if (CollectionUtils.isNotEmpty(ccaseWageGarnishmentCwgDAOList)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_UNKNOWN_NA_FOR_WAGE_EMP);
            }
        }
    }

    private static void validateDecimalWgAmt(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                             List<CollecticaseErrorEnum> errorEnums) {
        if (wageGarnishmentActivityDTO.getWageAmount() != null &&
                !CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FOUR_DIGIT_TWO_DEICMAL_PATTERN,
                        String.valueOf(wageGarnishmentActivityDTO.getWageAmount()))) {
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_AMOUNT_INVALID);
        }
    }

    private static void validateDonotGarnish(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                             List<CollecticaseErrorEnum> errorEnums) {
        if (CollecticaseConstants.INDICATOR.Y.name().equals(wageGarnishmentActivityDTO.getDoNotGarnishInd())) {
            if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                        .WG_EMPLOYER_MANDATORY_DO_NOT_GARNISH);
            }
        }
    }

    private static void validateWgFreq(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                       List<CollecticaseErrorEnum> errorEnums) {
        if (wageGarnishmentActivityDTO.getWageFrequency() != null) {
            if (CollecticaseConstants.INDICATOR.Y.name().equals(wageGarnishmentActivityDTO.getDoNotGarnishInd())) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_FREQUENCY_DO_NOT_GRANISH);
            }
            if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_FREQUENCY_NO_EMPLOYER);
            }
            if (wageGarnishmentActivityDTO.getWageAmount() != null
                    && !(wageGarnishmentActivityDTO.getWageAmount().compareTo(BigDecimal.ZERO) > 0)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                        .WG_FREQUENCY_WG_AMOUNT_POSITIVE_NUMBER);
            }
        }
    }

    private void validateEmpNonCompliance(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                          List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE)) {
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_NON_COMPLIANCE_REQUIRED);
        }
        if (UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getWageNonCompliance(),
                EMP_NON_COMP_FAIL_IMP_GARNISH)) {
            List<RepaymentRpmDAO> repaymentRpmDAOList =
                    repaymentRpmRepo.checkPaymentRecievedInDays(wageGarnishmentActivityDTO.getClaimantId(), 40,
                            CollecticaseConstants.INDICATOR.Y.name());
            if (CollectionUtils.isNotEmpty(repaymentRpmDAOList)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                        .WG_NON_COMPLIANCE_REPAYMENT_EXISTS);
            }
            List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaDAOList = ccaseActivitiesCmaRepo
                    .getActivityByActivityCdAndRemedyCd(
                            wageGarnishmentActivityDTO.getCaseId(), wageGarnishmentActivityDTO.getActivityTypeCd(),
                            wageGarnishmentActivityDTO.getActivityRemedyTypeCd());
            if (CollectionUtils.isNotEmpty(ccaseActivitiesCmaDAOList)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_NON_COMPLIANCE_CMT_NO_LONGER);
            }
        }
    }

    private static void validateContactAndRep(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                              List<CollecticaseErrorEnum> errorEnums) {
        if (wageGarnishmentActivityDTO.getEmployerId() != null &&
                !UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(),
                        0L)) {
            if (List.of(ACTIVITY_TYPE_CMT_NO_LONGER_EMPLOYED, ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                            ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT, ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS,
                            ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE, ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE)
                    .contains(wageGarnishmentActivityDTO.getActivityTypeCd())) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_CONTACT_REQUIRED);
            }
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_REPRESENTATIVE_REQUIRED);
        }
    }

    private static void validateMotionDate(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                           List<CollecticaseErrorEnum> errorEnums, Date currentDate) {
        if (List.of(ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE, ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT)
                .contains(wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_MOTION_FILED_ON_REQUIRED);
            }
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() != null &&
                    wageGarnishmentActivityDTO.getWageMotionFiledOn().after(currentDate)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_MOTION_FILED_ON_DATE_FUTURE);
            }
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() != null) {
                if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                            .WG_MOTION_FILED_ON_EMPLOYER_NONE);
                }
            }
        }

        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT,
                wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_MOTION_FILED_ON_REQUIRED);
            }
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() != null &&
                    wageGarnishmentActivityDTO.getWageMotionFiledOn().after(currentDate)) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_MOTION_FILED_ON_DATE_FUTURE);
            }
            if (wageGarnishmentActivityDTO.getWageMotionFiledOn() != null) {
                if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                            .WG_MOTION_FILED_ON_EMPLOYER_NONE);
                }
            }
        }
    }

    private static void validateEffDate(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                        List<CollecticaseErrorEnum> errorEnums) {
        if (List.of(ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT, ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT)
                .contains(wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getWageEffectiveFrom() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EFFECTIVE_DT_REQUIRED);
            }
        }
        if (wageGarnishmentActivityDTO.getWageEffectiveFrom() != null) {
            if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EFFECTIVE_DT_EMPLOYER_NONE);
            }
        }
        if (wageGarnishmentActivityDTO.getWageEffectiveUntil() != null) {
            if (wageGarnishmentActivityDTO.getWageEffectiveFrom() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EFFECTIVE_UNILT_DT_EFF_DT_NONE);
            }
        }
        if (wageGarnishmentActivityDTO.getWageEffectiveFrom() != null &&
                wageGarnishmentActivityDTO.getWageEffectiveUntil() != null) {
            if (wageGarnishmentActivityDTO.getWageEffectiveFrom().
                    after(wageGarnishmentActivityDTO.getWageEffectiveUntil())) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                        .WG_EFFECTIVE_DT_GREATER_THAN_UNTIL_DT);
            }
        }
    }

    private void validateCourtOrder(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                    List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SENT_NOTICE_OF_WG,
                wageGarnishmentActivityDTO.getActivityTypeCd())) {
            CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAOList =
                    ccaseWageGarnishmentCwgDAORepo.getWageInfoForCaseEmployerRemedy(wageGarnishmentActivityDTO
                                    .getCaseId(),
                            wageGarnishmentActivityDTO.getEmployerId(), wageGarnishmentActivityDTO
                                    .getActivityRemedyTypeCd());
            CcaseWageGarnishmentCwgDAO ccaseWageGarnishmentCwgDAO = null;
            if (ccaseWageGarnishmentCwgDAOList != null) {
                if (CollecticaseConstants.INDICATOR.Y.name().equals(ccaseWageGarnishmentCwgDAO.getCwgSuspended())) {
                    if (CollecticaseConstants.INDICATOR.N.name().equals(wageGarnishmentActivityDTO
                            .getCourtOrderedInd())) {
                        errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                .WG_COURT_ORDERED_NO_WAGE_SUSPENDED);
                    }
                } else {
                    if (wageGarnishmentActivityDTO.getCourtOrderedInd() == null) {
                        errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                .WG_COURT_ORDERED_REQUIRED);
                    }
                    if (CollecticaseConstants.INDICATOR.Y.name().equals(wageGarnishmentActivityDTO
                            .getCourtOrderedInd())) {
                        if (wageGarnishmentActivityDTO.getCourtOrderedDate() == null) {
                            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                                    .WG_COURT_ORDERED_DATE_REQUIRED);
                        }
                    }
                }
            }
        }
    }

    private static void validateForNotFutureDate(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                                 Date currentDate, List<CollecticaseErrorEnum> errorEnums) {
        if (wageGarnishmentActivityDTO.getCourtOrderedDate() != null &&
                wageGarnishmentActivityDTO.getCourtOrderedDate().after(currentDate)) {
            errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_COURT_ORDERED_DATE_FUTURE);
        }
    }

    private static void validateCourtOrdered(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                             List<CollecticaseErrorEnum> errorEnums) {
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getCourtOrderedInd() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_COURT_ORDERED_REQUIRED);
            }
            if (CollecticaseConstants.INDICATOR.Y.name().equals(wageGarnishmentActivityDTO.getCourtOrderedInd())) {
                if (wageGarnishmentActivityDTO.getCourtOrderedDate() == null) {
                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail
                            .WG_COURT_ORDERED_DATE_REQUIRED);
                }
            }
        }
    }

    private void validateEmployer(WageGarnishmentActivityDTO wageGarnishmentActivityDTO,
                                  List<CollecticaseErrorEnum> errorEnums) {
        if (List.of(ACTIVITY_TYPE_SENT_NOTICE_OF_WG, ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS,
                        ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE, ACTIVITY_TYPE_CMT_NO_LONGER_EMPLOYED,
                        ACTIVITY_TYPE_FIN_AFF_EMPLOYED_CMT, ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                        ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE, ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT).
                contains(wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getEmployerId() == null) {
                errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_ID_REQUIRED);
            }
        }
        if (UtilFunction.compareLongObject.test(ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT,
                wageGarnishmentActivityDTO.getActivityTypeCd())) {
            if (wageGarnishmentActivityDTO.getEmployerId() != null &&
                    !UtilFunction.compareLongObject.test(wageGarnishmentActivityDTO.getEmployerId(),
                            0L)) {
                List<CcaseWageGarnishmentCwgDAO> ccaseWageGarnishmentCwgDAOList =
                        ccaseWageGarnishmentCwgDAORepo
                                .getWageInfoForCaseEmployerStage(wageGarnishmentActivityDTO.getCaseId(),
                                        wageGarnishmentActivityDTO.getEmployerId(), List.of(CMR_STAGE_INEFFECT));
                if (CollectionUtils.isEmpty(ccaseWageGarnishmentCwgDAOList)) {
                    errorEnums.add(ErrorMessageConstant.WageGarnishmentActivityDTODetail.WG_EMPLOYER_NA_FOR_SUSPEND);
                }
            }
        }
    }

}
