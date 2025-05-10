package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DashboardCalResDTO {
    Long eventId; //eventId
    String label;
    @JsonFormat(pattern = DATE_FORMAT)
    Date appointmentDt;
    String startTime, endTime;
    String appointmentType;
    //String type;
    @JsonIgnore
    String firstName, lastName;
    String eventTypeDesc, usageDesc;
    Boolean eventSubmitted;
    @JsonIgnore
    Long eventType, usage;
    @JsonIgnore
    Long submitId;
    String mtgStatusDesc;
}
