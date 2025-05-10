package com.ssi.ms.resea.validator;

import com.ssi.ms.resea.constant.ErrorMessageConstant.UploadFieldErrorDetail;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.dao.ReseaUploadCmSchRucsDAO;
import com.ssi.ms.resea.database.dao.ReseaUploadSchSummaryRusmDAO;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.dto.upload.ReseaUploadFormReqDTO;
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
import java.util.Objects;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_PROG_STAFF;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_IN_USE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_MTG_STATUS_SCHEDULED;

@Component
@AllArgsConstructor
@Slf4j
public class ReseaUploadValidator {
	@Autowired
	ReseaIntvwerCalRsicRepository rsicRepository;
	public HashMap<String, List<String>> validateUploadForm(ReseaUploadFormReqDTO uploadReqDTO,
															boolean initiatedExists,
															Date systemDate, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (!ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_ACCESS);
		}
		if (!checkFutureDate.test(uploadReqDTO.getEffectiveDt(), systemDate)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_EFFECTIVE_DT_INVALID);
		}
		if (initiatedExists) {
			errorEnums.add(UploadFieldErrorDetail.INITIALIZE_DUPLICATE);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

	public HashMap<String, List<String>> validateDiscardForm(ReseaUploadCmSchRucsDAO rucsDAO, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (!ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_DISCARD_ACCESS);
		}
		if (ReseaAlvEnumConstant.RucsStatusCd.FINAL.getCode().equals(rucsDAO.getRucsStatusCdAlv().getAlvId())) {
			errorEnums.add(UploadFieldErrorDetail.DISCARD_FINALIZED);
		} else if (ReseaAlvEnumConstant.RucsStatusCd.DISCARDED.getCode().equals(rucsDAO.getRucsStatusCdAlv().getAlvId())) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_DISCARD_INVALID);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}

    public HashMap<String, List<String>> validateFinalizeSchedule(ReseaUploadCmSchRucsDAO rucsDAO, ReseaUploadSchSummaryRusmDAO rusmDAO,
																  Date systimestamp, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (!checkFutureDate.test(rucsDAO.getRucsEffectiveDt(), systimestamp)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_EFFECTIVE_DT_INVALID);
		}
		if (!ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_ACCESS);
		}
		if (rsicRepository.checkCaseManagerScheduledAppointmentsExists(
				rucsDAO.getStfDAO().getUserDAO().getUserId(), rucsDAO.getLofDAO().getLofId(),
				rucsDAO.getRucsEffectiveDt(), rucsDAO.getRucsExpirationDt(),
				RSIC_CAL_EVENT_TYPE_IN_USE_ALV, RSIC_MTG_STATUS_SCHEDULED) > 0) {
			errorEnums.add(UploadFieldErrorDetail.SCHEDULE_EVENT_EXISTS);
		}
		if (ReseaAlvEnumConstant.RucsStatusCd.INITIATED.getCode().equals(rucsDAO.getRucsStatusCdAlv().getAlvId())) {
			errorEnums.add(UploadFieldErrorDetail.FINALIZE_STATUS_INVALID);
		}
		if (!Objects.equals(ReseaAlvEnumConstant.RusmStatusCd.COMPLETE.getCode(), rusmDAO.getRusmStatusCdAlv().getAlvId())
				|| rusmDAO.getRusmNumErrs() > 0) {
			errorEnums.add(UploadFieldErrorDetail.FINALIZE_STATUS_INCOMPLETE);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
    }

	public HashMap<String, List<String>> validateFileId(ReseaUploadCmSchRucsDAO rucsDAO, Date systimestamp, String roleId) {
		final HashMap<String, List<String>> errorMap = new HashMap<>();
		final List<ReseaErrorEnum> errorEnums = new ArrayList<>();
		final int rolId = Integer.parseInt(roleId);
		if (!checkFutureDate.test(rucsDAO.getRucsEffectiveDt(), systimestamp)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_EFFECTIVE_DT_INVALID);
		}
		if (!ROL_RESEA_PROG_STAFF.equals(rolId)) {
			errorEnums.add(UploadFieldErrorDetail.RESEA_UPLOAD_ACCESS);
		}
		ReseaUtilFunction.updateErrorMap(errorMap, errorEnums);
		return errorMap;
	}
}
