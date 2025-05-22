package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
public class UpdateContactActivityDTO extends GeneralActivityDTO {

    private String entityRepresentedFor;

    private String entityName;

    private String entityUIAcctNBR;

    private String entityFEIN;

    private String entityAddressLine1;

    private String entityAddressLine2;

    private String entityCity;

    private Long entityCountry;

    private String entityState;

    private String entityZip;

    private String entityTelephone;

    private String entityFax;

    private Long entityPreference;

    private String entityWebsite;

    private String entityRemarks;

    private Long entityContactId;

    private String entityContactPrimaryInd;

    private String entityContactJobTitle;

    private String entityContactUseCompanyAddr;

    private String entityContactFirstName;

    private String entityContactLastName;

    private String entityContactSalutation;

    private String entityContactAddressLine1;

    private String entityContactAddressLine2;

    private String entityContactCity;

    private Long entityContactCountry;

    private String entityContactState;

    private String entityContactZip;

    private String entityContactPhoneWork;

    private String entityContactPhoneWorkExt;

    private String entityContactPhoneHome;

    private String entityContactPhoneCell;

    private String entityContactFax;

    private String entityContactEmails;

    private Long entityContactPreference;
    
}
