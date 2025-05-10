package com.ssi.ms.configuration.database.mapper;

import com.ssi.ms.configuration.database.dao.AllowValAlvDAO;
import com.ssi.ms.configuration.dto.alv.AllowValAlvResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AllowValAlvMapper {
    @Mapping(target = "alvId", source = "allowValAlvDAO.alvId")
    @Mapping(target = "fkAlcId", source = "allowValAlvDAO.allowCatAlcDAO.alcId")
    @Mapping(target = "alvActiveInd", source = "allowValAlvDAO.alvActiveInd")
    @Mapping(target = "alvRelatedCd", source = "allowValAlvDAO.alvRelatedCd")
    @Mapping(target = "alvShortDecTxt", source = "allowValAlvDAO.alvShortDecTxt")
    @Mapping(target = "alvSpShortDescTxt", source = "allowValAlvDAO.alvSpShortDescTxt")
    @Mapping(target = "alvLongDescTxt", source = "allowValAlvDAO.alvLongDescTxt")
    @Mapping(target = "alvDecipherCode", source = "allowValAlvDAO.alvDecipherCode")
    @Mapping(target = "alvDisplayOn", source = "allowValAlvDAO.alvDisplayOnAlvDao.alvId")
    @Mapping(target = "alvDisplayOnDesc", source = "allowValAlvDAO.alvDisplayOnAlvDao.alvLongDescTxt")
    @Mapping(target = "alvSortOrderNbr", source = "allowValAlvDAO.alvSortOrderNbr")
    @Mapping(target = "alvComments", source = "allowValAlvDAO.alvChangeLog")
    @Mapping(target = "alvCategoryCd", source = "allowValAlvDAO.allowCatAlcDAO.alcName")
    @Mapping(target = "alvCategoryName", source = "allowValAlvDAO.allowCatAlcDAO.alcDescTxt")
    AllowValAlvResDTO daoToDto(AllowValAlvDAO allowValAlvDAO);

    @Mapping(target = "alvId", source = "allowValAlvDAO.alvId")
    @Mapping(target = "alvActiveInd", source = "allowValAlvDAO.alvActiveInd")
    @Mapping(target = "alvShortDecTxt", source = "allowValAlvDAO.alvShortDecTxt")
    @Mapping(target = "alvLongDescTxt", source = "allowValAlvDAO.alvLongDescTxt")
    @Mapping(target = "alvDecipherCode", source = "allowValAlvDAO.alvDecipherCode")
    @Mapping(target = "alvDisplayOnDesc", source = "allowValAlvDAO.alvDisplayOnAlvDao.alvLongDescTxt")
    @Mapping(target = "alvSortOrderNbr", source = "allowValAlvDAO.alvSortOrderNbr")
    @Mapping(target = "fkAlcId", ignore = true)
    @Mapping(target = "alvRelatedCd", ignore = true)
    @Mapping(target = "alvSpShortDescTxt", ignore = true)
    @Mapping(target = "alvDisplayOn", ignore = true)
    @Mapping(target = "alvComments", ignore = true)
    @Mapping(target = "alvCategoryCd", ignore = true)
    @Mapping(target = "alvCategoryName", ignore = true)
    AllowValAlvResDTO daoToSummaryDto(AllowValAlvDAO allowValAlvDAO);

    @Mapping(target = "alvId", source = "allowValAlvDAO.alvId")
    @Mapping(target = "alvShortDecTxt", source = "allowValAlvDAO.alvShortDecTxt")
    @Mapping(target = "alvLongDescTxt", source = "allowValAlvDAO.alvLongDescTxt")
    @Mapping(target = "fkAlcId", ignore = true)
    @Mapping(target = "alvActiveInd", ignore = true)
    @Mapping(target = "alvRelatedCd", ignore = true)
    @Mapping(target = "alvSpShortDescTxt", ignore = true)
    @Mapping(target = "alvDecipherCode", ignore = true)
    @Mapping(target = "alvDisplayOn", ignore = true)
    @Mapping(target = "alvDisplayOnDesc", ignore = true)
    @Mapping(target = "alvSortOrderNbr", ignore = true)
    @Mapping(target = "alvComments", ignore = true)
    @Mapping(target = "alvCategoryCd", ignore = true)
    @Mapping(target = "alvCategoryName", ignore = true)
    AllowValAlvResDTO daoToDescDto(AllowValAlvDAO allowValAlvDAO);

    @Mapping(target = "alvId", source = "allowValAlvDAO.alvId")
    @Mapping(target = "alvShortDecTxt", source = "allowValAlvDAO.alvShortDecTxt")
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
    AllowValAlvResDTO daoToShortDescDto(AllowValAlvDAO allowValAlvDAO);
}
