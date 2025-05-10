package com.ssi.ms.resea.dto.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_MANAGER_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EFFECTIVE_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.OFFICE_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaUploadSummaryReqDTO {
    @NotNull(message = CASE_MANAGER_ID_MANDATORY)
    private List<Long> caseManagerId;
    @NotNull(message = EFFECTIVE_DT_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveStartDt;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveEndDt;
}