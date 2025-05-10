package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityGeneralPageResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityGeneralPageResponseFactory implements ResponseFactory<ActivityGeneralPageResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityGeneralPageResponse;
    }

    public ActivityGeneralPageResponse createResponse() {
        return new ActivityGeneralPageResponse();
    }

}
