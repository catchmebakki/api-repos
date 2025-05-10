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
public class UpdateContactActivityDTO extends GeneralActivityDTO{

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

    public String getEntityRepresentedFor() {
        return entityRepresentedFor;
    }

    public void setEntityRepresentedFor(String entityRepresentedFor) {
        this.entityRepresentedFor = entityRepresentedFor;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityUIAcctNBR() {
        return entityUIAcctNBR;
    }

    public void setEntityUIAcctNBR(String entityUIAcctNBR) {
        this.entityUIAcctNBR = entityUIAcctNBR;
    }

    public String getEntityFEIN() {
        return entityFEIN;
    }

    public void setEntityFEIN(String entityFEIN) {
        this.entityFEIN = entityFEIN;
    }

    public String getEntityAddressLine1() {
        return entityAddressLine1;
    }

    public void setEntityAddressLine1(String entityAddressLine1) {
        this.entityAddressLine1 = entityAddressLine1;
    }

    public String getEntityAddressLine2() {
        return entityAddressLine2;
    }

    public void setEntityAddressLine2(String entityAddressLine2) {
        this.entityAddressLine2 = entityAddressLine2;
    }

    public String getEntityCity() {
        return entityCity;
    }

    public void setEntityCity(String entityCity) {
        this.entityCity = entityCity;
    }

    public Long getEntityCountry() {
        return entityCountry;
    }

    public void setEntityCountry(Long entityCountry) {
        this.entityCountry = entityCountry;
    }

    public String getEntityState() {
        return entityState;
    }

    public void setEntityState(String entityState) {
        this.entityState = entityState;
    }

    public String getEntityZip() {
        return entityZip;
    }

    public void setEntityZip(String entityZip) {
        this.entityZip = entityZip;
    }

    public String getEntityTelephone() {
        return entityTelephone;
    }

    public void setEntityTelephone(String entityTelephone) {
        this.entityTelephone = entityTelephone;
    }

    public String getEntityFax() {
        return entityFax;
    }

    public void setEntityFax(String entityFax) {
        this.entityFax = entityFax;
    }

    public Long getEntityPreference() {
        return entityPreference;
    }

    public void setEntityPreference(Long entityPreference) {
        this.entityPreference = entityPreference;
    }

    public String getEntityWebsite() {
        return entityWebsite;
    }

    public void setEntityWebsite(String entityWebsite) {
        this.entityWebsite = entityWebsite;
    }

    public String getEntityRemarks() {
        return entityRemarks;
    }

    public void setEntityRemarks(String entityRemarks) {
        this.entityRemarks = entityRemarks;
    }

    public Long getEntityContactId() {
        return entityContactId;
    }

    public void setEntityContactId(Long entityContactId) {
        this.entityContactId = entityContactId;
    }

    public String getEntityContactPrimaryInd() {
        return entityContactPrimaryInd;
    }

    public void setEntityContactPrimaryInd(String entityContactPrimaryInd) {
        this.entityContactPrimaryInd = entityContactPrimaryInd;
    }

    public String getEntityContactJobTitle() {
        return entityContactJobTitle;
    }

    public void setEntityContactJobTitle(String entityContactJobTitle) {
        this.entityContactJobTitle = entityContactJobTitle;
    }

    public String getEntityContactUseCompanyAddr() {
        return entityContactUseCompanyAddr;
    }

    public void setEntityContactUseCompanyAddr(String entityContactUseCompanyAddr) {
        this.entityContactUseCompanyAddr = entityContactUseCompanyAddr;
    }

    public String getEntityContactFirstName() {
        return entityContactFirstName;
    }

    public void setEntityContactFirstName(String entityContactFirstName) {
        this.entityContactFirstName = entityContactFirstName;
    }

    public String getEntityContactLastName() {
        return entityContactLastName;
    }

    public void setEntityContactLastName(String entityContactLastName) {
        this.entityContactLastName = entityContactLastName;
    }

    public String getEntityContactSalutation() {
        return entityContactSalutation;
    }

    public void setEntityContactSalutation(String entityContactSalutation) {
        this.entityContactSalutation = entityContactSalutation;
    }

    public String getEntityContactAddressLine1() {
        return entityContactAddressLine1;
    }

    public void setEntityContactAddressLine1(String entityContactAddressLine1) {
        this.entityContactAddressLine1 = entityContactAddressLine1;
    }

    public String getEntityContactAddressLine2() {
        return entityContactAddressLine2;
    }

    public void setEntityContactAddressLine2(String entityContactAddressLine2) {
        this.entityContactAddressLine2 = entityContactAddressLine2;
    }

    public String getEntityContactCity() {
        return entityContactCity;
    }

    public void setEntityContactCity(String entityContactCity) {
        this.entityContactCity = entityContactCity;
    }

    public Long getEntityContactCountry() {
        return entityContactCountry;
    }

    public void setEntityContactCountry(Long entityContactCountry) {
        this.entityContactCountry = entityContactCountry;
    }

    public String getEntityContactState() {
        return entityContactState;
    }

    public void setEntityContactState(String entityContactState) {
        this.entityContactState = entityContactState;
    }

    public String getEntityContactZip() {
        return entityContactZip;
    }

    public void setEntityContactZip(String entityContactZip) {
        this.entityContactZip = entityContactZip;
    }

    public String getEntityContactPhoneWork() {
        return entityContactPhoneWork;
    }

    public void setEntityContactPhoneWork(String entityContactPhoneWork) {
        this.entityContactPhoneWork = entityContactPhoneWork;
    }

    public String getEntityContactPhoneWorkExt() {
        return entityContactPhoneWorkExt;
    }

    public void setEntityContactPhoneWorkExt(String entityContactPhoneWorkExt) {
        this.entityContactPhoneWorkExt = entityContactPhoneWorkExt;
    }

    public String getEntityContactPhoneHome() {
        return entityContactPhoneHome;
    }

    public void setEntityContactPhoneHome(String entityContactPhoneHome) {
        this.entityContactPhoneHome = entityContactPhoneHome;
    }

    public String getEntityContactPhoneCell() {
        return entityContactPhoneCell;
    }

    public void setEntityContactPhoneCell(String entityContactPhoneCell) {
        this.entityContactPhoneCell = entityContactPhoneCell;
    }

    public String getEntityContactFax() {
        return entityContactFax;
    }

    public void setEntityContactFax(String entityContactFax) {
        this.entityContactFax = entityContactFax;
    }

    public String getEntityContactEmails() {
        return entityContactEmails;
    }

    public void setEntityContactEmails(String entityContactEmails) {
        this.entityContactEmails = entityContactEmails;
    }

    public Long getEntityContactPreference() {
        return entityContactPreference;
    }

    public void setEntityContactPreference(Long entityContactPreference) {
        this.entityContactPreference = entityContactPreference;
    }
}
