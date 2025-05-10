package com.ssi.ms.resea.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object UploadStatisticsItemResDTO class for representing a response item in upload statistics.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ReseaUploadStatisticsItemResDTO {
    private String dayOfWeek;
    private String time;
    private String allowOnline;
    private String allowRemote;
    private String errorMessage;
}
