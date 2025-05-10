package com.ssi.ms.configuration.database.mapper;

import com.ssi.ms.configuration.database.dao.AllowCatAlcDAO;
import com.ssi.ms.configuration.dto.alc.AllowCatAlcResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AllowCatAlcMapper {
    @Mapping(target = "alcId", source = "allowCatAlcDAO.alcId")
    @Mapping(target = "alcName", source = "allowCatAlcDAO.alcName")
    @Mapping(target = "alcDescTxt", source = "allowCatAlcDAO.alcDescTxt")
    @Mapping(target = "alcDecipherLabel", source = "allowCatAlcDAO.alcDecipherLabel")
    AllowCatAlcResDTO daoToDto(AllowCatAlcDAO allowCatAlcDAO);
}
