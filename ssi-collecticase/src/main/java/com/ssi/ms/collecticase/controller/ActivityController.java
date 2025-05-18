package com.ssi.ms.collecticase.controller;

import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.FollowupActivityDTO;
import com.ssi.ms.collecticase.dto.OrganizationIndividualDTO;
import com.ssi.ms.collecticase.dto.StateDTO;
import com.ssi.ms.collecticase.inputpayload.ActivityInputPayload;
import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import com.ssi.ms.collecticase.outputpayload.ActivityUpdateContactPageResponse;
import com.ssi.ms.collecticase.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/activity")
@Validated
@Slf4j
@CrossOrigin
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping(path = "/actvitygeneral", produces = "application/json")
    public ResponseEntity<ActivityGeneralPageResponse> getGeneralActivityPage(@ModelAttribute ActivityInputPayload
                                                                                      activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getGeneralActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-entitycontact", produces = "application/json")
    public ResponseEntity<ActivityEntityContactResponse> getEntityContactActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEntityContactActivityPage(caseId, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-propertylien", produces = "application/json")
    public ResponseEntity<ActivityPropertyLienResponse> getPropertyLienActivityPage(@ModelAttribute ActivityInputPayload
                                                                                            activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getPropertyLienActivityPage(activityRemedyCd, caseId));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-followupshortnote", produces = "application/json")
    public ResponseEntity<ActivityFollowUpShortNoteResponse> getFollowUpShortNoteActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getFollowUpShortNoteActivityPage(activityTypeCd, activityRemedyCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-sendresend", produces = "application/json")
    public ResponseEntity<ActivitySendReSendResponse> getSendReSendActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getSendReSendActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activitypaymentplan", produces = "application/json")
    public ResponseEntity<ActivityPaymentPlanPageResponse> getPaymentPlanActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getPaymentPlanActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activitywagegarnish", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getWageGarnishmentActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getWageGarnishmentActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/awg-employercontact", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerContactWageGarnish
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long employerId = activityInputPayload.getEmployerId();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerContactWageGarnish(caseId, employerId));
    }

    @GetMapping(path = "/awg-employer", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerWageGarnish
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerWageGarnish(caseId));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/awg-employerrep", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerRepWageGarnish
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long employerId = activityInputPayload.getEmployerId();
        Long caseId = activityInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerRepWageGarnish(caseId, employerId));
    }

    @GetMapping(path = "/awg-other", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getWageGarnishOther(@ModelAttribute ActivityInputPayload
                                                                                           activityInputPayload) {
        Long employerId = activityInputPayload.getEmployerId();
        Long caseId = activityInputPayload.getCaseId();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getWageGarnishOther(caseId, employerId, activityTypeCd, activityRemedyCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activityupdatecontact", produces = "application/json")
    public ResponseEntity<ActivityUpdateContactPageResponse> getUpdateContactActivityPage
            (@ModelAttribute ActivityInputPayload activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        Long caseId = activityInputPayload.getCaseId();
        // Bak TODO need to decide and push all the calls here - static drop down list
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @GetMapping(path = "/getActivityInfoForFollowup", produces = "application/json")
    public ResponseEntity<FollowupActivityDTO> getActivityInfoForFollowup(@ModelAttribute ActivityInputPayload
                                                                                  activityInputPayload) {
        Long activityId = activityInputPayload.getActivityId();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getActivityInfoForFollowup(activityId));
    }

    @GetMapping(path = "/auc-country", produces = "application/json")
    public ResponseEntity<List<Map<String, String>>> getUpdateContactCountry() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactCountry());
    }

    @GetMapping(path = "/auc-state/{countryId}", produces = "application/json")
    public ResponseEntity<List<StateDTO>> getUpdateContactState(@Valid @PathVariable("countryId") Long countryId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactState(countryId));
    }

    @GetMapping(path = "/auc-entity", produces = "application/json")
    public ResponseEntity<Map<String, String>> getUpdateContactEntity(@ModelAttribute ActivityInputPayload
                                                                              activityInputPayload) {
        Long caseId = activityInputPayload.getCaseId();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactEntity(caseId, activityTypeCd));
    }

    @GetMapping(path = "/auc-contacts", produces = "application/json")
    public ResponseEntity<List<OrganizationIndividualDTO>> getUpdateContactContacts(@ModelAttribute ActivityInputPayload
                                                                                            activityInputPayload) {
        Long caseId = activityInputPayload.getCaseId();
        Long entityId = activityInputPayload.getEntityId();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactContacts(caseId, entityId));
    }

    @GetMapping(path = "/auc-rep", produces = "application/json")
    public ResponseEntity<List<EmployerListDTO>> getUpdateContactRep(@ModelAttribute ActivityInputPayload
                                                                             activityInputPayload) {
        Long caseId = activityInputPayload.getCaseId();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactRep(caseId));
    }

    @GetMapping(path = "/get-template", produces = "application/json")
    public ResponseEntity<Map<String, String>> getGeneralActivityGo(@ModelAttribute ActivityInputPayload
                                                                            activityInputPayload) {
        Long activityRemedyCd = activityInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = activityInputPayload.getActivityTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(activityService.getGeneralActivityGo(activityTypeCd, activityRemedyCd));
    }

}