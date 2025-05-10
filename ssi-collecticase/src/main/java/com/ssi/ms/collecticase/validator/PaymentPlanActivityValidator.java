package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.repository.*;
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
import java.util.*;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY;
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

    public HashMap<String, List<DynamicErrorDTO>> validatePaymentPlanActivity(PaymentPlanActivityDTO paymentPlanActivityDTO) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();

        Date currentDate = commonParRepository.getCurrentDate();
        List<VwCcaseHeaderDAO> vwCcaseHeaderDAOList = vwCcaseCaseloadRepository.getCaseHeaderInfoByCaseId(paymentPlanActivityDTO.getCaseId());
        VwCcaseHeaderDAO vwCcaseHeaderDAO = null;
        if(CollectionUtils.isNotEmpty(vwCcaseHeaderDAOList)) {
            vwCcaseHeaderDAO = vwCcaseHeaderDAOList.get(0);
        }
        String currFiling = vwCcaseHeaderDAO.getCurrFiling();
        if(List.of(ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                        ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP,
                        ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd()))
        {
            if(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount() == null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_REQUIRED);
            }
            if(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount() != null)
            {
                if(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount().compareTo(BigDecimal.ZERO) == 0)
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_ZERO);
                }
                if(CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FOUR_DIGIT_TWO_DEICMAL_PATTERN,
                        String.valueOf(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount())))
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_AMOUNT_INVALID);
                }
            }
        }

        if(List.of(ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                        ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP)
                .contains(paymentPlanActivityDTO.getActivityTypeCd()))
        {
            if(paymentPlanActivityDTO.getPaymentPlanReponseToCd() == null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.RESPONSE_TO_REQUIRED);
            }
            if (UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanReponseToCd(),
                    CollecticaseConstants.PAYMENT_RESPONSE_REASON_OTHER)) {
                if(StringUtils.isNotBlank(paymentPlanActivityDTO.getPaymentPlanReponseToOther()))
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.RESPONSE_TO_OTHER_REQUIRED);
                }
            }
        }

        if(List.of(ACTIVITY_TYPE_RECIEVED_PP_OFFSET, ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY)
                .contains(paymentPlanActivityDTO.getActivityTypeCd()))
        {
            if(paymentPlanActivityDTO.getPaymentPlanSignedDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.SIGNED_DATE_REQUIRED);
            }
            if(paymentPlanActivityDTO.getPaymentPlanSignedDate() != null &&
                    paymentPlanActivityDTO.getPaymentPlanSignedDate().after(currentDate))
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.SIGNED_DATE_FUTURE);
            }
        }

        if(List.of(ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd()))
        {
            if(paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate() == null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.FA_SIGNED_DATE_REQUIRED);
            }
            if(paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate() != null &&
                    paymentPlanActivityDTO.getPaymentPlanFinAffidavitSignedDate().after(currentDate))
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.FA_SIGNED_DATE_FUTURE);
            }
        }

        if(List.of(ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP, ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT)
                .contains(paymentPlanActivityDTO.getActivityTypeCd()))
        {
            if(paymentPlanActivityDTO.getPaymentPlanPaymentAmount() == null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_REQUIRED);
            }
            if(paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null)
            {
                if(paymentPlanActivityDTO.getPaymentPlanPaymentAmount().compareTo(BigDecimal.ZERO) == 0)
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_ZERO);
                }
                if(CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FOUR_DIGIT_TWO_DEICMAL_PATTERN,
                        String.valueOf(paymentPlanActivityDTO.getPaymentPlanPaymentAmount())))
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_INVALID);
                }
            }
        }

        if(UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE))
        {
            if(CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanGuideLineAmount(),
                    paymentPlanActivityDTO.getPaymentPlanPaymentAmount()))
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.GUIDE_LINE_PAYMENT_AMOUNT_NOT_SAME);
            }
        }
        else if(List.of(CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED,
                        CollecticaseConstants.PAYMENT_CATEGORY_REDUCED, CollecticaseConstants.PAYMENT_CATEGORY_VARIABLE)
                .contains(paymentPlanActivityDTO.getPaymentPlanPaymentCategory()))
        {
            if(!CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                    BigDecimal.ZERO.stripTrailingZeros()))
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_SHOULD_BE_ZERO);
            }
        }
        if(!CollecticaseUtilFunction.lessThanBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                paymentPlanActivityDTO.getPaymentPlanGuideLineAmount()))
        {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_AMOUNT_LESS_THAN_GUIDELINE_AMOUNT);
        }

        if(CollecticaseUtilFunction.greaterThanBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                BigDecimal.ZERO))
        {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_CATEGORY_REQUIRED);
        }

        if(paymentPlanActivityDTO.getPaymentPlanPaymentAmount() == null
                                && paymentPlanActivityDTO.getPaymentPlanPaymentCategory() != null)
        {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_APPLICABLE);
        }

        if(UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP))
        {
                if(List.of(CollecticaseConstants.PAYMENT_CATEGORY_REDUCED, CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED)
                        .contains(paymentPlanActivityDTO.getPaymentPlanPaymentCategory()))
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_CATEGORY_NOT_APPLICABLE);
                }
        }

        if(paymentPlanActivityDTO.getPaymentPlanPaymentAmount() != null
                        && !UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                                                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE))
        {
            errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_REQUIRED);
        }

        if(UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_GUIDELINE))
        {
            if(paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate() != null)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE);
            }
        }

        if(UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentCategory(),
                CollecticaseConstants.PAYMENT_CATEGORY_SUSPENDED))
        {
            if(paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate() != null)
            {
                //errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_NOT_APPLICABLE);
            }
        }

        if(UtilFunction.compareLongObject.test(paymentPlanActivityDTO.getActivityTypeCd(),
                ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT))
        {
            if(CollecticaseUtilFunction.daysBetweenStartAndEndTime().apply(currentDate,
                    paymentPlanActivityDTO.getPaymentPlanEffectiveUntilDate()) > 180)
            {
                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_EFF_UNITL_MORE_THAN_6_MONTHS);
            }
        }

        if(CollecticaseConstants.INDICATOR.Y.name().equals(vwCcaseHeaderDAO.getCurrFiling()))
        {
            if(List.of(ACTIVITY_TYPE_RECORD_DECISION_FIN_AFFIDAVIT,ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP).contains(
                    paymentPlanActivityDTO.getActivityTypeCd()))
            {
                List<OpmPayPlanOppDAO> opmPayPlanOppList = null;
                opmPayPlanOppList = opmPayPlanOppRepository.getOverpaymentPlanInfo(vwCcaseHeaderDAO.getCmtId());
                OpmPayPlanOppDAO opmPayPlanOppDAO = null;
                if(CollectionUtils.isEmpty(opmPayPlanOppList))
                {
                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_NO_ACTIVE_PAYMENT_PLAN);
                }else if(CollectionUtils.isNotEmpty(opmPayPlanOppList))
                {
                    opmPayPlanOppDAO = opmPayPlanOppList.get(0);
                    if(!CollecticaseUtilFunction.compareBigDecimalObject.test(paymentPlanActivityDTO.getPaymentPlanPaymentAmount(),
                            opmPayPlanOppDAO.getOppPaymentAmt())){
                        errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_PAYMENT_AMOUNT_NOT_SAME);
                    }
                }
            }
        }

        if(paymentPlanActivityDTO.getActivitySendCorrespondence() != null)
        {
            List<String> activityCorrespondenceList = Arrays
                    .stream(paymentPlanActivityDTO.getActivitySendCorrespondence())
                    .map(String::toUpperCase) // Transformation
                    .toList();
            List<CcaseCraCorrespondenceCrcDAO> ccaseCraCorrespondenceCrcDAOList = ccaseCraCorrespondenceCrcRepository.getManualCorrespondenceForRemedy(
                    List.of(CollecticaseConstants.INDICATOR.Y.name()),
                    List.of(CollecticaseConstants.INDICATOR.N.name()),
                    List.of(paymentPlanActivityDTO.getActivityRemedyTypeCd()));

            for(CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO : ccaseCraCorrespondenceCrcDAOList){
                if(activityCorrespondenceList.contains(String.valueOf(ccaseCraCorrespondenceCrcDAO.getCrcId()))){
                    if (CollecticaseConstants.INDICATOR.C.name().equals(ccaseCraCorrespondenceCrcDAO.getCrcEnable())) {
                        if(ccaseCraCorrespondenceCrcDAO.getCrcPmtCategory() != null)
                        {
                            if(paymentPlanActivityDTO.getPaymentPlanPaymentCategory() == null)
                            {
                                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NONE);
                            }
                            else if(!UtilFunction.compareLongObject.test(ccaseCraCorrespondenceCrcDAO.getCrcPmtCategory(),
                                    paymentPlanActivityDTO.getPaymentPlanPaymentCategory()))
                            {
                                errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_COR_PAYMENT_CATEGORY_NA);

                            }
                        }
                        if(ccaseCraCorrespondenceCrcDAO.getCrcCurrFiling() != null)
                        {
                            if(!currFiling.equals(ccaseCraCorrespondenceCrcDAO.getCrcCurrFiling()))
                            {
                                if(CollecticaseConstants.INDICATOR.N.name().equals(currFiling))
                                {
                                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_COR_CURRENT_FILING_NONE);
                                }
                                else
                                {
                                    errorEnums.add(ErrorMessageConstant.PaymentPlanActivityDTODetail.PAYMENT_PLAN_COR_CURRENT_FILING_NA);
                                }
                            }
                        }
                        if(ccaseCraCorrespondenceCrcDAO.getCrcCounty() != null)
                        {
                            if(paymentPlanActivityDTO.getPropertyLien() == null)
                            {
                                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                            }
                            else if (!UtilFunction.compareLongObject.test(ccaseCraCorrespondenceCrcDAO.getCrcCounty(),
                                    paymentPlanActivityDTO.getPropertyLien())) {
                                errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.CORRESPONDENCE_NOT_APPLICABLE_FOR_PROPERTY_LIEN);
                                errorParams.add(ccaseCraCorrespondenceCrcDAO.getCrcRptName());
                            }
                        }
                    }
                }
            }
        }

        CollecticaseUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
        return errorMap;
    }
}
