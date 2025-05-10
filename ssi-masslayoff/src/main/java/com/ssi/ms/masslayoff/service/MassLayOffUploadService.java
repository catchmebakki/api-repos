package com.ssi.ms.masslayoff.service;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MUSM_ID_NOT_FOUND;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.ssi.ms.common.service.UserService;
import com.ssi.ms.constant.AlvCodeConstantParent;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsSummaryListResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssi.ms.common.service.ParameterParService;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.constant.ErrorMessageConstant;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.database.mapper.MslUploadErrorMuseMapper;
import com.ssi.ms.masslayoff.database.mapper.MslUploadSummaryMusmMapper;
import com.ssi.ms.masslayoff.database.repository.msl.MslUploadErrorMuseRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslUploadSummaryMusmRepository;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsReqDTO;
import com.ssi.ms.masslayoff.dto.uploadstatistics.UploadStatisticsResDTO;
import com.ssi.ms.masslayoff.fileupload.AsyncFileExecutorService;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.exception.custom.NotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author munirathnam.surepalli
 * MassLayOffUploadService provides services to upload form and upload file functionality happened.
 */
@Service
@Slf4j
public class MassLayOffUploadService {
	@Autowired
	private MslUploadSummaryMusmRepository mslUploadSummaryMusmRepository;
	@Autowired
	private MslUploadSummaryMusmMapper mslUploadSummaryMusmMapper;
	@Autowired
	private AsyncFileExecutorService asyncFileExecutorService;
	@Autowired
	private MslUploadErrorMuseMapper errorMuseMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private MslUploadErrorMuseRepository errorMuseRepository;
	@Autowired
	private ParameterParService parService;
	@Autowired
	private EntityManager entityManager;

	@Value("${application.local.filepath:/opt/msapps/uploads/}")
	private String filePath;

	/**
	 * This method will save Mass LayOff summary details.
	 * @param musmDao {@link List<MslUploadSummaryMusmDAO>} The list of MslUploadSummaryMusmDAO instances to generate the summary from.
	 * @param request {@link Long} The HttpServletRequest associated with the request.
	 * @return {@link UploadStatisticsSummaryListResDTO} The UploadStatisticsSummaryListResDTO generated from the provided list and musmId.
	 */
	private final BiFunction<List<MslUploadSummaryMusmDAO>, Long, UploadStatisticsSummaryListResDTO> summaryPagination = (musmDAOs, musmId) -> {
		UploadStatisticsSummaryListResDTO summarylist = null;
		if (null != musmDAOs && musmDAOs.size() > 1) {
			final var summaryIdList = musmDAOs.stream()
					.map(MslUploadSummaryMusmDAO::getMusmId)
					.toList();
			summarylist = UploadStatisticsSummaryListResDTO.builder()
					.musmIds(summaryIdList)
					.totalNoOfRecords(summaryIdList.size())
					.currentItem(summaryIdList.indexOf(musmId))
					.build();
		}
		return  summarylist;
	};
	/**
	 * Save an instance of MslUploadSummaryMusmDAO.
	 * @param musmDao {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO instance to be saved.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link MslUploadSummaryMusmDAO} The saved MslUploadSummaryMusmDAO instance.
	 */
	public MslUploadSummaryMusmDAO saveSummaryMusmDao(MslUploadSummaryMusmDAO musmDao, HttpServletRequest request) {
		final var userId = getLoggedInStaffId.apply(request);
		musmDao.setMusmCreatedBy(userId);
		musmDao.setMusmCreatedUsing(CommonConstant.SYSTEM);
		musmDao.setMusmLastUpdBy(userId);
		musmDao.setMusmLastUpdUsing(CommonConstant.SYSTEM);
		musmDao.setFkUsrId(Long.parseLong(userId));
		musmDao.setMusmStatusCd(AlvMassLayoffEnumConstant.MslSummaryStatusCd.TO_BE_INITIATED.getCode());
		return mslUploadSummaryMusmRepository.save(musmDao);
	}
	/**
	 * Upload a file associated with the provided musmId and save it.
	 *
	 * @param file {@link MultipartFile} The MultipartFile to be uploaded.
	 * @param musmId {@link Long} The ID of the MslUploadSummaryMusmDAO associated with the upload.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated with the upload request.
	 * @return {@link Long} The ID of the uploaded file.
	 */
	public Long uploadForMusmId(MultipartFile file, Long musmId, HttpServletRequest request) {
		MslUploadSummaryMusmDAO existingMsl = null;
		File newFile = null;
		Timestamp dbTimeStamp = null;
		entityManager.clear();
		final Optional<MslUploadSummaryMusmDAO> existingMslOptional = mslUploadSummaryMusmRepository.findById(musmId);
		if (existingMslOptional.isPresent()) {
			try {
				dbTimeStamp = parService.getDBTimeStamp();
				existingMsl = existingMslOptional.get();
				final Long mslNo = existingMsl.getMusmMslNum();
				newFile = new File(filePath + musmId + "_" + mslNo + ".xlsx");
				log.info("File formate :" + newFile);
				final Path path = Paths.get(filePath);
				final Path directory = path.getParent();
				if (directory != null && !Files.exists(directory)) {
					Files.createDirectories(directory);
				}
				newFile.createNewFile();
				file.transferTo(newFile);
				existingMsl.setMusmFilename(file.getOriginalFilename());
				existingMsl.setMusmSysFilename(newFile.getName());
				existingMsl.setMusmStartTs(dbTimeStamp);
				existingMsl.setMusmStatusCd(AlvMassLayoffEnumConstant.MslSummaryStatusCd.FILE_UPLOADED.getCode());
				mslUploadSummaryMusmRepository.save(existingMsl);
				final var userId = getLoggedInStaffId.apply(request);
				asyncFileExecutorService.fileRead(newFile, userId, musmId);
			} catch (IOException e) {
				log.error("Error while storing data :" + e.getMessage(), e);
				existingMsl.setMusmErrorDesc(FileuploadFieldErrorConstant.FILE_UPLOAD_FAILED);
				existingMsl.setMusmStatusCd(AlvMassLayoffEnumConstant.MslSummaryStatusCd.FILE_UPLOAD_FAILED.getCode());
				mslUploadSummaryMusmRepository.save(existingMsl);
				throw new FileProcessingException("Unable to process the file for the musmId:" + musmId,
						ErrorMessageConstant.FILE_UPLOAD_FAILED, e);
			}
		} else {
			log.error("MSL upload Summary not found for the musmId:" + musmId);
			throw new NotFoundException("MSL upload Summary not found for the musmId:" + musmId, MUSM_ID_NOT_FOUND);
		}

		return musmId;
	}

	/**
	 * This method will return upload statistics data for the provided statistics request.
	 * @param reqDTO {@link UploadStatisticsReqDTO} The UploadStatisticsReqDTO containing the criteria for retrieving upload statistics.
	 * @return {@link UploadStatisticsResDTO} The UploadStatisticsResDTO containing the upload statistics information.
	 */
	public UploadStatisticsResDTO getUploadStatistics(UploadStatisticsReqDTO reqDTO) {
		UploadStatisticsResDTO resDto = null;
		final MslUploadSummaryMusmDAO summaryDao = new MslUploadSummaryMusmDAO();
		summaryDao.setMusmId(reqDTO.getMusmId());
		if (null != reqDTO.getNeedSummaryDetails() && reqDTO.getNeedSummaryDetails()) {
			resDto = mslUploadSummaryMusmRepository.findById(reqDTO.getMusmId())
					.map(dao -> mslUploadSummaryMusmMapper.daoToUploadStatisticsDto(dao)
							.withMrlrId(null != dao.getMslRefListMlrlDAO()
									? dao.getMslRefListMlrlDAO().getMlrlId() : null)
							.withUploadedBy(userService.getUserName(dao.getFkUsrId()))
							.withUploadStatusCdValue(null != dao.getMusmStatusCd() ? AlvCodeConstantParent.forAlvCode(
									AlvMassLayoffEnumConstant.MslSummaryStatusCd.class,
									dao.getMusmStatusCd()).name() : null))
					.orElseThrow(() -> new NotFoundException("No record found for MUSM ID" + reqDTO.getMusmId(),
							MUSM_ID_NOT_FOUND))
					.withErrorRecordList(errorMuseRepository.getAllByMslUploadSummaryMusm(summaryDao).stream()
						.map(errorDao -> errorMuseMapper.daoToDto(errorDao)).toList());
			if (null != resDto.getMrlrId()) {
				final var summayListDao = mslUploadSummaryMusmRepository.findAllByMslRefListMlrlDAO(resDto.getMrlrId());
				final var summaryPageDto = summaryPagination.apply(summayListDao, resDto.getMusmId());
				if (null != summaryPageDto) {
					resDto = resDto.withSummaryListResDTO(summaryPageDto);
				}
			}
		}
		return resDto;
	}

}