package com.ssi.ms.resea.service;

import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.NmiConstants;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.*;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.ssi.ms.platform.util.DateUtil.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.util.ReseaUtilFunction.*;

/**
 * {@code ReseaNoShowService} is a service component in the application responsible for
 * handling business logic related to No Show functionality.
 * This service performs operations updating the No show business requirements to DB.
 *
 * @author Anand
 *
 * 10/28/2024		Anand			AnD249239 	UE-241007-RESEA Rewrite-3
 */
@Service
@Slf4j
public class ReseaNoShowService extends ReseaBaseService {
	@Autowired
	EntityManager entityManager;
	@Autowired
	ReseaAppointmentService apptService;
	public ReseaNoShowService(EntityManager entityManager) {
		this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
	}
	@Transactional
    public String saveNoShowDetails(Long rsicId, String userId) {
		final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepo.findById(rsicId)
				.orElseThrow(() -> new NotFoundException("Invalid RSIC ID:" + rsicId, "rsicId.notFound"));
		ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
		Date systemDate = commonRepo.getCurrentDate();
		Timestamp timestamp = commonRepo.getCurrentTimestamp();
		/*if(null !=  rsicDAO.getRsicId()) {
			rscsDAO = rsicRepo.getCaseDetailsByRsicId(rsicDAO.getRsicId());
		}*/
		Long rscsId = rscsDAO.getRscsId();
		Long rscsStageCdAlv = rscsDAO.getRscsStageCdALV().getAlvId();
		Long rscsStatusCdAlv = ReseaAlvEnumConstant.RscsStatusCd.FAILED.getCode();

		ReseaIssueIdentifiedRsiiDAO rsiiDao = saveRsiiData(rsicDAO, userId, ReseaConstants.NO_SHOW_BY_USING, timestamp);
		final boolean clearWaitList = INDICATOR_YES.equals(rscsDAO.getRscsOnWaitlistInd());
		if (clearWaitList) {
			rscsDAO.setRscsOnWaitlistInd(INDICATOR_NO);
			rscsDAO.setRscsOnWaitlistAutoSchInd(INDICATOR_NO);
			rscsDAO.setRscsOnWaitlistDt(null);
			rscsDAO.setRscsOnWaitlistClearDt(parRepo.getCurrentDate());
			rscsDAO.setRscsLastUpdBy(userId);
			rscsDAO.setRscsLastUpdUsing(ReseaConstants.NO_SHOW_BY_USING);
			rscsRepo.save(rscsDAO);
		}
		updateRsicForNoShow(rsicDAO, userId, rscsId, rscsStageCdAlv, rscsStatusCdAlv,timestamp, systemDate);
		if(null != rsiiDao) {
			boolean createIssueActivityFlag = createActivityForIssuesAdded(rsicDAO, userId, systemDate, timestamp, rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsiiDao);
		}
		if (clearWaitList) {
			apptService.createActivityForWaitList(rsicDAO, rscsDAO,
					RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
					null, false, timestamp, false, ReseaConstants.NO_SHOW_BY_USING, false);
		}
		boolean createNoShowActivityFlag = createActivityForNoShow(userId, rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, timestamp, systemDate);
		return createNoShowActivityFlag == true ? ReseaConstants.NO_SHOW_SAVED : ReseaConstants.CREATE_ACTIVITY_FAILED_MSG;
    }

	@Transactional
	private boolean createActivityForNoShow(String userId, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv,
										 ReseaIntvwerCalRsicDAO rsicDAO, Timestamp timestamp, Date systemDate) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		boolean createNoShowActivityFlag = false;
		Map<String, Object> createActivity = createActivityForFailed(rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, timestamp, systemDate, userId);
		if(createActivity != null) {
			Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
			Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
			Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
			String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

			if(spPoutSuccess == 0L && null != spPoutRscaId) {
				createNoShowActivityFlag = true;
			}
			else if(spPoutSuccess == 1L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
				}
				createNoShowActivityFlag = false;
			}
			else if(spPoutSuccess == 2L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
				}
				createNoShowActivityFlag = false;
			}
			else if(spPoutSuccess < 0L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
				}
				createNoShowActivityFlag = false;
			}
		}
		else {
			createNoShowActivityFlag = false;
		}
		return createNoShowActivityFlag;
	}

	@Transactional
	private boolean createActivityForIssuesAdded(ReseaIntvwerCalRsicDAO rsicDAO, String userId, Date systemDate,
												Timestamp timestamp, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
		Map<String, Object> createActivity = createActivityForIssuesAddedinNoShowPage(rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, timestamp, systemDate, rsiiDAO);
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		boolean createIssueActivityFlag = false;

		if(createActivity != null) {
			Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
			Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
			Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
			String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

			if(spPoutSuccess == 0L && null != spPoutRscaId) {
				ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
				rscaDAO.setRscaId(spPoutRscaId);
				rsiiDAO.setRscaDAO(rscaDAO);
				rsiiDAO.setRsiiLastUpdBy(userId);
				rsiiDAO.setRsiiLastUpdUsing(ReseaConstants.NO_SHOW_BY_USING);
				rsiiDAO.setRsiiLastUpdTs(timestamp);
				rsiiRepo.save(rsiiDAO);
				createIssueActivityFlag = true;
			}
			else if(spPoutSuccess == 1L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
				}
				createIssueActivityFlag = false;
			}
			else if(spPoutSuccess == 2L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
				}
				createIssueActivityFlag = false;
			}
			else if(spPoutSuccess < 0L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
				}
				createIssueActivityFlag = false;
			}
		}
		else {
			createIssueActivityFlag = false;
		}
		return createIssueActivityFlag;
	}

	/**
	 * Create Activity associated with No Show page
	 *  Activities types
	 *      Failed (5583)
	 *
	 * @return  Map<String, Object>
	 */
	@Transactional
	private Map<String, Object> createActivityForFailed(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO rsicDAO,
															   Timestamp timestamp, Date systemDate, String userId) {
		Map<String, Object> createActivity = null;
		try {
			if(null != rscsId && null != rsicDAO) {
				String rscaDetails;
				String rschCalEventStTime = null;
				String meetingModeDesc = null;
				String rscnNoteCategory = null;
				String rscnNote = null;
				String showInNhuisInd = null;

				AllowValAlvDAO apptTypeAlvDao = alvRepo.findById(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();
				NonMonIssuesNmiDAO nmiDAO = nmiRepo.findById(NmiConstants.NMI_FAILURE_TO_ATTEND_1_ON_1_SESSION).orElseThrow();

				if(StringUtils.isNotBlank(rsicDAO.getRsicMtgModeInd())) {
					meetingModeDesc = rsicDAO.getRsicMtgModeInd().equalsIgnoreCase(ReseaConstants.MEETING_MODE.IN_PERSON.getCode()) ?
							ReseaConstants.MEETING_MODE.IN_PERSON.getDescription() :ReseaConstants.MEETING_MODE.VIRTUAL.getDescription();
				}
				else {
					meetingModeDesc = ReseaConstants.MEETING_MODE.IN_PERSON.getDescription();
				}

				if(null != rsicDAO.getRsicCalEventStTime()) {
					DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
					DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
					LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
					rschCalEventStTime = time.format(formatter12Hour);
				}
				rscaDetails = StringUtils.join("Claimant failed to attend the " +apptTypeAlvDao.getAlvShortDecTxt() +
						" at " + rschCalEventStTime+ " on " +  dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n" +
						"The issue " + nmiDAO.getParentNmiDAO().getNmiShortDescTxt() + " / " + nmiDAO.getNmiShortDescTxt() + " has been created" +".\n\n");

				String notes = StringUtils.join("The " + meetingModeDesc + " " + apptTypeAlvDao.getAlvShortDecTxt() +
						" scheduled for " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + " at " + rschCalEventStTime + " has been marked as failed.");

				saveNotes(rsicDAO, timestamp, userId, apptTypeAlvDao, notes); //CNO creation

				// Added to create RSCN entry for Failed
				rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),
						rscsStageCdAlv));
				rscnNote = notes;
				showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();

				entityManager.flush();
				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
						getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
						//getRscaStageFromRscsStage(rscsStageCdAlv),
						//getRscaStatusFromRscsStatus(rscsStatusCdAlv),
						ReseaAlvEnumConstant.RscaTypeCd.FAILED.getCode(),
						timestamp,
						"Claimant failed to attend the " +apptTypeAlvDao.getAlvShortDecTxt() + ".",
						ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
						"--",
						0L, 0L, stringToDate.apply(DATE_01_01_2000),
						StringUtils.EMPTY, StringUtils.EMPTY,
						rscaDetails,
						ReseaAlvEnumConstant.RscaReferenceIfkCd.RSIC.getCode(),
						rsicDAO.getRsicId(),
						rscnNoteCategory,
						rscnNote,
						showInNhuisInd,
						ReseaConstants.RSCA_CALLING_PROGRAM_NO_SHOW
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Failed Activity type (5583) associated with " +
					"RSIC ID "+ rsicDAO.getRsicId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}

	@Transactional
	private void saveNotes(ReseaIntvwerCalRsicDAO rsicDAO, Timestamp timestamp, String userId, AllowValAlvDAO apptTypeAlvDao, String notes) {
		CmtNotesCnoDao cnoDao = new CmtNotesCnoDao();
		cnoDao.setCnoEnteredBy(userId);
		cnoDao.setCnoEnteredTs(timestamp);
		cnoDao.setFkCmtId(rsicDAO.getClaimDAO().getClaimantDAO().getCmtId());
		cnoDao.setCnoSubjectTxt("RESEA: "+ apptTypeAlvDao.getAlvShortDecTxt() + " Failed");
		cnoDao.setCnoNotesTxt(notes);
		cnoDao.setCnoLastUpdBy(userId);
		cnoDao.setCnoLastUpdTs(timestamp);
		cnoRepo.save(cnoDao);
	}

	/**
	 * Create Activity for Issues added up in Reschedule page
	 *  RSCA Activities type
	 * 	 * 		- CREATED ISSUE 5586
	 *
	 * @return  Map<String, Object>
	 */
	@Transactional
	private Map<String, Object> createActivityForIssuesAddedinNoShowPage(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO rsicDAO,
																	   Timestamp timestamp, Date systemDate, ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
		Map<String, Object> createActivity = null;
		try {
			if(null != rsiiDAO.getRsiiId()  &&  null != rscsId &&  null != rsicDAO) {
				String rscaDetails = null;
				String rschCalEventStTime = null;
				String issueDatesDesc = null;

				NonMonIssuesNmiDAO nmiDAO = rsiiDAO.getNmiDAO();
				if(null != rsiiDAO.getRsiiIssueEndDt()) {
					issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to " + dateToString.apply(rsiiDAO.getRsiiIssueEndDt()) ;
				}
				else {
					issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to -";
				}
				AllowValAlvDAO apptTypeAlvDao = alvRepo.findById(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();

				if(null != rsicDAO.getRsicCalEventStTime()) {
					DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
					DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
					LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
					rschCalEventStTime = time.format(formatter12Hour);
				}
				rscaDetails =  "System has created the issue " + nmiDAO.getParentNmiDAO().getNmiShortDescTxt() + " / " + nmiDAO.getNmiShortDescTxt() + " effective " + issueDatesDesc
						+ " while recording no show for the " + apptTypeAlvDao.getAlvShortDecTxt() + " that was scheduled for " + rschCalEventStTime
						+ " on " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n";
				entityManager.flush();
				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
						getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
						//getRscaStageFromRscsStage(rscsStageCdAlv),
						//getRscaStatusFromRscsStatus(rscsStatusCdAlv),
						ReseaAlvEnumConstant.RscaTypeCd.CREATED_ISSUE.getCode(),
						timestamp,
						"Staff has created an issue.",
						ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
						"--",
						0L, 0L, stringToDate.apply(DATE_01_01_2000),
						StringUtils.EMPTY, StringUtils.EMPTY,
						rscaDetails,
						ReseaAlvEnumConstant.RscaReferenceIfkCd.RSII.getCode(),
						rsiiDAO.getRsiiId(),
						StringUtils.EMPTY,
						StringUtils.EMPTY,
						StringUtils.EMPTY,
						ReseaConstants.RSCA_CALLING_PROGRAM_NO_SHOW
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Issue added in No Show page associated with RSIC ID "+ rsicDAO.getRsicId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}


	/**
	 * Update No show business requirements in DB(RSIC)
	 * @return String
	 */
	@Transactional
	private void updateRsicForNoShow(ReseaIntvwerCalRsicDAO rsicDAO, String userId,  Long rscsId, Long rscsStageCdAlv,
									 	Long rscsStatusCdAlv,Timestamp timestamp, Date systemDate) {
		/*AnD249239 starts*/
		/*AllowValAlvDAO unUsedCalEventType = alvRepo.findById(ReseaAlvEnumConstant.RsicCalEventTypeCd.UNUSED.getCode()).orElseThrow();
		rsicDAO.setRsicCalEventTypeCdAlv(unUsedCalEventType);*/
		/*AnD249239 ends*/
		AllowValAlvDAO failedMeetingStatus = alvRepo.findById(ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED.getCode()).orElseThrow();
		rsicDAO.setRsicMtgStatusCdAlv(failedMeetingStatus);
		rsicDAO.setRsicMtgStatusUpdTs(commonRepo.getCurrentTimestamp());
		rsicDAO.setRsicMtgStatusUpdBy(Long.valueOf(userId));
		rsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
				dateToLocalDateTime.apply(timestamp).format(DATE_FORMATTER),
				StringUtils.trimToEmpty("No show for the appointment."),
				StringUtils.trimToEmpty(rsicDAO.getRsicTimeslotSysNotes())
				}));
		if(StringUtils.equalsIgnoreCase(INDICATOR.Y.toString(),rsicDAO.getRsicReopenAllowedInd())) {
			rsicDAO.setRsicReopenAllowedInd(INDICATOR.N.toString());
			rsicDAO.setRsicReopenAllowedTs(null);
			createActivityForReopenTurnedOff(userId, rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, timestamp, systemDate);
		}
		rsicDAO.setRsicLastUpdBy(userId);
		rsicDAO.setRsicLastUpdUsing(ReseaConstants.NO_SHOW_BY_USING);
		rsicRepo.save(rsicDAO);

	}

	/**
	 * Create the issue for No show
	 * 	- RESEA / Failure to Attend 1-on-1 session(2650)
	 */
	@Transactional
	private ReseaIssueIdentifiedRsiiDAO saveRsiiData(ReseaIntvwerCalRsicDAO rsicDAO, String userId, String createdUsing, Timestamp timestamp) {
		AllowValAlvDAO issueIdDuringCd = alvRepo.findById(ReseaAlvEnumConstant.RsiiIssueIdDuringCd.NO_SHOW.getCode()).orElseThrow();
		AllowValAlvDAO decIfkCd = alvRepo.findById(ReseaAlvEnumConstant.RsiiDecIfkCd.RSIC.getCode()).orElseThrow();
		LocalDate apptDate = dateToLocalDate.apply(rsicDAO.getRsicCalEventDt());
		ReseaIssueIdentifiedRsiiDAO rsiiDAO = null;
			try {
				LocalDate sundayDtOfApptDate = apptDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
				rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
				rsiiDAO.setRsicDAO(rsicDAO);
				rsiiDAO.setNmiDAO(nmiRepo.findById(NmiConstants.NMI_FAILURE_TO_ATTEND_1_ON_1_SESSION).orElseThrow());
				rsiiDAO.setRsiiIssueEffDt(localDateToDate.apply(sundayDtOfApptDate));
				rsiiDAO.setRsiiIssueIdDuringCdALV(issueIdDuringCd);
				rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(userId));
				rsiiDAO.setRsiiSourceIfkCd(decIfkCd);
				rsiiDAO.setRsiiSourceIfk(rsicDAO.getRsicId());
				rsiiDAO.setRsiiDecDetectDtInd(RSII_DEC_DETECT_DT_IND.APPOINTMENT_DT.getCode());
				rsiiDAO.setRsiiCreatedBy(userId);
				rsiiDAO.setRsiiCreatedUsing(createdUsing);
				rsiiDAO.setRsiiCreatedTs(timestamp);
				rsiiDAO.setRsiiLastUpdBy(userId);
				rsiiDAO.setRsiiLastUpdUsing(createdUsing);
				rsiiDAO.setRsiiLastUpdTs(timestamp);
				rsiiRepo.save(rsiiDAO);
			}
			catch (Exception e) {
				System.out.println("Error saving Issue (RSII) for No show functionality associated with RSIC Id" + rsicDAO.getRsicId());
				log.error(Arrays.toString(e.getStackTrace()));
			}
			return rsiiDAO;
	}


	@Transactional
	private String createActivityForReopenTurnedOff(String userId, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv,
										   ReseaIntvwerCalRsicDAO rsicDAO, Timestamp timestamp, Date systemDate) {
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		String noShowMsg = null;
		Map<String, Object> createRTWActivity = callCreateActivityForReopenTurnedOff(rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, timestamp, systemDate);
		if(createRTWActivity != null) {
			Long spPoutRscaId = (Long) createRTWActivity.get("POUT_RSCA_ID");
			Long spPoutSuccess = (Long) createRTWActivity.get("POUT_SUCCESS");
			if(null != spPoutRscaId && null != spPoutSuccess && spPoutSuccess == 0L) {
				noShowMsg = ReseaConstants.ACTIVITY_REOPEN_TURNED_OFF_SUCCESS;
			}
			else {
				noShowMsg = ReseaConstants.CREATE_ACTIVITY_FAILED_MSG;
			}
		}
		else {
			noShowMsg = ReseaConstants.CREATE_ACTIVITY_FAILED_MSG;
		}
		return noShowMsg;
	}

	@Transactional
	private Map<String, Object>  callCreateActivityForReopenTurnedOff(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO rsicDAO,
														Timestamp timestamp, Date systemDate) {
		Map<String, Object> createActivity = null;
		try {
			if(null != rscsId && null != rsicDAO) {
				String rscaDetails;
				String rschCalEventStTime = null;

				if(null != rsicDAO.getRsicCalEventStTime()) {
					DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
					DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
					LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
					rschCalEventStTime = time.format(formatter12Hour);
				}
				rscaDetails = StringUtils.join("The special reopen window to record details of appointment attendance is now closed at "+
						 rschCalEventStTime+ " on " +  dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n");
				entityManager.flush();
				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
						getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
						//getRscaStageFromRscsStage(rscsStageCdAlv),
						//getRscaStatusFromRscsStatus(rscsStatusCdAlv),
						ReseaAlvEnumConstant.RscaTypeCd.REOPEN_TURNED_OFF.getCode(),
						timestamp,
						"The special reopen window to record details of appointment attendance is now closed.",
						ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
						"--",
						0L, 0L, stringToDate.apply(DATE_01_01_2000),
						StringUtils.EMPTY, StringUtils.EMPTY,
						rscaDetails,
						ReseaAlvEnumConstant.RscaReferenceIfkCd.RSIC.getCode(),
						rsicDAO.getRsicId(),
						StringUtils.EMPTY,
						StringUtils.EMPTY,
						StringUtils.EMPTY,
						ReseaConstants.RSCA_CALLING_PROGRAM_NO_SHOW
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Reopen Turned Off Activity type (5760) associated with " +
					"RSIC ID "+ rsicDAO.getRsicId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}

}