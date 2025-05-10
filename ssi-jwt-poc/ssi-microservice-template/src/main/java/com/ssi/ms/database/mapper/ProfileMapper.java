package com.ssi.ms.database.mapper;

import com.ssi.ms.database.dao.ProfileDAO;
import com.ssi.ms.dto.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  ProfileMapper {

    ProfileDAO dtoToDao(Profile profile);

    Profile daoToDto(ProfileDAO profile);
}
