package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityFollowUpShortNoteResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityFollowUpShortNoteResponseFactory implements ResponseFactory<ActivityFollowUpShortNoteResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityFollowUpShortNoteResponse;
    }

    public ActivityFollowUpShortNoteResponse createResponse() {
        return new ActivityFollowUpShortNoteResponse();
    }

}
