package com.ssi.ms.resea.dto;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STATUS_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class LookupSummaryReqDTO {
    List<Long> officeNum;
    List<Long> caseManagerId;
    Date apptStartDt, apptEndDt;
    List<Long> timeslotTypeCd, timeslotUsageCd;
    List<Long> meetingStatusCd;
    String beyond21DaysInd, hiPriorityInd;
    List<Long> scheduledBy;
    String claimantName;
    String ssn;
    Date clmByeStartDt, clmByeEndDt;
    PaginationDTO pagination;
    SortByDTO sortBy;
}