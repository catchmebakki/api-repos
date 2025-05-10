package com.ssi.ms.resea.service;

import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.NonMonIssuesNmiDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseActivityRscaDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.ReseaIssueIdentifiedRsiiDAO;
import com.ssi.ms.resea.dto.AllowValAlvResDTO;
import com.ssi.ms.resea.dto.IssuesDTO;
import com.ssi.ms.resea.dto.ReseaSwitchMeetingModeReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.*;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CreateActivityErrorDTODetail.CREATE_ACTIVITY_EXCEPTION;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRsicTimeslotUsage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRsicMtgStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRsicTimeslotUsage;

/**
 * {@code ReseaSwitchMeetingModeService} is a service component in the application responsible for
 * handling business logic related to Switch Meeting Mode functionality.
 * This service performs operations such as to get reasons for switching meeting modes and saving the details for switch modes.
 * @author Anand
 */
@Service
@Slf4j
public class ReseaSwitchMeetingModeService extends ReseaBaseService {
	@Autowired
	EntityManager entityManager;
	public ReseaSwitchMeetingModeService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	/**
	 * Get the list of reasons for switching meeting modes based on current meeting mode
	 * @return List<AllowValAlvResCommonDTO>
	 */
	@Transactional
	public List<AllowValAlvResDTO> getReasonsForSwitchMeetingMode(String currentMeetingMode) {
		currentMeetingMode = ReseaConstants.MEETING_MODE.IN_PERSON.getCode().equalsIgnoreCase(currentMeetingMode) ? "INP" : "VIR";
		List<AllowValAlvResDTO> alvList;
		alvList = alvRepo.getActiveAlvsByAlcAndDecipherCode(ReseaConstants.RAT_MEETING_MODE_CHG_RSN_CD,
						currentMeetingMode).stream().map(dao -> allowValAlvMapper.daoToShortDescDto(dao)).toList();
		return alvList;
	}

	/**
	 * Save the switch meeting modes details
	 * @return String
	 */
	@Transactional
    public String saveSwitchMeetingMode(ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO, String userId, String roleId) {
		Date systemDate = commonRepo.getCurrentDate();
		Timestamp timestamp = commonRepo.getCurrentTimestamp();
		final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepo.findById(reseaSwitchMeetingModeReqDTO.getEventId())
				.orElseThrow(() -> new NotFoundException("Invalid RSIC ID:" + reseaSwitchMeetingModeReqDTO.getEventId(), "rsicId.notFound"));
		final HashMap<String, List<String>> errorMap = switchMtgModeValidator.validate(reseaSwitchMeetingModeReqDTO,
				rsicDAO.getClaimDAO().getClmBenYrEndDt(), systemDate, roleId, RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV == rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId());
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException(ErrorMessageConstant.SAVE_SWITCH_MEETING_MODE_VALIDATION_FAILED, errorMap);
		}
		String currentMeetingModeDesc;
		String switchedMeetingModeDesc;
		boolean isInPerson = MEETING_MODE.IN_PERSON.getCode().equalsIgnoreCase(reseaSwitchMeetingModeReqDTO.getCurrentMeetingMode());
		currentMeetingModeDesc = isInPerson ? MEETING_MODE.IN_PERSON.getDescription() : MEETING_MODE.VIRTUAL.getDescription();
		switchedMeetingModeDesc = isInPerson ? MEETING_MODE.VIRTUAL.getDescription() : MEETING_MODE.IN_PERSON.getDescription();

		boolean createSwitchMtgModeActivityFlag = false;

		ReseaCaseRscsDAO rscsDAO = rsicDAO.getRscsDAO();
		Long rscsId = rscsDAO.getRscsId();
		Long rscsStageCdAlv = rscsDAO.getRscsStageCdALV().getAlvId();
		Long rscsStatusCdAlv = rscsDAO.getRscsStatusCdALV().getAlvId();
		updateRsic(rsicDAO, reseaSwitchMeetingModeReqDTO, currentMeetingModeDesc, switchedMeetingModeDesc, userId, ReseaConstants.SWITCH_MEETING_MODE_BY_USING, timestamp );
		List<ReseaIssueIdentifiedRsiiDAO> rsiiDaoList = saveRsiiData(reseaSwitchMeetingModeReqDTO, rsicDAO, userId, ReseaConstants.SWITCH_MEETING_MODE_BY_USING, timestamp);
		createSwitchMtgModeActivityFlag = createActivityForSwitchMtgMode(reseaSwitchMeetingModeReqDTO, rsicDAO, userId, rscsId, rscsStageCdAlv, rscsStatusCdAlv, timestamp, systemDate,
												currentMeetingModeDesc, switchedMeetingModeDesc);
		if(createSwitchMtgModeActivityFlag) {
			for(ReseaIssueIdentifiedRsiiDAO rsiiDAO : rsiiDaoList) {
				boolean createIssueActivityFlag = createActivityForIssuesAdded(reseaSwitchMeetingModeReqDTO, rsicDAO, userId, systemDate, timestamp, rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsiiDAO);
			}
		}
		return createSwitchMtgModeActivityFlag == true ? ReseaConstants.SWITCH_MEETING_MODE_SAVED : ReseaConstants.REQUEST_FAILED_MSG;
    }

	@Transactional
	private boolean createActivityForSwitchMtgMode(ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO, ReseaIntvwerCalRsicDAO rsicDAO ,
												  	String userId, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv,
										 			Timestamp timestamp, Date systemDate, String currentMeetingModeDesc, String switchedMeetingModeDesc) {

		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		boolean createSwitchMtgModeActivityFlag = false;

		Map<String, Object> createActivity = createActivityForStaffModeChangeType(rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, reseaSwitchMeetingModeReqDTO, timestamp, systemDate,
												currentMeetingModeDesc, switchedMeetingModeDesc);
		if(createActivity != null) {
			Long spPoutRscaId = (Long) createActivity.get("POUT_RSCA_ID");
			Long spPoutSuccess = (Long) createActivity.get("POUT_SUCCESS");
			Long spPoutNhlId = (Long) createActivity.get("POUT_NHL_ID");
			String spPoutErrorMsg = (String) createActivity.get("POUT_ERROR_MSG");

			if(spPoutSuccess == 0L && null != spPoutRscaId) {
				createSwitchMtgModeActivityFlag = true;
			}
			else if(spPoutSuccess == 1L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_TECH_ERROR);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_TECH_ERROR.toString(), errorMap);
				}
				createSwitchMtgModeActivityFlag = false;
			}
			else if(spPoutSuccess == 2L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_DATA_INCONSIST);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_DATA_INCONSIST.toString(), errorMap);
				}
				createSwitchMtgModeActivityFlag = false;
			}
			else if(spPoutSuccess < 0L && null != spPoutNhlId) {
				errorEnums.add(CREATE_ACTIVITY_EXCEPTION);
				errorParams.add(String.valueOf(spPoutNhlId));
				ReseaUtilFunction.updateErrorMap(errorMap, errorEnums, errorParams);
				if (!errorMap.isEmpty()) {
					throw new DynamicValidationException(CREATE_ACTIVITY_EXCEPTION.toString(), errorMap);
				}
				createSwitchMtgModeActivityFlag = false;
			}
		}
		else {
			createSwitchMtgModeActivityFlag = false;
		}
		return createSwitchMtgModeActivityFlag;
	}

	@Transactional
	private Map<String, Object> createActivityForStaffModeChangeType(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO rsicDAO,
																	 ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO, Timestamp timestamp, Date systemDate,
																	 String currentMeetingModeDesc, String switchedMeetingModeDesc) {
		Map<String, Object> createActivity = null;
		try {
			if(null != rsicDAO  &&  null != rscsId && null != rsicDAO) {
				String rscaDetails;
				String issueCreatedDesc = null;
				String showInNhuisInd = null;
				String rschCalEventStTime = null;

				AllowValAlvDAO apptTypeAlvDao = alvRepo.findById(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()).orElseThrow();
				AllowValAlvDAO reasonForSwitchMtgModeAlvDao = alvRepo.findById(reseaSwitchMeetingModeReqDTO.getReasonForSwitchMeetingMode()).orElseThrow();

				if(!CollectionUtils.isEmpty(reseaSwitchMeetingModeReqDTO.getIssuesDTOList())) {
					issueCreatedDesc = reseaSwitchMeetingModeReqDTO.getIssuesDTOList().size() + " issue(s) have been created.";
					showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
				}
				else {
					showInNhuisInd =  ReseaConstants.INDICATOR.Y.toString();
				}

				if(null != rsicDAO.getRsicCalEventStTime()) {
					DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
					DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
					LocalTime time = LocalTime.parse(rsicDAO.getRsicCalEventStTime(), formatter24Hour);
					rschCalEventStTime = time.format(formatter12Hour);
				}
				rscaDetails = StringUtils.join("Staff has switched the mode of the upcoming " + apptTypeAlvDao.getAlvShortDecTxt() +
						" that is scheduled for " + rschCalEventStTime+ " on " +  dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n" +
						"The reason for the switch in meeting mode is " + reasonForSwitchMtgModeAlvDao.getAlvShortDecTxt() + ".\n\n");

				if(StringUtils.isNotBlank(issueCreatedDesc)) {
					rscaDetails = StringUtils.join(rscaDetails + issueCreatedDesc);
				}

				String rscnNoteCategory = null;
				String rscnNote = null;

				if(StringUtils.isNotBlank(reseaSwitchMeetingModeReqDTO.getStaffNotes())) {
					rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(), rscsStageCdAlv));
					rscnNote = reseaSwitchMeetingModeReqDTO.getStaffNotes();
					showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
				}
				else {
					rscnNoteCategory = StringUtils.EMPTY;
					rscnNote = StringUtils.EMPTY;
					showInNhuisInd = StringUtils.EMPTY;
				}

				if(reseaSwitchMeetingModeReqDTO.isIncludeThisNoteInCNO() && StringUtils.isNotBlank(reseaSwitchMeetingModeReqDTO.getStaffNotes())) {
					showInNhuisInd = ReseaConstants.INDICATOR.Y.toString();
				}

				entityManager.flush();
				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						//getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
						//getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
						getRscaStageFromRscsStage(rscsStageCdAlv),
						getRscaStatusFromRscsStatus(rscsStatusCdAlv),
						ReseaAlvEnumConstant.RscaTypeCd.STAFF_MODE_CHANGE.getCode(),
						timestamp,
						"Staff has switched the mode of the next appointment to " + switchedMeetingModeDesc,
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
						ReseaConstants.RSCA_CALLING_PROGRAM_SWITCH_MTG_MODE
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Staff Mode Change Activity type (5581) associated with " +
					"RSIC ID "+ rsicDAO.getRsicId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}

	@Transactional
	private boolean createActivityForIssuesAdded(ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO, ReseaIntvwerCalRsicDAO rsicDAO, String userId, Date systemDate,
											  Timestamp timestamp, Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
		final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final List<String> errorParams = new ArrayList<>();
		boolean createIssueActivityFlag = false;

		String switchMtgModeMsg = null;
		Map<String, Object> createActivity = createActivityForIssuesAddedinSwitchMtgModePage(rscsId, rscsStageCdAlv, rscsStatusCdAlv, rsicDAO, reseaSwitchMeetingModeReqDTO, timestamp, systemDate, rsiiDAO);

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
				rsiiDAO.setRsiiLastUpdUsing(ReseaConstants.SWITCH_MEETING_MODE_BY_USING);
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
	 * Create Activity for Issues added up in Switch Meeting Mode page
	 *  RSCA Activities type
	 * 	 * 		- CREATED ISSUE 5586
	 *
	 * @return  Map<String, Object>
	 */
	@Transactional
	private Map<String, Object> createActivityForIssuesAddedinSwitchMtgModePage(Long rscsId, Long rscsStageCdAlv, Long rscsStatusCdAlv, ReseaIntvwerCalRsicDAO rsicDAO,
																				ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO, Timestamp timestamp, Date systemDate,
																				ReseaIssueIdentifiedRsiiDAO rsiiDAO) {
		Map<String, Object> createActivity = null;
		try {
			if(null != rsiiDAO.getRsiiId()  &&  null != rscsId && null != rsicDAO) {
				String rscaDetails = null;
				String showInNhuisInd = null;
				String rschCalEventStTime = null;
				String issueDatesDesc = null;
				String staffNotes = null;
				NonMonIssuesNmiDAO nmiDAO = rsiiDAO.getNmiDAO();
				if(null != rsiiDAO.getRsiiIssueEndDt()) {
					issueDatesDesc = dateToString.apply(rsiiDAO.getRsiiIssueEffDt()) + " to " + dateToString.apply(rsiiDAO.getRsiiIssueEndDt());
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
				rscaDetails =  "Staff has created the issue " + nmiDAO.getParentNmiDAO().getNmiShortDescTxt() + " / " + nmiDAO.getNmiShortDescTxt() + " effective " + issueDatesDesc
						+ " while switching mode for the " + apptTypeAlvDao.getAlvShortDecTxt() + " that was scheduled for " + rschCalEventStTime + " on " + dateToString.apply(rsicDAO.getRsicCalEventDt()) + ".\n\n";

				int count = 0;
				if (!CollectionUtils.isEmpty(reseaSwitchMeetingModeReqDTO.getIssuesDTOList())) {
					if(reseaSwitchMeetingModeReqDTO.getIssuesDTOList().size() == 1) {
						showInNhuisInd =  ReseaConstants.INDICATOR.Y.toString();
					}
					else {
						showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
					}
				}
				else {
					showInNhuisInd =  ReseaConstants.INDICATOR.N.toString();
				}

				String rscnNoteCategory = null;
				String rscnNote = null;

				if(StringUtils.isNotBlank(reseaSwitchMeetingModeReqDTO.getStaffNotes())) {
					rscnNoteCategory = String.valueOf(getRscnNoteCategoryFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId(),rscsStageCdAlv));
					rscnNote = reseaSwitchMeetingModeReqDTO.getStaffNotes();
					showInNhuisInd = ReseaConstants.INDICATOR.N.toString();
				}
				else {
					rscnNoteCategory = StringUtils.EMPTY;
					rscnNote = StringUtils.EMPTY;
					showInNhuisInd = StringUtils.EMPTY;
				}

				entityManager.flush();
				createActivity = rscaRepo.createCaseActivity(
						rscsId,
						//getRscaStageFromRsicTimeslotUsage(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
						//getRscaStatusFromRsicMtgStatus(rsicDAO.getRsicMtgStatusCdAlv().getAlvId()),
						getRscaStageFromRscsStage(rscsStageCdAlv),
						getRscaStatusFromRscsStatus(rscsStatusCdAlv),
						ReseaAlvEnumConstant.RscaTypeCd.CREATED_ISSUE.getCode(),
						timestamp,
						"Staff has created an issue",
						ReseaConstants.RSCA_SYNOPSIS_TYPE.N.toString(),
						"--",
						0L, 0L, stringToDate.apply(DATE_01_01_2000),
						StringUtils.EMPTY, StringUtils.EMPTY,
						rscaDetails,
						ReseaAlvEnumConstant.RscaReferenceIfkCd.RSII.getCode(),
						rsiiDAO.getRsiiId(),
						rscnNoteCategory,
						rscnNote,
						showInNhuisInd,
						ReseaConstants.RSCA_CALLING_PROGRAM_SWITCH_MTG_MODE
				);
			}
		}
		catch (Exception e) {
			System.out.println("Error in calling the stored procedure CREATE_ACTIVITY for Issues added in Switch Meeting Mode page associated with RSIC ID "+ rsicDAO.getRsicId());
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return createActivity;
	}

	/**
	 * Update RSIC record associated with Switch meeting mode fields.
	 */
	@Transactional
	private void updateRsic(ReseaIntvwerCalRsicDAO rsicDAO, ReseaSwitchMeetingModeReqDTO switchMeetingModeReqDTO,
							String currentMtgModeDesc, String switchedMtgModeDesc, String userId, String updatedUsing, Timestamp timestamp) {
		AllowValAlvDAO noticeStatusToBeSent = alvRepo.findById(ReseaAlvEnumConstant.RsicNoticeStatusCd.TO_BE_SENT.getCode()).orElseThrow();
		rsicDAO.setRsicMtgModeInd(MEETING_MODE.IN_PERSON.getCode().equalsIgnoreCase(switchMeetingModeReqDTO.getCurrentMeetingMode())
						? MEETING_MODE.VIRTUAL.getCode() : MEETING_MODE.IN_PERSON.getCode());
        rsicDAO.setRsicModeChgRsnCdAlv(alvRepo.findById(switchMeetingModeReqDTO.getReasonForSwitchMeetingMode()).orElseThrow());
		if(StringUtils.isNotBlank(switchMeetingModeReqDTO.getMeetingModeChgReasonTxt())) {
			if(StringUtils.isNotBlank(rsicDAO.getRsicModeChgRsnTxt())) {
				rsicDAO.setRsicModeChgRsnTxt(StringUtils.trimToEmpty(rsicDAO.getRsicModeChgRsnTxt())
						+  StringUtils.trimToEmpty(switchMeetingModeReqDTO.getMeetingModeChgReasonTxt()));
			}
			else {
				rsicDAO.setRsicModeChgRsnTxt(StringUtils.trimToEmpty(switchMeetingModeReqDTO.getMeetingModeChgReasonTxt()));
			}
		}
		int currentCount = rsicDAO.getRsicMtgmodeSwchCnt() != null ? rsicDAO.getRsicMtgmodeSwchCnt() : 0;
		int meetingModeSwitchCount = currentCount + 1;
		rsicDAO.setRsicMtgmodeSwchCnt(meetingModeSwitchCount);
		rsicDAO.setRsicStaffNotes(GENERATECOMMENTS.apply(new String[]{
				StringUtils.trimToEmpty(rsicDAO.getRsicStaffNotes()),
				userService.getUserName(Long.valueOf(userId)),
				dateToLocalDateTime.apply(timestamp).format(DATE_TIME_FORMATTER),
				StringUtils.trimToEmpty(switchMeetingModeReqDTO.getStaffNotes())}));
		String timeSlotSystemMotesForSwitchMeetingMode = "Meeting mode switched from " + currentMtgModeDesc + " to " + switchedMtgModeDesc + ".";
		rsicDAO.setRsicTimeslotSysNotes(GEN_COMMENTS_IN_REVERSE_CHRONOLOGICAL_ORDER.apply(new String[]{
				dateToLocalDateTime.apply(parRepo.getCurrentTimestamp()).format(DATE_FORMATTER),
				StringUtils.trimToEmpty(timeSlotSystemMotesForSwitchMeetingMode),
				StringUtils.trimToEmpty(rsicDAO.getRsicTimeslotSysNotes())
		}));
		rsicDAO.setRsicNoticeStatusCdAlv(noticeStatusToBeSent);
		rsicDAO.setRsicLastUpdBy(userId);
		rsicDAO.setRsicLastUpdUsing(updatedUsing);
		rsicRepo.save(rsicDAO);
	}


	/**
	 * Save the issue details (RSII) created from Switch Meeting Mode page.
	 */
	@Transactional
	private List<ReseaIssueIdentifiedRsiiDAO> saveRsiiData(ReseaSwitchMeetingModeReqDTO switchMtgReqDTO, ReseaIntvwerCalRsicDAO rsicDAO,
							  String userId, String createdUsing, Timestamp timestamp) {
		List<ReseaIssueIdentifiedRsiiDAO> rsiiDaoList = new ArrayList<ReseaIssueIdentifiedRsiiDAO>();
		AllowValAlvDAO issueIdDuringCd = alvRepo.findById(ReseaAlvEnumConstant.RsiiIssueIdDuringCd.SWITCH_MEETING_MODE.getCode()).orElseThrow();
		AllowValAlvDAO decIfkCd = alvRepo.findById(ReseaAlvEnumConstant.RsiiDecIfkCd.RSIC.getCode()).orElseThrow();
		if (!CollectionUtils.isEmpty(switchMtgReqDTO.getIssuesDTOList())) {
				switchMtgReqDTO.getIssuesDTOList().forEach(getIssuesDTOList -> {
					try {
						ReseaIssueIdentifiedRsiiDAO rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
						rsiiDAO.setRsicDAO(rsicDAO);
						rsiiDAO.setNmiDAO(nmiRepo.findById(getIssuesDTOList.getIssueId()).orElseThrow());
						rsiiDAO.setRsiiIssueEffDt(getIssuesDTOList.getStartDt());
						rsiiDAO.setRsiiIssueEndDt(getIssuesDTOList.getEndDt());
						rsiiDAO.setRsiiIssueIdDuringCdALV(issueIdDuringCd);
						rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(userId));
						rsiiDAO.setRsiiDecDetectDtInd(RSII_DEC_DETECT_DT_IND.SYSTEM_DT.getCode());
						rsiiDAO.setRsiiSourceIfkCd(decIfkCd);
						rsiiDAO.setRsiiSourceIfk(rsicDAO.getRsicId());
						rsiiDAO.setRsiiStaffNotes(switchMtgReqDTO.getStaffNotes());
						rsiiDAO.setRsiiCreatedBy(userId);
						rsiiDAO.setRsiiCreatedUsing(createdUsing);
						rsiiDAO.setRsiiCreatedTs(timestamp);
						rsiiDAO.setRsiiLastUpdBy(userId);
						rsiiDAO.setRsiiLastUpdUsing(createdUsing);
						rsiiDAO.setRsiiLastUpdTs(timestamp);
						rsiiRepo.save(rsiiDAO);
						rsiiDaoList.add(rsiiDAO);
					}
					catch (Exception e) {
						System.out.println("Error saving Issue in Switch Meeting Mode page: " + getIssuesDTOList.getIssueId()
								+ " associated with RSIC Id" + rsicDAO.getRsicId());
					}
			});
		}
		return rsiiDaoList;
	}

	private String getReasonsForSwitchMeetingModeDesc(ReseaSwitchMeetingModeReqDTO reseaSwitchMeetingModeReqDTO) {
		AllowValAlvDAO allowValAlvDAO = alvRepo.findById(reseaSwitchMeetingModeReqDTO.getReasonForSwitchMeetingMode()).orElseThrow();
		return allowValAlvDAO.getAlvShortDecTxt();
	}
}