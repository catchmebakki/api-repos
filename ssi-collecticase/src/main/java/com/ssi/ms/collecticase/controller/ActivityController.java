package com.ssi.ms.collecticase.controller;

import com.ssi.ms.collecticase.dto.FollowupActivityDTO;
import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.dto.WageGarnishmentActivityDTO;
import com.ssi.ms.collecticase.dto.StateDTO;
import com.ssi.ms.collecticase.dto.UpdateContactActivityDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.PaymentPlanActivityDTO;
import com.ssi.ms.collecticase.dto.OrganizationIndividualDTO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import com.ssi.ms.collecticase.dto.CompleteFollowupActivityDTO;
import com.ssi.ms.collecticase.dto.AppendNotesDTO;
import com.ssi.ms.collecticase.inputpayload.CcaseInputPayload;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ActivityGeneralPageResponse> getGeneralActivityPage(@ModelAttribute CcaseInputPayload
                                                                                      ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getGeneralActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-entitycontact", produces = "application/json")
    public ResponseEntity<ActivityEntityContactResponse> getEntityContactActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEntityContactActivityPage(caseId, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-propertylien", produces = "application/json")
    public ResponseEntity<ActivityPropertyLienResponse> getPropertyLienActivityPage(@ModelAttribute CcaseInputPayload
                                                                                            ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getPropertyLienActivityPage(activityRemedyCd, caseId));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-followupshortnote", produces = "application/json")
    public ResponseEntity<ActivityFollowUpShortNoteResponse> getFollowUpShortNoteActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getFollowUpShortNoteActivityPage(activityTypeCd, activityRemedyCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/ag-sendresend", produces = "application/json")
    public ResponseEntity<ActivitySendReSendResponse> getSendReSendActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getSendReSendActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activitypaymentplan", produces = "application/json")
    public ResponseEntity<ActivityPaymentPlanPageResponse> getPaymentPlanActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getPaymentPlanActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activitywagegarnish", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getWageGarnishmentActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getWageGarnishmentActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/awg-employercontact", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerContactWageGarnish
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long employerId = ccaseInputPayload.getEmployerId();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerContactWageGarnish(caseId, employerId));
    }

    @GetMapping(path = "/awg-employer", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerWageGarnish
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerWageGarnish(caseId));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/awg-employerrep", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getEmployerRepWageGarnish
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long employerId = ccaseInputPayload.getEmployerId();
        Long caseId = ccaseInputPayload.getCaseId();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getEmployerRepWageGarnish(caseId, employerId));
    }

    @GetMapping(path = "/awg-other", produces = "application/json")
    public ResponseEntity<ActivityWageGarnishmentPageResponse> getWageGarnishOther(@ModelAttribute CcaseInputPayload
                                                                                           ccaseInputPayload) {
        Long employerId = ccaseInputPayload.getEmployerId();
        Long caseId = ccaseInputPayload.getCaseId();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getWageGarnishOther(caseId, employerId, activityTypeCd, activityRemedyCd));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping(path = "/activityupdatecontact", produces = "application/json")
    public ResponseEntity<ActivityUpdateContactPageResponse> getUpdateContactActivityPage
            (@ModelAttribute CcaseInputPayload ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        Long caseId = ccaseInputPayload.getCaseId();
        // Bak TODO need to decide and push all the calls here - static drop down list
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactActivityPage(caseId, activityRemedyCd, activityTypeCd));
    }

    @GetMapping(path = "/cactivity-followup", produces = "application/json")
    public ResponseEntity<FollowupActivityDTO> getActivityInfoForFollowup(@ModelAttribute CcaseInputPayload
                                                                                  ccaseInputPayload) {
        Long activityId = ccaseInputPayload.getActivityId();
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
    public ResponseEntity<Map<String, String>> getUpdateContactEntity(@ModelAttribute CcaseInputPayload
                                                                              ccaseInputPayload) {
        Long caseId = ccaseInputPayload.getCaseId();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactEntity(caseId, activityTypeCd));
    }

    @GetMapping(path = "/auc-contacts", produces = "application/json")
    public ResponseEntity<List<OrganizationIndividualDTO>> getUpdateContactContacts(@ModelAttribute CcaseInputPayload
                                                                                            ccaseInputPayload) {
        Long caseId = ccaseInputPayload.getCaseId();
        Long entityId = ccaseInputPayload.getEntityId();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactContacts(caseId, entityId));
    }

    @GetMapping(path = "/auc-rep", produces = "application/json")
    public ResponseEntity<List<EmployerListDTO>> getUpdateContactRep(@ModelAttribute CcaseInputPayload
                                                                             ccaseInputPayload) {
        Long caseId = ccaseInputPayload.getCaseId();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(activityService
                .getUpdateContactRep(caseId));
    }

    @GetMapping(path = "/get-template", produces = "application/json")
    public ResponseEntity<Map<String, String>> getGeneralActivityGo(@ModelAttribute CcaseInputPayload
                                                                            ccaseInputPayload) {
        Long activityRemedyCd = ccaseInputPayload.getActivityRemedyTypeCd();
        Long activityTypeCd = ccaseInputPayload.getActivityTypeCd();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(activityService.getGeneralActivityGo(activityTypeCd, activityRemedyCd));
    }

    @PostMapping(path = "/orglookup", produces = "application/json")
    public ResponseEntity orgLookup(
            @Valid @RequestBody final OrgLookupDTO orgLookupDTO) {
        //Test Purpose
        /*orgLookupDTO.setEntityType(CollecticaseConstants.EMPLOYER_ENTITY_TYPE);
        orgLookupDTO.setCaseId(7142L);
        orgLookupDTO.setOrgName("RED FISH WHITE FISH");
        orgLookupDTO.setUiAcctNbr("0000531233");
        orgLookupDTO.setFein("764747611");*/
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                activityService.searchOrgLookup(orgLookupDTO));
    }

    @PostMapping(path = "/orglookup-reset", produces = "application/json")
    public ResponseEntity<OrgLookupDTO> orgLookupReset() {
        //Bak TODO need to check whether hit DB and reset the org based on caseId or simply pass new OrgLookupDTO
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                activityService.resetOrgLookup());
    }

    @PostMapping(path = "/complete-followup", produces = "application/json")
    public ResponseEntity completeFollowupActivity(
            @Valid @RequestBody final CompleteFollowupActivityDTO completeFollowupActivityDTO,
            BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.completeFollowupActivity(completeFollowupActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(completeFollowupActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/appendnotes", produces = "application/json")
    public ResponseEntity appendNotes(
            @Valid @RequestBody final AppendNotesDTO appendNotesDTO,
            BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.appendNotes(appendNotesDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(appendNotesDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/add-general", produces = "application/json")
    public ResponseEntity<GeneralActivityDTO> addGeneralActivity(
            @Valid @RequestBody final GeneralActivityDTO generalActivityDTO, BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.createGeneralActivity(generalActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(generalActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/add-paymentplan", produces = "application/json")
    public ResponseEntity<PaymentPlanActivityDTO> addGeneralActivity(
            @Valid @RequestBody final PaymentPlanActivityDTO paymentPlanActivityDTO, HttpServletRequest request,
            BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.createPaymentPlanActivity(paymentPlanActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(paymentPlanActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/add-wagegarnish", produces = "application/json")
    public ResponseEntity<GeneralActivityDTO> addGeneralActivity(
            @Valid @RequestBody final WageGarnishmentActivityDTO wageGarnishmentActivityDTO, BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.createWageGarnishmentActivity(wageGarnishmentActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(wageGarnishmentActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }

    @PostMapping(path = "/add-updatecontact", produces = "application/json")
    public ResponseEntity<UpdateContactActivityDTO> addUpdateContactActivity(
            @Valid @RequestBody final UpdateContactActivityDTO updateContactActivityDTO, BindingResult result) {
        if(!result.hasErrors())
        {
            activityService.createUpdateContactActivity(updateContactActivityDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updateContactActivityDTO);
        }
        else {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
    }


}