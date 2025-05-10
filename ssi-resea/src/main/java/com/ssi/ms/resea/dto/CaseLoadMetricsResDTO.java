package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;



@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class CaseLoadMetricsResDTO {
    Long initialInterview, firstSubInterview, secondSubInterview,
            pendingSchedule, followUp, hiPriority, failed, delayed,
            waitlisted;
}
