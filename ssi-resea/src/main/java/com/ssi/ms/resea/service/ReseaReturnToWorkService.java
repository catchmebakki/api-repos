package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.ReseaReturnToWorkRsrwDAO;
import com.ssi.ms.resea.dto.ReturnToWorkReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.print.DocFlavor;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

import static com.ssi.ms.platform.util.DateUtil.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.util.ReseaUtilFunction.*;

/**
 * {@code ReseaReturnToWorkService} is a service component in the application responsible for
 * handling business logic related to RTW functionality.
 * This service performs operation to save the RTW details.
 *
 * @author Anand
 */

@Service
@Slf4j
public class ReseaReturnToWorkService extends ReseaBaseService {

	@Autowired
	EntityManager entityManager;
	@Autowired
	ReseaAppointmentService apptService;

	public ReseaReturnToWorkService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	/**
	 * Save RTW details and update RSIC record
	 *
	 *  @return String
	 */
	@Transactional
	public String saveReturnToWork(ReturnToWorkReqDTO rtwReqDTO, String userId) {
		Date systemDate = commonRepo.getCurrentDate();
		Timestamp timestamp = commonRepo.getCurrentTimestamp();
		long parNumericValue;
		ParameterParDao parameterParDao = parRepo.findByParShortName(ReseaConstants.PAR_RESEA_RTW_FUT_DAYS);
		parNumericValue = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;
		final ReseaIntvwerCalRsicDAO rsicDao = rsicRepo.findById(rtwReqDTO.getEventId())
				.orElseThrow(() -> new NotFoundException("Invalid RSIC ID:" + rtwReqDTO.getEventId(), "getRsicId.notFound"));
		final HashMap<String, List<String>> errorMap = rtwValidator.validateReturnToWorkDetails(rtwReqDTO,systemDate, parNumericValue);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException(ErrorMessageConstant.SAVE_RTW_VALIDATION_FAILED, errorMap);
		}
		ReseaReturnToWorkRsrwDAO rsrwDao = rtwMapper.dtoToDao(rtwReqDTO);
		rsrwDao.setRsrwNewEmpStartDt(localDateToDate.apply(rtwReqDTO.getEmploymentStartDt()));
		rsrwDao.setRsrwProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());
		setLogFieldsForCreatedFields(rsrwDao, userId, timestamp);
		setLogFieldsForLastUpdByFields(rsrwDao, userId, timestamp);
		setRtwConfirmedDetails(rtwReqDTO, systemDate, timestamp, rsrwDao, userId);
		Long rsrwId = rtwRsrwRepo.save(rsrwDao).getRsrwId();
		ReseaCaseRscsDAO rscsDAO = rsicDao.getRscsDAO();
		ReseaIntvwerCalRsicDAO rsicObj = rsrwDao.getRsicDao();
		/*if(null !=  rsicObj ) {
			rscsDAO = rsicRepo.getCaseDetailsByRsicId(rsicObj.getRsicId());
		}*/
		Long rscsId = rscsDAO.getRscsId();
		Long rscsStageCdAlv = rscsDAO.getRscsStageCdALV().getAlvId();
		boolean rtwForFutureAppt =  updateRsic(rsicDao, rtwReqDTO, timestamp, userId, ReseaConstants.RTW_BY_USING, systemDate);
		if (INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd())) {
			rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
			rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
			rscsDAO.setRscsOnWaitlistDt(null);
			rscsDAO.setRscsOnWaitlistClearDt(systemDate);
			rscsDAO.setRscsLastUpdBy(userId);
			rscsDAO.setRscsLastUpdUsing(ReseaConstants.RTW_BY_USING);
			rscsRepo.save(rscsDAO);
			apptService.createActivityForWaitList(rsicDao, rscsDAO,
					RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDao.getRsicTimeslotUsageCdAlv().getAlvId()),
						null, false, timestamp, true, ReseaConstants.RTW_BY_USING, false);
		}
		boolean createRTWActivityFlag = createActivityForRTW(rtwReqDTO, userId, rsrwId, rscsId, rscsStageCdAlv, timestamp, systemDate, rsrwDao, rsicDao, rtwForFutureAppt);
		return createRTWActivityFlag == true ? ReseaConstants.RTW_SAVED : ReseaConstants.REQUEST_FAILED_MSG;
	}

	/**
	 * Create Activity for RTW (Activity type : 5584);
	 * @return
	 */
	private boolean createActivityForRTW(ReturnToWorkReqDTO rtwReqDTO, String userId, Long rsrwId, Long rscsId, Long rscsStageCdAlv,Timestamp timestamp,
										 Date systemDate, ReseaReturnToWorkRsrwDAO rsrwDao, ReseaIntvwerCalRsicDAO rsicDao, boolean rtwForFutureAppt ) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		boolean createRTWActivityFlag = false;
		//try {
			Map<String, Object> createActivity = createActivity(rsrwId, rtwReqDTO, rscsId, rscsStageCdAlv, timestamp, systemDate, rsicDao, rtwForFutureAppt);
			if(createActivity != null) {
				Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
				Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
				Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
				String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

				if(spPoutSuccess == 0L && null != spPoutRscaId) {
					rsrwDao.setFkRscaId(Long.valueOf(spPoutRscaId));
					setLogFieldsForLastUpdByFields(rsrwDao, userId, timestamp);
					rsrwId = rtwRsrwRepo.save(rsrwDao).getRsrwId();
					createRTWActivityFlag = true;
				}
				else if(spPoutSuccess == 1L && null != spPoutNhlId) {
					errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
					errorParams.add(String.valueOf(spPoutNhlId));
					ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
					if (!errorMap.isEmpty()) {
						throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
					}
					createRTWActivityFlag = false;
				}
				else if(spPoutSuccess == 2L && null != spPoutNhlId) {
					errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
					errorParams.add(String.valueOf(spPoutNhlId));
					ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
					if (!errorMap.isEmpty()) {
						throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
					}
					createRTWActivityFlag = false;
				}
				else if(spPoutSuccess < 0L && null != spPoutNhlId) {
					errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
					errorParams.add(String.valueOf(spPoutNhlId));
					ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
					if (!errorMap.isEmpty()) {
						throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
					}
					createRTWActivityFlag = false;
				}
			}
			else {
				createRTWActivityFlag = false;
			}
		/*}
		catch (Exception e) {
			createRTWActivityFlag = false;
			log.error("Error in calling the stored procedure CREATE_ACTIVITY for RTW type "
					+Arrays.toString(e.getStackTrace()));
		}*/
		return createRTWActivityFlag;
	}

	/**
	 * Set Confirmation details for RTW, if provided employment start date is in PAST date
	 */
	private void setRtwConfirmedDetails(ReturnToWorkReqDTO returnToWorkReqDTO, Date systemDate, Timestamp timestamp, ReseaReturnToWorkRsrwDAO rsrwDao, String userId) {
		if (localDateToDate.apply(returnToWorkReqDTO.getEmploymentStartDt()).before(systemDate)) {
			List<String> checkedJmsCompletedCheckListItems = Arrays.asList(returnToWorkReqDTO.getJmsCloseGoalsInd(),
					returnToWorkReqDTO.getJmsCloseIEPInd(), returnToWorkReqDTO.getJmsCaseNotesInd(),
					returnToWorkReqDTO.getJmsResumeOffInd(), returnToWorkReqDTO.getEpChecklistUploadInd());
			boolean containsY = checkedJmsCompletedCheckListItems.stream().allMatch(item -> item.contains("Y"));
			if(containsY) {
				rsrwDao.setRsrwConFirmedTs(timestamp);
				rsrwDao.setRsrwConFirmedBy(Long.valueOf(userId));
				rsrwDao.setRsrwConfimedInd(INDICATOR.Y.toString());
			}
		}
	}

	private ReseaReturnToWorkRsrwDAO setLogFieldsForCreatedFields(ReseaReturnToWorkRsrwDAO rsrwDao, String userId, Timestamp timestamp) {
		rsrwDao.setRsrwCreatedBy(userId);
		rsrwDao.setRsrwCreatedUsing(ReseaConstants.RTW_BY_USING);
		rsrwDao.setRsrwCreatedTs(timestamp);
		return rsrwDao;
	}
	private ReseaReturnToWorkRsrwDAO setLogFieldsForLastUpdByFields(ReseaReturnToWorkRsrwDAO rsrwDao, String userId, Timestamp timestamp) {
		rsrwDao.setRsrwLastUpdBy(userId);
		rsrwDao.setRsrwLastUpdUsing(ReseaConstants.RTW_BY_USING);
		rsrwDao.setRsrwLastUpdTs(timestamp);
		return rsrwDao;
	}

	private Map<String, Object> createActivity(Long rsrwId, ReturnToWorkReqDTO returnToWorkReqDTO, Long rscsId, Long rscsStagecdAlv,
											   Timestamp timestamp, Date systemDate, ReseaIntvwerCalRsicDAO rsicDao, boolean rtwForFutureAppt) {
		Map<String, Object> createActivity = null;
		//Long rscaStatusCdAlv = null;
		String showInNhuisInd = null;
		try {
			if(null != rsrwId && null != rscsId && rsicDao != null) {
				/*if (ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED_RTW.getCode() == rsicDao.getRsicMtgStatusCdAlv().getAlvId()) {
					rscaStatusCdAlv = ReseaAlvEnumConstant.RscaStatusCd.FAILED_RTW.getCode();
				}
				else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED_RTW.getCode() == rsicDao.getRsicMtgStatusCdAlv().getAlvId()) {
					rscaStatusCdAlv = ReseaAlvEnumConstant.RscaStatusCd.COMPLETED_RTW.getCode();
				}*/
				String rscaDetails = "Claimant states that they have returned to work. The employment details provided are as follows:\n" +
						"Company: " + returnToWorkReqDTO.getEmpName() + " located in " + returnToWorkReqDTO.getEmpWorkLocCity() + ", " + returnToWorkReqDTO.getEmpWorkLocState() + ".\n" +
						"Job Title: " + returnToWorkReqDTO.getExactJobTitle() + "\n" +
						"Start Date: " + localDateToString.apply(returnToWorkReqDTO.getEmploymentStartDt()) + "\n" +
						"Pay Rate: " + returnToWorkReqDTO.getHourlyPayRate() + " /hour\n" +
						"Schedule: " + getWorkScheduleDetails(returnToWorkReqDTO) + " - " + getWorkModeDetails(returnToWorkReqDTO);

				String rscnNoteCategory = null;
				String rscnNote = null;

				if(StringUtils.isNotBlank(returnToWorkReqDTO.getStaffNotes())) {
					rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDao.getRsicTimeslotUsageCdAlv().getAlvId(),
							rscsStagecdAlv));
					rscnNote = returnToWorkReqDTO.getStaffNotes();
					showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
				}
				else {
					rscnNoteCategory = StringUtils.EMPTY;
					rscnNote = StringUtils.EMPTY;
					showInNhuisInd = StringUtils.EMPTY;
				}

				if(returnToWorkReqDTO.isIncludeThisNoteInCNO() && StringUtils.isNotBlank(returnToWorkReqDTO.getStaffNotes())) {
					showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
				}

				Long rscaStatusCd = 0L;
				if(rtwForFutureAppt) {
					rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.RETURNED_TO_WORK.getCode();
				}
				else {
					if(null != rsicDao.getRsicMtgStatusCdAlv()) {
						rscaStatusCd = getRscaStatusFromRsicMtgStatus(rsicDao.getRsicMtgStatusCdAlv().getAlvId());
					}
				}

				entityManager.flush();

				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						getRscaStageFromRsicTimeslotUsage(rsicDao.getRsicTimeslotUsageCdAlv().getAlvId()),
						rscaStatusCd,
						//getRscaStageFromRscsStage(rscsStagecdAlv),
						//rscaStatusCdAlv,
						ReseaAlvEnumConstant.RscaTypeCd.RETURNED_TO_WORK.getCode(),
						timestamp,
						ReseaConstants.RSCA_DESC_RTW,
						ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
						"--",
						0L, 0L, stringToDate.apply(DATE_01_01_2000),
						StringUtils.EMPTY, StringUtils.EMPTY,
						rscaDetails,
						ReseaAlvEnumConstant.RscaReferenceIfkCd.RSRW.getCode(),
						rsrwId,
						rscnNoteCategory,
						rscnNote,
						showInNhuisInd,
						ReseaConstants.RSCA_CALLING_PROGRAM_RTW
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for RTW Activity type (5584) associated with " +
					"RSIC ID "+ returnToWorkReqDTO.getEventId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}

	private String getWorkModeDetails(ReturnToWorkReqDTO dto) {
		String workModeDesc = null;
		if (dto.getWorkMode() != null) {
			if (ReseaAlvEnumConstant.RsrwWorkModeCd.HYBRID.getCode().intValue() == dto.getWorkMode()) {
				workModeDesc = ReseaAlvEnumConstant.RsrwWorkModeCd.HYBRID.getDescription();
			} else if (ReseaAlvEnumConstant.RsrwWorkModeCd.ONSITE_ONLY.getCode().intValue() == dto.getWorkMode()) {
				workModeDesc = ReseaAlvEnumConstant.RsrwWorkModeCd.ONSITE_ONLY.getDescription();
			} else {
				workModeDesc = ReseaAlvEnumConstant.RsrwWorkModeCd.REMOTE_ONLY.getDescription();
			}
		}
		return workModeDesc;
	}

	private String getWorkScheduleDetails(ReturnToWorkReqDTO dto) {
		String workScheduleDesc = dto.getPartFullTimeInd().equalsIgnoreCase(WORK_SCHEDULE.FULL_TIME.getCode()) ? WORK_SCHEDULE.FULL_TIME.getDescription() :
				WORK_SCHEDULE.PART_TIME.getDescription();
		return workScheduleDesc;
	}

	/**
	 * Update RSIC record with meeting status as
	 */
	@Transactional
	private boolean updateRsic(ReseaIntvwerCalRsicDAO rsicDao, ReturnToWorkReqDTO returnToWorkReqDTO,
									  Timestamp timestamp, String userId, String updatedUsing, Date systemDate ) {
		boolean rtwForFutureAppt = false;
		AllowValAlvDAO rsicCalEventTypeCdForAvailable = alvRepo.findById(ReseaAlvEnumConstant.RsicCalEventTypeCd.AVAILABLE.getCode()).orElseThrow();
		AllowValAlvDAO failedRtwMtgStatus = alvRepo.findById(ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED_RTW.getCode()).orElseThrow();
		AllowValAlvDAO completedRtwMtgStatus = alvRepo.findById(ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED_RTW.getCode()).orElseThrow();
		AllowValAlvDAO noticeStatusNotApplicable = alvRepo.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.NA.getCode()).orElseThrow();
		String[] rsicCalEventStTimeSplit = rsicDao.getRsicCalEventStTime().split(":");
		LocalTime currentTime = LocalTime.now();
		LocalTime rsicCalEventStTime = LocalTime.of(Integer.valueOf(rsicCalEventStTimeSplit[0]), Integer.valueOf(rsicCalEventStTimeSplit[1]));

		if (ReseaAlvEnumConstant.RsicMeetingStatusCd.SCHEDULED.getCode().longValue() == rsicDao.getRsicMtgStatusCdAlv().getAlvId()
				|| ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED.getCode().longValue() == rsicDao.getRsicMtgStatusCdAlv().getAlvId()) {
			rsicDao.setRsicMtgStatusCdAlv(completedRtwMtgStatus);
		}
		else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED.getCode().longValue() == rsicDao.getRsicMtgStatusCdAlv().getAlvId()) {
			rsicDao.setRsicMtgStatusCdAlv(failedRtwMtgStatus);
		}
		rsicDao.setRsicMtgStatusUpdTs(timestamp);
		rsicDao.setRsicMtgStatusUpdBy(Long.valueOf(userId));

		if(null != rsicDao && (
				(checkFutureDate.test(rsicDao.getRsicCalEventDt(), systemDate))
		       || (rsicDao.getRsicCalEventDt().equals(systemDate) && rsicCalEventStTime.isAfter(currentTime))) ) {
			rsicDao.setClaimDAO(null);
			rsicDao.setRscsDAO(null);
			rsicDao.setRsicCalEventTypeCdAlv(rsicCalEventTypeCdForAvailable);
			rsicDao.setRsicInSchWindowInd(ReseaConstants.INDICATOR.N.toString());
			rsicDao.setRsicLateSchNotes(null);
			rsicDao.setRsicScheduledOnTs(null);
			rsicDao.setRsicScheduledByCdAlv(null);
			rsicDao.setRsicScheduledByUsr(null);
			rsicDao.setRsicMtgModeInd(null);
			rsicDao.setRsicModeChgRsnCdAlv(null);
			rsicDao.setRsicModeChgRsnTxt(null);
			rsicDao.setRsicMtgmodeSwchCnt(0);
			rsicDao.setRsicMtgReschCnt(0);
			rsicDao.setRsicStaffNotes(null);
			rsicDao.setRsicTimeslotSysNotes(StringUtils.trimToEmpty(dateToString.apply(rsicDao.getRsicCreatedTs()) +":" + " Create session"));
			rsicDao.setRsicNoticeStatusCdAlv(noticeStatusNotApplicable);
			rsicDao.setRsicProcStatusInd(ReseaConstants.PROCESS_STATUS.P.toString());
			rsicDao.setRsicMtgStatusCdAlv(null);
			rsicDao.setRsicMtgStatusUpdTs(null);
			rsicDao.setRsicMtgStatusUpdBy(null);
			rtwForFutureAppt = true;
		}
		rsicDao.setRsicLastUpdBy(userId);
		rsicDao.setRsicLastUpdUsing(updatedUsing);
		rsicRepo.save(rsicDao);
		return rtwForFutureAppt;
	}

	/**
	 * View-only details for RTW details
	 *
	 *  @return ReturnToWorkReqDTO
	 */
	@Transactional
	public ReturnToWorkReqDTO viewReturnToWork(Long eventId) {
		ReturnToWorkReqDTO rtwReqDTO =  null;
		final ReseaIntvwerCalRsicDAO rsicDao = rsicRepo.findById(eventId)
				.orElseThrow(() -> new NotFoundException("Invalid RSIC ID:" + eventId, "getRsicId.notFound"));
		final ReseaReturnToWorkRsrwDAO rsrwDao = rsicDao.getRsrwDao();
		if (rsrwDao != null) {
			rtwReqDTO =  new ReturnToWorkReqDTO()
						.withRtwMode(RTW_VIEW_MODE)
					    .withEmpName(rsrwDao.getRsrwEmpName())
						.withEmploymentStartDt(dateToLocalDate.apply(rsrwDao.getRsrwNewEmpStartDt()))
						.withEmpWorkLocCity(rsrwDao.getRsrwEmpWorkLocCity())
						.withEmpWorkLocState(rsrwDao.getRsrwEmpWorkLocSt())
						.withExactJobTitle(rsrwDao.getRsrwExactJobTitle())
						.withHourlyPayRate(rsrwDao.getRsrwHourlyPayRate())
						.withWorkMode(rsrwDao.getRsrwWorkModeCdAlv().getAlvId())
						.withPartFullTimeInd(rsrwDao.getRsrwPtFtInd())
						.withStaffNotes(rsrwDao.getRsrwStaffNotes())
						.withJms890Ind(rsrwDao.getRsrwJms890Ind())
						.withJmsReferralInd(rsrwDao.getRsrwJmsRfrlHiredInd())
						.withJmsCloseGoalsInd(rsrwDao.getRsrwJmsClseGoalsInd())
						.withJmsCloseIEPInd(rsrwDao.getRsrwJmsClseIepInd())
						.withJmsCaseNotesInd(rsrwDao.getRsrwJmsCaseNotesInd())
						.withJmsResumeOffInd(rsrwDao.getRsrwJmsResumeOffInd())
						.withEpChecklistUploadInd(rsrwDao.getRsrwEpChklstUpldInd());
		}
		else {
			rtwReqDTO =  new ReturnToWorkReqDTO().withRtwMode(RTW_INSERT_MODE);

			Date systemDate = commonRepo.getCurrentDate();
			String[] rsicCalEventStTimeSplit = rsicDao.getRsicCalEventStTime().split(":");
			LocalTime currentTime = LocalTime.now();
			LocalTime rsicCalEventStTime = LocalTime.of(Integer.valueOf(rsicCalEventStTimeSplit[0]), Integer.valueOf(rsicCalEventStTimeSplit[1]));
			if(null != rsicDao && (
					(checkFutureDate.test(rsicDao.getRsicCalEventDt(), systemDate))
							|| (rsicDao.getRsicCalEventDt().equals(systemDate) && rsicCalEventStTime.isAfter(currentTime))) ) {
				rtwReqDTO =  rtwReqDTO.withFutureRtw(true);
			}


		}
		return rtwReqDTO;
	}

}