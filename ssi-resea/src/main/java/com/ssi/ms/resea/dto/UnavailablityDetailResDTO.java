package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class UnavailablityDetailResDTO {
    Long unavailabilityId;
    @JsonFormat(pattern = DATE_FORMAT)
    Date startDt, endDt;
    Long reason;
    String startTime, endTime, referenceNotes;
    List<Integer> days;
    boolean recurring;
}