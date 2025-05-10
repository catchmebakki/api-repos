package com.ssi.ms.collecticase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class VwCcaseHeaderEntityDTO {

    private Long caseEntityId;

    private String caseEntityRole;

    private Long caseId;

    private String caseEntityName;

    private String caseEntityAddress;

    private String caseEntityPhones;

    private String caseEntityFax;

    private String caseEntityContact;

    private String caseEntityContactTitle;

    private String caseEntityEmails;

    private String caseEntityWebsite;

    private String caseEntityCommPreference;

    private String caseEntityContactPhones;

    private String caseEntityContactFax;
}

