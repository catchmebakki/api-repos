package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.StateStaDAO;
import com.ssi.ms.resea.dto.StateResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StateMapper {

    @Mapping(target = "staCd", source = "stateStaDAO.staCd")
    @Mapping(target = "staFipsCd", source = "stateStaDAO.staFipsCd")
    @Mapping(target = "staDescTxt", source = "stateStaDAO.staDescTxt")
    @Mapping(target = "staSortOrderNbr", source = "stateStaDAO.staSortOrderNbr")
    @Mapping(target = "staProvinceInd", source = "stateStaDAO.staProvinceInd")
    StateResDTO listDaoToDto(StateStaDAO stateStaDAO);


}
