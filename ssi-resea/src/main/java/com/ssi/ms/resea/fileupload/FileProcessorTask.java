package com.ssi.ms.resea.fileupload;

import com.ssi.ms.common.database.repository.UserRepository;
import com.ssi.ms.common.service.ParameterParService;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.exception.custom.HeaderValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchDtlsRucdDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchErrorRuseDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchStagingRussDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadCmSchRucsRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadErrorRuseRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadSchStagingRussRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadCmSchDtlsRucdRepository;
import com.ssi.ms.resea.database.repository.ReseaUploadSchSummaryRusmRepository;
import com.ssi.ms.resea.service.ReseaUploadService;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.EXCEPTION_PROCESSING_RECORDS;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.HEADER_SHEET_MISSING;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.HEADER_WORKBOOK_MISSING;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;
import static com.ssi.ms.resea.constant.ReseaConstants.DISCARD_HEADER_COMLUMN;
import static com.ssi.ms.resea.constant.ReseaConstants.DISCARD_HEADER_MISSING_MSG;
import static com.ssi.ms.resea.constant.ReseaConstants.DISCARD_HEADER_MSG;
import static com.ssi.ms.resea.constant.ReseaConstants.ERROR_MSG_INDEX_249;
import static com.ssi.ms.resea.constant.ReseaConstants.ERROR_MSG_LENGTH_250;
import static com.ssi.ms.resea.constant.ReseaConstants.RESEA_UPLOAD_HEADER_COLUMN_COUNT;
import static com.ssi.ms.resea.constant.ReseaConstants.HEADER_ROW_MISSING;
import static com.ssi.ms.resea.util.ReseaUtilFunction.substring;

/**
 * A runnable task for processing files, annotated with SpanName for distributed tracing.
 * This class represents a file processing task that can be executed asynchronously.
 */
@SpanName("ReseaUploadProcessor")
@Service
@Component
@Scope("prototype")
@Slf4j
public class FileProcessorTask implements Runnable {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);
    private final String originalFileName;
    private final File newFile;
    private final Long rucsId;
    private final Long rusmId;
    private final String userId;

    private final Predicate<Row> isExcelRowEmpty = row -> {
        boolean isValid = true;
        if (row != null && row.getLastCellNum() > 0) {
            for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
                final Cell cell = row.getCell(cellNum);
                if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    };
    @Autowired
    private FileProcessorTaskValidator fileProcessorTaskValidator;
    @Autowired
    private ReseaUploadCmSchRucsRepository rucsRepository;
    @Autowired
    private ReseaUploadCmSchDtlsRucdRepository rucdRepository;
    @Autowired
    private ReseaUploadSchStagingRussRepository russRepository;
    @Autowired
    private ReseaUploadErrorRuseRepository ruseRepository;
    @Autowired
    private ReseaUploadSchSummaryRusmRepository rusmRepository;
    @Autowired
    private ParameterParService parService;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;
    @Autowired
    private ReseaUploadService uploadService;
    public FileProcessorTask(String originalFileName, File newFile, String userId, Long rucsId, Long rusmId) {
        this.originalFileName = originalFileName;
        this.newFile = newFile;
        this.rusmId = rusmId;
        this.rucsId = rucsId;
        this.userId = userId;
    }

    /**
     * Implements the run method from the Runnable interface.
     * This method contains the code to be executed when the task is run.
     */
    @Override
    @Transactional
    public void run() {
        ReseaUploadSchSummaryRusmDAO rusmDAO = null;
        try {
            log.info("Processing File Upload Start");
            rusmDAO = rusmRepository.findById(rusmId)
                    .orElseThrow(() -> new NotFoundException("RESEA upload Summary not found for the File Id:" + rusmId, "fileId.notFound"));
            rusmDAO.setRusmSysFilename(newFile.getName());
            rusmDAO.setRusmStatusCdAlv(alvRepository.findById(ReseaAlvEnumConstant.RusmStatusCd.UPLOADED.getCode()).orElse(null));
            rusmDAO.setRusmLastUpdBy(userId);
            rusmDAO.setRusmLastUpdUsing("UPLOAD_FILE");
            rusmRepository.save(rusmDAO);

            updateSummaryRusm(rusmDAO, ReseaAlvEnumConstant.RusmStatusCd.PARSING_STARTED.getCode(), "");
            parseFileAndSaveToStaging(newFile, rusmDAO, userId);
            updateRucs(rusmDAO.getRucsDAO(), userId, ReseaAlvEnumConstant.RucsStatusCd.INITIATED.getCode());
            updateSummaryRusm(rusmDAO, ReseaAlvEnumConstant.RusmStatusCd.STAGING_INITIATED.getCode(), "");
            validateDataInStagingAndUpdatingSummaryDB(rusmDAO);
            log.info("Processing File Upload End");
        } catch (Exception e) {
            var errorMessage = String.format(
                    "Error caught in run method, for uploadId: %d, userId: %s, File Name: %s, Error message:"
                            + e.getMessage(),
                    rusmId, userId, newFile);
            errorMessage = errorMessage.length() > ERROR_MSG_LENGTH_250 ? errorMessage.substring(0, ERROR_MSG_INDEX_249)
                    : errorMessage;
            updateSummaryRusm(rusmDAO, ReseaAlvEnumConstant.RusmStatusCd.PARSING_FAILED.getCode(),
                    e.getMessage().length() > ERROR_MSG_LENGTH_250 ? e.getMessage().substring(0, ERROR_MSG_INDEX_249)
                            : e.getMessage());
            updateRucs(rusmDAO.getRucsDAO(), userId, ReseaAlvEnumConstant.RucsStatusCd.INITIATED.getCode());
            ssiExceptionManager.onError(log, null, errorMessage, null, e);
        }
    }

    /*@Transactional
    private ReseaUploadSchSummaryRusmDAO initializeRusm(ReseaUploadCmSchRucsDAO rucsDAO, String originalFileName,
                                                        String loggedInUserId, Date sysTimestamp) {
        ReseaUploadSchSummaryRusmDAO rusmDAO = new ReseaUploadSchSummaryRusmDAO();
        rusmDAO.setUserDAO(userRepository.findByUserId(Long.valueOf(loggedInUserId)));
        rusmDAO.setRucsDAO(rucsDAO);
        rusmDAO.setRusmReqTs(sysTimestamp);
        rusmDAO.setRusmCaseManager(rucsDAO.getStfDAO().getStaffName());
        rusmDAO.setRusmLocalOffice(rucsDAO.getLofDAO().getLofName());
        rusmDAO.setRusmFilename(originalFileName);
        rusmDAO.setRusmSysFilename(newFile.getName());
        rusmDAO.setRusmStartTs(sysTimestamp);
        rusmDAO.setRusmCreatedBy(loggedInUserId);
        rusmDAO.setRusmCreatedUsing("UPLOAD_API");
        rusmDAO.setRusmCreatedTs(sysTimestamp);
        rusmDAO.setRusmLastUpdBy(loggedInUserId);
        rusmDAO.setRusmLastUpdUsing("UPLOAD_API");
        rusmDAO.setRusmLastUpdTs(sysTimestamp);
        return rusmDAO;
    }*/

    /**
      * Updates the summary of a RESEA upload for a specific ReseaUploadCmSchDtlsRucdDAO.
      *
      * @param rusmDao   {@link ReseaUploadSchSummaryRusmDAO} The RUSM DAO object representing the RESEA upload summary.
      * @param statusCd  {@link Integer} The status code to be updated in the summary.
      * @param errorDesc {@link String} The error description to be updated in the summary.
      */
    @Transactional
    private void updateSummaryRusm(ReseaUploadSchSummaryRusmDAO rusmDao, Long statusCd, String errorDesc) {
        if (StringUtils.isNotBlank(errorDesc) && errorDesc.length() > ERROR_MSG_INDEX_249) {
            log.error("ReseaUploadCmSchDtlsRucdDAO.setMusmErrorDesc truncated because size > 250" + errorDesc);
            errorDesc = errorDesc.substring(0, ERROR_MSG_INDEX_249);
        }
        rusmDao.setRusmErrorDesc(errorDesc);
        rusmDao.setRusmEndTs(parService.getDBTimeStamp());
        rusmDao.setRusmStatusCdAlv(alvRepository.findById(statusCd).orElse(null));
        rusmRepository.save(rusmDao);
    }

    @Transactional
    private void updateRucs(ReseaUploadCmSchRucsDAO rucsDAO, String userId, Long statusCd) {
        rucsDAO.setRucsStatusCdAlv(alvRepository.findById(statusCd).get());
        rucsDAO.setRucsLastUpdBy(userId);
        rucsDAO.setRucsLastUpdUsing("UPLOAD_API");
        rucsDAO.setRucsLastUpdTs(parService.getDBTimeStamp());
        rucsRepository.save(rucsDAO);
    }

    /**
     * Parses the provided file and saves the parsed data to the staging area, associated with the given ReseaUploadCmSchDtlsRucdDAO and user.
     *
     * @param newFile {@link File} The file to be parsed.
     * @param rusmDAO {@link ReseaUploadCmSchDtlsRucdDAO} The ReseaUploadCmSchDtlsRucdDAO object representing the RESEA upload summary.
     * @param userId  {@link String} The user ID associated with the parsing and saving process.
     * @throws Exception If an error occurs during file parsing and data saving.
     */
    @Transactional
    private void parseFileAndSaveToStaging(File newFile, ReseaUploadSchSummaryRusmDAO rusmDAO, String userId)
            throws FileProcessingException, IOException, HeaderValidationException {
        Row row = null;
        try (FileInputStream fis = new FileInputStream(newFile); Workbook workbook = WorkbookFactory.create(fis)) {
            if (null != workbook) {
                final Sheet sheet = workbook.getSheetAt(0);
                final List<ReseaUploadSchStagingRussDAO> stagingRussDAOS = new ArrayList<>();
                if (null != sheet) {
                    final int rowCount = sheet.getLastRowNum() + 1;
                    validateHeader(sheet.getRow(0), rusmDAO);
                    log.info("RESEA, file parsing row count: " + rowCount);
                    for (int i = 1; i < rowCount; i++) {
                        row = sheet.getRow(i);
                        if (!isExcelRowEmpty.test(row)) {
                            stagingRussDAOS.add(parseEachRowToDao(row, rusmDAO, userId));
                        }
                    }
                    russRepository.saveAll(stagingRussDAOS);
                    updateSummaryRusm(rusmDAO, ReseaAlvEnumConstant.RusmStatusCd.PARSING_COMPLETE.getCode(), "");
                } else {
                    throw new HeaderValidationException(HEADER_SHEET_MISSING);
                }
            } else {
                throw new HeaderValidationException(HEADER_WORKBOOK_MISSING);
            }
        }
    }

    /**
     * Validates the header row of the provided spreadsheet against the expected structure,
     * considering the context of the ReseaUploadCmSchDtlsRucdDAO for the RESEA upload.
     *
     * @param headerRow {@link Row} The header row of the spreadsheet to be validated.
     * @param rusmDAO   {@link ReseaUploadCmSchDtlsRucdDAO} The ReseaUploadCmSchDtlsRucdDAO object representing the RESEA upload summary.
     */
    private void validateHeader(Row headerRow, ReseaUploadSchSummaryRusmDAO rusmDAO) {
        if (null != headerRow) {
            final int rowCount = headerRow.getLastCellNum();
            if (rowCount < RESEA_UPLOAD_HEADER_COLUMN_COUNT) {
                throw new HeaderValidationException(
                        MessageFormat.format(DISCARD_HEADER_COMLUMN, rowCount, RESEA_UPLOAD_HEADER_COLUMN_COUNT));
            }
            for (int i = 0; i < rowCount; i++) {
                final Cell cell = headerRow.getCell(i); // cell index
                if (cell == null || cell.getCellType() == CellType.BLANK || cell.getStringCellValue().isEmpty()) {
                    throw new HeaderValidationException(DISCARD_HEADER_MSG + i);
                } else  if (i == 0 && !"Day of Week".equalsIgnoreCase(cell.getStringCellValue())) {
                    throw new HeaderValidationException(DISCARD_HEADER_MISSING_MSG);
                }
            }
        } else {
            throw new HeaderValidationException(HEADER_ROW_MISSING);
        }
    }

    /**
     * Parses each row of data in a spreadsheet and converts it into a RESEA Upload Staging RUSS DAO,
     * associated with the given RUSM ID.
     *
     * @param row    {@link Row} The row of data to be parsed.
     * @param rusmDAO {@link Long} The ID associated with the RUSM context.
     * @return {@link ReseaUploadSchStagingRussDAO} RESEA Upload Staging RUSS DAO representing the parsed row data.
     */
    @Transactional
    private ReseaUploadSchStagingRussDAO parseEachRowToDao(Row row, ReseaUploadSchSummaryRusmDAO rusmDAO, String userId) {
        ReseaUploadSchStagingRussDAO russDAO = new ReseaUploadSchStagingRussDAO();
        if (row != null) {
            int cellIndex = 0;
            try {
                russDAO.setRusmDAO(rusmDAO);
                russDAO.setRussDayOfWeek(substring.apply(readCellValue(row, cellIndex++, "Day Of Week").trim(), 20));
                russDAO.setRussApptTimeframe(substring.apply(readCellValue(row, cellIndex++, "Time").trim(), 50));
                russDAO.setRussZoomLinkDetails(substring.apply(readCellValue(row, cellIndex++, "Zoom Link"), 4000));
                russDAO.setRussAllowOnsiteInd(substring.apply(readCellValue(row, cellIndex++, "Allow Onsite").trim().toUpperCase(), 1));
                russDAO.setRussAllowRemoteInd(substring.apply(readCellValue(row, cellIndex++, "Allow Remote").trim().toUpperCase(), 1));
                russDAO.setRussCreatedBy(userId);
                russDAO.setRussCreatedUsing(CommonConstant.SYSTEM);
                //russDAO.setRussCreatedTs(parService.getDBTimeStamp());
                russDAO.setRussLastUpdBy(userId);
                russDAO.setRussLastUpdUsing(CommonConstant.SYSTEM);
                //russDAO.setRussLastUpdTs(parService.getDBTimeStamp());
            } catch (Exception e) {
                final var errorMessage = String.format(
                        "Error while parsing File rows ,rusmDAO:%d, UserId: %s, row number:"
                                + " %d, Cell Index: %d, must Details: %s, error detail: ",
                        rusmDAO.getRusmId(), userId, row.getRowNum(), cellIndex, russDAO) + e.getMessage();
                final var shortErrorMessage = String.format(
                        "Error while parsing File row number: %d, Cell Index: %d.", row.getRowNum(), cellIndex);
                ssiExceptionManager.onError(log, null, errorMessage, null, e);
                processParsingErrorsAndSave(rusmDAO, shortErrorMessage, null);
            }
        }
        return russDAO;
    }
    /**
     * Reads the value of a specific cell in a row of a excel sheet, using the provided cell index,
     * and associates it with the given field name.
     *
     * @param row       {@link Row} The row containing the cell to be read.
     * @param cellIndex {@link int} The index of the cell to be read within the row.
     * @param fieldName String The name of the field associated with the cell's value.
     * @return {@link String} The value of the specified cell as a string.
     */
    private String readCellValue(Row row, int cellIndex, String fieldName) {
        String stringValue = null;
        final Cell cell = row.getCell(cellIndex);
        if (null != cell) {
            try {
                final CellType cellType = cell.getCellType();
                switch (cellType) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            stringValue = SDF.format(cell.getDateCellValue());
                        } else {
                            stringValue = String.valueOf((int) cell.getNumericCellValue());
                        }
                        break;
                    case STRING:
                        stringValue = cell.getStringCellValue();
                        break;
                    case BOOLEAN:
                        stringValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        stringValue = "";
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new FileProcessingException("Error while parsing field : " + fieldName, "", e);
            }
        }
        return stringValue;
    }

    /**
     * Processes parsing errors, associates them with a specific MUSM ID and error description,
     * and saves the relevant data into the RESEA Upload Staging MUSM ReseaUploadSchStagingRussDAO.
     *
     * @param rusmDAO     {@link Long} The ID associated with the MUSM context.
     * @param errordesc  {@link String} The error description to be associated with the parsing error.
     * @param stagingDao {@link ReseaUploadSchStagingRussDAO} The MSL Upload Staging MUSM ReseaUploadSchStagingRussDAO containing the parsed data.
     */
    @Transactional
    private void processParsingErrorsAndSave(ReseaUploadSchSummaryRusmDAO rusmDAO, String errordesc, ReseaUploadSchStagingRussDAO stagingDao) {
        final ReseaUploadSchErrorRuseDAO errorDao = new ReseaUploadSchErrorRuseDAO();
        errorDao.setRusmDAO(rusmDAO);
        errorDao.setRussDAO(stagingDao);
        errorDao.setRuseErrDesc(errordesc);
        errorDao.setRuseCreatedBy(userId);
        errorDao.setRuseCreatedUsing(CommonConstant.SYSTEM);
        errorDao.setRuseLastUpdBy(userId);
        errorDao.setRuseLastUpdUsing(CommonConstant.SYSTEM);
        errorDao.setRuseErrNum(ErrorMessageConstant.UploadFieldErrorDetail.PARSING_ERROR.getCode());
        ruseRepository.save(errorDao);
    }

    /**
     * Validates the data in the RESEA Upload Staging RUSS DAO,
     * and updates the summary database using the provided RUSM DAO.
     *
     * @param rusmDao {@link ReseaUploadCmSchDtlsRucdDAO} The ReseaUploadCmSchDtlsRucdDAO object representing the RESEA upload summary.
     * 
     */
    @Transactional
    private void validateDataInStagingAndUpdatingSummaryDB(ReseaUploadSchSummaryRusmDAO rusmDao) {
        final AtomicInteger errorCount = new AtomicInteger(0);
        final List<ReseaUploadSchStagingRussDAO> uploadSummary = russRepository.findByRussDAORusmId(rusmDao.getRusmId());
        final List<ReseaUploadCmSchDtlsRucdDAO> rucdDAOList = new ArrayList<>();
        final String caseMgrFirstName = rusmDao.getRucsDAO().getStfDAO().getStfFirstName().trim().toUpperCase();
        final String caseMgrLastName = rusmDao.getRucsDAO().getStfDAO().getStfLastName().trim().toUpperCase();
        for (ReseaUploadSchStagingRussDAO record: uploadSummary) {
            boolean result = true;
            try {
                final List<ErrorMessageConstant.UploadFieldErrorDetail> errorDao = fileProcessorTaskValidator
                        .validateStagingData(record, rusmDao, userId);
                if (errorDao.isEmpty()) {
                    final List<Long> overlappingRussIds = russRepository.getOverlappingTimeSlots(rusmDao.getRusmId());
                    final List<ReseaUploadSchErrorRuseDAO> errorRuseDAOList = fileProcessorTaskValidator
                            .validateSchedulesDetails(record, rusmDao, overlappingRussIds, caseMgrFirstName, caseMgrLastName, userId);
                    if (!CollectionUtils.isEmpty(errorRuseDAOList)) {
                        ruseRepository.saveAll(errorRuseDAOList);
                        errorCount.incrementAndGet();
                        result = false;
                    }
                }
                if (!errorDao.isEmpty()) {
                    final List<ReseaUploadSchErrorRuseDAO> ruseDAOList = fileProcessorTaskValidator
                            .createReseaUploadErrorRuseDAO(errorDao, record, rusmDao, userId);
                    ruseRepository.saveAll(ruseDAOList);
                    errorCount.incrementAndGet();
                    result = false;
                }
                if (result) {
                    rucdDAOList.add(uploadService.createMeetingScheduleDetails(record, rusmDao, userId));
                }
            } catch (Exception e) {
                final var errorMessage = "Error while validating data, " + record.toString();
                ssiExceptionManager.onError(log, null, errorMessage, null, e);
                if (e instanceof FileProcessingException) {
                    processParsingErrorsAndSave(rusmDao,
                            "Data parsing failed for the field:" + ((FileProcessingException) e).getErrorCode(), record);
                } else {
                    processParsingErrorsAndSave(rusmDao, EXCEPTION_PROCESSING_RECORDS, record);
                }
                errorCount.incrementAndGet();
            }
        }
        if (!rucdDAOList.isEmpty()) {
            rucdRepository.saveAll(rucdDAOList);
        }
        rusmDao.setRusmNumErrs(errorCount.get());
        rusmDao.setRusmNumRecs(uploadSummary.size());
        if (errorCount.get() > 0) {
            updateSummaryRusm(rusmDao, ReseaAlvEnumConstant.RusmStatusCd.COMPLETE.getCode(),
                    "Some records are failed, for more details check error table.");
            updateRucs(rusmDao.getRucsDAO(), userId, ReseaAlvEnumConstant.RucsStatusCd.INITIATED.getCode());
        } else {
            updateSummaryRusm(rusmDao, ReseaAlvEnumConstant.RusmStatusCd.COMPLETE.getCode(),
                    null);
            updateRucs(rusmDao.getRucsDAO(), userId, ReseaAlvEnumConstant.RucsStatusCd.UPLOADED_PENDING_FINAL.getCode());
        }
    }

}