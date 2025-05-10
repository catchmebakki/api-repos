package com.ssi.ms.masslayoff.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.dto.MassLayOffReqDTO;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between MslRefListMlrlDAO and DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface UploadFormToMslRefListMlrlMapper {
	/**
     * Converts a MassLayOffReqDTO to an MslRefListMlrlDAO entity.
     * @param MassLayOffReqDTO {@link MassLayOffReqDTO} The MslRefListMlrlDAO to convert.
     * @return {@link MslRefListMlrlDAO} The resulting MslRefListMlrlDAO entity.
     */
    @Mapping(target = "mlrlDiInd", source = "massLayOffReqDTO.deductibleIncome")
    @Mapping(target = "mlrlEmpAcLoc", source = "massLayOffReqDTO.unit")
    @Mapping(target = "mlrlEmpAcNum", source = "massLayOffReqDTO.uiAccountNo")
    @Mapping(target = "mlrlMslDate", source = "massLayOffReqDTO.massLayOffDate")
    @Mapping(target = "mlrlMslEffDate", source = "massLayOffReqDTO.mslEffectiveDate")
    @Mapping(target = "mlrlRecallDate", source = "massLayOffReqDTO.recallDate")
    MslRefListMlrlDAO dtoToDao(MassLayOffReqDTO massLayOffReqDTO);
}
