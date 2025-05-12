package com.ssi.ms.collecticase.outputpayload;

import lombok.Data;
import java.util.Date;

@Data
public class ActivityFollowUpShortNoteResponse {
    // Follow-up Date
    Date activityFollowupDate;
    // Follow-up showInd
    boolean disableFollowupDate;
    // Short Note
    String activityFollowupShortNote;
    // Short Note showInd
    boolean disableFollowupShNote;
}
