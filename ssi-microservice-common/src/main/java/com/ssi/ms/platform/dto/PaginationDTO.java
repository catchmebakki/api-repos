package com.ssi.ms.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

import static com.ssi.ms.constant.ErrorMessageConstant.ITEM_PER_PAGE_NOT_VALID;
import static com.ssi.ms.constant.ErrorMessageConstant.PAGE_NUMBER_NOT_VALID;
/**
 * @author Praveenraja Paramsivam
 *Data Transfer Object ClaimantResDTO class for representing pagination.
 */
@Builder
@AllArgsConstructor
@Validated
@Getter
@With
public class PaginationDTO {
    @Min(value = 1, message = PAGE_NUMBER_NOT_VALID)
    private Integer pageNumber;
    @Min(value = 1, message = ITEM_PER_PAGE_NOT_VALID)
    private Integer pageSize;
    private Boolean needTotalCount;
    private Long totalItemCount;
}
