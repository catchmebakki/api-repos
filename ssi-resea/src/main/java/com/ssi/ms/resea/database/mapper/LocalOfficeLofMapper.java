package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.LocalOfficeLofDAO;
import com.ssi.ms.resea.dto.AllowValAlvResDTO;
import com.ssi.ms.resea.dto.OfficeResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LocalOfficeLofMapper {

    @Mapping(target = "officeNum", source = "lofId")
    @Mapping(target = "officeName", source = "lofName")
    OfficeResDTO daoToDto(LocalOfficeLofDAO lofDAO);
}
