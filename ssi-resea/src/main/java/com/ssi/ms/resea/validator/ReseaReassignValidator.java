package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.dto.ReseaCaseReassignReqDTO;
import com.ssi.ms.resea.dto.ReseaReassignAllReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CaseReassign;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_SCHEDULED;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_USAGE_TO_RSCS_STAGE_MAP;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaReassignValidator {


	public HashMap<String, List<String>> validateCaseReassign(ReseaCaseRscsDAO rscsDAO,
															  ReseaIntvwerCalRsicDAO rsicDAO,
															  String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (rsicDAO.getRsicCalEventTypeCdAlv().getAlvId() != RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV) {
			errorEnums.add(CaseReassign.APPOINTMENT_NOT_AVAILABLE);
		}
		if (Objects.equals(rscsDAO.getRscsStatusCdALV().getAlvId(), RSCS_STATUS_SCHEDULED) &&
				!Objects.equals(RSIC_USAGE_TO_RSCS_STAGE_MAP.get(rsicDAO.getRsicTimeslotUsageCdAlv().getAlvId()),
				rscsDAO.getRscsStageCdALV().getAlvId())) {
			errorEnums.add(CaseReassign.APPOINTMENT_USAGE_CASE_STAGE_MISMATCH);
		}
		int rolId = Integer.parseInt(roleId);
		if (!ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(CaseReassign.REASSIGN_ACCESS_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateReassignAll(ReseaReassignAllReqDTO reassignDTO,
															 Date systemDate,
															 String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		int rolId = Integer.parseInt(roleId);
		if (!checkFutureDate.test(reassignDTO.getReassignDt(), systemDate)) {
			errorEnums.add(CaseReassign.REASSIGN_DATE_INVALID);
		}
		if (reassignDTO.getReassignEndDt() != null && reassignDTO.getReassignEndDt().before(reassignDTO.getReassignDt())) {
			errorEnums.add(CaseReassign.REASSIGN_END_DT_INVALID);
		}
		if (!ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(CaseReassign.REASSIGN_ACCESS_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
}
