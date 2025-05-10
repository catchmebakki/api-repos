package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.database.repository.UserRepository;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwSchRsisDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.ReseaRmtMtgInfoRsrmDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchDtlsRucdDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchStagingRussDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.database.dao.TransactionTrxDAO;
import com.ssi.ms.resea.database.mapper.ReseaUploadSchErrorRuseMapper;
import com.ssi.ms.resea.database.mapper.ReseaUploadSchSummaryRusmMapper;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwSchRsisRepository;
import com.ssi.ms.resea.database.repository.ReseaRmtMtgInfoRsrmRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadCmSchDtlsRucdRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadCmSchRucsRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadErrorRuseRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadSchSummaryRusmRepository;
import com.ssi.ms.resea.database.repository.StaffStfRepository;
import com.ssi.ms.resea.database.repository.TransactionTrxRepository;
import com.ssi.ms.resea.dto.upload.ReseaUploadFileReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadFormReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadRecordsResDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadStatisticsResDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadSummaryReqDTO;
import com.ssi.ms.resea.dto.upload.ReseaUploadSummaryResDTO;
import com.ssi.ms.resea.dto.upload.UploadStatisticsSummaryListResDTO;
import com.ssi.ms.resea.fileupload.AsyncFileExecutorService;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaUploadValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInRoleId;
import static com.ssi.ms.platform.util.HttpUtil.getLoggedInStaffId;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.FILE_UPLOAD_FAILED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INTERNAL_ERROR;
import static com.ssi.ms.resea.constant.ReseaConstants.DAIL_BY_LOCATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_FIRST_SUB_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_INITIAL_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_SEC_SUB_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_MEETING_ID;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_OTHER_REF;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_PASSCODE;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_PHONE_NBR;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_DO_NOT_SCHEDULE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIS_INTVW_TYPE_CD_INIT_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSRM_MEETING_CATG_CD_RESEA_ONR_ON_ONE;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_1ST_SUB;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_2ND_SUB;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_INITIAL;
import static com.ssi.ms.resea.constant.ReseaConstants.TRX_01_IFK_CD_STF;
import static com.ssi.ms.resea.constant.ReseaConstants.TRX_STATUS_CD_NOT_PROCESSED;
import static com.ssi.ms.resea.constant.ReseaConstants.TRX_TRAN_CD_CREATE_CAL;
import static com.ssi.ms.resea.constant.ReseaConstants.ZOOM_LINK_TOPIC;

@Slf4j
@Service
public class ReseaUploadService extends  ReseaBaseService {
    @Autowired
    private ReseaUploadValidator reseaUploadValidator;
    @Autowired
    private ParameterParRepository parRepository;
    @Autowired
    AllowValAlvRepository alvRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffStfRepository stfRepository;
    @Autowired
    private LocalOfficeLofRepository lofRepository;
    @Autowired
    private ReseaUploadCmSchDtlsRucdRepository rucdRepository;
    @Autowired
    private ReseaUploadSchSummaryRusmMapper rusmMapper;
    @Autowired
    private ReseaUploadSchErrorRuseMapper ruseMapper;
    @Autowired
    private ReseaUploadCmSchRucsRepository rucsRepository;
    @Autowired
    private ReseaUploadSchSummaryRusmRepository rusmRepository;
    @Autowired
    private ReseaUploadErrorRuseRepository ruseRepository;
    @Autowired
    private ReseaIntvwSchRsisRepository rsisRepository;
    @Autowired
    private ReseaRmtMtgInfoRsrmRepository rsrmRepository;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AsyncFileExecutorService asyncFileExecutorService;
    @Autowired
    private TransactionTrxRepository trxRepository;
    // We are creating a seprate folder for Schedule Uploads, due to File Create Reads the folder fo
    @Value("${application.local.filepath:/opt/msapps/uploads/}")
    private String filePath;

    public List<ReseaUploadSummaryResDTO> getFileUploadSummary(ReseaUploadSummaryReqDTO uploadSummaryDTO) {
        return rucsRepository.getFileUploadSummary(uploadSummaryDTO.getCaseManagerId(),
                uploadSummaryDTO.getEffectiveStartDt(), uploadSummaryDTO.getEffectiveEndDt(),
                ReseaAlvEnumConstant.RucsStatusCd.UPLOADED_PENDING_FINAL.getCode(),
                List.of(ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode(),
                        ReseaAlvEnumConstant.RucsStatusCd.DISCARDED.getCode()),
                List.of(ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode(),
                        ReseaAlvEnumConstant.RucsStatusCd.DISCARDED.getCode()),
                List.of(ReseaAlvEnumConstant.RusmStatusCd.UPLOAD_FAILED.getCode(),
                        ReseaAlvEnumConstant.RusmStatusCd.PARSING_FAILED.getCode()));
    }

    public ReseaUploadStatisticsResDTO getFileUploadStats(ReseaUploadStatReqDTO reqDTO) {
        ReseaUploadStatisticsResDTO resDTO = null;
        if (null != reqDTO.getNeedSummaryDetails() && reqDTO.getNeedSummaryDetails()) {
            final ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(reqDTO.getUploadId())
                    .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload Id:"
                            + reqDTO.getUploadId(), "uploadId.notFound"));
            resDTO = ((reqDTO.getDocId() == null || reqDTO.getDocId() == 0)
                    ? rusmRepository.findTopByRucsDAOOrderByRusmReqTsDescRusmIdDesc(rucsDAO)
                    : rusmRepository.findById(reqDTO.getDocId()))
                    .map(dao -> rusmMapper.daoToUploadStatisticsDto(dao))
                    .map(dto -> dto
                            .withSummaryListResDTO(summaryPagination.apply(rusmRepository
                                .findAllRusmIdsByRucsDAO(dto.getUploadId()), dto.getDocId()))
                            .withErrorRecordList(ruseRepository.findAllByRusmId(dto.getDocId()).stream()
                                    .map(dao -> ruseMapper.daoToErrorDto(dao))
                                    .toList()))
                    .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload Id:"
                            + reqDTO.getUploadId(), "uploadId.records.notFound"));
        }
        return resDTO;
    }

    private final BiFunction<List<Long>, Long, UploadStatisticsSummaryListResDTO> summaryPagination = (rusmIds, rusmId) -> {
        UploadStatisticsSummaryListResDTO summarylist = null;
        if (null != rusmIds && rusmIds.size() > 1) {
            summarylist = UploadStatisticsSummaryListResDTO.builder()
                    .docIds(rusmIds)
                    .totalNoOfRecords(rusmIds.size())
                    .currentItem(rusmIds.indexOf(rusmId))
                    .build();
        }
        return  summarylist;
    };

    @Transactional
    public Long uploadForm(ReseaUploadFormReqDTO uploadReqDTO, String userId, String roleId) {
    	final Long pendingRucsId = rucsRepository.checkPendingRucsExists(uploadReqDTO.getCaseManagerId(), uploadReqDTO.getOfficeId(),
                List.of(ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode(),
                        ReseaAlvEnumConstant.RucsStatusCd.DISCARDED.getCode()));
        final HashMap<String, List<String>> errorMap = reseaUploadValidator.validateUploadForm(uploadReqDTO,
        		pendingRucsId != null && pendingRucsId > 0,
                parRepository.getCurrentDate(), roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Upload form Validation failed.", errorMap);
        }
        return Optional.of(saveSummaryRucsDao(uploadReqDTO, userId))
                .map(ReseaUploadCmSchRucsDAO::getRucsId)
                .orElseThrow(() -> ssiExceptionManager.onError(log, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error while creating RESEA Upload Summary ", INTERNAL_ERROR, null));
    }

    @Transactional
    private ReseaUploadCmSchRucsDAO saveSummaryRucsDao(ReseaUploadFormReqDTO uploadReqDTO, String userId) {
        final ReseaUploadCmSchRucsDAO rucsDAO = new ReseaUploadCmSchRucsDAO();
        rucsDAO.setStfDAO(stfRepository.findByUserId(uploadReqDTO.getCaseManagerId()));
        rucsDAO.setLofDAO(lofRepository.findById(uploadReqDTO.getOfficeId()).orElse(null));
        rucsDAO.setRucsEffectiveDt(uploadReqDTO.getEffectiveDt());
        rucsDAO.setRucsStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RucsStatusCd.TO_BE_INITIATED.getCode()).get());
        rucsDAO.setRucsCreatedBy(userId);
        rucsDAO.setRucsCreatedUsing("UPLOAD_FORM");
        rucsDAO.setRucsLastUpdBy(userId);
        rucsDAO.setRucsLastUpdUsing("UPLOAD_FORM");
        return rucsRepository.save(rucsDAO);
    }

    @Transactional
    public void uploadForRucsId(MultipartFile file, Long rucsId, Long rusmId, HttpServletRequest request) {
        entityManager.clear();
        final ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(rucsId)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload ID:" + rucsId, "uploadId.notFound"));
        File newFile;
        final String loggedInUserId = getLoggedInStaffId.apply(request);
        final ReseaUploadSchSummaryRusmDAO rusmDAO = rusmRepository.findById(rusmId)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the File Id:" + rusmId, "fileId.notFound"));;
        try {
            newFile = new File(filePath + "RESEA_SCH_UPLD_" + rucsDAO.getRucsId() +"_" + rusmId + ".xlsx");
            log.info("File format :" + newFile);
            final Path path = Paths.get(filePath);
            final Path directory = path.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            if (newFile.createNewFile()) {
                log.info("File transfer initiated to " + newFile);
                file.transferTo(newFile);
                log.info("File transfer complete. File Size: " + file.getSize());
                /*rusmDAO.setRusmSysFilename(newFile.getName());
                rusmDAO.setRusmStatusCdAlv(alvRepo.findById(ReseaAlvEnumConstant.RusmStatusCd.UPLOADED.getCode()).orElse(null));
                rusmDAO.setRusmLastUpdBy(loggedInUserId);
                rusmDAO.setRusmLastUpdUsing("UPLOAD_FILE");
                rusmRepository.save(rusmDAO);
                entityManager.flush();*/
                asyncFileExecutorService.fileRead(file.getOriginalFilename(), newFile, loggedInUserId, rucsId, rusmId);
            } else {
                log.error("Error while storing data : File Could not be create");
                //ReseaUploadSchSummaryRusmDAO rusmDAO = initializeRusm(rucsDAO, file.getOriginalFilename(), loggedInUserId, sysTimestamp);
                rusmDAO.setRusmErrorDesc(FILE_UPLOAD_FAILED);
                rusmDAO.setRusmStatusCdAlv(alvRepo.findById(ReseaAlvEnumConstant.RusmStatusCd.UPLOAD_FAILED.getCode()).orElse(null));
                rusmRepository.save(rusmDAO);
                throw new FileProcessingException("File could not be created for the RUSM_ID:" + rusmDAO.getRusmId(),
                        FILE_UPLOAD_FAILED, null);
            }
        } catch (IOException | IllegalStateException e) {
            log.error("Error while storing data :" + e.getMessage(), e);
            //ReseaUploadSchSummaryRusmDAO rusmDAO = initializeRusm(rucsDAO, file.getOriginalFilename(), loggedInUserId, sysTimestamp);
            rusmDAO.setRusmErrorDesc(FILE_UPLOAD_FAILED);
            rusmDAO.setRusmStatusCdAlv(alvRepo.findById(ReseaAlvEnumConstant.RusmStatusCd.UPLOAD_FAILED.getCode()).orElse(null));
            rusmRepository.save(rusmDAO);
            throw new FileProcessingException("Unable to process the file for the rucsId:" + rucsId,
                    FILE_UPLOAD_FAILED, e);
        }
    }

    @Transactional
    public Long generateFileId(ReseaUploadFileReqDTO uploadReqDTO, String userId, String roleId) {
        final ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(uploadReqDTO.getUploadId())
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload Id:"
                        + uploadReqDTO.getUploadId(), "uploadId.notFound"));
        final HashMap<String, List<String>> errorMap = reseaUploadValidator.validateFileId(rucsDAO,
                parRepository.getCurrentDate(), roleId);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Upload File Validation failed.", errorMap);
        }
        rucsDAO.setRucsStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RucsStatusCd.UPLOAD_IN_PROCESS.getCode()).get());
        rucsDAO.setRucsLastUpdBy(userId);
        rucsDAO.setRucsLastUpdUsing("FILE_ID_API");
        rucsRepository.save(rucsDAO);
        final ReseaUploadSchSummaryRusmDAO rusmDAO = rusmRepository.findTopByRucsDAOOrderByRusmReqTsDescRusmIdDesc(rucsDAO).orElse(null);
        Long rusmId = null;
        if (rusmDAO != null && Objects.equals(rusmDAO.getRusmStatusCdAlv().getAlvId(), ReseaAlvEnumConstant.RusmStatusCd.INITIATED.getCode())) {
            rusmId = rusmDAO.getRusmId();
        } else if (rusmDAO != null) {
            rusmDAO.setRusmEndTs(parRepository.getCurrentTimestamp());
            rusmDAO.setRusmLastUpdBy(userId);
            rusmDAO.setRusmLastUpdUsing("FILE_ID_API");
            rusmRepository.save(rusmDAO);
        }
        return rusmId != null
                ? rusmDAO.getRusmId()
                : Optional.of(initializeRusm(rucsDAO, uploadReqDTO.getFileName(), userId, parRepository.getCurrentTimestamp()))
                    .map(ReseaUploadSchSummaryRusmDAO::getRusmId)
                    .orElseThrow(() -> ssiExceptionManager.onError(log, HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error while generating RESEA Upload File ID ", INTERNAL_ERROR, null));
    }

    @Transactional
    private ReseaUploadSchSummaryRusmDAO initializeRusm(ReseaUploadCmSchRucsDAO rucsDAO, String originalFileName,
                                                        String loggedInUserId, Date sysTimestamp) {
        ReseaUploadSchSummaryRusmDAO rusmDAO = new ReseaUploadSchSummaryRusmDAO();
        rusmDAO.setUserDAO(userRepository.findByUserId(Long.valueOf(loggedInUserId)));
        rusmDAO.setRucsDAO(rucsDAO);
        rusmDAO.setRusmReqTs(sysTimestamp);
        rusmDAO.setRusmCaseManager(rucsDAO.getStfDAO().getStaffName());
        rusmDAO.setRusmLocalOffice(rucsDAO.getLofDAO().getLofName());
        rusmDAO.setRusmFilename(originalFileName);
        rusmDAO.setRusmStartTs(sysTimestamp);
        rusmDAO.setRusmStatusCdAlv(alvRepo.findById(ReseaAlvEnumConstant.RusmStatusCd.INITIATED.getCode()).orElse(null));
        rusmDAO.setRusmCreatedBy(loggedInUserId);
        rusmDAO.setRusmCreatedUsing("FILE_ID_API");
        rusmDAO.setRusmCreatedTs(sysTimestamp);
        rusmDAO.setRusmLastUpdBy(loggedInUserId);
        rusmDAO.setRusmLastUpdUsing("FILE_ID_API");
        rusmDAO.setRusmLastUpdTs(sysTimestamp);
        return rusmRepository.save(rusmDAO);
    }

    @Transactional
    public ReseaUploadCmSchDtlsRucdDAO createMeetingScheduleDetails(ReseaUploadSchStagingRussDAO russDAO,
                                             ReseaUploadSchSummaryRusmDAO rusmDAO, String userId) {
        final ReseaUploadCmSchDtlsRucdDAO rucdDAO = new ReseaUploadCmSchDtlsRucdDAO();
        rucdDAO.setRucsDAO(rusmDAO.getRucsDAO());
        rucdDAO.setRusmDAO(rusmDAO);
        rucdDAO.setRussDAO(russDAO);
        rucdDAO.setRucdDayOfWeek(ReseaUtilFunction.convertDayOfWeek().apply(russDAO.getRussDayOfWeek()));
        final String[] apptTime = russDAO.getRussApptTimeframe().split("-");
        final String startTime = StringUtils.leftPad(apptTime[0], 5, "0");
        final String endTime = StringUtils.leftPad(apptTime[1], 5, "0");
        rucdDAO.setRucdStartTime(startTime);
        rucdDAO.setRucdEndTime(endTime);
        rucdDAO.setRucdAllowOnsiteInd(russDAO.getRussAllowOnsiteInd());
        rucdDAO.setRucdAllowRemoteInd(russDAO.getRussAllowRemoteInd());
        final String[] zoomDetailArray = russDAO.getRussZoomLinkDetails().split("\n");
        final String topic = Arrays.stream(zoomDetailArray).filter(s -> s.trim().toUpperCase().startsWith(ZOOM_LINK_TOPIC.toUpperCase()))
                .findFirst().orElse("").toUpperCase();
        if (StringUtils.isNotBlank(topic)) {
            if (topic.contains(TOPIC_INITIAL)) {
                rucdDAO.setRucdIntvwTypeCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.INITIAL_APPOINTMENT.getCode()).get());
            } else if (topic.contains(TOPIC_1ST_SUB)) {
                rucdDAO.setRucdIntvwTypeCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.FIRST_SUBS_APPOINTMENT.getCode()).get());
            } else if (topic.contains(TOPIC_2ND_SUB)) {
                rucdDAO.setRucdIntvwTypeCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.SECOND_SUBS_APPOINTMENT.getCode()).get());
            }
        } else {
            final long duration = ReseaUtilFunction.durationBetweenStartAndEndTime().apply(startTime, endTime);
            AllowValAlvDAO meetingType = null;
            if (duration == parRepository.findByParShortName(PAR_RESEA_INITIAL_APPT_DURATION).getParNumericValue()) {
                meetingType = alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.INITIAL_APPOINTMENT.getCode()).orElse(null);
            } else if (duration == parRepository.findByParShortName(PAR_RESEA_FIRST_SUB_APPT_DURATION).getParNumericValue()) {
                meetingType = alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.FIRST_SUBS_APPOINTMENT.getCode()).orElse(null);
            } else if (duration == parRepository.findByParShortName(PAR_RESEA_SEC_SUB_APPT_DURATION).getParNumericValue()) {
                meetingType = alvRepository.findById(ReseaAlvEnumConstant.RsisIntvwTypeCd.SECOND_SUBS_APPOINTMENT.getCode()).orElse(null);
            }
            rucdDAO.setRucdIntvwTypeCdAlv(meetingType);
        }
        final String zoomLink = Arrays.stream(zoomDetailArray).filter(s -> s.trim().toUpperCase().contains("ZOOM.US/J/"))
                .findFirst().orElse("");
        final String meetingId = Arrays.stream(zoomDetailArray).filter(s -> PATTERN_MEETING_ID.matcher(s.toUpperCase()).find())
                .findFirst().orElse("");
        final String passcode = Arrays.stream(zoomDetailArray).filter(s -> PATTERN_PASSCODE.matcher(s.toUpperCase()).find())
                .findFirst().orElse("");
        final Integer index = IntStream.range(0, zoomDetailArray.length)
                .filter(i -> zoomDetailArray[i].toUpperCase().contains(DAIL_BY_LOCATION))
                .boxed()
                .findFirst().orElse(-1);
        final List<String> phoneNbrs = Arrays.stream(zoomDetailArray, index, zoomDetailArray.length)
                .filter(s -> PATTERN_PHONE_NBR.matcher(s).find()).toList();
        rucdDAO.setRucdMeetingUrl(zoomLink.substring(zoomLink.toUpperCase().indexOf("HTTPS")).trim());
        rucdDAO.setRucdMeetingId(meetingId.substring(meetingId.indexOf(":") + 1).trim());
        rucdDAO.setRucdMeetingPwd(passcode.substring(passcode.indexOf(":") + 1).trim());
        rucdDAO.setRucdMeetingTele1(phoneNbrs.get(0).substring(phoneNbrs.get(0).indexOf("+")));
        if (phoneNbrs.size() > 1) {
            rucdDAO.setRucdMeetingTele2(phoneNbrs.get(1).substring(phoneNbrs.get(1).indexOf("+")));
        }
        rucdDAO.setRucdMeetingOtherRef(Arrays.stream(zoomDetailArray).filter(s -> PATTERN_OTHER_REF.matcher(s.toUpperCase()).find())
                .findFirst().orElse(null));
        rucdDAO.setRucdCreatedBy(userId);
        rucdDAO.setRucdCreatedUsing("UPLOAD_API");
        rucdDAO.setRucdLastUpdBy(userId);
        rucdDAO.setRucdLastUpdUsing("UPLOAD_API");
        return rucdDAO;
    }

    @Transactional
    public void finalizeForRucsId(Long rucsId, HttpServletRequest request) {
        final ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(rucsId)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the rucsId:" + rucsId, "uploadId.notFound"));
        final ReseaUploadSchSummaryRusmDAO rusmDAO = rusmRepository.findTopByRucsDAOOrderByRusmReqTsDescRusmIdDesc(rucsDAO)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the rucsId:" + rucsId, "uploadId.records.notFound"));
        final Date systimestamp = parRepository.getCurrentTimestamp();
        final String userId = getLoggedInStaffId.apply(request);
        //final List<Integer> daysOfWeek = rucdRepository.getAllDayOfWeeksByRucsIdAndRusmId(rucsDAO.getRucsId(), rusmDAO.getRusmId());
        final HashMap<String, List<String>> errorMap = reseaUploadValidator.validateFinalizeSchedule(
                rucsDAO, rusmDAO, systimestamp, getLoggedInRoleId.apply(request));
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Finalize Staff Schedule Failed.", errorMap);
        }
        final List<ReseaIntvwSchRsisDAO> expireRsisDaoList = rsisRepository.getEffectiveRsisDAOList(
                rucsDAO.getStfDAO().getStfId(), rucsDAO.getLofDAO().getLofId(),
                rucsDAO.getRucsExpirationDt());
        final List<Long> expireRsisIdList = expireRsisDaoList.stream().map(ReseaIntvwSchRsisDAO::getRsisId).toList();
        final Date expirationDate = localDateToDate.apply(dateToLocalDate.apply(
                rucsDAO.getRucsEffectiveDt()).minusDays(1));
        final List<ReseaRmtMtgInfoRsrmDAO> expireRsrmDaoList = rsrmRepository.getEffectiveRsrmDAO(
                expireRsisIdList, rucsDAO.getRucsEffectiveDt());
        // Remove All Future Available RSIC events
        List<ReseaIntvwerCalRsicDAO> availableEventList = rsicRepo.getAllAvailableAppointment(
                rucsDAO.getRucsEffectiveDt(), rucsDAO.getRucsExpirationDt(), RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
                expireRsisIdList);
        final AllowValAlvDAO doNotScheduleAlv = alvRepository.findById(RSIC_CAL_EVENT_TYPE_DO_NOT_SCHEDULE_ALV).get();
        if (!availableEventList.isEmpty()) {
            availableEventList.forEach(rsicDAO -> {
                rsicDAO.setRsicCalEventDispInd(INDICATOR_NO);
                rsicDAO.setRsicCalEventTypeCdAlv(doNotScheduleAlv);
                rsicDAO.setRsicLastUpdBy(userId);
                rsicDAO.setRsicLastUpdUsing("UPLOAD_FINALIZE");
                rsicDAO.setRsicLastUpdTs(systimestamp);
                rsicRepo.save(rsicDAO);
            });
        }
        // Remove All effective RSRM and RSIS entries
        if (!expireRsrmDaoList.isEmpty()) {
            expireRsrmDaoList.forEach(rsrmDAO -> {
                rsrmDAO.setRsrmExpirationDt(expirationDate);
                rsrmDAO.setRsrmLastUpdBy(userId);
                rsrmDAO.setRsrmLastUpdUsing("UPLOAD_FINALIZE");
                rsrmDAO.setRsrmLastUpdTs(systimestamp);
                rsrmRepository.save(rsrmDAO);
            });
        }
        if (!expireRsisDaoList.isEmpty()) {
            expireRsisDaoList.forEach(rsisDAO -> {
                rsisDAO.setRsisExpirationDt(expirationDate);
                rsisDAO.setRsisLastUpdBy(userId);
                rsisDAO.setRsisLastUpdUsing("UPLOAD_FINALIZE");
                rsisDAO.setRsisLastUpdTs(systimestamp);
                rsisRepository.save(rsisDAO);
            });
        }

        final ReseaUploadCmSchRucsDAO oldRucsDAO = rucsRepository.findPrevOpenFinalRucs(
                ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode(),
                rucsDAO.getStfDAO().getStfId(), rucsDAO.getLofDAO().getLofId(), rucsDAO.getRucsEffectiveDt());
        if (oldRucsDAO != null) {
            oldRucsDAO.setRucsExpirationDt(expirationDate);
            oldRucsDAO.setRucsLastUpdBy(userId);
            oldRucsDAO.setRucsLastUpdUsing("UPLOAD_FINALIZE");
            oldRucsDAO.setRucsLastUpdTs(systimestamp);
            rucsRepository.save(oldRucsDAO);
        }

        final AllowValAlvDAO rsrmInitAppt = alvRepository.findById(ReseaAlvEnumConstant.RsrmMeetingTypeCd.INITIAL_APPOINTMENT.getCode()).get();
        final AllowValAlvDAO rsrmFirstSubAppt = alvRepository.findById(ReseaAlvEnumConstant.RsrmMeetingTypeCd.FIRST_SUBS_APPOINTMENT.getCode()).get();
        final AllowValAlvDAO rsrmSecSubAppt = alvRepository.findById(ReseaAlvEnumConstant.RsrmMeetingTypeCd.SECOND_SUBS_APPOINTMENT.getCode()).get();

        // Generate New RSIS and RSRM entries
        List<ReseaUploadCmSchDtlsRucdDAO> rucdDAOList = rucdRepository.getAllByRucsDAOAndRusmDAOOrderByRucdDayOfWeekAscRucdStartTimeAsc(rucsDAO, rusmDAO);
        rucdDAOList.forEach(rucdDAO -> {
            ReseaIntvwSchRsisDAO rsisDAO =  new ReseaIntvwSchRsisDAO();
            rsisDAO.setLofDAO(rucsDAO.getLofDAO());
            rsisDAO.setStfDAO(rucsDAO.getStfDAO());
            rsisDAO.setRsisEffectiveDt(rucsDAO.getRucsEffectiveDt());
            rsisDAO.setRsisExpirationDt(rucsDAO.getRucsExpirationDt());
            rsisDAO.setRsisDayOfWeek(Integer.valueOf(rucdDAO.getRucdDayOfWeek()));
            rsisDAO.setRsisIntvwTypeCd(rucdDAO.getRucdIntvwTypeCdAlv().getAlvId());
            rsisDAO.setRsisStartTime(rucdDAO.getRucdStartTime());
            rsisDAO.setRsisEndTime(rucdDAO.getRucdEndTime());
            rsisDAO.setRsisAllowOnsiteInd(rucdDAO.getRucdAllowOnsiteInd());
            rsisDAO.setRsisAllowRemoteInd(rucdDAO.getRucdAllowRemoteInd());
            rsisDAO.setRsisCreatedBy(userId);
            rsisDAO.setRsisCreatedUsing("UPLOAD_FINALIZE");
            rsisDAO.setRsisCreatedTs(systimestamp);
            rsisDAO.setRsisLastUpdBy(userId);
            rsisDAO.setRsisLastUpdUsing("UPLOAD_FINALIZE");
            rsisDAO.setRsisLastUpdTs(systimestamp);
            rsisRepository.save(rsisDAO);

            if (!Objects.equals(rucdDAO.getRucdIntvwTypeCdAlv().getAlvId(), RSIS_INTVW_TYPE_CD_INIT_APPT)) {
                ReseaRmtMtgInfoRsrmDAO rsrmDAO = new ReseaRmtMtgInfoRsrmDAO();
                rsrmDAO.setRsisDAO(rsisDAO);
                rsrmDAO.setRsrmEffectiveDt(rucsDAO.getRucsEffectiveDt());
                rsrmDAO.setRsrmMeetingCatgCd(RSRM_MEETING_CATG_CD_RESEA_ONR_ON_ONE);
                rsrmDAO.setRsrmMeetingTypeCdALV(getRsrmMeetingTypeAlvDAO(rucdDAO.getRucdIntvwTypeCdAlv().getAlvId(),
                        rsrmInitAppt, rsrmFirstSubAppt, rsrmSecSubAppt));
                rsrmDAO.setRsrmMeetingUrl(rucdDAO.getRucdMeetingUrl());
                rsrmDAO.setRsrmMeetingId(rucdDAO.getRucdMeetingId());
                rsrmDAO.setRsrmMeetingPwd(rucdDAO.getRucdMeetingPwd());
                rsrmDAO.setRsrmMeetingTele1(rucdDAO.getRucdMeetingTele1());
                rsrmDAO.setRsrmMeetingTele2(rucdDAO.getRucdMeetingTele2());
                rsrmDAO.setRsrmMeetingOtherRef(rucdDAO.getRucdMeetingOtherRef());
                rsrmDAO.setRsrmCreatedBy(userId);
                rsrmDAO.setRsrmCreatedUsing("UPLOAD_FINALIZE");
                rsrmDAO.setRsrmCreatedTs(systimestamp);
                rsrmDAO.setRsrmLastUpdBy(userId);
                rsrmDAO.setRsrmLastUpdUsing("UPLOAD_FINALIZE");
                rsrmDAO.setRsrmLastUpdTs(systimestamp);
                rsrmRepository.save(rsrmDAO);
            }
        });

        rucsDAO.setRucsStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode()).get());
        rucsDAO.setRucsLastUpdBy(userId);
        rucsDAO.setRucsLastUpdUsing("UPLOAD_FINALIZE");
        rucsDAO.setRucsLastUpdTs(systimestamp);
        rucsRepository.save(rucsDAO);

        //Insert Transaction
        TransactionTrxDAO trxDAO = new TransactionTrxDAO();
        trxDAO.setTrx01IfkCd(TRX_01_IFK_CD_STF);
        trxDAO.setTrx01Ifk(rucsDAO.getStfDAO().getStfId());
        trxDAO.setTrxOrgProdName("UPLOAD_FINALIZE");
        trxDAO.setTrxTranCd(TRX_TRAN_CD_CREATE_CAL);
        trxDAO.setTrxStatusCd(TRX_STATUS_CD_NOT_PROCESSED);
        trxDAO.setTrxCreatedTs(systimestamp);
        trxDAO.setTrxCreatedBy(userId);
        trxDAO.setTrxStatusUpdTs(systimestamp);
        trxRepository.save(trxDAO);
    }

    private AllowValAlvDAO getRsrmMeetingTypeAlvDAO(Long interviewTypeCd,
                                                    AllowValAlvDAO rsrmInitAppt,
                                                    AllowValAlvDAO rsrmFirstSubAppt,
                                                    AllowValAlvDAO rsrmSecSubAppt) {
        AllowValAlvDAO alvDAO = null;
        if (Objects.equals(interviewTypeCd, ReseaAlvEnumConstant.RsisIntvwTypeCd.INITIAL_APPOINTMENT.getCode())) {
            alvDAO = rsrmInitAppt;
        } else if (Objects.equals(interviewTypeCd, ReseaAlvEnumConstant.RsisIntvwTypeCd.FIRST_SUBS_APPOINTMENT.getCode())) {
            alvDAO = rsrmFirstSubAppt;
        } else if (Objects.equals(interviewTypeCd, ReseaAlvEnumConstant.RsisIntvwTypeCd.SECOND_SUBS_APPOINTMENT.getCode())) {
            alvDAO = rsrmSecSubAppt;
        }
        return alvDAO;
    }

    public List<ReseaUploadRecordsResDTO> getUploadRecords(Long rucsId) {
        final ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(rucsId)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the rucsId:" + rucsId, "uploadId.notFound"));
        final ReseaUploadSchSummaryRusmDAO rusmDAO = rusmRepository.findTopByRucsDAOOrderByRusmReqTsDescRusmIdDesc(rucsDAO)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload Id:"
                        + rucsId, "uploadId.records.notFound"));
        return rucdRepository.getAllByRucsDAOAndRusmDAOOrderByRucdDayOfWeekAscRucdStartTimeAsc(rucsDAO, rusmDAO).stream()
                .map(rucdDAO -> ReseaUploadRecordsResDTO.builder().build()
                        .withDayOfWeek(ReseaUtilFunction.convertDayOfWeekToString().apply(rucdDAO.getRucdDayOfWeek()))
                        .withStartTime(ReseaUtilFunction.convert24hTo12h().apply(rucdDAO.getRucdStartTime()))
                        .withEndTime(ReseaUtilFunction.convert24hTo12h().apply(rucdDAO.getRucdEndTime()))
                        .withInterviewTypeCd(rucdDAO.getRucdIntvwTypeCdAlv().getAlvShortDecTxt())
                        .withMeetingUrl(rucdDAO.getRucdMeetingUrl())
                        .withMeetingId(rucdDAO.getRucdMeetingId())
                        .withMeetingPwd(rucdDAO.getRucdMeetingPwd())
                        .withMeetingPhone1(rucdDAO.getRucdMeetingTele1())
                        .withMeetingPhone2(rucdDAO.getRucdMeetingTele2())
                        .withMeetingOtherRef(rucdDAO.getRucdMeetingOtherRef())
                        .withAllowOnsiteInd(rucdDAO.getRucdAllowOnsiteInd())
                        .withAllowRemoteInd(rucdDAO.getRucdAllowRemoteInd()))
                .toList();
    }

    public void discardForRucsId(Long uploadId, HttpServletRequest request) {
        ReseaUploadCmSchRucsDAO rucsDAO = rucsRepository.findById(uploadId)
                .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the Upload Id:" + uploadId, "uploadId.notFound"));
        final HashMap<String, List<String>> errorMap = reseaUploadValidator.validateDiscardForm(rucsDAO, getLoggedInRoleId.apply(request));
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Upload form Validation failed.", errorMap);
        }
        rucsDAO.setRucsStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RucsStatusCd.DISCARDED.getCode()).get());
        rucsDAO.setRucsLastUpdBy(getLoggedInStaffId.apply(request));
        rucsDAO.setRucsLastUpdUsing("DISCARD_API");
        rucsDAO.setRucsLastUpdTs(parRepository.getCurrentTimestamp());
        rucsRepository.save(rucsDAO);
    }
}
