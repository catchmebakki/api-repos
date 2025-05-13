package com.ssi.ms.collecticase.controller;

import com.ssi.ms.collecticase.inputpayload.ActivityInputPayload;
import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import com.ssi.ms.collecticase.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/activity")
@Validated
@Slf4j
@CrossOrigin
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping(path = "/getGeneral", produces = "application/json")
    public ActivityGeneralPageResponse getGeneralActivityPage(@ModelAttribute ActivityInputPayload
                                                                      activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getGeneralActivityPage(caseId, activityRemedyCd, activityTypeCd);
    }

    @GetMapping(path = "/getEntityContact", produces = "application/json")
    public ActivityEntityContactResponse getEntityContactActivityPage(@ModelAttribute ActivityInputPayload
                                                                              activityInputPayload) {
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getEntityContactActivityPage(caseId, activityTypeCd);
    }

    @GetMapping(path = "/getPropertyLien", produces = "application/json")
    public ActivityPropertyLienResponse getPropertyLienActivityPage(@ModelAttribute ActivityInputPayload
                                                                            activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getPropertyLienActivityPage(activityRemedyCd, caseId);
    }

    @GetMapping(path = "/getFollowUpShortNote", produces = "application/json")
    public ActivityFollowUpShortNoteResponse getFollowUpShortNoteActivityPage(@ModelAttribute ActivityInputPayload
                                                                                      activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();

        return activityService.getFollowUpShortNoteActivityPage(activityTypeCd, activityRemedyCd);
    }

    @GetMapping(path = "/getSendReSend", produces = "application/json")
    public ActivitySendReSendResponse getSendReSendActivityPage(@ModelAttribute ActivityInputPayload
                                                                        activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getSendReSendActivityPage(caseId, activityRemedyCd, activityTypeCd);
    }

    @GetMapping(path = "/getPaymentPlan", produces = "application/json")
    public ActivityPaymentPlanPageResponse getPaymentPlanActivityPage(@ModelAttribute ActivityInputPayload
                                                                      activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getPaymentPlanActivityPage(caseId, activityRemedyCd, activityTypeCd);
    }

    @GetMapping(path = "/getWageGarnishment", produces = "application/json")
    public ActivityWageGarnishmentPageResponse getWageGarnishmentActivityPage(@ModelAttribute ActivityInputPayload
                                                                              activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return activityService.getWageGarnishmentActivityPage(caseId, activityRemedyCd, activityTypeCd);
    }

}