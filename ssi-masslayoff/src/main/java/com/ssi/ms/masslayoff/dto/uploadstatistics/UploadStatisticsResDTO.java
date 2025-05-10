package com.ssi.ms.masslayoff.dto.uploadstatistics;

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
 * @author Praveenraja Paramsivam
 * Data Transfer Object UploadStatisticsResDTO class for representing a response in upload statistics.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class UploadStatisticsResDTO {
    private PaginationDTO pagination;
    private Long musmId;
    private Long mrlrId;
    private Long mslNumber;
    private String empAccNbr;
    private String empAccLoc;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date mslDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date mslEffDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date recallDate;
    private String uploadedBy;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date uploadedOn;
    @JsonFormat(pattern = "h:m a")
    private LocalTime uploadedAt;
    private Integer totalNoOfClaimants;
    private Integer noOfErrorClaimants;
    private String fileName;
    private String uploadStatusCdValue;
    private String errorDescription;
    private UploadStatisticsSummaryListResDTO summaryListResDTO;
    private List<UploadStatisticsItemResDTO> errorRecordList;
}
