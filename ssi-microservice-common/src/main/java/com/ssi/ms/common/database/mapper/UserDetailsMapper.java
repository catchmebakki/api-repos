package com.ssi.ms.common.database.mapper;


import com.ssi.ms.common.database.dao.UserDAO;
import com.ssi.ms.common.dto.UserDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * @author Praveenraja Paramsivam
 *Component responsible for mapping between UserDAO and DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface UserDetailsMapper {
	/**
     * Converts a UserDetailsDTO to an UserDAO entity.
     *
     * @param UserDetailsDTO {@link UserDetailsDTO} The UserDAO to convert.
     * @return {@link UserDAO} The resulting UserDAO entity.
     */
    @Mapping(target = "userFirstName", source = "userDetailsDTO.firstName")
    @Mapping(target = "userLastName", source = "userDetailsDTO.lastName")
    UserDAO dtoToDao(UserDetailsDTO userDetailsDTO);
    /**
     * Converts a UserDAO entity to a UserDetailsDTO DTO.
     *
     * @param dao {@link UserDAO} The UserDAO entity to convert.
     * @return {@link UserDetailsDTO} The resulting UserDetailsDTO DTO.
     */
    @Mapping(target = "firstName", source = "userDao.userFirstName")
    @Mapping(target = "lastName", source = "userDao.userLastName")
    UserDetailsDTO daoToDto(UserDAO userDao);

}
