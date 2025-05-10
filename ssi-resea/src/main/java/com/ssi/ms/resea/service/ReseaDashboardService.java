package com.ssi.ms.resea.service;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.mapper.DashboardCalMapper;
import com.ssi.ms.resea.database.mapper.IssueDecisionDecMapper;
import com.ssi.ms.resea.database.mapper.ReseaJobReferralRsjrMapper;
import com.ssi.ms.resea.database.mapper.ReseaRmtMtgInfoRsrmMapper;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.IssueDecisionDecRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwDetRsidRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.ReseaRmtMtgInfoRsrmRepository;
import com.ssi.ms.resea.database.repository.WeeklyWorkSearchWwsRepository;
import com.ssi.ms.resea.dto.CaseLoadMetricsResDTO;
import com.ssi.ms.resea.dto.CaseLoadSummaryResDTO;
import com.ssi.ms.resea.dto.DashboardCalReqDTO;
import com.ssi.ms.resea.dto.DashboardCalResDTO;
import com.ssi.ms.resea.dto.ReseaCaseLoadViewReqDTO;
import com.ssi.ms.resea.dto.ReseaCaseLoadViewResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_FIRST_SUBS_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_INITIAL_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_SECOND_SUBS_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_BENEFIT_YEAR_ENDED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_FAILED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_SCHEDULED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_SECOND_SUB_COMPLETED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_WAITLIST_REQ;
import static com.ssi.ms.resea.util.ReseaUtilFunction.convert24hTo12h;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getReseaEventLabel;

@Slf4j
@Service
public class ReseaDashboardService {

    @Autowired
    private DashboardCalMapper calMapper;
    @Autowired
    private ReseaIntvwerCalRsicRepository rsicRepository;
    @Autowired
    private ReseaRmtMtgInfoRsrmRepository rsrmRepository;
    @Autowired
    private ReseaIntvwDetRsidRepository rsidRepository;
    @Autowired
    private IssueDecisionDecRepository decRepository;
    @Autowired
    private WeeklyWorkSearchWwsRepository wwsRepository;
    @Autowired
    private ReseaRmtMtgInfoRsrmMapper rsrmMapper;
    @Autowired
    private IssueDecisionDecMapper decMapper;
    @Autowired
    private ReseaJobReferralRsjrMapper rsjrMapper;
    @Autowired
    private ReseaCaseRscsRepository rscsRepository;
    @Autowired
    private AllowValAlvRepository alvRepository;

    public List<DashboardCalResDTO> getStaffDashboardCalendar(DashboardCalReqDTO calDTO) {
        return rsicRepository.getInterviewerCal(calDTO.getUserId(), calDTO.getStartDt(), calDTO.getEndDt())
                .stream().map(dao -> calMapper.daoToDto(dao))
                .map(dto -> dto.withEventSubmitted(dto.getSubmitId() != null && dto.getSubmitId() > 0L))
                .map(dto -> dto.withLabel(getReseaEventLabel().apply(dto)))
                .map(dto -> dto.withStartTime(convert24hTo12h().apply(dto.getStartTime())))
                .map(dto -> dto.withEndTime(convert24hTo12h().apply(dto.getEndTime())))
                .toList();
    }

    public CaseLoadMetricsResDTO getCaseLoadMetrics(Long userId) {
        return new CaseLoadMetricsResDTO()
                .withInitialInterview(rscsRepository.getStageCaseLoadMetrics(userId, RSCS_STAGE_INITIAL_APPT,
                        List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED)))
                .withFirstSubInterview(rscsRepository.getStageCaseLoadMetrics(userId, RSCS_STAGE_FIRST_SUBS_APPT,
                        List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED)))
                .withSecondSubInterview(rscsRepository.getStageCaseLoadMetrics(userId, RSCS_STAGE_SECOND_SUBS_APPT,
                        List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED)))
                .withPendingSchedule(rscsRepository.getCaseStatusLoadMetrics(userId, List.of(RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF),
                        List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT)))
                .withFollowUp(rscsRepository.getFolloupCaseLoadMetrics(userId,
                        List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                        List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED)))
                .withHiPriority(rscsRepository.getHiPriorityCaseLoadMetrics(userId,
                        RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                        List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED)))
                .withFailed(rscsRepository.getCaseStatusLoadMetrics(userId, List.of(RSCS_STATUS_FAILED),
                        List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT)))
                .withDelayed(rscsRepository.getDelayedCaseLoadMetrics(userId, RSCS_STAGE_INITIAL_APPT,
                        RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT, List.of(RSCS_STATUS_WAITLIST_REQ,
                                RSCS_STATUS_SCHEDULED, RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF, RSCS_STATUS_FAILED)))
                .withWaitlisted(rscsRepository.getWaitlistedCaseLoadMetrics(userId,
                        List.of(ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode()),
                        List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT)));
    }

    public ReseaCaseLoadViewResDTO getCaseLoadSummary(ReseaCaseLoadViewReqDTO caseLoadViewReqDTO, String roleId) {
        List<CaseLoadSummaryResDTO> caseLoadSummaryResDTO;
        if (ReseaConstants.METRIC_TYPE.All.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getStageCaseSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.initialAppointment.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getStageCaseSummary(caseLoadViewReqDTO.getUserId(), List.of(RSCS_STAGE_INITIAL_APPT),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.firstSubsequentAppointment.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getStageCaseSummary(caseLoadViewReqDTO.getUserId(), List.of(RSCS_STAGE_FIRST_SUBS_APPT),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.secondSubsequentAppointment.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getStageCaseSummary(caseLoadViewReqDTO.getUserId(), List.of(RSCS_STAGE_SECOND_SUBS_APPT),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.hiPriority.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getHiPriorityCaseLoadSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.followUp.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getFolloupCaseLoadSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.delayed.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getDelayedCaseLoadSummary(caseLoadViewReqDTO.getUserId(),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    List.of(RSCS_STATUS_WAITLIST_REQ, RSCS_STATUS_SCHEDULED, RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF, RSCS_STATUS_FAILED),
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.failed.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getCaseStatusLoadSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(RSCS_STATUS_FAILED),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.pendingSchedule.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getCaseStatusLoadSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(RSCS_STATUS_PENDING_SCHEDULE_BY_STAFF),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else if (ReseaConstants.METRIC_TYPE.waitlisted.label.equals(caseLoadViewReqDTO.getMetric())) {
            caseLoadSummaryResDTO = rscsRepository.getWaitlistedCaseStatusLoadSummary(caseLoadViewReqDTO.getUserId(),
                    List.of(ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode()),
                    RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT,
                    RSCS_STATUS_SCHEDULED);
        } else {
            throw new CustomValidationException("Validation Error", Map.of("metric", List.of("metric.invalid")));
        }
        if (caseLoadViewReqDTO.getSortBy() != null && caseLoadSummaryResDTO != null) {
            Comparator<CaseLoadSummaryResDTO> caseLoadSort = switch (caseLoadViewReqDTO.getSortBy().getField()) {
                case "claimantName" -> Comparator.comparing(CaseLoadSummaryResDTO::getClaimantName, Comparator.nullsLast(Comparator.reverseOrder()));
                case "byeDt" -> Comparator.comparing(CaseLoadSummaryResDTO::getByeDt, Comparator.nullsLast(Comparator.reverseOrder()));
                case "stage" -> Comparator.comparing(CaseLoadSummaryResDTO::getStage, Comparator.nullsLast(Comparator.reverseOrder()));
                case "status" -> Comparator.comparing(CaseLoadSummaryResDTO::getStatus, Comparator.nullsLast(Comparator.reverseOrder()));
                case "ccaWeeks" -> Comparator.comparing(CaseLoadSummaryResDTO::getCcaWeeks, Comparator.nullsLast(Comparator.reverseOrder()));
                case "followUpDt" -> Comparator.comparing(CaseLoadSummaryResDTO::getFollowUpDt, Comparator.nullsLast(Comparator.reverseOrder()));
                case "indicator" -> Comparator.comparing(CaseLoadSummaryResDTO::getIndicator, Comparator.nullsLast(Comparator.reverseOrder()));
                default -> Comparator.comparing(CaseLoadSummaryResDTO::getStatusDt, Comparator.nullsLast(Comparator.reverseOrder()));
            };
            if (caseLoadSort != null) {
                caseLoadSort.thenComparing(CaseLoadSummaryResDTO::getCaseNum);
                if ("desc".equals(caseLoadViewReqDTO.getSortBy().getDirection())) {
                    caseLoadSummaryResDTO.sort(caseLoadSort.reversed());
                } else {
                    caseLoadSummaryResDTO.sort(caseLoadSort);
                }
            }
        }
        ReseaCaseLoadViewResDTO caseLoadViewResDTO;
        PaginationDTO pageDTO = caseLoadViewReqDTO.getPagination();
        int rolId = Integer.parseInt(roleId);
        if (caseLoadViewReqDTO.getPagination() != null && caseLoadSummaryResDTO != null) {
            int minIndex = pageDTO.getPageSize() * (pageDTO.getPageNumber() - 1);
            int maxIndex = Math.min(pageDTO.getPageSize() * pageDTO.getPageNumber() - 1, caseLoadSummaryResDTO.size());
            if (minIndex > caseLoadSummaryResDTO.size()) {
                minIndex = 0;
                maxIndex = Math.min(pageDTO.getPageSize(), caseLoadSummaryResDTO.size());
            }
            caseLoadViewResDTO = ReseaCaseLoadViewResDTO.builder()
                    .reassignInd(ROL_LOCAL_OFFICE_MANAGER == rolId || ROL_RESEA_PROG_STAFF == rolId)
                    .caseLoadSummaryList(caseLoadSummaryResDTO.subList(minIndex, maxIndex))
                    .pagination(pageDTO.withTotalItemCount((long) caseLoadSummaryResDTO.size())).build();
        } else {
            caseLoadViewResDTO = ReseaCaseLoadViewResDTO.builder()
                    .reassignInd(ROL_LOCAL_OFFICE_MANAGER == rolId || ROL_RESEA_PROG_STAFF == rolId)
                    .caseLoadSummaryList(caseLoadSummaryResDTO).build();
        };
        return caseLoadViewResDTO;
    }
}
