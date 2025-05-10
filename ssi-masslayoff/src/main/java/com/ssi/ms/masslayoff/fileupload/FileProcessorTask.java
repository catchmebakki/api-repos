package com.ssi.ms.masslayoff.fileupload;

import com.ssi.ms.common.service.ParameterParService;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.constant.ApplicationConstant;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail;
import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadErrorMuseDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadStagingMustDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.database.repository.msl.MslReferenceListMlrlRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslUploadErrorMuseRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslUploadStagingMustRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslUploadSummaryMusmRepository;
import com.ssi.ms.masslayoff.service.ClaimantService;
import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.exception.custom.HeaderValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.ssi.ms.masslayoff.constant.ApplicationConstant.ERROR_MSG_INDEX_249;
import static com.ssi.ms.masslayoff.constant.ApplicationConstant.ERROR_MSG_LENGTH_250;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.MLRL_ID_NOT_FOUND;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.EXCEPTION_PROCESSING_RECORDS;

/**
 * @author Praveenraja Paramsivam
 * A runnable task for processing files, annotated with SpanName for distributed tracing.
 * This class represents a file processing task that can be executed asynchronously.
 */
@SpanName("CmtListUploadProcessor")
@Service
@Component
@Scope("prototype")
@Slf4j
public class FileProcessorTask implements Runnable {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(ApplicationConstant.DATEFORMATE);
    private final File newFile;
    private final String userId;
    private final Long musmId;
    /**
     * Predicate for checking if an MSL upload is a re-upload based on the MslUploadSummaryMusmDAO.
     */
    private final Predicate<MslUploadSummaryMusmDAO> isReUpload = musmDao -> null != musmDao
            && null != musmDao.getMslRefListMlrlDAO() && null != musmDao.getMslRefListMlrlDAO().getMlrlId();
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
    private MslUploadStagingMustRepository mslUploadStagingMustRepository;
    @Autowired
    private MslUploadErrorMuseRepository mslUploadErrorMuseRepository;
    @Autowired
    private ClaimantService claimantService;
    @Autowired
    private MslReferenceListMlrlRepository mslReferenceListMlrlRepository;
    @Autowired
    private MslUploadSummaryMusmRepository mslUploadSummaryMusmRepository;
    @Autowired
    private ParameterParService parService;
    @Autowired
    private SSIExceptionManager ssiExceptionManager;


    public FileProcessorTask(File newFile, String userId, Long musmId) {
        this.newFile = newFile;
        this.userId = userId;
        this.musmId = musmId;
    }

    /**
     * Implements the run method from the Runnable interface.
     * This method contains the code to be executed when the task is run.
     */
    @Override
    public void run() {
        MslRefListMlrlDAO mlrlDao = null;
        MslUploadSummaryMusmDAO musmDao = null;
        try {
            musmDao = mslUploadSummaryMusmRepository.findById(musmId)
                    .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                            "File processing stopped, Musm ID : {0} not found. Uploaded by userId {1}, File Name {2}",
                            musmId, userId, newFile), ""));
            mlrlDao = getMlrlDaoByNewUploadOrReUploadToExistingMlrl(musmDao);
            updateSummaryMusm(musmDao, AlvMassLayoffEnumConstant.MslSummaryStatusCd.FILE_PARSING_STARTED.getCode(), "");
            parseFileAndSaveToStaging(newFile, musmDao, userId);
            updateSummaryMusm(musmDao,
                    AlvMassLayoffEnumConstant.MslSummaryStatusCd.TRANSFER_FROM_STAGING_INITIATED.getCode(), "");
            validateDataInStagingAndUpdatingSummaryDB(musmDao, mlrlDao);
        } catch (Exception e) {
            var errorMessage = String.format(
                    "Error caught in run method, for musmId: %d, MlrlId: %d, userId: %s, File Name: %s, Error message:"
                            + e.getMessage(),
                    musmDao.getMusmId(),
                    null != musmDao.getMslRefListMlrlDAO() && null != musmDao.getMslRefListMlrlDAO().getMlrlId()
                            ? musmDao.getMslRefListMlrlDAO().getMlrlId()
                            : 0,
                    userId, newFile);
            errorMessage = errorMessage.length() > ERROR_MSG_LENGTH_250 ? errorMessage.substring(0, ERROR_MSG_INDEX_249)
                    : errorMessage;
            updateSummaryMusm(musmDao, AlvMassLayoffEnumConstant.MslSummaryStatusCd.FILE_PARSING_FAILED.getCode(),
                    errorMessage);
            ssiExceptionManager.onError(log, null, errorMessage, null, e);
        }
    }

    /**
     * Updates the summary of a Mass Layoff (MSL) upload for a specific MUSM MslUploadSummaryMusmDAO.
     *
     * @param musmDao   {@link MslUploadSummaryMusmDAO} The MUSM DAO object representing the MSL upload summary.
     * @param statusCd  {@link Integer} The status code to be updated in the summary.
     * @param errorDesc {@link String} The error description to be updated in the summary.
     */
    private void updateSummaryMusm(MslUploadSummaryMusmDAO musmDao, Integer statusCd, String errorDesc) {
        if (StringUtils.isNotBlank(errorDesc) && errorDesc.length() > ERROR_MSG_INDEX_249) {
            log.error("MslUploadSummaryMusmDAO.setMusmErrorDesc truncated because size > 250" + errorDesc);
            errorDesc = errorDesc.substring(0, ERROR_MSG_INDEX_249);
        }
        musmDao.setMusmErrorDesc(errorDesc);
        musmDao.setMusmEndTs(parService.getDBTimeStamp());
        musmDao.setMusmStatusCd(statusCd);
        mslUploadSummaryMusmRepository.save(musmDao);
    }

    /**
     * Retrieves an MSL Reference List MslRefListMlrlDAO based on whether it's a new upload or a re-upload
     * to an existing Mass Layoff (MSL) Reference List.
     *
     * @param musmDao {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object representing the MSL upload summary.
     * @return {@link MslRefListMlrlDAO} An MSL Reference List MslRefListMlrlDAO based on the upload context.
     */
    private MslRefListMlrlDAO getMlrlDaoByNewUploadOrReUploadToExistingMlrl(MslUploadSummaryMusmDAO musmDao) {
        MslRefListMlrlDAO mlrlDao = null;
        if (isReUpload.test(musmDao)) {
            mlrlDao = mslReferenceListMlrlRepository.findById(musmDao.getMslRefListMlrlDAO().getMlrlId())
                    .orElseThrow(() -> new NotFoundException(
                            "MLRL_ID attached with MUSM is not available. MLRL_ID = "
                                    + musmDao.getMslRefListMlrlDAO().getMlrlId() + " MUSM_ID = " + musmDao.getMusmId(),
                            MLRL_ID_NOT_FOUND));
        } else {
            mlrlDao = insertDataIntoMlrlByMusmId(musmDao);
            musmDao.setMslRefListMlrlDAO(mlrlDao);
        }
        return mlrlDao;
    }

    /**
     * Inserts data into the Mass Layoff Reference List (MLRL) based on the provided MUSM ID.
     *
     * @param summaryDAO {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object representing the MSL upload summary.
     * @return {@link MslRefListMlrlDAO} An MSL Reference List MslRefListMlrlDAO after inserting the data.
     */
    private MslRefListMlrlDAO insertDataIntoMlrlByMusmId(MslUploadSummaryMusmDAO summaryDAO) {

        var mlrlDAO = new MslRefListMlrlDAO();
        mlrlDAO.setMlrlMslNum(summaryDAO.getMusmMslNum() != null ? summaryDAO.getMusmMslNum() : 0L);
        mlrlDAO.setMlrlEmpAcNum(summaryDAO.getMusmEmpAcNum());
        mlrlDAO.setMlrlEmpAcLoc(summaryDAO.getMusmEmpAcLoc());
        mlrlDAO.setMlrlMslDate(summaryDAO.getMusmMslDate());
        mlrlDAO.setMlrlMslEffDate(summaryDAO.getMusmMslEffDate());
        mlrlDAO.setMlrlRecallDate(summaryDAO.getMusmRecallDate());
        if (StringUtils.isNotBlank(summaryDAO.getMusmDiInd())) {
            mlrlDAO.setMlrlDiInd(summaryDAO.getMusmDiInd().toUpperCase());
        }
        mlrlDAO.setMlrlStatusCd(AlvMassLayoffEnumConstant.MassLayoffStatusCd.PENDING.getCode());
        mlrlDAO.setMlrlCreatedBy(userId);
        mlrlDAO.setMlrlCreatedUsing(CommonConstant.SYSTEM);
        mlrlDAO.setMlrlLastUpdBy(userId);
        mlrlDAO.setMlrlLastUpdUsing(CommonConstant.SYSTEM);
        mlrlDAO = mslReferenceListMlrlRepository.save(mlrlDAO);
        return mlrlDAO;
    }

    /**
     * Parses the provided file and saves the parsed data to the staging area, associated with the given MslUploadSummaryMusmDAO and user.
     *
     * @param newFile {@link File} The file to be parsed.
     * @param musmDao {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object representing the MSL upload summary.
     * @param userId  {@link String} The user ID associated with the parsing and saving process.
     * @throws Exception If an error occurs during file parsing and data saving.
     */
    private void parseFileAndSaveToStaging(File newFile, MslUploadSummaryMusmDAO musmDao, String userId)
            throws Exception {
        Row row = null;
        try (FileInputStream fis = new FileInputStream(newFile); Workbook workbook = WorkbookFactory.create(fis)) {
            if (null != workbook) {
                final Sheet sheet = workbook.getSheetAt(0);
                final List<MslUploadStagingMustDAO> mustDaos = new ArrayList<>();
                if (null != sheet) {
                    final int rowCount = sheet.getLastRowNum() + 1;
                    validateHeader(sheet.getRow(0), musmDao);
                    log.info("Mass Layoff, file parsing row count: " + rowCount);
                    for (int i = 1; i < rowCount; i++) {
                        row = sheet.getRow(i);
                        if (!isExcelRowEmpty.test(row)) {
                            mustDaos.add(parseEachRowToDao(row, musmDao.getMusmId()));
                        }
                    }
                    mslUploadStagingMustRepository.saveAll(mustDaos);
                    updateSummaryMusm(musmDao,
                            AlvMassLayoffEnumConstant.MslSummaryStatusCd.FILE_PARSING_COMPLETED.getCode(), "");
                } else {
                    throw new HeaderValidationException(FileuploadFieldErrorConstant.HEADER_SHEET_MISSING);
                }
            } else {
                throw new HeaderValidationException(FileuploadFieldErrorConstant.HEADER_WORKBOOK_MISSING);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Validates the header row of the provided spreadsheet against the expected structure,
     * considering the context of the MslUploadSummaryMusmDAO for the Mass Layoff (MSL) upload.
     *
     * @param headerRow {@link Row} The header row of the spreadsheet to be validated.
     * @param musmDao   {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object representing the MSL upload summary.
     */
    private void validateHeader(Row headerRow, MslUploadSummaryMusmDAO musmDao) {
        if (null != headerRow) {
            final int rowCount = headerRow.getLastCellNum();
            if (rowCount != ApplicationConstant.HEADER_ROW_COUNT) {
                throw new HeaderValidationException(
                        MessageFormat.format(FileuploadFieldErrorConstant.DISCARD_HEADER_COMLUMN, rowCount));
            }
            for (int i = 0; i < rowCount; i++) {
                final Cell cell = headerRow.getCell(i); // cell index
                if (cell == null || cell.getCellType() == CellType.BLANK || cell.getStringCellValue().isEmpty()) {
                    throw new HeaderValidationException(FileuploadFieldErrorConstant.DISCARD_HEADER_MSG + i);
                }
            }
        } else {
            throw new HeaderValidationException(FileuploadFieldErrorConstant.HEADER_ROW_MISSING);
        }
    }

    /**
     * Parses each row of data in a spreadsheet and converts it into a Mass Layoff (MSL) Upload Staging MUSM (Must) DAO,
     * associated with the given MUSM ID.
     *
     * @param row    {@link Row} The row of data to be parsed.
     * @param musmId {@link Long} The ID associated with the MUSM context.
     * @return {@link MslUploadStagingMustDAO} MSL Upload Staging MUSM (Must) DAO representing the parsed row data.
     */
    private MslUploadStagingMustDAO parseEachRowToDao(Row row, Long musmId) {
        MslUploadStagingMustDAO mustDao = new MslUploadStagingMustDAO();
        MslUploadSummaryMusmDAO musmDao;
        if (row != null) {
            mustDao = new MslUploadStagingMustDAO();
            int cellIndex = 0;
            try {
                mustDao.setMustMslNum(readCellValue(row, cellIndex, FileuploadFieldErrorConstant.MSL_NUM));
                mustDao.setMustEmpAcNum(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.EMP_AC_NUM));
                mustDao.setMustEmpAcLoc(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.EMP_AC_LOC));
                mustDao.setMustMslDate(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.MSL_DATE));
                mustDao.setMustMslEffDate(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.MSL_EFF_DATE));
                mustDao.setMustRecallDate(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.RECALL_DATE));
                mustDao.setMustDiInd(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.DI_IND));
                mustDao.setMustSsn(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.SSN)
                        .replaceAll("-", ""));
                mustDao.setMustFirstName(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.FIRST_NAME));
                mustDao.setMustLastName(readCellValue(row, ++cellIndex, FileuploadFieldErrorConstant.LAST_NAME));
                musmDao = new MslUploadSummaryMusmDAO();
                musmDao.setMusmId(musmId);
                mustDao.setMustCreatedBy(userId);
                mustDao.setMustCreatedUsing(CommonConstant.SYSTEM);
                mustDao.setMustLastUpdBy(userId);
                mustDao.setMustLastUpdUsing(CommonConstant.SYSTEM);
                mustDao.setMslUploadSummaryMusm(musmDao);
            } catch (Exception e) {
                final var errorMessage = String.format(
                        "Error while parsing File rows ,musmId:%d, UserId: %s, row number:"
                                + " %d, Cell Index: %d, must Details: %s, error detail: ",
                        musmId, userId, row.getRowNum(), cellIndex, mustDao) + e.getMessage();
                final var shortErrorMessage = String.format(
                        "Error while parsing File row number: %d, Cell Index: %d.", row.getRowNum(), cellIndex);
                ssiExceptionManager.onError(log, null, errorMessage, null, e);
                processParsingErrorsAndSave(musmId, shortErrorMessage, null);
            }
        }
        return mustDao;
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
     * and saves the relevant data into the Mass Layoff (MSL) Upload Staging MUSM MslUploadStagingMustDAO.
     *
     * @param musmId     {@link Long} The ID associated with the MUSM context.
     * @param errordesc  {@link String} The error description to be associated with the parsing error.
     * @param stagingDao {@link MslUploadStagingMustDAO} The MSL Upload Staging MUSM MslUploadStagingMustDAO containing the parsed data.
     */
    private void processParsingErrorsAndSave(Long musmId, String errordesc, MslUploadStagingMustDAO stagingDao) {
        final MslUploadErrorMuseDAO errorDao = new MslUploadErrorMuseDAO();
        final MslUploadSummaryMusmDAO musmErrorDao = new MslUploadSummaryMusmDAO();
        musmErrorDao.setMusmId(musmId);
        errorDao.setMslUploadSummaryMusm(musmErrorDao);
        errorDao.setMslUploadStagingMust(stagingDao);
        errorDao.setMuseErrDesc(errordesc);
        errorDao.setMuseCreatedBy(userId);
        errorDao.setMuseCreatedUsing(CommonConstant.SYSTEM);
        errorDao.setMuseLastUpdBy(userId);
        errorDao.setMuseLastUpdUsing(CommonConstant.SYSTEM);
        errorDao.setMuseErrNum(UploadFieldErrorDetail.PARSING_ERROR.getCode());
        mslUploadErrorMuseRepository.save(errorDao);
    }

    /**
     * Validates the data in the Mass Layoff (MSL) Upload Staging MUSM (Must) DAO,
     * and updates the summary database using the provided MUSM and MLRL DAOs.
     *
     * @param musmDao {@link MslUploadSummaryMusmDAO} The MslUploadSummaryMusmDAO object representing the MSL upload summary.
     * @param mlrlDao {@link MslRefListMlrlDAO} The MslRefListMlrlDAO object representing the MSL reference list.
     */
    private void validateDataInStagingAndUpdatingSummaryDB(MslUploadSummaryMusmDAO musmDao, MslRefListMlrlDAO mlrlDao) {
        final AtomicInteger errorCount = new AtomicInteger(0);
        final var uploadSummary = mslUploadStagingMustRepository.findByMslUploadSummaryMusmDAOMusmId(musmDao.getMusmId());
        uploadSummary.stream()
                .filter(record -> {
                    boolean result = true;
                    try {
                        final List<UploadFieldErrorDetail> errorDao = fileProcessorTaskValidator
                                .validateStagingData(record, musmDao, userId);
                        if (errorDao.size() == 0) {
                            final List<MslUploadErrorMuseDAO> mslUploadErrorMuseDAOs = fileProcessorTaskValidator
                                    .validateSsnAndClaimantDetails(record, musmDao, userId);
                            if (CollectionUtils.isNotEmpty(mslUploadErrorMuseDAOs)) {
                                mslUploadErrorMuseRepository.saveAll(mslUploadErrorMuseDAOs);
                                errorCount.incrementAndGet();
                                result = false;
                            }
                        }
                        if (errorDao.size() > 0) {
                            final List<MslUploadErrorMuseDAO> mslUploadErrorMuseDAOs = fileProcessorTaskValidator
                                    .createMslUploadErrorMuseDAO(errorDao, record.getMustId(), musmId, userId);
                            mslUploadErrorMuseRepository.saveAll(mslUploadErrorMuseDAOs);
                            errorCount.incrementAndGet();
                            result = false;
                        }
                        if (result) {
                            claimantService.createCmtListForUploadClaimants(record, mlrlDao, userId);
                        }
                    } catch (Exception e) {
                        final var errorMessage = "Error while validating data, " + record.toString();
                        ssiExceptionManager.onError(log, null, errorMessage, null, e);
                        if (e instanceof FileProcessingException) {
                            processParsingErrorsAndSave(musmId,
                                    "Data parsing failed for the field:" + ((FileProcessingException) e).getErrorCode(), record);
                        } else {
                            processParsingErrorsAndSave(musmId, EXCEPTION_PROCESSING_RECORDS, record);
                        }
                        errorCount.incrementAndGet();
                        result = false;
                    }
                    return result;
                }).count();
        musmDao.setMusmNumErrs(errorCount.get());
        musmDao.setMusmNumRecs(uploadSummary.size());
        updateSummaryMusm(musmDao, AlvMassLayoffEnumConstant.MslSummaryStatusCd.UPLOAD_PROCESS_COMPLETED.getCode(),
                FileuploadFieldErrorConstant.RECORDS_FAILED);
    }

}