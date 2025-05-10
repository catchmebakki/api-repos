package com.ssi.ms.configuration.database.mapper;

import com.ssi.ms.configuration.database.dao.ParameterParDAO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO;
import com.ssi.ms.configuration.dto.parameter.ParameterParResDTO;
import com.ssi.ms.platform.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CustomMapper.class})
public interface ParameterParMapper {
    @Mapping(target = "parId", source = "parameterParDAO.parId")
    @Mapping(target = "parLongName", source = "parameterParDAO.parName")
    @Mapping(target = "parShortName", source = "parameterParDAO.parShortName")
    @Mapping(target = "numericValue", source = "parameterParDAO.parNumericValue")
    @Mapping(target = "textValue", source = "parameterParDAO.parAlphaValText")
    @Mapping(target = "dateValue", source = "parameterParDAO.parAlphaValText")
    @Mapping(target = "parCategoryCd", source = "parameterParDAO.parCategoryCdAlvDAO.alvId")
    @Mapping(target = "parCategoryCdValue", source = "parameterParDAO.parCategoryCdAlvDAO.alvLongDescTxt")
    @Mapping(target = "parEffectiveDate", source = "parameterParDAO.parEffectiveDate", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "parExpirationDate", source = "parameterParDAO.parExpirationDate", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "parRemarks", source = "parameterParDAO.parRemarks")
    ParameterParResDTO daoToDto(ParameterParDAO parameterParDAO);

    @Mapping(target = "parId", source = "parameterParDAO.parId")
    @Mapping(target = "name", source = "parameterParDAO.parName")
    @Mapping(target = "parShortName", source = "parameterParDAO.parShortName")
    @Mapping(target = "startDate", source = "parameterParDAO.parEffectiveDate", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "endDate", source = "parameterParDAO.parExpirationDate", qualifiedByName = "sqlDateToLocalDate")
    ConfigParListItemResDTO daoToSummaryDto(ParameterParDAO parameterParDAO);
}
