package com.ssi.ms.collecticase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.DATE_FORMAT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TIME_FORMAT_12_HR_AM;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class GeneralActivityDTO {

    @NotNull(message = "Activity date is mandatory")
    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = "Activity Date cannot be in future")
    private Date activityDate;
    @NotNull(message = "Activity time is mandatory")
    @JsonFormat(pattern = TIME_FORMAT_12_HR_AM)
    private String activityTime;
    @NotNull(message = "Case Characteristics is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9-,'. :;/#()&@$+]*$", message = "Case characteristics is invalid")
    private String activityCaseCharacteristics;
    @NotNull(message = "Communication method is mandatory")
    private Long activityCommunicationMethod;

    @NotNull(message = "Activity Specific is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9-,'. :;/#()&@$+]*$", message = "Case specifics is invalid")
    private String activitySpecifics;

    @Future(message = "Activity Date cannot be in past")
    private Date activityFollowupDate;

    private String activityFollowupShortNote;

    @NotNull(message = "Claimant Representative is mandatory")
    private Long activityClaimantRepresentative;

    private String activityEntityContact;

    private Long propertyLien;

    @Size(max = 3800, message = "Activity Notes cannot be more than 3800 Characters")
    private String activityNotes;

    private String activityNotesIncludeInNHUIS;

    @Size(max = 750, message = "Activity Notes in NHUIS cannot be more than 750 Characters")
    private String activityNHUISNotes;

    private String[] activitySendCorrespondence;
    private String[] activityReSendCorrespondence;

    public String[] getActivityManualCorrespondence() {
        return activityManualCorrespondence;
    }

    public void setActivityManualCorrespondence(String[] activityManualCorrespondence) {
        this.activityManualCorrespondence = activityManualCorrespondence;
    }

    private String[] activityManualCorrespondence;

    private Long activityTypeCd;

    private Long activityRemedyTypeCd;

    private String activityAdditionalNotes;

    private Long caseId;

    private Long claimantId;

    private Long caseStatusCd;

    private Long casePriorityCd;

    private BigDecimal claimantOpmBalAmount;

    private String callingUser;

    private String usingProgramName;

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityCaseCharacteristics() {
        return activityCaseCharacteristics;
    }

    public void setActivityCaseCharacteristics(String activityCaseCharacteristics) {
        this.activityCaseCharacteristics = activityCaseCharacteristics;
    }

    public Long getActivityCommunicationMethod() {
        return activityCommunicationMethod;
    }

    public void setActivityCommunicationMethod(Long activityCommunicationMethod) {
        this.activityCommunicationMethod = activityCommunicationMethod;
    }

    public String getActivitySpecifics() {
        return activitySpecifics;
    }

    public void setActivitySpecifics(String activitySpecifics) {
        this.activitySpecifics = activitySpecifics;
    }

    public Date getActivityFollowupDate() {
        return activityFollowupDate;
    }

    public void setActivityFollowupDate(Date activityFollowupDate) {
        this.activityFollowupDate = activityFollowupDate;
    }

    public String getActivityFollowupShortNote() {
        return activityFollowupShortNote;
    }

    public void setActivityFollowupShortNote(String activityFollowupShortNote) {
        this.activityFollowupShortNote = activityFollowupShortNote;
    }

    public Long getActivityClaimantRepresentative() {
        return activityClaimantRepresentative;
    }

    public void setActivityClaimantRepresentative(Long activityClaimantRepresentative) {
        this.activityClaimantRepresentative = activityClaimantRepresentative;
    }

    public String getActivityEntityContact() {
        return activityEntityContact;
    }

    public void setActivityEntityContact(String activityEntityContact) {
        this.activityEntityContact = activityEntityContact;
    }

    public Long getPropertyLien() {
        return propertyLien;
    }

    public void setPropertyLien(Long propertyLien) {
        this.propertyLien = propertyLien;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
    }

    public String getActivityNotesIncludeInNHUIS() {
        return activityNotesIncludeInNHUIS;
    }

    public void setActivityNotesIncludeInNHUIS(String activityNotesIncludeInNHUIS) {
        this.activityNotesIncludeInNHUIS = activityNotesIncludeInNHUIS;
    }

    public String getActivityNHUISNotes() {
        return activityNHUISNotes;
    }

    public void setActivityNHUISNotes(String activityNHUISNotes) {
        this.activityNHUISNotes = activityNHUISNotes;
    }

        public Long getActivityTypeCd() {
        return activityTypeCd;
    }

    public void setActivityTypeCd(Long activityTypeCd) {
        this.activityTypeCd = activityTypeCd;
    }

    public Long getActivityRemedyTypeCd() {
        return activityRemedyTypeCd;
    }

    public void setActivityRemedyTypeCd(Long activityRemedyTypeCd) {
        this.activityRemedyTypeCd = activityRemedyTypeCd;
    }

    public String getActivityAdditionalNotes() {
        return activityAdditionalNotes;
    }

    public void setActivityAdditionalNotes(String activityAdditionalNotes) {
        this.activityAdditionalNotes = activityAdditionalNotes;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }

    public Long getCaseStatusCd() {
        return caseStatusCd;
    }

    public void setCaseStatusCd(Long caseStatusCd) {
        this.caseStatusCd = caseStatusCd;
    }

    public Long getCasePriorityCd() {
        return casePriorityCd;
    }

    public void setCasePriorityCd(Long casePriorityCd) {
        this.casePriorityCd = casePriorityCd;
    }

    public BigDecimal getClaimantOpmBalAmount() {
        return claimantOpmBalAmount;
    }

    public void setClaimantOpmBalAmount(BigDecimal claimantOpmBalAmount) {
        this.claimantOpmBalAmount = claimantOpmBalAmount;
    }

    public String getCallingUser() {
        return callingUser;
    }

    public void setCallingUser(String callingUser) {
        this.callingUser = callingUser;
    }

    public String getUsingProgramName() {
        return usingProgramName;
    }

    public void setUsingProgramName(String usingProgramName) {
        this.usingProgramName = usingProgramName;
    }
}

