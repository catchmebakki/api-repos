package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.database.dao.WsCpsConfigWsccDAO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccSaveReqDTO;
import com.ssi.ms.configuration.util.ConfigErrorEnum;
import com.ssi.ms.configuration.util.ConfigUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WorkSearchReqErrorDetail;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigWsccValidator extends ConfigValidator {
    public HashMap<String, List<String>> validateWsccSave(
            ConfigWsccSaveReqDTO wsccReqDTO, WsCpsConfigWsccDAO wsccDAO, Date systemDate) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        validateWsccFields(wsccReqDTO, errorEnums);
        if (wsccDAO.getWsccExpirationDt() != null && systemDate.after(wsccDAO.getWsccExpirationDt())) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_INACTIVE);
        }
        if (!localDateToDate.apply(wsccReqDTO.getModificationDate()).after(systemDate)) {
            if (ConfigurationConstants.WSCCMODIFICATIONTYPE.CONFIGURATION.name().equals(wsccReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_CONFIGURATIONDATE_INVALID);
            } else {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_STARTDATE_INVALID);
            }
        }
        if (wsccDAO.getWsccExpirationDt() != null
                && localDateToDate.apply(wsccReqDTO.getModificationDate()).after(wsccDAO.getWsccExpirationDt())) {
            if (ConfigurationConstants.WSCCMODIFICATIONTYPE.CONFIGURATION.name().equals(wsccReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_CONFIGURATIONDATE_EXPDT_INVALID);
            } else {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_STARTDATE_EXPDT_INVALID);
            }
        }
        if (!localDateToDate.apply(wsccReqDTO.getModificationDate()).after(wsccDAO.getWsccEffectiveDt())) {
            if (ConfigurationConstants.WSCCMODIFICATIONTYPE.CONFIGURATION.name().equals(wsccReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_CONFIGURATIONDATE_INVALID_EFFDT);
            } else {
                errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_STARTDATE_INVALID_EFFDT);
            }
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }

    private void validateWsccFields(ConfigWsccSaveReqDTO wsccReqDTO, List<ConfigErrorEnum> errorEnums) {
        final int length = 2;
        if (String.valueOf(wsccReqDTO.getInitialClaim()).length() > length) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_INIT_CLAIM_INVALID);
        }
        if (String.valueOf(wsccReqDTO.getAdditionalClaim()).length() > length) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_ADDL_CLAIM_INVALID);
        }
        if (String.valueOf(wsccReqDTO.getIncrementFrequency()).length() > length) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_INCREMENT_FREQUENCY_INVALID);
        }
        if (String.valueOf(wsccReqDTO.getIncrementVal()).length() > length) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_INCREMENT_VALUE_INVALID);
        }
    }

    public HashMap<String, List<String>> validateWsccDelete(WsCpsConfigWsccDAO wsccDAO, Date systemDate) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        if (!checkFutureDate.test(wsccDAO.getWsccEffectiveDt(), systemDate)) {
            errorEnums.add(WorkSearchReqErrorDetail.CONFIG_WSCC_DELETE_NOT_ALLOWED);
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }
}

