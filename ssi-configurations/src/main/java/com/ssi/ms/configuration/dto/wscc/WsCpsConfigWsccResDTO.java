package com.ssi.ms.configuration.dto.wscc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.With;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class WsCpsConfigWsccResDTO {
    private Long wsccId;
    private String program, comments;
    private Long initialClm, additionalClm, incrementFrequency, incrementVal;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;
    private boolean editFlag;
}
