package com.ssi.ms.resea.dto.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaUploadRecordsResDTO {
    private String dayOfWeek,
            startTime, endTime,
            interviewTypeCd,
            meetingUrl, meetingId, meetingPwd,
            meetingPhone1, meetingPhone2, meetingOtherRef,
            allowOnsiteInd, allowRemoteInd;
}