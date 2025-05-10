package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.IssueDecisionDecDAO;
import com.ssi.ms.resea.dto.HeaderIssueDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueDecisionDecMapper {
    @Mapping(target = "issueType", source = "decDAO.nmiDAO.parentNmiDAO.nmiShortDescTxt")
    @Mapping(target = "issueSubType", source = "decDAO.nmiDAO.nmiShortDescTxt")
    @Mapping(target = "startDt", source = "decDAO.decBeginDt")
    @Mapping(target = "endDt", source = "decDAO.decEndDt")
    @Mapping(target = "createdOn", source = "decDAO.decCreatedTs")
    @Mapping(target = "decStatusCd", source = "decDAO.decStatusCdAlv.alvId")
    @Mapping(target = "decStatus", source = "decDAO.decStatusCdAlv.alvShortDecTxt")
    @Mapping(target = "decDecisionCd", source = "decDAO.decDecisionCdAlv.alvId")
    @Mapping(target = "decDecision", source = "decDAO.decDecisionCdAlv.alvShortDecTxt")
    @Mapping(target = "rsiiId", source = "decDAO.rsiiDAO.rsiiId")
    HeaderIssueDetailsDTO daoToDto(IssueDecisionDecDAO decDAO);
}
