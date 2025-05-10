package com.ssi.ms.masslayoff.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.STATUSCDVALUE_NOT_NULL;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.REMARKS_NOT_NULL;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.REMARKS_MAX_CHARACTERS;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MRLR_ID_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MSLNO_MAX_DIGITS;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MSLNO_MIN_DIGITS;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MSLNO_NOT_NULL;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.STATUSCDVALUE_NOT_VALID;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MassLayoffStatusCd;
import com.ssi.ms.platform.validator.EnumValidator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author munirathnam.surepall
 * Data Transfer Object EditMassLayOffReqDTO class for holding the request data needed to clone a Mass Layoff.
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class EditMassLayOffReqDTO {

	@NotNull(message = MSLNO_NOT_NULL)
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
	@EnumValidator(enumClazz = MassLayoffStatusCd.class, message = STATUSCDVALUE_NOT_VALID)
	@NotNull(message = STATUSCDVALUE_NOT_NULL)
	private String statusCdValue;
	@NotNull(message = REMARKS_NOT_NULL)
	@Size(max = 150, message = REMARKS_MAX_CHARACTERS)
	private String remarks;
	@NotNull(message = MRLR_ID_MANDATORY)
	private Long mlrlId;
}
