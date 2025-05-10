package com.ssi.ms.masslayoff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPACCLOC_NOT_NULL;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPACCNUM_NOT_NULL;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_ACC_NO_NOT_10_DIGIT;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_LOC_NOT_3_DIGIT;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MSLNO_MAX_DIGITS;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MSLNO_MIN_DIGITS;

/**
 * @author munirathnam.surepall
 * Data Transfer Object MassLayOffReqDTO class for holding the request data needed to clone a Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@Setter
@NoArgsConstructor
public class MassLayOffReqDTO {

	@NotNull(message = EMPACCNUM_NOT_NULL)
	@Size(min = 10, max = 10, message = EMPLOYER_ACC_NO_NOT_10_DIGIT)
	private String uiAccountNo;
	@NotNull(message = EMPACCLOC_NOT_NULL)
	@Size(min = 3, max = 3, message = EMPLOYER_LOC_NOT_3_DIGIT)
	private String unit;
	//@NotNull(message = MSLNO_NOT_NULL)
	@Min(value = 1L, message = MSLNO_MIN_DIGITS)
	@Max(value = 9_999_999_999L, message = MSLNO_MAX_DIGITS)
	private Long massLayOffNo;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date massLayOffDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date mslEffectiveDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date recallDate;
	private String deductibleIncome;
}
