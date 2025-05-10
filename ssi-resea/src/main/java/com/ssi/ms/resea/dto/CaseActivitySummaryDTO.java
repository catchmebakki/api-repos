package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_TIME_FORMAT_24H_SEC;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class CaseActivitySummaryDTO {
    Long activityId;
    @JsonFormat(pattern = DATE_TIME_FORMAT_24H_SEC)
    Date activityDt;
    String stage, activity, user, description, detail,
            followUpInd, addFollowUpInd, editFollowUpInd,
            noteInd, addNoteInd, note, followUpType, followUpNote;
    Long followUpTypeCd;
    Date followUpDt;
    @JsonIgnore
    String caseSynopsis;

    // Do not delete, this method is used in ReseaCaseActivityRscaRepository -> getCaseActivitySummary method.
    public CaseActivitySummaryDTO(Long activityId, Date activityDt, String stage, String activity, String user,
                                  String description, String detail, String note, String addNoteInd,
                                  String followUpInd, String followUpType, Date followUpDt, String addFollowUpInd, String editFollowUpInd,
                                  String followUpNote, Long followUpTypeCd){
        this.activityId = activityId;
        this.activityDt = activityDt;
        this.stage = stage;
        this.activity = activity;
        this.user = user;
        this.description = description;
        this.detail = detail;
        this.note = note;
        if (StringUtils.isEmpty(note)) {
            this.noteInd = "N";
        } else {
            this.noteInd = "Y";
        }
        this.followUpInd = followUpInd;
        this.followUpType = followUpType;
        this.followUpDt = followUpDt;
        this.addNoteInd = addNoteInd;
        this.addFollowUpInd = addFollowUpInd;
        this.editFollowUpInd = editFollowUpInd;
        this.followUpNote = followUpNote;
        this.followUpTypeCd = followUpTypeCd;
    }
}
