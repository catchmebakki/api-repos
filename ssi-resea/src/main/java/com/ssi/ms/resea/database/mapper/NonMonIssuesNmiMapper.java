package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.NonMonIssuesNmiDAO;
import com.ssi.ms.resea.dto.NonMonIssuesNmiListResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NonMonIssuesNmiMapper {

    @Mapping(target = "issueId", source = "nmiDAO.nmiId")
    @Mapping(target = "issueDesc", source = "nmiDAO.nmiShortDescTxt")
    NonMonIssuesNmiListResDTO listDaoToDto(NonMonIssuesNmiDAO nmiDAO);

}
