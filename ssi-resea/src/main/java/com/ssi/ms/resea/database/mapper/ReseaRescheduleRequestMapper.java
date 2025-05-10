package com.ssi.ms.resea.database.mapper;




import com.ssi.ms.resea.database.dao.ReseaReschDetRsrsDAO;
import com.ssi.ms.resea.dto.ReseaRescheduleSaveReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;



@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReseaRescheduleRequestMapper {

    @Mapping(target = "oldRsicDAO.rsicId", source = "reseaRescheduleSaveReqDTO.oldEventId")
    @Mapping(target = "newRsicDAO.rsicId", source = "reseaRescheduleSaveReqDTO.newEventId")
    @Mapping(target = "rsrsReschReasonCdALV.alvId", source = "reseaRescheduleSaveReqDTO.reasonForRescheduling")
    @Mapping(target = "rsrsStatfNotes", source = "reseaRescheduleSaveReqDTO.staffNotes")
    @Mapping(target = "rsrsEntityName", source = "reseaRescheduleSaveReqDTO.entityName")
    @Mapping(target = "rsrsEntityCity", source = "reseaRescheduleSaveReqDTO.entityCity")
    @Mapping(target = "rsrsEntityState", source = "reseaRescheduleSaveReqDTO.entityState")
    @Mapping(target = "rsrsEntityTeleNum", source = "reseaRescheduleSaveReqDTO.entityTeleNumber")
    @Mapping(target = "rsrsJobTitle", source = "reseaRescheduleSaveReqDTO.jobTitle")
    @Mapping(target = "rsrsPtFtInd", source = "reseaRescheduleSaveReqDTO.partFullTimeInd")
    ReseaReschDetRsrsDAO dtoToDao(ReseaRescheduleSaveReqDTO reseaRescheduleSaveReqDTO);
}
