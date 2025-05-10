package com.ssi.ms.masslayoff.dto.lookup;

import com.ssi.ms.platform.dto.DateRangeDTO;
import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_ACC_NO_NOT_10_DIGIT;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_LOC_NOT_3_DIGIT;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.SSN_PATTEN_NOT_MATCHING;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object LookupReqDTO class for holding lookup request data.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class LookupReqDTO {
    private long mslId;
    @Size(min = 10, max = 10, message = EMPLOYER_ACC_NO_NOT_10_DIGIT)
    private String empUiAcctNbr;
    @Size(min = 3, max = 3, message = EMPLOYER_LOC_NOT_3_DIGIT)
    private String empUiAccLoc;
    @Pattern(regexp = "^[0-9]{9}$", message = SSN_PATTEN_NOT_MATCHING)
    private String claimantSsn;
    private DateRangeDTO layoffDateRange;
    private DateRangeDTO recallDateRange;
    private boolean currentAndFutureLayoffInd;
    private boolean pendingLayoffInd;
    @Valid
    private PaginationDTO pagination;
    private SortByDTO sortBy;
}
