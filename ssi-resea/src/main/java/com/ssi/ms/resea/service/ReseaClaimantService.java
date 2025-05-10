package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.resea.database.repository.LofStfLsfRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.ReseaRatRepository;
import com.ssi.ms.resea.dto.AvailableClaimantResDTO;
import com.ssi.ms.resea.dto.AvaliableApptReqDTO;
import com.ssi.ms.resea.validator.ReseaClaimantValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.getPrevSundayDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ReseaConstants.LOF_INTERSTATE;
import static com.ssi.ms.resea.constant.ReseaConstants.PAR_RESEA_NO_CCF_WEEKS;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV;

/**
 * {@code ReseaNoShowService} is a service component in the application responsible for
 * handling business logic related to No Show functionality.
 * This service performs operations updating the No show business requirements to DB.
 *
 * @author Anand
 */
@Service
@Slf4j
public class ReseaClaimantService extends ReseaBaseService {

	@Autowired
	private ParameterParRepository commonParRepository;
	@Autowired
	private LofStfLsfRepository lsfRepository;
	@Autowired
	private LocalOfficeLofRepository lofRepository;
	@Autowired
	private ReseaIntvwerCalRsicRepository rsicRepository;
	@Autowired
	private ReseaRatRepository ratRepository;
	@Autowired
	private ReseaCaseRscsRepository rscsRepository;
	@Autowired
	private ReseaClaimantValidator cmtValidator;

	private final String SCHEDULED_BEYOND_21 = "ScheduleBeyond21";
	private final String NO_SHOW = "NoShows";
	private final String NOT_SCHEDULED = "NotScheduled";
	private final String WAIT_LISTED = "WaitListed";
	private  final String ALL = "ALL";
	public List<AvailableClaimantResDTO> getAvailableClaimants(AvaliableApptReqDTO apptReqDTO,
                                                    String userId,
                                                    String roleId) {
		final Date systemDate = commonParRepository.getCurrentDate();
		final ReseaIntvwerCalRsicDAO rsicDAO = rsicRepository.findById(apptReqDTO.getEventId())
				.orElseThrow(() -> new NotFoundException("Invalid Event ID:" + apptReqDTO.getEventId(), EVENT_ID_NOT_FOUND));
		final HashMap<String, List<String>> errorMap = cmtValidator
				.validateAvailableAppointment(apptReqDTO, rsicDAO, userId, roleId, systemDate);
		if (!errorMap.isEmpty()) {
			throw new CustomValidationException("Claimant Retrieval Failed", errorMap);
		}
		List<AvailableClaimantResDTO> claimantList = null;
		List<Long> staffUserIdList;
		if(apptReqDTO.getUserId() == -1L) { // User ID is -1, request is to get all staff from the Local Office
			staffUserIdList = lsfRepository.getLocalOfficeStaffUserList(rsicDAO.getRsisDAO().getLofDAO().getLofId());
		} else {
			staffUserIdList = List.of(apptReqDTO.getUserId());
		}
		if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_INITIAL_APPT_ALV) {
			claimantList = getInitialApptClaimants(apptReqDTO, rsicDAO, staffUserIdList);
		} else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV) {
			claimantList = getFirstSubsequentApptClaimants(apptReqDTO, rsicDAO, staffUserIdList);
		} else if (rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId() == RSIC_TIMESLOT_USAGE_SECOND_SUBSEQUENT_APPT_ALV) {
			claimantList = getSecondSubsequentApptClaimants(apptReqDTO, rsicDAO, staffUserIdList);
		}
		return claimantList;
	}

	private List<AvailableClaimantResDTO> getInitialApptClaimants(AvaliableApptReqDTO apptReqDTO,
																  ReseaIntvwerCalRsicDAO rsicDAO,
																  List<Long> staffUserIdList) {
		List<AvailableClaimantResDTO> claimantList;
        switch (apptReqDTO.getStatus()) {
            case SCHEDULED_BEYOND_21 ->
				claimantList = rscsRepository.getInitialApptBeyond21(staffUserIdList, rsicDAO.getRsicCalEventDt(),
						ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case NO_SHOW ->
				claimantList = ratRepository.getNoShowInitialClaimantList(rsicDAO.getRsicCalEventDt(), staffUserIdList,
						ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
			case WAIT_LISTED -> claimantList = rscsRepository.getWaitlistClaimants(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.RscsStageCd.INITIAL_APT.getCode(),
					ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case ALL, NOT_SCHEDULED -> {
				ParameterParDao parDao = commonParRepository.findByParShortName(PAR_RESEA_NO_CCF_WEEKS);
				Date ccaDt = localDateToDate.apply(dateToLocalDate.apply(getPrevSundayDate.apply(commonParRepository.getCurrentDate()))
						.minusDays(1 + (parDao.getParNumericValue() * 7)));
				List<Long> officeIds = lofRepository.findAllReseaOrphanedLof(ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(),
						Long.valueOf(ROL_RESEA_CASE_MANAGER), ReseaAlvEnumConstant.UsrStatusCd.ACTIVE.getCode());
				officeIds.add(rsicDAO.getRsisDAO().getLofDAO().getLofId());
				claimantList = ratRepository.getInitialClaimantList(rsicDAO.getRsicCalEventDt(), ccaDt, officeIds);
				if (CollectionUtils.isEmpty(claimantList)) {
					claimantList = rscsRepository.getInitialClaimantList(staffUserIdList, rsicDAO.getRsicCalEventDt(), ccaDt,
							ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
				} else {
					claimantList.addAll(rscsRepository.getInitialClaimantList(staffUserIdList, rsicDAO.getRsicCalEventDt(), ccaDt,
							ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE));
				}
            }
			default -> claimantList = new ArrayList<>();
        }
		return claimantList;
	}

	private List<AvailableClaimantResDTO> getFirstSubsequentApptClaimants(AvaliableApptReqDTO apptReqDTO,
                                                               ReseaIntvwerCalRsicDAO rsicDAO,
                                                               List<Long> staffUserIdList) {
		return switch (apptReqDTO.getStatus()) {
            case SCHEDULED_BEYOND_21 ->
                    rscsRepository.getFistSubsequentApptBeyond21(staffUserIdList, rsicDAO.getRsicCalEventDt(),
							ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case NO_SHOW -> rscsRepository.getFistSubsequentApptNoShow(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case NOT_SCHEDULED ->
                    rscsRepository.getNotScheduledFistSubsequentAppt(staffUserIdList, rsicDAO.getRsicCalEventDt(),
							ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
			case WAIT_LISTED -> rscsRepository.getWaitlistClaimants(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.RscsStageCd.FIRST_SUBS_APT.getCode(),
					ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case ALL -> rscsRepository.getFistSubsequentAppt(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            default -> new ArrayList<>();
        };
	}

	private List<AvailableClaimantResDTO> getSecondSubsequentApptClaimants(AvaliableApptReqDTO apptReqDTO,
                                                                ReseaIntvwerCalRsicDAO rsicDAO,
                                                                List<Long> staffUserIdList) {
		return switch (apptReqDTO.getStatus()) {
            case SCHEDULED_BEYOND_21 ->
                    rscsRepository.getSecondSubsequentApptBeyond21(staffUserIdList, rsicDAO.getRsicCalEventDt(),
							ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case NO_SHOW -> rscsRepository.getSecondSubsequentApptNoShow(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case NOT_SCHEDULED -> rscsRepository.getNotScheduledSecondSubsequentAppt(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
			case WAIT_LISTED -> rscsRepository.getWaitlistClaimants(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.RscsStageCd.SECOND_SUBS_APT.getCode(),
					ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            case ALL -> rscsRepository.getSecondSubsequentAppt(staffUserIdList, rsicDAO.getRsicCalEventDt(),
					ReseaAlvEnumConstant.LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
            default -> new ArrayList<>();
        };
	}
}