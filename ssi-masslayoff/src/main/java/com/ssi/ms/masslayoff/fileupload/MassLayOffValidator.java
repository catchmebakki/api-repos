package com.ssi.ms.masslayoff.fileupload;

import com.ssi.ms.common.service.ParameterParService;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MassLayoffStatusCd;
import com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail;
import com.ssi.ms.masslayoff.database.dao.EmployerEmpDAO;
import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.database.repository.EmployerEmpRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslReferenceListMlrlRepository;
import com.ssi.ms.masslayoff.dto.CloneMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.EditMassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MassLayOffReqDTO;
import com.ssi.ms.masslayoff.dto.MslRefListMlrlResDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;

import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.ADDMSL_MSLNO_ALREADY_IN_MASSLAYOFF;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_EMPACCLOC_NOT_IN_NHUIS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_EMPACCNUMBER_NOT_IN_NHUIS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_FINAL_UPLOAD_NOT_ALLOWED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLDATE_INVALID_FUTURE_DATE;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLDATE_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLEFFDATE_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLEFFDATE_SUNDAY;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLEFFDATE_WEEKDAY;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLNUMBER_IS_NUMERIC_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_MSLNUMBER_IS_REQUIRED;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_PENDING_CLIAMANTS_EXISTS;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_RECALLDATE_IS_INVALID;
import static com.ssi.ms.masslayoff.constant.FileuploadFieldErrorConstant.UploadFieldErrorDetail.MSL_RECALLDATE_IS_REQUIRED;

/**
 ************************************************************************************************
 *                     Modification Log                                                        *
 ************************************************************************************************
 *
 *  Date            Developer           Defect  Description of Change
 *  ----------      ---------           ------  ---------------------
 *	12/22/2023		Sitaram				SV225652 - UE-231222-Remove the restriction on the Mass Layoff Date
 **
 ************************************************************************************************
 */

/**
 * @author munirathnam.surepalli
 * A component class responsible for validating mass layoff data.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class MassLayOffValidator {
	@Autowired
	private EmployerEmpRepository employerEmpRepository;
	@Autowired
	private ParameterParService parameterParService;
	@Autowired
	private MslReferenceListMlrlRepository mslReferenceListMlrlRepository;
	@Value("${parShortName:MAX_MONTHS_MSL_DT}")
	private String parShortName;
	/**
	 * BiPredicate for checking if a duplicate MSL reference list entry exists while updating an existing entry.
	 * Suppresses PMD warning for collapsible if statements.
	 */
	@SuppressWarnings("PMD.CollapsibleIfStatements")
	private BiPredicate<List<MslRefListMlrlDAO>, Long> mslDuplicateWhileupdateMlrl = (mlrlDaos, mlrlId) -> {
		boolean returnFlag = false;
		if (CollectionUtils.isNotEmpty(mlrlDaos)) {
			if (mlrlDaos.size() > 1 || mlrlDaos.size() > 0
					&& !UtilFunction.compareLongObject.test(mlrlDaos.get(0).getMlrlId(), mlrlId)) {
				returnFlag = true;
			}
		}
		return returnFlag;
	};
	/**
	 * Validates the data in the provided CloneMassLayOffReqDTO object.
	 *
	 * @param massLayOffReqDTO {@link CloneMassLayOffReqDTO} The CloneMassLayOffReqDTO containing data to be validated.
	 * @return {@link HashMap<String, List<String>>} A HashMap where keys represent validation error fields and
	 * values are lists of corresponding error messages.
	 */
	public HashMap<String, List<String>> validateCloneMassLayOff(CloneMassLayOffReqDTO massLayOffReqDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		List<UploadFieldErrorDetail> errorEnums;
		errorEnums = validateMslDate(massLayOffReqDTO.getMassLayOffDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateMslEffDate(massLayOffReqDTO.getMassLayOffDate(), massLayOffReqDTO.getMslEffectiveDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateRecallDate(massLayOffReqDTO.getMassLayOffDate(), massLayOffReqDTO.getRecallDate());
		updateErrorMap(errorMap, errorEnums);
		/*errorEnums = validateMslNum(massLayOffReqDTO.getMassLayOffNo(), massLayOffReqDTO.getMassLayOffNo(), false);
		updateErrorMap(errorMap, errorEnums);*/
		return errorMap;
	}
	/**
	 * Validates the data in the provided MassLayOffReqDTO object.
	 *
	 * @param massLayOffReqDTO {@link MassLayOffReqDTO} The MassLayOffReqDTO containing data to be validated.
	 * @return {@link HashMap<String, List<String>>} A HashMap where keys represent validation error fields and
	 * values are lists of corresponding error messages.
	 */
	public HashMap<String, List<String>> validateMassLayOff(MassLayOffReqDTO massLayOffReqDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		List<UploadFieldErrorDetail> errorEnums;
		errorEnums = validateEmployerDetails(massLayOffReqDTO.getUiAccountNo(), massLayOffReqDTO.getUnit());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateMslDate(massLayOffReqDTO.getMassLayOffDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateMslEffDate(massLayOffReqDTO.getMassLayOffDate(), massLayOffReqDTO.getMslEffectiveDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateRecallDate(massLayOffReqDTO.getMassLayOffDate(), massLayOffReqDTO.getRecallDate());
		updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
	/**
	 * Validates the data in the provided EditMassLayOffReqDTO object.
	 *
	 * @param EditMassLayOffReqDTO {@link EditMassLayOffReqDTO} The EditMassLayOffReqDTO containing data to be validated.
	 * @return {@link HashMap<String, List<String>>} A HashMap where keys represent validation error fields and values are
	 * lists of corresponding error messages.
	 */
	public HashMap<String, List<String>> validateUpdateMassLayOff(EditMassLayOffReqDTO editMassLayOffReqDTO,
			MslRefListMlrlResDTO massLayOffResDTO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		List<UploadFieldErrorDetail> errorEnums;
		errorEnums = validateMslDate(editMassLayOffReqDTO.getMassLayOffDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateMslEffDate(editMassLayOffReqDTO.getMassLayOffDate(),
				editMassLayOffReqDTO.getMslEffectiveDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateRecallDate(editMassLayOffReqDTO.getMassLayOffDate(), editMassLayOffReqDTO.getRecallDate());
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateMslNum(editMassLayOffReqDTO.getMassLayOffNo(), editMassLayOffReqDTO.getMlrlId(), true);
		updateErrorMap(errorMap, errorEnums);
		errorEnums = validateStatusCdValue(editMassLayOffReqDTO, massLayOffResDTO);
		updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateDirectUpdateMassLayOff(MslRefListMlrlDAO mlrlDAO) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		if (UtilFunction.CompareIntegerObject.test(mlrlDAO.getMlrlStatusCd(), MassLayoffStatusCd.FINAL.getCode())) {
			updateErrorMap(errorMap, List.of(MSL_FINAL_UPLOAD_NOT_ALLOWED));
		}
		return errorMap;
	}
	/**
	 * Validates the Mass Layoff Number (MslNum) for a specific scenario.
	 *
	 * @param massLayOffNo {@link Long} The Mass Layoff Number to be validated.
	 * @param mlrlId {@link Long}      The Mass Layoff Request Log ID associated with the validation.
	 * @param isEdit {@link boolean}      A boolean indicating whether the validation is for an edit scenario.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	public List<UploadFieldErrorDetail> validateMslNum(Long massLayOffNo, Long mlrlId, boolean isEdit) {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		if (massLayOffNo == null) {
			errorConstantList.add(MSL_MSLNUMBER_IS_REQUIRED);
		} else if (!StringUtils.isNumeric(String.valueOf(massLayOffNo))) {
			errorConstantList.add(MSL_MSLNUMBER_IS_NUMERIC_REQUIRED);
		}
		final List<MslRefListMlrlDAO> mslRefListMlrlDaos = mslReferenceListMlrlRepository
				.findByMlrlMslNum(massLayOffNo);
		if (isEdit) {
			if (mslDuplicateWhileupdateMlrl.test(mslRefListMlrlDaos, mlrlId)) {
				errorConstantList.add(ADDMSL_MSLNO_ALREADY_IN_MASSLAYOFF);
			}
		} else {
			if (CollectionUtils.isNotEmpty(mslRefListMlrlDaos) && mslRefListMlrlDaos.size() > 0) {
				errorConstantList.add(ADDMSL_MSLNO_ALREADY_IN_MASSLAYOFF);
			}
		}
		return errorConstantList;
	}
	/**
	 * Validates employer details, including employer account number and location.
	 *
	 * @param empAccNbr {@link String} The employer account number to be validated.
	 * @param empAccLoc {@link String} The employer account location to be validated.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	public List<UploadFieldErrorDetail> validateEmployerDetails(String empAccNbr, String empAccLoc) {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		final List<EmployerEmpDAO> empDaos = employerEmpRepository.getEmployerByAccNbrAndKillDate(empAccNbr);
		if (CollectionUtils.isEmpty(empDaos)) {
			errorConstantList.add(MSL_EMPACCNUMBER_NOT_IN_NHUIS);
		} else if (!empDaos.stream().anyMatch(dao -> empAccLoc.equalsIgnoreCase(dao.getEmpUiAcctLoc()))) {
			errorConstantList.add(MSL_EMPACCLOC_NOT_IN_NHUIS);
		}
		return errorConstantList;
	}
	/**
	 * Validates the Mass Layoff Date.
	 *
	 * @param mslDate {@link Date} The Mass Layoff Date to be validated.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	public List<UploadFieldErrorDetail> validateMslDate(Date mslDate) {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		if (mslDate == null) {
			errorConstantList.add(MSL_MSLDATE_IS_REQUIRED);
		} else {
			final LocalDate currentDate = Instant.ofEpochMilli(parameterParService.getDBDate().getTime())
					.atZone(ZoneId.systemDefault()).toLocalDate();
			final LocalDate input = Instant.ofEpochMilli(mslDate.getTime()).atZone(ZoneId.systemDefault())
					.toLocalDate();
			/* SV225652 Start
			//The Mass Layoff date cannot be earlier than the previous Sunday date.
			final LocalDate previousSunday = currentDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
			if (input.isBefore(previousSunday)) {
				errorConstantList.add(MSL_MSLDATE_INVALID_PAST_DATE);
			}
			SV225652 End*/
			// Check if the date is not later than 2 months in the future
			final Long maxMonthsMslDT = parameterParService.getLongParamValue(parShortName);
			final LocalDate maxDate = currentDate.plusMonths(maxMonthsMslDT);
			if (input.isAfter(maxDate)) {
				errorConstantList.add(MSL_MSLDATE_INVALID_FUTURE_DATE);
			}
		}
		return errorConstantList;
	}
	/**
	 * Validates the Mass Layoff Effective Date in relation to the Mass Layoff Date.
	 *
	 * @param mslDate {@link Date}    The Mass Layoff Date.
	 * @param mslEffDate {@link Date} The Mass Layoff Effective Date to be validated.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	public List<UploadFieldErrorDetail> validateMslEffDate(Date mslDate, Date mslEffDate) {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		if (mslEffDate == null) {
			errorConstantList.add(MSL_MSLEFFDATE_IS_REQUIRED);
		} else {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(mslDate);
			final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.SUNDAY) {
				if (!mslDate.equals(mslEffDate)) {
					errorConstantList.add(MSL_MSLEFFDATE_SUNDAY);
				}
			} else {
				final LocalDate localMslDate = Instant.ofEpochMilli(mslDate.getTime()).atZone(ZoneId.systemDefault())
						.toLocalDate();
				final LocalDate localMslEffDate = Instant.ofEpochMilli(mslEffDate.getTime())
						.atZone(ZoneId.systemDefault()).toLocalDate();

				final LocalDate previousSunday = localMslDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
				final LocalDate nextSunday = localMslDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

				if (!(localMslEffDate.equals(previousSunday) || localMslEffDate.equals(nextSunday))) {
					errorConstantList.add(MSL_MSLEFFDATE_WEEKDAY);
				}
			}
		}
		return errorConstantList;
	}
	/**
	 * Validates the Recall Date in relation to the Mass Layoff Date.
	 *
	 * @param mslDate  {@link Date}   The Mass Layoff Date.
	 * @param recallDate {@link Date} The Recall Date to be validated.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	public List<UploadFieldErrorDetail> validateRecallDate(Date mslDate, Date recallDate) {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		if (recallDate == null) {
			errorConstantList.add(MSL_RECALLDATE_IS_REQUIRED);
		} else if (mslDate == null || recallDate.before(mslDate)) {
			errorConstantList.add(MSL_RECALLDATE_IS_INVALID);
		}
		return errorConstantList;
	}
	/**
	 * Validates the status code value in the context of editing a Mass Layoff request.
	 *
	 * @param editMassLayOffReqDTO {@link EditMassLayOffReqDTO} The EditMassLayOffReqDTO containing data to be validated.
	 * @param mslRefListMlrlDAO {@link MslRefListMlrlResDTO}   The MslRefListMlrlResDTO object providing reference data for validation.
	 * @return {@link List<UploadFieldErrorDetail>} A list of UploadFieldErrorDetail constants representing validation errors, if any.
	 */
	private List<UploadFieldErrorDetail> validateStatusCdValue(EditMassLayOffReqDTO editMassLayOffReqDTO,
			MslRefListMlrlResDTO mslRefListMlrlDAO) {
		final var errorConstantList = new ArrayList<UploadFieldErrorDetail>();
		if (AlvMassLayoffEnumConstant.MassLayoffStatusCd.FINAL.getCode()
				.equals(MassLayoffStatusCd.valueOf(editMassLayOffReqDTO.getStatusCdValue()).getCode())
				&& mslRefListMlrlDAO.getNoOfPendingClaimants() > 0) {
			errorConstantList.add(MSL_PENDING_CLIAMANTS_EXISTS);
		}
		return errorConstantList;
	}
	/**
	 * Updates the provided error map with new error details from the given list of error enums.
	 *
	 * @param errorMap {@link HashMap<String, List<String>>}   The error map to be updated.
	 * @param errorEnums {@link List<UploadFieldErrorDetail>} The list of UploadFieldErrorDetail constants representing validation errors.
	 */
	private void updateErrorMap(HashMap<String, List<String>> errorMap, List<UploadFieldErrorDetail> errorEnums) {
		for (final var errorEnum : errorEnums) {
			errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<>());
			errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
					.add(errorEnum.getFrontendErrorCode());
		}
	}
}
