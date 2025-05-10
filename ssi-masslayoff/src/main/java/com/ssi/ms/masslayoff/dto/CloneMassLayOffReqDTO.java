package com.ssi.ms.masslayoff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MRLR_ID_MANDATORY;
/**
 * @author munirathnam.surepall
 * Data Transfer Object CloneMassLayOffReqDTO class for holding the request data needed to clone a Mass Layoff.
 */
@Validated
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CloneMassLayOffReqDTO {

	/*@NotNull(message = MSLNO_NOT_NULL)
	@Min(value = 1L, message = MSLNO_MIN_DIGITS)
	@Max(value = 9_999_999_999L, message = MSLNO_MAX_DIGITS)
	private Long massLayOffNo;*/
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date massLayOffDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date mslEffectiveDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date recallDate;
	private String deductibleIncome;
	@NotNull(message = MRLR_ID_MANDATORY)
	private Long mlrlId;

}
