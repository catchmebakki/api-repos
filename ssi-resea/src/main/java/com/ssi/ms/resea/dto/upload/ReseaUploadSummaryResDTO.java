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
public class ReseaUploadSummaryResDTO {
    private Long uploadId;
    private Long caseManagerId;
    private String caseManagerName;
    private Long officeId;
    private String officeName;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveStartDt;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveEndDt;
    private String status;
    private String errorInd, viewInd, finalInd, uploadInd, discardInd;
}