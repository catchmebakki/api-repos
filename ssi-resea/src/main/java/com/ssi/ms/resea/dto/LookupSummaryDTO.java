package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
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
public class LookupSummaryDTO {
    Long eventId;
    String officeName,
            caseManagerName,
            eventDateTime,
            eventType,
            eventUsage,
            meetingStatus,
            claimantName,
            lastFourDigitsOfSSN;
    @JsonFormat(pattern = DATE_FORMAT)
    Date byeDt;
    String indicator;
}