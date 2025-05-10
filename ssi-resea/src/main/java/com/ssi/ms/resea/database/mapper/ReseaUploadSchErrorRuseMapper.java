package com.ssi.ms.resea.database.mapper;

import com.ssi.ms.resea.database.dao.ReseaUploadSchErrorRuseDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatisticsItemResDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatisticsResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Component responsible for mapping between RuseDAO and DTO classes.
 */
@Component
@Mapper(componentModel = "spring")
public interface ReseaUploadSchErrorRuseMapper {
    @Mapping(target = "dayOfWeek", source = "dao.russDAO.russDayOfWeek")
    @Mapping(target = "time", source = "dao.russDAO.russApptTimeframe")
    @Mapping(target = "allowOnline", source = "dao.russDAO.russAllowOnsiteInd")
    @Mapping(target = "allowRemote", source = "dao.russDAO.russAllowRemoteInd")
    @Mapping(target = "errorMessage", source = "dao.ruseErrDesc")
    ReseaUploadStatisticsItemResDTO daoToErrorDto(ReseaUploadSchErrorRuseDAO dao);
}
