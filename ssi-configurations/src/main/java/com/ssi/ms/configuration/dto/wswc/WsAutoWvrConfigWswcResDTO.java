package com.ssi.ms.configuration.dto.wswc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class WsAutoWvrConfigWswcResDTO {
    private Long wswcId;
    private String scenario, reasonCd, reasonVal, reasonsAlcId, fkLofId, businessUnit,
            comments, autoOverwrite, event, eventSpecifics, addlRef, splActions;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;
    private boolean editFlag;

}
