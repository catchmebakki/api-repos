package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityWageGarnishmentPageResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityWageGarnishmentPageResponseFactory implements ResponseFactory<ActivityWageGarnishmentPageResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityWageGarnishmentPageResponse;
    }

    public ActivityWageGarnishmentPageResponse createResponse() {
        return new ActivityWageGarnishmentPageResponse();
    }

}
