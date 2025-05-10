package com.ssi.ms.masslayoff.fileupload;

import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.CLAIMANT_MRLR_ALREADY_ASSOCIATED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.EMPACCLOC_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.EMPACCNUMBER_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.FIRSTNAME_NOT_IN_NHUIS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.FIRST_NAME_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.LASTNAME_NOT_IN_NHUIS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.LAST_NAME_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSLDATE_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSLEFFDATE_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSLNUMBER_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLDATE_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLEFFDATE_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_RECALLDATE_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.RECALLDATE_IS_NOT_EQUALS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.SSN_NUMBER_IS_INVALID;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.DI_IND_IS_NOT_EQUALS;
import static com.ssi.ms.platform.util.UtilFunction.stringToLong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.masslayoff.constant.ApplicationConstant;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail;
import com.ssi.ms.masslayoff.database.dao.CliamantCmtDAO;
import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadErrorMuseDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadStagingMustDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadSummaryMusmDAO;
import com.ssi.ms.masslayoff.database.repository.CliamantRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslEntryCmtMlecRepository;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.util.UtilFunction;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author Praveenraja Paramsivam
 * A component class responsible for validating file processing tasks.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class FileProcessorTaskValidator {
	private static final SimpleDateFormat SDF = new SimpleDateFormat(ApplicationConstant.DATEFORMATE);

	@Autowired
	private CliamantRepository cliamantRepository;
	@Autowired
	private MslEntryCmtMlecRepository mlecRepository;

	private BiPredicate<String, String> compareNotEqual = (str1, str2) -> StringUtils.isNotBlank(str1)
			&& !str1.equalsIgnoreCase(str2);
	private BiPredicate<CliamantCmtDAO, String> validateFirstName = (dao, firstName) -> null != dao
			&& compareNotEqual.test(dao.getFirstName(), firstName);
	private BiPredicate<CliamantCmtDAO, String> validateLastName = (dao, lastName) -> null != dao
			&& compareNotEqual.test(dao.getLastName(), lastName);
	/**
	 * Validates Social Security Number (SSN) and claimant details for a given MSL upload record.
	 *
	 * @param record {@link MslUploadStagingMustDAO}  The MSL upload staging record to validate.
	 * @param musmDAO {@link MslUploadSummaryMusmDAO} The MSL upload summary DAO object.
	 * @param userId {@link String} The user ID associated with the validation.
	 * @return {@link List<MslUploadErrorMuseDAO>} A list of MslUploadErrorMuseDAO objects representing validation errors, if any.
	 */
	public List<MslUploadErrorMuseDAO> validateSsnAndClaimantDetails(MslUploadStagingMustDAO record,
			MslUploadSummaryMusmDAO musmDAO, String userId) {
		final List<MslUploadErrorMuseDAO> errorMuseDAOS = new ArrayList<>();
		MslUploadErrorMuseDAO errorDao = null;
		CliamantCmtDAO cmtDao = null;
		if (StringUtils.isBlank(record.getMustSsn()) || !StringUtils.isNumeric(record.getMustSsn())
				|| record.getMustSsn().length() != FileuploadFieldErrorConstant.SSN_LENGTH) {
			errorDao = createMslUploadErrorMuseDAO(SSN_NUMBER_IS_INVALID, record.getMustId(), musmDAO.getMusmId(),
					userId);
			errorMuseDAOS.add(errorDao);
		} else {
			cmtDao = cliamantRepository.findBySsn(record.getMustSsn());
			final List<MslEntryCmtMlecDAO> mlecDao = mlecRepository.findBySsnAndMlrlId(record.getMustSsn(),
					musmDAO.getMslRefListMlrlDAO() != null ? musmDAO.getMslRefListMlrlDAO().getMlrlId() : null);
			if (mlecDao != null && mlecDao.size() > 0) {
				errorDao = createMslUploadErrorMuseDAO(CLAIMANT_MRLR_ALREADY_ASSOCIATED, record.getMustId(), musmDAO.getMusmId(),
						userId);
				errorMuseDAOS.add(errorDao);
			} else {
				if (testnotBlankAndCreateMslUploadErrorMuseDAO(FIRST_NAME_IS_REQUIRED, record.getMustId(),
						musmDAO.getMusmId(), userId, errorMuseDAOS, record.getMustFirstName())) {
					compareAndCreateMslUploadErrorMuseDAO(FIRSTNAME_NOT_IN_NHUIS, record.getMustId(),
							musmDAO.getMusmId(), userId, errorMuseDAOS, validateFirstName, cmtDao,
							record.getMustFirstName());
				}
				if (testnotBlankAndCreateMslUploadErrorMuseDAO(LAST_NAME_IS_REQUIRED, record.getMustId(),
						musmDAO.getMusmId(), userId, errorMuseDAOS, record.getMustLastName())) {
					compareAndCreateMslUploadErrorMuseDAO(LASTNAME_NOT_IN_NHUIS, record.getMustId(),
							musmDAO.getMusmId(), userId, errorMuseDAOS, validateLastName, cmtDao,
							record.getMustLastName());
				}
			}
		}
		return errorMuseDAOS;
	}
	/**
	 * Tests whether a field is not blank, and if it is blank, creates a corresponding MSL upload error DAO
	 * for the specified error detail.
	 *
	 * @param errorDetailEnum {@link UploadFieldErrorDetail} The error detail enumeration representing the specific error.
	 * @param mustId  {@link Long}        The MSL upload staging ID associated with the field.
	 * @param musmId  {@link Long}       The MSL upload summary ID associated with the field.
	 * @param userId  {@link String}       The user ID associated with the validation.
	 * @param errorMuseDAOS {@link List<MslUploadErrorMuseDAO>}  The list of MslUploadErrorMuseDAO objects to add errors to.
	 * @param fieldString  {@link String}   The field value to be tested.
	 * @return {@code true} if the field is not blank, {@code false} otherwise.
	 */
	private boolean testnotBlankAndCreateMslUploadErrorMuseDAO(UploadFieldErrorDetail errorDetailEnum, Long mustId,
			Long musmId, String userId, List<MslUploadErrorMuseDAO> errorMuseDAOS, String fieldString) {
		boolean toReturn = true;
		if (StringUtils.isBlank(fieldString)) {
			errorMuseDAOS.add(createMslUploadErrorMuseDAO(errorDetailEnum, mustId, musmId, userId));
			toReturn = false;
		}
		return toReturn;
	}
	/**
	 * Compares a given field using a custom condition, and if the condition is met, creates a corresponding
	 * MSL upload error DAO for the specified error detail.
	 *
	 * @param errorDetailEnum {@link UploadFieldErrorDetail} The error detail enumeration representing the specific error.
	 * @param mustId {@link Long}         The MSL upload staging ID associated with the field.
	 * @param musmId {@link Long}         The MSL upload summary ID associated with the field.
	 * @param userId {@link String}         The user ID associated with the validation.
	 * @param errorMuseDAOS  {@link List<MslUploadErrorMuseDAO>} The list of MslUploadErrorMuseDAO objects to add errors to.
	 * @param condition  {@link BiPredicate<CliamantCmtDAO, String>}     The custom condition to evaluate for the comparison.
	 * @param dao {@link CliamantCmtDAO}            The CliamantCmtDAO object representing claimant details.
	 * @param fieldString {@link String}    The field value to be compared.
	 * @return {@code true} if the condition is met and an error is created, {@code false} otherwise.
	 */
	private boolean compareAndCreateMslUploadErrorMuseDAO(UploadFieldErrorDetail errorDetailEnum, Long mustId,
			Long musmId, String userId, List<MslUploadErrorMuseDAO> errorMuseDAOS,
			BiPredicate<CliamantCmtDAO, String> condition, CliamantCmtDAO dao, String fieldString) {
		boolean toReturn = false;
		if (condition.test(dao, fieldString)) {
			errorMuseDAOS.add(createMslUploadErrorMuseDAO(errorDetailEnum, mustId, musmId, userId));
			toReturn = true;
		}
		return toReturn;
	}
	/**
	 * Creates a list of MSL upload error DAOs based on the provided list of error constants, MSL upload staging ID,
	 * MSL upload summary ID, and user ID.
	 *
	 * @param errorConstantList {@link List<UploadFieldErrorDetail>} The list of UploadFieldErrorDetail constants
	 * representing the specific errors.
	 * @param mustId  {@link Long}          The MSL upload staging ID associated with the errors.
	 * @param musmId  {@link Long}          The MSL upload summary ID associated with the errors.
	 * @param userId  {@link String}          The user ID associated with the error creation.
	 * @return {@link List<MslUploadErrorMuseDAO>} list of MslUploadErrorMuseDAO objects representing the created errors.
	 */
	public List<MslUploadErrorMuseDAO> createMslUploadErrorMuseDAO(List<UploadFieldErrorDetail> errorConstantList,
			Long mustId, Long musmId, String userId) {
		return errorConstantList.stream()
				.map(errorEnum -> createMslUploadErrorMuseDAO(errorEnum, mustId, musmId, userId)).toList();
	}
	/**
	 * Creates an MSL upload error DAO for a specific error detail, associated with the provided MSL upload staging ID,
	 * MSL upload summary ID, and user ID.
	 *
	 * @param errorDetailEnum {@link UploadFieldErrorDetail} The error detail enumeration representing the specific error.
	 * @param mustId  {@link Long}        The MSL upload staging ID associated with the error.
	 * @param musmId  {@link Long}        The MSL upload summary ID associated with the error.
	 * @param userId  {@link String}        The user ID associated with the error creation.
	 * @return An instance of MslUploadErrorMuseDAO representing the created error.
	 */
	public MslUploadErrorMuseDAO createMslUploadErrorMuseDAO(UploadFieldErrorDetail errorDetailEnum, Long mustId,
			Long musmId, String userId) {
		final MslUploadErrorMuseDAO errorDao = new MslUploadErrorMuseDAO();
		final MslUploadSummaryMusmDAO musmErrorDao = new MslUploadSummaryMusmDAO();
		musmErrorDao.setMusmId(musmId);
		MslUploadStagingMustDAO mustDao = null;
		if (null != mustId) {
			mustDao = new MslUploadStagingMustDAO();
			mustDao.setMustId(mustId);
			errorDao.setMslUploadStagingMust(mustDao);
		}
		errorDao.setMslUploadSummaryMusm(musmErrorDao);
		if (errorDetailEnum != null) {
			errorDao.setMuseErrNum(errorDetailEnum.getCode());
		}
		if (errorDetailEnum != null) {
			errorDao.setMuseErrDesc(errorDetailEnum.getDescription());
		}
		errorDao.setMuseCreatedBy(userId);
		errorDao.setMuseCreatedUsing(CommonConstant.SYSTEM);
		errorDao.setMuseLastUpdBy(userId);
		errorDao.setMuseLastUpdUsing(CommonConstant.SYSTEM);
		return errorDao;
	}
	/**
	 * Validates staging data for an MSL upload record.
	 *
	 * @param record {@link MslUploadStagingMustDAO}  The MSL upload staging record to validate.
	 * @param musmDao {@link MslUploadSummaryMusmDAO} The MSL upload summary DAO object.
	 * @param userId  {@link String} The user ID associated with the validation.
	 * @return {@link List<UploadFieldErrorDetail>} list of UploadFieldErrorDetail constants representing validation errors, if any.
	 * @throws Exception If an exception occurs during the validation process.
	 */
	public List<UploadFieldErrorDetail> validateStagingData(MslUploadStagingMustDAO record,
			MslUploadSummaryMusmDAO musmDao, String userId) throws Exception {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		String fieldName = null;
		try {
			if (!UtilFunction.compareLongObject.test(stringToLong.apply(record.getMustMslNum()),
					musmDao.getMusmMslNum())) {
				errorConstantList.add(MSLNUMBER_IS_NOT_EQUALS);
			}
			if (StringUtils.isBlank(record.getMustEmpAcNum()) || !StringUtils.isNumeric(record.getMustEmpAcNum())
					|| !record.getMustEmpAcNum().equalsIgnoreCase(musmDao.getMusmEmpAcNum())) {
				errorConstantList.add(EMPACCNUMBER_IS_NOT_EQUALS);
			}
			if (StringUtils.isBlank(record.getMustEmpAcLoc())
					|| !record.getMustEmpAcLoc().equalsIgnoreCase(musmDao.getMusmEmpAcLoc())) {
				errorConstantList.add(EMPACCLOC_IS_NOT_EQUALS);
			}

			if (StringUtils.isBlank(record.getMustMslDate())) {
				errorConstantList.add(MSL_MSLDATE_IS_REQUIRED);
			} else {
				fieldName = "mustMslDate";
				final Date mslDateObj = SDF.parse(record.getMustMslDate());
				final String msmmMuslDatStr = SDF.format(musmDao.getMusmMslDate());
				// convert back to date
				final Date msmmMuslDate = SDF.parse(msmmMuslDatStr);
				if (!mslDateObj.equals(msmmMuslDate)) {
					errorConstantList.add(MSLDATE_IS_NOT_EQUALS);
				}
			}
			if (StringUtils.isBlank(record.getMustMslEffDate())) {
				errorConstantList.add(MSL_MSLEFFDATE_IS_REQUIRED);
			} else {
				fieldName = "mustMslEffDate";
				final Date mslEffDateObj = SDF.parse(record.getMustMslEffDate());
				final String musmMslDateStr = SDF.format(musmDao.getMusmMslEffDate());
				// convert back to date
				final Date msmmMuslDate = SDF.parse(musmMslDateStr);
				if (!mslEffDateObj.equals(msmmMuslDate)) {
					errorConstantList.add(MSLEFFDATE_IS_NOT_EQUALS);
				}
			}
			if (StringUtils.isBlank(record.getMustRecallDate())) {
				errorConstantList.add(MSL_RECALLDATE_IS_REQUIRED);
			} else {
				fieldName = "mustRecallDate";
				final Date mslRecallDateObj = SDF.parse(record.getMustRecallDate());
				final String musmRecallDateStr = SDF.format(musmDao.getMusmRecallDate());
				// convert back to date
				final Date musmRecallDate = SDF.parse(musmRecallDateStr);
				if (!mslRecallDateObj.equals(musmRecallDate)) {
					errorConstantList.add(RECALLDATE_IS_NOT_EQUALS);
				}
			}
			if (StringUtils.isNotBlank(musmDao.getMusmDiInd())	&& !musmDao.getMusmDiInd().equalsIgnoreCase(record.getMustDiInd())
					|| StringUtils.isBlank(musmDao.getMusmDiInd()) && StringUtils.isNotBlank(record.getMustDiInd())) {
				errorConstantList.add(DI_IND_IS_NOT_EQUALS);
			}
		} catch (Exception e) {
			throw new FileProcessingException(String.format(
					"Error while validation staging data field : %s , Staging Object : " + record.toString(), fieldName),
					fieldName, e);
		}
		return errorConstantList;
	}
}