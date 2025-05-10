package com.ssi.ms.masslayoff.dto.lookup;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
/**
 * @author Praveenraja Paramsivam
 * Data Transfer Object LookupItemResDTO class for representing a response item in a lookup.
 */
@With
@Builder
@Validated
@AllArgsConstructor
@Getter
public class LookupItemResDTO {
	private Long mlrlId;
	private Long musmId;
	private Long mslId;
	private String empName;
	private String empUiAcctNbr;
	private String empUiAccLoc;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate layoffDate;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate recallDate;
	// EnumConstant.MassLayoffStatusCd statusCd;
	private Integer statusCd;
	private String statusCdValue;
	private Long noOfClaimants;
	private List<Long> listOfMuseId;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate effectiveDate;
	private String deductibleIncomeInd;
	private Integer errorRecordCount;

	public LookupItemResDTO(final Long mlrlId, final Long musmId, final Integer errorCount, final Long mslId,
			final String empUiAcctNbr, final String empUiAccLoc, final Date layoffDate, final Date recallDate,
			final Date effectiveDate, final Integer statusCd, final String statusCdValue,
			final String deductibleIncomeInd, final Long noOfClaimants) {
		this.mlrlId = mlrlId;
		this.musmId = musmId;
		this.mslId = mslId;
		this.empUiAcctNbr = empUiAcctNbr;
		this.empUiAccLoc = empUiAccLoc;
		this.layoffDate = dateToLocalDate.apply(layoffDate);
		this.recallDate = dateToLocalDate.apply(recallDate);
		this.statusCd = statusCd;
		this.statusCdValue = statusCdValue;
		this.noOfClaimants = noOfClaimants;
		this.effectiveDate = dateToLocalDate.apply(effectiveDate);
		this.deductibleIncomeInd = deductibleIncomeInd;
		this.errorRecordCount = errorCount;
	}
}
