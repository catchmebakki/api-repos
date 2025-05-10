package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.dto.DashboardCalResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DashboardCalMapper {
    @Mapping(target = "eventId", source = "rsicDAO.rsicId")
    @Mapping(target = "firstName", source = "rsicDAO.claimDAO.claimantDAO.firstName")
    @Mapping(target = "lastName", source = "rsicDAO.claimDAO.claimantDAO.lastName")
    @Mapping(target = "appointmentDt", source = "rsicDAO.rsicCalEventDt")
    @Mapping(target = "startTime", source = "rsicDAO.rsicCalEventStTime")
    @Mapping(target = "endTime", source = "rsicDAO.rsicCalEventEndTime")
    @Mapping(target = "appointmentType", source = "rsicDAO.rsicMtgModeInd")
    @Mapping(target = "eventType", source = "rsicDAO.rsicCalEventTypeCdAlv.alvId")
    @Mapping(target = "eventTypeDesc", source = "rsicDAO.rsicCalEventTypeCdAlv.alvShortDecTxt")
    @Mapping(target = "usage", source = "rsicDAO.rsicTimeslotUsageCdAlv.alvId")
    @Mapping(target = "usageDesc", source = "rsicDAO.rsicTimeslotUsageCdAlv.alvShortDecTxt")
    @Mapping(target = "submitId", source = "rsicDAO.rsidDAO.rsidId")
    @Mapping(target = "mtgStatusDesc", source = "rsicDAO.rsicMtgStatusCdAlv.alvShortDecTxt")
    DashboardCalResDTO daoToDto(ReseaIntvwerCalRsicDAO rsicDAO);
}
