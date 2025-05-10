package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.ReseaJobReferralRsjrDAO;
import com.ssi.ms.resea.dto.HeaderJobRefDetailsDTO;
import com.ssi.ms.resea.dto.JobReferralDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ReseaJobReferralRsjrMapper {
    @Mapping(target = "employer", source = "rsjrDAO.rsjrEmpName")
    @Mapping(target = "title", source = "rsjrDAO.rsjrExactJobTitle")
    @Mapping(target = "startDt", source = "rsjrDAO.rsjrEffectiveFromDt")
    @Mapping(target = "endDt", source = "rsjrDAO.rsjrEffectiveUntilDt")
    @Mapping(target = "source", source = "rsjrDAO.rsjrSourceCdALV.alvShortDecTxt")
    @Mapping(target = "createdBy", source = "rsjrDAO.rsjrCreatedBy")
    HeaderJobRefDetailsDTO daoToDto(ReseaJobReferralRsjrDAO rsjrDAO);

    @Mapping(target = "empName", source = "rsjrDAO.rsjrEmpName")
    @Mapping(target = "jobTitle", source = "rsjrDAO.rsjrExactJobTitle")
    @Mapping(target = "jobRefId", source = "rsjrDAO.rsjrId")
    @Mapping(target = "empNum", source = "rsjrDAO.fkEmpId")
    JobReferralDTO interviewDaoToDto(ReseaJobReferralRsjrDAO rsjrDAO);
}
