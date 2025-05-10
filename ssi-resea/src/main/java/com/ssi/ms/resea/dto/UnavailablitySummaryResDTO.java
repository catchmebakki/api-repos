package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.USR_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class UnavailablitySummaryResDTO {
    String staffName;
    Long unavailabilityId;
    String type, period, reason, status, notes, editable, requestInd, approveInd, rejectInd, withdrawInd;
    boolean recurring;
    @JsonIgnore
    Date startDateTime, endDateTime;
    @JsonIgnore
    Long statusId;
    @JsonIgnore
    String typeInd;
    public UnavailablitySummaryResDTO(String staffName, Long unavailabilityId, String type, String period, String reason, String status, String notes, String editable,
                                      String requestInd, Long statusId, String typeInd, Date startDateTime, Date endDateTime){
        this.staffName = staffName;
        this.unavailabilityId = unavailabilityId;
        this.type = type;
        this.period = period;
        this.reason = reason;
        this.status = status;
        this.notes = notes;
        this.editable = editable;
        this.requestInd = requestInd;
        this.statusId = statusId;
        this.typeInd = typeInd;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}