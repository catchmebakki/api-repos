package com.ssi.ms.fraudreview.dto.lookup;

import static com.ssi.ms.constant.ErrorMessageConstant.ITEM_PER_PAGE_NOT_VALID;
import static com.ssi.ms.constant.ErrorMessageConstant.PAGE_NUMBER_NOT_VALID;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssi.ms.fraudreview.dto.lookup.response.LookupItemResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LookupResDTO {
	@Min(value = 1, message = PAGE_NUMBER_NOT_VALID)
	@JsonProperty("pageNumber")
	private Integer pageNumber;
	@Min(value = 1, message = ITEM_PER_PAGE_NOT_VALID)
	@JsonProperty("pageSize")
	private Integer pageSize;
	@JsonProperty("totalRows")
	private Long totalRows;
	@JsonProperty("rows")
	private LookupItemResDTO[] lookupRecordItems;
}