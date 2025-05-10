package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.VwCcaseRemedyDAO;
import com.ssi.ms.collecticase.dto.VwCcaseRemedyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VwCcaseRemedyMapper {

    @Mapping(target = "caseId", source = "cmcId")
    @Mapping(target = "caseRemedyDesc", source = "remedyDesc")
    @Mapping(target = "caseRemedyDate", source = "remedyDt")
    @Mapping(target = "caseRemedyStageDesc", source = "stageDesc")
    @Mapping(target = "caseRemedyStatusDesc", source = "statusDesc")
    @Mapping(target = "caseRemedyEligibleAmt", source = "eligibleAmt")
    @Mapping(target = "caseRemedyCollectedAmt", source = "collectedAmt")
    @Mapping(target = "caseRemedyNextStepDesc", source = "nextStepDesc")

    VwCcaseRemedyDTO daoToDto(VwCcaseRemedyDAO vwCcaseRemedyDAO);
}
