package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderEntityDAO;
import com.ssi.ms.collecticase.dto.VwCcaseHeaderEntityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VwCcaseHeaderEntityMapper {

    @Mapping(target = "caseEntityId", source = "cmeId")
    @Mapping(target = "caseEntityRole", source = "cmeRole")
    @Mapping(target = "caseId", source = "caseId")
    @Mapping(target = "caseEntityName", source = "entityName")
    @Mapping(target = "caseEntityAddress", source = "entityAddress")
    @Mapping(target = "caseEntityPhones", source = "entityPhones")
    @Mapping(target = "caseEntityFax", source = "entityFax")
    @Mapping(target = "caseEntityContact", source = "entityContact")
    @Mapping(target = "caseEntityContactTitle", source = "entityContactTitle")
    @Mapping(target = "caseEntityEmails", source = "entityEmails")
    @Mapping(target = "caseEntityWebsite", source = "entityWebsite")
    @Mapping(target = "caseEntityCommPreference", source = "entityCommPreference")
    @Mapping(target = "caseEntityContactPhones", source = "entityContactPhones")
    @Mapping(target = "caseEntityContactFax", source = "entityContactFax")
    VwCcaseHeaderEntityDTO daoToDto(VwCcaseHeaderEntityDAO vwCcaseHeaderEntityDAO);
}
