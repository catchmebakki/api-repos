package com.ssi.ms.resea.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaUploadStatReqDTO {
    @NotNull(message = "uploadId.mandatory")
    private Long uploadId;
    private Long docId;
    private Boolean needSummaryDetails;
}