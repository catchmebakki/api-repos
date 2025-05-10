package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.dto.CcaseCmaNoticesCmnDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CcaseCmaNoticesCmnMapper {

    @Mapping(target = "caseCorrespondenceId", source = "ccaseCmaNoticesCmnDAO.correspondenceCorDAO.corId")
    @Mapping(target = "caseCorrespondenceMailDate", source = "ccaseCmaNoticesCmnDAO.correspondenceCorDAO.corMailedDt")
    @Mapping(target = "caseCorrespondenceTimeStamp", source = "ccaseCmaNoticesCmnDAO.correspondenceCorDAO.corTs")
    @Mapping(target = "caseCorrespondenceName", source = "ccaseCmaNoticesCmnDAO.correspondenceCorDAO.reportsRptDAO.rptName")
    @Mapping(target = "caseCorrespondenceFormNBR", source = "ccaseCmaNoticesCmnDAO.correspondenceCorDAO.reportsRptDAO.rptFormNbr")
    CcaseCmaNoticesCmnDTO daoToDto(CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO);


}
