package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.repository.AllowValAlvRepository;
import com.ssi.ms.collecticase.database.repository.CcaseCraCorrespondenceCrcRepository;
import com.ssi.ms.collecticase.database.repository.OpmPayPlanOppRepository;
import com.ssi.ms.collecticase.database.repository.VwCcaseCaseloadRepository;
import com.ssi.ms.collecticase.dto.PaymentPlanActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentPlanActivityValidator {

    @Autowired
    private ParameterParRepository commonParRepository;

    @Autowired
    private OpmPayPlanOppRepository opmPayPlanOppRepository;

    @Autowired
    private VwCcaseCaseloadRepository vwCcaseCaseloadRepository;

    @Autowired
    private CcaseCraCorrespondenceCrcRepository ccaseCraCorrespondenceCrcRepository;

    @Autowired
    private AllowValAlvRepository allowValAlvRepository;

    public Map<String, List<DynamicErrorDTO>> validatePaymentPlanActivity(PaymentPlanActivityDTO
                                                                                  paymentPlanActivityDTO) {
        final Map<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();

        Date currentDate = commonParRepository.getCurrentDate();
        VwCcaseHeaderDAO vwCcaseHeaderDAO = vwCcaseCaseloadRepository
                .getCaseHeaderInfoByCaseId(paymentPlanActivityDTO.getCaseId());

        String currFiling = vwCcaseHeaderDAO != null ? vwCcaseHeaderDAO.getCurrFiling() : null;

        validateResponseToAndGuideLineAmt(paymentPlanActivityDTO, errorEnums);
        validatePPSignedOn(paymentPlanActivityDTO, errorEnums, currentDate);
        validateFinAffidavit(paymentPlanActivityDTO, errorEnums, currentDate);
        validatePaymentAmount(paymentPlanActivityDTO, errorEnums);
        validatePaymentCategory(paymentPlanActivityDTO, errorEnums);
        validateEffectiveUntil(paymentPlanActivityDTO, errorEnums, currentDate);

        if (vwCcaseHeaderDAO != null) {
            validateActivity(paymentPlanActivityDTO, vwCcaseHeaderDAO, errorEnums, errorParams);
        }
        validateNotices(paymentPlanActivityDTO, errorEnums, currFiling, errorParams);
        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
        return errorMap;
    }

    private void validateNotices(PaymentPlanActivityDTO paymentPlanActivityDTO, List<CollecticaseErrorEnum> errorEnums,
                                 String currFiling, List<String> errorParams) {
        if (paymentPlanActivityDTO.getActivitySendCorrespondence() != null) {
            List<String> activityCorrespondenceList = Arrays
                    .stream(paymentPlanActivityDTO.getActivitySendCorrespondence())
                    .map(String::toUpperCase) // Transformation
                    .toList();
            List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList =
                    ccaseCraCorrespondenceCrcRepository.getManualCorrespondenceForRemedy(
                            List.of(CollecticaseConstants.INDICATOR.Y.name()),
                            List.of(CollecticaseConstants.INDICATOR.N.name()),
                            List.of(paymentPlanActivityDTO.getActivityRemedyTypeCd()));

            for (CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO : ccaseCraCorrespondenceCrcDAOList) {
                if (activityCorrespondenceList.contains(String.valueOf(ccaseCraCorrespondenceCrcDAO.getCrcId()))) {
                    if (CollecticaseConstants.INDICATOR.C.name().equals(ccaseCraCorrespondenceCrcDAO.getCrcEnable())) {
                        if (ccaseCraCorrespondenceCrcDAO.getCrcPmtCategory() != null) {
                            if (paymentPlanActivityDTO.getPaymentPlanPaymentCategory() == null) {
                                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                                        .PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NONE);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                            } else if (!UtilFunction.compareLongObject
                                    .test(ccaseCraCorrespondenceCrcDAO.getCrcPmtCategory(),
                                            paymentPlanActivityDTO.getPaymentPlanPaymentCategory())) {
                                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                                        .PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NA);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                                errorParams.add(allowValAlvRepository.getShortDescByAlv(ccaseCraCorrespondenceCrcDAO
                                        .getCrcPmtCategory()).getDesc());
                            }
                        }
                        if (ccaseCraCorrespondenceCrcDAO.getCrcCurrFiling() != null) {
                            if (!currFiling.equals(ccaseCraCorrespondenceCrcDAO.getCrcCurrFiling())) {
                                if (CollecticaseConstants.INDICATOR.N.name().equals(currFiling)) {
                                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                                            .PAYMENT_PLAN_COR_CURRENT_FILING_NONE);
                                    errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                                } else {
                                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                                            .PAYMENT_PLAN_COR_CURRENT_FILING_NA);
                                    errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                                }
                            }
                        }
                        if (ccaseCraCorrespondenceCrcDAO.getCrcCounty() != null) {
                            if (paymentPlanActivityDTO.getPropertyLien() == null) {
                                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail
                                        .CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                            } else if (!UtilFunction.compareLongObject.test(ccaseCraCorrespondenceCrcDAO.getCrcCounty(),
                                    paymentPlanActivityDTO.getPropertyLien())) {
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

    private void validateActivity(PaymentPlanActivityDTO paymentPlanActivityDTO, VwCcaseHeaderDAO vwCcaseHeaderDAO,
                                  List<CollecticaseErrorEnum> errorEnums, List<String> errorParams) {
        if (CollecticaseConstants.INDICATOR.Y.name().equals(vwCcaseHeaderDAO.getCurrFiling())) {
            if (List.of(ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT, ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP).contains(
                    paymentPlanActivityDTO.getActivityTypeCd())) {
                List<OpmPayPlanOppDAO> opmPayPlanOppList = null;
                opmPayPlanOppList = opmPayPlanOppRepository.getOverpaymentPlanInfo(vwCcaseHeaderDAO.getCmtId());
                OpmPayPlanOppDAO opmPayPlanOppDAO = null;
                if (CollectionUtils.isEmpty(opmPayPlanOppList)) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_NO_ACTIVE_PAYMENT_PLAN);
                } else if (CollectionUtils.isNotEmpty(opmPayPlanOppList)) {
                    opmPayPlanOppDAO = opmPayPlanOppList.get(0);
                    if (!CollecticaseUtilFunction.compareBigDecimalObject
                            .test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                                    opmPayPlanOppDAO.getOppPaymentAmt())) {
                        errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                                .PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_SAME);
                        errorParams.add(String.valueOf(opmPayPlanOppDAO.getOppPaymentAmt()));
                    }
                }
            }
        }
    }

    private static void validateEffectiveUntil(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                               List<CollecticaseErrorEnum> errorEnums, Date currentDate) {
        if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null
                && !UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE)) {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_REQUIRED);
        }

        if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE)) {
            if (paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate() != null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE);
            }
        }

        if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED)) {
            if (paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate() != null) {
                //errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE);
            }
        }

        if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT)) {
            if (CollecticaseUtilFunction.daysBetweenStartAndEndTime().apply(currentDate,
                    paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate()) > 180) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                        .PAYMENT_PLAN_EFF_UNITL_MORE_THAN_6_MONTHS);
            }
            if (!paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate().after(currentDate)) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                        .PAYMENT_PLAN_EFF_UNITL_SHOULD_BE_IN_FUTURE);
            }
        }
    }

    private static void validatePaymentCategory(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                                List<CollecticaseErrorEnum> errorEnums) {
        if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null &&
                !CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                        BigDecimal.ZERO)) {
            if (paymentPlanActivityDTO.getPaymentPlanPaymentCategory() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_CATEGORY_REQUIRED);
            }
        }

        if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() == null
                && paymentPlanActivityDTO.getPaymentPlanPaymentCategory() != null) {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_APPLICABLE);
        }

        if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP)) {
            if (List.of(CollecticaseConstants.PAYMENT_CATEGORY_REDUCED, CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED)
                    .contains(paymentPlanActivityDTO.getPaymentPlanPaymentCategory())) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                        .PAYMENT_PLAN_PAYMENT_CATEGORY_NOT_APPLICABLE);
            }
        }
    }

    private static void validatePaymentAmount(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                              List<CollecticaseErrorEnum> errorEnums) {
        if (List.of(ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP, ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd())) {
            if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_REQUIRED);
            }
            if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null) {
                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FOUR_DIGIT_TWO_DEICMAL_PATTERN,
                        String.valueOf(paymentPlanActivityDTO.getPaymentPlanPaymentAmount()))) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_INVALID);
                }
            }
        }

        if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE)) {
            if (CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount(),
                    paymentPlanActivityDTO.getPaymentPlanPaymentAmount())) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_PAYMENT_AMOUNT_NOT_SAME);
            }
        } else if (Objects.equals(CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED,
                paymentPlanActivityDTO.getPaymentPlanPaymentCategory())) {
            if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null) {
                if (CollecticaseUtilFunction.greaterThanBigDecimalObject.test(
                        paymentPlanActivityDTO.getPaymentPlanPaymentAmount(), BigDecimal.ZERO)) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.
                            PAYMENT_AMOUNT_ZERO_WHEN_PAYMENT_CATEGORY_SUSPENDED);
                }
            }
        } else if (List.of(CollecticaseConstants.PAYMENT_CATEGORY_REDUCED, CollecticaseConstants.PAYMENT_CATEGORY_VARIABLE)
                .contains(paymentPlanActivityDTO.getPaymentPlanPaymentCategory())) {
            if (paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null &&
                    paymentPlanActivityDTO.getPaymentPlanGuideLineAmount() != null) {
                if (CollecticaseUtilFunction.greaterThanBigDecimalObject.test(paymentPlanActivityDTO
                                .getPaymentPlanPaymentAmount(),
                        paymentPlanActivityDTO.getPaymentPlanGuideLineAmount()) ||
                        CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO
                                        .getPaymentPlanPaymentAmount(),
                                paymentPlanActivityDTO.getPaymentPlanGuideLineAmount())) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                            .PAYMENT_AMOUNT_LESS_THAN_GUIDELINE_AMOUNT);
                }
            }
        }
        if (CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                BigDecimal.ZERO)) {
            if (List.of(CollecticaseConstants.PAYMENT_CATEGORY_REDUCED)
                    .contains(paymentPlanActivityDTO.getPaymentPlanPaymentCategory())) {
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail
                            .PAYMENT_AMOUNT_NON_ZERO_PAYMENT_CATEGORY_REDUCED);
                }
            }
        }
    }

    private static void validateFinAffidavit(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                             List<CollecticaseErrorEnum> errorEnums, Date currentDate) {
        if (List.of(ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd())) {
            if (paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.FA_SIGNED_DATE_REQUIRED);
            }
            if (paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate() != null &&
                    paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate().after(currentDate)) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.FA_SIGNED_DATE_FUTURE);
            }
        }
    }

    private static void validatePPSignedOn(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                           List<CollecticaseErrorEnum> errorEnums, Date currentDate) {
        if (List.of(ACTIVITY_TYPE_RECIEVED_PP_OFFSET, ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                        ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY)
                .contains(paymentPlanActivityDTO.getActivityTypeCd())) {
            if (paymentPlanActivityDTO.getPaymentPlanSignedDate() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.SIGNED_DATE_REQUIRED);
            }
            if (paymentPlanActivityDTO.getPaymentPlanSignedDate() != null &&
                    paymentPlanActivityDTO.getPaymentPlanSignedDate().after(currentDate)) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.SIGNED_DATE_FUTURE);
            }
        }
    }

    private static void validateResponseToAndGuideLineAmt(PaymentPlanActivityDTO paymentPlanActivityDTO,
                                                          List<CollecticaseErrorEnum> errorEnums) {
        if (List.of(ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                        ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP,
                        ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd())) {
            if (paymentPlanActivityDTO.getPaymentPlanGuideLineAmount() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_REQUIRED);
            }
            if (paymentPlanActivityDTO.getPaymentPlanGuideLineAmount() != null) {
                if (paymentPlanActivityDTO.getPaymentPlanGuideLineAmount().compareTo(BigDecimal.ZERO) == 0) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_ZERO);
                }
                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FOUR_DIGIT_TWO_DEICMAL_PATTERN,
                        String.valueOf(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount()))) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_INVALID);
                }
            }
        }

        if (List.of(ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                        ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP)
                .contains(paymentPlanActivityDTO.getActivityTypeCd())) {
            if (paymentPlanActivityDTO.getPaymentPlanResponseToCd() == null) {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.RESPONSE_TO_REQUIRED);
            }
            if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanResponseToCd(),
                    CollecticaseConstants.PAYMENT_RESPONSE_REASON_OTHER)) {
                if (StringUtils.isBlank(paymentPlanActivityDTO.getPaymentPlanResponseToOther())) {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.RESPONSE_TO_OTHER_REQUIRED);
                }
            }
        }
    }
}