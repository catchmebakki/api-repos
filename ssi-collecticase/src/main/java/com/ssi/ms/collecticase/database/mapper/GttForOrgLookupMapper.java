package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.dto.GTTForOrgLookupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface GttForOrgLookupMapper {

    @Mapping(target = "entityId", source = "entityId")
    @Mapping(target = "entityName", source = "entityName")
    @Mapping(target = "entityDBAName", source = "entityDBAName")
    @Mapping(target = "entityUIAccNbr", source = "entityUIAccNbr")
    @Mapping(target = "entityOrigin", source = "entityOrigin")
    @Mapping(target = "entitySource", source = "entitySource")
    @Mapping(target = "entityFEINNbr", source = "entityFEINNbr")
    @Mapping(target = "entityType", source = "entityType")
    @Mapping(target = "entityStatus", source = "entityStatus")
    GTTForOrgLookupDTO daoToDto(GTTForOrgLookupDAO gttForOrgLookupDAO);

}
