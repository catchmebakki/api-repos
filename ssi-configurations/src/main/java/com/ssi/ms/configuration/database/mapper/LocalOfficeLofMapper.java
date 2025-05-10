package com.ssi.ms.configuration.database.mapper;

import com.ssi.ms.configuration.database.dao.LocalOfficeLofDAO;
import com.ssi.ms.configuration.dto.lof.BusinessUnitListResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LocalOfficeLofMapper {
    @Mapping(target = "businessUnitCd", source = "localOfficeLofDAO.lofId")
    @Mapping(target = "businessUnitName", source = "localOfficeLofDAO.lofName")
    BusinessUnitListResDTO daoToDto(LocalOfficeLofDAO localOfficeLofDAO);
}
