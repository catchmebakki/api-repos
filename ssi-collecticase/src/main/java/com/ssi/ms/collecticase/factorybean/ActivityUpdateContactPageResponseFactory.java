package com.ssi.ms.collecticase.factorybean;

import com.ssi.ms.collecticase.outputpayload.ActivityUpdateContactPageResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityUpdateContactPageResponseFactory implements ResponseFactory<ActivityUpdateContactPageResponse> {

    public ResponseTypes getType() {
        return ResponseTypes.ActivityUpdateContactPageResponse;
    }

    public ActivityUpdateContactPageResponse createResponse() {
        return new ActivityUpdateContactPageResponse();
    }

}
