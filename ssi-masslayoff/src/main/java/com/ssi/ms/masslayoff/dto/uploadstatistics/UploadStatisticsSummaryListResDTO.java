package com.ssi.ms.masslayoff.dto.uploadstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
public class UploadStatisticsSummaryListResDTO {
    private Integer totalNoOfRecords;
    private Integer currentItem;
    private List<Long> musmIds;
}
