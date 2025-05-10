package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class HeaderDetailsResDTO {
    String claimant;
    Integer weeksFiled;
    String orientationDt;
    @JsonFormat(pattern = DATE_FORMAT)
    Date clmBeginDt, clmByDt, initialAppttDt, firstSubsequentApptDt, secondSubsequentApptDt, selfScheduleDt;
    Short orientatonRschCnt, initialAppttRschCnt, firstSubsequentApptRschCnt, secondSubsequentApptRschCnt;
    String meetingUrl;
    String meetingId, passcode;
    String telephoneNum1, telephoneNum2, phone, email;
    List<HeaderWorkSrchDetailsDTO> workSearch;
    List<HeaderIssueDetailsDTO> issues;
    List<HeaderJobRefDetailsDTO> jobReferrals;
    String rescheduleAccess, switchModeAccess, returnToWorkAccess, appointmentAccess, noShowAccess, submitAccess, reopenAccess, reopenInd;
    String ssnLast4Digits, appointmentStatus, caseStatus, caseStage;
    String applyWaitlistInd, clearWaitlistInd;
}
