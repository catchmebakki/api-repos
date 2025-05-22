package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.dto.GttForCaselookupDTO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface GttForCaselookupMapper {

    @Mapping(target = "assignedTo", source = "assignedTo")
    @Mapping(target = "caseNum", source = "caseNum")
    @Mapping(target = "caseStatusDesc", source = "caseStatusDesc")
    @Mapping(target = "cmtName", source = "cmtName")
    @Mapping(target = "cmtSsn", source = "cmtSsn")
    @Mapping(target = "lastRemedy", source = "lastRemedy")
    @Mapping(target = "nextFollowUp", source = "nextFollowUp")
    @Mapping(target = "opType", source = "opType")
    GttForCaselookupDTO daoToDto(GttForCaselookupDAO gttForCaselookupDAO);

}
