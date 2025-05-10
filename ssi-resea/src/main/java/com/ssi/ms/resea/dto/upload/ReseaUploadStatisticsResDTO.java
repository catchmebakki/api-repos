package com.ssi.ms.resea.dto.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.platform.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object UploadStatisticsResDTO class for representing a response in upload statistics.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class ReseaUploadStatisticsResDTO {
    private PaginationDTO pagination;
    private Long docId,
            uploadId;
    private String caseManager,
            officeName;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveDt;
    private String uploadedBy;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date uploadedOn;
    @JsonFormat(pattern = "h:m a")
    private LocalTime uploadedAt;
    private Integer totalNoOfRecords,
            noOfErrors;
    private String fileName,
            uploadStatusCdValue,
            errorDescription;
    private UploadStatisticsSummaryListResDTO summaryListResDTO;
    private List<ReseaUploadStatisticsItemResDTO> errorRecordList;
}
