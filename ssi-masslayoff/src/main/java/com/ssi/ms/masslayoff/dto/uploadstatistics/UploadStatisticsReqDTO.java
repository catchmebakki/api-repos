package com.ssi.ms.masslayoff.dto.uploadstatistics;

import com.ssi.ms.platform.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MUSM_ID_MANDATORY;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object UploadStatisticsReqDTO class for holding upload statistics request data.
 */
@With
@Builder
@AllArgsConstructor
@Getter
@Setter
public class UploadStatisticsReqDTO {
    @NotNull(message = MUSM_ID_MANDATORY)
    private Long musmId;
    @Valid
    private PaginationDTO pagination;
    private Boolean needSummaryDetails;
}
