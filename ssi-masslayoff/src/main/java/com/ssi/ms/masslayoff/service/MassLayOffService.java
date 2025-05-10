package com.ssi.ms.masslayoff.service;

import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MassLayoffStatusCd;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MslClaimantSourceCd;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MslClaimantStatusCd;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant;
import com.ssi.ms.masslayoff.database.dao.EmployerEmpDAO;
import com.ssi.ms.masslayoff.database.dao.MassLayoffMslDAO;
import com.ssi.ms.masslayoff.database.dao.MslEmployeesMleDAO;
import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.database.mapper.MslUploadSummaryMusmMapper;
import com.ssi.ms.masslayoff.database.mapper.UploadFormToMslRefListMlrlMapper;
import com.ssi.ms.masslayoff.database.repository.EmployerEmpRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MassLayoffMslRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslEmployeesMleRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslEntryCmtMlecRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslReferenceListMlrlRepository;
import com.ssi.ms.masslayoff.dto.CloneMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.EditMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MslRefListMlrlResDTO;
import com.ssi.ms.masslayoff.fileupload.MassLayOffValidator;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssi.ms.constant.AlvCodeConstantParent.forAlvCode;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.ALV_MASS_LAYOFF;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.BLANK_SPACE;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.FAR_FUTURE_DATE_YYYY_MM_DD;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.INCOMPLETE;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.MSL_TYPE_LAYOFF;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.INTERNAL_ERROR;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MLRL_ID_NOT_FOUND;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

/**
 ************************************************************************************************
 *                     Modification Log                                                        *
 ************************************************************************************************
 *
 *  	Date            Developer           Defect  Description of Change
 *  	----------      ---------           ------  ---------------------
 *	12/22/2023		Sitaram				SV225644 - UD-231222-Mass Layoff status is not updating in NHUIS to incomplete
 **
 ************************************************************************************************
 */

/**
 * @author munirathnam.surepalli
 * MassLayOffService provides services to add, edit, upload form, upload file,
 * re-upload file, clone and statistics for MassLayOff details.
 */
@Service
@Slf4j
public class MassLayOffService {

	@Autowired
	private UploadFormToMslRefListMlrlMapper uploadFormToMslRefListMlrlMapper;
	@Autowired
	private MslReferenceListMlrlRepository mslReferenceListMlrlRepository;
	@Autowired
	private MassLayoffMslRepository mslRepository;
	@Autowired
	private MslEmployeesMleRepository mleRepository;
	@Autowired
	private MassLayOffValidator massLayOffValidator;
	@Autowired
	private EmployerEmpRepository employerEmpRepository;
	@Autowired
	private MslEntryCmtMlecRepository mslEntryCmtMlecRepository;

	@Autowired
	private MassLayOffUploadService uploadService;
	@Autowired
	private MslUploadSummaryMusmMapper summaryMusmMapper;
	@Autowired
	private SSIExceptionManager ssiExceptionManager;

	/**
	 * This method will add Mass Lay Off details and return Id.
	 * @param massLayOffReqDTO {@link MassLayOffReqDTO} The MassLayOffReqDTO containing the uploaded data.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the upload request.
	 * @return {@link Long} The ID of the added mass layoff record.
	 */
	public Long addMassLayOff(MassLayOffReqDTO massLayOffReqDTO, HttpServletRequest request) {
		final MslRefListMlrlDAO mlrlDao = uploadFormToMslRefListMlrlMapper.dtoToDao(massLayOffReqDTO);
		processCreateDateStatus(mlrlDao, request);
		mlrlDao.setMlrlStatusCd(AlvMassLayoffEnumConstant.MassLayoffStatusCd.PENDING.getCode());
		final HashMap<String, List<String>> errorMap = massLayOffValidator.validateMassLayOff(massLayOffReqDTO);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException(FileuploadFieldErrorConstant.ADDMSL_VALIDATION_FAILED, errorMap);
		}
		return mslReferenceListMlrlRepository.save(mlrlDao).getMlrlId();
	}

	/**
	 * Retrieve a mass layoff record based on the provided mlrlId.
	 *
	 * @param mlrlId {@link Long} The mlrlId associated with the mass layoff record to retrieve.
	 * @return {@link MslRefListMlrlResDTO} The MslRefListMlrlResDTO representing the retrieved mass layoff record.
	 */
	public MslRefListMlrlResDTO getMassLayOff(Long mlrlId) {
		final MslRefListMlrlDAO mlrlDaoObj = mslReferenceListMlrlRepository.findById(mlrlId).get();
		final String empUiAcctNbr = mlrlDaoObj.getMlrlEmpAcNum();
		final String empUiAcctLoc = mlrlDaoObj.getMlrlEmpAcLoc();
		final EmployerEmpDAO empObj = employerEmpRepository.findByEmpUiAcctNbrAndEmpUiAcctLoc(empUiAcctNbr, empUiAcctLoc);
		return setmassLayOffResDTO(empObj, mlrlDaoObj, mlrlId);
	}

	/**
	 * This method will update mass layOff details and return the updated Id.
	 * @param editMassLayOffReqDTO {@link EditMassLayOffReqDTO}
	 * @param request {@link HttpServletRequest}
	 * @return {@link Long} Long
	 */
	/**
	 * Update a mass layoff record based on the provided EditMassLayOffReqDTO.
	 *
	 * @param editMassLayOffReqDTO {@link EditMassLayOffReqDTO} The EditMassLayOffReqDTO containing the information to be updated.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link Long} The ID of the updated mass layoff record.
	 */
	@Transactional
	public Long updateMassLayOff(EditMassLayOffReqDTO editMassLayOffReqDTO, HttpServletRequest request) {
		final var mlrlDaoObj = mslReferenceListMlrlRepository.findById(editMassLayOffReqDTO.getMlrlId()).get();
		final var errorMap = massLayOffValidator.validateUpdateMassLayOff(editMassLayOffReqDTO,
				setmassLayOffResDTO(null, mlrlDaoObj, editMassLayOffReqDTO.getMlrlId()));
		if (errorMap.size() > 0) {
			throw new CustomValidationException(FileuploadFieldErrorConstant.EDITMSL_VALIDATION_FAILED, errorMap);
		}
		populateMlrlDaoObj(editMassLayOffReqDTO, request, mlrlDaoObj);
		final Long mlrlId = mslReferenceListMlrlRepository.save(mlrlDaoObj).getMlrlId();
		if (MassLayoffStatusCd.FINAL.name().equals(editMassLayOffReqDTO.getStatusCdValue())) {
			finalizeMassLayOff(mlrlId, request);
		}
		return mlrlId;
	}
	/**
	 * Populate fields of an MslRefListMlrlDAO object based on the provided EditMassLayOffReqDTO and HttpServletRequest.
	 *
	 * @param editMassLayOffReqDTO {@link EditMassLayOffReqDTO} The EditMassLayOffReqDTO containing
	 * the data for populating the MSL reference list object.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @param mlrlDaoObj {@link MslRefListMlrlDAO} The MslRefListMlrlDAO object to be populated.
	 */
	private void populateMlrlDaoObj(EditMassLayOffReqDTO editMassLayOffReqDTO, HttpServletRequest request,
			MslRefListMlrlDAO mlrlDaoObj) {
		Optional.ofNullable(editMassLayOffReqDTO.getDeductibleIncome()).ifPresent(val -> mlrlDaoObj.setMlrlDiInd(val));
		Optional.ofNullable(editMassLayOffReqDTO.getMassLayOffNo()).ifPresent(val -> mlrlDaoObj.setMlrlMslNum(val));
		Optional.ofNullable(editMassLayOffReqDTO.getMassLayOffDate()).ifPresent(val -> mlrlDaoObj.setMlrlMslDate(val));
		Optional.ofNullable(editMassLayOffReqDTO.getMslEffectiveDate())
				.ifPresent(val -> mlrlDaoObj.setMlrlMslEffDate(val));
		Optional.ofNullable(editMassLayOffReqDTO.getRecallDate()).ifPresent(val -> mlrlDaoObj.setMlrlRecallDate(val));
		Optional.ofNullable(editMassLayOffReqDTO.getStatusCdValue())
				.ifPresent(val -> mlrlDaoObj.setMlrlStatusCd(MassLayoffStatusCd.valueOf(val).getCode()));
		processUpdateDateStatus(mlrlDaoObj, request);
		final var priorRemarks = new StringBuffer(
				StringUtils.isBlank(mlrlDaoObj.getMlrlRemarkTxt()) ? "" : mlrlDaoObj.getMlrlRemarkTxt().concat("\n"))
				.append(editMassLayOffReqDTO.getRemarks());
		mlrlDaoObj.setMlrlRemarkTxt(priorRemarks.toString());
	}

	/**
	 * Clone a mass layoff record based on the provided CloneMassLayOffReqDTO.
	 *
	 * @param cloneMassLayOffReqDTO {@link CloneMassLayOffReqDTO} The CloneMassLayOffReqDTO containing the information for the cloning operation.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link Long} The ID of the newly cloned mass layoff record.
	 */
	@Transactional
	public Long cloneMassLayOff(CloneMassLayOffReqDTO cloneMassLayOffReqDTO, HttpServletRequest request) {
		final HashMap<String, List<String>> errorMap = massLayOffValidator.validateCloneMassLayOff(cloneMassLayOffReqDTO);
		if (errorMap.size() > 0) {
			throw new CustomValidationException(FileuploadFieldErrorConstant.CLONEMSL_VALIDATION_FAILED, errorMap);
		}
		final long mlrlId = cloneMassLayOffReqDTO.getMlrlId();
		final var optionalMlrlDao = mslReferenceListMlrlRepository.findById(mlrlId);

		return optionalMlrlDao.map(dbMlrlDao -> {
			final var mlrlCloneDao = updateClonedMlrlDto(cloneMassLayOffReqDTO, request, dbMlrlDao);
			final var perisistedMlrlDao = mslReferenceListMlrlRepository.save(mlrlCloneDao);
			final List<MslEntryCmtMlecDAO> cmtDBList = mslEntryCmtMlecRepository.findAllByMslRefListMlrlDAOMlrlId(mlrlId);
			final List<MslEntryCmtMlecDAO> cmtCopyList = updateClonedCmtDto(perisistedMlrlDao, cmtDBList, request);
			mslEntryCmtMlecRepository.saveAll(cmtCopyList);
			return perisistedMlrlDao.getMlrlId();
		}).orElseThrow(() -> new NotFoundException("Requested MlrlId not Found:", MLRL_ID_NOT_FOUND));
	}
	/**
	 * Update cloned comment DTOs based on the persisted MSL reference list DAO and other data.
	 * @param perisistedMlrlDao {@link MslRefListMlrlDAO} The persisted MslRefListMlrlDAO to use for updating the cloned comment DTOs.
	 * @param cmtDBList {@link List<MslEntryCmtMlecDAO>} The list of MslEntryCmtMlecDAO to be updated.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link List<MslEntryCmtMlecDAO>} The updated list of MslEntryCmtMlecDAO after applying the changes.
	 */
	private List<MslEntryCmtMlecDAO> updateClonedCmtDto(MslRefListMlrlDAO perisistedMlrlDao,
			List<MslEntryCmtMlecDAO> cmtDBList, HttpServletRequest request) {
		final List<MslEntryCmtMlecDAO> cmtCopyList = new ArrayList<>();
		Optional.ofNullable(cmtDBList).ifPresent(cmtList -> {
			cmtList.forEach(cmt -> {
				final MslEntryCmtMlecDAO cmtUpdateDao = new MslEntryCmtMlecDAO();
				cmtUpdateDao.setMlecCreatedBy(getLoggedInStaffId.apply(request));
				cmtUpdateDao.setMlecCreatedUsing(cmt.getMlecCreatedUsing());
				cmtUpdateDao.setMlecFirstName(cmt.getMlecFirstName());
				cmtUpdateDao.setMlecLastName(cmt.getMlecLastName());
				cmtUpdateDao.setMlecLastUpdBy(cmt.getMlecLastUpdBy());
				cmtUpdateDao.setMlecLastUpdUsing(cmt.getMlecLastUpdUsing());
				cmtUpdateDao.setMlecSsn(cmt.getMlecSsn());
				cmtUpdateDao.setMlecStatusCd(MslClaimantStatusCd.PENDING.getCode());
				cmtUpdateDao.setMlecSourceCd(MslClaimantSourceCd.CLONED.getCode());
				cmtUpdateDao.setMslRefListMlrl(perisistedMlrlDao);
				cmtCopyList.add(cmtUpdateDao);
			});
		});
		return cmtCopyList;
	}
	/**
	 * Update a cloned MSL reference list DTO based on the provided data and persisted MSL reference list DAO.
	 * @param cloneMassLayOffReqDTO {@link CloneMassLayOffReqDTO} The CloneMassLayOffReqDTO containing the data for updating
	 * the cloned MSL reference list.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @param dbMlrlDao {@link MslRefListMlrlDAO} The persisted MslRefListMlrlDAO to be updated.
	 * @return {@link MslRefListMlrlDAO} The updated MslRefListMlrlDAO after applying the changes.
	 */
	private MslRefListMlrlDAO updateClonedMlrlDto(CloneMassLayOffReqDTO cloneMassLayOffReqDTO,
			HttpServletRequest request, MslRefListMlrlDAO dbMlrlDao) {
		final var mlrlCloneDao = new MslRefListMlrlDAO();
		//mlrlCloneDao.setMlrlMslNum(cloneMassLayOffReqDTO.getMassLayOffNo());
		mlrlCloneDao.setMlrlMslDate(cloneMassLayOffReqDTO.getMassLayOffDate());
		mlrlCloneDao.setMlrlMslEffDate(cloneMassLayOffReqDTO.getMslEffectiveDate());
		mlrlCloneDao.setMlrlRecallDate(cloneMassLayOffReqDTO.getRecallDate());
		mlrlCloneDao.setMlrlDiInd(cloneMassLayOffReqDTO.getDeductibleIncome());
		mlrlCloneDao.setMlrlEmpAcNum(dbMlrlDao.getMlrlEmpAcNum());
		mlrlCloneDao.setMlrlEmpAcLoc(dbMlrlDao.getMlrlEmpAcLoc());
		mlrlCloneDao.setMlrlStatusCd(MassLayoffStatusCd.PENDING.getCode());
		processCreateDateStatus(mlrlCloneDao, request);
		return mlrlCloneDao;
	}
	/**
	 * Process the create date and status fields of an MslRefListMlrlDAO object based on the provided data and request.
	 * @param mlrlDao {@link MslRefListMlrlDAO} The MslRefListMlrlDAO object to be processed.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link MslRefListMlrlDAO} The processed MslRefListMlrlDAO object after updating create date and status.
	 */
	private MslRefListMlrlDAO processCreateDateStatus(MslRefListMlrlDAO mlrlDao, HttpServletRequest request) {
		final var userId = getLoggedInStaffId.apply(request);
		mlrlDao.setMlrlCreatedBy(userId);
		mlrlDao.setMlrlCreatedUsing(CommonConstant.SYSTEM);
		processUpdateDateStatus(mlrlDao, request);
		return mlrlDao;
	}
	/**
	 * Process the update date and status fields of an MslRefListMlrlDAO object based on the provided data and request.
	 * @param mlrlDao {@link MslRefListMlrlDAO} The MslRefListMlrlDAO object to be processed.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link MslRefListMlrlDAO} The processed MslRefListMlrlDAO object after updating update date and status.
	 */
	private MslRefListMlrlDAO processUpdateDateStatus(MslRefListMlrlDAO mlrlDao, HttpServletRequest request) {
		final var userId = getLoggedInStaffId.apply(request);
		mlrlDao.setMlrlLastUpdBy(userId);
		mlrlDao.setMlrlLastUpdUsing(CommonConstant.SYSTEM);
		return mlrlDao;
	}
	/**
	 * Set the fields of an MslRefListMlrlResDTO based on the provided MslRefListMlrlDAO and other data.
	 * @param mlrlDaoObj {@link MslRefListMlrlDAO} The MslRefListMlrlDAO containing the data to populate the MslRefListMlrlResDTO.
	 * @param mlrlId {@link Long} The ID associated with the MSL reference list entry.
	 * @return {@link MslRefListMlrlResDTO} The populated MslRefListMlrlResDTO.
	 */
	private MslRefListMlrlResDTO setmassLayOffResDTO(EmployerEmpDAO empObj,
			MslRefListMlrlDAO mlrlDaoObj, Long mlrlId) {
		final MslRefListMlrlResDTO.MslRefListMlrlResDTOBuilder mlrlResBuilder =  MslRefListMlrlResDTO.builder()
				.massLayOffNo(mlrlDaoObj.getMlrlMslNum())
				.massLayOffDate(mlrlDaoObj.getMlrlMslDate())
				.mslEffectiveDate(mlrlDaoObj.getMlrlMslEffDate())
				.recallDate(mlrlDaoObj.getMlrlRecallDate())
				.deductibleIncome(mlrlDaoObj.getMlrlDiInd())
				.statusCd(mlrlDaoObj.getMlrlStatusCd())
				.statusCdValue(forAlvCode(MassLayoffStatusCd.class, mlrlDaoObj.getMlrlStatusCd()).name())
				.priorRemarks(mlrlDaoObj.getMlrlRemarkTxt())
				.employerName(null != empObj ? empObj.getEmpName() : null);

		return Optional.ofNullable(mslEntryCmtMlecRepository.getCountByMlrlStatusCdGroupByMlecStatusCd(mlrlId))
				.map(mlrlDataList -> mlrlDataList.stream()
					.filter(mlrlDataArray -> null != mlrlDataArray[0] && null != mlrlDataArray[1])
					.collect(Collectors.toMap(objArray -> ((BigDecimal) objArray[1]).intValue(), objArray ->
							((BigDecimal) objArray[0]).intValue())))
				.map(mapOfStatuCdCount ->
					mlrlResBuilder.noOfPendingClaimants(mapOfStatuCdCount
									.getOrDefault(MslClaimantStatusCd.PENDING.getCode(), 0))
							.noOfConfirmedClaimants(mapOfStatuCdCount
									.getOrDefault(MslClaimantStatusCd.CONFIRMED.getCode(), 0))
							.noOfClaimants(mapOfStatuCdCount.getOrDefault(MslClaimantStatusCd.PENDING.getCode(), 0)
								+ mapOfStatuCdCount.getOrDefault(MslClaimantStatusCd.CONFIRMED.getCode(), 0)))
				.orElse(mlrlResBuilder)
				.build();
	}

	/**
	 * This method will upload Mass LayOff details file and return Id.
	 * @param file {@link MultipartFile} he file to be upload.
	 * @param mlrlId {@link Long} The ID associated with the MSL reference list entry.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link Long} Id associated value to be return.
	 */
	public Long uploadforMlrl(MultipartFile file, Long mlrlId, HttpServletRequest request) {
		final var mlrlDaoLocal = mslReferenceListMlrlRepository.findById(mlrlId)
				.orElseThrow(() -> new NotFoundException("Requested mlrl_id not found :" + mlrlId, MLRL_ID_NOT_FOUND));

		final var errorList = massLayOffValidator.validateDirectUpdateMassLayOff(mlrlDaoLocal);
		if (!errorList.isEmpty()) {
			throw new CustomValidationException("Msl Uplaod validation failed", errorList);
		}

		return Optional.of(mlrlDaoLocal).map(mlrlDao -> summaryMusmMapper.mlrlDaoToMusmDao(mlrlDao))
				.map(musmDao -> uploadService.saveSummaryMusmDao(musmDao, request))
				.map(musmDao -> uploadService.uploadForMusmId(file, musmDao.getMusmId(), request))
				.orElseThrow(() -> ssiExceptionManager.onError(log, HttpStatus.INTERNAL_SERVER_ERROR,
						"Error while reading the MLRL and creating MUSM", INTERNAL_ERROR, null));
	}

	/**
	 * @author munirathnam.surepalli
	 * This method will upload form details of Mass LayOff.
	 * @param uploadMassLayOffReqDTO {@link MassLayOffReqDTO} The CloneMassLayOffReqDTO containing the data for update form.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO containing the data to populate the uploadMassLayOffReqDTO.
	 */
	public MslUploadSummaryMusmDAO uploadForm(MassLayOffReqDTO uploadMassLayOffReqDTO, HttpServletRequest request) {
		final HashMap<String, List<String>> errorMap = massLayOffValidator.validateMassLayOff(uploadMassLayOffReqDTO);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException("Upload form Validation failed.", errorMap);
		}
		return Optional.ofNullable(summaryMusmMapper.dtoToDao(uploadMassLayOffReqDTO))
				.map(musmDao -> uploadService.saveSummaryMusmDao(musmDao, request))
				.orElseThrow(() -> ssiExceptionManager.onError(log, HttpStatus.INTERNAL_SERVER_ERROR,
						"Error while creating Msl Summary ", INTERNAL_ERROR, null));
	}

	@Transactional
	public void finalizeMassLayOff(Long mlrlId, HttpServletRequest request) {
		final var mlrlDaoObj = mslReferenceListMlrlRepository.findById(mlrlId).get();
		final MassLayoffMslDAO massLayoffDAO = new MassLayoffMslDAO();
		final MassLayoffMslDAO mslDao = transferMslData(mlrlDaoObj, request);
		transferMleData(mlrlDaoObj, mslDao.getMslId(), request);
		//callAJI610(mslDao);
	}

	@Transactional
	private MassLayoffMslDAO transferMslData(MslRefListMlrlDAO mlrlDaoObj, HttpServletRequest request) {
		final MassLayoffMslDAO massLayoffDAO = new MassLayoffMslDAO();
		initializeMsl(massLayoffDAO);
		Optional.ofNullable(mlrlDaoObj.getMlrlMslNum()).ifPresent(massLayoffDAO::setMslNbr);
		Optional.ofNullable(mlrlDaoObj.getMlrlMslDate()).ifPresent(massLayoffDAO::setMslSepDt);
		if (mlrlDaoObj.getMlrlEmpAcNum() != null && mlrlDaoObj.getMlrlEmpAcLoc() != null) {
			massLayoffDAO.setFkEmpId(employerEmpRepository
					.findByEmpUiAcctNbrAndEmpUiAcctLoc(mlrlDaoObj.getMlrlEmpAcNum(), mlrlDaoObj.getMlrlEmpAcLoc())
					.getEmpId());
		}
		Optional.ofNullable(mlrlDaoObj.getMlrlMslEffDate()).ifPresent(massLayoffDAO::setMslEffStartDt);
		Optional.ofNullable(mlrlDaoObj.getMlrlRecallDate()).ifPresent(massLayoffDAO::setMslRecallDt);
		Optional.ofNullable(mlrlDaoObj.getMlrlRemarkTxt()).ifPresent(massLayoffDAO::setMslCommentsTxt);
		massLayoffDAO.setMslReportedDt(localDateToDate.apply(LocalDate.parse(FAR_FUTURE_DATE_YYYY_MM_DD)));
		Optional.ofNullable(mlrlDaoObj.getMlrlLastUpdTs()).ifPresent(massLayoffDAO::setMslLastUpdTs);
		Optional.ofNullable(mlrlDaoObj.getMlrlDiInd()).ifPresent(massLayoffDAO::setMslSeveranceInd);
		return mslRepository.save(massLayoffDAO);
	}

	private void initializeMsl(MassLayoffMslDAO massLayoffDAO) {
		// Initializing mandatory fields
		massLayoffDAO.setMslFipsCd(0L);
		massLayoffDAO.setMslCategoryCd(ALV_MASS_LAYOFF);
		massLayoffDAO.setFkNmiId(MSL_TYPE_LAYOFF);
		massLayoffDAO.setMslMcDescTxt(BLANK_SPACE);	//SV225644
		massLayoffDAO.setMslStatusInd(INCOMPLETE);	//SV225644
	}

	/*@Transactional
	void callAJI610(MassLayoffMslDAO mslDao) {
		Map<String, Object> outParameter =  mslRepository.callAJI610(mslDao.getFkEmpId(),
                null, mslDao.getFkNmiId(), null, null, null,
				new Date(Calendar.getInstance().getTime().getTime()), MSL_TYPE_OPEN,
				null, null, new Date(mslDao.getMslSepDt().getTime()), null,
				null, MSL_TYPE_MSL, mslDao.getMslId(), MSL_TYPE_MULTICLAIMANT, null, null, null,
				null, null, null, null, null, null, null,
				new Timestamp(Calendar.getInstance().getTime().getTime()), null, "SYSTEM"
				);
		Long decId = (Long) outParameter.get("wlp_o610_dec_id");
		Long result = (Long) outParameter.get("wlp_o610_return_cd");
		String errorMessage = (String) outParameter.get("wlp_o610_error_msg");
		if (result != 0) {
			throw new CustomValidationException(errorMessage, null);
		}
	}*/

	@Transactional
	private void transferMleData(MslRefListMlrlDAO mlrlDaoObj, Long mslId, HttpServletRequest request) {
		final List<MslEmployeesMleDAO> mleEmployeesDAO = new ArrayList<>();
		mslEntryCmtMlecRepository.findAllByMslRefListMlrlDAOMlrlId(mlrlDaoObj.getMlrlId()).forEach(
				mlecDAO -> {
					final MslEmployeesMleDAO mleDAO = new MslEmployeesMleDAO();
					Optional.ofNullable(mslId).ifPresent(mleDAO::setFkMslId);
					mleDAO.copyMlecData(mlecDAO);
					mleEmployeesDAO.add(mleDAO);
				}
		);
		if (!mleEmployeesDAO.isEmpty()) {
			mleRepository.saveAll(mleEmployeesDAO);
		}
	}
}