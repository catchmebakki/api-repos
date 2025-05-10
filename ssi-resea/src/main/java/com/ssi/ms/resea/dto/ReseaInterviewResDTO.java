package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class ReseaInterviewResDTO {
    Long eventId;
    Map<String, Boolean> jmsItems;
    List<JobReferralDTO> outsideWebReferral;
    @JsonProperty("jMSJobReferral")
    List<JobReferralDTO> jMSJobReferral;
    @JsonFormat(pattern = DATE_FORMAT)
    Date jmsResumeExpDt, jmsVRExpDt;
    List<ReseaWrkSearchIssuesDTO> workSearchIssues;
    List<ReseaIssuesDTO> otherIssues;
    Map<String, Boolean> actionTaken;
    String reviewedMrpChap, assignedMrpChap;
    @JsonFormat(pattern = DATE_FORMAT)
    Date selfSchByDt;
    String staffNotes;
    String empServicesConfirmInd;
}