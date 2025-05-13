package com.ssi.ms.collecticase.inputpayload;

import lombok.Data;

@Data
public class ActivityInputPayload {

    private Long activityRemedyTypeCd;
    private Long activityTypeCd;
    private Long caseId;

}
