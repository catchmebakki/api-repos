package com.ssi.ms.masslayoff.database.mapper;

import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.dto.claimant.MrecClaimantReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between MrecClaimant and MslEntryCmtMlec DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface MrecClaimantToMslEntryCmtMlecMapper {
	/**
     * Converts an MslEntryCmtMlecDAO entity to an MrecClaimantReqDTO DTO.
     *
     * @param dao {@link MrecClaimantReqDTO} The MslEntryCmtMlecDAO entity to convert.
     * @return {@link MslEntryCmtMlecDAO} The resulting MrecClaimantReqDTO DTO.
     */
    @Mapping(target = "mlecSsn", source = "dto.ssn")
    @Mapping(target = "mlecFirstName", source = "dto.firstName")
    @Mapping(target = "mlecLastName", source = "dto.lastName")
    @Mapping(target = "mslRefListMlrl.mlrlId", source = "dto.mlrlId")
    MslEntryCmtMlecDAO dtoToDao(MrecClaimantReqDTO dto);

}
