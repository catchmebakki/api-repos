package com.ssi.ms.resea.database.mapper;


import com.ssi.ms.resea.database.dao.ReseaReturnToWorkRsrwDAO;
import com.ssi.ms.resea.dto.ReturnToWorkReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReseaReturnToWorkMapper {


    @Mapping(target = "rsicDao.rsicId", source = "returnToWorkReqDTO.eventId")
    @Mapping(target = "rsrwEmpName", source = "returnToWorkReqDTO.empName")
    @Mapping(target = "rsrwExactJobTitle", source = "returnToWorkReqDTO.exactJobTitle")
    @Mapping(target = "rsrwPtFtInd", source = "returnToWorkReqDTO.partFullTimeInd")
    @Mapping(target = "rsrwHourlyPayRate", source = "returnToWorkReqDTO.hourlyPayRate")
    @Mapping(target = "rsrwEmpWorkLocSt", source = "returnToWorkReqDTO.empWorkLocState")
    @Mapping(target = "rsrwEmpWorkLocCity", source = "returnToWorkReqDTO.empWorkLocCity")
    @Mapping(target = "rsrwWorkModeCdAlv.alvId", source = "returnToWorkReqDTO.workMode")
    @Mapping(target = "rsrwStaffNotes", source = "returnToWorkReqDTO.staffNotes")
    @Mapping(target = "rsrwJms890Ind", source = "returnToWorkReqDTO.jms890Ind")
    @Mapping(target = "rsrwJmsRfrlHiredInd", source = "returnToWorkReqDTO.jmsReferralInd")
    @Mapping(target = "rsrwJmsClseGoalsInd", source = "returnToWorkReqDTO.jmsCloseGoalsInd")
    @Mapping(target = "rsrwJmsClseIepInd", source = "returnToWorkReqDTO.jmsCloseIEPInd")
    @Mapping(target = "rsrwJmsCaseNotesInd", source = "returnToWorkReqDTO.jmsCaseNotesInd")
    @Mapping(target = "rsrwJmsResumeOffInd", source = "returnToWorkReqDTO.jmsResumeOffInd")
    @Mapping(target = "rsrwEpChklstUpldInd", source = "returnToWorkReqDTO.epChecklistUploadInd")
    ReseaReturnToWorkRsrwDAO dtoToDao(ReturnToWorkReqDTO returnToWorkReqDTO);
}
