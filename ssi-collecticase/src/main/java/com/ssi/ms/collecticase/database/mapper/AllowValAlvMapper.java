package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.AllowValAlvDAO;
import com.ssi.ms.collecticase.dto.AllowValAlvResDTO;
import com.ssi.ms.collecticase.dto.AllowValueResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AllowValAlvMapper {

    @Mapping(target = "constId", source = "allowValAlvCommonDAO.alvId")
    @Mapping(target = "constShortDesc", source = "allowValAlvCommonDAO.alvShortDecTxt")
    @Mapping(target = "fkAlcId", ignore = true)
    @Mapping(target = "alvActiveInd", ignore = true)
    @Mapping(target = "alvRelatedCd", ignore = true)
    @Mapping(target = "alvSpShortDescTxt", ignore = true)
    @Mapping(target = "alvLongDescTxt", ignore = true)
    @Mapping(target = "alvDecipherCode", ignore = true)
    @Mapping(target = "alvDisplayOn", ignore = true)
    @Mapping(target = "alvDisplayOnDesc", ignore = true)
    @Mapping(target = "alvSortOrderNbr", ignore = true)
    @Mapping(target = "alvComments", ignore = true)
    @Mapping(target = "alvCategoryCd", ignore = true)
    @Mapping(target = "alvCategoryName", ignore = true)
    AllowValAlvResDTO daoToShortDescDto(AllowValAlvDAO allowValAlvCommonDAO);

    @Mapping(target = "id", source = "allowValAlvCommonDAO.alvId")
    @Mapping(target = "desc", source = "allowValAlvCommonDAO.alvShortDecTxt")
    AllowValueResDTO dropdownDaoToDto(AllowValAlvDAO allowValAlvCommonDAO);
}
