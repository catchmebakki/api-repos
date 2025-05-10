package com.ssi.ms.fraudreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BPCStaffDTO {
	private Long userId;
	private String firstName;
	private String lastName;
}