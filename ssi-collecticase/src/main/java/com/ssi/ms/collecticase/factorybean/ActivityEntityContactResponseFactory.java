package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityEntityContactResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityEntityContactResponseFactory implements ResponseFactory<ActivityEntityContactResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityEntityContactResponse;
    }

    public ActivityEntityContactResponse createResponse() {
        return new ActivityEntityContactResponse();
    }

}
