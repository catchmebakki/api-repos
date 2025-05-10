package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.StaffUnavailabilityReqSunrDAO;
import com.ssi.ms.resea.dto.UnavailablityDetailResDTO;
import com.ssi.ms.resea.dto.UnavailablitySummaryResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StaffUnavailabilityReqSunrMapper {
    @Mapping(target = "unavailabilityId", source = "sunrDAO.sunrId")
    @Mapping(target = "reason", source = "sunrDAO.sunrReasonTypeCdAlv.alvShortDecTxt")
    @Mapping(target = "status", source = "sunrDAO.sunrStatusCdAlv.alvShortDecTxt")
    @Mapping(target = "notes", source = "sunrDAO.sunrNote")
    UnavailablitySummaryResDTO summaryDaoToDto(StaffUnavailabilityReqSunrDAO sunrDAO);

    @Mapping(target = "unavailabilityId", source = "sunrDAO.sunrId")
    @Mapping(target = "startDt", source = "sunrDAO.sunrStartDt")
    @Mapping(target = "endDt", source = "sunrDAO.sunrEndDt")
    @Mapping(target = "reason", source = "sunrDAO.sunrReasonTypeCdAlv.alvId")
    @Mapping(target = "referenceNotes", source = "sunrDAO.sunrNote")
    UnavailablityDetailResDTO detailDaoToDto(StaffUnavailabilityReqSunrDAO sunrDAO);
}
