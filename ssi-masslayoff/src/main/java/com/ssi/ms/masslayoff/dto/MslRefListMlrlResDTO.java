package com.ssi.ms.masslayoff.dto;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
/**
 * @author munirathnam.surepall
 * Data Transfer Object MslRefListMlrlResDTO class for holding the request data needed to clone a Mass Layoff.
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class MslRefListMlrlResDTO {
	    private String uiAccountNo;
	    private String employerName;
	    private String unit;
	    private Long massLayOffNo;
	    @JsonFormat(pattern = "MM/dd/yyyy")
	    private Date massLayOffDate;
	    @JsonFormat(pattern = "MM/dd/yyyy")
	    private Date mslEffectiveDate;
	    @JsonFormat(pattern = "MM/dd/yyyy")
	    private Date recallDate;
	    private String deductibleIncome;
	    private int noOfClaimants;
	    private int noOfConfirmedClaimants;
	    private int noOfPendingClaimants;
	    private int statusCd;
	    private String statusCdValue;
	    private String priorRemarks;
}
