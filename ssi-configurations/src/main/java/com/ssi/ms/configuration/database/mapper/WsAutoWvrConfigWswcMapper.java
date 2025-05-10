package com.ssi.ms.configuration.database.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.ssi.ms.configuration.database.dao.WsAutoWvrConfigWswcDAO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO;
import com.ssi.ms.configuration.dto.wswc.WsAutoWvrConfigWswcResDTO;
import com.ssi.ms.platform.mapper.CustomMapper;

@Component
@Mapper(componentModel = "spring", uses = {CustomMapper.class})
public interface WsAutoWvrConfigWswcMapper {
    @Mapping(target = "wswcId", source = "wswcDAO.wswcId")
    @Mapping(target = "scenario", source = "wswcDAO.wswcScenarioDesc")
    @Mapping(target = "reasonCd", source = "wswcDAO.wswcReasonAlvDAO.alvId")
    @Mapping(target = "reasonsAlcId", source = "wswcDAO.wswcReasonAlvDAO.allowCatAlcDAO.alcId")
    @Mapping(target = "reasonVal", source = "wswcDAO.wswcReasonAlvDAO.alvShortDecTxt")
    @Mapping(target = "fkLofId", source = "wswcDAO.localOfficeLofDAO.lofId")
    @Mapping(target = "businessUnit", source = "wswcDAO.localOfficeLofDAO.lofName")
    @Mapping(target = "autoOverwrite", source = "wswcDAO.wswcSysOverwriteInd")
    @Mapping(target = "startDate", source = "wswcDAO.wswcEffectiveDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "endDate", source = "wswcDAO.wswcExpirationDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "comments", source = "wswcDAO.wswcUsrComments")
    @Mapping(target = "event", source = "wswcDAO.wswcEvent")
    @Mapping(target = "eventSpecifics", source = "wswcDAO.wswcEventSpecifics")
    @Mapping(target = "addlRef", source = "wswcDAO.wswcAddlRef")
    @Mapping(target = "splActions", source = "wswcDAO.wswcSplActions")
    WsAutoWvrConfigWswcResDTO daoToDto(WsAutoWvrConfigWswcDAO wswcDAO);

    @Mapping(target = "wswcId", source = "wswcDAO.wswcId")
    @Mapping(target = "scenario", source = "wswcDAO.wswcScenarioDesc")
    @Mapping(target = "reason", source = "wswcDAO.wswcReasonAlvDAO.alvShortDecTxt")
    @Mapping(target = "autoOverwrite", source = "wswcDAO.wswcSysOverwriteInd")
    @Mapping(target = "startDate", source = "wswcDAO.wswcEffectiveDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "endDate", source = "wswcDAO.wswcExpirationDt", qualifiedByName = "sqlDateToLocalDate")
    ConfigWswcListItemResDTO daoToSummaryDto(WsAutoWvrConfigWswcDAO wswcDAO);
}
