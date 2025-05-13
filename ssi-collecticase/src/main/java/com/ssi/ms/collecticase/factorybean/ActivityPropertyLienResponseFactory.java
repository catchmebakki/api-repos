package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityPropertyLienResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityPropertyLienResponseFactory implements ResponseFactory<ActivityPropertyLienResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityPropertyLienResponse;
    }

    public ActivityPropertyLienResponse createResponse() {
        return new ActivityPropertyLienResponse();
    }

}
