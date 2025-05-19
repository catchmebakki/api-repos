package com.ssi.ms.collecticase.inputpayload;

import lombok.Data;

@Data
public class CcaseInputPayload {

    private Long activityRemedyTypeCd;
    private Long activityTypeCd;
    private Long activityId;
    private Long caseId;
    private Long employerId;
    private Long entityId;
    private Long staffId;
    private Integer pageNo;
    private Integer pageSize;
    private Boolean pageAscendingEnable;

}
