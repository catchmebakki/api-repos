package com.ssi.ms.masslayoff.database.mapper;

import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantListItemResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between ClaimantList and MslEntryCmtMlec DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface ClaimantListToMslEntryCmtMlecMapper {
	 /**
     * Converts a MslEntryCmtMlecDAO entity to a ClaimantListItemResDTO DTO.
     * @param mslEntryCmtMlecDAO {@link MslEntryCmtMlecDAO} The MslEntryCmtMlecDAO entity to convert.
     * @return {@link ClaimantListItemResDTO} The resulting ClaimantListItemResDTO DTO.
     */
    @Mapping(target = "firstName", source = "mslEntryCmtMlecDAO.mlecFirstName")
    @Mapping(target = "lastName", source = "mslEntryCmtMlecDAO.mlecLastName")
    @Mapping(target = "ssnNumber", source = "mslEntryCmtMlecDAO.mlecSsn")
    @Mapping(target = "statusCd", source = "mslEntryCmtMlecDAO.mlecStatusCd")
    @Mapping(target = "sourceCd", source = "mslEntryCmtMlecDAO.mlecSourceCd")
    ClaimantListItemResDTO daoToDto(MslEntryCmtMlecDAO mslEntryCmtMlecDAO);
}
