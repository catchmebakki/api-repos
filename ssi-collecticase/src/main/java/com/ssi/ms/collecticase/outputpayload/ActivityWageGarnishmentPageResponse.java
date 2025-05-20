package com.ssi.ms.collecticase.outputpayload;

import com.ssi.ms.collecticase.dto.AllowValAlvResDTO;
import com.ssi.ms.collecticase.dto.EmployerContactListDTO;
import com.ssi.ms.collecticase.dto.EmployerListDTO;
import com.ssi.ms.collecticase.dto.OrganizationIndividualDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ActivityWageGarnishmentPageResponse {

    ActivityGeneralPageResponse activityGeneralPageResponse;

    private List<EmployerListDTO> employerList;

    private List<EmployerContactListDTO> employerContactList;

    private List<OrganizationIndividualDTO> employerRepList;

    private Long employerId;

    private Long employerContactId;

    private Long employerRepresentativeCd;

    private BigDecimal wageAmount;

    private String doNotGarnishInd;

    private Long wageFrequency;

    private Long wageNonCompliance;

    private Date wageMotionFiledOn;

    private Date wageEffectiveFrom;

    private Date wageEffectiveUntil;

    private Long courtId;

    private String courtOrderedInd;

    private Date courtOrderedDate;

    //wageAmount - showInd
    Boolean disableWageAmount;

    //doNotGarnishInd - showInd
    Boolean disableDoNotGarnishInd;

    //wageNonCompliance - showInd
    Boolean disableWageNonCompliance;

    //wageMotionFiledOn - showInd
    Boolean disableWageMotionFiledOn;

    //wageEffectiveFrom - showInd
    Boolean disableWageEffectiveFrom;

    //wageEffectiveUntil - showInd
    Boolean disableWageEffectiveUntil;

    //courtOrderedInd - showInd
    Boolean disableCourtOrderedInd;

    //courtOrderedDate - showInd
    Boolean disableCourtOrderedDate;
}
