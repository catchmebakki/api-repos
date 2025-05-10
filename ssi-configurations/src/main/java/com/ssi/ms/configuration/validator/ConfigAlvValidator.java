package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.constant.ErrorMessageConstant.WorkSearchAlvErrorDetail;
import com.ssi.ms.configuration.database.dao.AllowValAlvDAO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvSaveReqDTO;
import com.ssi.ms.configuration.util.ConfigErrorEnum;
import com.ssi.ms.configuration.util.ConfigUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALVMODIFICATIONTYPE.CHANGE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALVMODIFICATIONTYPE.REACTIVATE;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigAlvValidator extends ConfigValidator {
    public HashMap<String, List<String>> validateAlvSave(
            ConfigAlvSaveReqDTO alvReqDTO, AllowValAlvDAO alvUpdateDAO, String generatedComments) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        validateGeneratedComments(generatedComments, errorMap);
        if (REACTIVATE.name().equals(alvReqDTO.getModificationType())) {
            if (ConfigurationConstants.ACTIVE.Y.name().equals(alvUpdateDAO.getAlvActiveInd())) {
                errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_REINSTATE_ACTIVE);
            }
        } else {
            if (ConfigurationConstants.ACTIVE.N.name().equals(alvUpdateDAO.getAlvActiveInd())) {
                errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_INACTIVE);
            }
        }
        if (CHANGE.name().equals(alvReqDTO.getModificationType())) {
            validateParFields(alvReqDTO, errorEnums);
            if (StringUtils.isNotEmpty(alvUpdateDAO.getAllowCatAlcDAO().getAlcDecipherLabel())
                    && StringUtils.isEmpty(alvReqDTO.getAlvDecipherCd())) {
                errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_TYPE_MANDATORY);
            } else if (StringUtils.isEmpty(alvUpdateDAO.getAllowCatAlcDAO().getAlcDecipherLabel())
                    && StringUtils.isNotEmpty(alvReqDTO.getAlvDecipherCd())) {
                errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_DECIPHER_INVALID);
            }
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }

    public void validateParFields(ConfigAlvSaveReqDTO alvReqDTO,
                                  List<ConfigErrorEnum> errorEnums) {
        if (StringUtils.isNotBlank(alvReqDTO.getName()) && alvReqDTO.getName().length() > ConfigurationConstants.MAX_ALV_NAME_LENGTH) {
            errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_NAME_INVALID);
        }
        if (StringUtils.isNotBlank(alvReqDTO.getSpanishName())
        		&& alvReqDTO.getSpanishName().length() > ConfigurationConstants.MAX_ALV_SPANISH_NAME_LENGTH) {
            errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_SPANISH_NAME_INVALID);
        }
        if (StringUtils.isNotBlank(alvReqDTO.getDescription())
        		&& alvReqDTO.getDescription().length() > ConfigurationConstants.MAX_ALV_DESCRIPTION_LENGTH) {
            errorEnums.add(WorkSearchAlvErrorDetail.CONFIG_ALV_DESCRIPTION_INVALID);
        }
    }
}

