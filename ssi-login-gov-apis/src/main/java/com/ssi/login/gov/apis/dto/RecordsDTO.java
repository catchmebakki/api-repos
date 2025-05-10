package com.ssi.login.gov.apis.dto;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class RecordsDTO {

    private String enrollment_code;     
	private String first_name;
	private String last_name;
	private String street_address;
	private String city;
	private String state;
	private String zip_code;
	private String email_address;
	
	private Date expiration_date;
	private String status;
	//failure_reason
	
	private String proofing_post_office;
	private String proofing_city;
	private String proofing_state;
	
	private String primary_id_type_id;
	private Date transaction_start_date_time;
	private Date transaction_end_date_time;
	private Boolean fraud_suspected;
	private String proofing_confirmation_number;
	private Date time_created;
	private Date time_updated;
	private Date time_email_sent;
	
	private String primary_id_type;
	private String secondary_id_type;
	private String response_message;
	private Date time_email_reminder_sent;
}
