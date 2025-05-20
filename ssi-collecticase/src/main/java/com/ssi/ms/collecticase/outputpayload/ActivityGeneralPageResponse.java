package com.ssi.ms.collecticase.outputpayload;

import lombok.Data;
import java.util.Date;

@Data
public class ActivityGeneralPageResponse {
    //Date
    Date activityDate;
    //Time
    String activityTime;
    //Case Characteristics
    String activityCaseCharacteristics;
    Long activityClaimantRepresentative;
    // Activity Details : <Remedy desc> - <Activity Name>
    String activityHeaderName;
    // Follow-up Date
    Date activityFollowupDate;
    // Follow-up showInd
    Boolean disableFollowupDate;
    // Short Note
    String activityFollowupShortNote;
    // Short Note showInd
    Boolean disableFollowupShNote;
    // Property Lien
    Long propertyLien;
}
