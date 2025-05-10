package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.repository.WeeklyWorkSearchWwsRepository;
import com.ssi.ms.resea.dto.AvaliableApptReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.ROL_LOCAL_OFFICE_MANAGER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_SUPERUSER;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV;


@Component
@AllArgsConstructor
@Slf4j
public class ReseaClaimantValidator {

	@Autowired
	WeeklyWorkSearchWwsRepository wwsRepository;

    public HashMap<String, List<String>> validateAvailableAppointment(
			AvaliableApptReqDTO apptReqDTO, ReseaIntvwerCalRsicDAO rsicDAO, String userId, String roleId, Date systemDate) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		if (rsicDAO.getRsicCalEventTypeCdAlv().getAlvId() != RSIC_CAL_EVENT_TYPE_AVAILABLE_ALV) {
			errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_NOT_AVAILABLE);
		}

		int rolId = Integer.parseInt(roleId);
		if (!ROL_LOCAL_OFFICE_MANAGER.equals(rolId) && !ROL_RESEA_PROG_STAFF.equals(rolId)) {
			if (apptReqDTO.getUserId() == -1L) {
				errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_FOR_LOF_INVALID);
			} else if (Long.parseLong(userId) != apptReqDTO.getUserId()) {
		  		errorEnums.add(ErrorMessageConstant.AvailableApptErrorDetail.APPOINTMENT_FOR_MGR_INVALID);
			}
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
    }

}
