package com.ssi.ms.masslayoff.database.mapper;


import com.ssi.ms.masslayoff.database.dao.EmployerEmpDAO;
import com.ssi.ms.masslayoff.dto.employer.EmployerDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between EmployerDAO and EmployerDTO classes.
 */
@Component
@Mapper(componentModel = "spring")
public interface EmployerMapper {
	/**
     * Converts an EmployerEmpDAO entity to an EmployerDetailDTO DTO.
     * @param dao {@link EmployerEmpDAO} The EmployerEmpDAO entity to convert.
     * @return {@link EmployerDetailDTO} The resulting EmployerDetailDTO DTO.
     */
    @Mapping(target = "name", source = "dao.empName")
    @Mapping(target = "loc", source = "dao.empUiAcctLoc")
    EmployerDetailDTO daoToDto(EmployerEmpDAO dao);
}
