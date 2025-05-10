package com.ssi.ms.collecticase.dto;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;


public class CreateActivityDTO {

    private Long caseId;
    private Long employerId;
    private Long activityTypeCd;
    private Long remedyTypeCd;
    private Date activityDt;
    private String activityTime;
    private String activitySpecifics;
    private String activityNotes;
    private String activityNotesAdditional;
    private String activityNotesNHUIS;
    private Long communicationMethod;
    private String caseCharacteristics;
    private Long activityCmtRepCd;
    private Long activityCasePriority;
    private Date followupDt;
    private String followupShortNote;
    private String followupCompleteShortNote;
    private String callingUser;
    private String usingProgramName;

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public Long getActivityTypeCd() {
        return activityTypeCd;
    }

    public void setActivityTypeCd(Long activityTypeCd) {
        this.activityTypeCd = activityTypeCd;
    }

    public Long getRemedyTypeCd() {
        return remedyTypeCd;
    }

    public void setRemedyTypeCd(Long remedyTypeCd) {
        this.remedyTypeCd = remedyTypeCd;
    }

    public Date getActivityDt() {
        return activityDt;
    }

    public void setActivityDt(Date activityDt) {
        this.activityDt = activityDt;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivitySpecifics() {
        return activitySpecifics;
    }

    public void setActivitySpecifics(String activitySpecifics) {
        this.activitySpecifics = activitySpecifics;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
    }

    public String getActivityNotesAdditional() {
        return activityNotesAdditional;
    }

    public void setActivityNotesAdditional(String activityNotesAdditional) {
        this.activityNotesAdditional = activityNotesAdditional;
    }

    public String getActivityNotesNHUIS() {
        return activityNotesNHUIS;
    }

    public void setActivityNotesNHUIS(String activityNotesNHUIS) {
        this.activityNotesNHUIS = activityNotesNHUIS;
    }

    public Long getCommunicationMethod() {
        return communicationMethod;
    }

    public void setCommunicationMethod(Long communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    public String getCaseCharacteristics() {
        return caseCharacteristics;
    }

    public void setCaseCharacteristics(String caseCharacteristics) {
        this.caseCharacteristics = caseCharacteristics;
    }

    public Long getActivityCmtRepCd() {
        return activityCmtRepCd;
    }

    public void setActivityCmtRepCd(Long activityCmtRepCd) {
        this.activityCmtRepCd = activityCmtRepCd;
    }

    public Long getActivityCasePriority() {
        return activityCasePriority;
    }

    public void setActivityCasePriority(Long activityCasePriority) {
        this.activityCasePriority = activityCasePriority;
    }

    public Date getFollowupDt() {
        return followupDt;
    }

    public void setFollowupDt(Date followupDt) {
        this.followupDt = followupDt;
    }

    public String getFollowupShortNote() {
        return followupShortNote;
    }

    public void setFollowupShortNote(String followupShortNote) {
        this.followupShortNote = followupShortNote;
    }

    public String getFollowupCompleteShortNote() {
        return followupCompleteShortNote;
    }

    public void setFollowupCompleteShortNote(String followupCompleteShortNote) {
        this.followupCompleteShortNote = followupCompleteShortNote;
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

