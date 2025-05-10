package com.ssi.login.gov.apis.dto;

import java.util.List;

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
public class LoginGovResDTO {
	
	private String swa_xid;
	private List<RecordsDTO>  records;
	private String error_message;
	private String failure_reason;

}
