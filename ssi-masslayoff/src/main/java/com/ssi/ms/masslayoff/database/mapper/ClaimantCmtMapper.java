package com.ssi.ms.masslayoff.database.mapper;

import com.ssi.ms.masslayoff.database.dao.CliamantCmtDAO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantResDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between ClaimantCmtDAO and DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface ClaimantCmtMapper {
	 /**
     * Converts a ClaimantCmtDAO entity to a ClaimantResDTO DTO.
     *
     * @param dao {@link CliamantCmtDAO} The ClaimantCmtDAO entity to convert.
     * @return {@link ClaimantResDTO} The resulting ClaimantResDTO DTO.
     */
    ClaimantResDTO daoToDto(CliamantCmtDAO dao);
}
