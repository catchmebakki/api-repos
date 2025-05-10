package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.dto.CcaseActivitiesCmaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CcaseActivitiesCmaMapper {


    @Mapping(target = "activityId", source = "cmaId")
    @Mapping(target = "activityNotes", source = "cmaActivityNotes")
    @Mapping(target = "activityAdditionalNotes", source = "cmaActivityNotesAddl")
    @Mapping(target = "activityActivityCd", source = "cmaActivityTypeCd")
    @Mapping(target = "activityRemedyType", source = "cmaRemedyType")
    @Mapping(target = "activityDate", source = "cmaActivityDt")
    @Mapping(target = "activityCreatedBy", source = "cmaCreatedBy")
    CcaseActivitiesCmaDTO daoToDto(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO);

}
