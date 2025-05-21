package com.ssi.ms.collecticase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CompleteFollowupActivityDTO {

    private String caseId;

    private String staffId;

    private String userId;

    private String activityCreatedDate;

    private String activityId;

    @NotNull(message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY)
    @PastOrPresent(message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_ON_FUTURE)
    @Pattern(regexp = CollecticaseUtilFunction.DATE_PATTERN_MM_DD_YYYY, message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_ON_MANDATORY)
    private String activityCompletedOn;

    @NotNull(message = ErrorMessageConstant.COMPELTE_FOLLOWUP_COMPLETED_BY_MANDATORY)
    private String activityCompletedBy;

    private String activityCompletedNote;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getActivityCreatedDate() {
        return activityCreatedDate;
    }

    public void setActivityCreatedDate(String activityCreatedDate) {
        this.activityCreatedDate = activityCreatedDate;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityCompletedOn() {
        return activityCompletedOn;
    }

    public void setActivityCompletedOn(String activityCompletedOn) {
        this.activityCompletedOn = activityCompletedOn;
    }

    public String getActivityCompletedBy() {
        return activityCompletedBy;
    }

    public void setActivityCompletedBy(String activityCompletedBy) {
        this.activityCompletedBy = activityCompletedBy;
    }

    public String getActivityCompletedNote() {
        return activityCompletedNote;
    }

    public void setActivityCompletedNote(String activityCompletedNote) {
        this.activityCompletedNote = activityCompletedNote;
    }
}
