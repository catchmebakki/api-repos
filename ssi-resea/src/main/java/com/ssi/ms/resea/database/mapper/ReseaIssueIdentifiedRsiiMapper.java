package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.ReseaIssueIdentifiedRsiiDAO;
import com.ssi.ms.resea.dto.ReseaIssuesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ReseaIssueIdentifiedRsiiMapper {

    @Mapping(target = "issueSubType", source = "rsiiDAO.nmiDAO.nmiId")
    @Mapping(target = "issueType", source = "rsiiDAO.nmiDAO.parentNmiDAO.nmiId")
    @Mapping(target = "startDt", source = "rsiiDAO.rsiiIssueEffDt")
    @Mapping(target = "endDt", source = "rsiiDAO.rsiiIssueEndDt")
    @Mapping(target = "otherIssueId", source = "rsiiDAO.rsiiId")
    ReseaIssuesDTO daoToDto(ReseaIssueIdentifiedRsiiDAO rsiiDAO);
}
