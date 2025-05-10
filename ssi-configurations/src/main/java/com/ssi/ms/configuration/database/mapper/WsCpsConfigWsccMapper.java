package com.ssi.ms.configuration.database.mapper;

import com.ssi.ms.configuration.database.dao.WsCpsConfigWsccDAO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO;
import com.ssi.ms.configuration.dto.wscc.WsCpsConfigWsccResDTO;
import com.ssi.ms.platform.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CustomMapper.class})
public interface WsCpsConfigWsccMapper {
    @Mapping(target = "wsccId", source = "wsccDAO.wsccId")
    @Mapping(target = "program", source = "wsccDAO.clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt")
    @Mapping(target = "initialClm", source = "wsccDAO.wsccMinIcWsReq")
    @Mapping(target = "additionalClm", source = "wsccDAO.wsccMinAcWsReq")
    @Mapping(target = "incrementFrequency", source = "wsccDAO.wsccMinWsIncrFreq")
    @Mapping(target = "incrementVal", source = "wsccDAO.wsccMinWsIncrVal")
    @Mapping(target = "startDate", source = "wsccDAO.wsccEffectiveDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "endDate", source = "wsccDAO.wsccExpirationDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "comments", source = "wsccDAO.wsccComments")
    WsCpsConfigWsccResDTO daoToDto(WsCpsConfigWsccDAO wsccDAO);

    @Mapping(target = "wsccId", source = "wsccDAO.wsccId")
    @Mapping(target = "program", source = "wsccDAO.clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt")
    @Mapping(target = "initialClaim", source = "wsccDAO.wsccMinIcWsReq")
    @Mapping(target = "additionalClaim", source = "wsccDAO.wsccMinAcWsReq")
    @Mapping(target = "incrementFrequency", source = "wsccDAO.wsccMinWsIncrFreq")
    @Mapping(target = "incrementVal", source = "wsccDAO.wsccMinWsIncrVal")
    @Mapping(target = "startDate", source = "wsccDAO.wsccEffectiveDt", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "endDate", source = "wsccDAO.wsccExpirationDt", qualifiedByName = "sqlDateToLocalDate")
    ConfigWsccListItemResDTO daoToSummaryDto(WsCpsConfigWsccDAO wsccDAO);
}
