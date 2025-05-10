package com.ssi.ms.fraudreview.dto.lookup.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LookupItemResDTO {

	@JsonProperty("id")
	private String lookupId;
	private Long capId;
	private Long clmId;
	private Long cmtId;
	private Long cmtiId;

	//Base Claimant Template Fields
	@Getter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATE_TIME_AM_PM_MARKER)}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATE_TIME_YYYYMMDD_HHMM)}) // Used during deserialization
	private LocalDateTime dateTime;
	private NameDetails claimantName;
	private String emailAddress;
	private String ipAddress;
	private String payVia;
	private String activeClaimant;
	private String existingInvesticase;
	private InvesticaseAssignmentDetails assignmentDetails;
	private String exceptionHijacking;
	private String exceptionIdTheft;
	private String[] exceptionStatus;

	//Claimant Extensions Fields
	private Address address;
	private String phoneNo;
	private String idCard;
	private BankDetails bankDetails;
	private Demographics demographics;
	private String clmtSpideringDetails;
	private ChangeDetails changes;

	//Base Employment Template Fields
	private String activeBenefitYear;

	@Getter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_STD_USA)}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_YYYY_MM_DD)}) // Used during deserialization
	private LocalDate benefitYearStartDt;

	@Getter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_STD_USA)}) // Used during serialization
    @Setter(onMethod_ = {@JsonFormat(pattern = FraudReviewConstants.DATEFORMAT_YYYY_MM_DD)}) // Used during deserialization
	private LocalDate benefitYearEndDt;
	private Employer[] employmentHistory;
}