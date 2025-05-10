package com.ssi.ms.masslayoff.database.mapper;

import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsResDTO;
import com.ssi.ms.platform.mapper.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.dto.MassLayOffReqDTO;
/**
 * @author Praveenraja Paramsivam
 * Component responsible for mapping between MslUploadSummary and MslUploadSummaryMusm DTOs.
 */
@Component
@Mapper(componentModel = "spring", uses = {CustomMapper.class})
public interface MslUploadSummaryMusmMapper {
	 /**
     * Converts a MassLayOffReqDTO to an MslUploadSummaryMusmDAO entity.
     *
     * @param uploadMassLayOffReqDTO {@link MassLayOffReqDTO} The MslUploadSummaryMusmDAO to convert.
     * @return {@link MslUploadSummaryMusmDAO} The resulting MslRefListMlrlDAO entity.
     */
	@Mapping(target = "musmEmpAcNum", source = "uploadMassLayOffReqDTO.uiAccountNo")
	@Mapping(target = "musmEmpAcLoc", source = "uploadMassLayOffReqDTO.unit")
	@Mapping(target = "musmMslDate", source = "uploadMassLayOffReqDTO.massLayOffDate")
	@Mapping(target = "musmMslEffDate", source = "uploadMassLayOffReqDTO.mslEffectiveDate")
	@Mapping(target = "musmMslNum", source = "uploadMassLayOffReqDTO.massLayOffNo")
	@Mapping(target = "musmRecallDate", source = "uploadMassLayOffReqDTO.recallDate")
	@Mapping(target = "musmDiInd", source = "uploadMassLayOffReqDTO.deductibleIncome")
	MslUploadSummaryMusmDAO dtoToDao(MassLayOffReqDTO uploadMassLayOffReqDTO);
	/**
     * Converts an MslUploadSummaryMusmDAO entity to an UploadStatisticsResDTO DTO.
     *
     * @param dao {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO entity to convert.
     * @return {@link UploadStatisticsResDTO} The resulting UploadStatisticsResDTO DTO.
     */
	@Mapping(target = "mslNumber", source = "dao.musmMslNum")
	@Mapping(target = "empAccNbr", source = "dao.musmEmpAcNum")
	@Mapping(target = "empAccLoc", source = "dao.musmEmpAcLoc")
	@Mapping(target = "mslDate", source = "dao.musmMslDate")
	@Mapping(target = "mslEffDate", source = "dao.musmMslEffDate")
	@Mapping(target = "recallDate", source = "dao.musmRecallDate")
	@Mapping(target = "uploadedOn", source = "dao.musmStartTs")
	@Mapping(target = "uploadedAt", source = "dao.musmStartTs", qualifiedByName = "timestampToLocalTime")
	@Mapping(target = "totalNoOfClaimants",  source = "dao.musmNumRecs")
	@Mapping(target = "noOfErrorClaimants", source = "dao.musmNumErrs")
	@Mapping(target = "fileName", source = "dao.musmFilename")
	@Mapping(target = "errorDescription", source = "musmErrorDesc")
	UploadStatisticsResDTO daoToUploadStatisticsDto(MslUploadSummaryMusmDAO dao);
	/**
     * Converts a MslRefListMlrlDAO entity to an MslUploadSummaryMusmDAO entity.
     *
     * @param mlrlDao {@link MslRefListMlrlDAO} The MslRefListMlrlDAO entity to convert.
     * @return {@link MslUploadSummaryMusmDAO} The resulting MslUploadSummaryMusmDAO entity.
     */
	@Mapping(target = "musmEmpAcNum", source = "mlrlDao.mlrlEmpAcNum")
	@Mapping(target = "musmEmpAcLoc", source = "mlrlDao.mlrlEmpAcLoc")
	@Mapping(target = "musmMslDate", source = "mlrlDao.mlrlMslDate")
	@Mapping(target = "musmMslEffDate", source = "mlrlDao.mlrlMslEffDate")
	@Mapping(target = "musmMslNum", source = "mlrlDao.mlrlMslNum")
	@Mapping(target = "musmRecallDate", source = "mlrlDao.mlrlRecallDate")
	@Mapping(target = "musmDiInd", source = "mlrlDao.mlrlDiInd")
	@Mapping(target = "mslRefListMlrlDAO.mlrlId", source = "mlrlDao.mlrlId")
	MslUploadSummaryMusmDAO mlrlDaoToMusmDao(MslRefListMlrlDAO mlrlDao);

}
