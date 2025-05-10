package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VwCcaseCaseloadMapper {

    @Mapping(target = "caseStatus", source = "newCase")
    @Mapping(target = "caseNo", source = "caseNo")
    @Mapping(target = "claimantName", source = "claimantName")
    @Mapping(target = "casePriorityDesc", source = "casePriorityDesc")
    @Mapping(target = "caseAge", source = "caseAge")
    @Mapping(target = "mostRecentRemedy", source = "mostRecentRemedy")
    @Mapping(target = "caseCharacteristics", source = "caseCharacteristics")
    @Mapping(target = "nextFollowupDate", source = "nextFollowupDate")
    VwCcaseCaseloadDTO daoToDto(VwCcaseCaseloadDAO vwCcaseCaseloadDAO);

}
