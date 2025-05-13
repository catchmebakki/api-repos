package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityPaymentPlanPageResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityPaymentPlanPageResponseFactory implements ResponseFactory<ActivityPaymentPlanPageResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityPaymentPlanPageResponse;
    }

    public ActivityPaymentPlanPageResponse createResponse() {
        return new ActivityPaymentPlanPageResponse();
    }

}
