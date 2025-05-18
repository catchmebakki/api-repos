package com.ssi.ms.collecticase.util;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseCaseRemedyCmrDAO;
import com.ssi.ms.collecticase.database.dao.CcaseWageGarnishmentCwgDAO;
import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import com.ssi.ms.platform.util.UtilFunction;

import java.math.BigDecimal;
import java.util.*;

public class CollecticaseHelper {

    public static Map<String, String> getFraudStatusValues(boolean caseLoad) {
        Map<String, String> overPaymentTypeList = new LinkedHashMap<>();
        overPaymentTypeList.put(CollecticaseConstants.FRAUD_STATUS_FRAUD_VALUE,
                CollecticaseConstants.FRAUD_STATUS_FRAUD);
        overPaymentTypeList.put(CollecticaseConstants.FRAUD_STATUS_NF_EARNING_VALUE,
                CollecticaseConstants.FRAUD_STATUS_NF_EARNING);
        overPaymentTypeList.put(CollecticaseConstants.FRAUD_STATUS_FRAUD_NF_EARNING_VALUE,
                CollecticaseConstants.FRAUD_STATUS_FRAUD_NF_EARNING);
        overPaymentTypeList.put(CollecticaseConstants.FRAUD_STATUS_NF_VALUE,
                CollecticaseConstants.FRAUD_STATUS_NF);
        if (caseLoad) {
            overPaymentTypeList.put(CollecticaseConstants.BANKRUPTCY_VALUE,
                    CollecticaseConstants.BANKRUPTCY);
        }
        return overPaymentTypeList;
    }

    public static Map<String, String> getCLOverPaymentValue() {
        Map<String, String> overPaymentList = new LinkedHashMap<>();
        overPaymentList.put(CollecticaseConstants.ZERO_TO_NINE_NINE_NINE,
                CollecticaseConstants.ZERO_TO_NINE_NINE_NINE_DOLLAR);
        overPaymentList.put(CollecticaseConstants.THOUSAND_TO_TWO_FOUR_NINE_NINE,
                CollecticaseConstants.THOUSAND_TO_TWO_FOUR_NINE_NINE_DOLLAR);
        overPaymentList.put(CollecticaseConstants.TWO_FIVE_ZERO_ZERO_TO_FOUR_NINE_NINE_NINE,
                CollecticaseConstants.TWO_FIVE_ZERO_ZERO_TO_FOUR_NINE_NINE_NINE_DOLLAR);
        overPaymentList.put(CollecticaseConstants.FIVE_THOUSAND_TO_ABOVE,
                CollecticaseConstants.FIVE_THOUSAND_TO_ABOVE_DOLLAR);
        return overPaymentList;
    }

    public static Map<String, String> getCLFollowUpValue() {
        Map<String, String> nextFollowupList = new LinkedHashMap<>();
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE_ONE_MONTH_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE_ONE_MONTH);
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE_ONE_WEEK_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE_ONE_WEEK);
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_DUE_TODAY_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_DUE_TODAY);
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_DUE_WITHIN_MONTH_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_DUE_WITHIN_MONTH);
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_DUE_WITHIN_WEEK_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_DUE_WITHIN_WEEK);
        nextFollowupList.put(CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE_VALUE,
                CollecticaseConstants.NEXT_FOLLOWUP_OVERDUE);
        return nextFollowupList;
    }

    public static void populateEnableDisableReqDate(Long activityTypeCd,
                                  ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse,
                                                    CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy) {

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

        List<Long> wageAmtChangeSuspendWageList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_CMT_REQ_WG_AMT_CHANGE,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT);

        if (wageAmtChangeSuspendWageList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setDisableWageMotionFiledOn(false);
        }
    }

    public static void populateEnableDisableEffDate(Long activityTypeCd,
                                       ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse) {

        List<Long> changeWGSuspendWageList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SUSPEND_WAGE_GARNISHMENT);

        if (changeWGSuspendWageList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setWageEffectiveFrom(null);
            activityWageGarnishmentPageResponse.setWageEffectiveUntil(null);

        } else {
            activityWageGarnishmentPageResponse.setDisableWageEffectiveFrom(true);
            activityWageGarnishmentPageResponse.setDisableWageEffectiveUntil(true);
        }
    }

    public static void populateEnableDisableCourt(Long activityTypeCd,
                                ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse,
                                                  CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy) {

        activityWageGarnishmentPageResponse.setCourtId(wageInfoForCaseEmployerRemedy.getFkCmoIdCourt());

        List<Long> motionChangeSendWageNoticeList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_FILED_MOTION_PERIODIC_PYMTS,
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
    }

    public static void populateEnableDisableCourtOrder(Long activityTypeCd,
                            ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse) {

        List<Long> changeSendWageNoticeList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT,
                CollecticaseConstants.ACTIVITY_TYPE_SENT_NOTICE_OF_WG);
        if (!changeSendWageNoticeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setDisableCourtOrderedInd(true);
        }
        if (changeSendWageNoticeList.contains(activityTypeCd)) {
            activityWageGarnishmentPageResponse.setCourtOrderedInd(null);
        }
    }

    public static void populateEnableDisableCourtOrderDt(Long activityTypeCd,
                              ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse) {

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
    }

    public static void populateEnableDisableNonComp(Long activityTypeCd,
                             ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse) {

        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_EMPLOYER_NON_COMPLIANCE)) {
            activityWageGarnishmentPageResponse.setDisableWageNonCompliance(true);
        }
    }

    public static void populateEnableDisableWGFreq(ActivityWageGarnishmentPageResponse
                     activityWageGarnishmentPageResponse, CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy) {

        activityWageGarnishmentPageResponse.setWageFrequency(wageInfoForCaseEmployerRemedy
                .getCwgFreqCd());
    }

    public static void populateEnableDisableDoNotGarnish(Long activityTypeCd,
                                       ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse,
                                                         CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy) {

        activityWageGarnishmentPageResponse.setDoNotGarnishInd(wageInfoForCaseEmployerRemedy.getCwgDoNotGarnish());
        activityWageGarnishmentPageResponse.setDisableDoNotGarnishInd(true);

        List<Long> emplmntWGAmtChangeList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_RESEARCH_FOR_EMPLOYMENT,
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
    }

    public static void populateEnableDisableWGAmount(Long activityTypeCd,
                                   ActivityWageGarnishmentPageResponse activityWageGarnishmentPageResponse,
                                                     CcaseWageGarnishmentCwgDAO wageInfoForCaseEmployerRemedy) {

        activityWageGarnishmentPageResponse.setWageAmount(wageInfoForCaseEmployerRemedy
                .getCwgWgAmount());
        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_CHANGE_WG_GARNISH_AMT)) {
            activityWageGarnishmentPageResponse.setDisableWageAmount(true);
        }
    }

    public static List<Map<String, String>> getUpdateContactCountry() {
        List<Map<String, String>> countryList = new ArrayList<>();

        Map<String, String> countryMapCanada = new HashMap<>();
        Map<String, String> countryMapUS = new HashMap<>();

        countryMapCanada.put(CollecticaseConstants.COUNTRY_ID, String.valueOf(CollecticaseConstants.CANANDA));
        countryMapCanada.put(CollecticaseConstants.COUNTRY_NAME, CollecticaseConstants.CANADA_NAME);
        countryMapUS.put(CollecticaseConstants.COUNTRY_ID, String.valueOf(CollecticaseConstants.UNITED_STATES));
        countryMapUS.put(CollecticaseConstants.COUNTRY_NAME, CollecticaseConstants.UNITED_STATES_NAME);

        countryList.add(countryMapUS);
        countryList.add(countryMapCanada);

        return countryList;
    }

    public static void populatePPPaymentCatCd(Long activityTypeCd,
                              ActivityPaymentPlanPageResponse activityPaymentPlanPageResponse,
                                               CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy) {
        List<Long> ppSignedOffsetList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PAYMENT_NO_SIGNED_PP,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET);

        if (ppSignedOffsetList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanPaymentCategory
                    (caseRemedyByCaseRemedy.getCmrPpPaymentCatgCd());
            activityPaymentPlanPageResponse.setPaymentPlanEffectiveUntilDate
                    (caseRemedyByCaseRemedy.getCmrPpEffUntilDt());
        }
    }

    public static void populateEnableDisablePPPaymentAmt(Long activityTypeCd,
                            ActivityPaymentPlanPageResponse activityPaymentPlanPageResponse,
                                                          CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy) {
        List<Long> finAffList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_INITIATE_FINANCIAL_AFFIDAVIT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT);

        List<Long> guideLineDecFinAffList = Arrays.asList(
                CollecticaseConstants.ACTIVITY_TYPE_INITIATE_GUIDELINE_BASED_PP,
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
    }

    public static void populateEnableDisableFASignedOn(Long activityTypeCd,
                             ActivityPaymentPlanPageResponse activityPaymentPlanPageResponse,
                                                        CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy) {
        activityPaymentPlanPageResponse.setPaymentPlanFinAffidavitSignedDate
                (caseRemedyByCaseRemedy.getCmrPpFaSignedDt());
        if (!UtilFunction.compareLongObject.test(activityTypeCd,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_COMPLETE_FIN_AFFIDAVIT)) {
            activityPaymentPlanPageResponse.setDisablePPFASignedDate(true);
        }
    }

    public static void populateEnableDisablePPSignedDt(Long activityTypeCd,
                               ActivityPaymentPlanPageResponse activityPaymentPlanPageResponse,
                                                        CcaseCaseRemedyCmrDAO caseRemedyByCaseRemedy) {
        List<Long> signedOffsetPPList = Arrays.asList(CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_PP_OFFSET,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_PAYMENT,
                CollecticaseConstants.ACTIVITY_TYPE_RECIEVED_SIGNED_PP_ONLY);

        if (signedOffsetPPList.contains(activityTypeCd)) {
            activityPaymentPlanPageResponse.setPaymentPlanSignedDate(caseRemedyByCaseRemedy.getCmrPpPpSignedDt());
        } else {
            activityPaymentPlanPageResponse.setDisablePPSignedDate(true);
        }
    }

}
