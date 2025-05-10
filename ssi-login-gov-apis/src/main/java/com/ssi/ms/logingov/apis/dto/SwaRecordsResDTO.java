package com.ssi.ms.logingov.apis.dto;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * RecordsResDTO represents the Data Transfer Object (DTO) for records response.
 * This class encapsulates information related to records.
 * 
 * @Author: munirathnam.surepall
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class SwaRecordsResDTO {
	@JsonProperty("enrollment_code")
	private String enrollmentCode;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("street_address")
	private String streetAddress;
	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;
	@JsonProperty("zip_code")
	private String zipCode;
	@JsonProperty("email_address")
	private String emailAddress;
	@JsonProperty("expiration_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date expirationDate;
	@JsonProperty("status")
	private String status;
	@JsonProperty("failure_reason")
	private String failureReason;
	@JsonProperty("proofing_post_office")
	private String proofingPostOffice;
	@JsonProperty("proofing_city")
	private String proofingCity;
	@JsonProperty("proofing_state")
	private String proofingState;
	@JsonProperty("primary_id_type_id")
	private String primaryIdTypeId;
	@JsonProperty("secondary_id_type")
	private String secondaryIdType;
	@JsonProperty("transaction_start_date_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HHmmss")
	private Date transactionStartDateTime;
	@JsonProperty("transaction_end_date_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HHmmss")
	private Date transactionEndDateTime;
	@JsonProperty("fraud_suspected")
	private String fraudSuspected;
	@JsonProperty("proofing_confirmation_number")
	private String proofingConfirmationNumber;
	@JsonProperty("response_message")
	private String responseMessage;
	@JsonProperty("time_created")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timeCreated;
	@JsonProperty("time_updated")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timeUpdated;
	@JsonProperty("time_email_sent")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timeEmailSent;
	@JsonProperty("time_email_reminder_sent")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timeEmailReminderSent;

}
