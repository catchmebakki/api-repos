package com.ssi.ms.resea.database.mapper;


import com.ssi.ms.resea.database.dao.ReseaCaseActivityRscaDAO;
import com.ssi.ms.resea.dto.CaseActivitySummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReseaCaseActivityRscaMapper {


    @Mapping(target = "activityId", source = "rscaDAO.rscaId")
    @Mapping(target = "activityDt", source = "rscaDAO.rscaActivtyTs")
    @Mapping(target = "stage", source = "rscaDAO.rscaStageCdALV.alvShortDecTxt")
    @Mapping(target = "activity", source = "rscaDAO.rscaTypeCdALV.alvShortDecTxt")
    @Mapping(target = "user", source = "rscaDAO.rscaCreatedBy")
    @Mapping(target = "description", source = "rscaDAO.rscaDescription")
    @Mapping(target = "detail", source = "rscaDAO.rscaDetails")
    @Mapping(target = "followUpDt", source = "rscaDAO.rscaFollowupDt")
    @Mapping(target = "followUpInd", source = "rscaDAO.rscaFollowupDoneInd")
    CaseActivitySummaryDTO daoToSummaryDTO(ReseaCaseActivityRscaDAO rscaDAO);
}
