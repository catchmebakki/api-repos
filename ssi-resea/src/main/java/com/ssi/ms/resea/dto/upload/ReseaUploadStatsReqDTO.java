package com.ssi.ms.resea.dto.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_MANAGER_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EFFECTIVE_DT_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaUploadStatsReqDTO {
    @NotNull(message = "uploadId.mandatory")
    private Long uploadId;
    @NotNull(message = CASE_MANAGER_ID_MANDATORY)
    private Long caseManagerId;
    @NotNull(message = EFFECTIVE_DT_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveStartDt;
}