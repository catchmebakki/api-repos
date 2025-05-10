package com.ssi.ms.resea.dto.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.resea.dto.IssuesDTO;
import com.ssi.ms.resea.dto.JobReferralDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_MANAGER_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EFFECTIVE_DT_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.OFFICE_ID_MANDATORY;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ReseaUploadFormReqDTO {
    @NotNull(message = CASE_MANAGER_ID_MANDATORY)
    private Long caseManagerId;
    @NotNull(message = OFFICE_ID_MANDATORY)
    private Long officeId;
    @NotNull(message = EFFECTIVE_DT_MANDATORY)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date effectiveDt;
}