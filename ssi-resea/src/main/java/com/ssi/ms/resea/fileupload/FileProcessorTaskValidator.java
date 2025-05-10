package com.ssi.ms.resea.fileupload;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchDtlsRucdDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchErrorRuseDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchStagingRussDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ALLOW_ONSITE_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ALLOW_REMOTE_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.APPT_TIME_OVERLAP;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.DAY_OF_WEEK_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.END_TIME_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.RESEA_APPT_TIMEFRAME_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.RESEA_APPT_TIMEFRAME_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.START_TIME_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_CASE_MGR_MISMATCH;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_MEETING_ID_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_PASSCODE_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_PHONE_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_TOPIC_IS_INVALID;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_TOPIC_IS_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_LINK_URL_REQUIRED;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail.ZOOM_MEETING_DURATION_MISMATCH;
import static com.ssi.ms.resea.constant.ReseaConstants.DAIL_BY_LOCATION;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_FIRST_SUB_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_INITIAL_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_SEC_SUB_APPT_DURATION;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_MEETING_ID;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_PASSCODE;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_1ST_SUB;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_2ND_SUB;
import static com.ssi.ms.resea.constant.ReseaConstants.TOPIC_INITIAL;
import static com.ssi.ms.resea.constant.ReseaConstants.ZOOM_LINK_PASSCODE;
import static com.ssi.ms.resea.constant.ReseaConstants.ZOOM_LINK_TOPIC;
import static com.ssi.ms.resea.constant.ReseaConstants.TIME_PATTERN_24H;
import static com.ssi.ms.resea.constant.ReseaConstants.PATTERN_PHONE_NBR;

/**
 * A component class responsible for validating file processing tasks.
 */
@Component
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class FileProcessorTaskValidator {
	@Autowired
	AllowValAlvRepository alvRepository;
	@Autowired
	ParameterParRepository parRepository;
	private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);

	private BiPredicate<String, String> compareNotEqual = (str1, str2) -> StringUtils.isNotBlank(str1)
			&& !str1.equalsIgnoreCase(str2);

	/**
	 * Creates a list of RESEA upload error DAOs based on the provided list of error constants, RESEA upload staging ID,
	 * RESEA upload summary ID, and user ID.
	 *
	 * @param errorConstantList {@link List<UploadFieldErrorDetail>} The list of UploadFieldErrorDetail constants
	 * representing the specific errors.
	 * @param russDAO  {@link ReseaUploadSchStagingRussDAO}  The RESEA upload staging DAO associated with the errors.
	 * @param rusmDAO  {@link ReseaUploadSchSummaryRusmDAO}  The RESEA upload summary DAO associated with the errors.
	 * @param userId  {@link String}          The user ID associated with the error creation.
	 * @return {@link List< ReseaUploadSchErrorRuseDAO >} list of ReseaUploadSchErrorRuseDAO objects representing the created errors.
	 */
	public List<ReseaUploadSchErrorRuseDAO> createReseaUploadErrorRuseDAO(List<UploadFieldErrorDetail> errorConstantList,
																		  ReseaUploadSchStagingRussDAO russDAO,
																		  ReseaUploadSchSummaryRusmDAO rusmDAO, String userId) {
		return errorConstantList.stream()
				.map(errorEnum -> createReseaUploadErrorRuseDAO(errorEnum, russDAO, rusmDAO, userId)).toList();
	}
	/**
	 * Creates an RESEA upload error DAO for a specific error detail, associated with the provided RESEA upload staging ID,
	 * RESEA upload summary ID, and user ID.
	 *
	 * @param errorDetailEnum {@link UploadFieldErrorDetail} The error detail enumeration representing the specific error.
	 * @param russDAO  {@link Long}        The RESEA upload staging ID associated with the error.
	 * @param rusmDAO  {@link Long}        The RESEA upload summary ID associated with the error.
	 * @param userId  {@link String}        The user ID associated with the error creation.
	 * @return An instance of MslUploadErrorMuseDAO representing the created error.
	 */
	public ReseaUploadSchErrorRuseDAO createReseaUploadErrorRuseDAO(UploadFieldErrorDetail errorDetailEnum,
																	ReseaUploadSchStagingRussDAO russDAO,
																	ReseaUploadSchSummaryRusmDAO rusmDAO, String userId) {
		final ReseaUploadSchErrorRuseDAO errorDao = new ReseaUploadSchErrorRuseDAO();
		errorDao.setRussDAO(russDAO);
		errorDao.setRusmDAO(rusmDAO);
		if (errorDetailEnum != null) {
			errorDao.setRuseErrNum(errorDetailEnum.getCode());
		}
		if (errorDetailEnum != null) {
			errorDao.setRuseErrDesc(errorDetailEnum.getDescription());
		}
		errorDao.setRuseCreatedBy(userId);
		errorDao.setRuseCreatedUsing(CommonConstant.SYSTEM);
		errorDao.setRuseLastUpdBy(userId);
		errorDao.setRuseLastUpdUsing(CommonConstant.SYSTEM);
		return errorDao;
	}
	/**
	 * Validates staging data for an RESEA upload rusmDAO.
	 *
	 * @param rusmDAO {@link ReseaUploadSchSummaryRusmDAO} The RESEA upload summary DAO object.
	 * @param userId  {@link String} The user ID associated with the validation.
	 * @return {@link List< UploadFieldErrorDetail >} list of UploadFieldErrorDetail constants representing validation errors, if any.
	 * @throws Exception If an exception occurs during the validation process.
	 */
	public List<UploadFieldErrorDetail> validateStagingData(ReseaUploadSchStagingRussDAO russDAO,
															ReseaUploadSchSummaryRusmDAO rusmDAO,
															String userId) throws Exception {
		final List<UploadFieldErrorDetail> errorConstantList = new ArrayList<>();
		String fieldName = null;
		try {
			if (StringUtils.isBlank(russDAO.getRussDayOfWeek())) {
				errorConstantList.add(UploadFieldErrorDetail.RESEA_DAYOFWEEK_IS_REQUIRED);
				fieldName = UploadFieldErrorDetail.RESEA_DAYOFWEEK_IS_REQUIRED.getFrontendField();
			}
			if (StringUtils.isBlank(russDAO.getRussApptTimeframe())) {
				errorConstantList.add(UploadFieldErrorDetail.RESEA_APPT_TIMEFRAME_IS_REQUIRED);
				fieldName = UploadFieldErrorDetail.RESEA_APPT_TIMEFRAME_IS_REQUIRED.getFrontendField();
			}
			if (StringUtils.isBlank(russDAO.getRussZoomLinkDetails())) {
				errorConstantList.add(UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED);
				fieldName = UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED.getFrontendField();
			}
			if (StringUtils.isBlank(russDAO.getRussAllowOnsiteInd())) {
				errorConstantList.add(UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED);
				fieldName = UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED.getFrontendField();
			}
			if (StringUtils.isBlank(russDAO.getRussAllowRemoteInd())) {
				errorConstantList.add(UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED);
				fieldName = UploadFieldErrorDetail.RESEA_ZOOM_LINK_DETAIL_IS_REQUIRED.getFrontendField();
			}
		} catch (Exception e) {
			throw new FileProcessingException(String.format(
					"Error while validation staging data field : %s , Staging Object : " + rusmDAO.toString(), fieldName),
					fieldName, e);
		}
		return errorConstantList;
	}


	public List<ReseaUploadSchErrorRuseDAO> validateSchedulesDetails(ReseaUploadSchStagingRussDAO russDAO,
																	 ReseaUploadSchSummaryRusmDAO rusmDAO,
																	 List<Long> overlappingRussIds,
																	 String caseMgrFirstName,
																	 String caseMgrLastName,
																	 String userId) {
		final List<ReseaUploadSchErrorRuseDAO> errorRuseDAOS = new ArrayList<>();
		ReseaUploadSchErrorRuseDAO errorDao;
		Short dayOfWeek = ReseaUtilFunction.convertDayOfWeek().apply(russDAO.getRussDayOfWeek());
		if (!CollectionUtils.isEmpty(overlappingRussIds) && overlappingRussIds.contains(russDAO.getRussId())) {
			errorDao = createUploadErrorRuseDAO(APPT_TIME_OVERLAP,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		}
		if (dayOfWeek < 2 || dayOfWeek > 6) {
			errorDao = createUploadErrorRuseDAO(DAY_OF_WEEK_IS_INVALID,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		}
		String meetingType = null;
		if (StringUtils.isBlank(russDAO.getRussApptTimeframe())) {
			errorDao = createUploadErrorRuseDAO(RESEA_APPT_TIMEFRAME_IS_REQUIRED,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		} else if (russDAO.getRussApptTimeframe().lastIndexOf("-") == -1) {
			errorDao = createUploadErrorRuseDAO(RESEA_APPT_TIMEFRAME_IS_INVALID,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		} else {
			final String[] apptTime = russDAO.getRussApptTimeframe().split("-");
			final String startTime = StringUtils.leftPad(apptTime[0].trim(), 5, "0");
			final String endTime = StringUtils.leftPad(apptTime[1].trim(), 5, "0");
			if (!TIME_PATTERN_24H.matcher(startTime).matches()) {
				errorDao = createUploadErrorRuseDAO(START_TIME_IS_INVALID,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			} else if (!TIME_PATTERN_24H.matcher(endTime).matches()) {
				errorDao = createUploadErrorRuseDAO(END_TIME_IS_INVALID,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			} else {
				int startTimeHr = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
				int startTimeMin = Integer.parseInt(startTime.substring(startTime.indexOf(":")+1).trim());
				int endTimeHr = Integer.parseInt(endTime.substring(0, endTime.indexOf(":")));
				int endTimeMin = Integer.parseInt(endTime.substring(endTime.indexOf(":")+1).trim());
				if (startTimeHr < 8 || startTimeHr > 16 || (startTimeHr == 8 && startTimeMin < 30)
						|| (startTimeHr == 16 && startTimeMin != 0)) {
					errorDao = createUploadErrorRuseDAO(START_TIME_IS_INVALID,
							russDAO, rusmDAO, userId);
					errorRuseDAOS.add(errorDao);
				}
				if (endTimeHr < 9 || endTimeHr > 16 || (endTimeHr == 16 && endTimeMin > 30)) {
					errorDao = createUploadErrorRuseDAO(END_TIME_IS_INVALID,
							russDAO, rusmDAO, userId);
					errorRuseDAOS.add(errorDao);
				}
				if (StringUtils.isNotBlank(russDAO.getRussZoomLinkDetails()) &&
						russDAO.getRussZoomLinkDetails().contains(ZOOM_LINK_TOPIC)) {
					long duration = ReseaUtilFunction.durationBetweenStartAndEndTime().apply(startTime, endTime);
					if (duration == parRepository.findByParShortName(PAR_RESEA_INITIAL_APPT_DURATION).getParNumericValue()) {
						meetingType = TOPIC_INITIAL;
					} else if (duration == parRepository.findByParShortName(PAR_RESEA_FIRST_SUB_APPT_DURATION).getParNumericValue()) {
						meetingType = TOPIC_1ST_SUB;
					} else if (duration == parRepository.findByParShortName(PAR_RESEA_SEC_SUB_APPT_DURATION).getParNumericValue()) {
						meetingType = TOPIC_2ND_SUB;
					} else {
						errorDao = createUploadErrorRuseDAO(ZOOM_MEETING_DURATION_MISMATCH,
								russDAO, rusmDAO, userId);
						errorRuseDAOS.add(errorDao);
					}
				}
			}
		}
		if (StringUtils.isEmpty(russDAO.getRussZoomLinkDetails())) {
			errorDao = createUploadErrorRuseDAO(ZOOM_LINK_IS_REQUIRED,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		} else {
			final String[] zoomDetailArray = russDAO.getRussZoomLinkDetails().split("\n");
			if (!zoomDetailArray[0].toUpperCase().contains(caseMgrFirstName + " " + caseMgrLastName)) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_CASE_MGR_MISMATCH,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}
			/*if (!russDAO.getRussZoomLinkDetails().contains(ZOOM_LINK_TOPIC)) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_TOPIC_IS_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}*/
			if (russDAO.getRussZoomLinkDetails().contains(ZOOM_LINK_TOPIC)) {
				final String topic = Arrays.stream(zoomDetailArray).filter(s -> s.trim().toUpperCase().startsWith(ZOOM_LINK_TOPIC.toUpperCase()))
						.findFirst().orElse("").toUpperCase();
				final String interviewType = topic.toUpperCase().trim();
				if (StringUtils.isBlank(interviewType)) {
					errorDao = createUploadErrorRuseDAO(ZOOM_LINK_TOPIC_IS_INVALID,
							russDAO, rusmDAO, userId);
					errorRuseDAOS.add(errorDao);
				} else if (meetingType != null && !interviewType.contains(meetingType)) {
					errorDao = createUploadErrorRuseDAO(ZOOM_MEETING_DURATION_MISMATCH,
							russDAO, rusmDAO, userId);
					errorRuseDAOS.add(errorDao);
				}
			}

			if (StringUtils.isBlank(Arrays.stream(zoomDetailArray).filter(s -> s.trim().toUpperCase().contains("ZOOM.US/J/"))
					.findFirst().orElse("").trim())) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_URL_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}
			if (StringUtils.isBlank(Arrays.stream(zoomDetailArray).filter(s -> PATTERN_MEETING_ID.matcher(s.toUpperCase()).find())
					.findFirst().orElse("").trim())) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_MEETING_ID_IS_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}
			/*if (StringUtils.isBlank(Arrays.stream(zoomDetailArray).filter(s -> PATTERN_PASSCODE.matcher(s.toUpperCase()).find())
					.findFirst().orElse("").trim())) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_PASSCODE_IS_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}*/
			Integer index = IntStream.range(0, zoomDetailArray.length)
					.filter(i -> zoomDetailArray[i].toUpperCase().contains(DAIL_BY_LOCATION))
					.boxed()
					.findFirst().orElse(-1);
			if (index++ < zoomDetailArray.length -1 && !PATTERN_PHONE_NBR.matcher(zoomDetailArray[index]).find()) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_PHONE_IS_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}
			/*if (StringUtils.isBlank(Arrays.stream(zoomDetailArray).filter(s -> PATTERN_PHONE_NBR.matcher(s).find())
					.findFirst().orElse("").trim())) {
				errorDao = createUploadErrorRuseDAO(ZOOM_LINK_PHONE_IS_REQUIRED,
						russDAO, rusmDAO, userId);
				errorRuseDAOS.add(errorDao);
			}*/
		}
		if (!EnumUtils.isValidEnum(ReseaConstants.INDICATOR.class, russDAO.getRussAllowOnsiteInd())) {
			errorDao = createUploadErrorRuseDAO(ALLOW_ONSITE_IS_INVALID,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		}
		if (!EnumUtils.isValidEnum(ReseaConstants.INDICATOR.class, russDAO.getRussAllowOnsiteInd())) {
			errorDao = createUploadErrorRuseDAO(ALLOW_REMOTE_IS_INVALID,
					russDAO, rusmDAO, userId);
			errorRuseDAOS.add(errorDao);
		}
		return errorRuseDAOS;
	}

	/**
	 * Creates an MSL upload error DAO for a specific error detail, associated with the provided MSL upload staging ID,
	 * MSL upload summary ID, and user ID.
	 *
	 * @param errorDetailEnum {@link UploadFieldErrorDetail} The error detail enumeration representing the specific error.
	 * @param russDAO  {@link ReseaUploadSchStagingRussDAO}        The MSL upload staging DAO associated with the error.
	 * @param rusmDAO  {@link ReseaUploadSchSummaryRusmDAO}        The MSL upload summary DAO associated with the error.
	 * @param userId  {@link String}        The user ID associated with the error creation.
	 * @return An instance of MslUploadErrorMuseDAO representing the created error.
	 */
	public ReseaUploadSchErrorRuseDAO createUploadErrorRuseDAO(UploadFieldErrorDetail errorDetailEnum, ReseaUploadSchStagingRussDAO russDAO,
															   ReseaUploadSchSummaryRusmDAO rusmDAO, String userId) {
		final ReseaUploadSchErrorRuseDAO errorDao = new ReseaUploadSchErrorRuseDAO();
		errorDao.setRusmDAO(rusmDAO);
		errorDao.setRussDAO(russDAO);
		if (errorDetailEnum != null) {
			errorDao.setRuseErrNum(errorDetailEnum.getCode());
		}
		if (errorDetailEnum != null) {
			errorDao.setRuseErrDesc(errorDetailEnum.getDescription());
		}
		errorDao.setRuseCreatedBy(userId);
		errorDao.setRuseCreatedUsing(CommonConstant.SYSTEM);
		errorDao.setRuseLastUpdBy(userId);
		errorDao.setRuseLastUpdUsing(CommonConstant.SYSTEM);
		return errorDao;
	}
}