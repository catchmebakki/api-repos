package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.database.dao.WsAutoWvrConfigWswcDAO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcSaveReqDTO;
import com.ssi.ms.configuration.util.ConfigErrorEnum;
import com.ssi.ms.configuration.util.ConfigUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WorkSearchWaiverErrorDetail;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigWswcValidator extends ConfigValidator {
    public HashMap<String, List<String>> validateWswcSave(
            ConfigWswcSaveReqDTO wswcReqDTO, WsAutoWvrConfigWswcDAO wswcDAO, Date systemDate) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        if (!localDateToDate.apply(wswcReqDTO.getModificationDate()).after(systemDate)) {
            if (ConfigurationConstants.WSWCMODIFICATIONTYPE.CONFIGURATION.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_CONFIGURATIONDATE_INVALID);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.ENDDATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_ENDDATE_INVALID);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.STARTDATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_STARTDATE_INVALID);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.DEACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_DEACTIVATEDATE_INVALID);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.REACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_REACTIVATEDATE_INVALID);
            }
        }
        if (wswcDAO.getWswcExpirationDt() != null
                && localDateToDate.apply(wswcReqDTO.getModificationDate()).after(wswcDAO.getWswcExpirationDt())) {
            if (ConfigurationConstants.WSWCMODIFICATIONTYPE.CONFIGURATION.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_CONFIGURATIONDATE_EXPDT_INVALID);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.STARTDATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_STARTDATE_EXPDT_INVALID);
            }
        }
        if (!localDateToDate.apply(wswcReqDTO.getModificationDate()).after(wswcDAO.getWswcEffectiveDt())) {
            if (ConfigurationConstants.WSWCMODIFICATIONTYPE.CONFIGURATION.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_CONFIGURATIONDATE_INVALID_EFFDT);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.ENDDATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_ENDDATE_INVALID_EFFDT);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.STARTDATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_STARTDATE_INVALID_EFFDT);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.DEACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_DEACTIVATE_INVALID_EFFDT);
            } else if (ConfigurationConstants.WSWCMODIFICATIONTYPE.REACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_REACTIVATE_INVALID_EFFDT);
            }
        }
        if (ConfigurationConstants.WSWCMODIFICATIONTYPE.REACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
            if (wswcDAO.getWswcExpirationDt() == null) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_REACTIVATE_ACTIVE);
            } else if (!localDateToDate.apply(wswcReqDTO.getModificationDate()).after(wswcDAO.getWswcExpirationDt())) {
                errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_REACTIVATE_INACTIVE);
            }
        } else if (wswcDAO.getWswcExpirationDt() != null && systemDate.after(wswcDAO.getWswcExpirationDt())) {
            errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_INACTIVE);
        }

        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }

    public HashMap<String, List<String>> validateWswcDelete(WsAutoWvrConfigWswcDAO wswcDAO, Date systemDate) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        if (!checkFutureDate.test(wswcDAO.getWswcEffectiveDt(), systemDate)) {
            errorEnums.add(WorkSearchWaiverErrorDetail.CONFIG_WSWC_DELETE_NOT_ALLOWED);
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }
}

