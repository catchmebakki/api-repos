package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaWrkSearchIssuesDTO {
    private boolean recent;
    private String status;
    private Long activelySeekingWork;
    @JsonFormat(pattern = DATE_FORMAT)
    private Date weekEndingDt;
    private Long workSearchEntryId;
    private boolean editable;

    /**
     * @param activelySeekingWork
     * @param weekEndingDt
     * @param workSearchEntryId
     * This is constructor used in ReseaWrkSrchWksReviewRswrRepository -> findInterviewWorkSearchIssues()
     */
    public ReseaWrkSearchIssuesDTO(Long activelySeekingWork, Date weekEndingDt, Long workSearchEntryId) {
        this.recent = true;
        if (activelySeekingWork == null || activelySeekingWork == 0L) {
            this.status = "noIssues";
            this.editable = true;
        } else {
            this.status = "createIssue";
            this.editable = false;
        }
        this.activelySeekingWork =activelySeekingWork;
        this.weekEndingDt = weekEndingDt;
        this.workSearchEntryId = workSearchEntryId;
    }
}
