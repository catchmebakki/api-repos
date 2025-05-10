package com.ssi.ms.resea.service;

import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.dao.StaffStfDAO;
import com.ssi.ms.resea.database.repository.LofStfLsfRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.StaffStfRepository;
import com.ssi.ms.resea.dto.CaseManagerAvailabilityResDTO;
import com.ssi.ms.resea.dto.IdNameResDTO;
import com.ssi.ms.resea.dto.OfficeResDTO;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_FIRST_SUBS_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_INITIAL_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_PRE_RESEA;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_SECOND_SUBS_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_BENEFIT_YEAR_ENDED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_COMPLETED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_SECOND_SUB_COMPLETED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV;

/**
 * {@code ReseaStaffService} is a service component in the application responsible for
 * handling business logic related to Staff Services.
 *
 * This service performs operations fetch the list of Staff users belongs to RESEA.
 *
 * @author Sitaram
 */
@Service
@Slf4j
public class ReseaStaffService extends ReseaBaseService {
	@Autowired
	StaffStfRepository stfRepository;
	@Autowired
	ReseaCaseRscsRepository rscsRepository;
	@Autowired
	LofStfLsfRepository lsfRepository;
	@Autowired
	ReseaIntvwerCalRsicRepository rsicRepository;
	public List<IdNameResDTO> getReseaCaseManagerList() {
		return stfRepository.getReseaCaseManagerList();
	}

	public List<Long> getLocalOfficeManagerReseaCaseManagerList(Long caseManagerUserId) {
		return stfRepository.getReseaCaseManagerIdListByLocalOfficeManager(caseManagerUserId,
				ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode());
	}

	public List<IdNameResDTO> getReseaCaseManagerList(Long eventId) {
		final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("Invalid Event ID:" + eventId, EVENT_ID_NOT_FOUND));
		return stfRepository.getReseaCaseManagerList(
				rsicDAO.getRsisDAO().getLofDAO().getLofId(),
				rsicDAO.getRsisDAO().getStfDAO().getStfId());
	}

    public List<CaseManagerAvailabilityResDTO> getReassignCaseManagers(Long caseId, String caseOffice) {
		final ReseaCaseRscsDAO rscsDAO = rscsRepository.findById(caseId)
				.orElseThrow(() -> new NotFoundException("Invalid Event ID:" + caseId, CASE_ID_NOT_FOUND));
		final Long oldStfId = rscsDAO.getStfDAO().getStfId();
		Long apptUsage = null;
		Date prevStageApptDt = null;
		Long caseStage = rscsDAO.getRscsStageCdALV().getAlvId();
		if (RSCS_STATUS_COMPLETED.equals(rscsDAO.getRscsStatusCdALV().getAlvId())) {
			caseStage = switch ((int) caseStage.longValue()) {
				case (int) RSCS_STAGE_PRE_RESEA -> RSCS_STAGE_INITIAL_APPT;
				case (int) RSCS_STAGE_INITIAL_APPT -> RSCS_STAGE_FIRST_SUBS_APPT;
				case (int) RSCS_STAGE_FIRST_SUBS_APPT -> RSCS_STAGE_SECOND_SUBS_APPT;
				default -> null;
			};
		}
		if (caseStage != null) {
			switch ((int) caseStage.longValue()) {
				case (int) RSCS_STAGE_INITIAL_APPT -> {
					apptUsage = RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV;
					prevStageApptDt = rscsDAO.getRscsOrientationDt();
				}
				case (int) RSCS_STAGE_FIRST_SUBS_APPT -> {
					apptUsage = RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV;
					prevStageApptDt = rscsDAO.getRscsInitApptDt();
				}
				case (int) RSCS_STAGE_SECOND_SUBS_APPT -> {
					apptUsage = RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV;
					prevStageApptDt = rscsDAO.getRscsFirstSubsApptDt();
				}
				default -> {
				}
			}
		}
		final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findByScheduledByClaimId(rscsDAO.getClmDAO().getClmId(), apptUsage);
		final List<Long> caseLofList = lsfRepository.getCaseLocalOfficesId(caseId);
		final Long oldLofId = Collections.isEmpty(caseLofList) ? 0L : caseLofList.get(0);
		List<CaseManagerAvailabilityResDTO> caseManagerList = null;
		if (apptUsage == null) {
			if (INDICATOR_YES.equals(caseOffice)) {
				if (rsicDAO != null) {
					caseManagerList = rscsRepository.getLofReassignCaseManagers(apptUsage, prevStageApptDt, oldLofId, oldStfId,
							RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
							List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
							List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
							rscsDAO.getClmDAO().getClmBenYrEndDt());
				} else {
					caseManagerList = rscsRepository.getLofReassignCaseManagersNoSch(apptUsage, prevStageApptDt, oldLofId, oldStfId,
							RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
							List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
							List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
							rscsDAO.getClmDAO().getClmBenYrEndDt());
				}
			} else {
				if (rsicDAO != null) {
					caseManagerList = rscsRepository.getOtherOfficeReassignCaseManagers(apptUsage, prevStageApptDt, oldLofId, oldStfId,
							RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
							List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
							List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
							rscsDAO.getClmDAO().getClmBenYrEndDt());
				} else {
					caseManagerList = rscsRepository.getOtherOfficeReassignCaseManagersNoSch(apptUsage, prevStageApptDt, oldLofId, oldStfId,
							RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV,
							List.of(RSCS_STATUS_SECOND_SUB_COMPLETED, RSCS_STATUS_BENEFIT_YEAR_ENDED),
							List.of(RSCS_STAGE_INITIAL_APPT, RSCS_STAGE_FIRST_SUBS_APPT, RSCS_STAGE_SECOND_SUBS_APPT),
							rscsDAO.getClmDAO().getClmBenYrEndDt());
				}
			}
		}
		return caseManagerList;
    }

	public List<IdNameResDTO> getAllApptReseaCaseManagerList() {
		List<IdNameResDTO> caseMgrList = stfRepository.getAllApptReseaCaseManagerList()
				.stream()
				.map(stfDAO -> new IdNameResDTO(
						stfDAO.getUserDAO().getUserId(),
						stfDAO.getStfFirstName() + ReseaConstants.space + stfDAO.getStfLastName()))
				.collect(Collectors.toList());
		return caseMgrList;
	}
}