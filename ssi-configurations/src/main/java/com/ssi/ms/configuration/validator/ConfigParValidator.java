package com.ssi.ms.configuration.validator;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.constant.ErrorMessageConstant.ParameterErrorDetail;
import com.ssi.ms.configuration.database.dao.ParameterParDAO;
import com.ssi.ms.configuration.dto.parameter.ConfigParSaveReqDTO;
import com.ssi.ms.configuration.util.ConfigErrorEnum;
import com.ssi.ms.configuration.util.ConfigUtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.PAR_DATE_FORMAT_VALIDATOR;
import static com.ssi.ms.configuration.validator.ParValidationPredicate.isDateTypeForParameter;
import static com.ssi.ms.configuration.validator.ParValidationPredicate.isNumericTypeForParameter;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.checkPastDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigParValidator extends ConfigValidator {

    public HashMap<String, List<String>> validateParSave(
            ConfigParSaveReqDTO parSaveDTO, ParameterParDAO parDAO, Date systemDate,
            boolean activeExists, boolean anotherOpenEndedExists) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        final Date modificationDt = localDateToDate.apply(parSaveDTO.getModificationDt());
        final Date effectiveUntilDt = localDateToDate.apply(parSaveDTO.getEffectiveUntilDt());
        final boolean numericType = isNumericTypeForParameter.test(parDAO.getParNumericValue());
        final boolean dateType = isDateTypeForParameter.test(parDAO.getParAlphaValText());
        final boolean textType = !dateType && StringUtils.isNotEmpty(parDAO.getParAlphaValText());
        validateParFields(parSaveDTO, errorEnums);
        if (ConfigurationConstants.PARMODIFICATIONTYPE.ENDDATE.name().equals(parSaveDTO.getModificationType())) {
            if (parDAO.getParExpirationDate() != null) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_ENDDATE_NOT_ALLOWED);
            }
            if (parSaveDTO.getModificationDt().compareTo(dateToLocalDate.apply(parDAO.getParEffectiveDate())) <= 0) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_ENDDATE_NOT_VALID);
            } else if (systemDate.compareTo(modificationDt) >= 0) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_ENDDT_INVALID);
            } else if (modificationDt.before(parDAO.getParEffectiveDate())) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_ENDDT_INVALID_EFFDT);
            }
        } else if (anotherOpenEndedExists && effectiveUntilDt == null) {
            errorEnums.add(ParameterErrorDetail.CONFIG_PAR_EFF_UNTIL_MANDATORY);
        }
        if (ConfigurationConstants.PARMODIFICATIONTYPE.REINSTATE.name().equals(parSaveDTO.getModificationType())) {
            if (activeExists) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_REINSTATE_ACTIVE_EXISTS);
            }
            if (checkPastDate.test(modificationDt, systemDate)) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_REINSTATE_INVALID);
            } else if (systemDate.compareTo(modificationDt) >= 0) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_REINSTATEDT_INVALID);
            } else if (modificationDt.before(parDAO.getParEffectiveDate())) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_REINSTATEDT_INVALID_EFFDT);
            }
        }
        if (ConfigurationConstants.PARMODIFICATIONTYPE.CHANGE.name().equals(parSaveDTO.getModificationType())) {
            if (effectiveUntilDt != null && !effectiveUntilDt.after(modificationDt)) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_EFF_UNTIL_NOT_VALID);
            }
            if (effectiveUntilDt == null && anotherOpenEndedExists) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_OPEN_EFF_UNTIL_NOT_ALLOWED);
            }
            if (parDAO.getParExpirationDate() != null && modificationDt
                    .after(parDAO.getParExpirationDate())) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_CHANGEDT_PAST_EXPIRED);
            } else if (systemDate.compareTo(modificationDt) >= 0) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_CHANGEDT_INVALID);
            } else if (modificationDt.before(parDAO.getParEffectiveDate())) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_CHANGEDT_INVALID_EFFDT);
            }
            /*if (parDAO.getParExpirationDate() != null && parDAO.getParExpirationDate()
                    .after(effectiveUntilDt)) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_EFFECTIVE_UNTIL_PRIOR_EXPIRED);
            }*/
            // Validate values are set and types are matching
            if (parameterChanged(parSaveDTO, parDAO, dateType, textType)) {
                if (numericType && parSaveDTO.getNumericValue() == null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_NUMERICVAL_IS_REQUIRED);
                } else if (!numericType && parSaveDTO.getNumericValue() != null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_NUMERICVAL_IS_NOT_ALLOWED);
                }
                if (textType && parSaveDTO.getTextValue() == null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_TEXTVAL_IS_REQUIRED);
                } else if (!textType && parSaveDTO.getTextValue() != null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_TEXTVAL_IS_NOT_ALLOWED);
                } else if (textType && parSaveDTO.getTextValue().matches(PAR_DATE_FORMAT_VALIDATOR)) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_TEXTVAL_DATE_INVALID);
                }
                if (dateType && parSaveDTO.getDateValue() == null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_DATEVAL_IS_REQUIRED);
                } else if (!dateType && parSaveDTO.getDateValue() != null) {
                    errorEnums.add(ParameterErrorDetail.CONFIG_PAR_DATEVAL_IS_NOT_ALLOWED);
                }
            } else {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_CHANGE_NOT_ALLOWED);
            }

            // Type Matching End
        }

        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }

    private boolean parameterChanged(ConfigParSaveReqDTO parSaveDTO, ParameterParDAO parDAO, boolean dateType, boolean textType) {
        return !parDAO.getParName().equals(parSaveDTO.getName())
               || !Objects.equals(parSaveDTO.getNumericValue(), parDAO.getParNumericValue())
               || !(dateType && StringUtils.trimToEmpty(parSaveDTO.getDateValue().format(DateTimeFormatter.ofPattern("MM/DD/YYYY")))
                        .equals(StringUtils.trimToEmpty(parDAO.getParAlphaValText())))
               || !(textType && StringUtils.trimToEmpty(parSaveDTO.getTextValue()).equals(StringUtils.trimToEmpty(parDAO.getParAlphaValText())))
               || !parDAO.getParExpirationDate().equals(localDateToDate.apply(parSaveDTO.getEffectiveUntilDt()))
               || !parDAO.getParEffectiveDate().equals(localDateToDate.apply(parSaveDTO.getModificationDt()));
    }

    public HashMap<String, List<String>> validateParDelete(ParameterParDAO parDAO, Date systemDate) {
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        final List<ConfigErrorEnum> errorEnums = new ArrayList<>();
        if (!checkFutureDate.test(parDAO.getParEffectiveDate(), systemDate)) {
            errorEnums.add(ParameterErrorDetail.CONFIG_PAR_DELETE_NOT_ALLOWED);
        }
        ConfigUtilFunction.updateErrorMap(errorMap, errorEnums);
        return errorMap;
    }

    public void validateParFields(ConfigParSaveReqDTO parSaveDTO,
                                  List<ConfigErrorEnum> errorEnums) {
        final int decimalIndex = 15;
        final int decimalLength = 4;
        if (StringUtils.isNotBlank(parSaveDTO.getName()) && parSaveDTO.getName().length() > ConfigurationConstants.MAX_PARAM_NAME_LENGTH) {
            errorEnums.add(ParameterErrorDetail.CONFIG_PAR_PARNAME_INVALID);
        }
        if (StringUtils.isNotBlank(parSaveDTO.getTextValue())
        		&& parSaveDTO.getTextValue().length() > ConfigurationConstants.MAX_PARAM_TXT_VALUE_LENGTH) {
            errorEnums.add(ParameterErrorDetail.CONFIG_PAR_TEXTVAL_INVALID);
        }
        if (parSaveDTO.getNumericValue() != null) {
            final String numVal = String.valueOf(parSaveDTO.getNumericValue());
            final int index = numVal.indexOf(".");
            if (index != 0 && (index > decimalIndex || numVal.length() - index > decimalLength)) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_NUMERICVAL_INVALID);
            } else if (numVal.length() > decimalIndex) {
                errorEnums.add(ParameterErrorDetail.CONFIG_PAR_NUMERICVAL_INVALID);
            }
        }
    }
}

