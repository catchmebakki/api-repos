package com.ssi.ms.logingov.apis.dto;

import static com.ssi.ms.logingov.apis.constant.ErrorMessageConstant.FETCHED_MAX_CHARACTERS;
import static com.ssi.ms.logingov.apis.constant.ErrorMessageConstant.FETCHED_NOT_NULL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * FetchedDTO represents the data transfer object for fetched data.
 * This class contains attributes and their corresponding getters, setters, and builders.
 * AllArgsConstructor and NoArgsConstructor are provided for convenience in object instantiation.
 * Validated annotation ensures validation of constraints.
 * @Author: munirathnam.surepall
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class SwaRecordFetchedDTO {
	@NotNull(message = FETCHED_NOT_NULL)
	@Size(max = 30, message = FETCHED_MAX_CHARACTERS)
	private String fetched;

}
