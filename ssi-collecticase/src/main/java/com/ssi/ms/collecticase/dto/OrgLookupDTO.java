package com.ssi.ms.collecticase.dto;

import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ORG_LOOKUP_FEIN_INVALID;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ORG_LOOKUP_ORG_NAME_INVALID;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ORG_LOOKUP_UI_ACCT_NBR_INVALID;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class OrgLookupDTO {

    @Pattern(regexp = CollecticaseUtilFunction.ALPHANUMERIC_PATTERN, message = ORG_LOOKUP_ORG_NAME_INVALID)
    private String orgName;
    @Pattern(regexp = CollecticaseUtilFunction.UI_ACCT_NBR_PATTERN, message = ORG_LOOKUP_UI_ACCT_NBR_INVALID)
    private String uiAcctNbr;
    @Pattern(regexp = CollecticaseUtilFunction.FEIN_NUMERIC_PATTERN, message = ORG_LOOKUP_FEIN_INVALID)
    private String fein;
    private String entityType;
    private Long caseId;
}
