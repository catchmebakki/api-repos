package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.platform.util.DateUtil;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseActivityRscaDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseNoteRscnDAO;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.ReseaIssueIdentifiedRsiiDAO;
import com.ssi.ms.resea.database.repository.ReseaActivityConfigRsacRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseActivityRscaRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseNoteRscnRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.dto.CaseActivityAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupAddReqDTO;
import com.ssi.ms.resea.dto.CaseActivityFollowupEditReqDTO;
import com.ssi.ms.resea.dto.CaseActivitySummaryDTO;
import com.ssi.ms.resea.dto.CaseSummaryResDTO;
import com.ssi.ms.resea.dto.CorrStandardTextResDTO;
import com.ssi.ms.resea.dto.CreateIssuesDTO;
import com.ssi.ms.resea.dto.HeaderDetailsResDTO;
import com.ssi.ms.resea.dto.HeaderJobRefDetailsDTO;
import com.ssi.ms.resea.dto.HeaderWorkSrchDetailsDTO;
import com.ssi.ms.resea.dto.ReseaInterviewResDTO;
import com.ssi.ms.resea.dto.ReseaViewActivityResDTO;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaCaseSummaryValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDateTime;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.stringToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ReseaAlvEnumConstant.RscaReferenceIfkCd;
import static com.ssi.ms.resea.constant.ReseaAlvEnumConstant.RscaStageCd;
import static com.ssi.ms.resea.constant.ReseaAlvEnumConstant.RscaStatusCd;
import static com.ssi.ms.resea.constant.ReseaAlvEnumConstant.RscaTypeCd;
import static com.ssi.ms.resea.constant.ReseaConstants.ACTIVITY_TEMPLATE_TRN;
import static com.ssi.ms.resea.constant.ReseaConstants.ADD_ACTIVITY_STANDARD_TXT;
import static com.ssi.ms.resea.constant.ReseaConstants.API_NEW_ACTIVITY;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_01_01_2000;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.resea.constant.ReseaConstants.GENERATECOMMENTS;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_MTG_STATUS_COMPLETED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSII_DEC_IFK_RSID;
import static com.ssi.ms.resea.constant.ReseaConstants.RSII_ISSUE_ID_DURING_FIRST_SUB_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSII_ISSUE_ID_DURING_INITIAL_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSII_ISSUE_ID_DURING_SECOND_SUB_APPT;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert12hTo24h;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.string24HToDate;

@Slf4j
@Service
public class ReseaCaseSummaryService extends  ReseaBaseService {

	@Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ReseaCaseRscsRepository rscsRepository;
	@Autowired
	private ReseaCaseActivityRscaRepository rscaRepository;
	@Autowired
	private ReseaActivityConfigRsacRepository rsacRepository;
    @Autowired
    private ParameterParRepository parRepository;
    @Autowired
    private ReseaCaseNoteRscnRepository rscnRepository;
    @Autowired
    ReseaCalendarEventService calEventService;
	@Autowired
	ReseaCaseSummaryValidator caseSummValidator;
	@Autowired
	ReseaInterviewService reseaInterviewService;
	@Autowired
	ReseaCalendarEventService calendarEventService;
    public CaseSummaryResDTO getCaseSummaryDetails(final Long caseId, Sort.Direction sortDir, final String roleId) {
        final ReseaCaseRscsDAO rscsDAO = rscsRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Invalid Case ID:" + caseId, CASE_ID_NOT_FOUND));
        Date systemDate = parRepository.getCurrentDate();
        List<HeaderWorkSrchDetailsDTO> workSrchDetailsDTOList = calEventService.getCaseWorkSearchReq(
                rscsDAO.getClmDAO(), systemDate);
        List<HeaderJobRefDetailsDTO> jobRefDetailsDTOList = calEventService.getCaseJobReferrals(rscsDAO, systemDate);
        //ReseaPreStepsRspsDAO rspsDAO = rspsRepository.getRspsByClmAndItemType(rscsDAO.getClmDAO().getClmId(), ReseaAlvEnumConstant.RspsItemTypeCd.SCORED.getCode());
		StringBuilder followupsIndicator = new StringBuilder();
        long rolId = Long.parseLong(roleId);
		final List<ReseaCaseActivityRscaDAO> rscaDAOList = rscaRepository.getNextFolloupActivity(rscsDAO.getRscsId());
		String earliestFolloUpType = null;
		Date earliestFolloUpDt = null;
		if (!CollectionUtils.isEmpty(rscaDAOList)) {
			earliestFolloUpType = rscaDAOList.get(0).getRscaFollowupTypeCdALV().getAlvShortDecTxt();
			earliestFolloUpDt = rscaDAOList.get(0).getRscaFollowupDt();
			followupsIndicator.append(" ").append(earliestFolloUpType);
			followupsIndicator.append(" ").append(dateToString.apply(earliestFolloUpDt));
		}
        return new CaseSummaryResDTO()
                .withCaseManagerName(rscsDAO.getStfDAO().getStaffName())
                .withClaimantName(rscsDAO.getClmDAO().getClaimantDAO().getClaimantName())
                .withSsnLast4Digits(rscsDAO.getClmDAO().getClaimantDAO().getSsn().substring(5))
				.withClmBeginDt(rscsDAO.getClmDAO().getClmEffectiveDt())
                .withByeDt(rscsDAO.getClmDAO().getClmBenYrEndDt())
                .withCaseId(rscsDAO.getRscsId())
                .withStage(rscsDAO.getRscsStageCdALV().getAlvShortDecTxt())
                .withStatus(rscsDAO.getRscsStatusCdALV().getAlvShortDecTxt())
                .withWeeksFiled(workSrchDetailsDTOList.size())
                .withProfileScore(rscsDAO.getRscsScore() == null ? null : rscsDAO.getRscsScore().doubleValue())
                .withOrientationDt(rscsDAO.getRscsOrientationDt().after(stringToDate.apply(DATE_01_01_2000))
                        ? dateToString.apply(rscsDAO.getRscsOrientationDt()) : "Conversion")
				.withOrientatonRschCnt(rscsDAO.getRscsNumOrntnReschs())
                .withInitialAppttDt(rscsDAO.getRscsInitApptDt())
				.withInitialAppttRschCnt(rscsDAO.getRscsNumInitReschs())
                .withFirstSubsequentApptDt(rscsDAO.getRscsFirstSubsApptDt())
				.withFirstSubsequentApptRschCnt(rscsDAO.getRscsNumFstSubReschs())
                .withSecondSubsequentApptDt(rscsDAO.getRscsSecondSubsApptDt())
				.withSecondSubsequentApptRschCnt(rscsDAO.getRscsNumSecSubReschs())
                .withIndicators(getIndicator(rscsDAO))
                .withFollowupsIndicator(followupsIndicator.toString().trim())
                //.withJobReferral(CollectionUtils.isEmpty(jobRefDetailsDTOList) ? null :
				//       dateToString.apply(jobRefDetailsDTOList.get(0).getStartDt()))
				.withEarliestFollowUpType(earliestFolloUpType)
				.withEarliestFollowUpDt(earliestFolloUpDt)
                .withSynopsis(rscsDAO.getRscsSynopsis())
                .withClosedOnDt(rscsDAO.getRscsClosedDt())
                .withClosedReason(rscsDAO.getRscsClosedReasonCd() == null ? null :
                        rscsDAO.getRscsClosedReasonCd().getAlvShortDecTxt())
                .withIssues(calEventService.getCaseIssueDetails(rscsDAO.getClmDAO(), systemDate))
                .withWorkSearch(workSrchDetailsDTOList)
                .withJobReferrals(jobRefDetailsDTOList)
                .withActivitySummary(rscaRepo.getCaseActivitySummary(rscsDAO.getRscsId(),
                                            rolId == ROL_RESEA_CASE_MANAGER ||
                                            rolId == ROL_RESEA_PROG_STAFF ||
                                            rolId == ROL_LOCAL_OFFICE_MANAGER ? "Y" : "N",
						Sort.by(sortDir, "rscaId")));
    }

	public List<CaseActivitySummaryDTO> getCaseActivitySummary(final Long caseId, Sort.Direction sortDir, final String userId, final String roleId) {
		final ReseaCaseRscsDAO rscsDAO = rscsRepository.findById(caseId)
				.orElseThrow(() -> new NotFoundException("Invalid Case ID:" + caseId, CASE_ID_NOT_FOUND));
		Long caseUserId = rscsDAO.getStfDAO().getUserDAO().getUserId();
		Long loginUserId = Long.parseLong(userId);
		Integer rolId = Integer.parseInt(roleId);
		return rscaRepo.getCaseActivitySummary(caseId,
				(ROL_RESEA_CASE_MANAGER.equals(rolId) && Objects.equals(caseUserId, loginUserId)) ||
						ROL_RESEA_PROG_STAFF.equals(rolId) ||
						ROL_LOCAL_OFFICE_MANAGER.equals(rolId) ? "Y" : "N",
				Sort.by(sortDir, "rscaId"));
	}
    private String getIndicator(ReseaCaseRscsDAO rscsDAO) {
    	final ParameterParDao parameterParDao = commonParRepository.findByParShortName(ReseaConstants.RESEA_DEADLINE_DAYS);
    	final long reseaDeadLineDays = parameterParDao != null ? parameterParDao.getParNumericValue() : 0L;
    	
    	StringBuilder indicators = new StringBuilder(StringUtils.EMPTY);
    	if (ReseaConstants.YES_OR_NO_IND_Y.equals(rscsDAO.getRscsOnWaitlistInd())) {
    		indicators.append(" ").append(ReseaConstants.INDICATOR_WAITLISTED);
    	} else if(ReseaConstants.INDICATOR_HIGH_PRIORITY.equals(rscsDAO.getRscsPriority())) {
    		indicators.append(" ").append(ReseaConstants.INDICATOR_HIGH_PRIORITY);
    	} else {

			LocalDate orientationDate = rscsDAO.getRscsOrientationDt() != null ? DateUtil.dateToLocalDate.apply(rscsDAO.getRscsOrientationDt()) : LocalDate.now();
			LocalDate initApptDate = rscsDAO.getRscsInitApptDt() != null ? DateUtil.dateToLocalDate.apply(rscsDAO.getRscsInitApptDt()) : LocalDate.now();
			LocalDate firstSubApptDate = rscsDAO.getRscsFirstSubsApptDt() != null ? DateUtil.dateToLocalDate.apply(rscsDAO.getRscsFirstSubsApptDt()) : LocalDate.now();
			LocalDate secSubSeqApptDate = rscsDAO.getRscsSecondSubsApptDt() != null ? DateUtil.dateToLocalDate.apply(rscsDAO.getRscsSecondSubsApptDt()) : LocalDate.now();

			if ((ReseaConstants.RSCS_STAGE_INITIAL_APPT == rscsDAO.getRscsStageCdALV().getAlvId() &&
					rscsDAO.getRscsOrientationDt() != null &&
					DateUtil.stringToDate.apply("01/01/2000").compareTo(rscsDAO.getRscsOrientationDt()) != 0
					&& ChronoUnit.DAYS.between(orientationDate, initApptDate) > reseaDeadLineDays
					) ||
					(ReseaConstants.RSCS_STAGE_FIRST_SUBS_APPT == rscsDAO.getRscsStageCdALV().getAlvId()
							&& ChronoUnit.DAYS.between(initApptDate, firstSubApptDate) > reseaDeadLineDays
					) ||
					(ReseaConstants.RSCS_STAGE_SECOND_SUBS_APPT == rscsDAO.getRscsStageCdALV().getAlvId()
							&& ChronoUnit.DAYS.between(firstSubApptDate, secSubSeqApptDate) > reseaDeadLineDays
					)) {
				indicators.append(" ").append(ReseaConstants.INDICATOR_LATE);
			}
		}

    	return indicators.toString().trim();
    }

    public String addFollowup(CaseActivityFollowupAddReqDTO followupReqDTO, String userId, String roleId) {
		ReseaCaseActivityRscaDAO rscaDAO = rscaRepository.findById(followupReqDTO.getActivityId())
				.orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + followupReqDTO.getActivityId(), "activityId.notFound"));
		Date systemDate = parRepository.getCurrentDate();
		Date systemTimestamp = parRepository.getCurrentTimestamp();
		final HashMap<String, List<String>> errorMap = caseSummValidator.validateAddFollowup(
				followupReqDTO, rscaDAO, userId, roleId, systemDate);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException("Add Follow-up Request Failed", errorMap);
		}
		rscaDAO.setRscaFollowupDt(followupReqDTO.getFollowUpCompByDt());
		rscaDAO.setRscaFollowupTypeCdALV(alvRepo.findById(followupReqDTO.getFollowUpType()).get());
		rscaDAO.setRscaFollowupNote(GENERATECOMMENTS.apply(new String[]{
				StringUtils.trimToEmpty(rscaDAO.getRscaFollowupNote()),
				userService.getUserName(Long.valueOf(userId)),
				dateToLocalDateTime.apply(systemTimestamp).format(DATE_TIME_FORMATTER),
				StringUtils.trimToEmpty(followupReqDTO.getNotes())}));
		rscaDAO.setRscaLastUpdBy(userId);
		rscaDAO.setRscaLastUpdUsing("ADD_FOLLOWUP_API");
		rscaRepository.save(rscaDAO);

		/*ReseaCaseRscsDAO rscsDAO = rscaDAO.getRscsDAO();
		if (rscsDAO.getRscsNxtFolUpDt() == null) {
			rscsDAO.setRscsNxtFolUpDt(followupReqDTO.getFollowUpCompByDt());
			rscsDAO.setRscsNxtFolUpTypeCdALV(rscaDAO.getRscaFollowupTypeCdALV());
			rscsDAO.setRscsLastUpdBy(userId);
			rscsDAO.setRscsLastUpdUsing("ADD_FOLLOWUP_API");
			rscsRepository.save(rscsDAO);
		} else {
			List<ReseaCaseActivityRscaDAO> nextFolUpRscaDaoList = rscaRepository
					.getNextFolloupActivity(rscsDAO.getRscsId(), rscaDAO.getRscaId());
			if (CollectionUtils.isEmpty(nextFolUpRscaDaoList) && nextFolUpRscaDaoList.get(0)
					.getRscaFollowupDt().before(followupReqDTO.getFollowUpCompByDt())) {
				ReseaCaseActivityRscaDAO nextFolUpRscaDao = nextFolUpRscaDaoList.get(0);
				rscsDAO.setRscsNxtFolUpDt(nextFolUpRscaDao.getRscaFollowupDt());
				rscsDAO.setRscsNxtFolUpTypeCdALV(nextFolUpRscaDao.getRscaFollowupTypeCdALV());
				rscsDAO.setRscsLastUpdBy(userId);
				rscsDAO.setRscsLastUpdUsing("ADD_FOLLOWUP_API");
				rscsRepository.save(rscsDAO);
			} else {
				rscsDAO.setRscsNxtFolUpDt(followupReqDTO.getFollowUpCompByDt());
				rscsDAO.setRscsNxtFolUpTypeCdALV(rscaDAO.getRscaFollowupTypeCdALV());
				rscsDAO.setRscsLastUpdBy(userId);
				rscsDAO.setRscsLastUpdUsing("ADD_FOLLOWUP_API");
				rscsRepository.save(rscsDAO);
			}
		}*/
		return "Follow-up Added Successfully";
    }

	public String editFollowup(CaseActivityFollowupEditReqDTO followupReqDTO, String userId, String roleId) {
		ReseaCaseActivityRscaDAO rscaDAO = rscaRepository.findById(followupReqDTO.getActivityId())
				.orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + followupReqDTO.getActivityId(), "activityId.notFound"));
		Date systemDate = parRepository.getCurrentDate();
		Date systemTimestamp = parRepository.getCurrentTimestamp();
		final HashMap<String, List<String>> errorMap = caseSummValidator.validateEditFollowup(
				followupReqDTO, rscaDAO, userId, roleId, systemDate);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException("Edit Follow-up Request Failed", errorMap);
		}
		rscaDAO.setRscaFollowupCompDt(followupReqDTO.getCompletionDt());
		rscaDAO.setRscaFollowupDoneInd(followupReqDTO.getComplete());
		rscaDAO.setRscaFollowupNote(GENERATECOMMENTS.apply(new String[]{
				StringUtils.trimToEmpty(rscaDAO.getRscaFollowupNote()),
				userService.getUserName(Long.valueOf(userId)),
				dateToLocalDateTime.apply(systemTimestamp).format(DATE_TIME_FORMATTER),
				StringUtils.trimToEmpty(followupReqDTO.getNotes())}));
		rscaDAO.setRscaLastUpdBy(userId);
		rscaDAO.setRscaLastUpdUsing("EDIT_FOLLOWUP_API");
		rscaRepository.save(rscaDAO);

		/*if (INDICATOR_YES.equals(followupReqDTO.getComplete())) {
			ReseaCaseRscsDAO rscsDAO = rscaDAO.getRscsDAO();
			List<ReseaCaseActivityRscaDAO> nextFolUpRscaDaoList = rscaRepository
					.getNextFolloupActivity(rscsDAO.getRscsId(), rscaDAO.getRscaId());
			if (!CollectionUtils.isEmpty(nextFolUpRscaDaoList)) {
				ReseaCaseActivityRscaDAO nextFolUpRscaDao = nextFolUpRscaDaoList.get(0);
				rscsDAO.setRscsNxtFolUpDt(nextFolUpRscaDao.getRscaFollowupDt());
				rscsDAO.setRscsNxtFolUpTypeCdALV(nextFolUpRscaDao.getRscaFollowupTypeCdALV());
				rscsDAO.setRscsLastUpdBy(userId);
				rscsDAO.setRscsLastUpdUsing("EDIT_FOLLOWUP_API");
				rscsRepository.save(rscsDAO);
			} else {
				rscsDAO.setRscsNxtFolUpDt(null);
				rscsDAO.setRscsNxtFolUpTypeCdALV(null);
				rscsDAO.setRscsLastUpdBy(userId);
				rscsDAO.setRscsLastUpdUsing("EDIT_FOLLOWUP_API");
				rscsRepository.save(rscsDAO);
			}
		}*/
		return "Follow-up Edited Successfully";
	}

	public ReseaViewActivityResDTO viewCaseActivity(Long activityId, String loginUserId, String loginRoleId) {
		ReseaCaseActivityRscaDAO rscaDAO = rscaRepository.findById(activityId)
				.orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + activityId, "activityId.notFound"));
		ReseaInterviewResDTO interviewResDTO = null;
		HeaderDetailsResDTO headerDetailsDTO = null;
		String apptUsageDesc = null;
		if (RscaStatusCd.COMPLETED.getCode().equals(rscaDAO.getRscaStatusCdALV().getAlvId())) {
			Long rsicTimeSlotUsageCd = ReseaUtilFunction.rscaStageToRsicUsage(rscaDAO.getRscaStageCdALV().getAlvId());
			ReseaIntvwerCalRsicDAO rsicDAO = rsicRepo.getCompletedApptByCaseStage(
					rscaDAO.getRscsDAO().getRscsId(), rsicTimeSlotUsageCd, RSIC_MTG_STATUS_COMPLETED);
			if (rsicDAO != null) {
				apptUsageDesc = rsicDAO.getRsicTimeslotUsageCdAlv().getAlvShortDecTxt();
				interviewResDTO = reseaInterviewService.loadInterviewDetails(rsicDAO.getRsicId());
				headerDetailsDTO = calendarEventService.getCaseHeaderDetails(rsicDAO.getRsicId(), loginUserId, loginRoleId);
			}
		}
		List<ReseaCaseNoteRscnDAO> rscnDAOList = rscnRepository.findByCaseActivity(rscaDAO.getRscaId());
		String activityNotes = CollectionUtils.isEmpty(rscnDAOList) ? null
					: rscnDAOList.get(0).getRscnNote();
		final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		return new ReseaViewActivityResDTO()
				.withCaseNumber(rscaDAO.getRscsDAO().getRscsId())
				.withClaimantName(rscaDAO.getRscsDAO().getClmDAO().getClaimantDAO().getClaimantName())
				.withClmBeginDt(rscaDAO.getRscsDAO().getClmDAO().getClmEffectiveDt())
				.withClaimByeDt(rscaDAO.getRscsDAO().getClmDAO().getClmBenYrEndDt())
				.withActivityType(rscaDAO.getRscaTypeCdALV().getAlvShortDecTxt())
				.withActivityDate(stringToDate.apply(dateToString.apply(rscaDAO.getRscaActivtyTs())))
				.withActivityTime(formatter.format(rscaDAO.getRscaActivtyTs()))
				.withActivityDesc(rscaDAO.getRscaDescription())
				.withActivityDetail(rscaDAO.getRscaDetails())
				.withActivitySynopsis(rscaDAO.getRscaCaseSynopsis())
				.withActivityNote(activityNotes)
				.withFollowUpType(rscaDAO.getRscaFollowupTypeCdALV() != null ? rscaDAO.getRscaFollowupTypeCdALV().getAlvShortDecTxt() : null)
				.withFollowUpDt(rscaDAO.getRscaFollowupDt())
				.withFollowUpNote(rscaDAO.getRscaFollowupNote())
				.withFollowUpCompleteDt(rscaDAO.getRscaFollowupCompDt())
				.withApptUsageDesc(apptUsageDesc)
				.withCaseStatusDesc(rscaDAO.getRscaStatusCdALV().getAlvShortDecTxt())
				.withCaseStageDesc(rscaDAO.getRscaStageCdALV().getAlvShortDecTxt())
				.withHeaderDetailsDTO(headerDetailsDTO)
				.withInterviewResDTO(interviewResDTO);
	}

	public String addCaseActivity(CaseActivityAddReqDTO addActivityReqDTO,
								  String loginStfUsrId, String roleId) {
		ReseaCaseRscsDAO rscsDAO = rscsRepository.findById(addActivityReqDTO.getCaseId())
				.orElseThrow(() -> new NotFoundException("Invalid Case ID:" + addActivityReqDTO.getCaseId(), CASE_ID_NOT_FOUND));
		final String template = rsacRepository.getActivityTemplate(addActivityReqDTO.getActivityType())
				.orElseThrow(() -> new NotFoundException("Template not found for Activity: "
						+ addActivityReqDTO.getActivityType(), "activityType.template.notFound"));
		final Date systemDate = parRepository.getCurrentDate();
		final Date systemTimestamp = parRepository.getCurrentTimestamp();
		final HashMap<String, List<String>> errorMap = caseSummValidator.validateAddCaseActivity(addActivityReqDTO,
				rscsDAO, template, loginStfUsrId, roleId, systemDate, systemTimestamp);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException("Edit Follow-up Request Failed", errorMap);
		}
		ReseaCaseActivityRscaDAO rscaDAO = createActivity(addActivityReqDTO, rscsDAO, loginStfUsrId);
		rscsDAO = updateCaseTrainingDetails(addActivityReqDTO, rscsDAO, template, loginStfUsrId);
		generateActivityIssue(addActivityReqDTO, rscaDAO, template, loginStfUsrId);
		return "Case Activity Successfully Generated";
	}

	private ReseaCaseActivityRscaDAO createActivity(CaseActivityAddReqDTO addActivityReqDTO, ReseaCaseRscsDAO rscsDAO, String loginStfUsrId) {
		ReseaCaseActivityRscaDAO rscaDAO = new ReseaCaseActivityRscaDAO();
		rscaDAO.setRscsDAO(rscsDAO);
		rscaDAO.setRscaStageCdALV(alvRepo.findById(getRscaStageFromRscsStage(rscsDAO.getRscsStageCdALV().getAlvId())).get());
		rscaDAO.setRscaStatusCdALV(alvRepo.findById(getRscaStatusFromRscsStatus(rscsDAO.getRscsStatusCdALV().getAlvId())).get());
		rscaDAO.setRscaTypeCdALV(alvRepo.findById(addActivityReqDTO.getActivityType())
				.orElseThrow(() -> new NotFoundException("Invalid Activity Type:" + addActivityReqDTO.getActivityType(),
						"activityType.notFound")));
		rscaDAO.setRscaActivtyTs(string24HToDate().apply(dateToString.apply(addActivityReqDTO.getAddDate()),
				convert12hTo24h().apply(addActivityReqDTO.getAddTime())));
		rscaDAO.setRscaDescription(addActivityReqDTO.getShortDesc());
		rscaDAO.setRscaCaseSynopsis(addActivityReqDTO.getSynopsis());
		rscaDAO.setRscaDetails(addActivityReqDTO.getDetails());
		rscaDAO.setRscaReferenceIfkCd(alvRepo.findById(RscaReferenceIfkCd.RSCS.getCode()).get());
		rscaDAO.setRscaReferenceIfk(rscsDAO.getRscsId());
		rscaDAO.setRscaCreatedBy(loginStfUsrId);
		rscaDAO.setRscaCreatedUsing(API_NEW_ACTIVITY);
		rscaDAO.setRscaLastUpdBy(loginStfUsrId);
		rscaDAO.setRscaLastUpdUsing(API_NEW_ACTIVITY);
		return rscaRepository.save(rscaDAO);
	}

	private ReseaCaseRscsDAO updateCaseTrainingDetails(CaseActivityAddReqDTO addActivityReqDTO, ReseaCaseRscsDAO rscsDAO, String template,
													   String loginStfUsrId) {
		if (ACTIVITY_TEMPLATE_TRN.equals(template)) {
			if (Objects.equals(RscaTypeCd.APPROVED_TRAINING.getCode(),
					addActivityReqDTO.getActivityType())) {
				rscsDAO.setRscsTrngApprovalDt(addActivityReqDTO.getAddDate());
			} else if (Objects.equals(RscaTypeCd.REFER_TO_TRAINING.getCode(),
					addActivityReqDTO.getActivityType())) {
				rscsDAO.setRscsTrngReferralDt(addActivityReqDTO.getAddDate());
			} else if (Objects.equals(RscaTypeCd.REPORTED_TO_TRAINING_RESEA_TERMINATED.getCode(),
					addActivityReqDTO.getActivityType())) {
				rscsDAO.setRscsTrngReportedDt(addActivityReqDTO.getAddDate());
			}
			rscsDAO.setRscsLastUpdBy(loginStfUsrId);
			rscsDAO.setRscsLastUpdUsing(API_NEW_ACTIVITY);
			rscsRepository.save(rscsDAO);
		}
		return rscsDAO;
	}

	private void generateActivityIssue(CaseActivityAddReqDTO addActivityReqDTO, ReseaCaseActivityRscaDAO rscaDAO, String template, String loginStfUsrId) {
		if (ReseaConstants.ACTIVITY_TEMPLATE_ISS.equals(template)) {
			final ReseaIssueIdentifiedRsiiDAO rsiiDAO = new ReseaIssueIdentifiedRsiiDAO();
			final CreateIssuesDTO issueDTO = addActivityReqDTO.getIssueDetail();
			AllowValAlvDAO issueDuringAlv = null;
			if (rscaDAO.getRscaStageCdALV().getAlvId().longValue() == RscaStageCd.INITIAL_APT.getCode().longValue()) {
				issueDuringAlv = alvRepo.findById(RSII_ISSUE_ID_DURING_INITIAL_APPT).orElseThrow();
			}
			if (rscaDAO.getRscaStageCdALV().getAlvId().longValue() == RscaStageCd.FIRST_SUBS_APT.getCode().longValue()) {
				issueDuringAlv = alvRepo.findById(RSII_ISSUE_ID_DURING_FIRST_SUB_APPT).orElseThrow();
			}
			if (rscaDAO.getRscaStageCdALV().getAlvId().longValue() == RscaStageCd.SECOND_SUBS_APT.getCode().longValue()) {
				issueDuringAlv = alvRepo.findById(RSII_ISSUE_ID_DURING_SECOND_SUB_APPT).orElseThrow();
			}
			rsiiDAO.setRscaDAO(rscaDAO);
			rsiiDAO.setNmiDAO(nmiRepo.findById(issueDTO.getIssueSubType())
					.orElseThrow(() -> new NotFoundException("Invalid Issue Sub Type:" + issueDTO.getIssueSubType(),
							"issueSubType.notFound")));
			rsiiDAO.setRsiiIssueEffDt(issueDTO.getStartDt());
			rsiiDAO.setRsiiIssueEndDt(issueDTO.getEndDt());
			rsiiDAO.setRsiiIssueIdDuringCdALV(issueDuringAlv);
			rsiiDAO.setRsiiIssueIdentifiedBy(Long.valueOf(loginStfUsrId));
			rsiiDAO.setRsiiSourceIfkCd(alvRepo.findById(RSII_DEC_IFK_RSID).orElseThrow());
			rsiiDAO.setRsiiSourceIfk(rscaDAO.getRscaId());
			rsiiDAO.setRsiiDecDetectDtInd(ReseaConstants.RSII_DEC_DETECT_DT_IND.SYSTEM_DT.getCode());
			rsiiDAO.setRsiiCreatedBy(loginStfUsrId);
			rsiiDAO.setRsiiCreatedUsing(API_NEW_ACTIVITY);
			rsiiDAO.setRsiiLastUpdBy(loginStfUsrId);
			rsiiDAO.setRsiiLastUpdUsing(API_NEW_ACTIVITY);
			rsiiRepo.save(rsiiDAO);

		}
	}

	public List<CorrStandardTextResDTO> activityStandardText() {
		return ADD_ACTIVITY_STANDARD_TXT.entrySet().stream()
				.map(entry -> new CorrStandardTextResDTO(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
}
