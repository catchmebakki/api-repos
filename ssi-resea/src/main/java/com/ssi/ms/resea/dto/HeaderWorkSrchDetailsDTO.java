package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class HeaderWorkSrchDetailsDTO {
    @JsonFormat(pattern = DATE_FORMAT)
    Date weekEndingDt;
    Long wsContacts,
            goldActivities,
            bronzeActivities;
    Short minWorkSrchReq;
    String inadequateWorkSrch;
    Long ccaId;
    String viewCertifiedCCFUrl;
}
