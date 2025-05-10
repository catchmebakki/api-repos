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
public class ReseaUploadStatsResDTO {
    private Long uploadId;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date requestDt;
    private String caseManagerName, officeName;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveStartDt;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveEndDt;
    private String status;
}