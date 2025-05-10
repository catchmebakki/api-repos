package com.ssi.ms.masslayoff.database.mapper;

import com.ssi.ms.masslayoff.database.dao.MslUploadErrorMuseDAO;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsItemResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between MslUploadError and MslUploadErrorMuse DTOs.
 */
@Component
@Mapper(componentModel = "spring")
public interface MslUploadErrorMuseMapper {
	/**
     * Converts an MslUploadErrorMuseDAO entity to an UploadStatisticsItemResDTO DTO.
     *
     * @param dao {@link MslUploadErrorMuseDAO} The MslUploadErrorMuseDAO entity to convert.
     * @return {@link UploadStatisticsItemResDTO} The resulting UploadStatisticsItemResDTO DTO.
     */
    @Mapping(target = "ssn", source = "dao.mslUploadStagingMust.mustSsn")
    @Mapping(target = "firstName", source = "dao.mslUploadStagingMust.mustFirstName")
    @Mapping(target = "lastName", source = "dao.mslUploadStagingMust.mustLastName")
    @Mapping(target = "errorMessage", source = "dao.museErrDesc")
    @Mapping(target = "mslNumber", source = "dao.mslUploadStagingMust.mustMslNum")
    @Mapping(target = "empAccNbr", source = "dao.mslUploadStagingMust.mustEmpAcNum")
    @Mapping(target = "empAccLoc", source = "dao.mslUploadStagingMust.mustEmpAcLoc")
    @Mapping(target = "mslDate", source = "dao.mslUploadStagingMust.mustMslDate")
    @Mapping(target = "mslEffDate", source = "dao.mslUploadStagingMust.mustMslEffDate")
    @Mapping(target = "recallDate", source = "dao.mslUploadStagingMust.mustRecallDate")
    UploadStatisticsItemResDTO daoToDto(MslUploadErrorMuseDAO dao);
}
