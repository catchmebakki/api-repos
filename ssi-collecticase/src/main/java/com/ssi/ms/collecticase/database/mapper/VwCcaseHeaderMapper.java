package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.dto.VwCcaseHeaderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VwCcaseHeaderMapper {

    @Mapping(target = "claimantId", source = "cmtId")
    @Mapping(target = "completeSSN", source = "fullSsn")
    @Mapping(target = "last4SSN", source = "ssnLast4")
    @Mapping(target = "claimantName", source = "cmtName")
    @Mapping(target = "claimantAddress", source = "cmtAddress")
    @Mapping(target = "claimantEmailAddress", source = "cmtEmailAddress")
    @Mapping(target = "claimantPhoneNbrs", source = "cmtPhones")
    @Mapping(target = "claimantCurrFiling", source = "currFiling")
    @Mapping(target = "claimantFelony", source = "felony")
    @Mapping(target = "claimantOpCollected", source = "opCollected")
    @Mapping(target = "claimantLastCollectionDt", source = "lastCollDt")
    @Mapping(target = "claimantAppeal", source = "appeal")
    @Mapping(target = "bankruptcyStatusDt", source = "bktStatusNDt")
    @Mapping(target = "claimantFraudInd", source = "frd")
    @Mapping(target = "claimantNonFraudInd", source = "nonFrd")
    @Mapping(target = "claimantNonFraudEarnInd", source = "nonFrdEarnings")
    @Mapping(target = "claimantOpBalance", source = "opBal")
    @Mapping(target = "claimantCaseNo", source = "caseNo")
    @Mapping(target = "claimantCasePriority", source = "casePriority")
    @Mapping(target = "claimantCasePriorityDesc", source = "casePriorityDesc")
    @Mapping(target = "claimantCaseStatus", source = "caseStatus")
    @Mapping(target = "claimantCaseStatusDesc", source = "caseStatusDesc")
    @Mapping(target = "claimantCaseOpenDt", source = "caseOpenDt")
    @Mapping(target = "claimantCaseOrigOpenDt", source = "caseOrigOpenDt")
    @Mapping(target = "claimantCaseAge", source = "caseAge")
    @Mapping(target = "claimantCaseCharacteristics", source = "caseCharacteristics")
    @Mapping(target = "claimantCaseNextFollowupDate", source = "nextFollowupDate")
    @Mapping(target = "claimantCaseAssignedStaffId", source = "staffId")
    @Mapping(target = "claimantCaseAssignedStaffName", source = "staffName")

    VwCcaseHeaderDTO daoToDto(VwCcaseHeaderDAO vwCcaseHeaderDAO);
}

