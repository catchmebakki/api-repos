package com.ssi.ms.fraudreview.dto.lookup.response;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssi.ms.fraudreview.constant.FraudReviewConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employer {
	private Long cleId;
	private Long empId;
	private String employerName;

	@Getter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_STD_USA)}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_YYYY_MM_DD)}) // Used during deserialization
	private LocalDate employmentBeginDt;

	@Getter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_STD_USA)}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_YYYY_MM_DD)}) // Used during deserialization
	private LocalDate employmentEndDt;
	private double grossAww;
	private double hourlyRate;
	private String occupation;
	private String workLocation;
	private String sepReasonCode;
	private String sepReason;
	private String additionalSepReasonCode;
	private String additionalSepReason;
	private String employmentTypeCode;
	private String employmentType;
	private String employerType;
	private String selfEmploymentTypeCode;
	private String selfEmploymentType;
	private String selfEmpOwnerTypeCode;
	private String selfEmpOwnerType;
	@JsonProperty("hasRetirementPay")
	private String retirementPayInd;
	@JsonProperty("hasReturnedToWork")
	private String returnedToWorkInd;

	@Getter(onMethod_ = {@JsonFormat(pattern = "MM/dd/yyyy")}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_YYYY_MM_DD)}) // Used during deserialization
	private LocalDate rtwDate;
	private String otherStates;
	private String diOrPension;
	private String ownership;
	private String uiAcctNbr;
	private String uiAcctLoc;
	private Long fein;
}