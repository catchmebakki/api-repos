package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivitySendReSendResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivitySendReSendResponseFactory implements ResponseFactory<ActivitySendReSendResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivitySendReSendResponse;
    }

    public ActivitySendReSendResponse createResponse() {
        return new ActivitySendReSendResponse();
    }

}
