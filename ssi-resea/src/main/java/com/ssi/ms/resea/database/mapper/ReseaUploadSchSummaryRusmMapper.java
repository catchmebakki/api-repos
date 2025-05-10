package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.platform.mapper.CustomMapper;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatisticsResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Component responsible for mapping between ReseaUploadCmSchDtlsRucdDAO and EmployerDTO classes.
 */
@Component
@Mapper(componentModel = "spring", uses = {CustomMapper.class})
public interface ReseaUploadSchSummaryRusmMapper {
    @Mapping(target = "docId", source = "dao.rusmId")
    @Mapping(target = "uploadId", source = "dao.rucsDAO.rucsId")
    @Mapping(target = "caseManager", source = "dao.rucsDAO.stfDAO.staffName")
    @Mapping(target = "officeName", source = "dao.rucsDAO.lofDAO.lofName")
    @Mapping(target = "effectiveDt", source = "dao.rucsDAO.rucsEffectiveDt")
    @Mapping(target = "uploadedBy", source = "dao.userDAO.fullName")
    @Mapping(target = "uploadedOn", source = "dao.rusmStartTs")
    @Mapping(target = "uploadedAt", source = "dao.rusmStartTs", qualifiedByName = "timestampToLocalTime")
    @Mapping(target = "totalNoOfRecords",  source = "dao.rusmNumRecs")
    @Mapping(target = "noOfErrors", source = "dao.rusmNumErrs")
    @Mapping(target = "fileName", source = "dao.rusmFilename")
    @Mapping(target = "uploadStatusCdValue", source = "dao.rusmStatusCdAlv.alvShortDecTxt")
    @Mapping(target = "errorDescription", source = "dao.rusmErrorDesc")
    ReseaUploadStatisticsResDTO daoToUploadStatisticsDto(ReseaUploadSchSummaryRusmDAO dao);
}
