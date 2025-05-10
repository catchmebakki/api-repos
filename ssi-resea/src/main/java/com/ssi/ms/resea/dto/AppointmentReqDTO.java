package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class AppointmentReqDTO {
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;
    List<String> itemsCompletedInJMS;
    List<JobReferralDTO> outsideWebReferral, jMSJobReferral;
    Date jmsResumeExpDt, jmsVRExpDt;
    Map<String, Long> workSearchIssues;
    List<IssuesDTO> otherIssues;
    List<String> actionTaken;
    String reviewedMrpChap, assignedMrpChap;
    String staffNotes;
    String empServicesConfirmInd;
    boolean includeThisNoteInCNO;
}